<%@ page language="java" pageEncoding="utf-8" contentType="text/html; charset=utf-8" %>
<%@ page import="java.util.*, java.sql.*,com.ltms.model.*, com.ltms.dao.*" %>
<%@ include file="global.jsp" %>
<%
	String laboratoryName = (String)request.getAttribute("laboratoryName");
	String department = (String)request.getAttribute("department");
	String className = (String)request.getAttribute("class");
	String teacher = (String)request.getAttribute("teacher");
	String courseName = (String)request.getAttribute("courseName");
	Schedule schedule = (Schedule)request.getAttribute("schedule");
	Experiment experiment = (Experiment)request.getAttribute("experiment");
	ExItem exItem = (ExItem)request.getAttribute("exItem");
	String location = "查看实验项目";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title><%= location + " - " + site_name%></title>
<link href="css/base.css" rel="stylesheet" type="text/css" />
<link href="css/general.css" rel="stylesheet" type="text/css" />
</head>
<body>
<div class="contain">
<%@ include file="header.jsp" %>
<!-- ◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆-->   
<div class="stm_content">
	<div class="content">
		<p class="empty">&nbsp;</p>
		<div class="content_position">
			<p class="position_bg"><a href="index.jsp">首页</a>><a href="ExItemServlet?method=webList">实验教学</a><a href="ExItemServlet?method=view&id=<%= exItem.getId()%>&scheduleID=<%= schedule.getId()%>"><%= location%></a></p>
		</div>
		<div class="projects">
		<table>
		<caption>[实验项目信息]</caption>
		<tr>
        	<td class="name">实验项目名称</td>
            <td><%= exItem.getItemName()%></td>
		</tr>
        <tr>
            <td class="name">学年学期</td>
            <td><%= schedule.getTerm()%></td>
          </tr>
		<tr>
            <td class="name">实验课程</td>
            <td><%= courseName%></td>
		</tr>
		<tr>
            <td class="name">班级</td>
            <td><%= className%></td>
		</tr>
		<tr>
            <td class="name">实验地点</td>
        	<td><%= laboratoryName %></td>
		</tr>
		<tr>
            <td class="name">指导教师</td>
            <td><%= teacher %></td>
		</tr>
		<tr>
            <td class="name">实验目的</td>
            <td><%= schedule.getPurpose() %></td>
		</tr>
		<tr>
            <td class="name">备注</td>
            <td><%= exItem.getComment() %></td>
		</tr>
		<tr>
            <td class="name">操作</td>
            <td><input type="button" value="返回实验项目一览" onClick="javascript:history.go(-1)" class="btn" style="width:120px;"></td>
		</tr>
        </table>
		</div>
<!-- ◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆--> 
        <%@ include file="footer.jsp" %>
		</div>
	</div>
</div>
</body>
</html>