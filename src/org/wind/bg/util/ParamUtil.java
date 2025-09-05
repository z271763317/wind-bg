package org.wind.bg.util;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;

/**
 * @描述 : 参数工具类——针对http的请求参数。请求参数转对象，对象转请求参数（byte[]需要单独接收）
 * @作者 : 胡璐璐
 * @时间 : 2021年5月15日 20:49:26
 */
@SuppressWarnings("unchecked")
public class ParamUtil {

	/************正则表达式************/
	public static final String regex_object="[a-zA-Z_]{1}[a-zA-Z\\d_]*\\.{1}[a-zA-Z_]{1}[a-zA-Z\\d_\\.]*";		//对象
	public static final String regex_array="[a-zA-Z_]{1}[a-zA-Z\\d_]*\\[\\d+]";		//数组
	public static final String regex_array_object="[a-zA-Z_]{1}[a-zA-Z\\d_]*\\[\\d+]\\.{1}[a-zA-Z_]{1}[a-zA-Z\\d_\\.]*";		//数组对象
	
	/**获取 : 返回数据，Object式（普通对象）**/
  	public static Map<String,Object> get(Object t_obj,String keyPrefix){
  		keyPrefix=keyPrefix!=null?keyPrefix+".":"";
  		//
      	Map<String,Object> t_resultMap=new HashMap<String, Object>();
      	if(t_obj!=null) {
	      	Field t_fieldArr[]=ObjectUtil.getField(t_obj.getClass(), Object.class);
	  		if(t_fieldArr!=null && t_fieldArr.length>0) {
	  			Map<String,Method> methodMap=ObjectUtil.getMethodMap(t_obj.getClass(), Object.class);
	  			for(Field t_field:t_fieldArr) {
	  				String t_key=t_field.getName();
	  				Object t_value=ObjectUtil.get(t_obj, methodMap, t_field);
	  				if(t_value!=null) {
	  					//基础数据类型、String、Boolean
	  					if((t_value instanceof Number) || (t_value instanceof String) || (t_value instanceof Boolean)) {
	  						t_resultMap.put(keyPrefix+t_key, t_value);
	  					//普通对象
	  					}else{
	  						t_resultMap.putAll(get(t_value,keyPrefix+t_key));
	  					}
	  				}
	  			}
	  		}
      	}
  		return t_resultMap;
  	}
   
  	/**获取 : 请求参数转换后的对象（JDK的BeanInfo式）——paramNamePrefix=参数名前缀（若为空，则代表是顶层的所有参数，如：name、age等）**/
	public static <T> T getParamToObject(HttpServletRequest request, Class<T> clszz,String paramNamePrefix) throws Exception{
		paramNamePrefix=paramNamePrefix!=null && paramNamePrefix.trim().length()>0?paramNamePrefix.trim():null;
		Map<String,String[]> paramMap=request.getParameterMap();
		if(paramMap.size()>0) {
			//对象：主参数—>次参数—>值【对象Map—>参数—>值【对象Map...】......如此循环】
			Map<String, Object> paramMap_object=new HashMap<String, Object>();
			Iterator<Entry<String,String[]>> iter=paramMap.entrySet().iterator();
			while(iter.hasNext()) {
				Entry<String,String[]> entry=iter.next();
				String t_key=entry.getKey();
				String t_valueArr[]=entry.getValue();
				if(t_valueArr!=null && t_valueArr.length>0) {
					String t_value=t_valueArr[0];
					String t_fieldName=null;
					//指定前缀
					if(paramNamePrefix!=null) {
						if(t_key.indexOf(paramNamePrefix+".")==0) {
							t_fieldName=t_key.substring(t_key.indexOf(".")+1);		//字段（如：obj.name，则这里是name、obj.sex.id，则是sex.id）
						}
					}else{
						t_fieldName=t_key;
					}
					if(t_fieldName!=null) {
						//对象
						if(t_fieldName.matches(regex_object)){
							generateLayerParam(paramMap_object, t_fieldName, t_value);
						//基础数据类型
						}else{
							paramMap_object.put(t_fieldName, t_value);
						}
					}
				}
			}
			if(paramMap_object.size()>0) {
				return getParamToObject(paramMap_object, clszz);
			}
		}
		return null;
	}
	/**获取 : 请求的所有参数（paramName=参数名。dstTypeClass=要转换的目标类型，一般是基础类型、String、Boolean等，非可转的类型将会报错）**/
	public static <T> T getParameter(HttpServletRequest request,String paramName,Class<T> dstTypeClass){
		String value=request.getParameter(paramName);
		return (T) ObjectUtil.cast(value, dstTypeClass);
	}
	/**获取 : 请求的所有参数**/
	public static Map<String,String> getParameter(HttpServletRequest request){
		Map<String,String> paramMap=new HashMap<String,String>();
		Map<String,String[]> requestParramMap=request.getParameterMap();
		if(requestParramMap!=null && requestParramMap.size()>0) {
			for(Entry<String,String[]> entry:requestParramMap.entrySet()) {
				String valueArr[]=entry.getValue();
				if(valueArr!=null && valueArr.length>0) {
					String key=entry.getKey();
					for(String value:valueArr) {
						paramMap.put(new String(key), value);
					}
				}
			}
		}
		return paramMap;
	}
	/**获取 : （生成）列表参数**/
	public static Map<String,Object> getParamList(String namePrefix,List<? extends Object> list){
		Map<String,Object> paramMap=new HashMap<String, Object>();
		for(int i=0;list!=null && i<list.size();i++) {
			paramMap.put(namePrefix+"["+i+"]", list.get(i));
		}
		return paramMap;
	}
	
	//生成 : 层级Map对应的值（paramMap_object_before=上一层map）
	private static void generateLayerParam(Map<String,Object> paramMap_object_before ,String t_key,String t_value) {
		String t_fieldKey=t_key.substring(0,t_key.indexOf("."));		//当前主参数名
		String t_fieldName=t_key.substring(t_key.indexOf(".")+1);		//字段（如：sex.name，则这里是name、sex.ss.id，则是ss.id）
		Map<String,Object> paramMap_object=(Map<String, Object>) paramMap_object_before.get(t_fieldKey);		//主参数下的子参数
		if(paramMap_object==null) {
			paramMap_object=new HashMap<String, Object>();
			paramMap_object_before.put(t_fieldKey, paramMap_object);
		}
		//子对象
		if(t_fieldName.matches(regex_object)){
			generateLayerParam(paramMap_object, t_fieldName, t_value);
		//基础数据类型（伪）
		}else{
			paramMap_object.put(t_fieldName, t_value);
		}
	}
	
	/**获取 : 请求参数转换后的对象**/
	public static <T> T getParamToObject(Map<String,Object> paramMap_object, Class<T> clszz) throws Exception{
		// 获取该类的信息
		BeanInfo beanInfo = Introspector.getBeanInfo(clszz);
		// 实例化该class
		T obj = clszz.newInstance();
		// 获取该类属性的描述（字段名【属性名】、字段对应的可读取、写入的Method方法，一般是getXXX()和setXXX()）
		PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
		Map<String,PropertyDescriptor> pdMap=new HashMap<String,PropertyDescriptor>();
		for (PropertyDescriptor descriptor:propertyDescriptors) {
			String fieldName=descriptor.getName();		//字段名
			if(!pdMap.containsKey(fieldName)) {
				pdMap.put(fieldName, descriptor);
			}
		}
		//
		boolean isHaveValue=false;		//是否有设置值
		Iterator<Entry<String,Object>> iter_value=paramMap_object.entrySet().iterator();
		while(iter_value.hasNext()) {
			Entry<String,? extends Object> entry=iter_value.next();
			String t_key=entry.getKey();
			Object t_value=entry.getValue();
			PropertyDescriptor descriptor=pdMap.get(t_key);
			//存在
			if(descriptor!=null) {
				Object value_set=null;
				Method method = descriptor.getWriteMethod();		//可写入（设置）的方法，一般是setXXX()
				if(method!=null) {
					//子对象
					if(t_value instanceof Map) {
						Map<String,Object> t_valueMap=(Map<String, Object>) t_value;
						value_set=getParamToObject(t_valueMap, descriptor.getPropertyType());
					}else{
						value_set=ObjectUtil.cast(t_value, descriptor.getPropertyType());
					}
					if(value_set!=null) {
						method.invoke(obj, value_set);	//反射执行
						isHaveValue=true;
					}
				}
			}
		}
		if(isHaveValue) {
			return obj;
		}else {
			return null;
		}
	}
	
}