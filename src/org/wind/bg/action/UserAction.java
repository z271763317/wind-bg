package org.wind.bg.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.wind.bg.annotation.An_NotLogin;
import org.wind.bg.model.Config;
import org.wind.bg.model.Resource;
import org.wind.bg.model.RoleResource;
import org.wind.bg.model.Server;
import org.wind.bg.model.User;
import org.wind.bg.model.UserRole;
import org.wind.bg.util.EncryptUtil;
import org.wind.bg.util.FileOS;
import org.wind.bg.util.JsonUtil;
import org.wind.bg.util.RegexUtil;
import org.wind.bg.util.ToolUtil;
import org.wind.bg.util.ValidateUtil;
import org.wind.bg.util.system.SessionUtil;
import org.wind.mvc.annotation.controller.An_Controller;
import org.wind.mvc.annotation.controller.An_URL;
import org.wind.orm.Table;
import org.wind.orm.util.TableUtil;
import org.wind.sso.client.util.SSOUtil;

/**
 * @描述 : 用户Action
 * @作者 : 胡璐璐
 * @时间 : 2020年9月3日 18:06:43
 */
@An_Controller("/user")
public class UserAction {

	/**主页**/
	@An_URL
	public String main(HttpServletRequest request,Long id){
		String code="mainSystem";
		if(id!=null) {
			Server obj=Server.get(id);
			if(obj!=null) {
				code=obj.getCode();
			}
		}
		List<Server> serverList=Server.getAll();
		User obj=Table.findById(User.class, SessionUtil.userId(request));
		request.setAttribute("uid", SessionUtil.userId(request));
		request.setAttribute("userName", obj.getUserName());
		request.setAttribute("name", obj.getName());
		request.setAttribute("type", obj.getType());
		request.setAttribute("system", code);
		request.setAttribute("serverList", serverList);
		request.setAttribute("systemName", Config.get(Config.systemName));
		request.setAttribute("systemNameFontSize", Config.get(Config.systemNameFontSize));
		request.setAttribute("systemIndexUrl", Config.get(Config.systemIndexUrl));
		return "main";
	}
	/**首页**/
	@An_URL("/index")
	public String index(HttpServletRequest request, HttpServletResponse response) {
		return "index";
	}
	/**修改密码**/
	@An_URL("/modifyPassWord")
	public void modifyPassWord(HttpServletRequest request,HttpServletResponse response,String oldPassWord,String newPassWord,String confirmNewPassWord) throws Exception{
		ValidateUtil.notEmpty(oldPassWord, "缺少【旧密码】");
		ValidateUtil.notEmpty(newPassWord, "缺少【新密码】");
		ValidateUtil.notEmpty(confirmNewPassWord, "缺少【确认密码】");
		//
		if(newPassWord.length()<8) {
			throw new IllegalArgumentException("新密码长度不能少于8位数");
		}
		if(!newPassWord.equals(confirmNewPassWord)) {
			throw new IllegalArgumentException("新密码与确认密码不一致");
		}
		Long userId=SessionUtil.userId(request);
		User obj=	Table.findById(User.class, userId, "passWord");
		if(obj!=null) {
			String passWord_source=obj.getPassWord();
			//
			if(!EncryptUtil.getMD5(oldPassWord).equals(passWord_source)) {
				throw new IllegalArgumentException("旧密码错误");
			}
		}else{
			throw new IllegalArgumentException("当前用户不存在");
		}
		String newPassWord_cipher=EncryptUtil.getMD5(newPassWord);		//密文密码
		obj.setPassWord(newPassWord_cipher);
		obj.save();
	}
	/**验证当前用户密码（可做解锁用）**/
	@An_URL("/validPassWord")
	public void validPassWord(HttpServletRequest request,HttpServletResponse response,String passWord) throws Exception{
		ValidateUtil.notEmpty(passWord, "没有输入密码");
		//
		Long userId=SessionUtil.userId(request);
		User obj=	Table.findById(User.class, userId, "passWord");
		if(obj!=null) {
			String passWord_cipher=EncryptUtil.getMD5(passWord);
			String t_passWord=obj.getPassWord();
			if(!passWord_cipher.equals(t_passWord)) {
				throw new IllegalArgumentException("密码不正确");
			}
		}else{
			throw new IllegalArgumentException("用户名不存在");
		}
	}
	/**退出**/
	@An_URL("/exit")
	public Map<Object,Object> exit(HttpServletRequest request,HttpServletResponse response) {
		SSOUtil.exit(request,response);
		Map<Object,Object> resultMap=new HashMap<Object, Object>();
		resultMap.put("href", SSOUtil.getLoginPageUrl()+"?referer="+ToolUtil.getRootURL(request));
		return resultMap;
	}
	/**菜单列表**/
	@An_URL("/menu")
	public void menu(HttpServletRequest request,HttpServletResponse response,String code) {
		code=RegexUtil.clearEmpty(code);
		if(code==null) {
			return;
		}
		Long serverId=Server.getId(code);
		//
		Long userId=SessionUtil.userId(request);
		List<Object> tjList=new ArrayList<Object>();
		tjList.add(userId);
		List<Long> roleIdList=Table.findSpecifiedList(UserRole.class, "user_id=?", tjList, "roleId");
		//
		List<Map<Object,Object>> menuList=new ArrayList<Map<Object,Object>>();		//返回的数据
		if(roleIdList!=null && roleIdList.size()>0) {
			Set<Long> resIdList=RoleResource.get(roleIdList);
			if(resIdList!=null && resIdList.size()>0) {
				Map<Long,Map<Object,Object>> lastAllStructuredMap_orderBefore=new LinkedHashMap<Long, Map<Object,Object>>();		//最后一次的所有结构化数据（key=主菜单ID），排序前
				for(Long resId:resIdList) {
					Resource.put(serverId,resId, lastAllStructuredMap_orderBefore,null,true);
				}
				Map<Integer,Map<Long,Map<Object,Object>>> lastAllStructuredMap_order=new TreeMap<Integer,Map<Long,Map<Object,Object>>>();		//排序
				for(Entry<Long,Map<Object,Object>> entry:lastAllStructuredMap_orderBefore.entrySet()) {
					Long t_key=entry.getKey();
					Map<Object,Object> t_value=entry.getValue();
					Integer t_order=(Integer) t_value.get("_order");
					Map<Long,Map<Object,Object>> t_map=lastAllStructuredMap_order.get(t_order);
					if(t_map==null) {
						t_map=new LinkedHashMap<Long, Map<Object,Object>>();
						lastAllStructuredMap_order.put(t_order, t_map);
					}
					t_map.put(t_key, t_value);
				}
				for(Map<Long,Map<Object,Object>> t_map:lastAllStructuredMap_order.values()) {
					for(Map<Object,Object> t_t_map:t_map.values()) {
						menuList.add(t_t_map);	
					}
				}
			}
		}
		FileOS.writer(request, response, JsonUtil.toJson(menuList));
	}
	
	/***********用户类型***********/
	/**获取 : 用户类型（单个）**/
	@An_URL("/type/get")@An_NotLogin
	public Map<String,Object> type_get(Long userId){
		Map<String,Object> resultMap=new HashMap<>();
		ValidateUtil.notEmpty(userId, "缺少【用户ID】");
		//
		User obj=Table.findById(User.class, userId,"type");
		if(obj!=null) {
			resultMap.put("type", obj.getType());
		}
		return resultMap;
	}
	/**列表 : 用户类型（多个。key=userId【字符串】，value=类型【整型】）**/
	@An_URL("/type/list")@An_NotLogin
	public Map<String,Object> type_list(List<Long> userIdList) {
		ToolUtil.deleteListEmpty(userIdList);
		if(userIdList==null || userIdList.size()<=0) {
			throw new IllegalArgumentException("至少指定一个用户ID");
		}
		Map<String,Object> resultMap=new HashMap<>();
		List<User> objList=Table.find(User.class, "id in("+TableUtil.getPlaceholder(userIdList)+")", userIdList,false,null, null,null,"type");
		Map<String,Integer> map=new LinkedHashMap<String, Integer>();
		for(User obj:objList) {
			Long id=obj.getId();
			Integer type=obj.getType();
			map.put(id.toString(),type);
		}
		resultMap.put("typeMap", map);
		return resultMap;
	}
	/***********用户名称***********/
	/**获取 : 用户名称（单个）**/
	@An_URL("/name/get")@An_NotLogin
	public Map<String,Object> name_get(Long userId) {
		Map<String,Object> resultMap=new HashMap<>();
		ValidateUtil.notEmpty(userId, "缺少【用户ID】");
		//
		User obj=Table.findById(User.class, userId,"name");
		if(obj!=null) {
			resultMap.put("name", obj.getName());
		}
		return resultMap;
	}
	/**列表 : 用户名称（多个。key=userId【字符串】，value=名称）**/
	@An_URL("/name/list")@An_NotLogin
	public Map<String,Object> name_list(List<Long> userIdList) {
		ToolUtil.deleteListEmpty(userIdList);
		if(userIdList==null || userIdList.size()<=0) {
			throw new IllegalArgumentException("至少指定一个用户ID");
		}
		Map<String,Object> resultMap=new HashMap<>();
		List<User> objList=Table.find(User.class, "id in("+TableUtil.getPlaceholder(userIdList)+")", userIdList,false,null, null,null,"name");
		Map<String,String> map=new LinkedHashMap<String, String>();
		for(User obj:objList) {
			Long id=obj.getId();
			String name=obj.getName();
			name=name!=null?name:"";
			map.put(id.toString(),name);
		}
		resultMap.put("nameMap", map);
		return resultMap;
	}
	
}
