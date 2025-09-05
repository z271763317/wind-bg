/*********************全局变量******************/
var src="";
var wait_id;	//等待框ID
var global_dialog_id;		//当前打开的对话框id（通用：增删改查）
var jsonp_callback_name="callback";		//jsonp跨域的回调方法名
//
//获取当前网址，如： http://localhost:8083/uimcardprj/share/meun.jsp
var curWwwPath = window.document.location.href;
//获取主机地址之后的目录，如： uimcardprj/share/meun.jsp
var pathName = window.document.location.pathname;
var pos = curWwwPath.indexOf(pathName);
//获取主机地址，如： http://localhost:8083
var localhostPaht = curWwwPath.substring(0, pos);
//获取带"/"的项目名，如：/uimcardprj
var projectName = pathName.substring(0, pathName.substr(1).indexOf('/') + 1);
//项目根目录
var rootPath="";

//页面渲染后执行
$(document).ready(function(){
	
});
//"成功"和"错误"提示对话框（isSuccess=是否成功对话框【false=错误框；null=信息框】；title=标题；content=内容；okFn=点“确定”的回调函数）
function resultDialog(isSuccess,title,content,okFn){
	var t_icon=isSuccess==true?6:5;
	layui_layer.open({
		title:"<b>"+title+"</b>",
	    content: content,
	    icon:t_icon,
	    btn: '确定',
	    yes: function(index){
	    	layui_layer.close(index);
	    	if(okFn!=null){okFn();}
	    }
	});
}
//确认框(okFn=确认后回调函数；cencelFn=取消后回调函数）
function confirm(title,content,okFn,cencelFn){
	layui_layer.confirm(content, {
		title:"<b>"+title+"</b>",
		icon:3,
		btn: ['确定','取消'] //按钮
	}, function(index){
		layui_layer.close(index);
		if(okFn!=null){okFn();}
	}, function(){
		if(cencelFn!=null){cencelFn();}
	});
}
//弹出：内容窗口框——title=标题；content=HTML之UI代码
function htmlDialog(title, content, width,height){
	var width_set=(width!=null?width:900)+"px";
	var height_set=(height!=null?height+"px":"");
	var t_dialog_id=layui_layer.open({
		  type: 1 //Page层类型
		  ,skin: 'layui-layer-rim' //加上边框
		  ,area: [width_set,height_set]
		  ,title: "<b>"+title+"</b>"
		  ,shade: 0.6 //遮罩透明度
		  ,maxmin: true //允许全屏最小化
		  ,content: "<div style='padding:10px'>"+content+"</div>"
	});
	var t_layui_jq=$("#layui-layer"+t_dialog_id);
	t_layui_jq.css("top","30px");
	//
	return t_dialog_id;
}
//弹出：内容窗口框——title=标题；返回窗口index索引（类似id）
function openDialog(url,title,success){
	var index = layui_layer.open({
        title : title,
        type : 2,
        content : url,
        maxmin:true,	//允许全屏最小化
        success : function(layerObj, index){
        	if(success!=null){
        		success(layerObj,index);
        	}
        }
    });
    layui_layer.full(index);
	return index;
}
//关闭：asyncbox的HTML内容框，waitTime=等待多少毫秒关闭
function closeDialog(id,waitTime){
	setTimeout(function(){
		layui_layer.close(id);
	}, waitTime!=null?waitTime:10);
}
/*删除所有的标签*/
function delLab(str){
	return str.replace(/<[^>].*?>/g,"");  
}
/*提示框*/
function promptDialog(isSuccess,content){
	var imgName=isSuccess==true?"success_ico.png":"error_ico.png";
	imgName=isSuccess==null?"info_ico.png":imgName;
	content="<table style='margin:0 auto'><tr><td style='padding-right:5px'><img src='"+rootPath+"/image/"+imgName+"' style='margin-bottom:-4px'/></td><td>"+content+"</td></tr></table>";
	layui_layer.msg(content);
}
/*等待框（处理框、加载框）*/
function waitShow(){
	wait_id=layui_layer.load(1, {
		// content: html,
		shade : [ 0.6, '#000' ]
	// 0.1透明度的白色背景
	});
}
/* 关闭等待框（处理框、加载框） */
function waitClose(){
	closeDialog(wait_id);
}
/*通用ajax表单请求（对返回的数据进行验证）——formObj=form表单对象*/
function ajaxForm(formObj,successFn,failureFn,errorFn,isWaitShow,title,content,successInfo,isTipsShow){
	var t_formObj_jq=$(formObj);
	var t_enctype=t_formObj_jq.attr("enctype");
	var isUpload=false;
	//表单上传
	if(t_enctype!=null && t_enctype.toLowerCase() == "multipart/form-data") {
		isUpload=true;
	}else{
		var t_input_img_jq=t_formObj_jq.find("input[type='hidden'][imgType]");		//图片文件
		var t_input_file_jq=t_formObj_jq.find("input[type='file']");		//图片文件
		if(t_input_img_jq.length>0 || t_input_file_jq.length>0){
			isUpload=true;
		}
	}
	if(isUpload==true){
		upload(formObj,successFn,failureFn,errorFn,isWaitShow,title,content,successInfo,isTipsShow);
	}else{
		var url=t_formObj_jq.attr("action");
		var jsonData=t_formObj_jq.serialize();
		ajax(url, jsonData, successFn,failureFn,errorFn,isWaitShow,title,content,successInfo,isTipsShow);
	}
}
/**
 * 通用ajax请求（对返回的数据进行验证）
 * @param url : 请求URL
 * @param jsonData : json数据参数（还可以URL参数拼凑法）
 * @param successFn : 成功回调函数；failureFn=失败回调函数；errorFn=异常回调函数
 * @param isWaitShow : 是否显示等待框；title=等待框标题；content=等待框内容
 **/
function ajax(url,jsonData,successFn,failureFn,errorFn,isWaitShow,title,content,successInfo,isTipsShow){
	if(isWaitShow==null || isWaitShow==true){
		waitShow();	//显示等待框
	}
	$.ajax({url : url,traditional: true,data : jsonData,type : "post",jsonp:jsonp_callback_name,dataType:"jsonp",xhrFields: {withCredentials: true},
		success : function(result) {
			ajaxCallbackSuccessProcess(result,successFn,failureFn,errorFn,isWaitShow,title,successInfo,isTipsShow);
		},
		error : function(data) {
			if(isWaitShow==null || isWaitShow==true){
				waitClose();
			}
			if(errorFn!=null){
				errorFn(data);
			}else{
				promptDialog(false,"操作失败，请稍后再试");
			}
		}
	});
}
/*通用ajax简单请求——jsonData=json数据参数（还可以URL参数拼凑法）；successFn=成功回调函数；errorFn=失败回调函数；isWaitShow=是否显示等待框；title=等待框标题；content=等待框内容；*/
function ajaxSimple(url,jsonData,successFn,errorFn,isWaitShow,title,content){
	if(isWaitShow==null || isWaitShow==true){
		waitShow();	//显示等待框
	}
	$.ajax({url : url,traditional: true,data : jsonData,type : "post",dataType:"jsonp",jsonp:jsonp_callback_name,xhrFields: {withCredentials: true},
		success : function(result) {
			if(isWaitShow==null || isWaitShow==true){
				waitClose();
			}
			var data=JSON.stringify(result);
			successFn(result,data);
		},
		error : function(data) {
			if(isWaitShow==null || isWaitShow==true){
				waitClose();
			}
			if(errorFn!=null){
				errorFn(data);
			}else{
				promptDialog(false,"操作失败，请稍后再试");
			}
		}
	});
}
//ajax请求成功后的后续回调处理
function ajaxCallbackSuccessProcess(result,successFn,failureFn,errorFn,isWaitShow,title,successInfo,isTipsShow){
	if(isWaitShow==null || isWaitShow==true){
		waitClose();
	}
	try{
		title=title || "操作";
		successInfo=successInfo || "操作成功";
		var data=JSON.stringify(result);
		var code=result["code"]; 	//状态
		//成功
		if(code==1){
			if(isTipsShow==null || isTipsShow==true){
				var t_id=promptDialog(true,successInfo);
			}
			if(successFn!=null){
				successFn(result,data);
			}
		}else{
			var message=result["message"];
			if(code!=null){
				switch(code){
					//未登录
					case 6 : {
						resultDialog(false,"未登录",message,function(){
							parent.window.location=result["href"];
						});
						break;
					}
					//用户异常（禁用）
					case 10003 : {
						resultDialog(false,"用户异常",message,function(){
							parent.window.location=result["href"];
						});
						break;
					}
					//未完善信息
					case 10002 : {
						resultDialog(false,"未完善信息",message,function(){
							parent.window.location=result["href"];
						});
						break;
					}
					//没有权限
					case 4 : {
						if(isTipsShow==null || isTipsShow==true){
							promptDialog(false,message);
						}
						break;
					}
				}
			}
			if(failureFn!=null){
				failureFn(result,data);
			}else{
				if(isTipsShow==null || isTipsShow==true){
					promptDialog(false,message);
				}
			}
		}
	}catch(e){
		if(errorFn!=null){
			errorFn(data);
		}else{
			if(isTipsShow==null || isTipsShow==true){
				promptDialog(false,"系统或网络故障，请稍后再试");
			}
		}
	}
}
//表单上传——uploadObj=上传的元素对象（可button等等，可form）*/
function upload(uploadObj,successFn,failureFn,errorFn,isWaitShow,title,content,successInfo,isTipsShow){
	var t_form_obj;		//上传表单对象
	if(uploadObj!=null){
		var elementName=uploadObj.nodeName;		//获取元素属于的标签名
		if(elementName==null || elementName.toLowerCase()!="form"){
			t_form_obj=uploadObj.form;
		}else{
			t_form_obj=uploadObj;
		}
	}
	if(t_form_obj!=null){
		var t_form_jq=$(t_form_obj);
		var t_form_url=t_form_jq.attr("action");
		var t_formData = new FormData(t_form_obj);
		/*裁剪的图片文件数据转blob*/
		var t_input_img_jq=t_form_jq.find("input[type='hidden'][imgType]");		//图片文件
		for(var i=0;i<t_input_img_jq.length;i++){
			var t_t_input_img_jq=$(t_input_img_jq[i]);
			var t_belongName=t_t_input_img_jq.attr("belongName");	//所属的file
			var t_name=t_t_input_img_jq.attr("name");	//name
			var t_base64=t_t_input_img_jq.val();		//图片base64
//			t_formData.delete(t_belongName,null);		//删除属于的file
			if(t_base64!=null && t_base64.length>0){
				var t_base64Arr=t_base64.split(',');
				var t_imgTypData=t_base64Arr[0];
				var t_start=t_imgTypData.indexOf(":");
				var t_end=t_imgTypData.indexOf(";");
				var t_imgContentType="image/jpeg";
				if(t_start!=-1 && t_end!=-1){
					t_imgContentType=t_imgTypData.substring(t_start+1,t_end);
				}
				/*文件类型*/
				var t_fileName_haveArr_index=t_name.indexOf("[");
				var t_fileContentType=null;
				var t_arrNum="";
				if(t_fileName_haveArr_index!=-1){
					t_fileContentType=t_name.substring(0,t_fileName_haveArr_index);
					t_arrNum=t_name.substring(t_fileName_haveArr_index);
				}else{
					t_fileContentType=t_name;
				}
				t_fileContentType+="ContentType"+t_arrNum;
				/*转成二进制blob*/
				var t_data = t_base64Arr[1];
				var t_base64_atob=window.atob(t_data);
				var ia = new Uint8Array(t_base64_atob.length);
				for (var j = 0; j < t_base64_atob.length; j++) {
				    ia[j] = t_base64_atob.charCodeAt(j);
				}
				//canvas.toDataURL 返回的默认格式就是 image/png
				var blob_compress = new Blob([ia], {
				    type: t_imgContentType
				});
//				t_formData.set(t_name,blob_compress);
//				t_formData.set(t_fileContentType,t_imgContentType);
				
				t_formData.append(t_name,blob_compress);
				var t_t_fileContentType_jq=t_form_jq.find("input[name='"+t_fileContentType+"']");
				//不存在则加入
				if(t_t_fileContentType_jq.length<=0){
					t_formData.append(t_fileContentType,t_imgContentType);
				}
			}else{
//				t_formData.delete(t_name);		//不存在则删除
			}
		}
		ajaxUpload(t_form_url,t_formData,function(result,data){
			try{if(successFn!=null){successFn(result,data);}}catch(e){}
			/*清除数据*/
			t_input_img_jq.remove();
			t_form_jq.find("input[type='hidden'][imgContentType]").remove();
		},failureFn,errorFn,isWaitShow,title,content,successInfo,isTipsShow);
	}else{
		promptDialog(false,"操作的表单不存在");
	}
}
//ajax上传（successInfo=成功后的信息）
function ajaxUpload(url,formData,successFn,failureFn,errorFn,isWaitShow,title,content,successInfo,isTipsShow){
	if(isWaitShow==null || isWaitShow==true){
		waitShow();
	}
	//ajax发送
	$.ajax({url: url,type: "POST",data: formData,dataType:"jsonp",jsonp:jsonp_callback_name,xhrFields: {withCredentials: true},
        processData: false,  // 不处理发送的数据
        contentType: false,   // 不设置Content-Type请求头
        success: function(result){
        	ajaxCallbackSuccessProcess(result,successFn,failureFn,errorFn,isWaitShow,title,successInfo,isTipsShow);
        },error:function(data){
        	if(isWaitShow==null || isWaitShow==true){
				waitClose();
			}
	       	var erroText=data.responseText;
	    	var status=data.status;
	       	if(status==413){
	       		promptDialog(false,"您上传的文件太大，超出了服务器的限制");
	    	}else if(erroText.length>0){
	    		promptDialog(false,"上传文件错误，状态："+status+"，错误原因："+erroText);
	    	}else{
	    		promptDialog(false,"上传的文件不符合规则，可能您上传的文件超出了限制");
	    	}
	    	if(errorFn!=null){
	    		errorFn(data);
	    	}
        }
   });
}
//正常的ajax请求（不做任何处理）
function ajaxNorm(url,jsonData,successFn,errorFn,isWaitShow){
	if(isWaitShow==null || isWaitShow==true){
		waitShow();
	}
	$.ajax({type: "post",url:url,data:jsonData,traditional: true,xhrFields: {withCredentials: true},success: function(data){
	    	if(isWaitShow==null || isWaitShow==true){
				waitClose();
			}
	    	if(successFn!=null){successFn(data);}
		},error:function(response){
			if(isWaitShow==null || isWaitShow==true){
				waitClose();
			}
			if(errorFn!=null){errorFn(response);}
		}
	});
}
//获取json里深层次的json的指定深层key的数据，没有将返回空，可变长数组；参数顺序—>0=jsonData（json顶层对象）,key1,key2,key3......（json的下一级key，直到最底层key为返回的数据）
function getJsonData(){
	if(arguments!=null && arguments.length>0){
		var jsonData=arguments[0];
		if(jsonData!=null){
			for(var i=1;i<arguments.length;i++){
				var t_key=arguments[i];	//key
				jsonData=jsonData[t_key];
				if(jsonData==null){
					return "";
				}
			}
			return jsonData
		}
	}
	return "";
}
//是否其他设备（移动端）
function isMobileDevice() {
	return navigator.userAgent.match(/(iPhone|iPod|Android|ios)/i);
}
//打开QQ对话（对应不同的设备）
function openQQ(qq){
	if(isMobileDevice()){
		window.location.href="mqqwpa://im/chat?chat_type=wpa&uin="+qq+"&version=1&src_type=web&web_src=oicqzone.com";
	}else{
		window.location.href="tencent://message/?exe=qq&menu=yes&Uin="+qq;
//		window.open("http://wpa.qq.com/msgrd?v=3&uin="+qq+"&site=qq&menu=yes");
	}
}
//删除左右2端的空格
function trim(str){if(str==null){return "";}else{return str.replace(/(^\s*)|(\s*$)/g, "");}}
//模拟调用——元素点击
function callClick(id){$("#"+id).click();}

/*****************通用：添删改查************/
//添加UI
function addUI(obj){
	var t_obj_jq=$(obj);
	var t_url=t_obj_jq.attr("url");
	t_url=pathName+"/"+(t_url!=null && t_url!=""?t_url:"add");
	//openDialog(t_url,"添加");
	window.open(t_url);
}
//更新UI
function updateUI(url,id){
	url=(pathName+"/"+(url!=null && url!=""?url:"update"))+"?id="+id;
//	openDialog(url,"更新");
	window.open(url);
}
//保存
function save(obj,fn){
	var t_obj_jq=$(obj);
	var t_form_obj=obj.form;
	if(validationForm(t_form_obj)){
		ajaxForm(t_form_obj,function(result){
			var isCloseDialog=t_obj_jq.attr("isCloseDialog");
			if(isCloseDialog==null || isCloseDialog==true){
//				var index = parent.layui_layer.getFrameIndex(window.name); //先得到当前iframe层的索引
//				parent.layui_layer.close(index); //再执行关闭
				window.setTimeout(function(){
					window.close();
				}, 2000);
			}
			if(fn!=null){
				fn(result);
			}
		});
	}
}
//删除
function del(url,id,tableId,fn){
	url=pathName+"/"+(url!=null && url!=""?url:"delete");
	globalOneService(url,id,"是否删除","您确定要删除该项吗？",tableId,fn);
}
//批量删除
function delSelect(obj,title,content,fn){
	var t_obj_jq=$(obj);
	var t_url=t_obj_jq.attr("url");
	var t_paramKey=t_obj_jq.attr("paramKey");
	t_url=pathName+"/"+(t_url!=null && t_url!=""?t_url:"deleteSelect");
	var t_tableId=getTableId(obj);
	globalSelectService(t_url,t_paramKey,title!=null?title:"是否删除",content!=null?content:"您确定要删除所选择的项吗？",t_tableId,fn);
}
//启用
function enablel(url,id,tableId,fn){
	url=pathName+"/"+(url!=null && url!=""?url:"enable");
	globalOneService(url,id,"是否启用","您确定要启用该项吗？",tableId,fn);
}
//批量启用
function enableSelect(obj,fn){
	var t_obj_jq=$(obj);
	var t_url=t_obj_jq.attr("url");
	var t_paramKey=t_obj_jq.attr("paramKey");
	t_url=pathName+"/"+(t_url!=null && t_url!=""?t_url:"enableSelect");
	var t_tableId=getTableId(obj);
	globalSelectService(t_url,t_paramKey,"批量启用","您确定要启用所选择的项吗？",t_tableId,fn);
}
//禁用
function disable(url,id,tableId,fn){
	url=pathName+"/"+(url!=null && url!=""?url:"disable");
	globalOneService(url,id,"是否禁用","您确定要禁用该项吗？",tableId,fn);
}
//批量禁用
function disableSelect(obj,tableId,fn){
	var t_obj_jq=$(obj);
	var t_url=t_obj_jq.attr("url");
	var t_paramKey=t_obj_jq.attr("paramKey");
	t_url=pathName+"/"+(t_url!=null && t_url!=""?t_url:"disableSelect");
	var t_tableId=getTableId(obj);
	globalSelectService(t_url,t_paramKey,"批量禁用","您确定要禁用所选择的项吗？",t_tableId,fn);
}
//通用单项操作（tableId=表ID）
function globalOneService(url,id,title,content,tableId,fn){
	confirm(title,content,function(){
		 ajax(url,{id:id},function(result){
			 tableId=tableId!=null && tableId!=""?tableId:getTableId(null);
			 layui_table.reload(tableId);		//重载（刷新）
			 if(fn!=null){
				 fn(result);
			 }
		 });
	});
}
//通用批量操作（tableId=表ID）
function globalSelectService(url,paramKey,title,content,tableId,fn){
	var checkStatus = layui_table.checkStatus(tableId);
	var t_dataArr=checkStatus.data;	//获取选中行的数据
	//
	var t_idArr=getTableIIdList(tableId);
	if(t_idArr!=null && t_idArr.length>0){
		paramKey=paramKey!=null && paramKey!=""?paramKey:"idList";
		var paramJson={};
		paramJson[paramKey]=t_idArr;
		//
		confirm(title,content,function(){
			 ajax(url,paramJson,function(result){
				 layui_table.reload(tableId);		//重载（刷新）
				 if(fn!=null){
					 fn(result);
				 }
			 });
		});
	}else{
		resultDialog(false,"提示","您至少要选择一项");
	}
}
//获取表格所勾选项的id列表
function getTableIIdList(tableId,idKey){
	idKey=idKey!=null?idKey:"id";
	var checkStatus = layui_table.checkStatus(tableId);
	var t_dataArr=checkStatus.data;	//获取选中行的数据
	//
	var t_idArr=new Array();
	for(var i=0;i<t_dataArr.length;i++){
		var t_data=t_dataArr[i];
		var t_id=t_data[idKey];
		//
		t_idArr.push(t_id);
	}
	return t_idArr;
}
//全选/取消（默认）
function selectAll(obj){
	selectAll(obj,null);
}
//全选/取消
function selectAll(obj,elementName){
	var flag=obj.checked;
	var arr=document.getElementsByName(elementName!=null?elementName:"itemId");
	for(var i=0;i<arr.length;i++){
		var obj=arr[i];
		if(flag==true){
			obj.checked=true;
		}else{
			obj.checked=false;
		}
	}
}
//获取 : 选中项的对象
function getSelectObj(elementName){
	var arr=document.getElementsByName(elementName!=null?elementName:"itemId");
	var selectObjArr=new Array();
	for(var i=0;i<arr.length;i++){
		var obj=arr[i];
		var flag=obj.checked;
		if(flag==true){
			selectObjArr.push(obj);
		}
	}
	return selectObjArr;
}
//获取 : 选中项的值，并返回数组
function getSelect(elementName){
	var arr=getSelectObj(elementName);
	var selectValueArr=new Array();
	for(var i=0;i<arr.length;i++){
		var obj=arr[i];
		selectValueArr.push($(obj).val());
	}
	return selectValueArr;
}
//重置/初始化当前对象所属的表单（fn=重置成功后执行的回调方法）
function formReset(obj,fn){
	var t_form_obj=obj.form;
	if(t_form_obj!=null){
		confirm("重置/初始化","您确定要【重置/初始化】当前表单数据吗？",function(){
			t_form_obj.reset();
			if(fn!=null){
				fn(obj);
			}
		});
	}else{
		resultDialog(false,"重置/初始化","当前控件没有所属的表单");
	}
}
//搜索
function search(obj){
	var obj_jq=$(obj);
	var tableId=getTableId(obj);
	var formId=obj_jq.attr("formId");
	var form_obj;
	if(formId==null || formId==""){
		form_obj=obj.form;
	}else{
		form_obj=$("#"+formId)[0];
	}
	var t_param_json=getFormParamJson(form_obj);
	layui_table.reload(tableId,{
		//设定异步数据接口的额外参数，任意设
		where : t_param_json,
		page : {
			//重新从第 1 页开始
			curr : 1
		}
	});
}
//获取 : 表ID（默认：layui_list。先取当前元素的，若没有则取所属form表单的）
function getTableId(obj){
	var tableId;
	if(obj!=null){
		var t_obj_jq=$(obj);
		tableId=t_obj_jq.attr("tableId");
		if(tableId==null || tableId==""){
			var t_form_obj=obj.form;
			var t_form_jq=$(t_form_obj);
			 tableId=t_form_jq.attr("tableId");
		}
	}
	tableId=tableId==null || tableId==""?"layui_list":tableId;
	return tableId;
}
//获取表单参数（json格式）
function getFormParamJson(formObj){
	var t_form_jq=$(formObj);
	var t_param_json = {};
    var t_param_arr= t_form_jq.serializeArray();
    $.each(t_param_arr, function() {
    	t_param_json[this.name] = this.value;
    });
    return t_param_json;
}
//获取当前URL中指定的参数
function getUrlParam(variable){
	var query = window.location.search.substring(1);
	var vars = query.split("&");
	for (var i = 0; i < vars.length; i++) {
		var pair = vars[i].split("=");
		if (pair[0] == variable) {
			return pair[1];
		}
	}
	return null;
}
//获取JSON对象长度
function getJsonLength(jsonData) {
	var length=0;
	if(jsonData!=null){
		for(var ever in jsonData) {
		    length++;
		}
	}
	return length;
}