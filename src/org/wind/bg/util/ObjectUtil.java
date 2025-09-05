package org.wind.bg.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @描述 : 对象工具类
 * @作者 : 胡璐璐
 * @时间 : 2021年5月20日 17:59:15
 */
@SuppressWarnings("unchecked")
public class ObjectUtil {

	/**缓存**/
	private static final Map<Class<?>,?> objectMap=new ConcurrentHashMap<Class<?>, Object>();
	private static final Map<Class<?>,Field[]> classFieldMap=new HashMap<Class<?>,Field[]>();		//class类的字段集，value=深层次到Object的所有可用字段
	private static final Map<Class<?>,Map<String,Method>> classMethodMap=new HashMap<Class<?>,Map<String, Method>>();		//class类的方法集（次key=方法名+参数）
	
	/**获取 : 指定className（java格式的路径）的对象**/
	public static <T> T get(String className) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
		return (T) get(Class.forName(className));	
	}
	/**获取 : 指定class的对象**/
	public static <T> T get(Class<T> clazz) throws InstantiationException, IllegalAccessException {
		return clazz.newInstance();		//实例化
	}
	/**获取 : 指定class的单例对象**/
	public static <T> T getSingleton(String className) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
		Class<?> clazz=get(className);
		return (T) getSingleton(clazz);
	}
	/**获取 : 指定class的单例对象**/
	public static <T> T getSingleton(Class<T> clazz) throws InstantiationException, IllegalAccessException {
		if(clazz!=null) {
			T result=(T) objectMap.get(clazz);
			if(result==null) {
				synchronized (clazz) {
					result=(T) objectMap.get(clazz);
					if(result==null) {
						result=get(clazz);	//实例化
					}
				}
			}
			return result;
		}
		return null;
	}
	
	/************json结构转换成对象************/
	/**对象转换成Map（返回json结构。对象属性名=map的key，属性值=map的值）**/
  	public static Map<String,Object> objectConvertMap(Object obj){
      	Map<String,Object> t_resultMap=new HashMap<String, Object>();
      	if(obj!=null) {
      		Field fieldArr[]=getField(obj.getClass(), Object.class);
	  		if(fieldArr!=null && fieldArr.length>0) {
	  			for(Field t_field:fieldArr) {
	  				//非静态变量
	  				if (!Modifier.isStatic(t_field.getModifiers())) {
		  				t_field.setAccessible(true);		//取消安全检查
		  				String t_key=t_field.getName();
		  				try {
			  				Object t_value=t_field.get(obj);
			  				if(t_value!=null) {
			  					//Map
			  					if(t_value instanceof Map) {
			  						t_resultMap.put(t_key, (Map<Object,Object>) t_value);		//不做深入处理
			  					//集合
			  					}else if(t_value instanceof Collection) {
			  						t_resultMap.put(t_key, objectListConvertList((List<Object>) t_value));
			  					//基础数据类型、String、Boolean
			  					}else if((t_value instanceof Number) || (t_value instanceof String) || (t_value instanceof Boolean)) {
			  						t_resultMap.put(t_key, t_value);
			  					//字节数组
			  					}else if(t_value instanceof byte[]) {
			  						t_resultMap.put(t_key, Base64.getEncoder().encodeToString((byte[]) t_value));
			  					//普通对象
			  					}else{
			  						t_resultMap.put(t_key, objectConvertMap(t_value));
			  					}
			  				}
		  				}catch(IllegalAccessException e) {
		  					throw new RuntimeException(e);
		  				}
	  				}
	  			}
	  		}
      	}
  		return t_resultMap;
    }
  	/**对象列表转换成List（返回json结构。对象属性名=map的key，属性值=map的值。另外collection内只支持基础数类型、普通对象）*/
  	public static List<Object> objectListConvertList(List<? extends Object> list){
      	List<Object> resultList=new ArrayList<Object>();
      	if(list!=null && list.size()>0) {
	  		for(Object t_value:list) {
	  			if(t_value!=null) {
	  				//Map
	  				if(t_value instanceof Map) {
	  					//不支持
	  				//集合
	  				}else if(t_value instanceof Collection) {
	  					//不支持
	  				//基础数据类型、String、Boolean
					}else if((t_value instanceof Number) || (t_value instanceof String) || (t_value instanceof Boolean)) {
						resultList.add(t_value);
					//字节数组
  					}else if(t_value instanceof byte[]) {
  						resultList.add(Base64.getEncoder().encodeToString((byte[]) t_value));
	  				//普通对象
	  				}else{
	  					resultList.add(objectConvertMap(t_value));
	  				}
	  			}
	  		}
      	}
  		return resultList;
  	}
  	
  	/************json结构转换成对象************/
	/**map转换成对象（返回您的返回类型对象。map的key=对象属性名，map的值=属性值）**/
  	public static <T> T mapConvertObject(Class<T> objectClass,Map<String,? extends Object> map) {
      	if(objectClass!=null && map!=null && map.size()>0) {
      		Field fieldArr[]=getField(objectClass, Object.class);
	  		if(fieldArr!=null && fieldArr.length>0) {
	  			try {
		  			T obj=objectClass.newInstance();
		  			setProperty(obj, map);
		  			return obj;
	  			}catch(Exception e) {
	  				throw new RuntimeException(e);
	  			}
	  		}
      	}
      	return null;
    }
  	/**list转换成对象（返回您的返回类型对象。map的key=对象属性名，map的值=属性值）**/
  	public static <T> List<T> listConvertObject(Class<T> objectClass,List<Map<String,Object>> list){
      	if(objectClass!=null && list!=null && list.size()>0) {
      		Field fieldArr[]=getField(objectClass, Object.class);
	  		if(fieldArr!=null && fieldArr.length>0) {
	  			List<T> objList=new ArrayList<T>();
	  			try {
		  			for(Map<String,Object> map:list) {
			  			T obj=objectClass.newInstance();
			  			setProperty(obj, map);
			  			objList.add(obj);
		  			}
		  			return objList;
	  			}catch(Exception e) {
	  				throw new RuntimeException(e);
	  			}
	  		}
      	}
      	return null;
    }
  	
	/**
	 * 设置 : 指定对象的成员属性（变量），深层次父类到Object截止。<br/>
	 * 			<t style="margin-left:34px">
	 * 				基本数据类型，会转换成对应的变量类型（如：value为String，变量类型为Integer，则value会转成Integer）
	 * 			</t>
	 * @param object : 指定的对象
	 * @param propertyValueMap : 成员属性（变量）对应的值（key=属性（变量）名；value=值）
	 */
  	public static void setProperty(Object object,Map<String,? extends Object> propertyValueMap){
  		if(propertyValueMap!=null && propertyValueMap.size()>0) {
	  		Class<?> clazz=object.getClass();
	  		/*字段*/
	  		Field fieldArr[]=getField(clazz, Object.class);
	  		out:for(Field t_field:fieldArr){
	  			//非静态变量
				if (!Modifier.isStatic(t_field.getModifiers())) {
		  			t_field.setAccessible(true);		//取消安全检查
					String t_key=t_field.getName();
					//
					Object t_value=propertyValueMap.get(t_key);		//值
					Object t_value_set=null;		//被设置的值
					Class<?> t_fieldTypeClass=t_field.getType();
					//Map
					if(Map.class.isAssignableFrom(t_fieldTypeClass)) {
						//不支持
					//List
					}else if(List.class.isAssignableFrom(t_fieldTypeClass)) {
						if(t_value instanceof List) {
							List<Object> t_childPropertyValueList=(List<Object>) t_value;
							if(t_childPropertyValueList!=null && t_childPropertyValueList.size()>0) {
								List<Object> t_value_set_list=new ArrayList<Object>();
								//
								Type fc = t_field.getGenericType(); 	//关键的地方，如果是List类型，得到其Generic的类型  
								if(fc instanceof ParameterizedType){
									ParameterizedType pt = (ParameterizedType) fc;  
									Type tArr[]=pt.getActualTypeArguments();
									Type type=tArr[0];
									Class<?> typeClass=(Class<?>)type;
									//自定义类
									if(typeClass.getClassLoader()!=null) {
										for(Object t_obj:t_childPropertyValueList) {
											//是Map
											if(t_obj instanceof Map) {
												Map<String,Object> t_obj_map=(Map<String,Object>) t_obj;
												try {
													Object t_value_set_item=get(t_fieldTypeClass);
													setProperty(t_value_set_item,t_obj_map);
													t_value_set_list.add(t_value_set_item);
												} catch (Exception e) {
													
												}
											}else{
												continue out;
											}
										}
									//JDK类
									}else{
										t_value_set_list=t_childPropertyValueList;
									}
					            }
								//有值
								if(t_value_set_list.size()>0) {
									t_value_set=t_value_set_list;
								}
							}
				         //子属性值对象不是List，则结束本次循环
						}else{
							continue;
						}
					//自定义类
					}else if(t_fieldTypeClass.getClassLoader()!=null){
						if(t_value instanceof Map) {
							Map<String, Object> t_childPropertyValueMap=(Map<String, Object>) t_value;
							try {
								t_value_set=get(t_fieldTypeClass);
								setProperty(t_value_set,t_childPropertyValueMap);
							} catch (Exception e) {
		//						e.printStackTrace();
							} 
						//子属性值对象不是Map，则结束本次循环
						}else{
							continue;
						}
					//普通
					}else{
						t_value_set=cast(t_value, t_field.getType());
					}
					if(t_value_set!=null) {
						try {
							t_field.set(object, t_value_set);
						} catch (Exception e) {
							
						}
					}
				}
			}
	  	}
  	}
	
	/**获取 : 指定对象、字段的值**/
	public static Object get(Object obj, Map<String, Method> methodMap, Field f) {
		Method m = methodMap.get("get" + f.getName().toLowerCase());
		Object paramArr[] = getParameterNull(m);
		try {
			if (paramArr != null && obj != null) {
				return m.invoke(obj, paramArr);
			} else {
				return null;
			}
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}
	/**获取 : 指定class的方法集（parentClass=停止获取的父class的）**/
	public static Field[] getField(Class<?> cls, Class<?> parentClass) {
		Field fieldArr[] = classFieldMap.get(cls);
		if (fieldArr == null) {
			if (parentClass != null) {
				Map<String, Field> fieldMap = new LinkedHashMap<String, Field>(); // key=字段名称
				Class<?> t_parentClass = cls;
				while (t_parentClass != null && t_parentClass != parentClass) {
					Field t_fieldArr[] = t_parentClass.getDeclaredFields();
					for (Field f : t_fieldArr) {
						String t_fieldName = f.getName();
						// 若不存在
						if (!fieldMap.containsKey(t_fieldName)) {
							fieldMap.put(t_fieldName, f);
						}
					}
					t_parentClass = t_parentClass.getSuperclass();
				}
				Field fArr[] = new Field[fieldMap.size()];
				int i = 0;
				for (Field t_field : fieldMap.values()) {
					fArr[i] = t_field;
					i++;
				}
				fieldArr = fArr;
			} else {
				fieldArr = cls.getDeclaredFields();
			}
			classFieldMap.put(cls, fieldArr);
		}
		return fieldArr;
	}
	/**获取 : 指定class的方法集（parentClass=停止获取的父class的）**/
	public static Map<String, Method> getMethodMap(Class<?> cls, Class<?> parentClass) {
		Map<String, Method> methodMap = classMethodMap.get(cls); // key=小写（方法名+参数类型名） value=Method对象
		if (methodMap == null) {
			String prefix = "get";
			methodMap = new HashMap<String, Method>();
			Class<?> t_parentClass = cls;
			while (t_parentClass != null && t_parentClass != parentClass) {
				Method t_methodArr[] = t_parentClass.getDeclaredMethods();
				for (Method m : t_methodArr) {
					StringBuffer key = new StringBuffer(m.getName());
					if (prefix == null || key.indexOf(prefix) == 0) {
						Class<?> paramArr[] = m.getParameterTypes();
						for (int j = 0; j < paramArr.length; j++) {
							key.append(paramArr[j].getSimpleName());
						}
						// 是否不存在
						if (!methodMap.containsKey(key.toString().toLowerCase())) {
							methodMap.put(key.toString().toLowerCase(), m);
						}
					}
				}
				t_parentClass = t_parentClass.getSuperclass();
			}
			//
			classMethodMap.put(cls, methodMap);
		}
		return methodMap;
	}
	/**
	 * 对象类型转换
	 * @param source : 源对象
	 * @param converTypeClass : 目标类型Class
	 * @return 返回目标类型对象
	 */
	public static Object cast(Object source,Class<?> dstTypeClass){
		Object dstObj=null;
		if(source!=null && dstTypeClass!=null){
			if(source instanceof Number){
				Number number=(Number)source;
				if(dstTypeClass==Byte.class || dstTypeClass==byte.class){
					dstObj=number.byteValue();
				}else if(dstTypeClass==Double.class){
					dstObj=number.doubleValue();
				}else if(dstTypeClass==Float.class){
					dstObj=number.floatValue();
				}else if(dstTypeClass==Integer.class){
					dstObj=number.intValue();
				}else if(dstTypeClass==Long.class){
					dstObj=number.longValue();
				}else if(dstTypeClass==Short.class){
					dstObj=number.shortValue();
				}
			}
			//不是Number、基础数据类型
			if(dstObj==null){
				if (dstTypeClass.isAssignableFrom(source.getClass())){
					dstObj=dstTypeClass.cast(source);
				}else{
					String t_dstObj=source.toString();
					//有内容
					if(t_dstObj.length()>0){
						//Integer
						if(dstTypeClass==Integer.class){
							dstObj=Integer.parseInt(t_dstObj);
						//Long
						}else if(dstTypeClass==Long.class){
							dstObj=Long.parseLong(t_dstObj);
						//Float
						}else if(dstTypeClass==Float.class){
							dstObj=Float.parseFloat(t_dstObj);
						//Double
						}else if(dstTypeClass==Double.class){
							dstObj=Double.parseDouble(t_dstObj);
						//Byte
						}else if(dstTypeClass==Byte.class){
							dstObj=Byte.parseByte(t_dstObj);
						//byte数组
						}else if(dstTypeClass==byte[].class){
							dstObj=Base64.getDecoder().decode(t_dstObj);
						//Short
						}else if(dstTypeClass==Short.class){
							dstObj=Short.parseShort(t_dstObj);
						//Boolean
						}else if(dstTypeClass==Boolean.class){
							dstObj=Boolean.parseBoolean(t_dstObj);
						}else{
							dstObj=source;
						}
					}
				}
			}
		}else{
			dstObj=source;
		}
		return dstObj;
	}
	
	/**********************本地方法**********************/
	private static Object[] getParameterNull(Method m) {
		if (m != null) {
			Object paramArr[] = new Object[m.getParameterTypes().length];
			for (int i = 0; i < paramArr.length; i++) {
				paramArr[i] = null;
			}
			return paramArr;
		} else {
			return null;
		}
	}
	
}
