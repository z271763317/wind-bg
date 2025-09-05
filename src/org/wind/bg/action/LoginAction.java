package org.wind.bg.action;

import java.util.HashMap;
import java.util.Map;

import org.wind.bg.annotation.An_NotLogin;
import org.wind.bg.config.SessionKey;
import org.wind.bg.config.cache.Cache;
import org.wind.bg.model.User;
import org.wind.bg.util.EncryptUtil;
import org.wind.bg.util.ValidateUtil;
import org.wind.mvc.annotation.controller.An_Controller;
import org.wind.mvc.annotation.controller.An_URL;
import org.wind.orm.Table;

/**
 * @描述 : 登录Action
 * @作者 : 胡璐璐
 * @时间 : 2021年4月6日 11:35:41
 */
@An_Controller("/login")
public class LoginAction{

    /**登录**/
	@An_URL@An_NotLogin
	public Map<String,Object> index(String userName,String passWord) throws Exception{
		Map<String,Object> resultMap=new HashMap<>();
		try {
			ValidateUtil.notEmpty(userName, "缺少【用户名】");
			ValidateUtil.notEmpty(passWord, "缺少【密码】");
			//
			User obj=Table.findOne(User.class, "user_name=?", new Object[] {userName}, false);
			
			if(obj!=null) {
				int status=obj.getStatus();
				//正常
				if(status==User.status_normal) {
					String passWord_cipher=EncryptUtil.getMD5(passWord);
					String t_passWord=obj.getPassWord();
					if(!passWord_cipher.equals(t_passWord)) {
						throw new IllegalArgumentException("用户名或密码错误2");
					}else{
						Map<String,Object> sessionMap=new HashMap<>();
						sessionMap.put(SessionKey.userId, obj.getId());
						resultMap.put("session", sessionMap);
						return resultMap;
					}
				}else{
					switch(status) {
						//禁用
						case User.status_failure:{
							throw new IllegalArgumentException("该用户已禁用");
						}
						//冻结
						case User.status_freeze:{
							throw new IllegalArgumentException("该用户已冻结");
						}
						default:{
							throw new IllegalArgumentException("该用户状态未知");
						}
					}
				}
			}else{
				throw new IllegalArgumentException("用户名或密码错误");
			}
		}catch(IllegalArgumentException e) {
			resultMap.put("code", Cache.response_code_failure);
			resultMap.put("message", e.getMessage());
		}catch(Exception e) {
			resultMap.put("code", Cache.response_code_failure);
			resultMap.put("message", "【登录接口】异常");
		}
		return resultMap;
	}
}
