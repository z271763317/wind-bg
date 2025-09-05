package org.wind.bg.model;

import org.wind.bg.model.parent.Model_Long;
import org.wind.orm.annotation.Column;
import org.wind.orm.annotation.Tables;

/**
 * @描述 : 角色表
 * @作者 : 胡璐璐
 * @时间 : 2021年04月06日 18:52:35
 */
@Tables("role")
public class Role extends Model_Long{

	private String code;		//代码
	private String name;		//名称
	private String describe;		//描述
	@Column("is_allow_delete")
	private Boolean isAllowDelete;		//是否允许删除
	
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescribe() {
		return describe;
	}
	public void setDescribe(String describe) {
		this.describe = describe;
	}
	public Boolean getIsAllowDelete() {
		return isAllowDelete;
	}
	public void setIsAllowDelete(Boolean isAllowDelete) {
		this.isAllowDelete = isAllowDelete;
	}

}