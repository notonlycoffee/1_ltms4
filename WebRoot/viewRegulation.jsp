<%@ page language="java" pageEncoding="utf-8" contentType="text/html; charset=utf-8" %>
<%@ page import="java.util.*, java.sql.*,com.ltms.model.*, com.ltms.dao.*" %>
<%@ include file="global.jsp" %>
<%	Regulation regulation = (Regulation)request.getAttribute("regulation");
	if(regulation == null){%>
	<span>找不到该篇文章!</span>&nbsp;&nbsp;<a href="index.jsp" >返回首页</a>
<%
	return;
	}
	String location = "管理制度";
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
			<p class="titleName">管理制度</p>
				 <p class="position_bg"><a href="index.jsp">首页</a><span>></span><a href="RegulationServlet?method=webList"><%= location%></a><span>></span>查看管理制度</span></p>
 		</div>
			<div class="inform_content" id="inform_content_ltms">
				<h1><%= regulation.getTitle()%></h1>
				<p>系别：&nbsp;<%= DepartmentDAO.getName(regulation.getDepartmentID())%>&nbsp;&nbsp;实验室：&nbsp;<%= LaboratoryDAO.getName(regulation.getLaboratoryID())%></p>
				<p>发布者：&nbsp;<%= regulation.getAuthor()%>&nbsp;&nbsp;发布时间：&nbsp;<%= regulation.getDate()%></p>
				<div class="inform_line"></div>
				<div class="inform_detail">
				<%= regulation.getContent() %>
				</div>
			</div>
			<%@ include file="footer.jsp" %>
		</div>
	</div>
</div>
</body>
</html>
