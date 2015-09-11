<%@ page language="java" pageEncoding="utf-8" %>
<%@ page import="java.util.*, com.ltms.model.*, com.ltms.dao.*, java.sql.*" %>
<%@ include file="global.jsp" %>
<%  
	ArrayList<Regulation> regulationList = (ArrayList<Regulation>)request.getAttribute("regulationList");
	String laboratoryName = (String)request.getAttribute("laboratoryName");
	String location = "管理制度";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link href="css/base.css" rel="stylesheet" type="text/css" />
<link href="css/general.css" rel="stylesheet" type="text/css" />
<title><%= location + " - " + site_name%></title>
</head>
<body>
<div class="contain">
<%@ include file="header.jsp" %>
<!-- ◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆-->   
<div class="stm_content">
	<div class="content">
		<p class="empty">&nbsp;</p>
		<div class="content_position">
			<p class="position_bg">当前位置：<a href="index.jsp">首页</a>&nbsp;&gt;&gt;&nbsp;<a href="RegulationServlet?method=webList"><%= location%></a></p>
		</div>
		
		<div class="inform_content">
			<span class="general_name">[实验室管理制度]</span>
        	<div class="inform_line ge_width">
        </div>
        <div class="general_list" id="laboratory_list_ltms">
			<ul>
				<%for(Regulation n : regulationList){%>
	           	<li><p><a href="RegulationServlet?method=webList_obl&laboratoryID=<%= n.getLaboratoryID()%>" style="color:#0099CB">[<%= laboratoryName%>]</a><a href="RegulationServlet?method=view&id=<%= n.getId() %>"><%= n.getTitle()%></a></p><span><%= n.getDate()%></span></li>
				<%}%>
			</ul>
		</div>
		</div>
<!-- ◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆--> 
        <%@ include file="footer.jsp" %>
		</div>
	</div>
</div>
</body>
</html>
