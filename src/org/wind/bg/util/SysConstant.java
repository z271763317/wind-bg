package org.wind.bg.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @描述 : 系统常量类
 * @作者 : 胡璐璐
 * @时间 : 2019年11月26日 19:26:10
 */
public final class SysConstant{
	
    public static final String SYS_PROPERTIES = "application.properties";		//文件路径（默认工程根目录下）
    /*配置*/
    public static final int conTimeout=Integer.valueOf(getProperty("conTimeout",(10*1000)+""));			//http连接超时
    public static final int readTimeout=Integer.valueOf(getProperty("readTimeout",(60*1000)+""));		//http读取超时
 
    static {
        InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream(SYS_PROPERTIES);
        Properties p = new Properties();
        try {
            p.load(in);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    
    /**
	 * 获取Java配置文件属性（带默认值）
	 * @param name
	 * @param defaultValue
	 * @return
	 */
	public static final String getProperty(String name, String defaultValue) {
		return getPropertyFromFile(SYS_PROPERTIES, name,defaultValue);
	}

	/**
	 * 获取Java配置文件属性
	 * @param name
	 * @return
	 */
	public static final String getProperty(String name) {
		return getPropertyFromFile(SYS_PROPERTIES, name);
	}
	
	/**
	 * @param fullFileName
	 * @param propertyName
	 * @param defaultValue
	 * @return
	 */
	public static String getPropertyFromFile(String fullFileName,String propertyName, String defaultValue) {
		Properties p = new Properties();
		InputStream in = null;
		try {
			in = getFileInputStremByFullName(fullFileName);
			p.load(in);
			return p.getProperty(propertyName, defaultValue);
		} catch (IOException e) {
			throw new RuntimeException(e);
		} finally {
			try {
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	/**
	 * @param fullName
	 * @return
	 */
	public static InputStream getFileInputStremByFullName(String fullName) {
		return Thread.currentThread().getContextClassLoader().getResourceAsStream(fullName);
	}
	/**
	 * @param fullFileName
	 * @param propertyName
	 * @return
	 */
	public static String getPropertyFromFile(String fullFileName,String propertyName) {
		return getPropertyFromFile(fullFileName, propertyName, null);
	}
}