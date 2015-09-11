<%@ page language="java" import="java.util.*, com.ltms.model.*, java.sql.*,com.ltms.dao.*" pageEncoding="utf-8"%>
<%
	int role = ((Admin)request.getSession().getAttribute("admin")).getRole();
	int departmentID = ((Admin)request.getSession().getAttribute("admin")).getDepartmentID();
	String id_name = request.getParameter("id");
	String[] message = id_name.split(",");
	int id = Integer.parseInt(message[0]);
	Laboratory laboratory = LaboratoryDAO.load(id);
	String basepath = request.getContextPath();
	String syszr_name = message[1];
	String id_syszr = message[2];
	List<Admin> syszrList = AdminDAO.listSyszr(departmentID);
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link rel="stylesheet" type="text/css" href="css/global.css" />
</head>
<body>
<script type="text/javascript">
String.prototype.trim = function(){ return this.replace(/(^\s*)|(\s*$)/g, "");}
function ShowErrMsg(Info)
{
	document.getElementById("showMsg").innerHTML = Info;
}
function submitfrm(frm)
{
	return true;
}
</script>
<div id="position">您现在所在页面：实验室管理&nbsp;&gt;&gt;&nbsp;<a href="LaboratoryServlet?method=list&departmentID=<%= departmentID%>" target="content">实验室管理</a>&nbsp;&gt;&gt;&nbsp;修改实验室信息</div>
<div class="detailInfoContent">
<%@ include file="showMsg.jsp" %>
<form action="LaboratoryServlet" method="post" onsubmit="return submitfrm(this);" >
<input type="hidden" name="id" value="<%=laboratory.getId()%>" />
<input type="hidden" name="id_department" value="<%=laboratory.getDepartmentID()%>" />
<input type="hidden" name="method" value="updateSysfp" />
<!--  input type="hidden" name="id_syszr" value="<%=  id_syszr%>" /-->
<% if(syszr_name.equals("null")){%>
	<input type="hidden" name="save_update" value="save" />
<%} else{%>
	<input type="hidden" name="save_update" value="update" />
<%}%>
<table border=1 cellspacing=0 cellpadding=0 style="border:none;border-left:1px solid #CCCCCC;border-bottom:1px solid #CCCCCC;">
  	<tr>
  		<td class="TD_Left">系别</td>
  		<td class="TD_Right"><%= DepartmentDAO.getName(laboratory.getDepartmentID())%></td>
  	</tr>
  	<tr>
  		<td class="TD_Left">实验室名称</td>
  		<td class="TD_Right"><%= laboratory.getName()%></td>
  	</tr>
  	<tr>
  		<td class="TD_Left">实验室地址</td>
  		<td class="TD_Right"><%= laboratory.getAddress()%></td>
  	</tr>
  	<tr>
  		<td class="TD_Left" style="width:150px;">实验室主任</td>
  		<td class="TD_Right" style="width:450px;">
  			<select name="id_syszr" style="width:200px;height:23px;margin:0;">
			<%	for(Admin a : syszrList){%>
			<option value="<%= a.getId()%>"><%= a.getName()%></option>
			<%	}%>
			</select>
  		</td>
  	</tr>
  	<tr>
  		<td class="TD_Left">操作</td>
  		<td class="TD_Right"><input type="submit" value="修改" class="form_btn"/>
  		<INPUT type="button" value="返回实验室列表 " onClick="javascript:window.location.href='LaboratoryServlet?method=list&lc=sysfp&departmentID=<%= departmentID%>'" class="form_btn2"></td>
  	</tr>
</table>  
</form>
</div>
</body>
</html>