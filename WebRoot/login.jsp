<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="global.jsp" %>
<%
	if((com.ltms.model.Admin)session.getAttribute("admin") != null){
		response.sendRedirect("admin/index.jsp");
		return;
	}
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>管理系统登录</title>
<link href="css/login.css" rel="stylesheet" type="text/css" />
<script type="text/javascript">
String.prototype.trim = function(){ return this.replace(/(^\s*)|(\s*$)/g, "");}
function ShowErrMsg(Info)
{
	document.getElementById("showMsg").innerHTML = Info;
}
function submitfrm(frm)
{
	if(frm.id.value.trim()=="")
	{
		 ShowErrMsg("用户名不能为空，请输入");
		 frm.id.focus();
		 return false;
	}
	if(frm.userpwd.value.trim()=="")
	{
		 ShowErrMsg("密码不能为空，请输入");
		 frm.userpwd.focus();
		 return false;
	}
	return true;
}
</script>
</head>
<body>
<div id="showMsg">
<%=	(String)request.getAttribute("error") != null ? (String)request.getAttribute("error") : "欢迎使用，请登陆..." %>
</div>
<center>
<div id="login">
	<div id="top">
		<div id="top_left"><img src="images/login_03.gif" /></div>
		<div id="top_center"></div>
	</div>
	<div id="center">
		<div id="center_left"><a href="<%= basepath%>/index.jsp"><img src="<%= basepath%>/images/login_09.gif" /></a></div>
		<div id="center_middle">
		<div style="height:35px;"></div>
		<form id="form1" name="form1" method="post" action="LoginServlet" onsubmit="return submitfrm(this);">	
		<table border=0 cellspacing=0 cellpadding=0 style="border:none;">
			<tr>
				<td style="text-align:right;">用户名</td>
				<td class="user_input_ltms"><input id="id" type="text" name="id" /></td>
			</tr>
			<tr>
				<td style="text-align:right;">密　码</td>
				<td class="user_input_ltms"><input id="userpwd"  type="password" name="password" /></td>
			</tr>
			<tr>
				<td style="text-align:right;">角　色</td>
				<td class="user_input_ltms">
				<select name="role">
					<option value="administrator_0">系统管理员</option>
					<option value="administrator_3">学院主管领导</option>
					<option value="administrator_4">实验室主任</option>
					<option value="sysgly_5">实验室管理员</option>
					<option value="administrator_1">教务员</option>
					<option value="teacher_2">教师</option>
				</select>
				</td>
			</tr>
			<tr>
				<td colspan="2" id="btn">
				<input id="button1" type="submit" value="登陆" name="Button1" style="vertical-align:middle;"/>
				<input type="reset" value="清空" name="Submit3" style="vertical-align:middle;margin-left:10px;"/>
				</td>
			</tr>
		</table>
		</form>
		</div>
		<div id="center_right"></div>
	</div>		 
	<div id="down">
		<div id="down_left"></div>
		<div id="down_center"></div>		 
	</div>
</div>
</center>
</body>
</html>


