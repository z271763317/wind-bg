<%@ page language="java" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	path=path.equals("/")?path:path+"/";
	request.setAttribute("path", path);
%>
<meta http-equiv = "X-UA-Compatible" content = "IE=edge,chrome=1" />
<meta name="renderer" content="webkit|ie-comp|ie-stand" />
<meta http-equiv="Content-Type" content="text/html" charset="utf-8" />
<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0" />
<meta name="apple-mobile-web-app-capable" content="yes" />
<meta name="format-detection" content="telephone=no" />
<meta http-equiv="Access-Control-Allow-Origin" content="*" />
<meta name="apple-mobile-web-app-status-bar-style" content="black" />
<!-- css -->
<link rel="stylesheet" href="${path}js/plugin/layui/css/layui.css" media="all" />
<link rel="stylesheet" href="${path}css/global.css" media="all" />
<!-- 框架工具 -->
<script type="text/javascript" src="${path}js/plugin/jquery/jquery.min.js"></script>
<script type="text/javascript" src="${path}js/plugin/layui/layui.js"></script>
<!-- 自有 -->
<script type="text/javascript" src="${path}js/initModule.js"></script>
<script type="text/javascript" src="${path}js/global.js"></script>
<script type="text/javascript" src="${path}js/plugin/oneself/formUtil.js"></script>
<script type="text/javascript" src="${path}js/plugin/oneself/validateUtil.js"></script>
<script type="text/javascript" src="${path}js/dialog.js"></script>