package org.wind.bg.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;

import sun.misc.BASE64Encoder;

/**
 * @描述 : 加密工具类
 * @版权 : 胡璐璐
 * @时间 : 2016年4月9日 13:17:28
 */
public class EncryptUtil {

	private EncryptUtil(){
		//不允许实例化
	}
	/**获取：对str加密后的MD5**/
	public static String getMD5(String str) {
		MessageDigest messageDigest = null;
		try {
			messageDigest = MessageDigest.getInstance("MD5");
			messageDigest.reset();
			messageDigest.update(str.getBytes("UTF-8"));
		} catch (NoSuchAlgorithmException e) {
			System.out.println(e.getMessage());
			return null;
		} catch (UnsupportedEncodingException e) {
			System.out.println(e.getMessage());
			return null;
		}
		byte[] byteArray = messageDigest.digest();
		StringBuffer md5StrBuff = new StringBuffer();
		for (int i = 0; i < byteArray.length; i++) {
			if (Integer.toHexString(0xFF & byteArray[i]).length() == 1) {
				md5StrBuff.append("0").append(
						Integer.toHexString(0xFF & byteArray[i]));
			} else {
				md5StrBuff.append(Integer.toHexString(0xFF & byteArray[i]));
			}
		}
		return md5StrBuff.toString();
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
	 * 3DES 加密
	 * @param message : 需要加密的字符串
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
		cipher.init(Cipher.ENCRYPT_MODE, secretKey);
		return new BASE64Encoder().encode(cipher.doFinal(message.getBytes(charset)));
	}
}
