<%@ page language="java"
	import="java.util.*, com.ltms.model.*, java.sql.*,com.ltms.dao.*"
	pageEncoding="utf-8"%>
<%@ include file="global.jsp"%>
<%  
	String syllabusString = (String)request.getAttribute("syllabusString");
	List<Laboratory> laboratoryList = LaboratoryDAO.list();	
	List<Department> departmentList = DepartmentDAO.list();
	List<Teacher> teacherList = TeacherDAO.list();
	String searched = (String)request.getAttribute("searched");
	String departmentID = request.getParameter("departmentID");
	String laboratoryID = request.getParameter("laboratoryID");
	String location = "实验室课表";
	
	Calendar cal = Calendar.getInstance();
	int year = cal.get(Calendar.YEAR);
	String school_year_list[] = {(year - 3) + "-" + (year - 2) + "学年度",(year - 2) + "-" + (year - 1) + "学年度",(year - 1) + "-" + year + "学年度", year + "-" + (year + 1) + "学年度"};
	
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link rel="stylesheet" href="css/syllabusStyle.css" type="text/css" />
	<title><%=location + " - " + site_name%></title> <script type="text/javascript" src="js/jquery.js"></script>
	<script src="js/jquery.min.js"></script>
	<script src="js/jquery.easing.min.js"></script>
	<script src="js/base.js"></script>
	<script type="text/javascript">/*
	document.getElementsByClassName("week").onclick=function(){document.getElementsByClassName("week").style.background="background:url(../imgs/active.png) bottom no-repeat;"};*/
	$(".week li").click(function(){
		$(".week li").css("background") == 'url(../imgs/active.png) bottom no-repeat;';
	});	
	//ajax部分
	function getInfo(departmentID){
		if(departmentID != 0){
			$.ajax({url:"SyllabusServlet?method=getAjax&departmentID="+departmentID,
				type:"GET",
				async:false,
				dataType:"text",
				success: function(text){
			 		$("#ajax_la").html(text);
				}
				})
		}else{
			var ilastring = "<select name='laboratoryID' id='laboratoryID' class='select'>";
			<%for(Laboratory aj_la : laboratoryList){%> 
				ilastring += "<option value='<%=aj_la.getId()%>'><%=aj_la.getName()%></option>";
			<%}%>
			ilastring += "</select>"
			document.getElementById("ajax_la").innerHTML = ilastring;
		}
	} 

		function submitfrm(frm) {
			if (frm.laboratoryID && frm.laboratoryID.value == "0") {
				alert("请选择一个实验室后再点击查询");
				frm.departmentID.focus();
				return false;
			}
			return true;
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
						<p>按实验室查看</p>
					</div>
					<div class="left_cho">
						<form class="form" action="SyllabusServlet" method="post"
							onsubmit="return submitfrm(this);">
							<input type="hidden" name="method" value="weblist" /><select
								class="select" name="departmentID" id="departmentID"
								onchange="getInfo(this.value)">
								<option value="0">请选择学院</option>
								<%
									for (Department d : departmentList) {
								%>
								<option value="<%=d.getId()%>"><%=d.getName()%></option>
								<%
									}
								%>
								</select> 
								<div id="ajax_la">
									<select class="select" name="laboratoryID" id="laboratoryID">
										<%
											for (Laboratory l : laboratoryList) {
										%>
										<option value="<%=l.getId()%>"><%=l.getName()%></option>
										<%
											}
										%>
									</select>
								</div>
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
							</select> <input class="submit" type="submit" name="Submit" value="查询" />
						</form>
					</div>
				</div>
			</div>
			<!--内容左部结束-->
			<!--内容右部开始-->
			<div class="right">
				<div class="title">
					<p class="name">实验室课表&nbsp;&nbsp;&nbsp;The lab schedule</p>
					<p class="path">
						<a href="index.jsp">首页</a><span class="sign">></span><a
							href="syllabus.jsp"><%=location%></a>
					</p>
				</div>
				<p class="title2">实验项目一览</p>
				<img src="imgs/table_bor.png" />
				 
				<div class="clear"></div>
				<%= syllabusString == null ? "" : syllabusString %>
				<!--
				<table border="0" cellspacing="0" cellpadding="0">
					<tr>
						<th class="new_title">一，二节</th>
						<th class="new_title">三，四节</th>
						<th class="new_title">五，六节</th>
						<th class="new_title">七，八节</th>
						<th class="last_title">九，十节</th>
					</tr>
					<tr>
						<td>菜品创作实验</td>
						<td>菜品创作实验</td>
						<td>菜品创作实验</td>
						<td>菜品创作实验</td>
						<td class="last">菜品创作实验</td>
					</tr>
					<tr>
						<td>中餐演示室</td>
						<td>中餐演示室</td>
						<td>中餐演示室</td>
						<td>中餐演示室</td>
						<td class="last">中餐演示室</td>
					</tr>
					<tr>
						<td>20124781</td>
						<td>20124781</td>
						<td>20124781</td>
						<td>20124781</td>
						<td class="last">20124781</td>
					</tr>
					<tr>
						<td>第1周-第9周</td>
						<td>第1周-第9周</td>
						<td>第1周-第9周</td>
						<td>第1周-第9周</td>
						<td class="last">第1周-第9周</td>
					</tr>
				</table> -->
				<img src="imgs/table_bor.png" />
			</div>
			<!--内容右部结束-->
		</div>
	</div>
	<!--内容结束-->
	<div class="clear"></div>
	<!--页脚开始-->
	<%@ include file="footer.jsp" %>
	<!--页脚结束-->
	<script type="text/javascript"> 
<%if(departmentID != null){%>
	getInfo(<%=departmentID%>);
	$("#departmentID").val("<%=departmentID%>");
	$("#laboratoryID").val("<%=laboratoryID%>");
<%}%>
</script>
</body>
</html>
