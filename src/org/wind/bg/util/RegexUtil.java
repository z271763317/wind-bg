package org.wind.bg.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @描述 : 正则表达式：验证邮箱帐号、拆分字符串为List、获取所需信息等
 * @作者 : 胡璐璐
 * @时间 : 2012年10月10日 10:45:05
 */
public class RegexUtil {

	/**
	 * 验证邮箱帐号的格式是否合法
	 * @param email : 需要验证的邮箱帐号
	 */
	public static boolean isEmail(String email) {
		if(email!=null && email.trim().length()>0){
			email=email.trim();
			int count=count(email, "@");
			if(count==1){
				Pattern p = Pattern.compile("(\\p{Lower}|\\p{Upper}|[0-9]).*@(\\p{Lower}|\\p{Upper}|[0-9])*\\.\\p{Lower}|\\p{Upper}{1,}");
				Matcher m = p.matcher(email);
				return m.find();
			}else{
				return false;
			}
		}else{
			return false;
		}
	}

	/**
	 * 获取合法的邮箱帐号
	 * @param email : 需要验证的邮箱帐号
	 */
	public static String getEmail(String email) {
		return RegexUtil.email(email, false);
	}

	/**
	 * 判断str是否有汉字
	 * @param str : 需要验证是否为汉字的String
	 */
	public static boolean isHanzi(String str) {
		String regex = "([\u4e00-\u9fa5]+)";
		Matcher matcher = Pattern.compile(regex).matcher(str != null ? str : "");
		if (matcher.find()) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 简单判断value是否为电话号码
	 * @param str : 验证是否为电话号码的String
	 */
	public static boolean isPhone(String value) {
		Pattern p;
		Matcher m;
		boolean flag = false;
		if (value != null) {
			switch (value.length()) {
			case 7:
				p = Pattern.compile("[1-9][0-9]*");
				m = p.matcher(value);
				flag = m.matches();
				break;
			case 8:
				p = Pattern.compile("[1-9][0-9]*");
				m = p.matcher(value);
				flag = m.matches();
				break;
			case 10:
				p = Pattern.compile("0[1-9]*");
				m = p.matcher(value);
				flag = m.matches();
				break;
			case 11:
				p = Pattern.compile("0[1-9]*");
				m = p.matcher(value);
				flag = m.matches();
				break;
			case 12:
				p = Pattern.compile("0[1-9]*");
				m = p.matcher(value);
				flag = m.matches();
				break;
			}
		}
		if (flag) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 获取value中的所有汉字
	 * @param value : 要获取所有的汉字的String类型
	 */
	public static String getAllHanzi(String html) {
		StringBuffer hanzi = new StringBuffer();
		for (int i = 0; i < html.length(); i++) {
			byte[] bytes = (html.charAt(i) + "").getBytes();
			if (bytes.length == 2) {
				int[] ints = new int[2];
				ints[0] = bytes[0] & 0xff;
				ints[1] = bytes[1] & 0xff;
				if (ints[0] >= 0x81 && ints[0] <= 0xFE && ints[1] >= 0x40
						&& ints[1] <= 0xFE) {
					hanzi.append(html.charAt(i) + "");
				}
			}
		}
		return hanzi.toString();
	}

	/**
	 * 获取value中的IP地址
	 * @param value : 要获取IP地址的String类型
	 */
	public static String getIP(String value) {
		Pattern p;
		Matcher m;
		if (value == null || value.length() <= 0) {
			return null;
		}
		// 注意括号()
		p = Pattern.compile("(\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3})");
		m = p.matcher(value);
		String ip = null;
		// 捕捉匹配的内容(IP地址)
		if (m.find()) {
			ip = m.group(1);
		} else {
			return null;
		}
		String pp[] = ip.split("\\.");
		for (String s : pp) {
			// 判断有没有大于255的数值
			if (Integer.parseInt(s) > 255 || Integer.parseInt(pp[0]) <= 0) {
				return null;
			}
		}
		return ip;
	}

	/**
	 * 验证date中的日期
	 * @param value : 要验证日期的String类型
	 */
	public static boolean isDate(String date) {
		String returnDate = RegexUtil.date(date, false);
		if (returnDate.equals("1")) {
			return true;
		}
		return false;
	}

	/**
	 * 获取date字符串中的日期(String类型)
	 * @param value : 要获取日期的String类型
	 */
	public static String getDate(String date) {
		String returnDate = RegexUtil.date(date, true);
		if (returnDate.length() >= 0) {
			return returnDate;
		}
		return null;
	}

	/**
	 * 随机获取1个Email邮箱
	 * @param length[] : 前面的邮箱名称的长度(没传入,则系统默认长度为9)
	 */
	public static String getRandomEmail(int... length) {
		// 有长度的话
		String email = "";
		if (length != null && length.length > 0) {
			email = RegexUtil.getRandEngLish(length[0]) + "@"
					+ RegexUtil.getRandEngLish(3) + "."
					+ RegexUtil.getRandEngLish(3);
		} else {
			email = RegexUtil.getRandEngLish(9) + "@"
					+ RegexUtil.getRandEngLish(3) + "."
					+ RegexUtil.getRandEngLish(3);
		}
		return email;

	}

	/**
	 * 随机获取n个中文(汉字)字符
	 * @param n : 想要获取的中文字符数量
	 */
	public static String getRandHanzi(int n) {
		String text = "啊阿嗄吖吧不把啵才传成层的都对到饿哦额俄飞发非饭个给跟改好还会和就将加叫看库框空了类咯来"
				+ "吗没名买你那能男噢喔平怕排潘去请器恰人让如日是说上时他头它太听她我为无完玩问网王想下写新中则值也在中则周"
				+ "当你手中抓住一件东西不放时你只能拥有这件东西如果你肯放手你就有机会选择别的人的心若死执自己的观念不肯放下那么他的智慧也只能达到某种程度而已"
				+ "如果你准备结婚的话告诉你一句非常重要的哲学名言你一定要忍耐包容对方的缺点"
				+ "世界上没有一个人是只靠自己的力量就可以达到成功的成功的背后一定有许多人在有意或无意识地帮助你你要永远感激他们"
				+ "世界上每个人都在完成两大积累知识与经验的积累资本与信誉的积累当你自认已完成这两大积累时那你想要做什么都会成功"
				+ "我们确实活得艰难一要承受种种外部的压力更要面对自己内心的困惑在苦苦挣扎中如果有人向你投以理解的目光你会感到一种生命的暖意或许仅有短暂的一瞥就足以使我感奋不已"
				+ "永远不要嘲笑你的教师无知或者单调因为有一天当你发现你用瞌睡来嘲弄教师实际上很愚蠢时你在社会上已经碰了很多钉子了";
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < n; i++) {
			sb.append(text.charAt(new Random().nextInt(text.length())));
		}
		return sb.toString();
	}

	/**
	 * 随机获取数字（阿拉伯数字），String型
	 * @param n : 随机获取n长度的数字（常常用于随机的填充一些参数、返回的只有数字 n不能小于1）
	 */
	public static String getRandNumber(int n) {
		String text = "1234567890";
		StringBuffer sb = new StringBuffer();
		Random r=new Random();
		for (int i = 0; i < n; i++) {
			sb.append(text.charAt(r.nextInt(text.length())));
		}
		return sb.toString();
	}

	/**
	 * 随机获取一个中文名字
	 */
	public static String getRandomName() {
		Random random = new Random();
		String firstName = "张黄李罗刘唐宋雷王陈杨赵吴周徐孙朱胡郭何高郑梁许韩冯邓曹彭曾";
		String secondName = "好静远发烈南伟佳雪瑞锐蕊勇鑫欣峰芳明敏娟洋林玲凌浩跃红宏洪鸿鸿弘宾帝";
		int firstNameRand = random.nextInt(firstName.length() - 1);
		int secondNameRand = random.nextInt(firstName.length() - 1);
		String name = firstName.substring(firstNameRand, firstNameRand + 1)
				+ secondName.substring(secondNameRand, secondNameRand + 1);
		return name;
	}

	/**
	 * 
	 * 统计字符串出现的次数
	 * @param str : 字符串
	 * @param countStr : 要统计的字符串
	 */
	public static int count(String str, String countStr) {
		int count=0;
		if(str!=null && countStr!=null){
			int t_index=-1;
			boolean isFirst=true;		//是否首次查询
			while(t_index!=-1 || isFirst){
				t_index=str.indexOf(countStr, t_index+1);
				if(t_index!=-1){
					count++;
				}
				isFirst=false;
			}
		}
		return count;
	}
	/**
	 * 转换unicode文件
	 * @param dataStr
	 */
	public static String decodeUnicode(String dataStr) {
		final StringBuffer buffer = new StringBuffer();
		String tempStr = "";
		String operStr = dataStr;

		if (operStr != null && operStr.indexOf("\\u") == -1)
			return buffer.append(operStr).toString(); //
		if (operStr != null && !operStr.equals("")
				&& !operStr.startsWith("\\u")) { //
			tempStr = operStr.substring(0, operStr.indexOf("\\u")); //
			operStr = operStr.substring(operStr.indexOf("\\u"),
					operStr.length());// operStr字符一定是以unicode编码字符打头的字符串
		}
		buffer.append(tempStr);
		while (operStr != null && !operStr.equals("")
				&& operStr.startsWith("\\u")) { // 循环处理,处理对象一定是以unicode编码字符打头的字符串
			tempStr = operStr.substring(0, 6);
			operStr = operStr.substring(6, operStr.length());
			String charStr = "";
			charStr = tempStr.substring(2, tempStr.length());
			char letter = (char) Integer.parseInt(charStr, 16); // 16进制parse整形字符串。
			buffer.append(new Character(letter).toString());
			if (operStr.indexOf("\\u") == -1) { //
				buffer.append(operStr);
			} else { // 处理operStr使其打头字符为unicode字符
				tempStr = operStr.substring(0, operStr.indexOf("\\u"));
				operStr = operStr.substring(operStr.indexOf("\\u"),
						operStr.length());
				buffer.append(tempStr);
			}
		}
		return buffer.toString();
	}

	/**
	 * 随机生成IP地址
	 */
	public static String getRandomIP() {
		Random random = new Random();
		int i;
		for (i = random.nextInt(200) + 1; i == 10 || i == 127 || i == 192
				|| i == 172 || i == 169; i = random.nextInt(200) + 1)
			;
		int j = random.nextInt(253) + 1;
		int k = random.nextInt(253) + 1;
		int l = random.nextInt(253) + 1;
		String ip = (new StringBuilder(String.valueOf(i))).append(".")
				.append(j).append(".").append(k).append(".").append(l)
				.toString();
		return ip;
	}

	/**
	 * 验证yb是否为邮政编码
	 * @param zipCode : 要验证的“邮政编码”
	 */
	public static boolean isZipCode(String zipCode) {
		Pattern p;
		Matcher m;
		if(zipCode!=null){
			// 根据长度判断是否为一般邮编，还是特殊邮编(山西、河北、内蒙古)
			switch (zipCode.length()) {
				case 5:
					p = Pattern.compile("[1-7][0-9]{4}");
					break;
				case 6:
					p = Pattern.compile("[1-8][0-9]{5}");
					break;
				default:
					return false;
			}
			m = p.matcher(zipCode);
			if (m.matches()) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 验证IDcard的身份证号码格式是否正确
	 * @param IDcard : 要验证的“身份证号码”
	 */
	public static boolean isIdCard(String IDcard) {
		if(IDcard!=null){
			IDcard=IDcard.toUpperCase();
			int IDcard_int[] = new int[17]; // 身份证号码，转为int数组(只取前17位数字)
			int quan[] = { 7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2 }; // 身份证校验码的权
			String yzm[] = { "1", "0", "X", "9", "8", "7", "6", "5", "4", "3", "2" }; // 校验码
			int sum = 0; // 累加权和
			int mo; // 计算结果的模式
	
			// 判断“身份证号码”长度是否为18位
			if (IDcard.length() == 18) {
				// 判断日期“月”的格式
				if (IDcard.charAt(10) == '0') {
					// 判断日期“日”的格式
					if (IDcard.charAt(12) == '0') {
					} else if (IDcard.charAt(12) >= '1' && IDcard.charAt(10) <= '2') {
					} else if (IDcard.charAt(12) == '3') {
					} else {
						return false;
					}
				} else if (IDcard.charAt(10) == '1') {
					// 判断日期“日”的格式
					if (IDcard.charAt(12) == '0') {
					} else if (IDcard.charAt(12) >= '1' && IDcard.charAt(10) <= '2') {
					} else if (IDcard.charAt(12) == '3') {
					} else {
						return false;
					}
				} else {
					return false;
				}
			} else {
				return false;
			}
			// 拆分IDcard为int型
			for (int i = 0; i < IDcard.length() - 1; i++) {
				try {
					IDcard_int[i] = Integer.parseInt(String.valueOf(IDcard.charAt(i)));
				}catch(Exception e) {
					return false;
				}
			}
			// 累加"权"之和
			for (int i = 0; i < quan.length; i++) {
				int s = quan[i] * IDcard_int[i];
				sum += s;
			}
			// 计算出的模
			mo = sum % 11;
			// 判断身份证号码格式是否正确
			if (IDcard.equals(IDcard.subSequence(0, IDcard.length() - 1) + yzm[mo])) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 随机获取"大陆身份证"号码
	 */
	public static String getRandomIdCard() {
		String xzCode = null; // 行政区代码

		String date = "19"; // 日期
		int m = 0; // 准日期(月)
		int d = 0; // 准日期(日)
		boolean flag = true; // 准日期

		String order = ""; // 出生顺序“号码”
		String IDcard; // 身份证号码

		int sum = 0; // 累加“权”之和
		int quan[] = { 7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2 }; // 身份证校验码的权
		String yzm[] = { "1", "0", "X", "9", "8", "7", "6", "5", "4", "3", "2" }; // 校验码

		/* 随机获取行政区代码(共6位数) */
		while (flag) {
			String code = "" + (int) (Math.random() * 1000000);
			if (code.length() >= 6 && code.charAt(0) <= '6'
					&& code.charAt(1) < '5' && code.charAt(2) < '4'
					&& code.charAt(3) < '3' && code.charAt(4) < '3') {
				xzCode = "" + code;
				flag = false;
			}
		}
		flag = true;
		/* 随机获取日期中的"年"、"月、"日"(8位数) */
		// 年
		int y = (int) (Math.random() * 100);
		if (y < 10) {
			date += "0" + y;
		} else {
			date += y;
		}
		// 月
		while (flag) {
			m = (int) (Math.random() * 100);
			if (m <= 12 && m > 0) {
				flag = false;
			}
		}
		if (String.valueOf(m).length() == 1) {
			date += "0" + m;
		} else {
			date += m;
		}
		flag = true;
		// 日
		while (flag) {
			d = (int) (Math.random() * 100);
			if (d <= 31 && d > 0 && m != 2) {
				flag = false;
			} else if (d <= 29 && d > 0 && m == 2) {
				flag = false;
			}
		}
		if (String.valueOf(d).length() == 1) {
			date += "0" + d;
		} else {
			date += d;
		}
		flag = true;
		/* 随机获取顺序码(3位数) */
		while (flag) {
			String sx = String.valueOf((int) (Math.random() * 1000));
			if (sx.length() == 1) {
				order = "00" + sx;
				flag = false;
			} else if (sx.length() == 2) {
				order = "0" + sx;
				flag = false;
			} else {
				order = sx;
				flag = false;
			}
		}
		IDcard = xzCode + date + order; // 17位数准备身份证
		/* 计算出的模 */
		for (int i = 0; i < quan.length; i++) {
			int s = quan[i]
					* Integer.parseInt(String.valueOf(IDcard.charAt(i)));
			sum += s;
		}
		m = sum % 11;
		IDcard += yzm[m];
		return IDcard;

	}

	/**
	 * 验证url的格式为正确的网址格式
	 * @param url : 需要验证的url
	 */
	public static boolean isURL(String url) {
		if (url(url, false).equals("1")) {
			return true;
		}
		return false;

	}

	/**
	 * 获取value里的URL地址
	 * @param value : 需要获取URL地址的String
	 */
	public static String getURL(String value) {
		return RegexUtil.url(value, true);
	}

	/***
	 * 获取：对str加密后的MD5
	 * @param str
	 */
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

	/**
	 * 得到随机(英文字母)
	 * @param n : 随机获取n长度的文字
	 */
	public static String getRandEngLish(int n) {
		String text = "abcdefghijklmnopqrstuvwxyz";
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < n; i++) {
			sb.append(text.charAt(new Random().nextInt(text.length())));
		}
		return sb.toString();
	}

	/**
	 * 获取当前日期时间（标准式，格式：yyyy-MM-dd HH:mm:ss）
	 */
	public static String getDate() {
		return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
	}

	/**
	 * 获取当前日期时间（中文式，格式：yyyy年MM月dd日 HH:mm:ss）
	 */
	public static String getDateChina() {
		return new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss").format(new Date());
	}

	/**
	 * 获取当前日期时间（下载式，格式：yyyy-MM-dd_HH.mm.ss）
	 */
	public static String getDateDL() {
		return new SimpleDateFormat("yyyy-MM-dd_HH.mm.ss").format(new Date());
	}

	/**
	 * 获取当前日期（格式：yyyy-MM-dd）
	 */
	public static String getDateRQ() {
		return new SimpleDateFormat("yyyy-MM-dd").format(new Date());
	}

	/**
	 * 获取两个时间的差
	 * @param old_Time : 以前的时间
	 * @param new_time : 以后的时间(一般是当前的时间)
	 * @return 如：（1）、32秒；（2）、5分12秒；（3）、11小时55分32秒（4）、3天6小时28分别45秒
	 */
	public static String getMinusTime(String old_Time, String new_time) {
		String sb = "";
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date now = null;
		Date date = null;
		try {
			date = df.parse(old_Time); // 以前
			now = df.parse(new_time); // 当前
		} catch (ParseException e) {
			throw new IllegalArgumentException(e.getMessage(),e);
		}
		long l = now.getTime() - date.getTime();
		long day = l / (24 * 60 * 60 * 1000);
		long hour = (l / (60 * 60 * 1000) - day * 24);
		long min = ((l / (60 * 1000)) - day * 24 * 60 - hour * 60);
		long s = (l / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);
		if (day > 0) {
			sb = "" + day + "天" + hour + "小时" + min + "分" + s + "秒";
		} else if (hour > 0) {
			sb = "" + hour + "小时" + min + "分" + s + "秒";
		} else if (min > 0) {
			sb = "" + min + "分" + s + "秒";
		} else {
			sb = "" + s + "秒";
		}
		return sb;
	}

	/**
	 * 获取:"当前日期时间"距离"指定的日期时间"多远(格式：2013-11-02 18:23:22)
	 * @param old_time : 以前的时间
	 * @return 返回old_time和当前日期时间相差多远,比如：【5小时前】、【39分钟前】、【14秒前】
	 */
	public static String getDateCha(String old_time) {
		String result = null;
		try {
			String new_time = new SimpleDateFormat("yyyy,MM,dd,HH,mm,ss").format(new Date());	// 当前的日期时间
			String old_split[] = old_time.split(" ");
			if (old_split.length == 2) {
				// 获取日期+时间(数组)
				String old_rq[] = old_split[0].split("-");
				String old_sj[] = old_split[1].split(":");
				// "日期+时间"从左至右组合成1个数组
				String old[] = { old_rq[0], old_rq[1], old_rq[2], old_sj[0],
						old_sj[1], old_sj[2] };
				String _new[] = new_time.split(",");
				// 单位组
				String unitArr[] = { "年", "月", "天", "小时", "分钟", "秒" };
				// 日期时间间的单位换算(比如：1年换算成12个月)
				int into[] = { 100, 12, 30, 24, 60, 60 };
				// 记录不同的日期时间
				int record = 0;
				String unit = null;
				if (old.length == 6 && _new.length == 6) {
					for (int i = 0; i < 6; i++) {
						int old_temp = Integer.parseInt(old[i]);
						int _new_temp = Integer.parseInt(_new[i]);
						if (record > 0) {
							if (_new_temp < old_temp) {
								if (record == 1) {
									record = into[i] - (old_temp - _new_temp);
									unit = unitArr[i];
								} else if (record > 1) {
									record--;
									break;
								}
							} else if (_new_temp >= old_temp) {
								break;
							}
						} else if (record == 0) {
							if (_new_temp > old_temp) {
								record = _new_temp - old_temp;
								unit = unitArr[i];
							} else if (_new_temp < old_temp) {
								break;
							}
						}
					}
					result = record <= 0 ? "刚刚" : record + unit + "前";
				}
			}
		} catch (NumberFormatException e) {
			throw e;
		}
		return result;
	}

	/**
	 * 计算指定的时间和当前时间的差距（以毫秒为单位）
	 * @throws ParseException 
	 */
	public static Long getDataDifference(String date) throws ParseException {
		return getDataDifference(date, RegexUtil.getDate());
	}
	/**
	 * 计算指定的2个时间的差距（以毫秒为单位）
	 * @throws ParseException 
	 */
	public static Long getDataDifference(String date,String date2) throws ParseException {
		long between = 0;
		SimpleDateFormat dfs = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date begin = dfs.parse(date);
		Date end = dfs.parse(date2);
		between = end.getTime() - begin.getTime() ;// 除以1000是为了转换成秒
		return between;
	}
	/**
	 * 获取指定日期时间【往后或往前】多久的日期时间（字符串返回,未指定date则为当前日期时间）
	 * @param date : 指定的日期时间字符串，为空则实用当前日期时间【格式：yyyy-MM-dd HH:mm:ss】
	 * @param dateTimeType : 往后或往前的日期时间类型（1=秒；2=分；3=时；4=天；5=月；6=年）
	 * @param num : 往后或往前指定多久的日期时间（如：-3，5）
	 * @throws IllegalArgumentException : date参数不符合规则
	 */
	public static String getDateTimeAfter(String date,int dateTimeType,int num) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Calendar now = Calendar.getInstance();
		try {
			Date d;
			if (date!= null) {
				d = sdf.parse(date);
			} else {
				d = new Date();
			}
			now.setTime(d);
			int t_dateTimeType=4;		//默认
			switch(dateTimeType){
				case 1:t_dateTimeType=Calendar.SECOND;break;
				case 2:t_dateTimeType=Calendar.MINUTE;break;
				case 3:t_dateTimeType=Calendar.HOUR_OF_DAY;break;
				case 4:t_dateTimeType=Calendar.DATE;break;
				case 5:t_dateTimeType=Calendar.MONTH;break;
				case 6:t_dateTimeType=Calendar.YEAR;break;
			}
			now.set(t_dateTimeType, now.get(t_dateTimeType) + num);
		} catch (ParseException e) {
			throw new IllegalArgumentException(e);
		}
		return sdf.format(now.getTime());
	}
	/**
	 * 删除字符串中的敏感词(非法关键词)
	 * @param str : 需要替换的String
	 * @return 替换后的str
	 */
	public static String deleteWords(String str) {
		String illegalKeyWords = ",大法,法轮,法一轮,李洪志,大纪元,真善忍,新唐人,六合,阴唇,肉棍,阴户,强奸,淫靡,淫水,阴茎,阴蒂,乱伦,手淫,做鸡,六四事件,江泽民,胡锦涛,罗干,朱镕基,李鹏,江主席,温家宝,迷药,迷魂药,新生网,口交,天安门,共产主义,共产党,反共,达赖,反革命,反华,盗取,绕过封锁,新疆独立,西藏独立,民运分子,打倒,推翻,多党执政,专制,国民党,成人小说,成人电影,裸体,激情图片,激情电影,自杀手册";
		illegalKeyWords = illegalKeyWords.replaceAll(",,", ",");
		String[] illegalArray = illegalKeyWords.split(",");
		for (int i = 0; i < illegalArray.length; i++) {
			if (!illegalArray[i].trim().equals("") && str.indexOf(illegalArray[i].trim()) != -1) {
				str = str.replace(illegalArray[i].trim(), "");
			}
		}
		return str;
	}
	/**验证当前字符串是否不全是是数字* */
	public static boolean isNotInteger(String str) {
		return !isInteger(str);
	}
	/**验证当前字符串是否全都是数字* */
	public static boolean isInteger(String str) {
		return str!=null?str.matches("\\d+"):false;
	}
	/**验证当前字符串是否为数字类型（包含：整型、实数）**/
	public static boolean isNumber(String str) {
		boolean result=isInteger(str);
		if(!result){
			result=str!=null?str.matches("^\\d+\\.\\d+"):false;
		}
		return result;
	}
	/**
	 * 截取字符串(单个截取)
	 * @param html : HTML内容
	 * @param start : 开始部分（null=0）
	 * @param end : 结尾部分(null=html最后一个字符串的位置)
	 * @return 返回开始和结尾中间的内容
	 */
	public static String getCull(String html, String start, String end) {
		if (html!=null) {
			int start1 =0;
			int end1 = html.length();
			if(start!=null){
				int start2=html.indexOf(start);
				if(start2!=-1){
					start1=start2+ start.length();
				}else{
					return "";
				}
			}
			if (end != null) {
				end1 = html.indexOf(end, start1);
			}
			if (start1 > end1) {
				return "";
			}
			return html.substring(start1, end1);
		} else {
			return "";
		}
	}

	/**
	 * 计算字符串长度(字符：1个,汉字：2个)
	 */
	public static int strLength(String value) {
		int valueLength = 0;
		if (value != null) {
			String chinese = "[\u0391-\uFFE5]";
			/* 获取字段值的长度，如果含中文字符，则每个中文字符长度为2，否则为1 */
			for (int i = 0; i < value.length(); i++) {
				/* 获取一个字符 */
				String temp = value.substring(i, i + 1);
				/* 判断是否为中文字符 */
				if (temp.matches(chinese)) {
					/* 中文字符长度为2 */
					valueLength += 2;
				} else {
					/* 其他字符长度为1 */
					valueLength += 1;
				}
			}
		}
		return valueLength;
	}

	/**
	 * 格式化"数字"成"国际数字"(以3位数分隔1次，比如：100,000,000)
	 */
	public static String formatNumber(String number) {
		number = number != null ? number : "";
		String size = "";
		for (int i = 0; i < number.length(); i += 3) {
			if (number.length() - i > 3) {
				size += number.substring(i, i + 3) + ",";
			} else {
				size += number.substring(i);
			}
		}
		return size;
	}

	/**
	 * 转换换行符为HTML的换行符
	 * @param str :需要转换HTML的换行符
	 */
	public static String newLine(String str) {
		StringBuffer sb = new StringBuffer(str != null ? str : "");
		sb = new StringBuffer(sb.toString().replaceAll("\r\n", "<br>"));
		if (sb.indexOf("\r") != -1) {
			sb = new StringBuffer(sb.toString().replaceAll("\r", "<br />"));
		}
		if (sb.indexOf("\n") != -1) {
			sb = new StringBuffer(sb.toString().replaceAll("\n", "<br />"));
		}
		return sb.toString();
	}

	/**
	 * 解决乱码的问题
	 * @param str : 可能出现乱码的字符串
	 * @return 返回正常的字符串
	 */
	public static String getNormalStr(String str){
		try {
			try{
				str=java.net.URLDecoder.decode(str.trim(),"utf-8");
				//浏览器get编码——IE【gb2312】,Fifefox【UTF-8】
				String code[]={"UTF-8","gb2312"};
				for (int i = 0; i < code.length; i++) {
					if (!RegexUtil.isHanzi(str)) {
						String temp=str;
						str = str!=null?new String(str.getBytes("ISO-8859-1"),code[i]):"";
						if (RegexUtil.isHanzi(str)) {
							break;
						}else {
							str=temp;
						}
					}else{
						break;
					}
				}
			}catch(Exception e1){
				//
			}
			try{
				//Unicode（\\u）
				char aChar;
				int len = str.length();
				StringBuffer outBuffer = new StringBuffer(len);
				for (int x = 0; x < len;) {
					aChar = str.charAt(x++);
					if (aChar == '\\') {
						aChar = str.charAt(x++);
						if (aChar == 'u') {
							int value = 0;
							for (int i = 0; i < 4; i++) {
								aChar = str.charAt(x++);
								switch (aChar) {
									case '0':
									case '1':
									case '2':
									case '3':
									case '4':
									case '5':
									case '6':
									case '7':
									case '8':
									case '9':value = (value << 4) + aChar - '0';break;
									case 'a':
									case 'b':
									case 'c':
									case 'd':
									case 'e':
									case 'f':value = (value << 4) + 10 + aChar - 'a';break;
									case 'A':
									case 'B':
									case 'C':
									case 'D':
									case 'E':
									case 'F':value = (value << 4) + 10 + aChar - 'A';break;
									default:throw new IllegalArgumentException("Malformed      encoding.");
								}
							}
							outBuffer.append((char) value);
						} else {
							if (aChar == 't') {
								aChar = '\t';
							} else if (aChar == 'r') {
								aChar = '\r';
							} else if (aChar == 'n') {
								aChar = '\n';
							} else if (aChar == 'f') {
								aChar = '\f';
							}
							outBuffer.append(aChar);
						}
					} else {
						outBuffer.append(aChar);
					}
				}
				if(outBuffer.toString().trim().length()>0){
					return outBuffer.toString();
				}
			}catch(Exception e2){
				//
			}
			//Unicode（&#）
			Pattern pattern = Pattern.compile("\\&\\#(\\d+)"); 
			StringBuilder sb = new StringBuilder();
			String[] childs = str.split(";");  
	        for(String child : childs){
	             if(child.contains("&#")){  
	                 Matcher m =pattern.matcher(child);  
	                 while (m.find()){
	                      sb.append((char)Integer.valueOf(m.group(1)).intValue());    
	                 }
	             }else{
	                sb.append(child);  
	             }  
			}
	        str=sb.toString();
		}catch(NumberFormatException e){
			throw e;
		}
		return str;
	}
	/**
	 * 判断phoneNum参数是否为手机号码
	 */
	public static boolean isMobilePhone(String phoneNum) {
		if(phoneNum!=null){
			Pattern p = Pattern.compile("^1([0-9]|8)[0-9]{9}$");
			Matcher m = p.matcher(phoneNum);
			if (m.find()) {
				return true;
			} else {
				return false;
			}
		}else{
			return false;
		}
	}

	/**
	 * 随机获取一个手机号码
	 */
	public static String getMobilePhone() {
		Random r = new Random();
		String sectionArr[] = { "0","1","2","3", "4", "5","6","7","8","9"}; // 号码段组，如13，14，15，18
		String section = sectionArr[r.nextInt(sectionArr.length)];
		String num = String.valueOf(Math.random()); // 生成随机数
		num = num.split("\\.")[1]; // 拆分，已【.】为分隔符，取【.】后面的数字
		num = num.substring(0, 9); // 截取前9个数字

		String phoneNum = "1" + section + num;
		Pattern p = Pattern.compile("^1([0-9]|8)[0-9]{9}$");
		Matcher m = p.matcher(phoneNum);
		if (m.find()) {
			return phoneNum;
		} else {
			return null;
		}
	}

	/**删除：验证码的特殊字符**/
	public static String deleteSpecial(String valCode){
		valCode=valCode.trim();
		valCode=valCode.replaceAll(" ", "");
        valCode=valCode.replaceAll("'", "");
        valCode=valCode.replaceAll("‘", "");
        valCode=valCode.replaceAll(":", "");
        valCode=valCode.replaceAll(",", "");
        valCode=valCode.replaceAll("-", "");
        valCode=valCode.replaceAll("“", "");
        valCode=valCode.replaceAll(";", "");
        valCode=valCode.replaceAll("’", "");
        valCode=valCode.replaceAll("»", "");
        valCode=valCode.replaceAll("/", "");
        valCode=valCode.replaceAll("¢", "");
        valCode=valCode.replaceAll("\\.", "");
        valCode=valCode.replaceAll("}", "");
        valCode=valCode.replaceAll("\\\\", "");
        valCode=valCode.replaceAll("_", "");
        return valCode;
	}
	
	// 通用“获取”或“验证”URL
	public static String url(String url, boolean isGetURL) {
		Pattern p;
		Matcher m;
		if (url == null || url.length() <= 0) {
			return "-1";
		}
		if (isGetURL) {
			p = Pattern
					.compile("([hH][tT][tT][pP][sS]?://(w|W){0,3}\\.?(\\d|[a-zA-Z])+\\.([com]|[Com]|[COm]|[COM]|[CoM]|[cOm]|[cOM]|[coM]|[net]|[Net]|[NEt]|[NET]|[NeT]|[nEt]|[nET]|[neT]|[org]|[Org]|[ORg]|[ORG]|[OrG]|[oRg]|[oRG]|[orG]|[edu]|[Edu]|[EDu]|[EDU]|[EdU]|[eDu]|[eDU]|[edU]|[mil]|[Mil]|[MIl]|[MIL]|[MiL]|[mIl]|[mIL]|[miL]|[gov]|[Gov]|[GOv]|[GOV]|[GoV]|[gOv]|[gOV]|[goV]|[cn]|[Cn]|[CN]){2,3})");
		} else {
			if (url.substring(0, 5).equalsIgnoreCase("https")) {
				if (url.charAt(8) != '.') {
					p = Pattern
							.compile("[hH][tT][tT][pP][sS]://(w|W){0,3}\\.?(\\d|[a-zA-Z])+\\.([com]|[Com]|[COm]|[COM]|[CoM]|[cOm]|[cOM]|[coM]|[net]|[Net]|[NEt]|[NET]|[NeT]|[nEt]|[nET]|[neT]|[org]|[Org]|[ORg]|[ORG]|[OrG]|[oRg]|[oRG]|[orG]|[edu]|[Edu]|[EDu]|[EDU]|[EdU]|[eDu]|[eDU]|[edU]|[mil]|[Mil]|[MIl]|[MIL]|[MiL]|[mIl]|[mIL]|[miL]|[gov]|[Gov]|[GOv]|[GOV]|[GoV]|[gOv]|[gOV]|[goV]|[cn]|[Cn]|[CN]){2,3}");
				} else {
					return "-1";
				}
			} else if (url.substring(0, 4).equalsIgnoreCase("http")) {
				if (url.charAt(7) != '.') {
					p = Pattern
							.compile("[hH][tT][tT][pP]://(w|W){0,3}\\.?(\\d|[a-zA-Z])+\\.([com]|[Com]|[COm]|[COM]|[CoM]|[cOm]|[cOM]|[coM]|[COM]|[net]|[Net]|[NEt]|[NET]|[NeT]|[nEt]|[nET]|[neT]|[NET]|[org]|[Org]|[ORg]|[ORG]|[OrG]|[oRg]|[oRG]|[orG]|[ORG]|[edu]|[Edu]|[EDu]|[EDU]|[EdU]|[eDu]|[eDU]|[edU]|[EDU]|[mil]|[Mil]|[MIl]|[MIL]|[MiL]|[mIl]|[mIL]|[miL]|[MIL]|[gov]|[Gov]|[GOv]|[GOV]|[GoV]|[gOv]|[gOV]|[goV]|[GOV]|[cn]|[Cn]|[CN]){2,3}");
				} else {
					return "-1";
				}
			} else if (url.substring(0, 4).equalsIgnoreCase("www.")) {
				p = Pattern
						.compile("(w|W){0,3}\\.(\\d|[a-zA-Z])+\\.([com]|[Com]|[COm]|[COM]|[CoM]|[cOm]|[cOM]|[coM]|[COM]|[net]|[Net]|[NEt]|[NET]|[NeT]|[nEt]|[nET]|[neT]|[NET]|[org]|[Org]|[ORg]|[ORG]|[OrG]|[oRg]|[oRG]|[orG]|[ORG]|[edu]|[Edu]|[EDu]|[EDU]|[EdU]|[eDu]|[eDU]|[edU]|[EDU]|[mil]|[Mil]|[MIl]|[MIL]|[MiL]|[mIl]|[mIL]|[miL]|[MIL]|[gov]|[Gov]|[GOv]|[GOV]|[GoV]|[gOv]|[gOV]|[goV]|[GOV]|[cn]|[Cn]|[CN]){2,3}");
			} else if (url.charAt(0) != '.') {
				p = Pattern
						.compile("(\\d|[a-zA-Z])+\\.([com]|[Com]|[COm]|[COM]|[CoM]|[cOm]|[cOM]|[coM]|[COM]|[net]|[Net]|[NEt]|[NET]|[NeT]|[nEt]|[nET]|[neT]|[NET]|[org]|[Org]|[ORg]|[ORG]|[OrG]|[oRg]|[oRG]|[orG]|[ORG]|[edu]|[Edu]|[EDu]|[EDU]|[EdU]|[eDu]|[eDU]|[edU]|[EDU]|[mil]|[Mil]|[MIl]|[MIL]|[MiL]|[mIl]|[mIL]|[miL]|[MIL]|[gov]|[Gov]|[GOv]|[GOV]|[GoV]|[gOv]|[gOV]|[goV]|[GOV]|[cn]|[Cn]|[CN]){2,3}");
			} else {
				return "-1";
			}
		}
		m = p.matcher(url);
		if (m.find()) {
			if (isGetURL) {
				return m.group(1);
			} else {
				return "1";
			}
		}
		return "-1";

	}

	// 通用“获取”或“验证”Email
	private static String email(String email, boolean isGetEmail) {
		Pattern p;
		Matcher m;
		if(email.indexOf("@")!=-1){
			String user = email.substring(0, email.indexOf("@"));
			// 手机邮箱（如果第1个字符是数字,则是）
			if (email != null && email.substring(0, 1).indexOf("1") == 0 && user.length() == 11) {
				p = Pattern.compile("1([3-5]|8)[0-9]*@([a-zA-Z]|[0-9])*\\.[a-zA-Z]{1,3}");
				m = p.matcher(email);
				if (isGetEmail) {
					if (m.find()) {
						return m.group(1);
					} else {
						return null;
					}
				} else {
					if (m.find() && user.length() == 11) {
						return "3";
					} else {
						return "0";
					}
				}
			// QQ邮箱
			} else if (email != null && user.substring(0, 1).matches("\\d") && user.length() <= 10) {
				p = Pattern.compile("[1-9]{5,10}@[q|Q]{2}\\.com");
				m = p.matcher(email);
				if (isGetEmail) {
					if (m.find()) {
						return m.group(1);
					} else {
						return null;
					}
				} else {
					if (m.find()) {
						return "2";
					} else {
						return "0";
					}
				}
	
			// 正常邮箱
			} else if (email != null && email.length() >= 6) {
				p = Pattern.compile("(\\p{Lower}|\\p{Upper}).*@(\\p{Lower}|\\p{Upper}|[0-9])*\\.\\p{Lower}|\\p{Upper}{1,3}");
				m = p.matcher(email);
				if (isGetEmail) {
					if (m.find()) {
						return m.group(1);
					} else {
						return null;
					}
				} else {
					if (m.find() && email.indexOf("_") != 0 && user.indexOf("_") != user.length() - 1) {
						return "1";
					} else {
						return "0";
					}
				}
			} else if (isGetEmail) {
				return null;
			} else {
				return "0";
			}
		}else{
			return "0";
		}
	}

	// 通用“验证”或“获取”正确的日期字符串
	private static String date(String date, boolean isGetDate) {
		Pattern p = Pattern.compile("(\\d{1,4}(年|-|/|\\.){1}[0-9]{1,2}(月|-|/|\\.){1}[0-9]{1,2}(日|\\s){0,1})");
		Matcher m = p.matcher(date);
		String xDate = null;
		String regex = null;
		// 捕捉日期
		if (m.find()) {
			xDate = m.group(1);
		}
		if (xDate != null && xDate.indexOf("-") != -1) {
			regex = "-";
		} else if (xDate != null && xDate.indexOf("/") != -1) {
			regex = "/";
		} else if (xDate != null && xDate.indexOf(".") != -1) {
			regex = ".";
		} else if (xDate != null && xDate.indexOf("年") != -1 && xDate.indexOf("月") != -1 && xDate.indexOf("日") != -1) {
			//
		} else {
			return "-1";
		}
		int jl = 0; // 记录regex的次数,2次代表暂时是正确的日期
		String pp[];
		for (int i = 0; i < xDate.length(); i++) {
			if (xDate.charAt(i) == regex.charAt(0)) {
				jl++;
			}
		}
		// 2次为正常的日期格式
		if (jl == 2) {
			pp = xDate.split(regex);
			// 判断"日"是否还存有非数字
			p = Pattern.compile("\\d*");
			m = p.matcher(pp[2].trim());
			String day = pp[2].trim();
			if (!m.matches()) {
				day = day.substring(0, day.length() - 1);
			}
			if ((Integer.parseInt(pp[1]) > 0 && Integer.parseInt(pp[1]) <= 12)
					&& (Integer.parseInt(day) > 0 && Integer.parseInt(day) <= 31)) {
				if (isGetDate) {
					return xDate;
				} else {
					return "1";
				}

			}
		}
		return "-1";
	}

	/**
	 * 通过身份证得到姓别
	 * @param idCard
	 * @return 默认为0(女)，1（男）
	 */
	public static String getIdCardSex(String idCard) {
		String sexF="0";
		if(isIdCard(idCard)){
			String sex = idCard.substring(16, 17);
			if(Integer.parseInt(sex)%2==0){
				sexF= "0";
			}else{
				sexF ="1";
			}
		}
		return sexF;
	}
	
	/**
	 * 通过身份证得到年龄
	 * @param idCard
	 */
	public static int getIdCardAge(String idCard){
		int age=0;
		if(isIdCard(idCard)){
			String bday = idCard.substring(6,14);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
			Calendar c = Calendar.getInstance();
			try {
				c.setTime(sdf.parse(bday));
			} catch (ParseException e) {
				throw new IllegalArgumentException(e.getMessage(),e);
			}
			Date d = c.getTime();
			age = (int) ((new Date().getTime()-d.getTime())/1000/60/60/24/365);
		}
		return age;
	}
	/** 
     * 根据日期获得星期
     * @param date
     */ 
	public static String getWeekOfDate(Date date) { 
//	  String[] weekDaysName = { "星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六" }; 
	  String[] weekDaysCode = { "0", "1", "2", "3", "4", "5", "6" }; 
	  Calendar calendar = Calendar.getInstance(); 
	  calendar.setTime(date); 
	  int intWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1; 
	  return weekDaysCode[intWeek]; 
	} 
	/**
	 * 获取 : 随机字符串（含各种各样的字符串，如：数字、特殊符号）——根据指定长度生成<br />
	   说明：<br />
	  			//0~9的ASCII为48~57<br />
    			//A~Z的ASCII为65~90<br />
    			//a~z的ASCII为97~122<br />
	 * @param length : 指定生成的长度
	 * @param isSpecial : 是否可以生成特殊的字符
	 */
    public static String getRandomStr(int length,boolean isSpecial) {
        StringBuilder sb=new StringBuilder();
        Random rand=new Random();//随机用以下三个随机生成器
        String special[]={"#","_","$","%","@","!"};
        int randomSeed=isSpecial?4:3;
        for(int i=0;i<length;i++){
            int index=rand.nextInt(randomSeed);
            int data=0;
            switch(index) {
	            case 0:{data=rand.nextInt(10)+48;break;}	//保证只会产生0~9之间的整数
	            case 1:{data=rand.nextInt(26)+65;break;}	//保证只会产生65~90之间的ASCII码（大写字母）
	            case 2:{data=rand.nextInt(26)+97;break;}	//保证只会产生97~122之间的ASCII码（小写字母）
            }
            if(index!=3){
            	sb.append((char)data);
            //特殊字符
            }else{
            	int t_index=rand.nextInt(special.length);
            	String t_special=special[t_index];
            	sb.append(t_special);
            }
        }
        return sb.toString();
    }
    //是否 : 移动端设备
  	public static boolean isMobileDevice(String str){
  		if(str!=null){
	  		Pattern p=Pattern.compile("(iPhone|iPod|Android|ios)");
	  		Matcher m=p.matcher(str);
	  		return m.find();
  		}else{
  			return false;
  		}
  	}
  	/**获取 : html的文本（去掉html标签）**/
  	public static String getText(String html){
  		String regEx_script="<script[^>]*?>[\\s\\S]*?<\\/script>"; //定义script的正则表达式 
        String regEx_style="<style[^>]*?>[\\s\\S]*?<\\/style>"; //定义style的正则表达式 
        String regEx_html="<[^>]+>"; //定义HTML标签的正则表达式 
         
        Pattern p_script=Pattern.compile(regEx_script,Pattern.CASE_INSENSITIVE); 
        Matcher m_script=p_script.matcher(html); 
        html=m_script.replaceAll(""); //过滤script标签 
         
        Pattern p_style=Pattern.compile(regEx_style,Pattern.CASE_INSENSITIVE); 
        Matcher m_style=p_style.matcher(html); 
        html=m_style.replaceAll(""); //过滤style标签 
         
        Pattern p_html=Pattern.compile(regEx_html,Pattern.CASE_INSENSITIVE); 
        Matcher m_html=p_html.matcher(html); 
        html=m_html.replaceAll(""); //过滤html标签 
        return html.trim();
  	}
  	/**清除左右空格的数据，如果长度为0，则返回null**/
	public static String clearEmpty(String str){
		return str!=null && str.trim().length()>0?str.trim():null;
	}
}