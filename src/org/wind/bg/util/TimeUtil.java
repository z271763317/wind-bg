package org.wind.bg.util;

import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @描述 : 定时任务工具类
 * @作者 : 胡璐璐
 * @时间 : 2020年6月29日 11:37:11
 */
public final class TimeUtil {

	private static final ScheduledExecutorService executorService = Executors.newScheduledThreadPool(50); 	//定时线程池（定长，支持超时返回） 

	//
	public static void addTimeTask(Class<? extends TimerTask> objClass,long delay, long period) throws Exception{
		addTimeTask(objClass.newInstance(), delay, period);
	}
	/**
	 * 添加 : 定时执行的任务（需等到上次任务结束后，在按规则等待间隔时间执行）
	 * @param objClass : 继承TimerTask类的对象
	 * @param delay : 初次加载时等待delay毫秒执行
	 * @param period : 每次执行后的间隔period毫秒执行
	 */
	public static void addTimeTask(final TimerTask taskObj,long delay, long period){
		executorService.scheduleWithFixedDelay(new Thread(){
			public void run(){
				try{
					taskObj.run();		//若异常，则继续执行
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		}, delay, period, TimeUnit.MILLISECONDS);
	}
	
}