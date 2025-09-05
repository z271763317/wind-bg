package org.wind.bg.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.wind.bg.annotation.An_Time;
import org.wind.bg.config.cache.Cache;
import org.wind.bg.model.parent.Model_Long;
import org.wind.orm.Table;
import org.wind.orm.annotation.Column;
import org.wind.orm.annotation.Tables;

/**
 * @描述 : 系统表（可用作权限的URL前缀拼接）
 * @作者 : 胡璐璐
 * @时间 : 2021年04月06日 19:20:41
 */
@An_Time(period=2*60*1000)
@Tables("server")
public class Server extends Model_Long{

	private static Map<Long,Server> map=new LinkedHashMap<Long,Server>();
	private static Map<String,Long> mapCode=new HashMap<String, Long>();
	
	private String name;		//名称
	private String code;		//代码
	private String protocol;		//网络协议（如：http://）
	private String domain;		//外网访问链接（域名）
	private String server;		//服务端名（系统、项目名）
	@Column("intranet_ip")
	private String intranetIp;		//内网IP
	private Integer port;		//端口
	
	public static void init() throws Exception{
		List<Server> list=Table.find(Server.class, "status="+Cache.modelStatus_1, null,false,null);
		Map<Long,Server> t_map=new LinkedHashMap<Long, Server>();
		Map<String,Long> t_mapCode=new LinkedHashMap<String, Long>();
		for(Server obj:list) {
			t_map.put(obj.getId(), obj);
			t_mapCode.put(obj.getCode(), obj.getId());
		}
		map=t_map;
		mapCode=t_mapCode;
	}
	/**获取**/
	public static Server get(Long id) {
		return map.get(id);
	}
	/**获取（根据code）**/
	public static Server get(String code) {
		Long t_id=getId(code);
		if(t_id!=null) {
			return get(t_id);
		}
		return null;
	}
	/**获取 : 主键（根据代码）**/
	public static Long getId(String code) {
		return mapCode.get(code);
	}
	/**获取 : 所有**/
	public static List<Server> getAll() {
		return new ArrayList<Server>(map.values());	
	}
	
	/**获取 : 外网访问地址（主键式）**/
	public static String getNetwork(Long id){
		return getNetwork(map.get(id));
	}
	/**获取 : 外网访问地址（对象式）**/
	public static String getNetwork(Server obj){
		if(obj!=null){
			String t_domain=obj.getDomain();
			t_domain=t_domain!=null && t_domain.trim().length()>0?t_domain.trim():null;
			if(t_domain!=null){
				/*去除同一层的多个【/】符号为1个*/
				String t_url=obj.getDomain()+"/"+obj.getServer()+"/";
				//如果不是
				if(t_url!=null){
					t_url=t_url.replaceAll("//", "/");
				}
				t_url=obj.getProtocol()+t_url;
				return t_url;
			}
		}
		return "";
	}
	/**获取 : 内部访问地址（主键式）**/
	public static String getInternal(Long id){
		return getInternal(map.get(id));
	}
	/**获取 : 内部访问地址（对象式）**/
	public static String getInternal(Server obj){
		if(obj!=null) {
			String t_server=obj.getServer()!=null && obj.getServer().trim().length()>0?obj.getServer().trim()+"/":"";
			return obj.getProtocol()+obj.getIntranetIp()+":"+obj.getPort()+"/"+t_server;
		}
		return "";
	}
	
	public String getName() {
		return name;
	}	
	public void setName(String name) {
		this.name = name;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getProtocol() {
		return protocol;
	}
	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}
	public String getDomain() {
		return domain;
	}
	public void setDomain(String domain) {
		this.domain = domain;
	}
	public String getServer() {
		return server;
	}
	public void setServer(String server) {
		this.server = server;
	}
	public String getIntranetIp() {
		return intranetIp;
	}
	public void setIntranetIp(String intranetIp) {
		this.intranetIp = intranetIp;
	}
	public Integer getPort() {
		return port;
	}
	public void setPort(Integer port) {
		this.port = port;
	}
}