<%@ page language="java" import="java.util.*,java.sql.*, com.ltms.model.Admin" pageEncoding="utf-8"%>
<%
	String basepath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath();
	int role = ((Admin)session.getAttribute("admin")).getRole();
	String loginName = (String)request.getSession().getAttribute("admin_name");
	String _currenTerm_ = (String)request.getSession().getAttribute("currenTerm");
	String role_name = "";
	if(role == 1)role_name = "教务员";
	else if(role == 3)role_name = "学院主管领导";
	else if(role == 4)role_name = "实验室主任";
	else if(role == 5)role_name = "实验室管理员";
	else if(role != 2)role_name = "管理员";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link rel="stylesheet" type="text/css" href="css/global.css" />
<title>实验室教学管理系统</title>
</head>

<body style="background:url(images/banner_bg.png);">
<div id="banner">
	<div id="welcome">
		欢迎您，<%= loginName%><% if(role!=2){ %>(<%=role_name %>)<% } %>&nbsp;<a href="<%= basepath%>/index.jsp" target="new_window">浏览网站</a>&nbsp;<a href="logout.jsp" target="content">安全退出</a><br/>
		<% if(role == 1 || role == 2 || role == 0 || role == 5 || role == 3|| role == 4){ %> 学年学期为:<%=_currenTerm_ %> <%} %>
	</div>
</div>
</body>
</html>

