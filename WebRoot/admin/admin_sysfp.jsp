<%@ page language="java" import="java.util.*, com.ltms.model.*, java.sql.*,com.ltms.dao.LaboratoryDAO,com.ltms.dao.DepartmentDAO" pageEncoding="utf-8"%>
<%  
	int role = ((Admin)request.getSession().getAttribute("admin")).getRole();
	int departmentID = ((Admin)request.getSession().getAttribute("admin")).getDepartmentID();
	List<Laboratory> laboratoryList = (ArrayList<Laboratory>)request.getAttribute("laboratoryList");
	Map<Integer,String> sysfpMap = (Map)request.getAttribute("sysfpMap");
	Map<Integer,String> syszrMap = (Map)request.getAttribute("syszrMap");
	if(laboratoryList == null){
		request.getRequestDispatcher("LaboratoryServlet?method=list&lc=sysfp&departmentID="+departmentID).forward(request, response);
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
<div id="position">您现在所在页面：实验室管理&nbsp;&gt;&gt;&nbsp;<a href="#" target="content">实验室管理</a></div>
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
  		<th>实验室主任</th>
  		<th>操作</th>
  	</tr>
    <% for(Laboratory laboratory : laboratoryList){%>
   	<tr class="exTr">
   		<td class="showTD" style="width:20%;text-align:left;padding:0 8px;"><%= nameMap.get(laboratory.getDepartmentID())%></td>
   	    <td class="showTD" style="width:30%;text-align:left;"><%= laboratory.getName()%></td>
   	    <td class="showTD" style="width:30%;text-align:left;"><%= laboratory.getAddress()%></td>
   	    <% 
   	    	String data_message = laboratory.getId()+",null,null";
   	    	if(sysfpMap == null){
   	    %>
   	       	<td class="showTD" style="width:30%;text-align:left;">无</td>
   	    <% }else{
   	    	data_message = laboratory.getId()+","+syszrMap.get(sysfpMap.get(laboratory.getId()))+","+sysfpMap.get(laboratory.getId());
   	    %>
   	        <td class="showTD" style="width:30%;text-align:left;"><% if(syszrMap.get(sysfpMap.get(laboratory.getId())) == null){%> 无 <%}else{ %><%=syszrMap.get(sysfpMap.get(laboratory.getId())) %><%} %></td>
   	    <%}%>
   	    
   		<td style="width:22%;">
			<input type="button" value="修改" onclick="javascript:window.location.href='editSysfp.jsp?id=<%= data_message%>'" class="form_btn"/>
   		</td>
   	</tr>
<% }%>
</table>

<!-- 分页--> 	
<% String baseUrl = "LaboratoryServlet?method=list&lc=sysfp&departmentID="+departmentID; %>
<%@ include file="page.jsp" %>
</div>

<div class="ps_foot">
<strong>注：</strong><br/>
查看某个系别的实验室请选择系别后点击查询按钮<br/>
</div>	
</body>
</html>