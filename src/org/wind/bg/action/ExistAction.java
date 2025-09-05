package org.wind.bg.action;

import java.util.HashMap;
import java.util.Map;

import org.wind.bg.annotation.An_NotLogin;
import org.wind.bg.config.cache.Cache;
import org.wind.bg.model.User;
import org.wind.bg.util.ValidateUtil;
import org.wind.mvc.annotation.controller.An_Controller;
import org.wind.mvc.annotation.controller.An_URL;
import org.wind.orm.Table;

/**
 * @描述 : 用户存在Action——验证用户是否存在
 * @作者 : 胡璐璐
 * @时间 : 2021年5月3日 02:22:50
 */
@An_Controller("/exist")
public class ExistAction{

	@An_URL@An_NotLogin
	public Map<String,Object> index(String userName) throws Exception{
		Map<String,Object> resultMap=new HashMap<>();
		try {
			ValidateUtil.notEmpty(userName, "缺少【用户名】");
			//
			User obj=Table.findOne(User.class, "user_name=?", new Object[] {userName}, false,"id");
			if(obj==null) {
				throw new IllegalArgumentException("用户名不存在");
			}
		}catch(IllegalArgumentException e) {
			resultMap.put("code", Cache.response_code_failure);
			resultMap.put("message", e.getMessage());
		}catch(Exception e) {
			resultMap.put("code", Cache.response_code_failure);
			resultMap.put("message", "【验证用户】接口异常");
		}
		return resultMap;
	}
}
