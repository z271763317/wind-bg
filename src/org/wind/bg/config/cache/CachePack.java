package org.wind.bg.config.cache;

import java.io.Serializable;

/**
 * @描述 : 缓存包装类 —— 封装了所有加入到Cache的对象
 * @版权 : 胡璐璐
 * @时间 : 2016年12月2日 09:52:56
 */
public class CachePack implements Serializable{

	private static final long serialVersionUID = 5353366498168271183L;
	
	private String key;			//缓存key
	private Serializable cacheObject;		//实现了Serializable接口的对象
	private Boolean isSave;		//是否保存
	
	public CachePack(){
		
	}
	public CachePack(String key,Serializable cacheObject,Boolean isSave){
		this.key=key;
		this.cacheObject=cacheObject;
		this.isSave=isSave;
	}
	public String getKey() {
		return key;
	}
	public Serializable getCacheObject() {
		return cacheObject;
	}
	public Boolean getIsSave() {
		return isSave;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public void setCacheObject(Serializable cacheObject) {
		this.cacheObject = cacheObject;
	}
	public void setIsSave(Boolean isSave) {
		this.isSave = isSave;
	}
	
}