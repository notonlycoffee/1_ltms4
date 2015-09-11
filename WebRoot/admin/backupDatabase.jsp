<%@ page language="java" import="java.util.*, com.ltms.model.*, java.sql.*,com.ltms.dao.*, com.ltms.util.*" pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link rel="stylesheet" type="text/css" href="css/global.css" />
</head>
<body>
<div id="position">您现在所在页面：系统管理&nbsp;&gt;&gt;&nbsp;<a class="child" href="databaseBackup.jsp" target="content">数据库备份</a></div>
<div id="content" style="text-align:left">
<script type="text/javascript">
String.prototype.trim = function(){ return this.replace(/(^\s*)|(\s*$)/g, "");}
function ShowErrMsg(Info)
{
	document.getElementById("showMsg").innerHTML = Info;
}
function submitfrm(frm)
{
	if(frm.file && frm.file.value.trim()=="")
	{
		 ShowErrMsg("请上传数据库文件");
		 frm.file.focus();
		 return false;
	}
	return true;
}
</script>
<%@ include file="showMsg.jsp" %>
<table border=1 cellspacing=0 cellpadding=0>
  	<tr>
  		<th colspan=2>备份数据库</th>
  	</tr>
    <tr>
   		<form action="DatabaseServlet" method="post">
    		<td colspan=2 style="border-bottom:1px #aaa solid;" >
    		<input type="hidden" name="method" value="backup" />
    		<input type="submit" value="备份" class="form_btn"/>
    		</td>
    	</form>
    </tr>
    <tr >
  		<th width=220>选择数据库文件</th>
  		<th width=120>操作</th>
  	</tr>
  	<tr>
		<td>
	<form action="DatabaseServlet" method="post" style="margin:0px;" enctype="multipart/form-data" onsubmit="return submitfrm(this);">
		<input type="file" name="file"/>
		</td>
		<td>
			<input type="hidden" name="method" value="load" />
	 		<input type="submit" value="恢复数据库" class="form_btn2"/>
		</td>
	</form>
    </tr>
</table>

</div>
</body>
</html>