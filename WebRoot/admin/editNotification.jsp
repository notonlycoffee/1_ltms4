<%@ page language="java" import="java.util.*, com.ltms.model.*, java.sql.*,com.ltms.dao.*" pageEncoding="utf-8"%>
<%@ taglib uri="http://ckeditor.com" prefix="ckeditor"%>
<%@ taglib uri="http://ckfinder.com" prefix="ckfinder"%>
<%	Admin admin = ((Admin)request.getSession().getAttribute("admin"));
	int role = admin.getRole();
	int id = Integer.parseInt(request.getParameter("id"));
	Notification notification = NotificationDAO.load(id);
	String basepath = request.getContextPath();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link rel="stylesheet" type="text/css" href="css/global.css" />
<script type="text/javascript" src="<%=basepath%>/admin/ckeditor/ckeditor.js"></script>
<script type="text/javascript" src="<%=basepath%>/admin/ckfinder/ckfinder.js"></script>
</head>
<body>
<% if(role != 0){%>
<div style="padding:10px;">
<font style="color:red;font-size:14px;">您不是系统管理员, 无权限修改通知公告内容!&nbsp;&nbsp;3秒后返回上一个页面</font><br/>
<script type="text/javascript">
	function go(){
		window.history.go(-1);
	}
setTimeout("go()",3000);
</script>
<span id="error_return"><a href="javascript:window.history.go(-1)">立即返回</a>&nbsp;&nbsp;<a href="javascript:window.parent.location = 'index.jsp'">返回首页</a></span>
</div>
<% return; }%>
<div id="newsContent" style="text-align:left">
<script type="text/javascript">
String.prototype.trim = function(){ return this.replace(/(^\s*)|(\s*$)/g, "");}
function ShowErrMsg(Info)
{
	document.getElementById("showMsg").innerHTML = Info;
}
function submitfrm(frm)
{
	if(frm.title && frm.title.value.trim() == ""){
		ShowErrMsg("标题不能为空，请输入");
		frm.title.focus();
		return false;
	}
	return true;
}
</script>
<%@ include file="showMsg.jsp" %>
<form action="NotificationServlet" method="post" onsubmit="return submitfrm(this);">
<input type="hidden" name="id" value="<%= notification.getId()%>" /> 
<input type="hidden" name="method" value="update" />
	<table border="0" cellspacing="0" cellpadding="0">
	   	<tr>
	   		<td>标题<span style="color:red">*</span></td>
	   		<td><input type="text" size="120" maxlength="50" name="title" value="<%= notification.getTitle()%>" class="input1"/></td>
	   	</tr>
	   	<tr>
	   		<td>发布者</td>
	   		<td><input type="text" name="author" value="<%= notification.getAuthor()%>" class="input1"/></td>
	   	</tr>
	   	<tr>
	   		<td>内容<span style="color:red">*</span></td>
	   		<td>
				<textarea id="editor_text" name="content" rows="12" cols="450"><%= notification.getContent()%></textarea>
				<ckfinder:setupCKEditor basePath="ckfinder/" editor="editor1" />
				<ckeditor:replace replace="editor1" basePath="ckeditor/" />
			</td>
		</tr>
		<tr>
	   		<td colspan="2">
			<input type="submit"  value="修改" class="form_btn"/>
			<input type="button" value="返回通知公告列表" onClick="javascript:window.location.href='NotificationServlet?method=list'" class="form_btn2"/>
			</td>
	   	</tr>
	</table>
</form>
</div>
<div class="ps_foot">
<strong>注：</strong><br>
<br>
</div>	
<script type="text/javascript">
// This is a check for the CKEditor class. If not defined, the paths must be checked.
if ( typeof CKEDITOR == 'undefined' )
{
document.write('<strong><span style="color: #ff0000">Error</span>: CKEditor not found</strong>.' +'This sample assumes that CKEditor (not included with CKFinder) is installed in' +'the "/ckeditor/" path. If you have it installed in a different place, just edit' +'this file, changing the wrong paths in the <head> (line 5) and the "BasePath"' +'value (line 32).' ) ;
}
else
{
var editor = CKEDITOR.replace( 'editor_text' );
CKFinder.setupCKEditor(editor, '<%=basepath%>/admin/ckfinder' );
// It is also possible to pass an object with selected CKFinder properties as a second argument.
// CKFinder.SetupCKEditor( editor, { BasePath : '../../', RememberLastFolder : false } ) ;
}
</script>
</body>
</html>