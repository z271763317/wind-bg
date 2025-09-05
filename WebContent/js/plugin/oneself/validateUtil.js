/*
 * 表单内元素的属性说明：【val-type】或【valid-type】或【validation-type】，可选值（多个请以【空格】分隔）
 * 
 * empty：是否为空
 * json：是否为json字符串
 * allChinese：是否全部为中文字符
 * existChinese：是否存在中文字符
 * notExistChinese：是否没有中文字符
 * number：是否为数字
 * int：是否为整数
 * notNumber : 是否不全为数字
 * negative：是否为负数（含小数）
 * positive：是否为正数（含小数）
 * ip：是否为ip
 * CIDR：是否为无类域内路由
 * mobile：是否为手机号
 * email：是否为email邮箱
 * phone：是否为电话
 * tel：是否为联系电话（含：手机号、电话号）
 * idCard：是否为身份证
 * image：是否为image图片文本类型
 */

/**
 * 这是一个验证数据格式的工具类
 */
var ValidateUitl = {};
//验证类型的属性名
var valTypeArr=["val-type","valid-type","validation-type"];	
//验证类型对应的错误信息
var errorMsgMap={"empty":"该项不能为空"};
errorMsgMap["json"]="不符合json格式";
errorMsgMap["allChinese"]="不全是中文字符";
errorMsgMap["existChinese"]="不存在中文字符";
errorMsgMap["notExistChinese"]="不能有中文字符";
errorMsgMap["number"]="不是数字";
errorMsgMap["int"]="不是整数";
errorMsgMap["notNumber"]="不能全为数字";
errorMsgMap["negative"]="不是负数";
errorMsgMap["positive"]="不是正数";
errorMsgMap["ip"]="ip地址格式不正确";
errorMsgMap["CIDR"]="不是CIDR";
errorMsgMap["mobile"]="不是手机号";
errorMsgMap["notMobile"]="不能是手机号";
errorMsgMap["email"]="不是邮箱";
errorMsgMap["notEmail"]="不能是邮箱";
errorMsgMap["phone"]="不是电话号码";
errorMsgMap["notPhone"]="不是电话号码";
errorMsgMap["tel"]="不是联系电话（手机号、电话号）";
errorMsgMap["idCard"]="不是身份证";
errorMsgMap["image"]="不是图片文本类型";

/**
 *  判断元素对象对应验证类型集（取自属性：【val-type】或【valid-type】或【validation-type】）
 *  说明：
 *  	1、返回值如果不为boolean类型的true,则验证错误信息（默认）
 *  	2、元素若是隐藏的将不会验证
 */
ValidateUitl.validate= function(obj) {
	if(!(obj instanceof jQuery)){ 
		obj=$(obj);
	}
	for(var i=0;i<valTypeArr.length;i++){
		var typeValueStr=obj.attr(valTypeArr[i]);
		//若显示则验证
		if(obj.is(":visible")){
			if(typeValueStr!=null){
				var typeValueArr=typeValueStr.split(" ");
				for(var j=0;j<typeValueArr.length;j++){
					var typeValue=typeValueArr[j];
					//是否有值
					if(ValidateUitl.isValidObj(typeValue)){
						var value=obj.val();
						var isCorrect=false;	//是否正确（默认：错误）
						if(trim(value).length>0 || typeValue=='empty'){
							switch(typeValue){
								case "empty": isCorrect=ValidateUitl.isValidObj(value);break;
								case "json": isCorrect=ValidateUitl.isJsonString(value);break;
								case "allChinese": isCorrect=ValidateUitl.isAllChn(value);break;
								case "existChinese": isCorrect=ValidateUitl.isHasChn(value);break;
								case "notExistChinese": isCorrect=!ValidateUitl.isHasChn(value);break;
								case "number": isCorrect=ValidateUitl.isNumber(value);break;
								case "int": isCorrect=ValidateUitl.isNumber(value);break;
								case "notNumber": isCorrect=ValidateUitl.isNotNumber(value);break;
								case "negative": isCorrect=ValidateUitl.isReal(value);break;
								case "positive": isCorrect=ValidateUitl.isReal2(value);break;
								case "ip": isCorrect=ValidateUitl.isIP(value);break;
								case "CIDR": isCorrect=ValidateUitl.isCIDR(value);break;
								case "mobile": isCorrect=ValidateUitl.isMobile(value);break;
								case "notMobile": isCorrect=!ValidateUitl.isMobile(value);break;
								case "email": isCorrect=ValidateUitl.isEmail(value);break;
								case "notEmail": isCorrect=!ValidateUitl.isEmail(value);break;
								case "phone": isCorrect=ValidateUitl.isPhone(value);break;
								case "notPhone": isCorrect=!ValidateUitl.isPhone(value);break;
								case "tel": isCorrect=ValidateUitl.isTel(value);break;
								case "idCard": isCorrect=ValidateUitl.isIDCard(value);break;
								case "image": isCorrect=ValidateUitl.isImage(value);break;
								default:{
									var isReal=ValidateUitl.isReal2(typeValue);
									//最大长度
									if(isReal){
										if(value.length<=typeValue){
											isCorrect=true;
										}else{
											return "不能超过【"+typeValue+"】个字符";
										}
									}else{
										var isMate=/^m|M\d+$/.test(typeValue);			//如：m4、m12等
										//最小长度
										if(isMate){
											var minSize=parseInt(typeValue.substring(1));
											if(value.length>=minSize){
												isCorrect=true; 
											}else{
												return "不能少于【"+minSize+"】个字符";
											}
										}else{
											return "未知的验证类型";
										}
									}
								}
							}
						}else{
							isCorrect=true;
						}
						if(!isCorrect){
							var errorMsg=errorMsgMap[typeValue];
							//存在该类型
							if(ValidateUitl.isValidObj(errorMsg)){
								return errorMsg;
							}
						}
					}
				}
			}
		}
	}
	return true;
}
//删除左右2端的空格
function trim(str){
	if(str==null){
		return "";
	}else{
		return str.replace(/(^\s*)|(\s*$)/g, "");
	}
}
//判断元素是否合法（或为空）
ValidateUitl.isValidObj = function(obj) {
	if (typeof (obj) == "undefined" || obj == null || obj == "") {
		return false;
	}
	return true;
};

//是否空对象
ValidateUitl.isEmptyObject = function(obj) {
	for (var name in obj) {
		return false;
	}
	return true;
};

//判断是否是方法
ValidateUitl.isFunction = function(fn) {
	if (typeof fn != "function") {
		return false;
	}
	return true;
};

//判断是否字符串
ValidateUitl.isString = function(str) {
	if (typeof str != "string") {
		return false;
	}
	return true;
};

//判断是否JSON格式字符串
ValidateUitl.isJsonString = function(jsonstr) {
	var flag = false;
	if (ValidateUitl.isValidObj(jsonstr)) {
		try {
			eval("(" + jsonstr + ")");
			flag = true;
		} catch (e) {
			flag = false;
		}
	}
	return flag;
};

//判断是否全部是中文
ValidateUitl.isAllChn = function(str) {
	var reg = /^[\u4E00-\u9FA5]+$/;
	if (!reg.test(str)) {
		return false;
	}
	return true;
};

//判断字符是否有中文字符
ValidateUitl.isHasChn = function(s) {
	var patrn = /[\u4E00-\u9FA5]|[\uFE30-\uFFA0]/gi;
	if (!patrn.exec(s)) {
		return false;
	} else {
		return true;
	}
};

//判断是否是数字
ValidateUitl.isNumber = function(num) {
	return /^\d+$/.test(num);
};
//判断是否不全为是数字
ValidateUitl.isNotNumber = function(num) {
	var valResult=ValidateUitl.isNumber(num);
	return valResult==false?true:false;
};
//判断是否为负数
ValidateUitl.isReal = function(num) {
	return /^(-)?[0-9][0-9]*$/.test(num);
};
//判断是否为正数
ValidateUitl.isReal2 = function(num) {
	return /^[0-9][0-9]*$/.test(num);
};

//判断是不是IE浏览器
ValidateUitl.isIE = function() {
	return navigator.userAgent.indexOf("MSIE") > 0 || navigator.userAgent.indexOf(".NET") > -1;
};

//判断是不是Chrome浏览器
ValidateUitl.isChrome = function() {
	return navigator.userAgent.indexOf("Chrome") > 0;
};

//判断是不是Opera浏览器
ValidateUitl.isOpera = function() {
	return navigator.userAgent.indexOf("OPR") > 0;
};

//是否是IP
ValidateUitl.isIP = function(strIP) {
	var flag = false;
	if (ValidateUitl.isValidObj(strIP)) {
		var reg = /^(\d+)\.(\d+)\.(\d+)\.(\d+)$/g; // 匹配IP地址的正则表达式
		if (reg.test(jQuery.trim(strIP))) {
			if (RegExp.$1 < 256 && RegExp.$2 < 256 && RegExp.$3 < 256
					&& RegExp.$4 < 256)
				flag = true;
		}
	}
	return flag;
};

//是否是CIDR
ValidateUitl.isCIDR = function(strCIDR) {
	var flag = false;
	if (ValidateUitl.isValidObj(strCIDR)) {
		var reg = /^(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\/([0-9]|[1-2][0-9]|3[0-1])$/;
		if (reg.test(jQuery.trim(strCIDR))) {
			flag = true;
		}
	}
	return flag;
};

//判断是否手机号码格式
ValidateUitl.isMobile = function(phone) {
	var flag = false;
	if (ValidateUitl.isValidObj(phone)) {
		var re = /(^1[0-9][0-9]{9}$)/;
		flag = re.test(phone);
	}
	return flag;
};

//是否是邮箱
ValidateUitl.isEmail = function(str) {
	var myReg = /^[\w-]+(\.[\w-]+)*@[\w-]+(\.[\w-]+)+$/;
	if (myReg.test(str))
		return true;
	return false;
};

//是否是电话支持带区号
ValidateUitl.isPhone = function(strPhone) {
	var phoneRegWithArea = /^[0][1-9][0-9]{1,2}-[0-9]{5,10}$/;
	var phoneRegWithArea1 = /^[0][1-9][0-9]{1,2}[0-9]{5,10}$/;
	var phoneRegNoArea = /^[1-9]{1}[0-9]{5,8}$/;
	if (strPhone.length > 9) {
		if (phoneRegWithArea.test(strPhone)) {
			return true;
		} else {
			if (phoneRegWithArea1.test(strPhone)) {
				return true;
			}
			return false;
		}
	} else {
		if (phoneRegNoArea.test(strPhone)) {
			return true;
		} else {
			return false;
		}
	}
};

//判断是否是联系电话（ (支持区号)固话、移动号码通用）
ValidateUitl.isTel = function(num) {
	if (ValidateUitl.isNumber(num)) {// 先判断是否是数字
		if (num.length == 11) {// 如果是11位则为移动电话
			if (ValidateUitl.isMobile(num)) {
				return true;
			} else {
				return false
			}
		} else {// 固话
			if (ValidateUitl.isPhone(num)) {
				return true;
			} else {
				return false
			}
		}
	} else {
		return false;
	}
};

//是否是合法身份证
ValidateUitl.isIDCard = function(idCard) {
	idCard = trim(idCard.replace(/ /g, "")); // 去掉字符串头尾空格
	if (idCard.length == 18) {
		var a_idCard = idCard.split(""); // 得到身份证数组
		if (isValidityBrithBy18IdCard(idCard)
				&& isTrueValidateCodeBy18IdCard(a_idCard)) { // 进行18位身份证的基本验证和第18位的验证
			return true;
		} else {
			return false;
		}
	} else {
		return false;
	}
};

//判断身份证号码为18位时最后的验证位是否正确（a_idCard=身份证号码数组）
function isTrueValidateCodeBy18IdCard(a_idCard) {
	var Wi = [ 7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2, 1 ]; // 加权因子
	var ValideCode = [ 1, 0, 10, 9, 8, 7, 6, 5, 4, 3, 2 ]; // 身份证验证位值.10代表X
	var sum = 0; // 声明加权求和变量
	if (a_idCard[17].toLowerCase() == 'x') {
		a_idCard[17] = 10; // 将最后位为x的验证码替换为10方便后续操作
	}
	for (var i = 0; i < 17; i++) {
		sum += Wi[i] * a_idCard[i]; // 加权求和
	}
	valCodePosition = sum % 11; // 得到验证码所位置
	if (a_idCard[17] == ValideCode[valCodePosition]) {
		return true;
	} else {
		return false;
	}
}

//验证18位数身份证号码中的生日是否是有效生日
function isValidityBrithBy18IdCard(idCard18) {
	var year = idCard18.substring(6, 10);
	var month = idCard18.substring(10, 12);
	var day = idCard18.substring(12, 14);
	var temp_date = new Date(year, parseFloat(month) - 1, parseFloat(day));
	// 这里用getFullYear()获取年份，避免千年虫问题
	if (temp_date.getFullYear() != parseFloat(year)
			|| temp_date.getMonth() != parseFloat(month) - 1
			|| temp_date.getDate() != parseFloat(day)) {
		return false;
	} else {
		return true;
	}
}

//是否是图片文本类型
ValidateUitl.isImage = function(strImage) {
	return (/image/i).test(strImage);
};

//待完善 判断特殊字符
ValidateUitl.isContainSpecialChar = function(str) {
	var rtn = {
		flag : false,
		message : "not"
	};
	var reg = /[~$%^￥]+/;
	if (str != "") {
		if (reg.test(str)) {
			rtn.flag = true;
			rtn.message = "请勿输入：~、$、%、^、￥ 等特殊字符！";
		}
	}
	return rtn;
};