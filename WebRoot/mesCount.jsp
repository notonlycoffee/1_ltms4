<%@ page language="java" pageEncoding="utf-8" %>
<%@ page import="java.util.*, com.ltms.model.*, com.ltms.dao.*, java.sql.*" %>
<%@ include file="global.jsp" %>
<%  
	ArrayList<Department> departmentList = (ArrayList<Department>)DepartmentDAO.list();
	
	String location = "信息统计";
	//标记
	String sign = (String)request.getAttribute("sign");
	//实验开课记录
	Map<Integer,Integer> kaikeMap = (Map)request.getAttribute("kaikeMap");
	//实验教学项目
	Map<Integer,Integer> xiangmuMap = (Map)request.getAttribute("xiangmuMap");
	//管理制度
	Map<Integer,Integer> zhiduMap = (Map)request.getAttribute("zhiduMap");
	//实验室个数
	Map<Integer,Integer> shiyanshiMap = (Map)request.getAttribute("shiyanshiMap");
	//实验室信息数
	Map<Integer,Integer> xinxiMap = (Map)request.getAttribute("xinxiMap");
	
	Calendar cal = Calendar.getInstance(); 
	int year = cal.get(Calendar.YEAR);
	String school_year_list[] = {(year - 3) + "-" + (year - 2) + "学年度",(year - 2) + "-" + (year - 1) + "学年度",(year - 1) + "-" + year + "学年度", year + "-" + (year + 1) + "学年度"};
	
	String term_ = (String)request.getAttribute("term_");
	
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link rel="stylesheet" href="css/mesCountStyle.css" type="text/css"/>
	<script src="js/jquery.min.js"></script>
	<script src="js/jquery.easing.min.js"></script>
	<script src="js/base.js"></script>
	<script src="<%=basepath%>/js/prototype.js" type="text/javascript"></script>
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
						<p>信息统计</p>
					</div>
					<div class="left_cho">
						<form class="form" action="DbfServlet" method="post" onsubmit="return submitfrm(this);">
						<input type="hidden" name="method" value="mesCount" />
							<select class="select" name="year_weblist">
								<option value="0">选择学年</option>
								<option value="<%=school_year_list[0]%>"><%=school_year_list[0]%></option>
								<option value="<%=school_year_list[1]%>"><%=school_year_list[1]%></option>
								<option value="<%=school_year_list[2]%>"><%=school_year_list[2]%></option>
								<option value="<%=school_year_list[3]%>"><%=school_year_list[3]%></option>
							</select> <select class="select" name="term_weblist">
								<option value="0">选择学期</option>
								<option value="第一学期">第一学期</option>
								<option value="第二学期">第二学期</option>
							</select> <input class="submit" type="submit" name="Submit" value="查询"/>
						</form>
					</div>
				</div>
			</div>
			<!--内容左部结束-->
			<!--内容右部开始-->
			<div class="right">
				<div class="title">
					<p class="name">信息统计&nbsp;&nbsp;&nbsp;Statistical information</p>
					<p class="path">
						<a href="index.jsp">首页</a><span class="sign">></span><a
							href="mesCount.jsp"><%=location%></a>
					</p>
				</div>
				<p class="title2">${term_ }实验数据统计表</p>
				<img src="imgs/table_bor.png" />
				<% if(sign != null){ %>
				<table>
					<tr>
						<td>系别</td>
						<td>实验开课记录</td>
						<td>实验教学项目</td>
						<td>管理制度</td>
						<td>实验室个数</td>
						<td>实验室信息数</td>
					</tr>
					 <%	for(Department department : departmentList){ %>
					<tr>
						<td><%= department.getName() %></td>
						<td><%= kaikeMap.get(department.getId()) %></td>
						<td><%= xiangmuMap.get(department.getId()) %></td>
						<td><%= zhiduMap.get(department.getId()) %></td>
						<td><%= shiyanshiMap.get(department.getId()) %></td>
						<td><%= xinxiMap.get(department.getId()) %></td>
					</tr>
					<% }%>  
				</table>
				<%} %>
				<img src="imgs/table_bor.png" />
			</div>
			<!--内容右部结束-->
		</div>
	</div>
	<!--内容结束-->
	<!--页脚开始-->
	<%@ include file="footer.jsp" %>
	<!--页脚结束-->
</body>
</html>
