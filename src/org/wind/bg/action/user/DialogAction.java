package org.wind.bg.action.user;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.wind.bg.model.Resource;
import org.wind.bg.model.Server;
import org.wind.bg.util.ObjectUtil;
import org.wind.bg.util.RegexUtil;
import org.wind.bg.util.system.SystemUtil;
import org.wind.mvc.annotation.controller.An_Controller;
import org.wind.mvc.annotation.controller.An_URL;
import org.wind.orm.Table;
import org.wind.orm.bean.Page;

/**
 * @描述 : 弹出框Action
 * @作者 : 胡璐璐
 * @时间 : 2021年4月12日 08:50:06
 */
@An_Controller("/user/dialog")
public class DialogAction {

	/**首页**/
	@An_URL
	public String index(String type) {
		return type+".html";
	}
	
	/**权限**/
	@An_URL("/resource")
	public Map<Object,Object> resource(Integer page,Integer limit,String name,String code,Integer status) {
		name=RegexUtil.clearEmpty(name);
		code=RegexUtil.clearEmpty(code);
		//
		Page pageObj=new Page(SystemUtil.getPage(page), SystemUtil.getLimit(limit));
		StringBuffer tjSQL=new StringBuffer("1=1");
		List<Object> tjList=new ArrayList<Object>();
		//名称
		if(name!=null) {
			tjSQL.append(" and name like ?");
			tjList.add("%"+name+"%");
		}
		//代码
		if(code!=null) {
			tjSQL.append(" and code like ?");
			tjList.add("%"+code+"%");
		}
		List<Resource> list=Table.find(Resource.class, tjSQL.toString(), tjList, false, pageObj,null,null,"name","code");
		long size=Table.findSize(Resource.class, tjSQL.toString(), tjList);
		//
		Map<Object,Object> resultMap=new HashMap<Object, Object>();
		resultMap.put("data",ObjectUtil.objectListConvertList(list));
		resultMap.put("count", size);
		return resultMap;
	}
	/**服务端**/
	@An_URL("/server")
	public Map<Object,Object> server(Integer page,Integer limit,String name,String code,Integer status) {
		name=RegexUtil.clearEmpty(name);
		code=RegexUtil.clearEmpty(code);
		//
		Page pageObj=new Page(SystemUtil.getPage(page), SystemUtil.getLimit(limit));
		StringBuffer tjSQL=new StringBuffer("1=1");
		List<Object> tjList=new ArrayList<Object>();
		//名称
		if(name!=null) {
			tjSQL.append(" and name like ?");
			tjList.add("%"+name+"%");
		}
		//代码
		if(code!=null) {
			tjSQL.append(" and code like ?");
			tjList.add("%"+code+"%");
		}
		List<Server> list=Table.find(Server.class, tjSQL.toString(), tjList, false, pageObj,null,null,"name","code");
		long size=Table.findSize(Server.class, tjSQL.toString(), tjList);
		//
		Map<Object,Object> resultMap=new HashMap<Object, Object>();
		resultMap.put("data",ObjectUtil.objectListConvertList(list));
		resultMap.put("count", size);
		return resultMap;
	}

}
