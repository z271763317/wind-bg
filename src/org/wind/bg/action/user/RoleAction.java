package org.wind.bg.action.user;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.wind.bg.action.parent.ModuleAction;
import org.wind.bg.model.Resource;
import org.wind.bg.model.Role;
import org.wind.bg.model.RoleResource;
import org.wind.bg.model.Server;
import org.wind.bg.util.ObjectUtil;
import org.wind.bg.util.RegexUtil;
import org.wind.bg.util.system.SystemUtil;
import org.wind.mvc.annotation.controller.An_Controller;
import org.wind.mvc.annotation.controller.An_URL;
import org.wind.orm.Table;
import org.wind.orm.bean.Page;

/**
 * @描述 : 角色管理
 * @作者 : 胡璐璐
 * @时间 : 2021年4月8日 21:36:49
 */
@SuppressWarnings("unchecked")
@An_Controller(value="/user/system/role",name="角色管理")
public class RoleAction extends ModuleAction<Role>{

	public static final String superAdminCode="superAdmin";		//超级管理员代码，该代码代表的角色不允许更改重要信息
	
	/**数据列表**/
	@An_URL("/list")
	public Map<Object,Object> list(Integer page,Integer limit,String name,String code,Integer status,String startTime,String endTime) {
		name=RegexUtil.clearEmpty(name);
		code=RegexUtil.clearEmpty(code);
		startTime=RegexUtil.clearEmpty(startTime);
		endTime=RegexUtil.clearEmpty(endTime);
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
		//状态
		if(status!=null) {
			tjSQL.append(" and status=?");
			tjList.add(status);
		}
		//起始时间
		if(startTime!=null) {
			tjSQL.append(" and create_time>=?");
			tjList.add(startTime);
		}
		//结束时间
		if(endTime!=null) {
			tjSQL.append(" and create_time<=?");
			tjList.add(endTime);
		}
		List<Role> list=Table.find(this.tableClass, tjSQL.toString(), tjList, true, pageObj);
		long size=Table.findSize(this.tableClass, tjSQL.toString(), tjList);
		//
		Map<Object,Object> resultMap=new HashMap<Object, Object>();
		resultMap.put("data",ObjectUtil.objectListConvertList(list));
		resultMap.put("count", size);
		return resultMap;
	}
	/**保存**/
	@An_URL("/save")
	public void save(HttpServletRequest request,Role obj) throws Exception {
		if(obj!=null) {
			String code=obj.getCode();
			if(code==null) {
				throw new IllegalArgumentException("未填写【代码】");
			}
			//超级管理员不允许修改代码
			if(superAdminCode.equals(code)) {
				throw new IllegalArgumentException("不允许修改的角色信息");
			}
			Long id=obj.getId();
			StringBuffer tjSQL=new StringBuffer("code=?");
			List<Object> tjList=new ArrayList<Object>();
			tjList.add(code);
			if(id!=null) {
				tjSQL.append(" and id!=?");
				tjList.add(id);
			}
			Role obj_source=Table.findOne(this.tableClass, tjSQL.toString(), tjList, false, "id");
			if(obj_source!=null) {
				throw new IllegalArgumentException("您填写的【代码】已存在");
			}
			obj.save();
		}else{
			throw new IllegalArgumentException("未填选任何数据");
		}
	}
	/**删除**/
	@An_URL("/delete")
	public void delete(Object id) {
		Role obj=Table.findById(tableClass, id, "isAllowDelete");
		if(obj!=null && obj.getIsAllowDelete()) {
			super.delete(tableClass, id);
		}else{
			throw new IllegalArgumentException("不允许删除");
		}
	}
	/**批量删除**/
	@An_URL("/deleteSelect")
	public void deleteSelect(List<Object> idList) {
		List<Role> list=Table.findByIdList(tableClass, idList,"isAllowDelete");
		List<Object> opIdList=new ArrayList<Object>();
		for(Role obj:list) {
			Boolean isAllowDelete=obj.getIsAllowDelete();
			if(isAllowDelete) {
				opIdList.add(obj.getId());
			}
		}
		super.deleteSelect(tableClass, opIdList);
	}
	/**批量启用**/
	@An_URL("/enableSelect")
	public void enableSelect(List<Object> idList) {
		super.enableSelect(tableClass, idList);
	}
	/**批量禁用**/
	@An_URL("/disableSelect")
	public void disableSelect(List<Object> idList) {
		List<Role> list=Table.findByIdList(tableClass, idList,"isAllowDelete");
		List<Object> opIdList=new ArrayList<Object>();
		for(Role obj:list) {
			Boolean isAllowDelete=obj.getIsAllowDelete();
			if(isAllowDelete) {
				opIdList.add(obj.getId());
			}
		}
		super.disableSelect(tableClass, opIdList);
	}
	
	/*************角色权限*************/
	/**权限UI**/
	@An_URL("/resource")
	public String resource() throws Exception {
		return "resource.html";
	}
	/**权限数据**/
	@An_URL("/resourceData")
	public Map<Object,Object> resourceData(Long roleId) throws Exception{
		SystemUtil.refreshResource();
		Map<Object,Object> resultMap=new HashMap<Object, Object>();
		if(roleId!=null) {
			List<Object> tjList=new ArrayList<Object>();
			tjList.add(roleId);
			List<Long> resIdList=Table.findSpecifiedList(RoleResource.class, "role_id=?", tjList, "resourceId");
			resultMap.put("resExist", resIdList);
		}
		Map<Long,Map<Object,Object>> map=Resource.getAllStructured();
		Map<Long,Map<Object,Object>> serverMenuMap=new LinkedHashMap<Long, Map<Object,Object>>();		//key=系统ID；value=菜单数据
		for(Entry<Long,Map<Object,Object>> entry:map.entrySet()) {
			Long mainMenuId=entry.getKey();		//主菜单ID
			Map<Object,Object> value=entry.getValue();
			Resource obj=Resource.get(mainMenuId);
			if(obj!=null) {
				Long serverId=obj.getServerId().getId();
				Server objServer=Server.get(serverId);
				if(objServer!=null) {
					String serverName=objServer.getName();
					//
					Map<Object,Object> serverMenuMap_item=serverMenuMap.get(serverId);		//单项
					if(serverMenuMap_item==null) {
						serverMenuMap_item=new LinkedHashMap<Object, Object>();
						serverMenuMap.put(serverId, serverMenuMap_item);
						//
						serverMenuMap_item.put("id", "-"+serverId);		//名称
						serverMenuMap_item.put("title", serverName);		//名称
						serverMenuMap_item.put("icon", "");		//图标
						serverMenuMap_item.put("spread", true);		//是否展开
						serverMenuMap_item.put("children", new ArrayList<Map<Object,Object>>());		//子菜单（List结构）
					}
					List<Map<Object,Object>> children=(List<Map<Object, Object>>) serverMenuMap_item.get("children");
					children.add(value);
				}
			}
		}
		List<Map<Object,Object>> resList=new ArrayList<Map<Object,Object>>();
		if(map!=null) {
			for(Map<Object,Object> t_map:serverMenuMap.values()) {
				resList.add(t_map);
			}
		}
		resultMap.put("resList", resList);
		return resultMap;
	}
	/**获取 : 权限信息**/
	@An_URL("/getResource")
	public Map<Object,Object> getResource(Long id){
		if(id!=null) {
			Resource obj=Resource.get(id);
			Map<Object,Object> resultMap=new HashMap<Object,Object>();
			resultMap.put("obj", ObjectUtil.objectConvertMap(obj));
			return resultMap;
		}else{
			throw new IllegalArgumentException("未指定项");
		}
	}
	/**权限保存**/
	@An_URL("/resourceSave")
	public void resourceSave(HttpServletRequest request,Long id,List<Long> resourceIdList) throws Exception {
		Role obj=Table.findById(this.tableClass, id, "code");
		if(obj!=null) {
			String code=obj.getCode();
			/*先删除可以删除的*/
			List<Object> tjList=new ArrayList<Object>();
			tjList.add(id);
			tjList.add(true);		//允许删除的
			Table.delete(RoleResource.class, "role_id=? and is_allow_delete=?", tjList);
			//
			Role objRole=new Role();
			objRole.setId(id);
			List<Long> newAddResIdList=new ArrayList<Long>();		//新的可以添加的权限ID列表
			List<RoleResource> saveList=new ArrayList<RoleResource>();		//保存的
			//超级管理员
			if(code.equals(superAdminCode)) {
				tjList.clear();
				tjList.add(id);
				tjList.add(false);
				//不能删除的权限
				List<Long> notDeleteList=Table.findSpecifiedList(RoleResource.class, "role_id=? and is_allow_delete=?", tjList, "resourceId");
				Set<Long> notDeleteSet=new HashSet<Long>();
				for(Long notDeleteResId:notDeleteList) {
					notDeleteSet.add(notDeleteResId);
				}
				if(resourceIdList!=null) {
					for(Long resId:resourceIdList) {
						if(!notDeleteSet.contains(resId)) {
							newAddResIdList.add(resId);
						}
					}
				}
			}else{
				newAddResIdList=resourceIdList;
			}
			//
			if(newAddResIdList!=null && newAddResIdList.size()>0) {
				for(Long newResId:newAddResIdList) {
					RoleResource objRR=new RoleResource();
					Resource objR=new Resource();
					objR.setId(newResId);
					objRR.setRoleId(objRole);
					objRR.setResourceId(objR);
					objRR.setIsAllowDelete(true);		//允许删除
					//
					saveList.add(objRR);
				}
				Table.save(saveList);
				SystemUtil.refreshResource_async();
			}
		}
	}
}
