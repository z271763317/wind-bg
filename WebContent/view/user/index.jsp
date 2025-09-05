<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="utf-8">
	<title>个人中心</title>
	<jsp:include page="/globalFile/global.jsp" />
	<style type="text/css">
		
	</style>
	<script type="text/javascript" >
		//页面渲染后执行
		$(document).ready(function(){
			
		});
	</script>
</head>
<body>
	<div style="padding:15px; background-color: #F2F2F2;">
		<div class="layui-row layui-col-space15">
			<div class="layui-col-md12">
				<div class="layui-card">
					<div class="layui-card-body">
						<div style="padding:20px;">
							<form action="modifyPassWord"  class="layui-form" >
								<table style="margin:0 auto;">
									<tr>
										<td style="text-align:right">旧密码：</td>
										<td>
											<div class="layui-input-inline" style="width:250px">
										    	<input type="password" name="oldPassWord" autocomplete="off" class="layui-input" />
											</div>
										</td>
									</tr>
									<tr>
										<td style="text-align:right">新密码：</td>
										<td>
											<div class="layui-input-inline" style="width:250px">
										    	<input type="password" name="newPassWord" autocomplete="off" class="layui-input" />
											</div>
										</td>
									</tr>
									<tr>
										<td style="text-align:right">确认密码：</td>
										<td>
											<div class="layui-input-inline" style="width:250px">
										    	<input type="password" name="confirmNewPassWord" autocomplete="off" class="layui-input" />
											</div>
										</td>
									</tr>
									<tr>
										<td colspan="2" style="padding:15px 0px">
											<div style="text-align:center">
												<button type="button" class="layui-btn" onClick="save(this)">修 改</button>
											</div>
										</td>
									</tr>
								</table>
							</form>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
</body>
</html>