package org.wind.bg.config.cache;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.List;

import org.wind.bg.util.FileOS;

/**
 * @描述 : Cache缓存类——数据字典类：CacheParent；初始化方法类：CacheInit
 * @详情 : 存放所有初始化、缓存在内存里用来全局调用的数据（含字典）
 * @版权 : 胡璐璐
 * @时间 : 2015年10月24日 11:49:41
 */
@SuppressWarnings("unchecked")
public class Cache extends CacheInit{
	
	private static Cache cacheObj;	//缓存单例
	
	/**初始化**/
	private Cache(){
		//单例，不允许其他实例化
	}
	public synchronized static Cache getInstance(){
		if(cacheObj==null){
			synchronized(Cache.class){
				if(cacheObj==null){
					cacheObj=new Cache();
				}
			}
		}
		return cacheObj;
	}
	/**初始化**/
	public void init(){
//		CacheService.getInstance().loadCache();	//加载所有硬盘缓存
		List<Method> list=FileOS.getMethodList(CacheInit.class, "init");	//指定带有init名的方法
		for(Method m:list){
			int paramLength=m.getParameterTypes().length;
			//只取没有参数的
			if(paramLength==0){
				try {
					m.invoke(this);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	/**获取 : 对应缓存数据**/
	public static <T extends Serializable> T get(String key){
		CachePack t_cacheObj=map.get(key);
		if(t_cacheObj!=null){
			return (T) t_cacheObj.getCacheObject();
		}else{
			return null;
		}
	}
	/**设置 : 对应缓存数据**/
	public synchronized static void set(String key,Serializable value,boolean isSave){
		CachePack t_cacheObj=map.get(key);
		if(t_cacheObj==null){
			t_cacheObj=new CachePack(key, value, isSave);
		}else{
			t_cacheObj.setCacheObject(value);
		}
		map.put(key, t_cacheObj);
	}
}