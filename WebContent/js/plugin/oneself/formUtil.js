/**
 * 1、表单元素的其他处理
 * 2、input、select、div等的type属性控制，目前类型有如下：
 * 
 * 【input】
 * @param number：数字类型，允许小数
 * @param int、integer：整数型
 * 
 * 【select】
 * @param region : 地区初始化（第一级）
 * 
 */
var region_code="region";	//select地区编码
var region_group_index=1;	//select地区组索引
//允许的ascii码——number
var _ASCII_allow_number={8:1,9:1,37:1,38:1,39:1,40:1,46:1};
//允许的ascii码——int
var _ASCII_allow_int={8:1,9:1,37:1,38:1,39:1,40:1};
var _serviceUrl=rootPath;
//页面渲染后执行
$(document).ready(function(){
	window.setInterval("elementService()", 500);	//每0.5秒钟后执行
}); 
/*当前所有表单元素的初始化处理*/
function elementService(isSelect,isInput,isRegion){
	if(isSelect==null || isSelect==true){
		select();
	}
	if(isInput==null || isInput==true){
		input();
	}
	if(isRegion==null || isRegion==true){
		region();		//地区
	}
}
/**************************表单元素类型的初始化处理***************************/
/****input****/
function input(){
	var t_objArr=$("input[type]");
	for(var i=0;i<t_objArr.length;i++){
		input_service(t_objArr[i])
	}
}
//input——处理服务
function input_service(obj){
	var t_obj=$(obj);
	var typeValue=t_obj.attr("type");
	var t_isInit=t_obj.attr("isInit");
	if(typeValue!="file"){
		if(t_isInit!="true"){
			switch(typeValue){
				case "number":{
					t_obj.attr("isInit","true");
					var t_isInt=t_obj.attr("isInt");		//是否只输入整型
					t_obj.css("ime-mode","disabled");
					t_obj.keypress(function(event){
						var keyCode=getKeyCode(event);
						//alert(keyCode);
						//8=退格键；9=跳格键；46=点号
						if(t_isInt=="true"){
							return (keyCode >= 48 && keyCode <= 57) || _ASCII_allow_int[keyCode]!=null;
						}else{
							var t_value=$(this).val();
							if(keyCode==46 && (t_value.length<=0 || t_value.indexOf(".")!=-1)){
								return false;
							}else{
								return (keyCode >= 48 && keyCode <= 57) || _ASCII_allow_number[keyCode]!=null;
							}
						}
					});
					t_obj.blur(function(){
						var value=$(this).val();
						if(isNaN(value) || value.length<=0){
							var t_defaultValue=t_obj.attr("defaultValue");	//默认值
							$(this).val(t_defaultValue!=null?t_defaultValue:"");
						}
					});
					break;
				}
				case "int":case "integer":{
					t_obj.attr("isInit","true");
					t_obj.css("ime-mode","disabled");
					t_obj.keypress(function(event){
						var keyCode=getKeyCode(event);
						return (keyCode >= 48 && keyCode <= 57) || _ASCII_allow_int[keyCode]!=null;
					});
					t_obj.blur(function(){
						var value=$(this).val();
						if(isNaN(value) || value.indexOf(".")!=-1){
							var t_defaultValue=t_obj.attr("defaultValue");	//默认值
							$(this).val(t_defaultValue!=null?t_defaultValue:"");
						}
					});
					break;
				}
			}
		}
	}
}
/****select****/
function select(){
	var t_objArr=$("select[type]");
	for(var i=0;i<t_objArr.length;i++){
		select_service(t_objArr[i]);
	}
}
//select——处理服务
function select_service(obj){
	var t_obj=$(obj);
	var typeValue=t_obj.attr("type");
	var t_isInit=t_obj.attr("isInit");
	if(typeValue!=null && typeValue.length>0){
		if(t_isInit!="true"){
			t_obj.attr("isInit","true");
			switch(typeValue){
				case "region":{
					//获取全国省级（json）,所有上层次数据
					select_region_init(obj);
					break;
				}
			}
		}
	}
}
/****region****/
function region(){
	var t_objArr=$("[type='region']");
	for(var i=0;i<t_objArr.length;i++){
		region_service(t_objArr[i]);
	}
}
//region——处理服务
function region_service(obj){
	var t_obj=$(obj);
	var t_isInit=t_obj.attr("isInit");
	if(t_isInit!="true"){
		t_obj.attr("isInit","true");
		//初始化单个地区（json）,所有上层次数据
		region_init(obj);
	}
}

/***************************表单元素的其他处理****************************/
/*
 * region处理
 */
//region标签的初始化，sleep=睡眠多少毫秒在执行
function region_init(obj_source,sleep){
	setTimeout(function(){
		var obj_source_jq=$(obj_source);
		var value=obj_source_jq.attr("value");
		var startLevel=obj_source_jq.attr("startLevel");
		//获取全国省级（json）,所有上层次数据
		getRegionSingle(obj_source,value,function(t_obj_source,t_value,regionList){
			var obj_jq=$(t_obj_source);
			var value=obj_jq.attr("value");
			var isSplit=obj_jq.attr("isSplit");		//是否分离、分隔
			var address=obj_jq.attr("address");		//接后续的地址
			var t_html="";
			for(var i=0;regionList!=null && i<regionList.length;i++){
				var t_regionJson=regionList[i];
				var t_id=t_regionJson["id"];
				var t_name=t_regionJson["name"];
				t_html+=t_name;
				if(isSplit!=null && (isSplit==true || isSplit=="true")){
					t_html+=" ";
				}
			}
			t_html+=address!=null?address:"";
			obj_jq.attr("title",t_html)
			obj_jq.html(t_html);
		},startLevel);
	}, sleep!=null?sleep:0);
}
//select_region的初始化
function select_region_init(obj_source){
	var obj_source_jq=$(obj_source);
	var level=obj_source_jq.attr("level");	//显示地区级的层级，不存在该属性则取全部的（如：1=省【直辖市】级；2=省【直辖市】、市、级；3=省【直辖市】、市、县【区】级等等依次类推）
	var value=obj_source_jq.attr("value");
	var isHidden=obj_source_jq.attr("isHidden");		//是否隐藏，并且不取值
	if(isHidden==null || !isHidden || isHidden!="true"){
		//获取全国省级（json）,所有上层次数据
		getRegionAll(obj_source,value,function(t_obj_source,t_value,regionAllList){
			var obj_jq=$(t_obj_source);
			var index=0;	//每组，默认从0开始
			var t_name=obj_jq.attr("name");
			var t_width=obj_jq.attr("width");
			t_width=t_width!=null?t_width:"100";		//默认：100px
			var t_cutIndex=t_name.indexOf("[");
			if(t_cutIndex!=-1){
				t_name=t_name.substring(0,t_cutIndex);
			}
			var before_parentId_temp;
			var specified_region_jq;		//value指定的地区jq对象
			for(var i=0;regionAllList!=null && i<regionAllList.length;i++){
				var region_json=regionAllList[i];
				var t_obj_jq=obj_jq.clone();		//拷贝元素
				t_obj_jq.empty();
				
				t_obj_jq.attr("id",region_code+"_"+region_group_index+"_"+index);
				t_obj_jq.attr("groupIndex",region_group_index);
				t_obj_jq.attr("index",index);
				t_obj_jq.attr("group",t_name);
				t_obj_jq.attr("name",t_name+"["+index+"]");
				t_obj_jq.removeAttr("value");
				index++;
				t_obj_jq.attr("onchange","selectChange_region(this)");
				 
				t_obj_jq.css("width",t_width+"px");
				t_obj_jq.addClass("select");
				
				var isNewLine=t_obj_jq.attr("isNewLine");		//是否独占一行
				if(isNewLine!=null && isNewLine=="true"){
					t_obj_jq.css("display","block");
				}
				if(level!=null && level!=""){
					t_obj_jq.attr("level",level--);
				}
				
				//t_parentId_temp=parentId
				for(var t_parentId_temp in region_json){
					before_parentId_temp=t_parentId_temp;
					var regionList=region_json[t_parentId_temp];
					t_obj_jq.append('<option value="" selected="selected">请选择</option>');
					for(var k=0;regionList!=null && k<regionList.length;k++){
						var t_json=regionList[k];
						//j=地区ID
						for(var j in t_json){
							var t_t_josn=t_json[j];
							var t_t_name=t_t_josn["name"];
							t_obj_jq.append("<option value='"+j+"'"+(j==before_parentId_temp || j==t_value?" selected='selected' ":"")+">"+t_t_name+"</option>");
							if(j==value){
								specified_region_jq=t_obj_jq;
							}
						}
					}
				}
				t_obj_jq.attr("parentId",before_parentId_temp==null?"1":before_parentId_temp);
				obj_jq.before(t_obj_jq);
				obj_jq.css("width",t_width+"px");
				var t_region_jq=$("#"+region_code+"_"+region_group_index+"_"+(index-2));
				t_region_jq.val(before_parentId_temp);
			}
			obj_source_jq.remove();
			region_group_index++;	//组累加
			//如果有后续，则请求
			if(specified_region_jq!=null && specified_region_jq.length>0){
				selectChange_region(specified_region_jq[0]);
			}
		});
	}else{
		obj_source_jq.css("display","none");
	}
}
/*
 * select的change——地区
 * t_obj_source : 当前select元素对象
 */
function selectChange_region(t_obj_source){
	var t_obj=$(t_obj_source);
	var level=t_obj.attr("level");	//显示地区级的层级，不存在该属性则取全部的（如：1=省【直辖市】级；2=省【直辖市】、市、级；3=省【直辖市】、市、县【区】级等等依次类推）
	var t_current_parentId=t_obj.attr("parentId");		//设置当前层次的上一层ID
	if(level==null || --level>0){
		var value=t_obj.val();
		value=value!=null && value.length>0?value:null;
		//当前选择的是——请选择，则父ID必须为空
		if(value!=null){
			//获取全国省级（json）
			getRegion(t_obj_source,value,function(t_obj_source,t_value,regionList){
				var t_obj=$(t_obj_source);
				var t_groupIndex=t_obj.attr("groupIndex");
				var index=t_obj.attr("index");
				var group=t_obj.attr("group");	//组编码
				//
				index=index!=null?++index:null;	//累加
				if(regionList!=null && regionList.length>0){
					var obj_target = document.getElementById(region_code+'_'+t_groupIndex+'_'+index);		//下一级对象
					//元素对象存在
					if(obj_target){
						var obj_target_jquery=$(obj_target);
						obj_target_jquery.attr("parentId",t_value);		//设置当前层次的上一层ID
						//有后续地区
						if(regionList!=null && regionList.length>0){
							obj_target_jquery.removeAttr('readonly');  
							obj_target_jquery.empty();
							obj_target_jquery.append('<option value="" selected="selected">请选择</option>');
							for(var i=0;i<regionList.length;i++){
								var t_json=regionList[i];
								for(var j in t_json){
									obj_target_jquery.append('<option value="'+j+'">'+t_json[j]["name"]+'</option>');
								}
							}
							validateItem_success(obj_target);		//验证信息
						//没有就删除
						}else{
							obj_target_jquery.remove();
						}
						//循环删除后续的地区select
						var t_index=index+1;
						while(true){
							var t_obj_after = document.getElementById(region_code+'_'+t_groupIndex+'_'+t_index);
							if(t_obj_after!=null){
								$(t_obj_after).remove();
								t_index++;
							}else{
								break;
							}
						}
					}else{
						var t_obj_jq=t_obj.clone();		//拷贝元素
						/*清除*/
						t_obj_jq.empty();
						validateItem_success(t_obj_jq[0]);		//验证信息
						
						var isNewLine=t_obj_jq.attr("isNewLine");		//是否独占一行
						if(isNewLine!=null && isNewLine=="true"){
							t_obj_jq.css("display","block");
						}
						var isNotEmpty=t_obj_jq.attr("isNotEmpty");		//是否不为空（必选）
						var t_val_type=t_obj_jq.attr("val-type");
						var t_val_type_new="";
						if(isNotEmpty!=null && isNotEmpty=="true"){
							if(t_val_type!=null && t_val_type.length>0){
								var t_val_type_arr=t_val_type.split(" ");
								for(var i=0;i<t_val_type_arr.length;i++){
									var t_val_type_item=t_val_type_arr[i];
									if(t_val_type_item.toLowerCase()!="empty"){
										t_val_type_new+=t_val_type_item+" ";
									}
								}
							}
						}else{
							t_val_type_new=t_val_type;
						}
						t_obj_jq.attr("val-type",t_val_type_new);
						t_obj_jq.attr("name",group+'['+index+']');
						t_obj_jq.attr("id",region_code+'_'+t_groupIndex+'_'+index);
						t_obj_jq.attr("group",group);
						t_obj_jq.attr("groupIndex",t_groupIndex);
						t_obj_jq.attr("index",index);
						t_obj_jq.attr("parentId",t_value);
						if(level==null){
							t_obj_jq.attr("onChange","selectChange_region(this)");
						}else{
							t_obj_jq.attr("level",level);
						}
						
						t_obj_jq.append('<option value="" selected="selected">请选择</option>');
						for(var i=0;i<regionList.length;i++){
							var t_json=regionList[i];
							for(var j in t_json){
								var t_t_josn=t_json[j];
								var t_name=t_t_josn["name"];
								t_obj_jq.append('<option value="'+j+'">'+t_name+'</option>');
							}
						}
						t_obj.after(t_obj_jq);
					}
				}else{
					//循环删除后续的地区select
					var t_index=index;
					while(true){
						var t_obj_after = document.getElementById(region_code+'_'+t_groupIndex+'_'+t_index);
						if(t_obj_after!=null){
							$(t_obj_after).remove();
							t_index++;
						}else{
							break;
						}
					}
				}
			});
		}else{
			var index=t_obj.attr("index");
			var t_groupIndex=t_obj.attr("groupIndex");
			//循环删除后续的地区select
			var t_index=++index;
			while(true){
				var t_obj_after = document.getElementById(region_code+'_'+t_groupIndex+'_'+t_index);
				if(t_obj_after!=null){
					$(t_obj_after).remove();
					t_index++;
				}else{
					break;
				}
			}
		}
	}
}

/*************************其他处理************************/
//number数字输入框增加1；id=目标数字输入框
function numberIncrease(id,fn){
	numberProcee(id, 1,true,fn);
}
//number数字输入框减少1；id=目标数字输入框；isNegativeNumber=是否允许负数
function numberDecrease(id,isNegativeNumber,fn){
	numberProcee(id, -1,isNegativeNumber,fn);
}
//number数字输入框处理；id=目标数字输入框；number=增减数；fn=回调函数（参数1=处理后的值）
function numberProcee(id,number,isNegativeNumber,fn){
	var t_jq=$("#"+id);
	if(t_jq!=null && t_jq.length>0){
		var t_val=t_jq.val();
		if(!isNaN(t_val)){
			t_val=parseInt(t_val);
			if(t_val<=0 && isNegativeNumber!=null && !isNegativeNumber){
				return;
			}
			t_val=t_val+number;
		}else{
			t_val=1;
		}
		t_jq.val(t_val);
		if(fn!=null){fn(t_val);}
	}
}
/*表单验证*/
function validationForm(formObj){
	var formObj_jq=$(formObj);
	var paramObjArr_out=new Array();		//表单下所有指定标签的jquery对象(外层)
	paramObjArr_out.push(formObj_jq.find("input"));
	paramObjArr_out.push(formObj_jq.find("select"));
	paramObjArr_out.push(formObj_jq.find("textarea"));
	for(var i=0;i<paramObjArr_out.length;i++){
		var paramObjArr_inner=paramObjArr_out[i];
		for(var j=0;j<paramObjArr_inner.length;j++){
			var obj=paramObjArr_inner[j];
			var isValid=validateItem(obj);
			if(isValid==false){
				return false;
			}
		}
	}
	return true;
}
//验证：带val-type属性的元素及处理
function validateItem(obj){
	var result=ValidateUitl.validate(obj);	//不为boolean类型的true,则是验证错误信息（默认）
	if(result!=true){
		return validateItem_failure(obj,result);
	}else{
		return validateItem_success(obj);
	}
}
//验证单项失败（failureMsg=失败提示语）
function validateItem_failure(obj,failureMsg){
	var obj_jq=$(obj);
	obj_jq.css("border-color","#CD0000");
	/*滚动至元素位置*/
	var t_obj_top=obj_jq.offset().top;
	
	/**取出当前元素的父窗体（根据class是否存在model，当指定次数后未寻找到，则取当前主窗体【html,body】）**/
	var t_num=50;		//指定循环次数
	var t_parent_jq=obj_jq.parent();
	var t_isExist_class_model=t_parent_jq.hasClass("layui-layer-content");		//是否存在【layui-layer-content】class
	while(!t_isExist_class_model && t_num>0){
		t_parent_jq=t_parent_jq.parent();
		if(t_parent_jq!=null && t_parent_jq.length>0){
			t_isExist_class_model=t_parent_jq.hasClass("layui-layer-content");		//是否存在【layui-layer-content】class
			t_num--;
		}else{
			t_isModal=true;
			break;
		}
	}
	var t_scroll_panel_jq;		//需要滚动的面板jq
	var t_scroll_height=100;	//body窗体
	var t_scroll_top=100;		//距离顶部多少才把提示框放到bottom（默认body窗体）
	if(t_isExist_class_model){
		t_scroll_panel_jq=t_parent_jq;
		t_scroll_height=60;
	}else{
		t_scroll_panel_jq=$('html, body');
	}
	/*滚动*/
	t_scroll_panel_jq.animate({  
        scrollTop: t_obj_top-t_scroll_height
    }, 250);  
	//提示
	promptDialog(false,failureMsg);
	obj_jq.attr("onblur","validateItem_blur(this)");
	return false;
}
//验证单项成功
function validateItem_success(obj){
	var obj_jq=$(obj);
	obj_jq.css("border-color","");
	return true;
}
//验证：带val-type属性的元素及处理——焦点式
function validateItem_blur(obj){
	var result=ValidateUitl.validate(obj);	//不为boolean类型的true,则是验证错误信息（默认）
	if(result!=true){
		
	}else{
		return validateItem_success(obj);
	}
}
//验证所有的表单元素
function validateAllItem(){
	var t_formAll_jq=$("form");
	for(var i=0;i<t_formAll_jq.length;i++){
		var t_form_obj=t_formAll_jq[i];
		validationForm(t_form_obj);
	}
}
//获取传入的event，您当前按的键盘的AscII值
function getKeyCode(event){
	return event.keyCode ? event.keyCode : event.which;
}
//当前js的ajax请求
function _ajax_formUtil(url,jsonData,successFn,errorFn){
	$.ajax({url : url,traditional: true,data : jsonData,type : "post",dataType:"jsonp",timeout:5000,jsonp:"callback",
		success : function(result) {
			if(successFn!=null){successFn(result);};
		},
		error : function(data) {
			if(errorFn!=null){errorFn(data);}
		}
	});
}

/*********地区请求*********/
/*获取全国地区所有上层次，t_obj_source=原DOM元素对象，parentId=上一级的ID*/
function getRegionAll(t_obj_source,value,fn){
	_ajax_formUtil(_serviceUrl+"global/regionAll",{"code":value},function(result){
		var regionList=result;
		if(fn!=null){
			fn(t_obj_source,value,regionList);
		}
    });
}
/*获取全国地区，t_obj_source=原DOM元素对象，value=展示该级子层级数据的的ID*/
function getRegion(t_obj_source,value,fn){
	_ajax_formUtil(_serviceUrl+"global/region",{"parentCode":value},function(result){
		var regionList=result;
		if(fn!=null){
			fn(t_obj_source,value,regionList);
		}
    });
}
/*获取单个地区，t_obj_source=原DOM元素对象，value=展示该级子层级数据的的ID，fn=回调函数，startLevel=起始层级（1=省级；2=市级......）*/
function getRegionSingle(t_obj_source,value,fn,startLevel){
	_ajax_formUtil(_serviceUrl+"global/regionSingle", {"code":value,"startLevel":startLevel},function(result){
		var regionList=result;
		if(fn!=null){
			fn(t_obj_source,value,regionList);
		}
    });
}