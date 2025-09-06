<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
	<title>${_moduleName}</title>
	<jsp:include page="/globalFile/global.jsp" />
	<style type="text/css">
		
	</style>
	<script type="text/javascript" >
		//页面渲染后执行
		$(document).ready(function(){
			//表格配置
			var tableConfig={
				cols: [[
					{type:'checkbox', fixed: 'left'}
					,{field:'code', title: '代码'}
					,{field:'name', title: '名称'}
					,{field:'value', title: '配置值（前缀+值+后缀）',templet: '#temp_value'}
					,{field:'createTime', title: '创建时间', sort: true,width:145}
					,{fixed: 'right', title:'操作', toolbar: '#bar',width:160}
				]]
			}
			renderTable(tableConfig,function(){
				//监听行工具事件
				layui_table.on('tool(list)', function(obj){
					var data = obj.data;
				    var id=data["id"];
				    var event=obj.event;
				    switch(event){
						//编辑
				    	case "edit":{
				    		updateUI(null,id);
				    		break;
						}
				    	//删除
				    	case "del":{
				    		del(null,id,null,null);
				    		break;
						}
					}
				});
			});
			//起始时间
			renderLaydate({elem: '#startTime',type: 'datetime'});
			//结束时间
			renderLaydate({elem: '#endTime',type: 'datetime'});
		});
	</script>
</head>
<body>
	<div>
		<!-- 条件区 -->
		<blockquote class="layui-elem-quote quoteBox">
			<form class="layui-form" style="width:100%">
				<table class="table" style="width:100%">
					<tr>
						<td style="width:50px;text-align:right">名称：</td>
						<td style="width:140px;">
							<div class="layui-input-inline" >
								<input type="text" name="name" class="layui-input" placeholder="模糊匹配" onkeydown="if(event.keyCode==13) {search(this)}"/>
							</div>
						</td>
						<!-- <td style="width:40px;text-align:right">状态：</td>
						<td style="width:110px;">
							<div class="layui-input-inline" style="width:100px">
								<select class="select" name="status">
									<option value="">全部</option>
									<option value="1">启用</option>
									<option value="0">下线</option>
								</select>
							</div>
						</td> -->
						<td style="width:50px;text-align:right">代码：</td>
						<td style="width:135px">
							<div class="layui-input-inline" >
								<input type="text" name="code" class="layui-input" placeholder="模糊匹配" onkeydown="if(event.keyCode==13) {search(this)}"/>
							</div>
						</td>
						<td style="width:80px;text-align:right">创建时间：</td>
						<td colspan="5">
							<div class="layui-input-inline">
							  <input type="text" class="layui-input" name="startTime" id="startTime" placeholder="起始时间" />
							</div>
							<div class="layui-input-inline">
								~
							</div>
							<div class="layui-input-inline">
							  <input type="text" class="layui-input" name="endTime" id="endTime" placeholder="结束时间" />
							</div>
						</td>
						<td style="text-align:right">
							<div class="layui-btn-group">
								<button type="button" class="layui-btn layui-btn-normal " data-type="reload" onClick="search(this)"><i class="layui-icon layui-icon-search" style="font-size: 18px;"></i> 搜 索</button>
								<button type="button"  class="layui-btn" onClick="addUI(this)"><i class="layui-icon layui-icon-add-circle" style="font-size: 18px;"></i> 添 加</button>
							</div>
						</td>
					</tr>
				</table>
			</form>
		</blockquote>
		<!-- 列表数据 -->
		<div id="list" lay-filter="list"  style="display: none;"></div>
		<!-- 每行按钮 -->
		<div type="text/html" id="bar" style="display: none;">
			<div class="layui-btn-group">
				<button type="button" class="layui-btn layui-btn-sm" lay-event="edit"><i class="layui-icon layui-icon-edit"></i>编辑</button>
				<button type="button" class="layui-btn layui-btn-sm layui-btn-danger" lay-event="del"><i class="layui-icon layui-icon-subtraction"></i>删除</button>
			</div>
		</div>
		<div type="text/html" id="temp_value" style="display: none;">
			 <span>{{d.prefix}}{{d.value}}{{d.suffix}}</span>
		 </div>
	</div>
</body>
</html>