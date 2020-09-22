package com.github.codingdebugallday.mvcframework.annotations;

import java.lang.annotation.*;

/**
 * <p>
 * 自定义功能，跟自定义springmvc框架无关
 * 定义注解@Security（有value属性，接收String数组）
 * 该注解用于添加在Controller类或者Handler方法上，表明哪些用户拥有访问该Handler方法的权限（注解配置用户名）
 * </p>
 *
 * @author isaac 2020/09/22 9:23
 * @since 1.0.0
 */
@Documented
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Security {

    String[] value() default {};
}
