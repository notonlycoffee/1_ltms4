<%@ page language="java" pageEncoding="utf-8"%>
<%@ page
	import="java.util.*, com.ltms.model.*, com.ltms.dao.*, java.sql.*"%>
<%@ include file="global.jsp"%>
<%
	ArrayList<Regulation> regulationList = (ArrayList<Regulation>) request
			.getAttribute("regulationList");
	if (regulationList == null) {
		request.getRequestDispatcher("RegulationServlet?method=webList")
				.forward(request, response);
	}
	Map<Integer, String> laboratoryNameMap = (Map<Integer, String>) request
			.getAttribute("laboratoryNameMap");
	ArrayList<Department> departmentList = (ArrayList<Department>) DepartmentDAO
			.list();
	String location = "实验室管理制度";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link rel="stylesheet" href="css/listRegulationStyle.css"
	type="text/css">
<title><%=location + " - " + site_name%></title>
</head>

<body>
	<!--头部开始-->
	<%@ include file="header.jsp"%>
	<!--头部结束-->
	<!--内容开始-->
	<div id="content">
		<div class="main">
			<!--内容左部开始-->
			<div class="left">
				<div class="search">
					<div class="left_margin"></div>
					<div class="left_title">
						<p>按教学单位查看</p>
					</div>
					<div class="left_cho">
						<form class="form" action="RegulationServlet" method="post">
							<input type="hidden" name="method" value="webList_obd" /> <select
								class="select" id="departmentID" name="departmentID">
								<%
									for (Department d : departmentList) {
								%>
								<option value="<%=d.getId()%>"><%=d.getName()%></option>
								<%
									}
								%>
							</select> <input class="submit" type="submit" name="Submit" value="查询">
						</form>
					</div>
				</div>
			</div>
			<!--内容左部结束-->
			<!--内容右部开始-->
			<div class="right">
				<div class="title">
					<p class="name">实验室管理制度&nbsp;&nbsp&nbsp;Laboratory management
						system</p>
					<p class="path">
						<a href="index.jsp">首页</a><span class="sign">></span><a
							href="RegulationServlet?method=webList"><%=location%></a>
					</p>
				</div>
				<p class="title2">实验室管理制度</p>
				<img src="imgs/table_bor.png" />
				<table id="laboratory_list_ltms">
					<%
						for (Regulation n : regulationList) {
					%>
					<tr>
						<td class="new_Lab"><a
							href="RegulationServlet?method=webList_obl&laboratoryID=<%=n.getLaboratoryID()%>"><%=laboratoryNameMap.get(n.getLaboratoryID())%></a></td>
						<td class="new_title">
						<a href="RegulationServlet?method=view&id=<%= n.getId() %>"><%= n.getTitle()%></a></p>
						</td>
						<td><%=n.getDate()%></td>
					</tr>
					<%
						}
					%>
					<%-- <div class="general_list" id="laboratory_list_ltms">
			<ul>
				<%for(Regulation n : regulationList){%>
	           	<li><p><a href="RegulationServlet?method=webList_obl&laboratoryID=<%= n.getLaboratoryID()%>" style="color:#0099CB">[<%= laboratoryNameMap.get(n.getLaboratoryID())%>]</a><a href="RegulationServlet?method=view&id=<%= n.getId() %>"><%= n.getTitle()%></a></p><span><%= n.getDate()%></span></li>
				<%}%>
			</ul>
		</div> --%>
				</table>
				<img src="imgs/table_bor.png" />
				<div class="page" id="sort_page_ltms">
					<!-- 分页-->
					<%
						String baseUrl = "RegulationServlet?method=webList";
					%>
					<%@ include file="page.jsp"%>
				</div>
			</div>
			<!--内容右部结束-->
		</div>
	</div>
	<!--内容结束-->
	<div class="clear"></div>
	<!--页脚开始-->
	<%@ include file="footer.jsp" %>
	<!--页脚结束-->
</body>
</html>
