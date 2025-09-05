package org.wind.bg.model.parent;

import org.wind.orm.annotation.Id;

/**
 * @描述 : 实体类的父类——数据（主键：长整型）
 * @版权 : 胡璐璐
 * @时间 : 2021年3月16日 17:04:59
 */
public class Model_Long extends Model{
	
	@Id
	protected Long id;		//主键

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
}