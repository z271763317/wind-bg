package org.wind.bg.model;

import org.wind.bg.model.parent.Model_Long;
import org.wind.orm.annotation.Column;
import org.wind.orm.annotation.ForeignKey;
import org.wind.orm.annotation.Tables;

/**
 * @描述 : 用户角色表
 * @作者 : 胡璐璐
 * @时间 : 2021年04月06日 18:52:35
 */
@Tables("user_role")
public class UserRole extends Model_Long{

	@Column("user_id")@ForeignKey
	private User userId;		//用户ID
	@Column("role_id")@ForeignKey
	private Role roleId;		//角色ID
	@Column("is_allow_delete")
	private Boolean isAllowDelete;		//是否允许删除
	
	public User getUserId() {
		return userId;
	}
	public void setUserId(User userId) {
		this.userId = userId;
	}
	public Role getRoleId() {
		return roleId;
	}
	public void setRoleId(Role roleId) {
		this.roleId = roleId;
	}

}