package org.wind.bg.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.wind.bg.annotation.An_Time;
import org.wind.bg.model.parent.Model_Long;
import org.wind.orm.Table;
import org.wind.orm.annotation.Column;
import org.wind.orm.annotation.ForeignKey;
import org.wind.orm.annotation.Tables;

/**
 * @描述 : 角色权限表
 * @作者 : 胡璐璐
 * @时间 : 2021年04月06日 18:52:35
 */
@An_Time(period=2*60*1000)
@Tables("role_resource")
public class RoleResource extends Model_Long{

	private static Map<Long,List<Long>> roleToResMap=new HashMap<Long,List<Long>>();
	
	@Column("role_id")@ForeignKey
	private Role roleId;		//角色ID
	@Column("resource_id")@ForeignKey
	private Resource resourceId;		//权限ID
	@Column("is_allow_delete")
	private Boolean isAllowDelete;		//是否允许删除
	
	public static void init() throws Exception{
		String sql="select t_rr.role_id as 'roleId',t.id as 'resId' from resource t INNER JOIN role_resource t_rr on t.id=t_rr.resource_id ORDER BY t.serial_number";
		List<Map<String, Object>> list=Table.find(RoleResource.class, sql, null);
		Map<Long,List<Long>> t_roleToResMap=new HashMap<Long,List<Long>>();
		for(Map<String, Object> obj:list) {
			Long roleId=Long.parseLong(obj.get("roleId").toString());
			Long resId=Long.parseLong(obj.get("resId").toString());
			List<Long> resIdList=t_roleToResMap.get(roleId);
			if(resIdList==null) {
				resIdList=new ArrayList<Long>();
				t_roleToResMap.put(roleId, resIdList);
			}
			resIdList.add(resId);
		}
		roleToResMap=t_roleToResMap;
	}
	/**获取 : 权限列表（根据roleId）**/
	public static List<Long> get(Long roleId) {
		return roleToResMap.get(roleId);
	}
	/**获取 : 权限列表（根据roleId列表）**/
	public static Set<Long> get(List<Long> roleIdList) {
		Set<Long> resIdSet=new LinkedHashSet<Long>();
		for(Object roleId:roleIdList) {
			List<Long> resIdList=roleToResMap.get(roleId);
			if(resIdList!=null && resIdList.size()>0) {
				for(Long resId:resIdList) {
					resIdSet.add(resId);
				}
			}
		}
		return resIdSet;
	}
	
	public Role getRoleId() {
		return roleId;
	}
	public void setRoleId(Role roleId) {
		this.roleId = roleId;
	}
	public Resource getResourceId() {
		return resourceId;
	}
	public void setResourceId(Resource resourceId) {
		this.resourceId = resourceId;
	}
	public Boolean getIsAllowDelete() {
		return isAllowDelete;
	}
	public void setIsAllowDelete(Boolean isAllowDelete) {
		this.isAllowDelete = isAllowDelete;
	}

}