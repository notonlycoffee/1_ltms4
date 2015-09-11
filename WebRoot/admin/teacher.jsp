<%@ page language="java" import="java.util.*, com.ltms.model.*, java.sql.*,com.ltms.dao.*" pageEncoding="utf-8"%>
<%  
	int role = ((Admin)request.getSession().getAttribute("admin")).getRole();
	List<Teacher> teacherList = (ArrayList<Teacher>)request.getAttribute("teacherList");	
	if(teacherList == null){
		request.getRequestDispatcher("TeacherServlet?method=list").forward(request, response);
		return;
	}
	List<Unit> unitList = UnitDAO.list();
	Map<Integer, String> nameMap = UnitDAO.getNameMap();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link rel="stylesheet" type="text/css" href="css/global.css" />
<script src="js/jquery.easing.min.js"></script>
<script src="js/base.js"></script>
</head>
<body>
<div id="position">您现在所在页面：实验室管理&nbsp;&gt;&gt;&nbsp;<a href="TeacherServlet?method=list" target="content">教师管理</a></div>
<div id="content" style="text-align:left">
<script type="text/javascript">
String.prototype.trim = function(){ return this.replace(/(^\s*)|(\s*$)/g, "");}
function ShowErrMsg(Info)
{
	$("#showMsg").html(Info);
}
function submitfrm(frm)
{
	if(frm.name.value.trim()=="")
	{
		 ShowErrMsg("教师名不能为空，请输入");
		 frm.name.focus();
		 return false;
	}
	return true;
}
</script>
<%@ include file="showMsg.jsp" %>
<!-- 搜索 -->
<form action="TeacherServlet" method="post" >
<input type="hidden" name="method" value="search" />
<input type="hidden" name="returnPage" value="teacher" />
<table cellspacing=0 cellpadding=1 >
	<tr>
		<th>单位</th>
		<th>姓名</th>
		<th>操作</th>
	</tr>
	<tr>
		<td>
 		<select name="unitID" id="unitID">
 		<option value="0"></option>
   		<% for(Iterator<Unit> it = unitList.iterator();it.hasNext();){
   			Unit uu = it.next();%> 
   		 	<option value="<%= uu.getId()%>"><%= uu.getName()%></option>
   		<%}%>
   		</select>
 		</td>
		<td><input type="text" name="name" /></td>
		<td><input type="submit" value="查询" class="form_btn"/></td>
	</tr>
</table>
</form>

<form action="TeacherServlet" method="post" >
<input type="hidden" name="method" value="add" />
<table cellspacing=0 cellpadding=1 >
	<tr>
		<th>单位</th>
		<th>姓名</th>
		<th>工号</th>
		<th>操作</th>
	</tr>
	<tr>
		<td>
 		<select name="unitID" id="unitID">
 		<option value="0"></option>
   		<% for(Iterator<Unit> it = unitList.iterator();it.hasNext();){
   			Unit uu = it.next();%> 
   		 	<option value="<%= uu.getId()%>"><%= uu.getName()%></option>
   		<%}%>
   		</select>
 		</td>
		<td><input type="text" name="name" /></td>
		<td><input type="text" name="number" /></td>
		<td><input type="submit" value="添加" class="form_btn"/></td>
	</tr>
</table>
</form>

<script type="text/javascript">
  function linkok(url){
    question = confirm("是否确认删除？");
    if (question){
      window.location.href = url;
    }
  }
</script>

<form action="TeacherServlet" method="post" >
<input type="hidden" name="method" value="update_"/>
<table border=1 cellspacing=0 cellpadding=0 >
	<tr>
		<th>工号</th>
		<th>姓名</th>
		<th style="width:250px;">单位</th>
		<% if(role==0 || role == 1){ %>
		<th>操作</th>
		<% } %>
	</tr>
	<% for(Teacher teacher : teacherList){%>
	<tr class="exTr">
		<td style="width:150px;"><%= teacher.getId()%></td>
	    <td style="width:150px;"><%= teacher.getName()%></td>
	    <td style="width:150px;">
			<select name="unitID" id="unitID">
		 		<option value="0"></option>
		   		<% for(Iterator<Unit> it = unitList.iterator();it.hasNext();){
		   			Unit uu = it.next();%> 
		   		 	<option value="<%= uu.getId()%>" <%= uu.getId() == teacher.getUnitID() ? "selected=\"selected\"" : ""%>><%= uu.getName()%></option>
		   		<%}%>
	   		</select>
		</td>
		<td>
		
		<input type="button" value="删除" onclick="javascript:linkok('TeacherServlet?method=delete&id=<%= teacher.getId()%>&unitID=<%= teacher.getUnitID()%>')" class="form_btn"/>
		<input type="hidden" name="id" value="<%= teacher.getId()%>" />
		<input type="submit" value="修改" class="form_btn"/>
		<input type="button" value="重置密码" onclick="javascript:window.location.href='TeacherServlet?method=reset_password&id=<%= teacher.getId()%>&unitID=<%= teacher.getUnitID()%>'" class="form_btn2"/>
		</td>
	</tr>
	<% }%>
</table>
</form>
<!-- 分页--> 	
<% String baseUrl = "TeacherServlet?method=list"; %>
<%@ include file="page.jsp" %>
</div>	

<div class="ps_foot">
<strong>注：</strong><br/>
默认密码为123456
</div>	
</body>
</html>