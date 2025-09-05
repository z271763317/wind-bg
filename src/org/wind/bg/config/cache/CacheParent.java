package org.wind.bg.config.cache;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @描述 : 缓存顶级父类——数据关键词（带部分字典）
 * @版权 : 胡璐璐
 * @时间 : 2017年9月29日 15:18:40
 */
public abstract class CacheParent{

	/******************************缓存全局Map*********************************/
	protected final static Map<String,CachePack> map=new ConcurrentHashMap<String, CachePack>();
	
	/****************************系统常量*******************************/
	/*AppClient（本系统）*/
	public final static String appKey="userAdmin";	//应用key
		
	/*其他*/
	public final static String jsonp_callBack_param="callback";		//ajax jsonp回调方法

    /*****************实体类状态（通用）*****************/
	/*基础*/
	public final static int modelStatus_1=1;		//实体类状态——正常（发布、同意、接受......）
	public final static int modelStatus_0=0;		//实体类状态——禁用（停招、拒绝......）
	public final static int modelStatus_2=2;		//实体类状态——未审核（未发布......）
	/*其他状态*/
	public final static int modelStatus_3=3;		//一般为【正在***】
	//100开头
	public final static int modelStatus_10000=10000;	
	public final static int modelStatus_10001=10001;		
	public final static int modelStatus_10002=10002;	
	public final static int modelStatus_10003=10003;	
	//200开头
	public final static int modelStatus_20000=20000;	
	public final static int modelStatus_20001=20001;	
	public final static int modelStatus_20002=20002;	
	//300开头
	public final static int modelStatus_30000=30000;	
	public final static int modelStatus_30001=30001;	
	public final static int modelStatus_30002=30002;	
	/*************响应状态*************/
	//code=执行某方法的状态
	public final static int response_code_failure=-1;		//失败
	public final static int response_code_success=1;		//成功
	public final static int response_code_notPermission=4;	//没有权限
	public final static int response_code_notLogin=6;		//未登录
	public final static int response_code_notComplete=8;	//未完善信息
	public final static int response_code_userExists=10000;		//用户已存在
	public final static int response_code_userNotExists=10001;	//用户不存在
	public final static int response_code_userNotComplete=10002;		//用户信息未完善（对立：10103）
	public final static int response_code_userError=10003;		//用户错误、异常
	public final static int response_code_verificationCodeError=10104;	//验证码错误（失效）
	public final static int response_code_passWordError=10107;	//密码错误
	public final static int response_code_emailCodeError=10204;		//邮箱验证码错误
	
	/**********************数据字典**********************/
	public final static String dict="dictionary";	
	
}