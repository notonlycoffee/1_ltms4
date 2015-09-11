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
<% if(role != 0 && departmentID != laboratory.getDepartmentID()){%>
<div style="padding:10px;">
<font style="color:red;font-size:14px;">您不是该系教务员, 无权限修改该系实验室信息!&nbsp;&nbsp;3秒后返回上一个页面</font><br/>
<script type="text/javascript">
	function go(){
		window.history.go(-1);
	}
setTimeout("go()",3000);
</script>
<span id="error_return"><a href="javascript:window.history.go(-1)">立即返回</a>&nbsp;&nbsp;<a href="javascript:window.parent.location = 'index.jsp'">返回首页</a></span>
</div>
<% return; }%>
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
<form action="LaboratoryServlet" method="post"  enctype="multipart/form-data" onsubmit="return submitfrm(this);" >
<input type="hidden" name="id" value="<%=laboratory.getId()%>" />
<input type="hidden" name="method" value="update" />
<input type="hidden" name="old_pic1" value="<%= laboratory.getPic1() == null ? "" : laboratory.getPic1()%>" />
<input type="hidden" name="old_pic2" value="<%= laboratory.getPic2() == null ? "" : laboratory.getPic2()%>" />
<input type="hidden" name="old_pic3" value="<%= laboratory.getPic3() == null ? "" : laboratory.getPic3()%>" />
<table border=1 cellspacing=0 cellpadding=0 style="border:none;border-left:1px solid #CCCCCC;border-bottom:1px solid #CCCCCC;">
  	<tr>
  		<td class="TD_Left">系别</td>
  		<td class="TD_Right"><%= DepartmentDAO.getName(laboratory.getDepartmentID())%></td>
  	</tr>
  	<tr>
  		<td class="TD_Left">实验室名称</td>
  		<td class="TD_Right"><input type="text" name="sysmc_" value="<%= laboratory.getName()%>" /></td>
  	</tr>
  	<tr>
  		<td class="TD_Left">实验室地址</td>
  		<td class="TD_Right"><input type="text" name="sysdz_" value="<%= laboratory.getAddress()%>"/></td>
  	</tr>
  	<tr>
  		<td class="TD_Left" style="width:150px;">实验室负责人</td>
  		<td class="TD_Right" style="width:450px;"><input type="text" name="admin" value="<%= laboratory.getAdmin()%>"/></td>
  	</tr>
  		<td class="TD_Left">实验室类型</td>
  		<td class="TD_Right"><input type="text" name="type" value="<%= laboratory.getType()%>"/></td>
	<tr>
  		<td class="TD_Left">开课专业</td>
  		<td class="TD_Right"><textarea name="specialty" rows="4" cols="50"><%= laboratory.getSpecialty()%></textarea></td>
	</tr>
	<tr>
  		<td class="TD_Left">开设课程</td>
  		<td class="TD_Right"><textarea name="course" rows="4" cols="50"><%= laboratory.getCourse()%></textarea></td>
	</tr>
	<tr>
  		<td class="TD_Left">实验室设备</td>
  		<td class="TD_Right"><textarea name="equipment" rows="4" cols="50"><%= laboratory.getEquipment() == null ? "" : laboratory.getEquipment()%></textarea></td>
	</tr>
	<tr>
  		<td class="TD_Left">实验室照片1</td>
  		<td class="TD_Right">
  		<% if(laboratory.getPic1() != null && !"".equals(laboratory.getPic1())){%><a href="<%= basepath + "/" + laboratory.getPic1()%>" target="_blank"><img src="<%= basepath + "/" + laboratory.getPic1()%>" class="la_pic_ltms" /></a>
  		<br/>修改图片：<input type="file" name="pic1"/><br/>删除图片：<input type="checkbox" name="del_pic1"/><%}else{%>上传图片：<input type="file" name="pic1"/><%}%>
  		</td>
	</tr>
	<tr>
  		<td class="TD_Left">实验室照片2</td>
  		<td class="TD_Right">
  		<% if(laboratory.getPic2() != null && !"".equals(laboratory.getPic2())){%><a href="<%= basepath + "/" + laboratory.getPic2()%>" target="_blank"><img src="<%= basepath + "/" + laboratory.getPic2()%>" class="la_pic_ltms" /></a>
  		<br/>修改图片：<input type="file" name="pic2"/><br/>删除图片：<input type="checkbox" name="del_pic2"/><%}else{%>上传图片：<input type="file" name="pic2"/><%}%>
  		</td>
	</tr>
	<tr>
  		<td class="TD_Left">实验室照片3</td>
  		<td class="TD_Right">
  		<% if(laboratory.getPic3() != null && !"".equals(laboratory.getPic3())){%><a href="<%= basepath + "/" + laboratory.getPic3()%>" target="_blank"><img src="<%= basepath + "/" + laboratory.getPic3()%>" class="la_pic_ltms" /></a>
  		<br/>修改图片：<input type="file" name="pic3"/><br/>删除图片：<input type="checkbox" name="del_pic3"/><%}else{%>上传图片：<input type="file" name="pic3"/><%}%>
  		</td>
	</tr>
  	<tr>
  		<td class="TD_Left">操作</td>
  		<td class="TD_Right"><input type="submit" value="修改" class="form_btn"/>
  		<% if(role!=5){ %>
  			<INPUT type="button" value="返回实验室列表 " onClick="javascript:window.location.href='LaboratoryServlet?method=list&departmentID=<%= departmentID%>'" class="form_btn2"></td>
  		<%} %>
  	</tr>
</table>  
</form>
</div>
</body>
</html>