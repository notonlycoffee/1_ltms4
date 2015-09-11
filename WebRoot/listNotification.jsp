<%@ page language="java" pageEncoding="utf-8" %>
<%@ page import="java.util.*, com.ltms.model.*, com.ltms.dao.*, java.sql.*" %>
<%@ include file="global.jsp" %>
<%  
	ArrayList<Notification> notificationList = (ArrayList<Notification>)request.getAttribute("notificationList");
	if(notificationList == null){
		request.getRequestDispatcher("NotificationServlet?method=webList").forward(request, response);
	}
	String location = "通知公告";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link rel="stylesheet" href="css/listNotificationStyle.css" type="text/css">
<title><%= location + " - " + site_name%></title>
</head>

<body>
	<!--头部开始-->
	<%@ include file="header.jsp" %>
    <!--头部结束-->
    <!--内容开始-->
    <div class="clear"></div>    
    <div id="content">
        <div class="main">
    		<div class="title">
            	<p class="name">通知公告&nbsp;&nbsp&nbsp;news</p>
                <p class="path"><a href="index.jsp">首页</a><span class="sign">></span><a href="NotificationServlet?method=webList">通知公告</a></p>
        	</div>
        	<p class="title2">通知公告</p>
            <img src="imgs/table_bor.png" />
            <table>
            	<%for(Notification n : notificationList){%>
            	<tr>
                	<td class="new_title"><a href="NotificationServlet?method=view&id=<%= n.getId() %>"><%= n.getTitle()%></a></td>
                    <td><%= n.getDate()%></td>
                </tr>
                <%}%>
            </table>
            <img src="imgs/table_bor.png" />
            <div class="page" id="sort_page_ltms">
        	<!-- 分页--> 
        	<% String baseUrl = "NotificationServlet?method=webList"; %>
			<%@ include file="page.jsp" %>
		</div>
        </div>
    </div>
    <!--内容结束-->
    <!--页脚开始-->
    <%@ include file="footer.jsp" %>
    <!--页脚结束-->
</body>
</html>
