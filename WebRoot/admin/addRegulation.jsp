<%@ page language="java" import="java.util.*, com.ltms.model.*, java.sql.*,com.ltms.dao.*" pageEncoding="utf-8"%>
<%@ taglib uri="http://ckeditor.com" prefix="ckeditor"%>
<%@ taglib uri="http://ckfinder.com" prefix="ckfinder"%>
<%	String basepath = request.getContextPath();
	Admin admin = (Admin)request.getSession().getAttribute("admin");
	int departmentID = admin.getDepartmentID();
	int role = admin.getRole();
	List<Laboratory> laboratoryList = (ArrayList<Laboratory>)request.getAttribute("laboratoryList");
	List<Department> departmentList = (ArrayList<Department>)session.getAttribute("departmentList");	
	if(departmentList == null){
		departmentList = DepartmentDAO.list();
		session.setAttribute("departmentList", departmentList);
	}
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
<form action="RegulationServlet" method="post" onsubmit="return submitfrm(this);">
<input type="hidden" name="method" value="add" /> 
<input type="hidden" name="author" value="<%= ((Admin)request.getSession().getAttribute("admin")).getName()%>" /> 
	<table border="0" cellspacing="0" cellpadding="0">
	   	<tr>
	   		<td>标题<span style="color:red">*</span></td>
	   		<td><input type="text" size="120" maxlength="50" name="title" class="input1"/></td>
	   	</tr>
	   	<tr>
	   		<td>系别<span style="color:red">*</span></td>
	   		<td class="regulation_de_td">
			<% if(role == 0){ %>
    		<select name="departmentID" id="departmentID">
		   		<%	for(Iterator<Department> it = departmentList.iterator();it.hasNext();){
					Department d = it.next();%> 
		   		 	<option value="<%=d.getId()%>"><%=d.getName()%></option>
		   		<% } %>
	   		</select>
	   		<% }else{ %>
		    <%= DepartmentDAO.getName(departmentID)%>
		   	<input type="hidden" name="departmentID" value="<%= departmentID%>" />
		   	<% } %>
			</td>
	   	</tr>
	   	<tr>
	   		<td>实验室<span style="color:red">*</span></td>
	   		<td>
			<select name="laboratoryID" id="laboratoryID">
		   		<%	for(Iterator<Laboratory> it = laboratoryList.iterator();it.hasNext();){
					Laboratory l = it.next(); %> 
		   		 	<option value="<%=l.getId()%>"><%=l.getName()%></option>
		   		<% }%>
	   		</select>
			</td>
	   	</tr>
	   	<tr>
	   		<td>发布者</td>
	   		<td style="height: 20px;"><%= ((Admin)request.getSession().getAttribute("admin")).getName()%></td>
	   	</tr>
	   	<tr>
	   		<td>内容<span style="color:red">*</span></td>
	   		<td>
				<textarea id="editor_text" name="content" rows="12" cols="450"></textarea>
				<ckfinder:setupCKEditor basePath="ckfinder/" editor="editor1" />
				<ckeditor:replace replace="editor1" basePath="ckeditor/" />
			</td>
	   	</tr>
	   	<tr>
	   		<td colspan="2">
	   		<input type="submit" value="添加" class="form_btn"/>
	   		<input type="button" value="返回管理制度列表" onclick="javascript:window.location.href='RegulationServlet?method=list'" class="form_btn2"/>
			</td>
	   	</tr>
	 </table>
</form>
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