package edu.cuit.lushan.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>
 * 数据日志注解
 * </p>
 *
 * @author Tophua
 * @since 2020/7/27
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DataLog {
    /**
     * sPel表达式1
     */
    String sPel1() default "";

    /**
     * sPel表达式2
     */
    String sPel2() default "";

    /**
     * sPel表达式3
     */
    String sPel3() default "";

    /**
     * <p>
     * 类型
     * </p>
     *
     * @return int
     * @author Tophua
     * @since 2020/8/11
     */
    int type() default -1;

    /**
     * <p>
     * 标签
     * </p>
     *
     * @return java.lang.String
     * @author Tophua
     * @since 2020/8/12
     */
    String tag() default "";

    /**
     * <p>
     * 注释
     * </p>
     *
     * @return java.lang.String
     * @author Tophua
     * @since 2020/8/11
     */
    String note() default "";
}
