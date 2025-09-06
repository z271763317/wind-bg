<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
	<title>${_moduleName}</title>
	<jsp:include page="/globalFile/global.jsp" />
	<style type="text/css">
		.layui-form-item{
			margin-bottom: 0px;
		}
		.layui-form-item .layui-inline {
			margin-right:0px
		}
		.layui-form-item .layui-input-inline {
		    max-width: 150px;margin-right:0px;
		 }
		 .layui-form-label{
		 	width:50px;padding:9px 9px 9px 5px
		 }
		 .layui-table-cell{
		 	padding:0 10px;font-size:12px
		 }
		 
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
					,{field:'status', title: '状态',width:50,toolbar:'#bar_status'}
					,{field:'url', title: '访问url'}
					,{field:'describe', title: '描述'}
					,{field:'serialNumber', title: '顺序',width:55}
					,{field:'', title: '主菜单',width:60,toolbar:'#bar_isMenu'}
					//,{field:'isAllowDelete', title: '允许删除',toolbar:'#bar_isAllowDelete'}
					//,{field:'describe', title: '描述'}
					,{field:'createTime', title: '创建时间', sort: true,width:135}
					,{fixed: 'right', title:'操作', width:190,toolbar: '#bar',width:210}
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
				    	//添加子权限
				    	case "addChild":{
				    		var t_url=pathName+"/add?id="+id;
				    		window.open(t_url);
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
		//清除对话框选择
		function clearDialogSelect(obj){
			var t_jq=$(obj);
			var t_dialogName=t_jq.attr("dialogName");
			$("[dialogSelectId=\""+t_dialogName+"\"]").val("");
			$("[dialogSelectName=\""+t_dialogName+"\"]").html("");
		}
		//刷新缓存权限
		function refresh(){
			ajax(pathName+"/refresh");
		}
	</script>
</head>
<body>
	<div>
		<!-- 条件区 -->
		<blockquote class="layui-elem-quote quoteBox">
			<form class="layui-form" style="width:100%">
				<table class="table" style="width:100%">
					<tr>
						<td style="width:80px;text-align:right">名称：</td>
						<td style="width:140px;">
							<div class="layui-input-inline" >
								<input type="text" name="name" class="layui-input" placeholder="模糊匹配" onkeydown="if(event.keyCode==13) {search(this)}"/>
							</div>
						</td>
						<td style="width:40px;text-align:right">状态：</td>
						<td style="width:110px;">
							<div class="layui-input-inline" style="width:100px">
								<select class="select" name="status">
									<option value="">全部</option>
									<option value="1">启用</option>
									<option value="2">未审核</option>
									<option value="0">禁用</option>
								</select>
							</div>
						</td>
						<td style="width:50px;text-align:right">代码：</td>
						<td style="width:135px">
							<div class="layui-input-inline" >
								<input type="text" name="code" class="layui-input" placeholder="模糊匹配" onkeydown="if(event.keyCode==13) {search(this)}"/>
							</div>
						</td>
						<td style="width:140px;text-align:right">
							<div class="layui-btn-group">
								<button type="button" class="layui-btn layui-btn-normal" dialogName="parentResource" dialog="resource" onClick="dialogOpen(this)">选择</button>
								<button type="button" class="layui-btn layui-btn-danger" dialogName="parentResource" onClick="clearDialogSelect(this)">清除</button>
							</div>
						</td>
						<td style="padding-left:5px;">
							<spna >上一级权限：</spna>
							<span>
								<input type="hidden" name="parentId" class="layui-input"  dialogSelectId="parentResource"/>
								<span dialogSelectName="parentResource" style="color:red"></span>
							</span>
						</td>
						<td style="text-align:right">
							<div class="layui-btn-group">
								<button type="button" class="layui-btn layui-btn-normal " data-type="reload" onClick="search(this)"><i class="layui-icon layui-icon-search" style="font-size: 18px;"></i> 搜 索</button>
								<button type="button"  class="layui-btn" onClick="addUI(this)"><i class="layui-icon layui-icon-add-circle" style="font-size: 18px;"></i> 添 加</button>
							</div>
						</td>
					</tr>
					<tr>
						<td style="text-align:right">创建时间：</td>
						<td colspan="3">
							<div class="layui-input-inline">
							  <input type="text" class="layui-input" name="startTime" id="startTime" style="width:135px" placeholder="起始时间" />
							</div>
							<div class="layui-input-inline">
								~
							</div>
							<div class="layui-input-inline">
							  <input type="text" class="layui-input" name="endTime" id="endTime" style="width:135px" placeholder="结束时间" />
							</div>
						</td>
						<td style="text-align:right">主菜单：</td>
						<td>
							<div class="layui-input-inline" style="width:100px">
								<select class="select" name="isMainMenu">
									<option value="">不限制</option>
									<option value="true">是</option>
									<option value="false">否</option>
								</select>
							</div>
						</td>
						<td style="text-align:right">
							<div class="layui-btn-group">
								<button type="button" class="layui-btn layui-btn-normal" dialogName="server" dialog="server" onClick="dialogOpen(this)">选择</button>
								<button type="button" class="layui-btn layui-btn-danger" dialogName="server" onClick="clearDialogSelect(this)">清除</button>
							</div>
						</td>
						<td style="padding-left:5px;">
							<spna>所属系统：</spna>
							<span>
								<input type="hidden" name="serverId" class="layui-input"  dialogSelectId="server"/>
								<span dialogSelectName="server" style="color:red"></span>
							</span>
						</td>
						<td colspan="20" style="text-align:right">
							<div class="layui-btn-group">
								<button type="button"  class="layui-btn" onClick="enableSelect(this)">批量启用</button>
								<button type="button"  class="layui-btn layui-btn-danger" onClick="disableSelect(this)">批量禁用</button>
							</div>
						</td>
					</tr>
				</table>
			</form>
		</blockquote>
		<div style="padding:0px 10px;font-size:12px;color:#7A7A7A;line-height:22px">
			<div><b style="color:red">说明</b>：若选择了【主菜单】，则【上一级权限】的条件失效，只查看【上一级权限】为空的。<button type="button" class="layui-btn layui-btn-sm" onClick="refresh(this)">点我刷新缓存权限</button></div>
		</div>
		<!-- 列表数据 -->
		<div id="list" lay-filter="list"  style="display: none;"></div>
		<!-- 每行按钮 -->
		<div type="text/html" id="bar" style="display: none;">
			<div class="layui-btn-group">
				<button type="button" class="layui-btn layui-btn-sm" lay-event="edit"><i class="layui-icon layui-icon-edit"></i>编辑</button>
				<button type="button" class="layui-btn layui-btn-sm layui-btn-danger" lay-event="del">删除</button>
				{{#if (d.parentId== null) { }}
					<button type="button" class="layui-btn layui-btn-sm layui-btn-normal" lay-event="addChild">添加子权限</button>
				{{# } }}
			</div>
		</div>
		<div type="text/html" id="bar_status" style="display: none;">
			{{#if (d.status == 1) { }}
			 	<span style="color:green">启用</span>
			{{# }else if(d.status == 2){ }}
				<span style="color:#EE9A00">未审核</span>
			{{# }else if(d.status == 0){ }}
				<span style="color:red">禁用</span>
			{{# }else{ }}
				<span>未知</span>
			{{# } }}
		 </div>
		 <div type="text/html" id="bar_isAllowDelete" style="display: none;">
			{{#if (d.isAllowDelete == true) { }}
			 	<span>是</span>
			{{# }else if(d.isAllowDelete == false){ }}
				<span>否</span>
			{{# }else{ }}
				<span>未知</span>
			{{# } }}
		 </div>
		 <div type="text/html" id="bar_isMenu" style="display: none;">
			{{#if (d.parentId== null) { }}
				<i class="layui-icon layui-icon-ok" style="font-size:25px;color: blue;"></i>
			{{# } }}
		 </div>
		 
	</div>
</body>
</html>