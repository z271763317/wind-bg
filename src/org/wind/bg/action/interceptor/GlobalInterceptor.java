package org.wind.bg.action.interceptor;

import java.lang.reflect.Method;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.wind.bg.annotation.An_Add;
import org.wind.bg.annotation.An_NotLogin;
import org.wind.bg.annotation.An_Update;
import org.wind.bg.config.cache.Cache;
import org.wind.bg.model.User;
import org.wind.bg.util.FileOS;
import org.wind.bg.util.JsonUtil;
import org.wind.bg.util.ToolUtil;
import org.wind.mvc.annotation.controller.An_Controller;
import org.wind.mvc.annotation.interceptor.An_Interceptor;
import org.wind.mvc.bean.context.ActionContext;
import org.wind.mvc.interceptor.Interceptor;
import org.wind.mvc.result.Result;
import org.wind.orm.Table;
import org.wind.sso.client.util.SSOUtil;

/**
 * @描述 : 通用拦截器
 * @作者 : 胡璐璐
 * @时间 : 2020年8月29日 14:44:39
 */
@An_Interceptor("/*")
public class GlobalInterceptor implements Interceptor{

	private static final Logger logger=Logger.getLogger(GlobalInterceptor.class);
	
	private static Set<Class<? extends Table>> transactionSet=new LinkedHashSet<Class<? extends Table>>();
	
	static {
		transactionSet.add(User.class);
	}
	
	/**执行前**/
	public boolean before(ActionContext context) throws Exception {
		HttpServletRequest request=context.getRequest();
		HttpServletResponse response=context.getResponse();
		//
		boolean isNeedLogin=!(context.getMethod().isAnnotationPresent(An_NotLogin.class));		//是否 : 需要登录
		//需要登录，且未登录
		if(isNeedLogin && !isLogin(request,response)) {
			clearSession(request, response);
			return false;
		}
		//开启事务
		for(Class<? extends Table> t_class:transactionSet) {
			Table.setAutoCommit(t_class, false);
		}
		return true;
	}

	/**执行后**/
	@SuppressWarnings("unchecked")
	public void after(ActionContext context,Result result) {
		if(!context.getResponse().isCommitted()) {
			afterHandler(context);		//执行后的一些处理
			//
			if(result.getData()==null) {
				Object methodResult=result.getMethodResult();
				Map<String,Object> resultMap=null;
				if(methodResult!=null) {
					if(methodResult instanceof Map) {
						resultMap=(Map<String, Object>) methodResult;
					}
				}else{
					resultMap=new HashMap<String,Object>();
				}
				if(resultMap!=null) {
					if(!resultMap.containsKey("code")) {
						resultMap.put("code", Cache.response_code_success);
					}
					String t_result=JsonUtil.toJson(resultMap);
					if(t_result!=null) {
						FileOS.writer(context.getRequest(), context.getResponse(), t_result);		//响应
					}
				}
			}
		}
		//提交
		try {
			for(Class<? extends Table> t_class:transactionSet) {
				Table.commit(t_class);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**完成后（所有处理完，渲染页面后执行。若在此之前出现了异常，则exception会有值，可做异常处理）**/
	public void complete(ActionContext context, Throwable e) {
		HttpServletRequest request=context.getRequest();
		HttpServletResponse response=context.getResponse();
		try{
			//有异常，说明处理失败
			if(e!=null){
				//回滚
				for(Class<? extends Table> t_class:transactionSet) {
					Table.rollback(t_class);
				}
				if(e.getClass()==IllegalArgumentException.class){
					returnError(request, response,e.getMessage());
				}else{
					logger.error(e.getMessage(),e);
					returnError(request, response,"系统错误，请稍后在试试");
				}
			}
		}catch(Exception e1){
			logger.error(e.getMessage(),e);
			returnError(request, response,"系统错误，请稍后在试试2");
		}finally{
			//释放连接
			for(Class<? extends Table> t_class:transactionSet) {
				Table.close(t_class);
			}
		}
	}
	/**
	 * 返回错误
	 * @param request
	 * @param response
	 * @param errorMsg : 错误信息
	 */
	private static void returnError(HttpServletRequest request,HttpServletResponse response,String errorMsg){
		if(!response.isCommitted()) {
			//如果是：Ajax请求
			if(ToolUtil.isAjax(request)){
				Map<String,Object> resultMap=new HashMap<String,Object>();
				resultMap.put("code", Cache.response_code_failure);
				resultMap.put("message", errorMsg);
				String t_result=JsonUtil.toJson(resultMap);
				//
				FileOS.writer(request, response, t_result);		//返回错误信息
			}else{
				//返回错误页面
				request.setAttribute("errorMessage", errorMsg);
				try {
					response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, errorMsg);		//request.setAttribute("javax.servlet.error.message", errorMsg);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	/**************原User拦截的内容**************/
	//执行后的处理
	private static void afterHandler(ActionContext context) {
		Method method=context.getMethod();
		An_Add an_add=method.getAnnotation(An_Add.class);
		HttpServletRequest request=context.getRequest();
		//添加
		if(an_add!=null) {
			request.setAttribute("isUpdate", false);
			if(request.getAttribute("_pageName")==null) {
				request.setAttribute("_pageName", an_add.value());
			}
		}else{
			//更新
			An_Update an_update=method.getAnnotation(An_Update.class);
			if(an_update!=null) {
				request.setAttribute("isUpdate", true);
				if(request.getAttribute("_pageName")==null) {
					request.setAttribute("_pageName", an_update.value());
				}
			}
		}
		Object controller=context.getController();
		An_Controller an_controller=controller.getClass().getAnnotation(An_Controller.class);
		request.setAttribute("_moduleName", an_controller.name());	//模块名
	}
	//是否登录（同时获取并保存会话信息）
	private static boolean isLogin(HttpServletRequest request,HttpServletResponse response) throws Exception{
		return SSOUtil.isLogin(request, response);
	}
	//删除 : 会话所有信息，并跳转到登录页面、提示异常信息
	private static void clearSession(HttpServletRequest request,HttpServletResponse response){
		String requestType = request.getHeader("X-Requested-With");		//请求类型（ajax还是普通）
		/*传统登录URL*/
//		String project=request.getContextPath();
//		if(project==null || project.length()<=0) {
//			project="/";
//		}
		/*SSO登录URL*/
		String project=SSOUtil.getLoginPageUrlParam(request, null, null);
		//ajax请求
		if((requestType!=null && requestType.equalsIgnoreCase("XMLHttpRequest"))){
			Map<String,Object> resultMap=new HashMap<String, Object>();
			int code=Cache.response_code_notLogin;		//默认：未登录
			String message="请先登录";
			resultMap.put("code", code);	//登录状态
			resultMap.put("href", project);		//登录Url地址
			resultMap.put("message", message);
			FileOS.writer(request,response, JsonUtil.toJson(resultMap));
		//普通请求（跳转登录页面）
		}else{
			SSOUtil.redirectLoginPage(request, response, null, null);
		}
	}
}
