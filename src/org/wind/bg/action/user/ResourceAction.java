package org.wind.bg.action.user;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.wind.bg.action.parent.ModuleAction;
import org.wind.bg.annotation.An_Add;
import org.wind.bg.annotation.An_Update;
import org.wind.bg.model.Resource;
import org.wind.bg.model.Server;
import org.wind.bg.util.ObjectUtil;
import org.wind.bg.util.RegexUtil;
import org.wind.bg.util.system.SystemUtil;
import org.wind.mvc.annotation.controller.An_Controller;
import org.wind.mvc.annotation.controller.An_URL;
import org.wind.orm.Table;
import org.wind.orm.bean.Page;
import org.wind.orm.util.TableUtil;

/**
 * @描述 : 权限管理
 * @作者 : 胡璐璐
 * @时间 : 2021年4月8日 21:36:49
 */
@An_Controller(value="/user/system/resource",name="权限管理")
public class ResourceAction extends ModuleAction<Resource>{

	/**数据列表**/
	@An_URL("/list")
	public Map<Object,Object> list(Integer page,Integer limit,String name,String code,Integer status,String startTime,String endTime,Long parentId,Long serverId,Boolean isMainMenu) {
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
		//是否主菜单
		if(isMainMenu==null) {
			//上一级权限
			if(parentId!=null) {
				tjSQL.append(" and parent_id=?");
				tjList.add(parentId);
			}
		}else{
			//主菜单
			if(isMainMenu) {
				tjSQL.append(" and parent_id is null");
			//非主菜单
			}else{
				tjSQL.append(" and parent_id is not null");
			}
		}
		//所属系统
		if(serverId!=null) {
			tjSQL.append(" and server_id=?");
			tjList.add(serverId);
		}
		List<Resource> list=Table.find(Resource.class, tjSQL.toString(), tjList, true, pageObj);
		long size=Table.findSize(Resource.class, tjSQL.toString(), tjList);
		//
		Map<Object,Object> resultMap=new HashMap<Object, Object>();
		resultMap.put("data",ObjectUtil.objectListConvertList(list));
		resultMap.put("count", size);
		return resultMap;
	}
	/**添加UI（若有指定id，则是在此之上做添加新的）**/
	@An_URL("/add")@An_Add
	public String add(HttpServletRequest request,Long id) {
		//添加子权限
		if(id!=null) {
			Table obj=Table.findById(this.tableClass, id);
			if(obj!=null) {
				Table.findOneForeign(obj, "serverId", "name");
				request.setAttribute("obj", obj);
				return "edit_child";
			}else{
				throw new IllegalArgumentException("指定的项不存在");
			}
		}else{
			return "edit";
		}
	}
	/**更新UI**/
	@An_URL("/update")@An_Update
	public String update(HttpServletRequest request,Long id) {
		Table obj=Table.findById(this.tableClass, id);
		Table.findOneForeign(obj, "parentId", "name");
		Table.findOneForeign(obj, "serverId", "name");
		request.setAttribute("obj", obj);
		return "edit";
	}
	/**保存**/
	@An_URL("/save")
	public void save(HttpServletRequest request,Resource obj) throws Exception {
		if(obj!=null) {
			String code=obj.getCode();
			if(code==null) {
				throw new IllegalArgumentException("未填写【代码】");
			}
			Server objServer=obj.getServerId();
			if(objServer==null || objServer.getId()==null) {
				throw new IllegalArgumentException("未选择【所属系统】");
			}
			Long id=obj.getId();
			StringBuffer tjSQL=new StringBuffer("code=?");
			List<Object> tjList=new ArrayList<Object>();
			tjList.add(code);
			//更新
			if(id!=null) {
				tjSQL.append(" and id!=?");
				tjList.add(id);
				//
				List<Object> tjList_clear=new ArrayList<Object>();
				tjList_clear.add(id);
				//清除parent_id、server_id
				Resource objParent=obj.getParentId();
				StringBuffer setSQL=new StringBuffer();
				if(objParent==null || objParent.getId()==null) {
					setSQL.append(",parent_id=null");
				}
				if(setSQL.length()>0) {
					String setSQL_set=setSQL.substring(1);
					Table.update(this.tableClass, setSQL_set, "id=?", tjList_clear);
				}
			}
			Resource obj_source=Table.findOne(Resource.class, tjSQL.toString(), tjList, false, "id");
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
		Resource obj=Table.findById(tableClass, id, "isAllowDelete");
		if(obj!=null && obj.getIsAllowDelete()) {
			super.delete(tableClass, id);
			List<Object> conditionsList=new ArrayList<Object>();
			conditionsList.add(id);
			Table.delete(Resource.class, "parent_id=?", conditionsList);
		}else{
			throw new IllegalArgumentException("不允许删除");
		}
	}
	/**批量删除**/
	@An_URL("/deleteSelect")
	public void deleteSelect(List<Long> idList) {
		List<Resource> list=Table.findByIdList(tableClass, idList,"isAllowDelete");
		List<Object> opIdList=new ArrayList<Object>();
		for(Resource obj:list) {
			Boolean isAllowDelete=obj.getIsAllowDelete();
			if(isAllowDelete) {
				opIdList.add(obj.getId());
			}
		}
		if(opIdList.size()>0) {
			super.deleteSelect(tableClass, opIdList);
			Table.delete(Resource.class, "parent_id in("+TableUtil.getPlaceholder(opIdList)+")", opIdList);
		}
	}
	/**批量启用**/
	@An_URL("/enableSelect")
	public void enableSelect(List<Long> idList) {
		super.enableSelect(tableClass, idList);
	}
	/**批量禁用**/
	@An_URL("/disableSelect")
	public void disableSelect(List<Long> idList) {
		List<Resource> list=Table.findByIdList(tableClass, idList, "isAllowDelete");
		List<Object> opIdList=new ArrayList<Object>();
		for(Resource obj:list) {
			Boolean isAllowDelete=obj.getIsAllowDelete();
			if(isAllowDelete) {
				opIdList.add(obj.getId());
			}
		}
		super.disableSelect(tableClass, opIdList);
	}
	/**刷新（缓存里的权限）**/
	@An_URL("/refresh")
	public void refresh() throws Exception {
		SystemUtil.refreshResource();
	}
}
