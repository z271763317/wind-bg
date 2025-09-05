package org.wind.bg.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import org.wind.bg.annotation.An_Time;
import org.wind.bg.model.parent.Model_Long;
import org.wind.orm.Table;
import org.wind.orm.annotation.Column;
import org.wind.orm.annotation.ForeignKey;
import org.wind.orm.annotation.Tables;

/**
 * @描述 : 权限表
 * @作者 : 胡璐璐
 * @时间 : 2021年04月06日 18:52:35
 */
@SuppressWarnings("unchecked")
@An_Time(period=2*60*1000)
@Tables("resource")
public class Resource extends Model_Long{

	private static Map<Long,Resource> objMap=new LinkedHashMap<Long,Resource>();
	
	@Column("parent_id")@ForeignKey
	private Resource parentId;		//所属权限ID
	private String code;		//代码
	private String name;		//名称
	private String url;		//访问URL（相对路径）
	private String path;		//图标路径
	@Column("server_id")@ForeignKey
	private Server serverId;		//关联的系统ID（取对应ID系统的url，做菜单拼接链接的前缀用）
	@Column("serial_number")
	private Integer serialNumber;		//顺序
	private String describe;		//描述
	@Column("is_allow_delete")
	private Boolean isAllowDelete;		//是否允许删除
	
	public static void init() throws Exception{
		List<Resource> list=Table.find(Resource.class, "status=1", null, false, null,"serialNumber",null);
		Map<Long,Resource> t_objMap=new LinkedHashMap<Long,Resource>();
		for(Resource obj:list) {
			t_objMap.put(obj.getId(), obj);
		}
		objMap=t_objMap;
	}
	/**获取**/
	public static Resource get(Long id) {
		return objMap.get(id);
	}
	/**获取 : 所有结构化的数据（isCache=是否取缓存的）**/
	public static Map<Long,Map<Object,Object>> getAllStructured( ){
		Map<Long,Map<Object,Object>> lastAllStructuredMap_orderBefore=new HashMap<Long, Map<Object,Object>>();		//最后一次的所有结构化数据（key=主菜单ID），排序前
		for(Resource obj:objMap.values()) {
			put(null,obj.getId(), lastAllStructuredMap_orderBefore, null,false);
		}
		Map<Integer,Map<Long,Map<Object,Object>>> lastAllStructuredMap_order=new TreeMap<Integer,Map<Long,Map<Object,Object>>>();		//排序
		for(Entry<Long,Map<Object,Object>> entry:lastAllStructuredMap_orderBefore.entrySet()) {
			Long t_key=entry.getKey();
			Map<Object,Object> t_value=entry.getValue();
			Integer t_order=(Integer) t_value.get("_order");
			t_order=t_order!=null?t_order:0;		//为空则默认0
			Map<Long,Map<Object,Object>> t_map=lastAllStructuredMap_order.get(t_order);
			if(t_map==null) {
				t_map=new LinkedHashMap<Long, Map<Object,Object>>();
				lastAllStructuredMap_order.put(t_order, t_map);
			}
			t_map.put(t_key, t_value);
		}
		Map<Long,Map<Object,Object>> lastAllStructuredMap=new LinkedHashMap<Long, Map<Object,Object>>();		//最后一次的所有结构化数据（key=主菜单ID），排序后
		for(Map<Long,Map<Object,Object>> t_map:lastAllStructuredMap_order.values()) {
			lastAllStructuredMap.putAll(t_map);
		}
		return lastAllStructuredMap;
	}
	/**
	 * 放入 : 指定id的完全型结构化数据到lastAllStructuredMap里，将形成的主菜单放入mainSystem里（子菜单不需要）
	 * @param serverId : 指定系统ID（没有就查全部，不判断）
	 * @param id : 当前菜单或父菜单的ID
	 * @param lastAllStructuredMap : 最后一次的所有结构化数据
	 * @param navMenuList : 导航下的菜单数据
	 * @param isLimit : 是否限制条件
	 * @return
	 */
	public static void put(Long serverId,Long id,Map<Long,Map<Object,Object>> lastAllStructuredMap,List<Map<Object,Object>> navMenuList,boolean isLimit) {
		Map<Object,Object> map=Resource.get(serverId,id, null,isLimit);		//主菜单
		if(map!=null) {
			Long menuId=(Long) map.get("id");		//主菜单ID
			Map<Object,Object> structuredMap=lastAllStructuredMap.get(menuId);		//主菜单（结构化）
			//已放入过该主菜单
			if(structuredMap!=null) {
				List<Map<Object,Object>> children=(List<Map<Object, Object>>) map.get("children");		//主菜单_子菜单列表
				//有子菜单
				if(children!=null && children.size()>0) {
					Map<Object,Object> childMap=children.get(0);		//主菜单_子菜单
					//主菜单（结构化）_子菜单列表
					List<Map<Object,Object>> structuredChildren=(List<Map<Object, Object>>) structuredMap.get("children");	
					putChild(childMap, structuredChildren);
				}
			//未放过
			}else{
				lastAllStructuredMap.put(menuId,map);
				if(navMenuList!=null) {
					navMenuList.add(map);
				}
			}
		}
	}
	
	//放入 : 子菜单结构化数据（map=当前层菜单；structuredChildren=上一层的子菜单列表（结构化））
	private static void putChild(Map<Object,Object> map,List<Map<Object,Object>> beforeStructuredChildren) {
		//有内容才需要判断
		if(beforeStructuredChildren.size()>0) {
			Object menuId=map.get("id");			//当前层菜单ID
			for(Map<Object,Object> t_map:beforeStructuredChildren) {
				Object structuredMenuId=t_map.get("id");		//当前菜单ID（结构化）
				//在上一层子菜单列表中存在id
				if(structuredMenuId.equals(menuId)) {
					List<Map<Object,Object>> children=(List<Map<Object, Object>>) map.get("children");		//当前层菜单_子菜单列表
					//有子菜单
					if(children!=null && children.size()>0) {
						//主菜单（结构化）_子菜单列表
						List<Map<Object,Object>> structuredChildren=(List<Map<Object, Object>>) t_map.get("children");	
						putChild(children.get(0), structuredChildren);
					}
					return;		//停止
				}
			}
		}
		beforeStructuredChildren.add(map);		//加入到上一层的子菜单列表里
		
	}
	/**
	 * 返回 : 深层次父菜单的结构化数据，没有父级则为当前层数据（最外层为最顶级父类，其下的层级子菜单在每个菜单层次下的children里）。id对应的数据不存在，则返回map（上一级菜单级的结构化数据，首次为null）
	 * @param serverId : 指定系统ID（没有就查全部，不判断）
	 * @param id : 当前菜单或父菜单的ID
	 * @param map : 上一级菜单级的结构化数据，首次为null
	 * @param isNotLimit : 是否限制条件
	 * @return
	 */
	public static Map<Object,Object> get(Long serverId,Long id,Map<Object,Object> map,boolean isLimit) {
		if(id!=null) {
			Resource obj=objMap.get(id);
			if(obj!=null) {
				String name=obj.getName();
				String url=obj.getUrl();
	//			String path=obj.getPath();
				Resource objParent=obj.getParentId();
				Server objServer=obj.getServerId();
				Long t_serverId=objServer!=null && objServer.getId()!=null?objServer.getId():null;
				//有指定系统ID
				if(serverId!=null) {
					//匹配
					if(serverId.equals(t_serverId)) {
						url=Server.getNetwork(t_serverId)+url;
					//不匹配
					}else{
						return map;
					}
				//未指定
				}else{
					if(t_serverId!=null) {
						url=Server.getNetwork(t_serverId)+url;
					}
				}
				List<Object> children=new ArrayList<Object>();
				if(map!=null) {
					children.add(map);
				}
				//取消斜杆
				if(url!=null && url.indexOf("/")==0) {
					url=url.substring(1);
				}
				
				//
				Map<Object,Object> t_map=new LinkedHashMap<Object, Object>();
				t_map.put("id", obj.getId());		//菜单ID
				t_map.put("title", name);		//名称
				t_map.put("icon", "");		//图标
				t_map.put("href", url);		//链接
				t_map.put("spread", false);		//是否展开
				t_map.put("children", children);		//子菜单（List结构）
				t_map.put("_order", obj.getSerialNumber());		//顺序
				//有父菜单
				if(objParent!=null && objParent.getId()!=null) {
					return get(serverId,objParent.getId(), t_map,isLimit);
				}else{
					return t_map;
				}
			}
		}
		return map;
	}
	/**放入 : 指定ID的深层次到最顶级的ID列表到layerIdList里**/
	public static void putLayerId(Long id,Set<Long> layerIdList){
		if(layerIdList!=null) {
			Resource obj=objMap.get(id);
			if(obj!=null) {
				layerIdList.add(id);
				Resource objParent=obj.getParentId();
				if(objParent!=null && objParent.getId()!=null) {
					putLayerId(objParent.getId(), layerIdList);
				}
			}
		}
	}
	
	public Resource getParentId() {
		return parentId;
	}
	public void setParentId(Resource parentId) {
		this.parentId = parentId;
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
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public Server getServerId() {
		return serverId;
	}
	public void setServerId(Server serverId) {
		this.serverId = serverId;
	}
	public Integer getSerialNumber() {
		return serialNumber;
	}
	public void setSerialNumber(Integer serialNumber) {
		this.serialNumber = serialNumber;
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