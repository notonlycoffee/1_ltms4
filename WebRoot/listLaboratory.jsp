<%@ page language="java" pageEncoding="utf-8"%>
<%@ page
	import="java.util.*, com.ltms.model.*, com.ltms.dao.*, java.sql.*"%>
<%@ include file="global.jsp"%>
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
<base href="<%=basepath%>/" />
<link rel="stylesheet" href="css/listLaboratoryStyle.css"
	type="text/css" />
<script src="<%=basepath%>/js/prototype.js" type="text/javascript"></script>
<title><%=location + " - " + site_name%></title>
<script type="text/javascript">
	function getLa(departmentID) {
		if (departmentID == 0) {
			window.location.href = "LaboratoryServlet?method=webList";
		} else {
			var url = 'LaboratoryServlet';
			var params = 'method=getIndexAjax&departmentID=' + departmentID;
			var myAjax = new Ajax.Request(url, {
				method : 'post',
				parameters : params,
				onComplete : callback
			});
		}
	}
	function callback(response_text) {
		var is_text = response_text.responseText;

		$('laboratory_list_ltms').innerHTML = is_text;
		$('sort_page_ltms').innerHTML = "";
	}
</script>
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
						<form class="form" action="LaboratoryServlet" method="post" >
						<input type="hidden" name="method" value="getIndexAjax"/>
							<select class="select" id="departmentID" name="departmentID"
								>
								<option value="0">请选择学院</option>
								<%
									for (Department d : departmentList) {
								%>
								<option value="<%=d.getId()%>"><%=d.getName()%></option>
								<%
									}
								%>
							</select><input class="submit" type="submit" name="Submit" value="查询" />
						</form>
					</div>
				</div>
			</div>
			<!--内容左部结束-->
			<!--内容右部开始-->
			<div class="right">
				<div class="title">
					<p class="name">实验室&nbsp;&nbsp&nbsp;laboratory</p>
					<p class="path">
						<a href="index.jsp">首页</a><span class="sign">></span><a
							href="LaboratoryServlet?method=webList"><%=location%></a>
					</p>
				</div>
				<p class="title2">实验室一览</p>
				<img src="imgs/table_bor.png" />
				<table id="laboratory_list_ltms">
					<%for(Laboratory l : laboratoryList){%>
					<tr>
						<td class="new_title"><a href="viewLaboratory.jsp?id=<%= l.getId()%>"><%= l.getName()%></a></td>
						<td><%= nameMap.get(l.getDepartmentID())%></td>
					</tr>
					<%}%> 
				</table>
				<img src="imgs/table_bor.png" />
				<div class="page" id="sort_page_ltms"> 
					<!-- 分页-->
					<%
						String baseUrl = "LaboratoryServlet?method=webList";
					%>
					<%
						if(laboratoryList.size()>0 && laboratoryList.get(0).getDepartmentID() == 0){
						%>
						<%@ include file="page.jsp"%>
						<%
						}
					 %>
					
				</div>
			</div>
			<!--内容右部结束-->
		</div>
	</div>
	<!--内容结束-->
	<div class="clear"></div>
	<!--页脚开始-->
	<%@ include file="footer.jsp"%>
	<!--页脚结束-->
</body>
</html>
