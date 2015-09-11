<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%	com.ltms.model.Admin admin = (com.ltms.model.Admin)request.getSession().getAttribute("admin");%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<title>系统管理 - 实验室教学管理平台</title>
</head>
<frameset border=0 frameSpacing=0 rows="127, *" frameBorder="yes">
	<frame name=header src="header.jsp" frameBorder=1 noResize scrolling=no />
	<frameset cols="170, *">
		<frame name=nav src="nav.jsp" frameBorder=1 noResize scrolling=no style="border-right:1px solid #ccc;"/>
		<%	if(admin.getRole() == 2){ %>
		<frame name=content src="ScheduleServlet?method=list" frameBorder=1 noResize scrolling=yes />
		<%	}else if(admin.getRole() == 3){ %>
		<frame name=content src="LaboratoryServlet?method=list&departmentID=<%= admin.getDepartmentID()%>" frameBorder=1 noResize scrolling=yes />
		<%	}else if(admin.getRole() == 4 || admin.getRole() == 5){ %>
		<frame name=content src="blank.html" frameBorder=1 noResize scrolling=yes />
		<%	} else{ %>
		<frame name=content src="blank.html" frameBorder=1 noResize scrolling=yes />
		<%	} %>
	</frameset>
</frameset>
<noframes></noframes>
</html>

