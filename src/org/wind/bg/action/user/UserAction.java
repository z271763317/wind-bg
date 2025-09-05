package org.wind.bg.action.user;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.wind.bg.action.parent.ModuleAction;
import org.wind.bg.annotation.An_Add;
import org.wind.bg.annotation.An_Update;
import org.wind.bg.model.Role;
import org.wind.bg.model.User;
import org.wind.bg.model.UserRole;
import org.wind.bg.util.EncryptUtil;
import org.wind.bg.util.ObjectUtil;
import org.wind.bg.util.RegexUtil;
import org.wind.bg.util.ValidateUtil;
import org.wind.bg.util.system.SessionUtil;
import org.wind.bg.util.system.SystemUtil;
import org.wind.mvc.annotation.controller.An_Controller;
import org.wind.mvc.annotation.controller.An_URL;
import org.wind.orm.Table;
import org.wind.orm.bean.Page;

/**
 * @描述 : 用户管理
 * @作者 : 胡璐璐
 * @时间 : 2021年4月8日 21:36:49
 */
@An_Controller(value="/user/system/user",name="用户管理")
public class UserAction extends ModuleAction<User>{

	/**数据列表**/
	@An_URL("/list")
	public Map<Object,Object> list(HttpServletRequest request,Integer page,Integer limit,String name,String userName,Integer status,String startTime,String endTime) {
		name=RegexUtil.clearEmpty(name);
		userName=RegexUtil.clearEmpty(userName);
		startTime=RegexUtil.clearEmpty(startTime);
		endTime=RegexUtil.clearEmpty(endTime);
		//
		User objUser_curr=Table.findById(User.class, SessionUtil.userId(request), "type");
		Integer type_curr=objUser_curr.getType();
		//
		Page pageObj=new Page(SystemUtil.getPage(page), SystemUtil.getLimit(limit));
		StringBuffer tjSQL=new StringBuffer("type>?");
		List<Object> tjList=new ArrayList<Object>();
		tjList.add(type_curr);
		
		//用户名
		if(userName!=null) {
			tjSQL.append(" and user_name like ?");
			tjList.add("%"+userName+"%");
		}
		//名称
		if(name!=null) {
			tjSQL.append(" and name like ?");
			tjList.add("%"+name+"%");
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
		List<User> list=Table.find(this.tableClass, tjSQL.toString(), tjList, true, pageObj,null,null,"status","userName","createTime","name","type","isAllowDelete");
		long size=Table.findSize(this.tableClass, tjSQL.toString(), tjList);
		//
		Map<Object,Object> resultMap=new HashMap<Object, Object>();
		resultMap.put("data",ObjectUtil.objectListConvertList(list));
		resultMap.put("count", size);
		return resultMap;
	}
	/**添加UI**/
	@An_URL("/add")@An_Add
	public String add(HttpServletRequest request) {
		User objUser_curr=Table.findById(User.class, SessionUtil.userId(request), "type");
		Integer type_curr=objUser_curr.getType();
		request.setAttribute("userTypeMap", SystemUtil.getUserTypeCanMap(type_curr));
		return "edit";
	}
	/**更新UI**/
	@An_URL("/update")@An_Update
	public String update(HttpServletRequest request,Object id) {
		User objUser_curr=Table.findById(User.class, SessionUtil.userId(request), "type");
		Integer type_curr=objUser_curr.getType();
		request.setAttribute("obj", Table.findById(this.tableClass, id,"userName","createTime","name","type","isAllowDelete"));
		request.setAttribute("userTypeMap", SystemUtil.getUserTypeCanMap(type_curr));
		return "edit";
	}
	/**保存**/
	@An_URL("/save")
	public void save(HttpServletRequest request,User obj) throws Exception {
		if(obj!=null) {
			String userName=obj.getUserName();
			String passWord=obj.getPassWord();
			String name=obj.getName();
			userName=RegexUtil.clearEmpty(userName);
			passWord=RegexUtil.clearEmpty(passWord);
			name=RegexUtil.clearEmpty(name);
			if(passWord!=null) {
				if(passWord.length()<8) {
					throw new IllegalArgumentException("密码长度不能少于8位数");
				}
				String passWord_encrypt=EncryptUtil.getMD5(passWord);
				obj.setPassWord(passWord_encrypt);
			}else{
				obj.setPassWord(null);
			}
			//
			obj.setUserName(userName);
			obj.setName(name);
			//
			Long id=obj.getId();		//用来判断是否更新
			Integer type=obj.getType();
			
			//当前用户
			User obj_curr=Table.findById(this.tableClass, SessionUtil.userId(request), "type");
			Integer type_curr=obj_curr.getType();
			//更新
			if(id!=null) {
				obj.setUserName(null);		//不允许更改用户名
				//
				this.isAllowModify(request, id);
			//添加
			}else{
				ValidateUtil.notEmpty(userName, "未填写【用户名】");
				ValidateUtil.notEmpty(passWord, "未填写【密码】");
				ValidateUtil.notEmpty(type, "没有选择【类型】");
				ValidateUtil.notEmpty(name, "未填写【名称】");
				//
				User obj_source=Table.findOne(this.tableClass, "user_name=?", new Object[] {userName}, false, "id");
				if(obj_source!=null) {
					throw new IllegalArgumentException("您填写的【用户名】已存在");
				}
			}
			if(!SystemUtil.isAllowHandle(type_curr, type)) {
				throw new IllegalArgumentException("您无权设置该类型的用户");
			}
			//
			obj.save();
		}else{
			throw new IllegalArgumentException("未填选任何数据");
		}
	}
	/**批量启用**/
	@An_URL("/enableSelect")
	public void enableSelect(HttpServletRequest request,List<Long> idList) {
		List<Long> opIdList=getAllowModifyIdList(request, idList);
		super.enableSelect(tableClass, opIdList);
	}
	/**批量禁用**/
	@An_URL("/disableSelect")
	public void disableSelect(HttpServletRequest request,List<Long> idList) {
		List<Long> opIdList=getAllowModifyIdList(request, idList);
		super.disableSelect(tableClass, opIdList);
	}
	
	/**********角色***********/
	/**角色UI**/
	@An_URL("/role")
	public String role() {
		return "role.html";
	}
	/**角色数据**/
	@An_URL("/roleData")
	public Map<Object,Object> roleData(HttpServletRequest request,Long userId) {
		Map<Object,Object> resultMap=new HashMap<Object, Object>();
		if(userId!=null) {
			List<Role> list=Table.findAll(Role.class, false);
			List<Object> tjList=new ArrayList<Object>();
			tjList.add(userId);
			List<Object> roleIdList=Table.findSpecifiedList(UserRole.class, "user_id=?", tjList, "roleId");
			//
			resultMap.put("data",ObjectUtil.objectListConvertList(list));
			resultMap.put("count", list.size());
			resultMap.put("roleIdList", roleIdList);
		}
		return resultMap;
	}
	/**角色保存**/
	@An_URL("/roleSave")
	public void roleSave(HttpServletRequest request,Long userId,List<Long> roleIdList) {
		this.isAllowModify(request, userId);
		/*先删除原数据*/
		List<Object> tjList=new ArrayList<Object>();
		tjList.add(userId);
		Table.delete(UserRole.class, "user_id=?", tjList);
		/*再保存新的*/
		if(roleIdList!=null) {
			User objUser=new User();
			objUser.setId(userId);
			List<UserRole> urList=new ArrayList<UserRole>();
			for(Long roleId:roleIdList) {
				UserRole ur=new UserRole();
				Role objRole=new Role();
				objRole.setId(roleId);
				ur.setUserId(objUser);
				ur.setRoleId(objRole);
				//
				urList.add(ur);
			}
			Table.save(urList);
		}
	}
	
	
	/*************本地方法*************/
	/**是否允许修改**/
	private void isAllowModify(HttpServletRequest request,Long id) {
		Long userId=SessionUtil.userId(request);
		if(userId.equals(id)) {
			throw new IllegalArgumentException("自己的内容不可以修改");
		}
		User obj_source=Table.findById(this.tableClass, id,"type");
		/*当前用户数据。判断是否有权修改*/
		if(obj_source!=null) {
			Integer type_source=obj_source.getType();
			if(type_source==SystemUtil.superAdminType ) {
				throw new IllegalArgumentException("【超级管理员】不允许修改");
			}
			//当前用户
			User obj_curr=Table.findById(this.tableClass, userId, "type");
			Integer type_curr=obj_curr.getType();
			if(!SystemUtil.isAllowHandle(type_curr, type_source)) {
				throw new IllegalArgumentException("您无权修改该用户的数据");
			}
		}
	}
	/**获取 : 可以修改数据的ID列表**/
	private List<Long> getAllowModifyIdList(HttpServletRequest request,List<Long> idList) {
		Long userId=SessionUtil.userId(request);
		List<User> list=Table.findByIdList(this.tableClass, idList,"type");
		List<Long> opIdList=new ArrayList<Long>();
		if(list!=null && list.size()>0) {
			User obj_curr=Table.findById(this.tableClass, userId, "type");
			Integer type_curr=obj_curr.getType();
			//
			for(User obj:list) {
				Integer type=obj.getType();
				//自己的不能操作
				if(!obj_curr.getId().equals(obj.getId()) && SystemUtil.isAllowHandle(type_curr, type)){
					opIdList.add(obj.getId());
				}
			}
		}
		return opIdList;
	}
}