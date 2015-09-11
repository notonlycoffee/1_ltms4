<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
String error = (String)session.getAttribute("error");
%>		
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
</head>
<body>
<font color="red"><%= error%>，3秒后返回上一个页面</font>
<br />
<SCRIPT language=javascript>
function go()
{
	window.history.go(-1);
}
setTimeout("go()",3000);
</SCRIPT>
<a href="javascript:window.parent.location = 'index.jsp'">返回首页</a>
</body>
</html>
