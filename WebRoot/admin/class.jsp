<%@ page language="java" import="java.util.*, com.ltms.model.*, java.sql.*,com.ltms.dao.ClassDAO,com.ltms.dao.DepartmentDAO" pageEncoding="utf-8"%>
<%@ include file="global.jsp" %>
<%
	int role = ((Admin)request.getSession().getAttribute("admin")).getRole();
	int departmentID = ((Admin)request.getSession().getAttribute("admin")).getDepartmentID();
	List<_Class> classList = null;
	if(role == 1){
		classList = ClassDAO.list(departmentID);
	}else{
		classList = ClassDAO.list(1);
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
<div id="position">您现在所在页面：实验室管理&nbsp;&gt;&gt;&nbsp;<a href="ClassServlet?method=list" target="content">班级管理</a></div>
<div id="content" style="text-align:left">
<script type="text/javascript">
String.prototype.trim = function(){ return this.replace(/(^\s*)|(\s*$)/g, "");}
function ShowErrMsg(Info)
{
	$("#showMsg").html(Info);
}

function submitfrm(frm)
{
	if(frm.id && frm.id.value.trim()=="")
	{
		 ShowErrMsg("班级不能为空，请输入");
		 frm.id.focus();
		 return false;
	}
	if(frm.name && frm.name.value.trim()=="")
	{
		 ShowErrMsg("专业名称不能为空，请输入");
		 frm.name.focus();
		 return false;
	}
	if(frm.studentNumber && frm.studentNumber.value.trim()=="")
	{
		 ShowErrMsg("人数不能为空，请输入");
		 frm.studentNumber.focus();
		 return false;
	}
	var reg = /^\+?[1-9][0-9]*$/;
	if(frm.studentNumber && !reg.test(frm.studentNumber.value)){
		ShowErrMsg("人数只能为正整数，请重新输入");
		frm.studentNumber.focus();
		return false;
	}
	if(frm.grade && frm.grade.value.trim()=="")
	{
		 ShowErrMsg("年级不能为空，请输入");
		 frm.grade.focus();
		 return false;
	}
	return true;
}

//ajax部分
function getCl(departmentID){
	$.ajax({url:"ClassServlet?method=getAjax&departmentID="+departmentID,
		type:"GET",
		async:false,
		dataType:"text",
		success: function(text){
	 		$("#ajax_cl").html(text);
		}
	})
	extr_function();
}
</script>
<%@ include file="showMsg.jsp" %>
<!-- 查询 -->
<table cellspacing=0 cellpadding=0 class="ajax_de">
<tr>
	<td class="TD_Left" style="width:90px;" >系别</td>
	<td class="TD_Right" style="padding:0;">
	<select id="departmentID" name="departmentID" onchange="getCl(this.value)" style="width:200px;height:23px;margin:0;">
		<%for(Department d : departmentList){%>
		<option value="<%= d.getId()%>" <%= d.getId() == departmentID ? "selected" : ""%> ><%= d.getName()%></option>
		<%}%>
	</select>
	</td>
</tr>
</table>
<% if(role != 2){%>
<!-- 添加 -->
<form action="ClassServlet" method="post" onsubmit="return submitfrm(this);" >
<input type="hidden" name="method" value="add" />
<input type="hidden" name="returnPage" value="class" />
<table cellspacing=0 cellpadding=1>
  	<tr>
  		<th>班级</th>
  		<th>专业名称</th>
  		<th>系别</th>
  		<th>人数</th>
  		<th>年级</th>
  		<th>操作</th>
  	</tr>
    <tr>
  		<td><input style="width:90px;margin:0 3px;" type="text" name="id" /></td>	
   		<td><input style="width:200px;margin:0 3px;" type="text" name="name" /></td>		
   		<td>
   		<% if(role == 0){%>
   		<select name="departmentID" id="departmentID">
	   		<%
	   			for(Iterator<Department> it = departmentList.iterator();it.hasNext();){
	   				   			Department d = it.next();
	   		%> 
	   		 	<option value="<%=d.getId()%>"><%=d.getName()%></option>
	   		<%
	   			}
	   		%>
   		</select>
   		<% }else{%>
	   	<%= nameMap.get(departmentID)%>
	   	<input type="hidden" name="departmentID" value="<%= departmentID%>" />
	   	<% }%>
		</td>
		<td><input style="width:50px;margin:0 3px;" type="text" name="studentNumber" /></td>
		<td><input style="width:50px;margin:0 3px;" type="text" name="grade" /></td>	
   		<td><input type="submit" value="添加" class="form_btn"/></td>
    </tr>
</table>
</form>
<%} %>

	<div id="ajax_cl">
	<table cellspacing=0 cellpadding=0>
	  	<tr>
	  		<th>班级</th>
	  		<th>专业名称</th>
	  		<th>系别</th>
	  		<th>人数</th>
	  		<th>年级</th>
	  	</tr>
	<% for(_Class clazz : classList){%>
	   	<tr class="exTr">
			<td class="showTD" style="width:100px;"><%= clazz.getId()%></td>
	   	    <td class="showTD" style="width:210px;text-align:left;"><%= clazz.getName()%></td>
	   	    <td class="showTD" style="width:140px;text-align:center;"><%= nameMap.get(clazz.getDepartmentID())%></td>
	   		<td class="showTD" style="width:60px;"><%= clazz.getStudentNumber()%></td>
	   		<td class="showTD" style="width:60px;"><%= clazz.getGrade()%></td>
	   	</tr>
	<% }%>
	</table>
	</div>
	
	<div class="clear"></div>
</div>


<div class="ps_foot">
<strong>注：</strong><br/>
选择系别可查看各系别班级列表<br/>
</div>	

</body>
</html>