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
				<label class="layui-form-label">用户名</label>
				<div class="layui-input-inline">
					<input type="text" name="obj.userName" value="${obj.userName}" <c:if test="${isUpdate}"> disabled="disabled"</c:if> val-type="empty" class="layui-input" placeholder="不可重复"/>
				</div>
			</div>
			<div class="layui-inline">
				<label class="layui-form-label">类型</label>
				<div class="layui-input-inline" style="width:100px">
					<select class="select" name="obj.type">
						<c:forEach items="${userTypeMap}" var="t_map">
							<option value="${t_map.key}" <c:if test="${t_map.key==obj.type || (obj==null && t_map.key==2)}"> selected="selected"</c:if>>${t_map.value}</option>
						</c:forEach>
					</select>
				</div>
			</div>
		</div>
		<div class="layui-form-item">
			<div class="layui-inline">
				<label class="layui-form-label">密码</label>
				<div class="layui-input-inline">
					<input type="text" name="obj.passWord" class="layui-input" placeholder="长度最少8位"/>
				</div>
			</div>
			<div class="layui-inline">
				<label class="layui-form-label">名称</label>
				<div class="layui-input-inline">
					<input type="text" name="obj.name"  value="${obj.name}" val-type="empty" class="layui-input" placeholder="用户名称"/>
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