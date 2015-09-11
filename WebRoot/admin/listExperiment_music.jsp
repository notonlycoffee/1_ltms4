<%@ page language="java" import="java.util.*, com.ltms.model.*, java.sql.*,com.ltms.dao.*" pageEncoding="utf-8"%>
<%
	Teacher teacher = (Teacher)request.getSession().getAttribute("teacher");
	int departmentID = teacher.getUnitID();
	String teacherName = teacher.getName();
	List<Experiment> experimentList = (ArrayList<Experiment>)request.getAttribute("experimentList");	
	if(experimentList == null){
		request.getRequestDispatcher("ExperimentServlet?method=list_music&teacherID="+teacher.getId()).forward(request, response);
		return;
	}
	List<Laboratory> laboratoryList = LaboratoryDAO.list(departmentID);	
	List<Department> departmentList = (ArrayList<Department>)session.getAttribute("departmentList");	
	if(departmentList == null){
		departmentList = DepartmentDAO.list();
		session.setAttribute("departmentList", departmentList);
	}
	String weekday[] = {"周一", "周二", "周三", "周四", "周五"};
	String everyWeek[] = {"每周", "单周", "双周"};
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
<div id="position">您现在所在页面：实验室管理&nbsp;&gt;&gt;&nbsp;<a href="ExperimentServlet?method=list_music&teacherID=<%= teacher.getId()%>" target="content">实验管理</a>
<span id="addInfo"><a href="addExperiment_music.jsp">添加实验信息</a></span></div>

<div id="content" style="text-align:left">
<script type="text/javascript">
String.prototype.trim = function(){ return this.replace(/(^\s*)|(\s*$)/g, "");}
function ShowErrMsg(Info)
{
	$("#showMsg").html(Info);
}

function submitfrm(frm)
{
	if(Number(frm.startTime.value) > Number(frm.endTime.value)){
		ShowErrMsg("开始节数不能大于结束节数，请重新输入");
		return false;
	}
	if(frm.laboratoryID && frm.laboratoryID.value == "0"){
		alert("请先选择一个实验室");
		frm.laboratoryID.focus();
		return false;
	}
	return true;
}

function getLa(departmentID){
	$.ajax({url:"LaboratoryServlet?method=getAjax&departmentID='+departmentID,
		type:"GET",
		async:false,
		dataType:"text",
		success: function(text){
	 		$("#la_ajax").html(text);
		}
		})
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
<table cellspacing=0 cellpadding=0 >
	<tr>
  		<th>课程</th>
  		<th>实验地点</th>
  		<th>班级</th>
  		<th>时间</th>
  		<th>学期</th>
  		<th>操作</th>
  	</tr>
   	<%= ExperimentDAO.printList2(experimentList)%>
</table>  

<!-- 分页--> 	
<% String baseUrl = "ExperimentServlet?method=list_music&teacherID="+teacher.getId(); %>
<%@ include file="page.jsp" %>
</div>
</body>
</html>