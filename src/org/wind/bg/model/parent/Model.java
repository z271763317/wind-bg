package org.wind.bg.model.parent;

import org.wind.orm.Table;
import org.wind.orm.annotation.Column;
import org.wind.orm.annotation.DateTime;

/**
 * @param <T>
 * @描述 : 实体类的顶级父类
 * @版权 : 胡璐璐
 * @时间 : 2019年9月5日 16:54:42
 */
public abstract class Model extends Table{
	
	@Column("create_time")@DateTime("yyyy-MM-dd HH:mm:ss")
	protected String createTime;		//创建时间
	@Column("update_time")@DateTime("yyyy-MM-dd HH:mm:ss")
	protected String updateTime;		//更新时间
	protected Integer status;		//状态（0=禁用；1=启用；[2=未审核]）

	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}
	
}