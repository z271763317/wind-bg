<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
	<title>${_pageName} - ${_moduleName}</title>
	<jsp:include page="/globalFile/global.jsp" />
	<script type="text/javascript" >
	
	</script>
</head>
<body class="edit">
	<div class="edit_title">${_pageName} - ${_moduleName}</div>
	<form action="save"  method="post" class="layui-form layui-form-pane" >
		<div class="layui-form-item">
			<div class="layui-inline">
				<label class="layui-form-label">名称</label>
				<div class="layui-input-inline">
					<input type="text" name="obj.name"  value="${obj.name}" val-type="empty" class="layui-input" placeholder="系统名称"/>
				</div>
			</div>
			<div class="layui-inline">
				<label class="layui-form-label">代码</label>
				<div class="layui-input-inline">
					<input type="text" name="obj.code" value="${obj.code}" val-type="empty" class="layui-input" placeholder="不可重复"/>
				</div>
			</div>
		</div>
		
		<div class="layui-form-item">
			<div class="layui-inline">
			    <label class="layui-form-label">协议</label>
			    <div class="layui-input-inline">
					<select class="select" name="obj.protocol">
						<option value="http://"  <c:if test="${obj.protocol=='http://'}"> selected="selected"</c:if>>http://</option>
						<option value="https://"  <c:if test="${obj.protocol=='https://'}"> selected="selected"</c:if>>https://</option>
					</select>
			    </div>
		    </div>
		    <div class="layui-inline">
				<label class="layui-form-label">域名</label>
				<div class="layui-input-inline">
					<input type="text" name="obj.domain" value="${obj.domain}" class="layui-input" placeholder="外网访问链接"/>
				</div>
			</div>
			<div class="layui-inline">
				<label class="layui-form-label">系统名</label>
				<div class="layui-input-inline">
					<input type="text" name="obj.server" value="${obj.server}" class="layui-input" placeholder="系统名"/>
				</div>
			</div>
		</div>
		<div class="layui-form-item">
		    <div class="layui-inline">
				<label class="layui-form-label">内网IP</label>
				<div class="layui-input-inline">
					<input type="text" name="obj.intranetIp" value="${obj.intranetIp}" val-type="empty" class="layui-input" />
				</div>
			</div>
			<div class="layui-inline">
				<label class="layui-form-label">端口</label>
				<div class="layui-input-inline">
					<input type="text" name="obj.port" value="${obj.port}" val-type="empty" class="layui-input" />
				</div>
			</div>
		</div>
		<div style="text-align:center;">
			<div class="layui-btn-group">
				<button type="button" class="layui-btn" onClick="save(this)">立 即 保 存</button>
				<button type="button" class="layui-btn layui-btn-normal" onClick="formReset(this)">重 置</button>
			</div>
		</div>
		<!-- 隐藏元素 -->
		<div style="display: none">
			<input type="hidden" name="obj.id" value="${obj.id}" />
		</div>
	</form>
</body>
</html>