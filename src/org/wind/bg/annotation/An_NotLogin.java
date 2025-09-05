package org.wind.bg.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @描述 : 不用登录注解
 * @作者 : 胡璐璐
 * @时间 : 2021年8月14日 17:23:38
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface An_NotLogin {

}