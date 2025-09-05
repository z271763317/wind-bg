package org.wind.bg.model;

import org.wind.bg.model.parent.Model_Long;
import org.wind.orm.annotation.Column;
import org.wind.orm.annotation.Tables;

/**
 * @描述 : 用户表
 * @作者 : 胡璐璐
 * @时间 : 2020年5月21日 18:03:55
 */
@Tables("user")
public class User extends Model_Long{

	/*****************状态*****************/
	/**禁用**/
	public static final int status_failure=0;		
	/**正常**/
	public static final int status_normal=1;
	/**冻结**/
	public static final int status_freeze=2;
	
	@Column("user_name")
	private String userName;		//用户名
	@Column("pass_word")
	private String passWord;		//密码
	@Column("name")
	private String name;			//用户名称
	private Integer type;			//用户类型（0=超级管理员；1=管理员；2=普通用户）
	@Column("is_allow_delete")
	private Boolean isAllowDelete;		//是否允许删除
	
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPassWord() {
		return passWord;
	}
	public void setPassWord(String passWord) {
		this.passWord = passWord;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public Boolean getIsAllowDelete() {
		return isAllowDelete;
	}
	public void setIsAllowDelete(Boolean isAllowDelete) {
		this.isAllowDelete = isAllowDelete;
	}
	
}
