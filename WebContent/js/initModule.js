/*****加载layui模块*****/
var layui_layer;			//弹出层
var layui_table;			//表格
var layui_form;			//表单
var layui_laydate;		//日期
var layui_laypage;		//分页
var layui_tree;			//树
var layui_element;	//元素
var layui_upload;		//上传
var layui_dropdown;	//下拉菜单

/*****配置*****/
var _layui_tableConfig;
var _layui_pageConfig;

layui.use(['layer'], function(){
	layui_layer = layui.layer;
});

//页面渲染后执行
$(document).ready(function(){
	_initConfig();
	_initLoad();
});
/*****初始化加载*****/
function _initLoad(){
	layui.use(['form','element'], function(){
		layui_form = layui.form;
		layui_element = layui.element;
	});
}
/*****初始化配置*****/
function _initConfig(){
	//表格配置
	_layui_tableConfig={elem: '#list',id:'layui_list',url:pathName+'/list',method:'post',loading:true
		//全局定义常规单元格的最小宽度，layui 2.2.1 新增
//		,cellMinWidth: 70 
		//分页组件
		,page:true
		,response: {
			statusName: 'code' 		//规定数据状态的字段名称，默认：code
			,statusCode: 1 				//规定成功的状态码，默认：0
			,msgName: 'message' 	//规定状态信息的字段名称，默认：msg
			,countName: 'count' 	//规定数据总数的字段名称，默认：count
			,dataName: 'data' 			//规定数据列表的字段名称，默认：data
		}
	}
	//分页
	_layui_pageConfig={
		dataUrl:pathName+"/list"		//数据请求URL
		,param:{}			//请求参数json
//		,callback:function(){}		//请求成功后的回调
		,layout: ['limit', 'count', 'prev', 'page', 'next', 'skip']
		,jump:function(obj,first){
			if(!first){
				renderLaypageRun(obj);
			}
		}
	}
}
/*使用默认配置（已有的不会被替换）*/
function useDefaultConfig(defaultConfig,config){
	for(var key in defaultConfig) {
	    if(!config.hasOwnProperty(key)){
	    	config[key]=defaultConfig[key];
	    }
	}
	return config;
}

/*****渲染*****/
/*表格*/
function renderTable(config,fn){
	useDefaultConfig(_layui_tableConfig,config);
	for(var key in config) {
	    //列
	    if(key=="cols"){
	    	var cols=config[key];
	    	for(var i=0;i<cols.length;i++){
	    		var arr2=cols[i];
	    		for(var j=0;j<arr2.length;j++){
		    		var colMap=arr2[j];
		    		var style=colMap["style"];
		    		//样式
		    		if(style!=null){
			    		//字体
		    			if(style.indexOf("font-size")==-1){
		    				style+=";font-size:12px";
		    			}
		    		}else{
		    			style="font-size:12px";
		    		}
		    		colMap["style"]=style;
		    	}
	    	}
	    }
	}
	//存在
	if(layui_table!=null){
		var renderObj=layui_table.render(config);
		if(fn!=null){
			fn(renderObj);
		}
	}else{
		layui.use('table', function(){
			layui_table = layui.table;
			var renderObj=layui_table.render(config);
			if(fn!=null){
				fn(renderObj);
			}
		});
	}
}
//弹出层
function renderLayer(fn){
	//存在
	if(layui_layer!=null){
		if(fn!=null){
			fn(layui_layer);
		}
	}else{
		layui.use('layer', function(){
			layui_layer = layui.layer;
			if(fn!=null){
				fn(layui_layer);
			}
		});
	}
}
//日期时间选择框
function renderLaydate(config,fn){
	if(layui_laydate!=null){
		var renderObj=layui_laydate.render(config);
		if(fn!=null){
			fn(renderObj);
		}
	}else{
		layui.use('laydate', function(){
			layui_laydate = layui.laydate;
			var renderObj=layui_laydate.render(config);
			if(fn!=null){
				fn(renderObj);
			}
		});
	}
}
//分页
function renderLaypage(config,isRunFirst,fn){
	useDefaultConfig(_layui_pageConfig,config);
	if(isRunFirst!=null && isRunFirst==true){
		renderLaypageRun(config,fn);
	}else{
		if(layui_laypage!=null){
			var renderObj=layui_laypage.render(config);
			if(fn!=null){
				fn(renderObj);
			}
		}else{
			layui.use('laypage', function(){
				layui_laypage=layui.laypage;
				var renderObj=layui_laypage.render(config);
				if(fn!=null){
					fn(renderObj);
				}
			});
		}
	}
}
//分页执行
function renderLaypageRun(config,fn){
	var dataUrl=config["dataUrl"];
	var param=config["param"];
	var callback=config["callback"];
	//
	var page=config.curr; //得到当前页，以便向服务端请求对应页的数据。
    var limit=config.limit; //得到每页显示的条数
    param["page"]=page;
    param["limit"]=limit;
	//
	ajax(dataUrl,param,function(result){
		var t_count=result["count"];
		config["count"]=t_count;
		renderLaypage(config,false,fn);
		if(callback!=null){
			callback(result,config);
		}
	},function(result){
		if(callback!=null){
			callback(result,config);
		}
	},null,false,null,null,null,false);
}
//树形组件
function renderTree(config,fn){
	if(layui_laypage!=null){
		var renderObj=layui_tree.render(config);
		if(fn!=null){
			fn(renderObj);
		}
	}else{
		layui.use('tree', function(){
			layui_tree=layui.tree;
			var renderObj=layui_tree.render(config);
			if(fn!=null){
				fn(renderObj);
			}
		});
	}
}
//表单
function renderForm(fn){
	if(layui_form!=null){
		if(fn!=null){
			fn();
		}
	}else{
		layui.use('form', function(){
			layui_form = layui.form;
			if(fn!=null){
				fn();
			}
		});
	}
}
//元素
function renderElement(fn){
	if(layui_element!=null){
		if(fn!=null){
			fn();
		}
	}else{
		layui.use('element', function(){
			layui_element = layui.element;
			if(fn!=null){
				fn();
			}
		});
	}
}
//上传
function renderUpload(config,fn){
	if(layui_upload!=null){
		var renderObj = layui_upload.render(config);
		if(fn!=null){
			fn(renderObj);
		}
	}else{
		layui.use('upload', function(){
			layui_upload = layui.upload;
			var renderObj = layui_upload.render(config);
			if(fn!=null){
				fn(renderObj);
			}
		});
	}
}
//下拉菜单
function renderDropdown(config,fn){
	if(layui_dropdown!=null){
		var renderObj = layui_dropdown.render(config);
		if(fn!=null){
			fn(renderObj);
		}
	}else{
		layui.use('dropdown', function(){
			layui_dropdown = layui.dropdown;
			var renderObj = layui_dropdown.render(config);
			if(fn!=null){
				fn(renderObj);
			}
		});
	}
}
