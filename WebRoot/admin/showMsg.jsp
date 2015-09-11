<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<div id="showMsg">
	欢迎使用...
</div>
<%String msg = (String)request.getAttribute("msg");
if(msg != null){%>
<script type="text/javascript">
	var mes =document.getElementById("showMsg").value = '<%= msg%>';
	 alert(msg);
</script>
<%}%>