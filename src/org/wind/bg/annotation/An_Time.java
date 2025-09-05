package org.wind.bg.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @描述 : 定时注解——为指定的类，定时执行指定的一些方法，只支持静态方法
 * @说明 : 默认执行【init】方法，只针对加入该注解的该类
 * @作者 : 胡璐璐
 * @时间 : 2019年11月27日 14:45:44
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface An_Time {

	String name() default "";		//描述
	String[] method() default "init";		//要执行的改Class的一些方法的名称（默认：init），数组式，按顺序执行多个
	long delay() default 0;		//首次执行要延迟多久执行（单位：毫秒，默认：0，0代表立即执行）
	long period();		//每次间隔执行延迟的时间（单位：毫秒）
	
}