package edu.cuit.lushan.annotation;


import edu.cuit.lushan.enums.WebLogTypeEnum;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface WebLog {
    WebLogTypeEnum value() default WebLogTypeEnum.INFO;

    boolean hasToken() default true;
}
