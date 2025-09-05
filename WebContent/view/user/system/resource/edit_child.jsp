<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
	<title>${_pageName}子权限 - ${_moduleName}</title>
	<jsp:include page="/globalFile/global.jsp" />
	<script type="text/javascript" >
	
	</script>
</head>
<body class="edit">
	<div class="edit_title">${_pageName}子权限 - ${_moduleName}</div>
	<form action="save"  method="post" class="layui-form layui-form-pane" >
		<div class="layui-form-item">
			<div class="layui-inline">
			    <label class="layui-form-label">上一级权限</label>
			    <div class="layui-input-inline">
			    	<input type="text" value="${obj.name}" class="layui-input" style="color:red" readonly="readonly"/>
			    	<input type="hidden" name="obj.parentId.id" class="layui-input" value="${obj.id}" dialogSelectId="parentResource" />
			    </div>
		    </div>
		    <div class="layui-inline">
				<label class="layui-form-label">所属系统</label>
				<div class="layui-input-inline">
					<input type="text" value="${obj.serverId.name}" class="layui-input" style="color:red" readonly="readonly"/>
					<input type="hidden" name="obj.serverId.id" class="layui-input" value="${obj.serverId.id}" dialogSelectId="server"/>
				</div>
			</div>
		</div>
		
		<div class="layui-form-item">
			<div class="layui-inline">
				<label class="layui-form-label">名称</label>
				<div class="layui-input-inline">
					<input type="text" name="obj.name"  val-type="empty" class="layui-input" placeholder="权限名称"/>
				</div>
			</div>
			<div class="layui-inline">
				<label class="layui-form-label">代码</label>
				<div class="layui-input-inline">
					<input type="text" name="obj.code" value="${obj.code}_" val-type="empty" class="layui-input" placeholder="不可重复"/>
				</div>
			</div>
			<div class="layui-inline">
				<label class="layui-form-label">顺序</label>
				<div class="layui-input-inline">
					<input type="int" name="obj.serialNumber" class="layui-input" placeholder="数字" />
				</div>
			</div>
		</div>
		
		<div class="layui-form-item">
		    <label class="layui-form-label">访问URL</label>
		    <div class="layui-input-block">
				<input type="text" name="obj.url" value="${obj.url}/" class="layui-input" placeholder="相对路径" />
		    </div>
		</div>
		<div class="layui-form-item">
			<label class="layui-form-label">图标路径</label>
			<div class="layui-input-block">
				<input type="text" name="obj.path" class="layui-input" placeholder="一般是Url。样式名或URL路径" />
			</div>
		</div>
		<div class="layui-form-item layui-form-text">
		    <label class="layui-form-label">描述</label>
		    <div class="layui-input-block">
				<textarea name="obj.describe" class="layui-textarea" ></textarea>
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
			<%-- <input type="hidden" name="obj.id" value="${obj.id}" /> --%>
		</div>
	</form>
</body>
</html>