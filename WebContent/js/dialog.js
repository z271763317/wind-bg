/**支持的类型**/
var _dialogType={
	"resource":"权限"
	,"server":"系统"
}
var dialogPath="../../";
/*********弹出框操作********/
//打开对话框
function dialogOpen(obj){
	var t_jq=$(obj);
	var t_dialogType=t_jq.attr("dialog");
	var t_dialogName=t_jq.attr("dialogName");
	ajaxNorm(dialogPath+"user/dialog",{type:t_dialogType},function(data){
		var title=_dialogType[t_dialogType];
		/*主区*/
		var html='<form method="post" class="layui-form" innerName="panel" dialogName="'+t_dialogName+'">';
		/*条件区*/
		html+='<blockquote class="layui-elem-quote quoteBox">';
		html+=data;
		//按钮
		html+='<div class="layui-inline" style="float:right">';
		html+='<div class="layui-btn-group">';
		html+='<button type="button" class="layui-btn layui-btn-normal layui-btn-sm " style="width:80px" onClick="dialogSearch(this)"><i class="layui-icon layui-icon-search" style="font-size: 18px;"></i> 搜索</button>';
		html+='<button type="button" class="layui-btn layui-btn-sm " style="width:80px" onClick="dialogSelect(this)"><i class="layui-icon layui-icon-add-circle" style="font-size: 18px;"></i> 选择</button>';
		html+='<button type="button" class="layui-btn layui-btn-danger layui-btn-sm " style="width:80px" onClick="dialogSelectClear(this)"><i class="layui-icon layui-icon-reduce-circle" style="font-size: 18px;"></i>清除</button>';
		html+='</div>';
		html+='</div>';
		//html+='<div style="clear:both" />';
		//
		html+='</blockquote>';
		/*数据区*/
		html+='<div innerName="dataPanel" class="dialog_dataPanel">';
		//内容区
		html+='<div innerName="dataPanelContent"></div>';
		html+='</div>';
		//隐藏区
		html+='<div style="display: none">';
		html+='<input type="hidden" name="type" value="'+t_dialogType+'" />';
		html+='</div>';
		//分页区
		html+='<div id="dialogPage"></div>';
		/*尾部*/
		html+='</form>';
		//
		global_dialog_id=htmlDialog("【"+title+"】选择框",html,850);
		var t_panel_jq=$("[innerName=\"panel\"]");
		dialogInitInputEnter(t_panel_jq[0]);
		dialogInitPage(t_panel_jq[0]);
	});
}
//初始化分页区，并执行
function dialogInitPage(form_obj){
	var param_json=getFormParamJson(form_obj);
	param_json["page"]=1;
    param_json["limit"]=50;
    //
	var pageConfig=dialogPageConfig(form_obj,param_json);
	dialogSearchAjax(form_obj,param_json, pageConfig,function(result,pageConfig){
		var t_count=result["count"];
		pageConfig["count"]=t_count;
		renderLaypage(pageConfig);
	});
}
//获取分页完整的配置
function dialogPageConfig(form_obj,param_json){
	var config={};
	config["elem"]= 'dialogPage',
	config["limit"]= 50,
	config["jump"]=function(obj, first){
		 if(!first){
		    //obj包含了当前分页的所有参数（缺少回调方法），比如：
		    var page=obj.curr; //得到当前页，以便向服务端请求对应页的数据。
		    var limit=obj.limit; //得到每页显示的条数
		    //
		    param_json["page"]=page;
		    param_json["limit"]=limit;
		    dialogNextPage(form_obj,param_json,obj);
		}
	};
	return config;
}
//分页的下一页
function dialogNextPage(form_obj,param_json,obj){
    dialogSearchAjax(form_obj,param_json,obj,function(result2,obj){
    	var t_count2=result2["count"];
    	obj.count=t_count2;
    	renderLaypage(obj);
    });
}
//搜索（表单元素式，条件更换）
function dialogSearch(obj){
	dialogInitPage(obj.form);
}
//搜索（ajax式）
function dialogSearchAjax(form_obj,param_json,pageConfig,fn){
	var type=param_json["type"];
	ajax(dialogPath+"user/dialog/"+type,param_json,function(result){
		var t_dataArr=result["data"];
		var t_count=result["count"];
		//
		var t_form_jq=$(form_obj);
		var t_content_jq=t_form_jq.find("[innerName=\"dataPanel\" ]");
		var html='';
		for(var i=0;i<t_dataArr.length;i++){
			var t_json=t_dataArr[i];
			var t_id=t_json["id"];
			var t_name=t_json["name"];
			var t_code=t_json["code"];
			//
			var t_title=t_name;
			if(t_code!=null){
				t_title=t_code;
			}
			//
			html+='<label class="dialog_dataPanel_item" title="'+t_title+'"><table style="width:100%"><tr>';
			html+='<td class="dialog_dataPanel_item_td" style="width:8px;"><input type="radio" title="'+t_name+'" name="dialog_item" value="'+t_id+'" lay-ignore /></td>';
			html+='<td class="dialog_dataPanel_item_td omit" style="border-left:1px solid #E6E6FA">'+t_name+'</td>';
			html+='</tr></table></label>';
		}
		t_content_jq.html(html);
		layui_form.render('radio');
		if(fn!=null){
			fn(result,pageConfig);
		}
	},null,null,false,null,null,null,false);
}
//弹出框：初始化需要回车事件的表单内容所有内容
function dialogInitInputEnter(formObj){
	var t_form_jq=$(formObj);
	var t_input_jq=t_form_jq.find("input[type=\"text\"]");
	for(var i=0;i<t_input_jq.length;i++){
		var t_obj=t_input_jq[i];
		var t_obj_jq=$(t_obj);
		t_obj_jq.keypress(function(event){
			var keyCode=getKeyCode(event);
			if(keyCode==13){
				dialogSearch(t_obj);
				return false;
			}
		});
	}
}

//选择
function dialogSelect(obj){
	var t_form_jq=$(obj.form);
	var dialogName=t_form_jq.attr("dialogName");
	var arr=getSelectObj("dialog_item");
	if(arr!=null && arr.length>0){
		var idArr=new Array();
		var nameArr=new Array();
		for(var i=0;i<arr.length;i++){
			var t_jq=$(arr[i]);
			var t_id=t_jq.val();
			var t_name=t_jq.attr("title");
			idArr.push(t_id);
			nameArr.push(t_name);
		}
		dialogSelectSet(dialogName,idArr[0],nameArr[0]);
		//
		closeDialog(global_dialog_id);
	}else{
		resultDialog(false,"选择结果","您至少要选择一项");
	}
}
//设置
function dialogSelectSet(dialogName,value,name){
	//单项
	$("[dialogSelectId=\""+dialogName+"\"]").val(value);
	$("[dialogSelectName=\""+dialogName+"\"]").html(name);
}
//清除
function dialogSelectClear(obj){
	var t_form_jq=$(obj.form);
	var dialogName=t_form_jq.attr("dialogName");
	dialogSelectSet(dialogName,"","");
	closeDialog(global_dialog_id);
}