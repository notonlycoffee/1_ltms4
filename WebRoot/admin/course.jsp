<%@ page language="java" import="java.util.*, com.ltms.model.*, java.sql.*,com.ltms.dao.CourseDAO,com.ltms.dao.DepartmentDAO" pageEncoding="utf-8"%>
<%  
	int role = ((Admin)request.getSession().getAttribute("admin")).getRole();
	int departmentID = ((Admin)request.getSession().getAttribute("admin")).getDepartmentID();
	List<Course> courseList = (ArrayList<Course>)request.getAttribute("courseList");	
	if(courseList == null){
		request.getRequestDispatcher("CourseServlet?method=list").forward(request, response);
		return;
	}
	List<Department> departmentList = (ArrayList<Department>)session.getAttribute("departmentList");	
	if(departmentList == null){
		departmentList = DepartmentDAO.list();
		session.setAttribute("departmentList", departmentList);
	}
	Map<Integer, String> nameMap = (HashMap<Integer, String>)session.getAttribute("nameMap");	
	if(nameMap == null){
		nameMap = DepartmentDAO.getNameMap();
		session.setAttribute("nameMap", nameMap);
	}
	String courseType[] = {"必修课", "限选课", "任选课", "公任课", "公限课"};
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link rel="stylesheet" type="text/css" href="css/global.css" />
<script src="js/jquery.min.js"></script>
<script src="js/jquery.easing.min.js"></script>
<script src="js/base.js"></script>
</head>
<body>
<div id="position">您现在所在页面：实验室管理&nbsp;&gt;&gt;&nbsp;<a href="CourseServlet?method=list" target="content">课程管理</a></div>
<div id="content" style="text-align:left">
<script type="text/javascript">
String.prototype.trim = function(){ return this.replace(/(^\s*)|(\s*$)/g, "");}
function ShowErrMsg(Info)
{
	$("#showMsg").html(Info);
}
function submitfrm(frm)
{
	if(frm.id && frm.id.value.trim()=="")
	{
		 ShowErrMsg("课程代码不能为空，请输入");
		 frm.id.focus();
		 return false;
	}
	if(frm.name && frm.name.value.trim()=="")
	{
		 ShowErrMsg("课程名称不能为空，请输入");
		 frm.name.focus();
		 return false;
	}
	return true;
}
</script>
<%@ include file="showMsg.jsp" %>
<!-- 搜索 -->
<form action="CourseServlet" method="post" onsubmit="return submitfrm(this);" >
 		<input type="hidden" name="method" value="search" />
<table cellspacing=0 cellpadding=1>
	<tr>
		<th>课程名称</th>
		<th>操作</th>
	</tr>
	<tr>
		<td><input type="text" name="name" /></td>
		<td><input type="submit" value="查询" class="form_btn"/></td>
  </tr>
</table>
</form>
<%	if(role != 2){	%>
<!-- 添加 -->
<form action="CourseServlet" method="post" onsubmit="return submitfrm(this);" >
<input type="hidden" name="method" value="add" />
<table cellspacing=0 cellpadding=1>
	<tr>
		<th>课程代码</th>
		<th>课程名称</th>
		<th>开课单位</th>
		<th>课程性质</th>
		<th>操作</th>
	</tr>
	<tr>
		<td><input type="text" name="id" /></td>
 		<td><input type="text" name="name" /></td>
 		<td>
		<% if(role == 0){ %>
			<select name="departmentID" id="departmentID">
		<% for(Iterator<Department> it = departmentList.iterator();it.hasNext();){
		   	Department d = it.next();
		%> 
			<option value="<%= d.getId()%>"><%= d.getName()%></option>
		<% }%>
			</select>
		<% }else{%>
		   	<%= nameMap.get(departmentID)%>
		   	<input type="hidden" name="departmentID" value="<%= departmentID%>" />
		<% }%>
		</td>
		<td>
		<select name="type" id="type">
		<% for(int i=0; i<5; i++){%> 
			<option value="<%= courseType[i]%>"><%= courseType[i]%></option>
		<% }%>
		</select>
		</td>
		<td><input type="submit" value="添加" class="form_btn"/></td>
	</tr>
</table>
</form>
<%	}	%>

<script type="text/javascript">
   function linkok(url){
   	question = confirm("是否确认删除？");
   	if (question){
   		window.location.href = url;
   	}
   }
</script>
<table border=1 cellspacing=0 cellpadding=0 >
<tr>
	<th>课程代码</th>
	<th>课程名称</th>
	<th>开课单位</th>
	<th>课程性质</th>
	<th>操作</th>
</tr>
<% for(Course course : courseList){%>
<tr class="exTr">
	<form action="CourseServlet" method="post" onsubmit="return submitfrm(this);" >
	<input type="hidden" name="method" value="update" />
	<input type="hidden" name="id" value="<%= course.getId()%>" />
	<td class="showTD" style="width:100px;"><%= course.getId()%></td>
    <td class="showTD"><input type="text" name="name" value="<%= course.getName()%>" style="width:250px;"/></td>
    <td class="showTD" style="width:170px;"><%= nameMap.get(course.getDepartmentID())%></td>
	<td class="showTD" style="width:60px;">
	<select name="type" id="type">
	<% for(int i=0; i<5; i++){%> 
		<option value="<%= courseType[i]%>" <%= courseType[i].equals(course.getType()) ? "selected=\"selected\"" : ""%>><%= courseType[i]%></option>
	<% }%>
	</select>
	</td>
	<td>
	<input type="submit" value="修改" class="form_btn"/>
	<input type="button" value="删除" onClick="javascript:linkok('CourseServlet?method=delete&id=<%= course.getId()%>')" class="form_btn"/>
	</td>
	</form>
</tr>
<% }%>
</table>

<!-- 分页--> 	
<% String baseUrl = "CourseServlet?method=list"; %>
<%@ include file="page.jsp" %>
</div>
</body>
</html>