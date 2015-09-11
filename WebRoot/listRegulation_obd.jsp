<%@ page language="java" pageEncoding="utf-8" %>
<%@ page import="java.util.*, com.ltms.model.*, com.ltms.dao.*, java.sql.*" %>
<%@ include file="global.jsp" %>
<%  
	ArrayList<Regulation> regulationList = (ArrayList<Regulation>)request.getAttribute("regulationList");
	int departmentID = (Integer)request.getAttribute("departmentID");
	Map<Integer, String> laboratoryNameMap = (Map<Integer, String>)request.getAttribute("laboratoryNameMap");
	ArrayList<Department> departmentList = (ArrayList<Department>)DepartmentDAO.list();
	String location = DepartmentDAO.getName(departmentID) + "实验室管理制度";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link href="css/base.css" rel="stylesheet" type="text/css" />
<link href="css/general.css" rel="stylesheet" type="text/css" />
<script src="<%= basepath%>/js/prototype.js" type="text/javascript"></script>
<title><%= location + " - " + site_name%></title>
</head>
<body>
<div class="contain">
<%@ include file="header.jsp" %>
<!-- ◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆-->   
<div class="stm_content">
	<div class="content">
		<p class="empty">&nbsp;</p>
		<div class="content_position">
			<p class="position_bg">当前位置：<a href="index.jsp">首页</a>&nbsp;&gt;&gt;&nbsp;<a href="RegulationServlet?method=webList_obd&departmentID=<%= departmentID%>"><%= location%></a></p>
		</div>
		<div class="sort">
		<form action="RegulationServlet" method="post">
		<input type="hidden" name="method" value="webList_obd" />
		<table>
		<tr>
            <td width="40%" class="td_bg">按教学单位查看：</td>
            <td width="40%" class="td_bg">
	            <select id="departmentID" name="departmentID" style="height:26px;width:200px;">
		        <%for(Department d : departmentList){%>
					<option value="<%= d.getId()%>"><%= d.getName()%></option>
		        <%}%>
		        </select>
	        </td>
	        <td width="20%" class="td_bg"><input type="submit" value="查询" class="btn"/></td>
		</tr>
		</table>
		</form>
		</div>
		
		<div class="inform_content">
			<span class="general_name">[实验室管理制度]</span>
        	<div class="inform_line ge_width">
        </div>
        <div class="general_list" id="regulation_list_ltms">
			<ul>
				<%for(Regulation n : regulationList){%>
	           	<li><p><a href="RegulationServlet?method=webList_obl&laboratoryID=<%= n.getLaboratoryID()%>" style="color:#0099CB">[<%= laboratoryNameMap.get(n.getLaboratoryID())%>]</a><a href="RegulationServlet?method=view&id=<%= n.getId() %>"><%= n.getTitle()%></a></p><span><%= n.getDate()%></span></li>
				<%}%>
			</ul>
		</div>
        <div class="sort_page" id="sort_page_ltms">
        	<!-- 分页--> 
        	<% String baseUrl = "RegulationServlet?method=webList_obd&departmentID=" + departmentID; %>
			<%@ include file="page.jsp" %>
		</div>
		</div>
<!-- ◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆--> 
        <%@ include file="footer.jsp" %>
		</div>
	</div>
</div>
<%	if(regulationList.size() == 0){%>
<script type="text/javascript">
	$('sort_page_ltms').innerHTML = "";
	$('regulation_list_ltms').innerHTML = "<div class=\"no_result_msg\">暂无管理制度</div>";
</script>
<%	} %>
</body>
</html>
