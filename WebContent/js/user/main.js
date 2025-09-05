var $,tab,dataStr,layer;
layui.config({
	base : "js/user/"
}).extend({
	"bodyTab" : "bodyTab"
})
layui.use(['bodyTab','form','element','layer','jquery'],function(){
	var form = layui.form,
	element = layui.element;
	$ = layui.$;
	layer = parent.layer === undefined ? layui.layer : top.layer;
	tab = layui.bodyTab({
		openTabNum : "50",  //最大可打开窗口数量
		url : "user/menu" //获取菜单json地址
	});
	   
	//展示当前指定的系统菜单
	getData("mainSystem");
	//通过顶部菜单获取左侧二三级菜单
	function getData(code){
		var param="?code="+code;
		$.getJSON(tab.tabConfig.url+param,function(data){
			dataStr = data;
			//重新渲染左侧菜单、菜单搜索框
			tab.render();
			reloadSearchMenu(dataStr);
		});
	}
	//页面加载时判断左侧菜单是否显示
	//通过顶部菜单获取左侧菜单
	$(".topLevelMenus li,.mobileTopLevelMenus dd").click(function(){
		if($(this).parents(".mobileTopLevelMenus").length != "0"){
			$(".topLevelMenus li").eq($(this).index()).addClass("layui-this").siblings().removeClass("layui-this");
		}else{
			$(".mobileTopLevelMenus dd").eq($(this).index()).addClass("layui-this").siblings().removeClass("layui-this");
		}
		$(".layui-layout-admin").removeClass("showMenu");
		$("body").addClass("site-mobile");
		getData($(this).data("menu"));
		//渲染顶部窗口
		tab.tabMove();
	});

	//隐藏左侧导航
	$(".hideMenu").click(function(){
		if($(".topLevelMenus li.layui-this a").data("url")){
			layer.msg("此栏目状态下左侧菜单不可展开");  //主要为了避免左侧显示的内容与顶部菜单不匹配
			return false;
		}
		$(".layui-layout-admin").toggleClass("showMenu");
		//渲染顶部窗口
		tab.tabMove();
	});

	//手机设备的简单适配
    $('.site-tree-mobile').on('click', function(){
		$('body').addClass('site-mobile');
	});
    $('.site-mobile-shade').on('click', function(){
		$('body').removeClass('site-mobile');
	});

	// 添加新窗口
	$("body").on("click",".layui-nav .layui-nav-item a:not('.mobileTopLevelMenus .layui-nav-item a')",function(){
		//如果不存在子级
		addTab_entrance(this);
	});

	//清除缓存
	$(".clearCache").click(function(){
		window.sessionStorage.clear();
        window.localStorage.clear();
        var index = layer.msg('清除缓存中，请稍候',{icon: 16,time:false,shade:0.8});
        setTimeout(function(){
            layer.close(index);
            layer.msg("缓存清除成功！");
        },1000);
    })

	//刷新后还原打开的窗口
    if(cacheStr == "true") {
        if (window.sessionStorage.getItem("menu") != null) {
            menu = JSON.parse(window.sessionStorage.getItem("menu"));
            curmenu = window.sessionStorage.getItem("curmenu");
            var openTitle = '';
            for (var i = 0; i < menu.length; i++) {
                openTitle = '';
                if (menu[i].icon) {
                    if (menu[i].icon.split("-")[0] == 'icon') {
                        openTitle += '<i class="seraph ' + menu[i].icon + '"></i>';
                    } else {
                        openTitle += '<i class="layui-icon">' + menu[i].icon + '</i>';
                    }
                }
                openTitle += '<cite>' + menu[i].title + '</cite>';
                openTitle += '<i class="layui-icon layui-unselect layui-tab-close" data-id="' + menu[i].layId + '">&#x1006;</i>';
                alert(rightContentHeight);
                element.tabAdd("bodyTab", {
                    title: openTitle,
                    content: "<iframe src='" + menu[i].href + "' data-id='" + menu[i].layId + "' scrolling=\"yes\" style=\"height:"+rightContentHeight+"px\"></frame>",
                    id: menu[i].layId
                })
                //定位到刷新前的窗口
                if (curmenu != "undefined") {
                    if (curmenu == '' || curmenu == "null") {  //定位到后台首页
                        element.tabChange("bodyTab", '');
                    } else if (JSON.parse(curmenu).title == menu[i].title) {  //定位到刷新前的页面
                        element.tabChange("bodyTab", menu[i].layId);
                    }
                } else {
                    element.tabChange("bodyTab", menu[menu.length - 1].layId);
                }
            }
            //渲染顶部窗口
            tab.tabMove();
        }
    }else{
		window.sessionStorage.removeItem("menu");
		window.sessionStorage.removeItem("curmenu");
	}
	
	/**********新增加得功能（2021年4月15日 18:08:58）*********/
	//搜索框时间
	form.on('select(searchPage)', function(data){
		searchMenu();
	}); 
	//重载新的菜单搜索框（menuList=菜单json）
	function reloadSearchMenu(menuList){
		var search_jq=$("#search");
		var t_return_json=getSpecificMenu(menuList);
		//
		search_jq.empty();	//清空select现有的内容
		for(var t_key in t_return_json){
			var t_name=t_return_json[t_key];
			search_jq.append("<option value='"+t_key+"'>"+t_name+"</option>");
		}
		form.render('select'); //刷新select选择框渲染
	}
	//获取 : 具体的菜单
	function getSpecificMenu(menuList){
		var t_return_json={};
		if(menuList!=null){
			for(var i=0;i<menuList.length;i++){
				var t_json=menuList[i];
				var t_children=t_json["children"];
				//有子菜单
				if(t_children!=null && t_children.length>0){
					var t_specificMenuJson=getSpecificMenu(t_children);
					for(var t_key in t_specificMenuJson){
						var t_specificMenuName=t_specificMenuJson[t_key];
						t_return_json[t_key]=t_specificMenuName;
					}
				}else{
					var t_id=t_json["href"];
					var t_name=t_json["title"];
					t_return_json[t_id]=t_name;
				}
			}
		}
		return t_return_json;
	}
	//搜索菜单
	function searchMenu(){
		var val=$("#search").val();
		var t_jq=$("a[data-url='"+val+"']");
		if(t_jq!=null && t_jq.length>0){
			addTab_entrance(t_jq[0]);
		}
	}
});

//打开新窗口（原始元素，点击菜单的入口方法）
function addTab_entrance(obj){
	//如果不存在子级
	if($(obj).siblings().length == 0){
		addTab($(obj));
		$('body').removeClass('site-mobile');  //移动端点击菜单关闭菜单层
	}
	$(obj).parent("li").siblings().removeClass("layui-nav-itemed");
}
//打开新窗口
function addTab(_this){
	tab.tabAdd(_this);
}
