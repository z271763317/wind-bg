package org.wind.bg.util.system;

/**
 * @描述 : 验证码bean
 * @作者 : 胡璐璐
 * @时间 : 2021年5月2日 22:27:25
 */
public class Yzm {

	private String text;		//验证码文本内容
	private byte[] image;		//验证码图片数据
	
	public Yzm(String text,byte[] image) {
		this.text=text;
		this.image=image;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public byte[] getImage() {
		return image;
	}
	public void setImage(byte[] image) {
		this.image = image;
	}
	
}
