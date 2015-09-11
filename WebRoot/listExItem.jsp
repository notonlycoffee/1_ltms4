<%@ page language="java" pageEncoding="utf-8"%>
<%@ page
	import="java.util.*, com.ltms.model.*, com.ltms.dao.*, java.sql.*"%>
<%@ include file="global.jsp"%>
<%
	ArrayList<ExItem> exItemList = (ArrayList<ExItem>)request.getAttribute("exItemList");
	Map<Integer, String> laboratoryNameMap = (Map<Integer, String>)request.getAttribute("laboratoryNameMap");
	ArrayList<Department> departmentList = (ArrayList<Department>)DepartmentDAO.list();
	String location = "实验教学";
	
	Calendar cal = Calendar.getInstance();
	int year = cal.get(Calendar.YEAR);
	String school_year_list[] = {(year - 3) + "-" + (year - 2) + "学年度",(year - 2) + "-" + (year - 1) + "学年度",(year - 1) + "-" + year + "学年度", year + "-" + (year + 1) + "学年度"};
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link rel="stylesheet" href="css/listExItemStyle.css" type="text/css" />
	<script src="<%=basepath%>/js/prototype.js" type="text/javascript"></script>
	<title><%=location + " - " + site_name%></title> <script
		type="text/javascript">
		function getLa(departmentID) {
			var url = 'LaboratoryServlet';
			var params = 'method=getAjax2&departmentID=' + departmentID;
			var myAjax = new Ajax.Request(url, {
				method : 'post',
				parameters : params,
				onComplete : callback
			});
		}

		function callback(response_text) {
			var is_text = response_text.responseText;
			$('la_ajax').innerHTML = is_text;
		}

		function submitfrm(frm) {
			if (frm.laboratoryID && frm.laboratoryID.value == "0") {
				alert("请先选择一个实验室");
				frm.laboratoryID.focus();
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
					<form class="form" action="ExItemServlet" method="post" onsubmit="return submitfrm(this);">
								<input type="hidden" name="method" value="list_obl" />
						<div id="de_list">
								<select class="select" id="departmentID" name="departmentID"
									onchange="getLa(this.value)">
									<option value="0">请选择学院</option>
									<%
										for (Department d : departmentList) {
									%>
									<option value="<%=d.getId()%>"><%=d.getName()%></option>
									<%
										}
									%>
								</select>
						</div>
						<div id="la_ajax" >
							<select id="laboratoryID" name="laboratoryID" class="select">
								<option value="0">请先选择学院</option>
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
					<p class="name">实验教学&nbsp;&nbsp;&nbsp;The experimental teaching</p>
					<p class="path">
						<a href="index.jsp">首页</a><span class="sign">></span><a
							href="listExItem.jsp"><%=location%></a>
					</p>
				</div>
				<p class="title2">实验项目一览</p>
				<img src="imgs/table_bor.png" />
				<%
					if (exItemList != null) {
				%>
				<table id="laboratory_list_ltms">
					<%
						for (ExItem ei : exItemList) {
					%>
					<tr>
						<td class="new_title"><a
							href="ExItemServlet?method=view&id=<%=ei.getId()%>&scheduleID=<%=ei.getScheduleID()%>"><%=ei.getItemName()%></a></td>
						<td><%=laboratoryNameMap.get(ei.getLaboratoryID())%></td>
					</tr>
					<%
						}
					%>
				</table>
				<img src="imgs/table_bor.png" />
				<div class="page" id="sort_page_ltms">
					<!-- 分页-->

					<%
						String baseUrl = "ExItemServlet?method=webList_more";
					%>
					<%@ include file="page.jsp"%>
				</div>
				<%
					}
				%>
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
