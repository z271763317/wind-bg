package org.wind.bg.util.system;

import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.wind.bg.config.SessionKey;
import org.wind.bg.model.Config;
import org.wind.bg.model.Resource;
import org.wind.bg.model.RoleResource;
import org.wind.bg.util.SysConstant;
import org.wind.sso.client.util.SSOUtil;

/**
 * @描述 : 系统重要工具类
 * @版权 : 胡璐璐
 * @时间 : 2019年11月22日 16:14:46
 */
public final class SystemUtil {
	
	/*用户类型*/
	private static Map<Integer,String> userTypeMap=new LinkedHashMap<Integer, String>();
	public static final int superAdminType=0;			//超级管理员类型
	
	/*系统代码*/
	private static String mainSystemCode="mainSystem";		//主系统
	
	/*系统配置*/
	private static Set<String> systemConfigSet=new HashSet<String>();
	
	static {
//		userTypeMap.put(0, "超级管理员");
		userTypeMap.put(1, "管理员");
		userTypeMap.put(2, "普通");
		//
		systemConfigSet.add(Config.systemName);
		systemConfigSet.add(Config.systemNameFontSize);
	}
	
	/**获取 : 分页可以使用的limit**/
	public static int getPage(Integer page) {
		if(page!=null && page>0) {
			return page;
		}else {
			return 1;
		}
	}
	/**获取 : 分页可以使用的limit（最大1000）**/
	public static int getLimit(Integer limit) {
		if(limit!=null && limit>0) {
			if(limit>1000) {
				return 1000;
			}else{
				return limit;
			}
		}else {
			return 1;
		}
	}
	/**获取 : 用户类型**/
	public static Map<Integer,String> getUserTypeMap(){
		return userTypeMap;
	}
	/**获取 : 用户类型（能操作的）**/
	public static Map<Integer,String> getUserTypeCanMap(Integer type_curr){
		Map<Integer,String> t_userTypeMap=new LinkedHashMap<Integer, String>();
		for(Entry<Integer,String> entry:userTypeMap.entrySet()) {
			Integer key=entry.getKey();
			String value=entry.getValue();
			if(isAllowHandle(type_curr, key)) {
				t_userTypeMap.put(key, value);
			}
		}
		return t_userTypeMap;
	}
	/**是否允许指定类型type_curr对type处理（type_curr=指定类型【一般是当前登录的用户】；type=判断的用户类型）**/
	public static boolean isAllowHandle(Integer type_curr,Integer type) {
		if(isSuperAdmin(type_curr) ||  type>type_curr) {
			return true;
		}
		return false;
	}
	/**是否超级管理员**/
	public static boolean isSuperAdmin(Integer type_curr) {
		if(type_curr!=null && type_curr.equals(superAdminType)) {
			return true;
		}
		return false;
	}
	/**是否主系统**/
	public static boolean isMainSystem(String systemCode) {
		if(systemCode!=null && systemCode.equals(mainSystemCode)) {
			return true;
		}
		return false;
	}
	/**是否系统配置**/
	public static boolean isSystemConfig(String code) {
		if(code!=null && systemConfigSet.contains(code)) {
			return true;
		}
		return false;
	}
	/**刷新权限（及其相关）**/
	public static void refreshResource() throws Exception {
		Resource.init();
		RoleResource.init();
	}
	/**刷新权限（及其相关，异步执行）**/
	public static void refreshResource_async() throws Exception {
		new Thread() {
			public void run() {
				try {
					SystemUtil.refreshResource();
				} catch (Exception e) {
					e.printStackTrace();
				}		
			}
		}.start();
	}
	/**是否 : 已登录**/
	public static boolean isLogin(HttpServletRequest request,HttpServletResponse response) {
		int type=SysConstant.type;
		switch(type) {
			//单体
			case 1:{
				Object userId=request.getSession().getAttribute(SessionKey.userId);
				return userId!=null;
			}
			//分布式
			case 2:{ 
				try {
					return SSOUtil.isLogin(request, response);
				}catch(Exception e) {
					e.printStackTrace();
					return false;
				}
			}
			default:{
				throw new IllegalArgumentException("未知的系统类型");
			}
		}
	}
	/**跳转 : 登录界面（未登录则跳转，只给默认页使用）**/
	public static void jumpLogin(HttpServletRequest request,HttpServletResponse response) throws IOException {
		boolean isLogin=SystemUtil.isLogin(request, response);
		//已登录
		if(isLogin){
			response.sendRedirect("user");
		}else{
			int type=SysConstant.type;
			switch(type) {
				//单体
				case 1:{
					/*传统登录URL*/
//					String project=request.getContextPath();
//					if(project==null || project.length()<=0) {
//						project="/";
//					}
//					response.sendRedirect(project);
					break;
				}
				//分布式
				case 2:{ 
					String loginPageUrl=SSOUtil.getLoginPageUrlParam(request);
					response.sendRedirect(loginPageUrl);
					break;
				}
				default:{
					throw new IllegalArgumentException("未知的系统类型");
				}
			}
		}
	}
	/**获取 : 系统名称**/
	public static String getSystemName() {
		return Config.get(Config.systemName);
	}
	 
}