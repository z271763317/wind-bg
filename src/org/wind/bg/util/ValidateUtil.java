package org.wind.bg.util;

/**
 * @描述 : 验证工具类——验证失败抛出错误
 * @版权 : 胡璐璐
 * @时间 : 2016年9月25日 12:56:18
 */
public final class ValidateUtil{
	

	private ValidateUtil(){
		//不允许实例化
	}
	//判断并抛出异常（通用）
	private static void throwException(boolean result,String exception){
		if(!result){
			throw new IllegalArgumentException(exception);
		}
	}
	/**空数据，key=提示关键词**/
	public static void empty(Object str,String errorContent) throws IllegalArgumentException{
		throwException(str==null || str.toString().trim().length()<=0?true:false, errorContent);
	}
	/**非空数据，key=提示关键词**/
	public static void notEmpty(Object str,String errorContent) throws IllegalArgumentException{
		throwException(str!=null && str.toString().trim().length()>0?true:false, errorContent);
	}
	/**非空数据（可变数组式），可为字符串，可为其他对象，errorContent=错误内容**/
	public static void notEmpty(String errorContent,Object... args) throws IllegalArgumentException{
		if(args!=null && args.length>0){
			for(int i=0;i<args.length;i++){
				Object t_obj=args[i];
				if(t_obj!=null){
					if(t_obj instanceof String && t_obj.toString().trim().length()<=0){
						throw new IllegalArgumentException(errorContent);
					}
				}else{
					throw new IllegalArgumentException(errorContent);
				}
			}
		}else{
			throw new IllegalArgumentException("缺少验证的参数");
		}
	}
	/**手机号**/
	public static void mobilePhone(String str,String errorContent) throws IllegalArgumentException{
		throwException(RegexUtil.isMobilePhone(str), errorContent!=null?errorContent:"【"+str+"】不是手机号");
	}
	/**非数字（不全为数字）**/
	public static void notInteger(String str,String errorContent) throws IllegalArgumentException{
		throwException(RegexUtil.isNotInteger(str), errorContent!=null?errorContent:"【"+str+"】不是数字（整数）");
	}
	/**数字——整数**/
	public static void integer(String str,String errorContent) throws IllegalArgumentException{
		throwException(RegexUtil.isInteger(str), errorContent!=null?errorContent:"【"+str+"】不是数字（整数）");
	}
	/**数字**/
	public static void number(String str,String errorContent) throws IllegalArgumentException{
		throwException(RegexUtil.isNumber(str), errorContent!=null?errorContent:"【"+str+"】不是数字");
	}
	/**身份证**/
	public static void idCard(String str,String errorContent) throws IllegalArgumentException{
		throwException(RegexUtil.isIdCard(str), errorContent!=null?errorContent:"【"+str+"】不是身份证");
	}
}