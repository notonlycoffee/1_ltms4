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
  		<td class="TD_Left">实验室名称</td>
  		<td class="TD_Right">下拉列表</td>
  	</tr>
  	<tr>
  		<td class="TD_Left">实验名称</td>
  		<td class="TD_Right">下拉列表</td>
  	</tr>
  	<tr>
  		<td class="TD_Left">课程或项目</td>
  		<td class="TD_Right">下拉列表</td>
  	</tr>
  	<tr>
  		<td class="TD_Left" style="width:150px;">实验者专业</td>
  		<td class="TD_Right" style="width:450px;">下拉列表</td>
  	</tr>
  	<tr>
  		<td class="TD_Left">实验类别</td>
  		<td class="TD_Right">下拉列表，已有但可选</td>
	</tr>
	<tr>
  		<td class="TD_Left">实验要求</td>
  		<td class="TD_Right">下拉列表</td>
	</tr>
	<tr>
  		<td class="TD_Left">实验者</td>
  		<td class="TD_Right">下拉列表</td>
	</tr>
	<tr>
  		<td class="TD_Left">实验情况</td>
  		<td class="TD_Right">
  			每组人数<input type="text" name="" style="width: 50px"/>
  			实验学时<input type="text" name="" style="width: 50px"/>
		</td>
	</tr>
	<tr>
  		<td class="TD_Left">实验人数</td>
  		<td class="TD_Right">
  			校内<input type="text" name="" style="width: 50px"/><br/>
  			校外<input type="text" name="" style="width: 50px"/>
		</td>
	</tr>
	<tr>
  		<td class="TD_Left">备注</td>
  		<td class="TD_Right">
  			<textarea name="" rows="4" cols="50"></textarea>
		</td>
	</tr>
  	<tr>
  		<td class="TD_Left">操作</td>
  		<td class="TD_Right"><input type="submit" value="修改" class="form_btn"/>
  		<INPUT type="button" value="返回实验室列表 " onClick="javascript:window.location.href='LaboratoryServlet?method=list&departmentID=<%= departmentID%>'" class="form_btn2"></td>
  	</tr>
</table>  
</form>
</div>
</body>
</html>