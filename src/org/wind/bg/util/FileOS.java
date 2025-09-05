package org.wind.bg.util;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.log4j.Logger;
import org.wind.bg.config.cache.Cache;

/**
 *@描述 : 文件操作集
 *@版权 : 胡璐璐
 *@时间 : 2019年11月22日 17:57:30
 */
public class FileOS {

	public static Logger logger=Logger.getLogger(FileOS.class);
	
	/**获取 : 项目路径**/
	public static String getRootPath() {
		URL url = FileOS.class.getResource("");
		String path = url.getFile();
		path = path.substring(0, path.indexOf("WEB-INF"));
		if (path.indexOf("file:") > -1) {
			path = path.substring(5);
		} else if (path.indexOf("ile:") > -1) {
			path = path.substring(path.indexOf(":") + 2);
		}
		try {
			path=java.net.URLDecoder.decode(path,"utf-8");
		} catch (UnsupportedEncodingException e) {}
		return path;
	}
    /**
     * 取得当前类路径下的所有类  
     * @param cls:要获取当前类下的所有类的class类  
     * @return  返回当前类下的所有类
     */  
    public static List<Class<?>> getClassList(Class<?> cls) {
    	List<Class<?>> objectList = new ArrayList<Class<?>>();
    	try{
	        String pk = cls.getPackage().getName();   
	        String path = pk.replace('.', '/');   
	        ClassLoader classloader = Thread.currentThread().getContextClassLoader();   
	        URL url = classloader.getResource(path);
	        File dir= new File(java.net.URLDecoder.decode(url.getFile(),"UTF-8"));
	        if (!dir.exists()) {   
	            return objectList;   
	        }   
	        for (File f : dir.listFiles()){
	            String name = f.getName();   
	            if (name.endsWith(".class")) {   
	            	objectList.add(Class.forName(pk + "." + name.substring(0, name.lastIndexOf("."))));   
	            }   
	        }   
    	}catch(Exception e){
    		e.printStackTrace();
    	}
        return objectList; 
    }
    /**  
     * 获取 : 指定文件目录路径下的所有深层次Class类
     * @param fileDirectoryPath : 文件夹（文件目录）路径
     * @param packagePrefix : 包名前缀（会拼上文件名，形成类的全路径）
     */  
    public static List<Class<?>> getAllClassList(String fileDirectoryPath,String packagePrefix) {
    	List<Class<?>> classList = new ArrayList<Class<?>>();
    	try{
	        File fileDirectory= new File(fileDirectoryPath);
	        if(fileDirectory.exists() && fileDirectory.isDirectory()) {
	        	packagePrefix=packagePrefix!=null && packagePrefix.trim().length()>0?packagePrefix.trim():null;
	        	if(packagePrefix!=null) {
	        		packagePrefix+=".";
	        	}else{
	        		packagePrefix="";
	        	}
        		for (File t_file : fileDirectory.listFiles()){
        			String name = t_file.getName();
 		            //class文件
 		            if (name.endsWith(".class")) {   
 		            	classList.add(Class.forName(packagePrefix + name.substring(0, name.lastIndexOf("."))));
 		            //目录
 		            }else if(t_file.isDirectory()){
 		            	classList.addAll(getAllClassList(t_file, packagePrefix+t_file.getName()));
 		            }
 	            }
	        } 
    	}catch(Exception e){
    		e.printStackTrace();
    	}
        return classList; 
    }
    /**  
     * 获取 : 指定文件目录下的所有深层次Class类
     * @param fileDirectory : 文件夹（文件目录）对象
     * @return packagePrefix : 包名前缀（会拼上文件名，形成类的全路径）
     */  
    public static List<Class<?>> getAllClassList(File fileDirectory,String packagePrefix) {
    	List<Class<?>> classList = new ArrayList<Class<?>>();
    	try{
    		//目录
	        if (fileDirectory.exists() && fileDirectory.isDirectory()) {
	        	packagePrefix=packagePrefix!=null && packagePrefix.trim().length()>0?packagePrefix.trim():null;
	        	if(packagePrefix!=null) {
	        		packagePrefix+=".";
	        	}
        		for (File t_file : fileDirectory.listFiles()){
        			String name = t_file.getName();
 		            //class文件
 		            if (name.endsWith(".class")) {   
 		            	classList.add(Class.forName(packagePrefix + name.substring(0, name.lastIndexOf("."))));
 		            //目录
 		            }else if(t_file.isDirectory()){
 		            	classList.addAll(getAllClassList(t_file, packagePrefix+t_file.getName()));
 		            }
 	            }
	        } 
    	}catch(Exception e){
    		e.printStackTrace();
    	}
        return classList; 
    }
    /**
  	 * 获取指定类模糊匹配prefix的所有方法，prefix为空则取所有的方法，List式
  	 * @param objClass : 指定的类
  	 * @param prefix : 前缀，模糊匹配方法名
  	 */
  	public static List<Method> getMethodList(Class<?> objClass,String prefix){
  		Method methodArr[]=objClass.getDeclaredMethods();
  		List<Method> list=new ArrayList<Method>();
		for(int i=0;i<methodArr.length;i++){
			Method method=methodArr[i];
			String key=method.getName();
			if(prefix==null || key.indexOf(prefix)!=-1){
				list.add(method);
			}
		}
		return list;
  	}
	/**返回给调用者的数据（字符集：UTF-8）**/
  	public static void writer(ServletRequest request,ServletResponse response,String result) {
		try {
  			String t_result=handlerReturn(request, response, result);
  			response.setCharacterEncoding("UTF-8");
  			response.setContentType("application/json;charset=UTF-8");
			response.getWriter().write(t_result);
			response.getWriter().close();
		} catch (IOException e) {
			logger.error(e.getMessage(),e);
		}
  	}
  	/**处理返回给调用者的数据**/
  	public static String handlerReturn(ServletRequest request,ServletResponse response,String result){
  		String callback=request.getParameter(Cache.jsonp_callBack_param);		//回调方法
		if(callback!=null && callback.trim().length()>0){
			return callback.trim()+"("+result+")";
		}else {
			return result;
		}
  	}
}