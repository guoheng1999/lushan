package edu.cuit.lushan.aop;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import edu.cuit.lushan.annotation.DataLog;
import edu.cuit.lushan.entity.SysLogDetail;
import edu.cuit.lushan.handler.BaseDataLog;
import edu.cuit.lushan.handler.DataChange;
import edu.cuit.lushan.service.ISysLogDetailService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSession;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.mybatis.spring.SqlSessionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * DataLog切面
 * </p>
 *
 * @author Tophua
 * @since 2020/7/15
 */
@Aspect
@Order()
@Component
@AllArgsConstructor
@Slf4j
public class DataLogAspect {

    private final BaseDataLog baseDataLog;
    @Autowired
    ISysLogDetailService sysLogDetailService;

    /**
     * <p>
     * 初始化
     * </p>
     *
     * @return void
     * @author Tophua
     * @since 2020/10/30
     */
    @PostConstruct
    public void init() {
        baseDataLog.setting();
    }

    /**
     * <p>
     * 切面前执行
     * </p>
     *
     * @param dataLog dataLog
     * @return void
     * @author Tophua
     * @since 2020/7/15
     */
    @Before("@annotation(dataLog)")
    public void before(JoinPoint joinPoint, DataLog dataLog) {
        // 使用 ThreadLocal 记录一次操作
        BaseDataLog.DATA_CHANGES.set(new LinkedList<>());
        BaseDataLog.JOIN_POINT.set(joinPoint);
        BaseDataLog.DATA_LOG.set(dataLog);
        if (baseDataLog.isIgnore(dataLog)) {
            BaseDataLog.DATA_CHANGES.set(null);
        }
    }

    /**
     * <p>
     * 切面后执行
     * </p>
     *
     * @param dataLog dataLog
     * @return void
     * @author Tophua
     * @since 2020/7/15
     */
    @AfterReturning("@annotation(dataLog)")
    public void after(DataLog dataLog) {

        List<DataChange> list = BaseDataLog.DATA_CHANGES.get();

        if (CollUtil.isEmpty(list)) {
            return;
        }
        list.forEach(change -> {
            List<?> oldData = change.getOldData();
            if (CollUtil.isEmpty(oldData)) {
                return;
            }
            List<Long> ids = oldData.stream()
                    .map(o -> ReflectUtil.invoke(o, "getId").toString())
                    .filter(ObjectUtil::isNotNull)
                    .map(Long::parseLong)
                    .collect(Collectors.toList());
            SqlSession sqlSession = change.getSqlSessionFactory().openSession();
            try {
                Map<String, Object> map = new HashMap<>(1);
                map.put(Constants.WRAPPER, Wrappers.query().in("id", ids));
                List<?> newData = sqlSession.selectList(change.getSqlStatement(), map);
                change.setNewData(Optional.ofNullable(newData).orElse(new ArrayList<>()));
            } finally {
                SqlSessionUtils.closeSqlSession(sqlSession, change.getSqlSessionFactory());
            }

        });
        // 对比调模块
        this.compareAndTransfer(list);
    }

    /**
     * <p>
     * 对比保存
     * </p>
     *
     * @param list list
     * @return void
     * @author Tophua
     * @since 2020/7/15
     */
    public void compareAndTransfer(List<DataChange> list) {
        StringBuilder sb = new StringBuilder();
        StringBuilder rsb = new StringBuilder();
        list.forEach(change -> {
            List<?> oldData = change.getOldData();
            List<?> newData = change.getNewData();
            String tableName = change.getTableName();

            SysLogDetail sysLogDetail = SysLogDetail.builder()
                    .newValue(JSONUtil.toJsonStr(newData))
                    .oldValue(JSONUtil.toJsonStr(newData))
                    .tableName(tableName)
                    .build();
            sysLogDetailService.save(sysLogDetail);
            // 更新前后数据量不对必定是删除（逻辑删除）不做处理
            if (newData == null) {
                return;
            }
            if (oldData == null) {
                return;
            }
            if (oldData.size() != newData.size()) {
                return;
            }
            // 按id排序
            oldData.sort(Comparator.comparingLong(d -> Long.parseLong(ReflectUtil.invoke(d, "getId").toString())));
            newData.sort(Comparator.comparingLong(d -> Long.parseLong(ReflectUtil.invoke(d, "getId").toString())));

            for (int i = 0; i < oldData.size(); i++) {
                SysLogDetail.SysLogDetailBuilder sysLogDetailBuilder = SysLogDetail.builder();
                final int[] finalI = {0};
                baseDataLog.sameClazzDiff(oldData.get(i), newData.get(i)).forEach(r -> {
                    String oldV = r.getOldValue() == null ? "无" : r.getOldValue().toString();
                    String newV = r.getNewValue() == null ? "无" : r.getNewValue().toString();
                    if (ObjectUtil.equal(oldV.trim(), newV.trim())) {
                        return;
                    }
                    if (finalI[0] == 0) {
                        sb.append(StrUtil.LF);
                        sb.append(StrUtil.format("修改表：【{}】", tableName));
                        sb.append(StrUtil.format("id：【{}】", r.getId()));
                    }
                    sb.append(StrUtil.LF);
                    rsb.append(StrUtil.LF);
                    sb.append(StrUtil.format("把字段[{}]从[{}]改为[{}]",
                            r.getFieldName(), r.getOldValue(), r.getNewValue()));
                    rsb.append(StrUtil.indexedFormat(baseDataLog.getLogFormat(),
                            r.getId(), r.getFieldName(), r.getFieldComment(),
                            oldV, newV));
                    finalI[0]++;
                });
            }
        });
        if (sb.length() > 0) {
            sb.deleteCharAt(0);
            rsb.deleteCharAt(0);
        }

        BaseDataLog.DATA_CHANGES.set(list);
        BaseDataLog.LOG_STR.set(rsb.toString());
        baseDataLog.transfer();
    }

}
