<%@ page language="java" import="java.util.*, com.ltms.util.StringUtil, com.ltms.model.*, java.sql.*,com.ltms.dao.*" pageEncoding="utf-8"%>
<%
	String id_admin = ((Admin)request.getSession().getAttribute("admin")).getId();
	String name_admin = ((Admin)request.getSession().getAttribute("admin")).getName();
	List<Regulation> regulationList = (ArrayList<Regulation>)request.getAttribute("regulationList");	
	if(regulationList == null){
		request.getRequestDispatcher("RegulationServlet?method=list").forward(request, response);
		return;
	}
	Map<Integer, String> laboratoryNameMap = (Map<Integer, String>)request.getAttribute("laboratoryNameMap");
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
<div id="position">您现在所在页面：信息展示&nbsp;&gt;&gt;&nbsp;<a href="RegulationServlet?method=list" target="content">管理制度</a>
<span id="addInfo"><a href="LaboratoryServlet?method=list_glzd&id=<%=id_admin %>">添加管理制度</a></span></div>
<div id="content" style="text-align:left">
<script type="text/javascript">
String.prototype.trim = function(){ return this.replace(/(^\s*)|(\s*$)/g, "");}
function ShowErrMsg(Info)
{
	$("#showMsg").html(Info);
}
function submitfrm(frm)
{
	if(frm.keyword && frm.keyword.value.trim() == ""){
		ShowErrMsg("请输入搜索关键字");
		frm.keyword.focus();
		return false;
	}
	return true;
}
</script>
<%@ include file="showMsg.jsp" %>
<form action="RegulationServlet" method="post" onsubmit="return submitfrm(this);" >
<input type="hidden" name="method" value="search" />
<table cellspacing=0 cellpadding=1 >
  	<tr>
  		<th>关键字</th>
  		<th>操作</th>
  	</tr>
    <tr>
   		<td><input type="text" name="keyword" /></td>
    	<td><input type="submit" value="查询" class="form_btn"/></td>
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
<table border=1 cellspacing=0 cellpadding=0>
  	<tr>
  		<th>实验室</th>
  		<th>标题</th>
  		<th>作者</th>
  		<th>添加时间</th>
  		<th>操作</th>
  	</tr>
    <%	for(Regulation regulation : regulationList){
    	if(regulation.getAuthor().equals(name_admin)){%>
	   	<tr class="exTr">
	   	 	<td width=150><%= laboratoryNameMap.get(regulation.getLaboratoryID())%></td>
	   	    <td width=400 style="text-align:left;"><a href="editRegulation.jsp?id=<%= regulation.getId() %>"><%= StringUtil.trimString(regulation.getTitle(), 25)%></a></td>
	   	    <td width=100><%= StringUtil.trimString(regulation.getAuthor(), 6)%></td>
	   	    <td width=90><%= regulation.getDate()%></td>
	   	    <td>
	   		<input type="button" value="修改" onclick="javascript:window.location.href='editRegulation.jsp?id=<%= regulation.getId()%>'" class="form_btn"/>
			<input type="button" value="删除" onclick="javascript:linkok('RegulationServlet?method=delete&id=<%= regulation.getId()%>&departmentID=<%= regulation.getDepartmentID()%>')" class="form_btn"/>
			</td>
	   	</tr>	
   <%} }%>
</table>  
<!-- 分页--> 	
<% String baseUrl = "RegulationServlet?method=list&categoryID=2"; %>
<%@ include file="page.jsp" %>
</div>
</body>
</html>