package edu.cuit.lushan.handler;

import cn.hutool.core.date.LocalDateTimeUtil;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;

@Component
public class EntityMetaObjectHandler implements MetaObjectHandler {
    @Override
    public void insertFill(MetaObject metaObject) {
        this.setFieldValByName("modifyTime", LocalDateTimeUtil.of(new Date()), metaObject);
        this.setFieldValByName("accountStatus", 0, metaObject);
        this.setFieldValByName("roleId", 0, metaObject);
        this.setFieldValByName("isDelete", 0, metaObject);

    }

    @Override
    public void updateFill(MetaObject metaObject) {
        this.setFieldValByName("modifyTime", LocalDateTimeUtil.of(new Date()), metaObject);
    }

}
