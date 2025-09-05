package org.wind.bg.action.user;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.wind.bg.action.parent.ModuleAction;
import org.wind.bg.model.Config;
import org.wind.bg.util.ObjectUtil;
import org.wind.bg.util.RegexUtil;
import org.wind.bg.util.system.SystemUtil;
import org.wind.mvc.annotation.controller.An_Controller;
import org.wind.mvc.annotation.controller.An_URL;
import org.wind.orm.Table;
import org.wind.orm.bean.Page;

/**
 * @描述 : 系统配置
 * @作者 : 胡璐璐
 * @时间 : 2021年4月18日 16:45:13
 */
@An_Controller(value="/user/system/config",name="系统配置")
public class ConfigAction extends ModuleAction<Config>{
	
	/**数据列表**/
	@An_URL("/list")
	public Map<Object,Object> list(Integer page,Integer limit,String name,String code,String startTime,String endTime) {
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
		List<Config> list=Table.find(this.tableClass, tjSQL.toString(), tjList, true, pageObj);
		long size=Table.findSize(this.tableClass, tjSQL.toString(), tjList);
		//
		Map<Object,Object> resultMap=new HashMap<Object, Object>();
		resultMap.put("data",ObjectUtil.objectListConvertList(list));
		resultMap.put("count", size);
		return resultMap;
	}
	/**保存**/
	@An_URL("/save")
	public void save(HttpServletRequest request,Config obj) throws Exception {
		if(obj!=null) {
			Long id=obj.getId();
			String code=obj.getCode();
			if(code==null) {
				throw new IllegalArgumentException("未填写【代码】");
			}
			if(id!=null) {
				Config t_obj=Table.findById(this.tableClass, id, "code");
				if(t_obj==null) {
					throw new IllegalArgumentException("指定项不存在");
				}
				String t_code_source=t_obj.getCode();
				//是系统配置
				if(SystemUtil.isSystemConfig(t_code_source)) {
					if(!t_code_source.equals(code)) {
						throw new IllegalArgumentException("当前数据是系统重要的配置，不允许更改代码");
					}
				//不是系统配置
				}else{
					this.isAllowHandle(code);
				}
			}else{
				this.isAllowHandle(code);
			}
			//
			StringBuffer tjSQL=new StringBuffer("code=?");
			List<Object> tjList=new ArrayList<Object>();
			tjList.add(code);
			if(id!=null) {
				tjSQL.append(" and id!=?");
				tjList.add(id);
			}
			Config obj_source=Table.findOne(this.tableClass, tjSQL.toString(), tjList, false, "id");
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
		super.delete(this.tableClass, id);
	}
	
	/*************本地方法*************/
	/**是否允许操作**/
	private boolean isAllowHandle(String systeCode) {
		if(SystemUtil.isSystemConfig(systeCode)) {
			throw new IllegalArgumentException("不能操作代码【"+systeCode+"】的数据");
		}
		return true;
	}
	/**是否允许操作（并返回指定id的对象）**/
	private Config isAllowHandle(Long id) {
		Config obj=Table.findById(this.tableClass, id, "code");
		if(obj!=null) {
			this.isAllowHandle(obj.getCode());
			return obj;
		}else{
			throw new IllegalArgumentException("指定的项不存在");
		}
	}
}
