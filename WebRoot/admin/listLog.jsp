<%@ page language="java" import="java.util.*, com.ltms.util.StringUtil, com.ltms.model.*, java.sql.*,com.ltms.dao.*" pageEncoding="utf-8"%>
<%@ page import="com.ltms.util.Tools"%>
<%@page import="java.net.URLEncoder"%>
<%
	List<Log> logList = (ArrayList<Log>)request.getAttribute("logList");	
	if(logList == null){
		request.getRequestDispatcher("LogServlet?method=list").forward(request, response);
		return;
	}
	int sign = Integer.parseInt((String)request.getAttribute("sign"));
	String search_name = (String)request.getAttribute("name");
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
<div id="position">您现在所在页面：系统管理&nbsp;&gt;&gt;&nbsp;<a href="LogServlet?method=list" target="content">系统日志</a></div>
<div id="content" style="text-align:left">
<script type="text/javascript">
String.prototype.trim = function(){ return this.replace(/(^\s*)|(\s*$)/g, "");}
function ShowErrMsg(Info)
{
	$("#showMsg").html(Info);
}
</script>
<%@ include file="showMsg.jsp" %>
<script type="text/javascript">
    function linkok(url){
    	question = confirm("是否确认删除？");
    	if (question){
    		window.location.href = url;
    	}
    }
</script>

<!-- 搜索 -->
<form action="LogServlet" method="post" >
<input type="hidden" name="method" value="search_log" />
<table cellspacing=0 cellpadding=1 >
	<tr>
		<th>姓名</th>
		<th>操作</th>
	</tr>
	<tr>
		<td><input type="text" name="name" /></td>
		<td><input type="submit" value="查询" class="form_btn"/></td>
	</tr>
</table>
</form>

<table border=1 cellspacing=0 cellpadding=0 >
  	<tr>
  		<th>操作者</th>
  		<th>事件</th>
  		<th>时间</th>
  	</tr>
    <%for(Log log : logList){%>
   	<tr class="exTr">
		<td width=90><%= log.getOperator()%></td>
   	    <td width=500 style="text-align:left;"><%= log.getEvent()%></td>
   	    <td width=150><%= Tools.formatDate(log.getDadatetime())%></td>
   	</tr>	
   <% }%>
</table>  
<!-- 分页--> 	
<% 
	String baseUrl = "";
	if(sign == 1){
		baseUrl = "LogServlet?method=list&state_sign=1&name=" + search_name; 
	}else{
		baseUrl = "LogServlet?method=list";
	}
%>
<%@ include file="page.jsp" %>
</div>
</body>
</html>