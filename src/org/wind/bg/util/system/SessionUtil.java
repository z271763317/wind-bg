package org.wind.bg.util.system;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.wind.bg.config.SessionKey;
import org.wind.sso.client.util.SSOUtil;

/**
 * @描述 : 会话工具类（一般指HttpSession）——获取session的数据
 * @作者 : 胡璐璐
 * @时间 : 2020年12月9日 13:03:23
 */
@SuppressWarnings("unchecked")
public final class SessionUtil {
	
	//获取 : 指定Key的值
	private static <T> T getAttribute(HttpServletRequest request,String key) {
		Map<Object,Object> map=(Map<Object, Object>) SSOUtil.getSSO(request).getSession();
		if(map!=null) {
			return (T) map.get(key);
		}
		return null;
	}
	
	/**用户ID**/
	public static Long userId(HttpServletRequest request){
		return Long.parseLong(getAttribute(request, SessionKey.userId).toString());
	}
	
}