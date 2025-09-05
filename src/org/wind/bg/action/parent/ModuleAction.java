package org.wind.bg.action.parent;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.wind.bg.annotation.An_Add;
import org.wind.bg.annotation.An_Update;
import org.wind.bg.util.ParamUtil;
import org.wind.mvc.annotation.controller.An_URL;
import org.wind.orm.Table;
import org.wind.orm.util.TableUtil;

/**
 * @描述 : 模块父类——通用方法
 * @作者 : 胡璐璐
 * @时间 : 2021年4月8日 21:36:49
 */
@SuppressWarnings("unchecked")
public class ModuleAction<T extends Table> {
	
	/*当前泛型类*/
	protected final Class<T> tableClass;
	
	public ModuleAction() {
		Type t_objType=this.getClass().getGenericSuperclass();
		if(t_objType!=null && t_objType instanceof ParameterizedType) {
			tableClass=(Class<T>) ((ParameterizedType)t_objType).getActualTypeArguments()[0];
		}else {
			tableClass=null;
		}
	}
	
	
	/**首页**/
	@An_URL
	public String index() {
		return "index";
	}
	/**添加UI**/
	@An_URL("/add")@An_Add
	public String add(HttpServletRequest request) {
		return "edit";
	}
	/**更新UI**/
	@An_URL("/update")@An_Update
	public String update(HttpServletRequest request,Object id) {
		request.setAttribute("obj", Table.findById(this.tableClass, id));
		return "edit";
	}
	
	/****************静态方法***************/
	/**保存（需要手动在子类调用）**/
	public static void save(HttpServletRequest request,Class<? extends Table> tableClass) throws Exception {
		Table obj=ParamUtil.getParamToObject(request, tableClass, "obj");
		if(obj!=null) {
			obj.save();
		}else{
			throw new IllegalArgumentException("未填选任何数据");
		}
	}
	/**删除**/
	public static void delete(Class<? extends Table> tableClass,Object id) {
		if(id!=null) {
			List<Object> idList=new ArrayList<Object>();
			idList.add(id);
			deleteSelect(tableClass, idList);
		}else{
			throw new IllegalArgumentException("没有指定（可）处理的项");
		}
	}
	/**批量删除**/
	public static void deleteSelect(Class<? extends Table> tableClass,List<? extends Object> idList) {
		if(idList!=null && idList.size()>0) {
			Table.delete(tableClass, idList);
		}else{
			throw new IllegalArgumentException("没有指定（可）处理的项");
		}
	}
	/**批量启用**/
	public static void enableSelect(Class<? extends Table> tableClass,List<? extends Object> idList) {
		statusSelect(tableClass, 1, idList);
	}
	/**批量禁用**/
	public static void disableSelect(Class<? extends Table> tableClass,List<? extends Object> idList) {
		statusSelect(tableClass, 0, idList);
	}
	/**批量状态更改**/
	public static void statusSelect(Class<? extends Table> tableClass,Object status,List<? extends Object> idList) {
		if(idList!=null && idList.size()>0) {
			String setSQL="status="+status;
			String conditionsSQL="id in("+TableUtil.getPlaceholder(idList)+")";
			Table.update(tableClass, setSQL, conditionsSQL, idList);
		}else{
			throw new IllegalArgumentException("没有指定（可）处理的项");
		}
	}
}
