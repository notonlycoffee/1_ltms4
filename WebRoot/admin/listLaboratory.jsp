<%@ page language="java" import="java.util.*, com.ltms.model.*, java.sql.*,com.ltms.dao.LaboratoryDAO,com.ltms.dao.DepartmentDAO" pageEncoding="utf-8"%>
<%  
	int role = ((Admin)request.getSession().getAttribute("admin")).getRole();
	int departmentID = ((Admin)request.getSession().getAttribute("admin")).getDepartmentID();
	
	List<Laboratory> depArr = (ArrayList<Laboratory>)request.getAttribute("DepArr");	
	List depArrID = (ArrayList)request.getAttribute("arrID");	
	//System.out.println("depArrID is " + depArrID.size());
	//System.out.println("depArr is " + depArr.size());
	List<Laboratory> laboratoryList = (ArrayList<Laboratory>)request.getAttribute("laboratoryList");	
	if(laboratoryList == null){
		request.getRequestDispatcher("LaboratoryServlet?method=list&departmentID="+departmentID).forward(request, response);
		return;
	}
	List<Department> departmentList = (ArrayList<Department>)session.getAttribute("departmentList");	
	if(departmentList == null){
		departmentList = DepartmentDAO.list();
		session.setAttribute("departmentList", departmentList);
	}
	Map<Integer, String> nameMap = (HashMap<Integer, String>)session.getAttribute("nameMap");	
	if(nameMap == null){
		nameMap = DepartmentDAO.getNameMap();
		session.setAttribute("nameMap", nameMap);
	}
    boolean isContain = false;
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link rel="stylesheet" type="text/css" href="css/global.css" />
<script src="js/jquery.min.js"></script>
<script src="js/jquery.easing.min.js"></script>
<script src="js/base.js"></script>
</head>
<body>
<div id="position">您现在所在页面：实验室管理&nbsp;&gt;&gt;&nbsp;<a href="LaboratoryServlet?method=list&departmentID=<%= departmentID%>" target="content">实验室管理</a></div>
<div id="content" style="text-align:left">
<script type="text/javascript">
String.prototype.trim = function(){ return this.replace(/(^\s*)|(\s*$)/g, "");}
function ShowErrMsg(Info)
{
	$("#showMsg").html(Info);
}
function submitfrm(frm)
{
	if(frm.name.value.trim()=="")
	{
		 ShowErrMsg("实验室名称不能为空，请输入");
		 frm.name.focus();
		 return false;
	}
	if(frm.address.value.trim()=="")
	{
		 ShowErrMsg("实验室地址不能为空，请输入");
		 frm.address.focus();
		 return false;
	}
	return true;
}
</script>
<%@ include file="showMsg.jsp" %>
<% if(role == 0){%>
<!-- 搜索 -->
<form action="LaboratoryServlet" method="post" >
<input type="hidden" name="method" value="search" />
<table cellspacing=0 cellpadding=1>
	<tr>
		<th>系别</th>
		<th>实验室名称</th>
		<th>操作</th>
	</tr>
	<tr>
 		<td>
 		<select name="departmentID" id="departmentID">
 		<option value="0"></option>
   		<% for(Iterator<Department> it = departmentList.iterator();it.hasNext();){
   			Department d = it.next();%> 
   		 	<option value="<%= d.getId()%>"><%= d.getName()%></option>
   		<%}%>
   		</select>
 		</td>
  		<td><input type="text" name="name" /></td>
  		<td><input type="submit" value="查询" class="form_btn"/></td>
	</tr>
</table>
</form>
<%} %>

<% if(role == 3) {%>
	<form action="LaboratoryServlet" method="post" onsubmit="return submitfrm(this);" >
	<input type="hidden" name="method" value="add" />
	<table cellspacing=0 cellpadding=1>
	  	<tr>
	  		<th>系别</th>
	  		<th>实验室名称</th>
	  		<th>实验室地址</th>
	  		<th>操作</th>
	  	</tr>
	    <tr>
	   		<td>
	   		<% if(role == 0){ %>
	   		<select name="departmentID" id="departmentID">
	   		<% for(Iterator<Department> it = departmentList.iterator();it.hasNext();){
	   			Department d = it.next();%> 
	   		 	<option value="<%= d.getId()%>"><%= d.getName()%></option>
	   		<%}%>
	   		</select>
	   		<%}else{%>
		   	<%= DepartmentDAO.getName(departmentID)%>
		   	<input type="hidden" name="departmentID" value="<%= departmentID%>" />
		   	<%}%>
			</td>
			<td><input type="text" name="name" /></td>		
			<td><input type="text" name="address" /></td>
	   		<td><input type="submit" value="添加" /></td>
	    </tr>
	</table>
	</form>
<% } %>

<script type="text/javascript">
  function linkok(url){
    question = confirm("是否确认删除？");
    if (question){
      window.location.href = url;
    }
  }
</script>
<table cellspacing=0 cellpadding=0>
  	<tr>
  		<th>系别</th>
  		<th>实验室名称</th>
  		<th>实验室地址</th>
  		<th>状态</th>
  		<th>操作</th>
  	</tr>
    <% for(Laboratory laboratory : laboratoryList){%>
   	<tr class="exTr">
   		<td class="showTD" style="width:20%;text-align:left;padding:0 8px;"><%= nameMap.get(laboratory.getDepartmentID())%></td>
   	    <td class="showTD" style="width:30%;text-align:left;"><%= laboratory.getName()%></td>
   	    <td class="showTD" style="width:30%;text-align:left;"><%= laboratory.getAddress()%></td>
   		<td style="width:7%;">
    		<% if(depArrID.contains(laboratory.getId())){ %>未完成<%}else{ %>已完成<%} %>
   		</td>
   		<td style="width:15%;">
    		<input type="button" value="查看" onclick="javascript:window.location.href='viewLaboratory.jsp?id=<%= laboratory.getId() %>'" class="form_btn"/>
		<% if(role == 0 || role == 5){%>
			<input type="button" value="修改" onclick="javascript:window.location.href='editLaboratory.jsp?id=<%= laboratory.getId() %>'" class="form_btn"/>
		<%} %>
		<% if(role == 0 || role == 3){%>
   			<input type="button" value="删除" onclick="javascript:linkok('LaboratoryServlet?method=delete&id=<%= laboratory.getId()%>')" class="form_btn"/>
		<%} %>
   		</td>
   	</tr>
<% }%>
</table>

<% if(role!=5){ %>
	<!-- 分页--> 	
	<% String baseUrl = "LaboratoryServlet?method=list&departmentID="+departmentID; %>
	<%@ include file="page.jsp" %>
<% } %>
</div>

<div class="ps_foot">
<strong>注：</strong><br/>
查看某个系别的实验室请选择系别后点击查询按钮<br/>
</div>	
</body>
</html>