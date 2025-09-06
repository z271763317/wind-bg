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
					,{field:'id', title: 'UID'}
					,{field:'userName', title: '用户名'}
					,{field:'name', title: '名称'}
					,{field:'status', title: '状态',toolbar:'#bar_status'}
					,{field:'type', title: '类型',toolbar:'#bar_type'}
					,{field:'isAllowDelete', title: '允许删除',toolbar:'#bar_isAllowDelete'}
					,{field:'createTime', title: '创建时间', sort: true}
					,{fixed: 'right', title:'操作', toolbar: '#bar',width:170}
				]]
			}
			renderTable(tableConfig,function(){
				//监听行工具事件
				layui_table.on('tool(list)', function(obj){
					var data = obj.data;
				    var id=data["id"];
				    var userName=data["userName"];
				    var event=obj.event;
				    switch(event){
						//编辑
				    	case "edit":{
				    		updateUI(null,id);
				    		break;
						}
				    	//角色
				    	case "role":{
				    		ajaxNorm(pathName+"/role",{},function(data){
				    			var t_dialog_id=layer.open({
				    				type:1
				    				,title: "<b>【"+userName+"】角色配置</b>"	
									,content: "<div style='padding:10px'>"+data+"</div>"
									,skin: 'layui-layer-rim' //加上边框
									,area: ["1000px",""]
									,shade: 0.6 //遮罩透明度
									,maxmin: true //允许全屏最小化
									,btn: ['保存配置']
									,yes: function(index, layero){
										roleSave(index);
									}
				    			});
				    			var t_layui_jq=$("#layui-layer"+t_dialog_id);
				    			t_layui_jq.css("top","30px");
				    			
				    			//htmlDialog("【"+userName+"】角色配置",data);
				    			$("#roleDialog_userId").val(id);
				    			//
				    			ajax(pathName+"/roleData",{userId:id},function(result){
				    				var t_dataArr=result["data"];
				    				var t_roleIdList=result["roleIdList"];
				    				var t_roleJson={};		//已有的角色
				    				for(var i=0;t_roleIdList!=null && i<t_roleIdList.length;i++){
				    					t_roleJson[t_roleIdList[i]]=1;
				    				}
				    				var html='';
				    				for(var i=0;i<t_dataArr.length;i++){
				    					var t_json=t_dataArr[i];
				    					var t_id=t_json["id"];
				    					var t_code=t_json["code"];
				    					var t_name=t_json["name"];
				    					var checked="";
				    					if(t_roleJson.hasOwnProperty(t_id)){
				    						checked=" checked=\"checked\"";
				    					}
				    					//
				    					html+='<div style="float:left;margin-left:10px;margin-bottom:10px" title="'+t_name+'">';
				    					html+='<input type="checkbox" name="roleId" title="'+t_name+'" value="'+t_id+'"'+checked+'/>';
				    					html+='</div>'
				    					//
				    					/*
				    					html+='<label class="dialog_dataPanel_item" title="'+t_name+'"><table style="width:100%"><tr>';
				    					html+='<td class="dialog_dataPanel_item_td" style="width:8px;"><input type="checkbox" title="'+t_name+'" name="dialog_item" value="'+t_id+'"  /></td>';
				    					html+='<td class="dialog_dataPanel_item_td omit" style="border-left:1px solid #E6E6FA">'+t_name+'</td>';
				    					html+='</tr></table></label>';
				    					*/
				    				}
				    				
				    				$("#roleList").html(html);
				    				layui_form.render('checkbox');
								},null,null,false,null,null,null,false);
				    		});
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
		//保存角色配置
		function roleSave(index){
			var userId=$("#roleDialog_userId").val();
			var roleIdArr=getSelect("roleId");
			var paramJson={};
			paramJson["userId"]=userId;
			paramJson["roleIdList"]=roleIdArr;
			ajax(pathName+"/roleSave",paramJson,function(result){
				closeDialog(index);
			});
		}
	</script>
</head>
<body>
	<div>
		<!-- 条件区 -->
		<blockquote class="layui-elem-quote quoteBox" style="padding:5px">
			<form class="layui-form" style="width:100%">
				<table class="table" style="width:100%">
					<tr>
						<td style="width:80px;text-align:right">用户名：</td>
						<td style="width:140px;">
							<div class="layui-input-inline" >
								<input type="text" name="userName" class="layui-input" placeholder="模糊匹配" onkeydown="if(event.keyCode==13) {search(this)}"/>
							</div>
						</td>
						<td style="width:40px;text-align:right">名称：</td>
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
									<option value="0">禁用</option>
								</select>
							</div>
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
		<!-- 列表数据 -->
		<div id="list" lay-filter="list"  style="display: none;"></div>
		<!-- 每行按钮 -->
		<div type="text/html" id="bar" style="display: none;">
			<div class="layui-btn-group">
				<button type="button" class="layui-btn layui-btn-sm" lay-event="edit"><i class="layui-icon layui-icon-edit"></i>编辑</button>
				<button type="button" class="layui-btn layui-btn-sm layui-btn-normal" lay-event="role"><i class="layui-icon layui-icon-group"></i>角色</button>
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
		 <div type="text/html" id="bar_type" style="display: none;">
			{{#if (d.type == 0) { }}
			 	<span>超级管理员</span>
			{{# }else if(d.type == 1){ }}
				<span>管理员</span>
			{{# }else if(d.type == 2){ }}
				<span>普通</span>
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
	</div>
</body>
</html>