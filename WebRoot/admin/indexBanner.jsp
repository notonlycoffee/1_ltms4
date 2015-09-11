<%@ page language="java" import="java.util.*, com.ltms.model.*, java.sql.*,com.ltms.dao.KeyLabDAO,com.ltms.dao.DepartmentDAO" pageEncoding="utf-8"%>
<%@ include file="global.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link rel="stylesheet" type="text/css" href="css/global.css" />
<script src="js/jquery.easing.min.js"></script>
<script src="js/base.js"></script>
</head>
<body>
<div id="position">您现在所在页面：信息展示&nbsp;&gt;&gt;&nbsp;<a href="indexBanner.jsp" target="content">首页banner管理</a></div>
<div id="content" style="text-align:left">
<script type="text/javascript">
String.prototype.trim = function(){ return this.replace(/(^\s*)|(\s*$)/g, "");}
function ShowErrMsg(Info)
{
	document.getElementById("showMsg").innerHTML = Info;
}
function submitfrm(frm)
{
	if(frm.pic.value != null && frm.pic.value.trim() == "")
	{
		 alert("请上传图片");
		 frm.pic.focus();
		 return false;
	}
	return true;
}
</script>
<%@ include file="showMsg.jsp" %>
<form action="IndexBannerServlet" method="post" enctype="multipart/form-data" onsubmit="return submitfrm(this);">
<input type="hidden" name="method" value="add" />
<table border=1 cellspacing=0 cellpadding=1>
  	<tr>
  		<th>链接</th>
  		<th>banner</th>
    	<th>顺序</th>	
  		<th>操作</th>
  	</tr>
    <tr>
   		<td><input type="text" name="url" style="width:200px;margin:0 3px;"/></td>
   		<td><input type="file" name="pic" style="width: 150px;"/></td>
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
<table cellspacing=0 cellpadding=1>
	<tr>
		<th>链接</th>
		<th>banner</th>
		<th>更换banner</th>
		<th>顺序</th>
		<th>操作</th>
	</tr>
	<%	List<IndexBanner> indexBannerList = IndexBannerDAO.list();	 
		for(IndexBanner ib : indexBannerList){%>
	<form action="IndexBannerServlet" method="post" enctype="multipart/form-data">
	<input type="hidden" name="method" value="update" />
	<input type="hidden" name="old_pic" value="<%= ib.getBanner()%>" />
	<input type="hidden" name="id" value="<%= ib.getId()%>" />
	<tr class="exTr">
		<td><input type="text" name="url" value="<%= ib.getUrl()%>" style="width: 150px;"/></td>
  		<td style="padding-top:3px;padding-bottom:3px;">
  			<a href="<%= basepath + "/" + ib.getBanner()%>" target="_view_banner_pic"><img src="<%= basepath + "/" + ib.getBanner()%>" style="width:60px;"/></a>
  		</td>
  		<td><input type="file" name="pic" style="width: 150px;"/></td>
  		<td><input type="text" name="sort" value="<%= ib.getSort()%>" style="width: 30px;"/></td>
  		<td><input type="submit" value="修改" class="form_btn"/><input type="button" value="删除" onClick="javascript:linkok('IndexBannerServlet?method=delete&id=<%= ib.getId()%>')" class="form_btn"/></td>
	</tr>
	</form>
	<%	} %>
</table>
</div>
注： 图片尺寸最好是955 * 188，否则可能出现图片变形
</body>
</html>