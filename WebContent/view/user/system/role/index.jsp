<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
	<title>${_moduleName}</title>
	<jsp:include page="/globalFile/global.jsp" />
	<style type="text/css">
		/*权限_面板区*/
		.res_panel{
			border:1px solid #D4D4D4;
		}
		/*权限_标题*/
		.res_title{
			background-color:#FFFFFF;
		}
		.res_table th{
			text-align:center;padding:10px;background-color: #CFCFCF;word-break:break-all;
		}
		.res_table td{
			padding:5px 0px 5px 0px;
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
					,{field:'status', title: '状态',toolbar:'#bar_status'}
					,{field:'describe', title: '描述'}
					,{field:'createTime', title: '创建时间', sort: true}
					,{fixed: 'right', title:'操作', toolbar: '#bar'}
				]]
			}
			renderTable(tableConfig,function(){
				//监听行工具事件
				layui_table.on('tool(list)', function(obj){
					var data = obj.data;
				    var id=data["id"];
				    var name=data["name"];
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
				    	//权限配置
				    	case "resource":{
				    		ajaxNorm(pathName+"/resource",{},function(data){
				    			var pageHeight=window.screen.availHeight-120;		//608
				    			var t_cha=pageHeight/3;
				    			var t_height=pageHeight-t_cha;		//606-200=400;
				    			var t_dialog_id=layer.open({
				    				type:1
				    				,title: "<b><t style='color:red'>"+name+"</t> —— 权限配置</b>"	
									,content: "<div style='padding:10px'>"+data+"</div>"
									,skin: 'layui-layer-rim' //加上边框
									,area: ["1000px",t_height+"px"]
									,shade: 0.6 //遮罩透明度
									,maxmin: true //允许全屏最小化
									,btn: ['保存配置']
									,yes: function(index, layero){
										resourceSave(index);
									}
				    			});
				    			var t_layui_jq=$("#layui-layer"+t_dialog_id);
				    			t_layui_jq.css("top","30px");
				    			
				    			//htmlDialog("【"+userName+"】角色配置",data);
				    			$("#resourceDialog_roleId").val(id);
				    			//
				    			ajax(pathName+"/resourceData",{roleId:id},function(result){
				    				var t_resExist=result["resExist"];
				    				var t_resList=result["resList"];
				    				//
				    				var t_resExistMap={};	//数组转成map
				    				for(var i=0;t_resExist!=null && i<t_resExist.length;i++){
				    					t_resExistMap[t_resExist[i]]=1;
				    				}
				    				//处理数据
				    				t_resList=handleTreeData(t_resList,t_resExistMap);
				    				//渲染
				    				renderTree({
										elem: '#resPanel'
										,id:'tree_resPanel'
										,data: t_resList
										,showCheckbox: true  //是否显示复选框
										,onlyIconControl:true
										,isJump: false //是否允许点击节点时弹出新窗口跳转
										,click: function(obj){
											var data = obj.data;  //获取当前点击的节点数据
											var t_id=data["id"];		//权限ID
											if(t_id!=null && t_id!=""){
												getResource(t_id);
											}
										}
									});
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
		//处理树形数据（数组式）
		function handleTreeData(itemArr,resExistMap){
			for(var key in itemArr){
				var t_resJson=itemArr[key];
				var t_menuId=t_resJson["id"];
				var t_children=t_resJson["children"];
				//匹配，选择
				if(t_menuId!=null && resExistMap[t_menuId]!=null){
					t_resJson["checked"]=true;
				}
				delete t_resJson['href'];
				if(t_children!=null && t_children.length>0){
					t_children=handleTreeData(t_children,resExistMap);
					t_resJson["children"]=t_children;
				}
				itemArr[key]=t_resJson;
			}
			return itemArr;
		}
		//获取 : 权限信息
		function getResource(id){
			var t_resInfoPanel_jq=$("#resInfoPanel");
			ajax(pathName+"/getResource",{id:id},function(result){
				var obj=result["obj"];
				var html='';
				if(obj!=null){
					var code=obj["code"];
					var name=obj["name"];
					var status=obj["status"];
					var url=obj["url"];
					var path=obj["path"];
					var serialNumber=obj["serialNumber"];
					var describe=obj["describe"];
					var isAllowDelete=obj["isAllowDelete"];
					var createTime=obj["createTime"];
					//
					var isAllowDeleteStr="否"
					if(isAllowDelete!=null && isAllowDelete==true){
						isAllowDeleteStr="是";
					}
					var statusStr;
					switch(status){
						case 1:statusStr="正常";break;
						case 0:statusStr="禁用";break;
					}
					//
					var html='<div class="layui-form-item">';
					html+='<label class="layui-form-label">名称</label>';
					html+='<div class="layui-input-inline">';
					html+='<input type="text" class="layui-input" value="'+(name!=null?name:"")+'"/>';
					html+='</div>';
					html+='<label class="layui-form-label">代码</label>';
					html+='<div class="layui-input-inline">';
					html+='<input type="text" class="layui-input" value="'+(code!=null?code:"")+'"/>';
					html+='</div>';
					html+='</div>';
					//第2行
					html+='<div class="layui-form-item">';
					html+='<label class="layui-form-label">状态</label>';
					html+='<div class="layui-input-inline">';
					html+='<input type="text" class="layui-input" value="'+(statusStr!=null?statusStr:"")+'"/>';
					html+='</div>';
					html+='<label class="layui-form-label">创建时间</label>';
					html+='<div class="layui-input-inline">';
					html+='<input type="text" class="layui-input" value="'+(createTime!=null?createTime:"")+'"/>';
					html+='</div>';
					html+='</div>';
					//html+=getResourceItemHtml("状态",statusStr);
					html+=getResourceItemHtml("访问路径",url);
					//html+=getResourceItemHtml("图标路径",path);
					//html+=getResourceItemHtml("顺序",serialNumber);
					//html+=getResourceItemHtml("是否允许删除",isAllowDeleteStr);
					//html+=getResourceItemHtml("创建时间",createTime);
					html+=getResourceItemHtml("描述",describe);
				}
				t_resInfoPanel_jq.html(html);
			},null,null,false,null,null,null,false);
		}
		//获取 : 权限信息单项Html
		function getResourceItemHtml(name,value){
			var html='<div class="layui-form-item">';
			html+='<label class="layui-form-label">'+name+'</label>';
			html+='<div class="layui-input-block">';
			html+='<input type="text" class="layui-input" value="'+(value!=null?value:"")+'"/>';
			html+='</div>';
			html+='</div>';
			return html;
		}
		//获取 : 具体选择的权限ID列表
		function getResourceSelectValue(checkData){
			var resourceIdArr=new Array();
			if(checkData!=null){
				for(var i=0;i<checkData.length;i++){
					var t_json=checkData[i];
					var t_id=t_json["id"];
					var t_children=t_json["children"];
					//子
					if(t_children!=null && t_children.length>0){
						var t_childArr=getResourceSelectValue(t_children);
						if(t_childArr!=null && t_childArr.length>0){
							for(var j=0;j<t_childArr.length;j++){
								resourceIdArr.push(t_childArr[j]);
					        }
						}
					}else{
						resourceIdArr.push(t_id);
					}
				}
			}
			return resourceIdArr;
		}
		//保存权限配置
		function resourceSave(index){
			//获得选中的节点
			var checkData = layui_tree.getChecked('tree_resPanel');
			var resourceIdArr=getResourceSelectValue(checkData);
			//
			var roleId=$("#resourceDialog_roleId").val();
			//var resourceIdArr=getSelect("resourceId");
			var paramJson={};
			paramJson["id"]=roleId;
			paramJson["resourceIdList"]=resourceIdArr;
			
			ajax(pathName+"/resourceSave",paramJson,function(result){
				closeDialog(index);
			});
			
		}
	</script>
</head>
<body>
	<div>
		<!-- 条件区 -->
		<blockquote class="layui-elem-quote quoteBox">
			<form class="layui-form" style="width:100%;">
				<table class="table" style="width:100%;">
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
				{{#if (d.isAllowDelete == true) { }}
					<button type="button" class="layui-btn layui-btn-sm" lay-event="edit"><i class="layui-icon layui-icon-edit"></i>编辑</button>
					<button type="button" class="layui-btn layui-btn-sm layui-btn-danger" lay-event="del">删除</button>
				{{# } }}
				<button type="button" class="layui-btn layui-btn-sm layui-btn-normal" lay-event="resource">权限配置</button>
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
	</div>
</body>
</html>