<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<% String msg = (String)request.getAttribute("msg");%>		
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link rel="stylesheet" type="text/css" href="css/global.css" />
</head>
<body>
<div style="padding:10px;">
<font style="color:red;font-size:14px;"><%= msg%>&nbsp;&nbsp;3秒后返回上一个页面</font><br/>
<script type="text/javascript">
	function go(){
		window.history.go(-1);
	}
setTimeout("go()",3000);
</script>
<span id="error_return"><a href="javascript:window.history.go(-1)">立即返回</a>&nbsp;&nbsp;<a href="javascript:window.parent.location = 'index.jsp'">返回首页</a></span>
</div>
</body>
</html>
