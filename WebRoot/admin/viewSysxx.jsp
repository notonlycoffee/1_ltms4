<%@ page language="java" import="java.util.*, com.ltms.model.*, java.sql.*,com.ltms.dao.*" pageEncoding="utf-8"%>
<%
	int role = ((Admin)request.getSession().getAttribute("admin")).getRole();
	int departmentID = ((Admin)request.getSession().getAttribute("admin")).getDepartmentID();
	int id = Integer.parseInt(request.getParameter("id"));
	String currenTerm = (String) request.getSession().getAttribute("currenTerm");
	Laboratory laboratory = LaboratoryDAO.load(id);
	Sysxx sysxx = LaboratoryDAO.load_sysxxByTeam(id,currenTerm);
	String basepath = request.getContextPath();
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
<div id="position">您现在所在页面：实验室管理&nbsp;&gt;&gt;&nbsp;实验室情况</div>
<div class="detailInfoContent">
<%@ include file="showMsg.jsp" %>
<form action="#" method="post"  enctype="multipart/form-data" onsubmit="return submitfrm(this);" >
<input type="hidden" name="method" value="#" />
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
  	<!--  tr>
  		<td class="TD_Left" style="width:150px;">实验室代码</td>
  		<td class="TD_Right" style="width:450px;"><%=sysxx.getSysdm() %></td>
  	</tr-->
  	<tr>
  		<td class="TD_Left">实验室类型</td>
  		<td class="TD_Right">
			<% if(sysxx.getSyslx() == 1){%> 科学<%} %>
			<% if(sysxx.getSyslx() == 2){%> 教研<%} %>
			<% if(sysxx.getSyslx() == 3){%> 其他<%}%>
			<% if((sysxx.getSyslx() != 1) &&(sysxx.getSyslx() != 2) && (sysxx.getSyslx() != 3)){%> 未选<%}%>
		</td>
	</tr>
	<tr>
  		<td class="TD_Left">实验室类别</td>
  		<td class="TD_Right">
				<% if(sysxx.getSyslb() == 1){%> 国家级实验教学示范中心<%} %>
				<% if(sysxx.getSyslb() == 2){%> 省级实验教学示范中心<%} %>
				<% if(sysxx.getSyslb() == 3){%> 按平台建设的校   院（系）实验室<%} %>
				<% if(sysxx.getSyslb() == 4){%> 其他类型实验室<%}%>
				<% if((sysxx.getSyslb() != 1) && (sysxx.getSyslb() != 2) && (sysxx.getSyslb() != 3) && (sysxx.getSyslb() != 4)){%> 未选<%}%>
		</td>
	</tr>
	<tr>
  		<td class="TD_Left">建立年份</td>
  		<td class="TD_Right" style="width:450px;"><% if(sysxx.getJlnf() == null){ %>未填<%}else{ %><%=sysxx.getJlnf() %><%} %></td>
	</tr>
	<tr>
  		<td class="TD_Left">使用面积<br/>(平方米)</td>
  		<td class="TD_Right" style="width:450px;"><% if(sysxx.getSymj() == null){ %>未填<%}else{ %><%=sysxx.getSymj() %><%} %></td>
	</tr>
	<tr>
  		<td class="TD_Left">所属学科</td>
  		<td class="TD_Right">
  			<% if(sysxx.getSsxk() == null){ %>未填<%}else{ %><%=sysxx.getSsxk() %><%} %>
		</td>
	</tr>
	<tr>
  		<td class="TD_Left">操作</td>
  		<td><input type="button" value="返回" onclick="javascript:history.go(-1)" class="form_btn" /></td>
  	</tr>
</table>  
</form>
</div>
</body>
</html>