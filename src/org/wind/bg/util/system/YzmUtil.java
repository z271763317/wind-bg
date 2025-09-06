package org.wind.bg.util.system;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Properties;

import javax.imageio.ImageIO;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;

/**
 * @描述 : 验证码工具类
 * @作者 : 胡璐璐
 * @时间 : 2021年5月2日 22:22:08
 */
public class YzmUtil {

	public static Yzm generate() throws IOException{
		DefaultKaptcha defaultKaptcha = new DefaultKaptcha();
	    Properties properties = new Properties();
	    // 图片边框
	    properties.setProperty("kaptcha.border", "yes");
	    // 边框颜色
	    properties.setProperty("kaptcha.border.color", "105,179,90");
	    // 字体颜色
	    properties.setProperty("kaptcha.textproducer.font.color", "red");
	    // 图片宽
	    properties.setProperty("kaptcha.image.width", "110");
	    // 图片高
	    properties.setProperty("kaptcha.image.height", "44");
	    // 字体大小
	    properties.setProperty("kaptcha.textproducer.font.size", "30");
	    // session key
	    properties.setProperty("kaptcha.session.key", "code");
	    // 验证码长度
	    properties.setProperty("kaptcha.textproducer.char.length", "4");
	    // 字体
	    properties.setProperty("kaptcha.textproducer.font.names", "宋体,楷体,微软雅黑");
	    Config config = new Config(properties);
	    defaultKaptcha.setConfig(config);
	    //
	    ByteArrayOutputStream out = new ByteArrayOutputStream();
        // 将生成的验证码保存在session中
        String createText = defaultKaptcha.createText();
        //
        BufferedImage bi = defaultKaptcha.createImage(createText);
        ImageIO.write(bi, "jpg", out);
        byte[] captcha = out.toByteArray();
        //
        return new Yzm(createText, captcha);
	}
	
		
}
