<%@ page language="java" import="java.util.*, com.ltms.model.*, java.sql.*" pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link rel="stylesheet" type="text/css" href="css/global.css" />
</head>
<body>
<script type="text/javascript">
String.prototype.trim = function(){ return this.replace(/(^\s*)|(\s*$)/g, "");}
//显示错误信息
function ShowErrMsg(Info)
{
	document.getElementById("showMsg").innerHTML = Info;
}

window.onscroll = function()   
{   
	var t = document.body.scrollTop + 30;     
	document.getElementById("showMsg").style.top = t + "px";
}
</script>
<div id="position">您现在所在页面：系统管理&nbsp;&gt;&gt;&nbsp;<a href="modifyPassword.jsp" target="content">修改密码</a></div>
<div class="detailInfoContent">
<%@ include file="showMsg.jsp" %>
<form id="form1" name="form1" method="post" action="AdminServlet">
<input type="hidden" name="method" value="update" />
<table border=0 cellspacing=0 cellpadding=0>
	<tr>
  		<td class="TD_Left" style="text-align:right;">原密码&nbsp;</td>
  		<td class="TD_Right" style="width:200px;"><input name="oldpwd" type="password" class="input1" id="oldpwd" /></td>
  	</tr>
  	<tr>
  		<td class="TD_Left" style="text-align:right;">新密码&nbsp;</td>
  		<td class="TD_Right"><input name="newpwd" type="password" class="input1" id="newpwd" /></td>
  	</tr>
  	<tr>
  		<td class="TD_Left" style="text-align:right;width:100px;">确认新密码&nbsp;</td>
  		<td class="TD_Right"><input name="newpwd1" type="password" class="input1" id="newpwd1" /></td>
  	</tr>
  	<tr>
  		<td class="TD_Left" style="text-align:right;">操作&nbsp;</td>
  		<td class="TD_Right"><input type="button" value="修改" onClick="javascript:formsubmit(this.form);" class="form_btn"/></td>
  	</tr>
</table>
</form>
<script type="text/javascript">
function formsubmit(frm)
{
	if(frm.oldpwd.value.trim()=="")
	{
		ShowErrMsg("原密码不能为空，请重新输入")
		frm.oldpwd.focus();
		return false;
	}
	if(frm.newpwd.value.trim()=="")
	{
		ShowErrMsg("新密码不能为空，请重新输入")
		frm.newpwd.focus();
		return false;
	}
	if(frm.newpwd.value.trim()!=frm.newpwd1.value.trim())
	{
		ShowErrMsg("两次输入的新密码不一致，请重新输入")
		frm.newpwd1.focus();
		return false;
	}
	frm.submit();
}
</script>
</div>
</body>
</html>