<%@ page language="java" import="java.util.*, com.ltms.util.StringUtil, com.ltms.model.*, java.sql.*,com.ltms.dao.*" pageEncoding="utf-8"%>
<%	
	List<Notification> notificationList = (ArrayList<Notification>)request.getAttribute("notificationList");	
	if(notificationList == null){
		request.getRequestDispatcher("NotificationServlet?method=list").forward(request, response);
		return;
	}
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link rel="stylesheet" type="text/css" href="css/global.css" />
<script src="js/jquery.easing.min.js"></script>
<script src="js/base.js"></script>
</head>
<body>
<div id="position">您现在所在页面：信息展示&nbsp;&gt;&gt;&nbsp;<a href="NotificationServlet?method=list" target="content">通知公告</a>
<span id="addInfo"><a href="addNotification.jsp">添加通知公告</a></span></div>
<div id="content" style="text-align:left">
<script type="text/javascript">
String.prototype.trim = function(){ return this.replace(/(^\s*)|(\s*$)/g, "");}
function ShowErrMsg(Info)
{
	$("#showMsg").html(Info);
}
function submitfrm(frm)
{
	if(frm.keyword && frm.keyword.value.trim() == ""){
		ShowErrMsg("请输入搜索关键字");
		frm.keyword.focus();
		return false;
	}
	return true;
}
</script>
<%@ include file="showMsg.jsp" %>
<form action="NotificationServlet" method="post" onsubmit="return submitfrm(this);" >
<input type="hidden" name="method" value="search" />
<table cellspacing=0 cellpadding=1 >
  	<tr>
  		<th>关键字</th>
  		<th>操作</th>
  	</tr>
    <tr>
   		<td><input type="text" name="keyword" /></td>
    	<td><input type="submit" value="查询" class="form_btn"></td>
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
  		<th>标题</th>
  		<th>发布者</th>
  		<th>发布时间</th>
  		<th>操作</th>
  	</tr>
    <% for(Notification notification : notificationList){%>
   	<tr class="exTr">
   	    <td width=400 style="text-align:left;"><a href="editNotification.jsp?id=<%= notification.getId() %>"><%= StringUtil.trimString(notification.getTitle(), 25)%></a></td>
   	    <td width=100><%= StringUtil.trimString(notification.getAuthor(), 6)%></td>
   	    <td width=90><%= notification.getDate()%></td>
   	    <td>
   		<input type="button" value="修改" onClick="javascript:window.location.href='editNotification.jsp?id=<%= notification.getId()%>'" class="form_btn"/>
		<input type="button" value="删除" onClick="javascript:linkok('NotificationServlet?method=delete&id=<%= notification.getId()%>')" class="form_btn"/>
		</td>
   	</tr>	
   <% }%>
</table>  
<!-- 分页--> 	
<% String baseUrl = "NotificationServlet?method=list"; %>
<%@ include file="page.jsp" %>
</div>
</body>
</html>