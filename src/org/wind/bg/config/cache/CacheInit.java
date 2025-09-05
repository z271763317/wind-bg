package org.wind.bg.config.cache;

import java.lang.reflect.Method;
import java.util.List;

import org.wind.bg.annotation.An_Time;
import org.wind.bg.model.User;
import org.wind.bg.service.time.TimeService_model;
import org.wind.bg.util.FileOS;
import org.wind.bg.util.TimeUtil;

/**
 * @描述 : 缓存父类——初始化方法
 * @版权 : 胡璐璐
 * @时间 : 2020年9月1日 12:31:20
 */
@SuppressWarnings("unchecked")
public abstract class CacheInit extends CacheParent{
	
	//初始化（通用）
	public void initConfig() throws Exception{
		List<Class<?>> classList=FileOS.getClassList(User.class);		//获取指定Class下所有的Class
		//classList.add(MainUserActionParent.class);
		for(Class<?> t_class:classList){
			try{
				An_Time t_an_time=t_class.getAnnotation(An_Time.class);
				if(t_an_time!=null){
					long delay=t_an_time.delay();
					long period=t_an_time.period();
					//
					TimeService_model t_time_obj=new TimeService_model((Class<? extends An_Time>) t_class);
					TimeUtil.addTimeTask(t_time_obj, delay, period);
				}else{
					//
					Method t_m=t_class.getMethod("init");
					if(t_m!=null){
						t_m.invoke(null);
					}
				}
			}catch(NoSuchMethodException e){
				//跳过该异常
			}
		}
	}
}