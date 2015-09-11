<%@ page language="java" import="java.util.*, com.ltms.model.*, java.sql.*,com.ltms.dao.LinkDAO" pageEncoding="utf-8"%>
<%@ include file="global.jsp" %>
<% 
	List<Link> linkList = LinkDAO.list();	
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
<div id="position">您现在所在页面：信息展示&nbsp;&gt;&gt;&nbsp;<a class="child" href="link.jsp" target="content">友情链接</a></div>
<div id="content" style="text-align:left">
<script type="text/javascript">
String.prototype.trim = function(){ return this.replace(/(^\s*)|(\s*$)/g, "");}
function ShowErrMsg(Info)
{
	$("#showMsg").html(Info);
}
function submitfrm(frm)
{
	if(frm.name && frm.name.value.trim()=="")
	{
		 ShowErrMsg("链接名称不能为空，请输入");
		 frm.name.focus();
		 return false;
	}
	if(frm.url && frm.url.value.trim()=="")
	{
		 ShowErrMsg("链接网址不能为空，请输入");
		 frm.url.focus();
		 return false;
	}
	return true;
}
</script>
<%@ include file="showMsg.jsp" %>

<form action="LinkServlet" method="post" onsubmit="return submitfrm(this);">
<input type="hidden" name="method" value="add" />
<table border=1 cellspacing=0 cellpadding=1>
  	<tr>
  		<th>名称</th>
  		<th>网址</th>
    	<th>顺序</th>	
  		<th>操作</th>
  	</tr>
    <tr>
   		<td><input type="text" name="name" style="width:200px;margin:0 3px;"/></td>
   		<td><input type="text" name="url" style="width:200px;margin:0 3px;"/></td>
   		<td><input type="text" name="sort" style="width:30px;margin:0 3px;"/></td>
   		<td><input type="submit" value="添加" class="form_btn"/></td>
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
<table border=1 cellspacing=0 cellpadding=1>
  	<tr>
  		<th>名称</th>
  		<th>网址</th>
    	<th>顺序</th>
  		<th>操作</th>
  	</tr>
    <% for(Link link : linkList){%>
    <form action="LinkServlet" method="post" onsubmit="return submitfrm(this);">
	<input type="hidden" name="method" value="update" />
	<input type="hidden" name="id" value="<%= link.getId()%>" />
    <tr class="exTr">
   	    <td><input type="text" name="name" value="<%= link.getName()%>" style="width:200px;margin:0 3px;" /></td>
   		<td><input type="text" name="url" value="<%= link.getUrl()%>" style="width:200px;margin:0 3px;" /></td>
   		<td><input type="text" name="sort" value="<%= link.getSort()%>" style="width:30px;margin:0 3px;" /></td>
   		<td>
		<input type="submit" value="修改" class="form_btn"/>
		<input type="button" value="删除" onClick="javascript:linkok('LinkServlet?method=delete&id=<%= link.getId()%>')" class="form_btn"/>
		</td>
	</tr>
	</form>
	<% }%>
</table>
</div>
<div class="ps_foot">
<strong>注：</strong><br>
网址请以http://开头<br>
</div>	
</body>
</html>
