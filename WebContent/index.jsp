<%@page import="org.wind.bg.util.system.SystemUtil" %>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	SystemUtil.jumpLogin(request, response);		//跳转 : 登录界面（未登录则跳转）
%>
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<title>登录跳转</title>
</head>
</html>