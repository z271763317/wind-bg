package org.wind.bg.service.time;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.TimerTask;

import org.wind.bg.annotation.An_Time;
import org.wind.bg.model.User;
import org.wind.orm.Table;

/**
 * @描述 : 定时任务——【Model通用】执行objClass的An_Time注解的方法列表，只支持静态方法
 * @作者 : 胡璐璐
 * @时间 : 2019年11月27日 14:52:11
 */
public class TimeService_model extends TimerTask{

	private List<Method> methodList;	//要执行的方法列表（按顺序执行）
	
	public TimeService_model(Class<? extends An_Time> t_class) throws Exception{
		if(t_class==null){
			throw new IllegalArgumentException("[【"+TimeService_model.class.getSimpleName()+"】构造方法的参数【objClass】不能为空");
		}
		An_Time t_an_time=t_class.getAnnotation(An_Time.class);
		String methodArr[]=t_an_time.method();		//要执行的方法列表（按顺序执行）
		methodList=new ArrayList<Method>();
		for(String t_methodName:methodArr){
			try{
				Method t_method=t_class.getMethod(t_methodName);
				if(t_method==null){
					throw new IllegalArgumentException("[【"+t_class.getSimpleName()+"】类不存在【"+t_methodName+"】方法2");
				}
				methodList.add(t_method);
			}catch(NoSuchMethodException e){
				throw new IllegalArgumentException("[【"+t_class.getSimpleName()+"】类不存在【"+t_methodName+"】方法");
			}
		}
		Table.close(User.class);
	}
	
	/**执行**/
	public void run() {
		for(Method m:methodList){
			try{
				m.invoke(null);
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}

}
