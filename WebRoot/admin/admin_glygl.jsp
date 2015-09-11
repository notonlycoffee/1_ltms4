<%@ page language="java" import="java.util.*, com.ltms.model.*, java.sql.*,com.ltms.dao.LaboratoryDAO,com.ltms.dao.DepartmentDAO" pageEncoding="utf-8"%>
<%  
	String currenTerm_ = (String) request.getSession().getAttribute("currenTerm");
		String currenTerm__ = (String) request.getSession().getAttribute("currenTerm");
		if("".equals(currenTerm__) || currenTerm__.trim().length() == 0){
			
			request.setAttribute("msg", "请先选择学年学期");
			request.getRequestDispatcher("showMsg.jsp").forward(request, response);
			return ;
		}
	int role = ((Admin)request.getSession().getAttribute("admin")).getRole();
	int departmentID = ((Admin)request.getSession().getAttribute("admin")).getDepartmentID();
	String id_syszr = ((Admin)request.getSession().getAttribute("admin")).getId();
	List<Laboratory> laboratoryList = (ArrayList<Laboratory>)request.getAttribute("laboratoryList");
	Map<Integer,String> sysfpMap = (Map)request.getAttribute("sysfpMap");
	Map<Integer,String> syszrMap = (Map)request.getAttribute("syszrMap");
	if(laboratoryList == null){
		request.getRequestDispatcher("LaboratoryServlet?method=list_syszr_sys&id_syszr="+id_syszr).forward(request, response);
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
	List arrID = (ArrayList)request.getAttribute("arrID");
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
<div id="position">您现在所在页面：实验室管理&nbsp;&gt;&gt;&nbsp;<a href="LaboratoryServlet?method=list&departmentID=<%= departmentID%>" target="content">实验室管理员管理</a></div>
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
  		<th>实验室管理员</th>
  		<th>状态</th>
  		<th>实验信息</th>
  		<th>操作</th>
  	</tr>
    <% for(Laboratory laboratory : laboratoryList){%>
   	<tr class="exTr">
   		<td class="showTD" style="width:13%;text-align:left;padding:0 8px;"><%= nameMap.get(laboratory.getDepartmentID())%></td>
   	    <td class="showTD" style="width:13%;text-align:left;"><%= laboratory.getName()%></td>
   	    <td class="showTD" style="width:12%;text-align:left;"><%= laboratory.getAddress()%></td>
   	    <% 
   	    	String data_message = laboratory.getId()+",null,null";
   	    	if(sysfpMap == null){
   	    %>
   	       	<td class="showTD" style="width:8%;text-align:left;">无</td>
   	    <% }else{
   	    	data_message = laboratory.getId()+","+syszrMap.get(sysfpMap.get(laboratory.getId()))+","+sysfpMap.get(laboratory.getId());
   	    %>
   	        <td class="showTD" style="width:8%;text-align:left;"><% if(syszrMap.get(sysfpMap.get(laboratory.getId())) == null){ %> 无 <%}else{ %> <%=syszrMap.get(sysfpMap.get(laboratory.getId())) %> <%} %></td>
   	    <%}%>
   	    
   	    <td class="showTD" style="width:5%;text-align:left;"> <% if(arrID.contains(laboratory.getId())){ %>未完成<%}else{ %>已完成<%} %> </td>
   	    <td class="showTD" style="width:35%;text-align:left;">
   	    	<input type="button" value="查看实验室" onclick="javascript:window.location.href='viewLaboratory.jsp?id=<%= laboratory.getId() %>'" class="form_btn" style="width: 100px"/>
   	    	<input type="button" value="查看基本属性" onclick="javascript:window.location.href='viewSysxx.jsp?id=<%= laboratory.getId() %>'" class="form_btn" style="width: 100px"/>
			<input type="button" value="查看基本情况" onclick="javascript:window.location.href='viewSysqk.jsp?id=<%= laboratory.getId() %>'" class="form_btn" style="width: 100px"/>
			<input type="button" value="查看经费情况" onclick="javascript:window.location.href='viewSysjf.jsp?id=<%= laboratory.getId() %>'" class="form_btn" style="width: 100px"/>
		</td>
		<td class="showTD" style="width:10%;text-align:left;">
   	    	<input type="button" value="设置管理员" onclick="javascript:window.location.href='editGly.jsp?id=<%= data_message%>'" class="form_btn" style="width: 100px"/>
		</td>
   	</tr>
<% }%>
</table>

<!-- 分页--> 	
<% String baseUrl = "LaboratoryServlet?method=list_syszr_sys&id_syszr="+id_syszr; %>
<%@ include file="page.jsp" %>
</div>

<div class="ps_foot">
<strong>注：</strong><br/>
查看某个系别的实验室请选择系别后点击查询按钮<br/>
</div>	
</body>
</html>