<%@ page language="java" import="java.util.*, com.ltms.model.*, java.sql.*,com.ltms.dao.LaboratoryDAO,com.ltms.dao.DepartmentDAO" pageEncoding="utf-8"%>
<%@page import="com.ltms.dao.CourseDAO"%>
<%  
	int role = ((Admin)request.getSession().getAttribute("admin")).getRole();
	int departmentID = ((Admin)request.getSession().getAttribute("admin")).getDepartmentID();
	List<Course_sy> Course_syList = CourseDAO.load_course_sy(departmentID);
	
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link rel="stylesheet" type="text/css" href="css/global.css" />
<script src="http://ajax.googleapis.com/ajax/libs/jquery/1.10.2/jquery.min.js"></script>
<script src="js/jquery.easing.min.js"></script>
<script src="js/base.js"></script>
</head>
<body>
<div id="position">您现在所在页面：实验室管理&nbsp;&gt;&gt;&nbsp;<a href="LaboratoryServlet?method=list&departmentID=<%= departmentID%>" target="content">实验课程管理</a></div>
<div id="content" style="text-align:left">
<script type="text/javascript">
</script>
<%@ include file="showMsg.jsp" %>
<table cellspacing=0 cellpadding=0>
  	<tr>
  		<th>课程代码</th>
  		<th>课程名字</th>
  		<th>操作</th>
  	</tr>
    <% for(Course_sy course_sy : Course_syList){%>
   	<tr class="exTr">
   		<td class="showTD" style="width:20%;text-align:left;padding:0 8px;"><%= course_sy.getId()%></td>
   	    <td class="showTD" style="width:30%;text-align:left;"><%= course_sy.getName()%></td>
   		<td style="width:22%;">
    		<input type="button" value="查看" onclick="javascript:window.location.href='viewSysxx.jsp?course_sy=<%= course_sy %>'" class="form_btn"/>
			<input type="button" value="修改" onclick="javascript:window.location.href='editSysxx.jsp?id=<%= course_sy %>'" class="form_btn"/>
   		</td>
   	</tr>
<% }%>
</table>

</div>

<div class="ps_foot">
<strong>注：</strong><br/>
查看某个系别的实验室请选择系别后点击查询按钮<br/>
</div>	
</body>
</html>