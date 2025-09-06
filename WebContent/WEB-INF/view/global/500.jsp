<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="indexUrl" value="" />
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv="Content-Type" content="text/html" charset="utf-8" />
	<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0" />
	<meta name="apple-mobile-web-app-capable" content="yes" />
	<meta name="format-detection" content="telephone=no" />
	<title>系统异常</title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<style>
		body {
			font-size: 12px;
			font-family:Arial,宋体;
			-webkit-text-size-adjust:none;
			color:#4F6F7D;
			background:#F6F6F6;
		}
		.title {
			font-size:23px;
			color: #87A0A7;
			font-weight:bold;
		}
		a {
			cursor: pointer;
			outline:none; 
			color:#7698A7;
			blr:expression(this.onFocus=this.blur());
			text-decoration: none;
		}
		.main_content {
			background:#fff;
			padding:20px;padding-top:40px;padding-bottom:40px;
			line-height:22px;
			font-family:Arial,宋体;
			border-bottom:9px solid #E7EFF1;
			box-shadow: 1px 0px 5px rgba(100, 100, 100, 0.3);
			width:90%;
		}
		p {
			font-size:10px;
			_font-size:9px;
			color:#919191;
			margin-top:15px;
		}
	</style>
</head>
<body>
	<div style="padding:10px;">
		<div class="main_content">
			<div class="title">您所访问的连接异常</div>
			<div style="padding:20px;">
				<div style="color:red;padding-top:15px;padding-bottom:15px;font-size:16px;font-weight:bold">
					${requestScope['javax.servlet.error.message']}
				</div>
				<div>查看更多请返回网站主页。</div>
			</div>
		</div>
		<p>基于<a href="http://www.tcin.cn">tcin</a> [<a href="http://www.tcin.cn">wind</a>] 架构为您提供快速安全高效的用户体验。
		<br />Powered by <a href="http://www.tcin.cn">tcin</a> </p>
	</div>
</body>
</HTML>