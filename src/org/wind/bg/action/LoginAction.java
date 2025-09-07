package org.wind.bg.action;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.wind.bg.annotation.An_NotLogin;
import org.wind.bg.config.SessionKey;
import org.wind.bg.config.cache.Cache;
import org.wind.bg.model.User;
import org.wind.bg.util.EncryptUtil;
import org.wind.bg.util.SysConstant;
import org.wind.bg.util.ValidateUtil;
import org.wind.bg.util.system.Yzm;
import org.wind.bg.util.system.YzmUtil;
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

	private static final String key_captcha="captcha";		//验证码内容（存入session）
	
    /**登录**/
	@An_URL@An_NotLogin
	public Map<String,Object> index(HttpSession session,String userName,String passWord,String captcha) throws Exception{
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
					}
					int type=SysConstant.type;
			    	switch(type) {
			    		case 1:return this.login_1(session, obj,captcha);
			    		case 2:return this.login_2(obj);
			    		default:throw new IllegalArgumentException("未知的系统类型："+type);
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
	//登录（单体）
	private Map<String, Object> login_1(HttpSession session,User obj,String captcha){
		boolean isYzm=SysConstant.isCaptcha;
    	//是否开启验证码
    	if(isYzm) {
    		ValidateUtil.notEmpty(captcha, "缺少【验证码】");
    		String captchaSource=(String) session.getAttribute(key_captcha);
    		if(!captcha.equalsIgnoreCase(captchaSource)) {
    			throw new IllegalArgumentException("验证码错误");
    		}
    	}
		session.removeAttribute(key_captcha);		//删除验证码
		session.setAttribute(SessionKey.userId, obj.getId());		//用户ID
		return null;
	}
	//登录_分布式
	private Map<String,Object> login_2(User obj) throws Exception{
		Map<String,Object> resultMap=new HashMap<>();
		Map<String,Object> sessionMap=new HashMap<>();
		sessionMap.put(SessionKey.userId, obj.getId());
		resultMap.put("session", sessionMap);
		return resultMap;
	}
	
	/**验证码**/
	@An_URL("/captcha")@An_NotLogin
	public void captcha(HttpServletResponse response,HttpSession session) throws Exception{
		byte[] captcha = null;		//验证码图片数据
		boolean isYzm=SysConstant.isCaptcha;
    	//是否开启验证码
    	if(isYzm) {
			Yzm objYzm = YzmUtil.generate();
			String createText = objYzm.getText();
			captcha = objYzm.getImage();
			session.setAttribute(key_captcha, createText);		//验证码存入session
			//
		    response.setHeader("Cache-Control", "no-store");
		    response.setHeader("Pragma", "no-cache");
		    response.setDateHeader("Expires", 0);
		    response.setContentType("image/jpeg");
		    ServletOutputStream sout = null;
		    try {
		    	sout = response.getOutputStream();
		        sout.write(captcha);
		        sout.flush();
		    }catch(IOException e) {
		    	throw e;
		    }finally {
		    	if(sout!=null){try {sout.close();}catch(Exception e) {}}
		    }
        }
		
	}
}
