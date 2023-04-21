package com.alexon.operation.log;


import java.lang.annotation.*;

@Target({ElementType.PARAMETER, ElementType.METHOD})//作用在参数和方法上
@Retention(RetentionPolicy.RUNTIME)//运行时注解
@Documented//表明这个注解应该被 javadoc工具记录
public @interface OperationLog {
    String operateModule() default "";
    String operateType() default "";
    String operateDesc() default "";
}
