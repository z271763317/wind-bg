package org.wind.bg.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @描述 : 更新注解——代表该方法是更新方法
 * @说明 : 会在request里加上——isUpdate=true；moduleName=更新（若有则不做替换）
 * @作者 : 胡璐璐
 * @时间 : 2021年4月13日 11:41:34
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface An_Update {

	String value() default "更新";		//描述
	
}