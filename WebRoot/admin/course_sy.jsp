<%@ page language="java" import="java.util.*, com.ltms.model.*, java.sql.*,com.ltms.dao.CourseDAO,com.ltms.dao.DepartmentDAO" pageEncoding="utf-8"%>
<%  
	int role = ((Admin)request.getSession().getAttribute("admin")).getRole();
	int departmentID = ((Admin)request.getSession().getAttribute("admin")).getDepartmentID();
	List<Course_sy> courseList = (ArrayList<Course_sy>)request.getAttribute("courseList");	
	if(courseList == null){
		request.getRequestDispatcher("CourseServlet?method=list_syke").forward(request, response);
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
<div id="position">您现在所在页面：实验室管理&nbsp;&gt;&gt;&nbsp;<a href="CourseServlet?method=list_syke" target="content">实验课程管理</a></div>
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
 		<input type="hidden" name="method" value="search_syke" />
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
	<th>操作</th>
</tr>
<% for(Course_sy course_sy : courseList){%>
<tr class="exTr">
	<td class="showTD" style="width:100px;"><%= course_sy.getId()%></td>
    <td class="showTD"><%= course_sy.getName()%></td>
	<td>
		<input type="button" value="填写专业" onClick="javascript:window.location.href='editsykcgl.jsp?id=<%= course_sy.getId() %>&course_name=<%= course_sy.getName()%>'" class="form_btn" style="width: 100px;"/>
	</td>
</tr>
<% }%>
</table>

<!-- 分页--> 	
<% String baseUrl = "CourseServlet?method=list"; %>
<%@ include file="page.jsp" %>
</div>
注: 1.需要任课老师对开设的实验填写完毕后这里才能显示完整的实验课程，该操作最好最后完成
</body>
</html>