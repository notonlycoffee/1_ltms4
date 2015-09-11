<%@ page language="java" import="java.util.*, com.ltms.model.*, java.sql.*,com.ltms.dao.*" pageEncoding="utf-8"%>
<%
	int role = ((Admin)request.getSession().getAttribute("admin")).getRole();
	int departmentID = ((Admin)request.getSession().getAttribute("admin")).getDepartmentID();
	int id = Integer.parseInt(request.getParameter("id"));
	Laboratory laboratory = LaboratoryDAO.load(id);
	String basepath = request.getContextPath();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link rel="stylesheet" type="text/css" href="css/global.css" />
</head>
<body>
<div id="position">您现在所在页面：实验室管理&nbsp;&gt;&gt;&nbsp;<a href="LaboratoryServlet?method=list&departmentID=<%= departmentID%>" target="content">实验室管理</a>&nbsp;&gt;&gt;&nbsp;查看实验室信息</div>
<div class="detailInfoContent">
<script type="text/javascript">
String.prototype.trim = function(){ return this.replace(/(^\s*)|(\s*$)/g, "");}
function ShowErrMsg(Info)
{
	document.getElementById("showMsg").innerHTML = Info;
}
</script>
<%@ include file="showMsg.jsp" %>

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
  		<td class="TD_Left" style="width:150px;">实验室负责人</td>
  		<td class="TD_Right"  style="width:450px;"><%= laboratory.getAdmin()%></td>
  	</tr>
  		<td class="TD_Left">实验室类型</td>
  		<td class="TD_Right"><%= laboratory.getType()%></td>
	<tr>
  		<td class="TD_Left">开课专业</td>
  		<td class="TD_Right"><%= laboratory.getSpecialty()%></td>
	</tr>
	<tr>
  		<td class="TD_Left">开设课程</td>
  		<td class="TD_Right" style="width:600px;"><%= laboratory.getCourse()%></td>
	</tr>
	<tr>
  		<td class="TD_Left">实验室设备</td>
  		<td class="TD_Right" style="width:600px;"><%= laboratory.getEquipment() == null ? "" : laboratory.getEquipment()%>&nbsp;</td>
	</tr>
	<tr>
  		<td class="TD_Left">实验室照片1</td>
  		<td class="TD_Right"><% if(laboratory.getPic1() != null && !"".equals(laboratory.getPic1())){%><a href="<%= basepath + "/" + laboratory.getPic3()%>" target="_blank"><img src="<%= basepath + "/" + laboratory.getPic1()%>" class="la_pic_ltms" /></a><%}else{%>暂无照片<%}%></td>
	</tr>
	<tr>
  		<td class="TD_Left">实验室照片2</td>
  		<td class="TD_Right"><% if(laboratory.getPic2() != null && !"".equals(laboratory.getPic2())){%><a href="<%= basepath + "/" + laboratory.getPic3()%>" target="_blank"><img src="<%= basepath + "/" + laboratory.getPic2()%>" class="la_pic_ltms" /></a><%}else{%>暂无照片<%}%></td>
	</tr>
	<tr>
  		<td class="TD_Left">实验室照片3</td>
  		<td class="TD_Right"><% if(laboratory.getPic3() != null && !"".equals(laboratory.getPic3())){%><a href="<%= basepath + "/" + laboratory.getPic3()%>" target="_blank"><img src="<%= basepath + "/" + laboratory.getPic3()%>" class="la_pic_ltms" /></a><%}else{%>暂无照片<%}%></td>
	</tr>
  	<tr>
  		<td class="TD_Left">操作</td>
  		<td><input type="button" value="返回" onclick="javascript:history.go(-1)" class="form_btn" /></td>
  	</tr>
</table>  
</div>
</body>
</html>