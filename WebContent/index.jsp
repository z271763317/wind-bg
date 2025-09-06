<%@page import="org.wind.bg.util.system.SystemUtil" %>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	SystemUtil.jumpLogin(request, response);		//跳转 : 登录界面（未登录则跳转）
	request.setAttribute("systemName", SystemUtil.getSystemName());
%>
<!DOCTYPE HTML>
<html>
<head>
	<title>${systemName}</title>
	<jsp:include page="/globalFile/global.jsp" />
	<link rel="stylesheet" type="text/css" href="css/index.css" />
	<style type="text/css">
		.J_codeimg {
			z-index: -1;
			position: absolute;
		}
	</style>
	<script>
		var cookie_loginUserName_name="_local_loginUserName";		//登录的用户名
		var cookie_loginPwd_name="_local_loginPwd";		//登录的密码
		var referer="user";		//默认跳转URL
		$(document).ready(function() {
			/**cookie——用户名**/
			setCookieValueToInput(cookie_loginUserName_name,"userName");
			var referer_new=decodeURIComponent(getUrlParam("refere"));
			if(referer_new!=null && referer_new!="" && referer_new!="null"){
				referer=referer_new;
			}
		});
		$(document).keydown(function(event){ 
			if(event.keyCode==13){
				login();
			}
		});
		
		//获取cookie
		function getCookieValue(cookieName){
			var arr,reg=new RegExp("(^| )"+cookieName+"=([^;]*)(;|$)");
			if(arr=document.cookie.match(reg)){
				return unescape(arr[2]);
			}
		}
		//取cookie设置到输入框
		function setCookieValueToInput(cookieName,id){
			var t_value=getCookieValue(cookieName);
			if(t_value!=null){
				$("#"+id).val(t_value);	
			}
		}
		//登录
		function login(){
			var userName=$("#userName").val();		//用户名
			var passWord=$("#passWord").val();		//密码
			var captcha=$("#J_codetext").val();			//验证码
			ajax("login",{"userName":userName,"passWord":passWord,"captcha":captcha},function(result){
				var Days = 30;		//保存30天
				var exp = new Date();
				exp.setTime(exp.getTime() + Days*24*60*60*1000);
				/**记住用户名——cookie**/
				document.cookie=cookie_loginUserName_name+"="+userName+";expires=" + exp.toGMTString();
				//
				window.setTimeout(function(){
					window.location.href=referer;
				},1000);
			//失败
			},function(result){
				var isYzm=result["isYzm"];		//是否开启验证码
				var message=result["message"];
				if(isYzm!=null && isYzm==true){
					var t_panel_yzm_jq=$("#panel_yzm");
					var isHide=t_panel_yzm_jq.is(":hidden");
					//是否隐藏
					if(isHide){
						t_panel_yzm_jq.show();
					//之前就显示
					}else{
						promptDialog(false,message);
					}
					createCode();
				}else{
					promptDialog(false,message);
				}
			},null,true,null,null,"登录成功",true);
		}
		//清空
		function empty(){
			$("#userName").val("");
			$("#passWord").val("");
			$("#J_codetext").val("");
		}
		//生成验证码
		function createCode(){
			var userName=trim($("#userName").val());		//用户名
			if(userName!=null && userName.length>0){
			   	var t_jq=$("#myCanvas");
			   	var t_src=t_jq.attr("temp_src");
			   	t_jq.attr("src",t_src+"?"+Math.random()+"&userName="+userName);
			}
		}
	</script>
</head>
<body>
	<div class="login-box" id="demo">
		<div class="input-content">
			<div class="login_tit">
				<div>
					<i class="tit-bg left"></i>${systemName}<i class="tit-bg right"></i>
					<p>by&nbsp;.&nbsp;wind（www.tcin.cn）</p>
				</div>
			</div>
			<p class="p user_icon">
				<input type="text" id="userName" placeholder="用户名" autocomplete="off" class="login_txtbx" />
			</p>
			<p class="p pwd_icon">
				<input type="password" id="passWord" placeholder="密码" autocomplete="off" class="login_txtbx" />
			</p>
			<!-- 验证码 -->
			<div class="p val_icon" id="panel_yzm" style="display: none">
				<div class="checkcode">
					<input type="text" id="J_codetext" placeholder="验证码" autocomplete="off" maxlength="4" class="login_txtbx" />
					<img temp_src="login/captcha" class="J_codeimg" id="myCanvas" οnclick="createCode()" />
				</div>
				<a href="javascript:createCode()" class="ver_btn"  onselectstart="return false">看不清，换一张</a>
			</div>
			<div class="signup">
				<a class="gv" href="javascript:void()" onclick="login()">登&nbsp;&nbsp;录</a> 
				<a class="gv" href="javascript:void()" onClick="empty()">清&nbsp;&nbsp;空</a>
			</div>
		</div>
		<div class="canvaszz"></div>
		<canvas id="canvas"></canvas>
	</div>
	<script>
		//宇宙特效
		"use strict";
		var canvas = document.getElementById('canvas'),
		  ctx = canvas.getContext('2d'),
		  w = canvas.width = window.innerWidth,
		  h = canvas.height = window.innerHeight,
		
		  hue = 217,
		  stars = [],
		  count = 0,
		  maxStars = 2500;//星星数量
		
		var canvas2 = document.createElement('canvas'),
		  ctx2 = canvas2.getContext('2d');
		canvas2.width = 100;
		canvas2.height = 100;
		var half = canvas2.width / 2,
		  gradient2 = ctx2.createRadialGradient(half, half, 0, half, half, half);
		gradient2.addColorStop(0.025, '#CCC');
		gradient2.addColorStop(0.1, 'hsl(' + hue + ', 61%, 33%)');
		gradient2.addColorStop(0.25, 'hsl(' + hue + ', 64%, 6%)');
		gradient2.addColorStop(1, 'transparent');
		
		ctx2.fillStyle = gradient2;
		ctx2.beginPath();
		ctx2.arc(half, half, half, 0, Math.PI * 2);
		ctx2.fill();
		
		// End cache
		
		function random(min, max) {
		  if (arguments.length < 2) {
		    max = min;
		    min = 0;
		  }
		
		  if (min > max) {
		    var hold = max;
		    max = min;
		    min = hold;
		  }
		
		  return Math.floor(Math.random() * (max - min + 1)) + min;
		}
		
		function maxOrbit(x, y) {
		  var max = Math.max(x, y),
		    diameter = Math.round(Math.sqrt(max * max + max * max));
		  return diameter / 2;
		  //星星移动范围，值越大范围越小，
		}
		
		var Star = function() {
		
		  this.orbitRadius = random(maxOrbit(w, h));
		  this.radius = random(60, this.orbitRadius) / 18; 
		  //星星大小
		  this.orbitX = w / 2;
		  this.orbitY = h / 2;
		  this.timePassed = random(0, maxStars);
		  this.speed = random(this.orbitRadius) / 500000; 
		  //星星移动速度
		  this.alpha = random(2, 10) / 10;
		
		  count++;
		  stars[count] = this;
		}
		
		Star.prototype.draw = function() {
		  var x = Math.sin(this.timePassed) * this.orbitRadius + this.orbitX,
		    y = Math.cos(this.timePassed) * this.orbitRadius + this.orbitY,
		    twinkle = random(10);
		
		  if (twinkle === 1 && this.alpha > 0) {
		    this.alpha -= 0.05;
		  } else if (twinkle === 2 && this.alpha < 1) {
		    this.alpha += 0.05;
		  }
		
		  ctx.globalAlpha = this.alpha;
		  ctx.drawImage(canvas2, x - this.radius / 2, y - this.radius / 2, this.radius, this.radius);
		  this.timePassed += this.speed;
		}
		
		for (var i = 0; i < maxStars; i++) {
		  new Star();
		}
		
		function animation() {
		  ctx.globalCompositeOperation = 'source-over';
		  ctx.globalAlpha = 0.5; //尾巴
		  ctx.fillStyle = 'hsla(' + hue + ', 64%, 6%, 2)';
		  ctx.fillRect(0, 0, w, h)
		
		  ctx.globalCompositeOperation = 'lighter';
		  for (var i = 1, l = stars.length; i < l; i++) {
		    stars[i].draw();
		  };
		
		  window.requestAnimationFrame(animation);
		}
		animation();
	</script>
</body>
</html>