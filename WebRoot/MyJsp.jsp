<%@ page language="java" pageEncoding="utf-8" %>
<%@ page import="java.util.*, com.ltms.model.*, com.ltms.dao.*, java.sql.*" %>
<%@ include file="global.jsp" %>
<%  
	ArrayList<Laboratory> laboratoryList = (ArrayList<Laboratory>)request.getAttribute("laboratoryList");
	Map<Integer, String> nameMap = DepartmentDAO.getNameMap();
	ArrayList<Department> departmentList = (ArrayList<Department>)DepartmentDAO.list();
	String location = "实验室一览";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<base href="<%=basepath%>/">
<link href="css/base.css" rel="stylesheet" type="text/css" />
<link href="css/general.css" rel="stylesheet" type="text/css" />
<script src="<%= basepath%>/js/prototype.js" type="text/javascript"></script>
<title><%= location + " - " + site_name%></title>
<script type="text/javascript"> 
	function getLa(departmentID){
		if(departmentID == 0){
			window.location.href = "LaboratoryServlet?method=webList";
		}else{
			var url = 'LaboratoryServlet';
			var params = 'method=getIndexAjax&departmentID='+departmentID;
			var myAjax = new Ajax.Request(
					url,
					{
						method: 'post', 
						parameters:params,
						onComplete:callback
					} );
		}
	} 
	function callback(response_text){
		var is_text = response_text.responseText;
	
		$('laboratory_list_ltms').innerHTML = is_text;
		$('sort_page_ltms').innerHTML = "";
	} 
</script> 
</head>
<body>
<div class="contain">
<%@ include file="header.jsp" %>
<!-- ◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆-->   
<div class="stm_content">
	<div class="content">
		<p class="empty">&nbsp;</p>
		<div class="content_position">
			<p class="position_bg">当前位置：<a href="index.jsp">首页</a>&nbsp;&gt;&gt;&nbsp;<a href="LaboratoryServlet?method=webList"><%= location%></a></p>
		</div>
		<div class="sort">
		<table>
		<tr>
            <td width="50%" class="td_bg">按系别查看：</td>
            <td width="50%" class="td_bg">
	            <select id="departmentID" name="departmentID" onchange="getLa(this.value)" style="height:26px;width:200px;">
				<option value="0">--请选择系别--</option>
		        <%for(Department d : departmentList){%>
					<option value="<%= d.getId()%>"><%= d.getName()%></option>
		        <%}%>
		        </select>
	        </td>
		</tr>
		</table>
		</div>
		
		<div class="inform_content">
			<span class="general_name">[实验室一览]</span>
        	<div class="inform_line ge_width">
        </div>
        <div class="general_list" id="laboratory_list_ltms">
			<ul>
				<%for(Laboratory l : laboratoryList){%>
				<li><p><a href=""><%= l.getName()%></a></p><span>[<%= nameMap.get(l.getDepartmentID())%>]</span></li>
	       		<%}%>
			</ul>
		</div>
        <div class="sort_page" id="sort_page_ltms">
        	<!-- 分页--> 
        	<% String baseUrl = "LaboratoryServlet?method=webList"; %>
			<%@ include file="page.jsp" %>
		</div>
		</div>
<!-- ◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆--> 
        <%@ include file="footer.jsp" %>
		</div>
	</div>
</div>
</body>
</html>