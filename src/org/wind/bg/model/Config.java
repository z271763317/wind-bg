package org.wind.bg.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.wind.bg.annotation.An_Time;
import org.wind.bg.model.parent.Model_Long;
import org.wind.orm.Table;
import org.wind.orm.annotation.Tables;

/**
 * @描述 : 配置表
 * @作者 : 胡璐璐
 * @时间 : 2021年04月18日 16:12:11
 */
@An_Time(period=2*60*1000)
@Tables("config")
public class Config extends Model_Long{

	/**************************code列表**********************/
	public static final String systemName="systemName";		//本系统名称
	public static final String systemNameFontSize="systemNameFontSize";		//本系统名称字体大小
	public static final String systemIndexUrl="systemIndexUrl";		//本系统首页URL
	
	 /******************以下是本Model的信息****************/
	private static Map<String,String> map=new HashMap<String, String>();
	
	private String code;		//代码
	private String name;		//名称
	private String prefix;		//前缀
	private String value;		//配置值
	private String suffix;		//后缀
	
	//初始化
	public synchronized static void init(){
		List<Config> list=Table.findAll(Config.class,false);
		Map<String,String> t_map=new HashMap<String, String>();
		for(Config t_config:list){
			t_map.put(t_config.getCode(), t_config.getPrefix()+t_config.getValue()+t_config.getSuffix());
		}
		map=t_map;
	}
	/**
	 * 获取 : 配置值（不存在则为空字符串）
	 * @param code : 代码
	 */
	public static String get(String code){
		return map.get(code);
	}
	
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
	public String getPrefix() {
		return prefix;
	}
	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getSuffix() {
		return suffix;
	}
	public void setSuffix(String suffix) {
		this.suffix = suffix;
	}

}