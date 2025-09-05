package org.wind.bg.util;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @描述 : 通用工具类
 * @版权 : 胡璐璐
 * @时间 : 2015年10月9日 09:38:45
 */
@SuppressWarnings("unchecked")
public final class ToolUtil {

//	private static final Logger logger=Logger.getLogger(ToolUtil.class);

	/** 获取 : 请求者IP **/
	public static String getRequestIP(HttpServletRequest request) {
		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		return ip;
	}
	/** 获取 : 请求者端口 **/
	public static int getRequestPort(HttpServletRequest request) {
		return request.getRemotePort();
	}
	/** 获取 : 当前系统的URL根路径 **/
	public static String getRootURL(HttpServletRequest request) {
		try {
			StringBuffer url = new StringBuffer(request.getScheme());
			url.append("://").append(request.getServerName());
			int port=request.getServerPort();
			if(port!=80 && port!=443) {
				url.append(":").append(port);
			}
			url.append(request.getContextPath());
			return URLEncoder.encode(url.toString(), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}
	/** 获取 : 当前请求的完整URL（isUrlEncode=是否对url进行URLEncoding）**/
	public static String getRequestURL(HttpServletRequest request,boolean isUrlEncode) {
		try {
			StringBuffer url = new StringBuffer(request.getScheme());
			url.append("://").append(request.getServerName());
			int port=request.getServerPort();
			if(port!=80 && port!=443) {
				url.append(":").append(port);
			}
			url.append(request.getRequestURI());
			String queryStr = request.getQueryString();
			if (queryStr != null) {
				url.append("?").append(queryStr);
			}
			if(isUrlEncode) {
				return URLEncoder.encode(url.toString(), "UTF-8");
			}else {
				return url.toString();
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 修改图片宽和高(一般做图片大小)
	 * 
	 * @param imgFile  : 图片源文件
	 * @param fileType : 修改后的文件类型（符文图片的后缀名规范）
	 * @param mWidth   : 修改后的宽
	 * @param mHeight  : 修改后的高
	 */
	public static byte[] modifyImgSize(File imgFile, String fileType, int mWidth, int mHeight) throws RuntimeException {
		Graphics g = null;
		try {
			fileType = fileType != null && fileType.trim().length() > 0 ? fileType.trim() : null;
			if (fileType == null) {
				throw new RuntimeException("未指定fileType文件类型（新文件类型）");
			}
			fileType = fileType.indexOf("/") != -1 ? fileType.substring(fileType.indexOf("/") + 1) : fileType;
			BufferedImage image = ImageIO.read(imgFile);
			int imageType = BufferedImage.TYPE_INT_RGB; // 默认RGB类型
			/* png */
			if ("png".equalsIgnoreCase(fileType)) {
				imageType = BufferedImage.TYPE_INT_ARGB;
			}
			BufferedImage tag = new BufferedImage(mWidth, mHeight, imageType);
			g = tag.getGraphics();
			// g.drawImage(image.getScaledInstance(mWidth, mHeight, Image.SCALE_DEFAULT), 0,
			// 0, null);
			g.drawImage(image.getScaledInstance(mWidth, mHeight, Image.SCALE_SMOOTH), 0, 0, null);
			ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(); // 取得内存输出流
			ImageIO.write(tag, fileType, byteArrayOutputStream);
			return byteArrayOutputStream.toByteArray();
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage(), e);
		} finally {
			if (g != null) {
				g.dispose();
			}
		}
	}

	
	/** 获取 : List中Map中的一个key对应的数据（取最后1个List） **/
	public static <T> T getMapList(List<Map<String, Object>> resultMapList, String key) {
		if (resultMapList != null && resultMapList.size() > 0) {
			Map<String, Object> t_resultMap = resultMapList.get(resultMapList.size() - 1);
			return (T) t_resultMap.get(key);
		}
		return null;
	}

	/** 删除List空的数据，序号重新从0开始排列 **/
	public static void deleteListEmpty(List<? extends Object> list) {
		for (int i = 0; list != null && i < list.size(); i++) {
			if (list.get(i) == null || list.get(i).toString().trim().length() <= 0
					|| list.get(i).toString().trim().equalsIgnoreCase("null")) {
				list.remove(i);
				i--;
			}
		}
	}

	/** 是否ajax请求 **/
	public static boolean isAjax(HttpServletRequest request) {
		String requestType = request.getHeader("X-Requested-With"); // 请求类型（ajax还是普通）
		// ajax请求
		if (requestType != null && requestType.equalsIgnoreCase("XMLHttpRequest")) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 获取指定图片url流
	 */
	public static InputStream getImgInputStreamFromUrl(String url) {
		InputStream inputStream = null;
		try {
			int num = url.indexOf('/', 8);
			String u = url.substring(0, num);
			URL imageurl = new URL(url);
			URLConnection connection = imageurl.openConnection();
			connection.setDoOutput(true);
			connection.setRequestProperty("referer", u);
			inputStream = connection.getInputStream();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return inputStream;
	}
	
	/** 获取 : 指定key的cookie值 **/
	public static String getCookieValue(HttpServletRequest request, String key) {
		if (key == null) {
			return null;
		}
		Cookie[] cookies = request.getCookies();
		if (cookies != null && cookies.length > 0) {
			for (Cookie cookie : cookies) {
				if (key.equals(cookie.getName())) {
					return cookie.getValue();
				}
			}
		}
		return null;
	}
	/**下载文件（filePath=文件绝对路径）**/
	public static void downFile(HttpServletRequest request, HttpServletResponse response,String filePath) throws Exception{
		File file = new File(filePath);
		if(file.exists()) {
			//告诉客户端该文件不是直接解析 而是以附件形式打开(下载)----filename="+filename 客户端默认对名字进行解码
			response.addHeader("content-Type", "application/octet-stream");
			response.addHeader("content-Disposition", "attachment;filename="+file.getName());
			//获得该文件的输入流
			InputStream in=null;
			BufferedInputStream bis=null;
			ServletOutputStream out=null;
			BufferedOutputStream bos=null;
			try {
				in = new FileInputStream(file);
				bis=new BufferedInputStream(in);
				//获得输出流---通过response获得的输出流 用于向客户端写内容
				out = response.getOutputStream();
				bos=new BufferedOutputStream(out);
				//文件拷贝的模板代码
				int len = 0;
				byte[] buffer = new byte[1024];
				while((len=bis.read(buffer))>0){
					bos.write(buffer, 0, len);
				}
				bos.flush();
			}catch(Exception e) {
				throw e;
			}finally {
				if(bis!=null) {try{bis.close();}catch(Exception e) {}}
				if(in!=null) {try{in.close();}catch(Exception e) {}}
				if(bos!=null) {try{bos.close();}catch(Exception e) {}}
				if(out!=null) {try{out.close();}catch(Exception e) {}}
			}
		}else{
			throw new IllegalArgumentException("指定的文件不存在");
		}
	}
	/**关闭流**/
	public static void close(Closeable... objArr){
		if(objArr!=null && objArr.length>0){
			for(Closeable t_obj:objArr){
				if(t_obj!=null){try{t_obj.close();}catch(Exception e){e.printStackTrace();}}
			}
		}
	}
}