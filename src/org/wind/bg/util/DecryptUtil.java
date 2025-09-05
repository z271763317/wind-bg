package org.wind.bg.util;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;

import sun.misc.BASE64Decoder;

/**
 * @描述 : 加密工具类
 * @版权 : 胡璐璐
 * @时间 : 2016年4月9日 13:17:28
 */
public class DecryptUtil {

	private DecryptUtil(){
		//不允许实例化
	}
	//获取处理的“可用密钥”的前24位密文（不足24位的往后面补“0”）
	private static String getSecretKey(String key_source){
		String key="";
		if(key_source.length()>24){
			key = key_source.substring(0, 24);
		}else if(key_source.length()<24){
			String zero="";
			for(int i=0;i<24-key_source.length();i++){
				zero+="0";
			}
			key=key_source+zero;
		}else{
			key=key_source;
		}
		return key;
	}
	/**
	 * 3DES 解密
	 * @param message : 加密后的字符串
	 * @param key : 密钥（不足24位的往后面补“0”）
	 */
	public static String DESede(String message, String key) throws Exception {
		String charset="UTF-8";
		String encryptionType="DESede";		//DES=DES 3DES=DESede
		key=getSecretKey(key);		//处理key
		Cipher cipher = Cipher.getInstance(encryptionType);
		byte key_ch_arr[]=key.getBytes(charset);
		DESedeKeySpec desKeySpec=new DESedeKeySpec(key_ch_arr);
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(encryptionType);
		SecretKey secretKey = keyFactory.generateSecret(desKeySpec);
		cipher.init(Cipher.DECRYPT_MODE, secretKey);
		return new String(cipher.doFinal(new BASE64Decoder().decodeBuffer(message)),charset);
	}
}
