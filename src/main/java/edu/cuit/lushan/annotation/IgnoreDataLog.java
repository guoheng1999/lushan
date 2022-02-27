package edu.cuit.lushan.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>
 * 忽略日志记录注解
 * 使用该注解的表或字段将不进行修改记录
 * </p>
 *
 * @author Tophua
 * @since 2021/1/1
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.FIELD})
public @interface IgnoreDataLog {
}
