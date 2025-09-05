package org.wind.bg.action.user;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.wind.mvc.annotation.controller.An_Controller;

/**
 * @描述 : 通用Action
 * @作者 : 胡璐璐
 * @时间 : 2021年4月12日 08:50:06
 */
@An_Controller("/user/global")
public class GlobalAction {

	/**首页**/
//	@An_URL("/index	")
	public String index(HttpServletRequest request, HttpServletResponse response) {
		return "index";
	}

}
