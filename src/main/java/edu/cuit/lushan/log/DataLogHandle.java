package edu.cuit.lushan.log;

import cn.hutool.core.collection.CollUtil;
import edu.cuit.lushan.annotation.DataLog;
import edu.cuit.lushan.handler.BaseDataLog;
import edu.cuit.lushan.handler.LogData;
import org.springframework.stereotype.Component;

/**
 * <p>
 * 数据日志处理
 * </p>
 *
 * @author Tophua
 * @since 2020/8/11
 */
@Component
public class DataLogHandle extends BaseDataLog {

    @Override
    public void setting() {
        // 设置排除某张表、某些字段
        this.addExcludeTableName("sys_log");
        this.addExcludeFieldName("modifyTime");
    }

    @Override
    public boolean isIgnore(DataLog dataLog) {
        // 根据注解判断是否忽略某次操作
        return false;
    }

    @Override
    public void change(DataLog dataLog, LogData data) {
        if (CollUtil.isEmpty(data.getDataChanges())) {
            return;
        }
    }

}
