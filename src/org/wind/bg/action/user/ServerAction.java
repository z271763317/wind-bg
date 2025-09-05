package org.wind.bg.action.user;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.wind.bg.action.parent.ModuleAction;
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
 * @描述 : 所有系统信息
 * @作者 : 胡璐璐
 * @时间 : 2021年4月8日 21:36:49
 */
@An_Controller(value="/user/system/server",name="所有系统信息")
public class ServerAction extends ModuleAction<Server>{
	
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
		List<Server> list=Table.find(this.tableClass, tjSQL.toString(), tjList, true, pageObj);
		long size=Table.findSize(this.tableClass, tjSQL.toString(), tjList);
		//
		Map<Object,Object> resultMap=new HashMap<Object, Object>();
		resultMap.put("data",ObjectUtil.objectListConvertList(list));
		resultMap.put("count", size);
		return resultMap;
	}
	/**保存**/
	@An_URL("/save")
	public void save(HttpServletRequest request,Server obj) throws Exception {
		if(obj!=null) {
			String code=obj.getCode();
			if(code==null) {
				throw new IllegalArgumentException("未填写【代码】");
			}
			this.isAllowHandle(code);
			//
			Long id=obj.getId();
			StringBuffer tjSQL=new StringBuffer("code=?");
			List<Object> tjList=new ArrayList<Object>();
			tjList.add(code);
			if(id!=null) {
				tjSQL.append(" and id!=?");
				tjList.add(id);
				//
				isAllowHandle(id);
			}
			Server obj_source=Table.findOne(this.tableClass, tjSQL.toString(), tjList, false, "id");
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
	public void delete(Long id) {
		if(id==null) {
			throw new IllegalArgumentException("没有指定项");
		}
		this.isAllowHandle(id);
		Resource objRes=Table.findOne(Resource.class, "server_id=?", new Object[] {id}, false, "id","name");
		if(objRes!=null) {
			throw new IllegalArgumentException("有绑定权限【"+objRes.getName()+"】，请先解除");
		}
		super.delete(this.tableClass, id);
	}
	/**批量启用**/
	@An_URL("/enableSelect")
	public void enableSelect(List<Long> idList) {
		super.enableSelect(tableClass, idList);
	}
	/**批量下线**/
	@An_URL("/disableSelect")
	public void disableSelect(List<Long> idList) {
		List<Long> opIdList=this.getAllowHandleIdList(idList);
		super.disableSelect(tableClass, opIdList);
	}
	
	/*************本地方法*************/
	/**是否允许操作**/
	private boolean isAllowHandle(String systeCode) {
		if(SystemUtil.isMainSystem(systeCode)) {
			throw new IllegalArgumentException("不能操作该系统的数据");
		}
		return true;
	}
	/**是否允许操作（并返回指定id的对象）**/
	private Server isAllowHandle(Long id) {
		Server obj=Table.findById(this.tableClass, id, "code");
		if(obj!=null) {
			this.isAllowHandle(obj.getCode());
			return obj;
		}else{
			throw new IllegalArgumentException("指定的项不存在");
		}
	}
	/**获取 : 可以操作的ID列表**/
	private List<Long> getAllowHandleIdList(List<Long> idList) {
		List<Server> list=Table.findByIdList(this.tableClass, idList,"code");
		List<Long> opIdList=new ArrayList<Long>();
		if(list!=null && list.size()>0) {
			for(Server obj:list) {
				String code=obj.getCode();
				if(!SystemUtil.isMainSystem(code)){
					opIdList.add(obj.getId());
				}
			}
		}
		return opIdList;
	}
}
