<%@ page language="java" import="java.util.*, com.ltms.model.*, java.sql.*,com.ltms.dao.KeyLabDAO,com.ltms.dao.DepartmentDAO" pageEncoding="utf-8"%>
<%@ include file="global.jsp" %>
<%  
	List<KeyLab> keyLabList = (ArrayList<KeyLab>)session.getAttribute("keyLabList");	
	if(keyLabList == null){
		keyLabList = KeyLabDAO.list();
		session.setAttribute("keyLabList", keyLabList);
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
<div id="position">您现在所在页面：信息展示&nbsp;&gt;&gt;&nbsp;<a href="keyLab.jsp" target="content">省级示范中心</a></div>
<div id="content" style="text-align:left">
<script type="text/javascript">
String.prototype.trim = function(){ return this.replace(/(^\s*)|(\s*$)/g, "");}
function ShowErrMsg(Info)
{
	document.getElementById("showMsg").innerHTML = Info;
}
function submitfrm(frm)
{
	if(frm.name.value != null && frm.name.value.trim() == "")
	{
		 ShowErrMsg("示范中心名称不能为空，请输入");
		 frm.name.focus();
		 return false;
	}
	if(frm.gotoURL.value != null && frm.gotoURL.value.trim() == "")
	{
		 ShowErrMsg("示范中心网址不能为空，请输入");
		 frm.gotoURL.focus();
		 return false;
	}
	return true;
}
</script>
<%@ include file="showMsg.jsp" %>

<form action="KeyLabServlet" method="post" enctype="multipart/form-data" onsubmit="return submitfrm(this);" >
<input type="hidden" name="method" value="add" />
<table cellspacing=0 cellpadding=1>
	<tr>
		<th>示范中心名称</th>
		<th>示范中心网址</th>
		<th>显示排序</th>
		<th>logo</th>
		<th>操作</th>
	</tr>
	<tr>
  		<td><input type="text" name="name"  style="width:200px;margin:0 3px;" /></td>
  		<td><input type="text" name="gotoURL" style="width:250px;margin:0 3px;" /></td>
		<td><input type="text" name="sort"  style="width:50px;margin:0 3px;" /></td>
		<td><input type="file" name="pic" style=""/></td>
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
<table border=1 cellspacing=0 cellpadding=0 >
	<tr>
		<th>示范中心名称</th>
		<th>示范中心网址</th>
		<th>显示排序</th>
		<th>logo</th>
		<th>更换logo</th>
		<th>操作</th>
	</tr>
<% for(KeyLab keyLab : keyLabList){%>
<tr class="exTr">
<form action="KeyLabServlet" method="post" enctype="multipart/form-data" onsubmit="return submitfrm(this);">
<input type="hidden" name="id" value="<%= keyLab.getId()%>" />
<input type="hidden" name="method" value="update" />
<input type="hidden" name="old_pic" value="<%= keyLab.getPic() == null ? "" : keyLab.getPic()%>" />
	<td><input type="text" name="name" value="<%= keyLab.getName()%>" style="width:200px;margin:0 3px;" /></td>
	<td><input type="text" name="gotoURL" style="width:250px;margin:0 3px;" value="<%= keyLab.getGotoURL()%>" /></td>
	<td><input type="text" name="sort" value="<%= keyLab.getSort()%>" style="width:50px;margin:0 3px;" /></td>
	<td>
	<%	if(keyLab.getPic() != null && !"".equals(keyLab.getPic())){ %>
		<a href="<%= basepath + "/" + keyLab.getPic()%>" target="_view_logo_pic"><img src="<%= basepath + "/" + keyLab.getPic()%>" style="width:30px;"/></a>
	<%	}else{ %>
		<span>暂无logo</span>
	<%	}%>
	</td>
	<td><input type="file" name="pic" style="width: 150px;"/>删除logo<input type="checkbox" name="is_del" /></td>
	<td>
	<input type="submit" value="修改" class="form_btn"/>
	<input type="button" value="删除" onclick="javascript:linkok('KeyLabServlet?method=delete&id=<%= keyLab.getId()%>')" class="form_btn"/>
	</td>
</form>
</tr>
<% }%>
</table>

</div>

<div class="ps_foot">
<strong>注：</strong><br/>
网址请以http://开头
</div>	

</body>
</html>