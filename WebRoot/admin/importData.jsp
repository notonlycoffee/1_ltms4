<%@ page language="java" import="java.util.*, com.ltms.model.*, java.sql.*,com.ltms.dao.*" pageEncoding="utf-8"%>
<%
	int role = ((Admin)request.getSession().getAttribute("admin")).getRole();
	int departmentID = ((Admin)request.getSession().getAttribute("admin")).getDepartmentID();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link rel="stylesheet" type="text/css" href="css/global.css" />
<script src="js/prototype.js" type="text/javascript"></script>
</head>
<body>
<div id="position">您现在所在页面：系统管理&nbsp;&gt;&gt;&nbsp;<a href="importData.jsp" target="content">数据导入</a></div>

<script type="text/javascript">
String.prototype.trim = function(){ return this.replace(/(^\s*)|(\s*$)/g, "");}
function ShowErrMsg(Info)
{
	$("showMsg").innerHTML = Info;
}
function submitfrm(frm)
{
	if(frm.excel_file.value != null && frm.excel_file.value.trim() == "")
	{
		 alert("请先上传excel文件");
		 frm.excel_file.focus();
		 return false;
	}
	return true;
}
</script>
<div id="content" style="text-align:left">
<%@ include file="showMsg.jsp" %>

<!--
<form action="CourseServlet" method="post" enctype="multipart/form-data" onsubmit="return submitfrm(this);">
<input type="hidden" name="method" value="import_from_excel" />
<table cellspacing=0 cellpadding=1>
	<tr><th colspan="2">&nbsp;</th></tr>
	<tr>
		<td>导入课程数据:&nbsp;<input type="file" name="excel_file" id="excel_file" /></td>
		<td><input type="submit" value="导入" /></td>
	</tr>
</table>
</form>
-->

<form action="ClassServlet" method="post" enctype="multipart/form-data" onsubmit="return submitfrm(this);">
<input type="hidden" name="method" value="import_from_excel" />
<table cellspacing=0 cellpadding=1>
	<tr><th colspan="2">&nbsp;</th></tr>
	<tr>
		<td>导入班级数据:&nbsp;<input type="file" name="excel_file" id="excel_file" /></td>
		<td><input type="submit" value="导入" class="form_btn"/></td>
	</tr>
</table>
</form>

<form action="TeacherServlet" method="post" enctype="multipart/form-data" onsubmit="return submitfrm(this);">
<input type="hidden" name="method" value="import_from_excel" />
<table cellspacing=0 cellpadding=1>
	<tr><th colspan="2">&nbsp;</th></tr>
	<tr>
		<td>导入教师数据:&nbsp;<input type="file" name="excel_file" id="excel_file" /></td>
		<td><input type="submit" value="导入" class="form_btn"/></td>
	</tr>
</table>
</form>

<form action="ProfessionServlet" method="post" enctype="multipart/form-data" onsubmit="return submitfrm(this);">
<input type="hidden" name="method" value="import_zyl" />
<table cellspacing=0 cellpadding=1>
	<tr><th colspan="2">&nbsp;</th></tr>
	<tr>
		<td>导专业类数据:&nbsp;<input type="file" name="excel_file" id="excel_file" /></td>
		<td><input type="submit" value="导入" class="form_btn"/></td>
	</tr>
</table>
</form>

<form action="ProfessionServlet" method="post" enctype="multipart/form-data" onsubmit="return submitfrm(this);">
<input type="hidden" name="method" value="import_zy" />
<table cellspacing=0 cellpadding=1>
	<tr><th colspan="2">&nbsp;</th></tr>
	<tr>
		<td>导入专业数据:&nbsp;<input type="file" name="excel_file" id="excel_file" /></td>
		<td><input type="submit" value="导入" class="form_btn"/></td>
	</tr>
</table>
</form>

<form action="ProfessionServlet" method="post" enctype="multipart/form-data" onsubmit="return submitfrm(this);">
<input type="hidden" name="method" value="import_whcd" />
<table cellspacing=0 cellpadding=1>
	<tr><th colspan="2">&nbsp;</th></tr>
	<tr>
		<td>导入文化程度:&nbsp;<input type="file" name="excel_file" id="excel_file" /></td>
		<td><input type="submit" value="导入" class="form_btn"/></td>
	</tr>
</table>
</form>

<form action="ProfessionServlet" method="post" enctype="multipart/form-data" onsubmit="return submitfrm(this);">
<input type="hidden" name="method" value="import_zyzc" />
<table cellspacing=0 cellpadding=1>
	<tr><th colspan="2">&nbsp;</th></tr>
	<tr>
		<td>导入职务名称:&nbsp;<input type="file" name="excel_file" id="excel_file" /></td>
		<td><input type="submit" value="导入" class="form_btn"/></td>
	</tr>
</table>
</form>


</div>

<div class="ps_foot">
<strong>注：</strong><br/>
只能上传03版excel文件(后缀名为.xls)<br/>
</div>	

</body>
</html>