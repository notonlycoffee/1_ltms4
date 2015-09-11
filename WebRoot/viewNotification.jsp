<%@ page language="java" pageEncoding="utf-8" contentType="text/html; charset=utf-8" %>
<%@ page import="java.util.*, java.sql.*,com.ltms.model.*, com.ltms.dao.*" %>
<%@ include file="global.jsp" %>
<%  Notification notification = (Notification)request.getAttribute("notification");
	if(notification == null){%>
	<span>找不到该篇文章!</span>&nbsp;&nbsp;<a href="index.jsp" >返回首页</a>
<%
	return;
	}
	String location = "通知公告";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title><%= location + " - " + site_name%></title>
<link href="css/base.css" rel="stylesheet" type="text/css" />
<script src="js/jquery.min.js"></script>
<script src="js/jquery.easing.min.js"></script>
<script src="js/base.js"></script>
</head>
<body>
<div class="contain">
	<%@ include file="header.jsp" %>
<!-- ◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆-->   
	<div class="stm_content">
		<div class="content">
			<p class="empty">&nbsp;</p>
			<div class="content_position">
				<p class="titleName">通知公告</p>
				<p class="position_bg"><a href="index.jsp">首页</a><span>></span><a href="NotificationServlet?method=webList"><%= location%></a><span>></span><span>查看通知公告</span></p>
			</div>
			<div class="inform_content" id="inform_content_ltms">
				<h1 style="text-align: center;"><%= notification.getTitle()%></h1>
				<p style="text-align: center;">发布者：&nbsp;<%= notification.getAuthor()%>&nbsp;&nbsp;发布时间：&nbsp;<%= notification.getDate()%></p>
				<div class="inform_line"></div>
				<div class="inform_detail">
				<%= notification.getContent() %>
				</div>
			</div>
			
			<%@ include file="footer.jsp" %>
		</div>
	</div>
</div>
</body>
</html>
