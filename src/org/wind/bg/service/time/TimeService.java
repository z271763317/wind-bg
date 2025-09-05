package org.wind.bg.service.time;

import java.util.TimerTask;

/**
 * @描述 : 定时任务Service父抽象类
 * @版权 : 胡璐璐
 * @时间 : 2020年6月16日 22:37:18
 */
public abstract class TimeService extends TimerTask{

	/*默认——数据查询分页相关数据*/
	protected final int pageNew=1;
	protected final int pageLimit=100;
	
}