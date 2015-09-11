<%@ page language="java" import="java.util.*, com.ltms.model.*, java.sql.*,com.ltms.dao.*" pageEncoding="utf-8"%>
<%
	int id = Integer.parseInt(request.getParameter("id"));
	Experiment experiment = ExperimentDAO.load(id);
	String teacherInfo[] = experiment.getTeacherInfo().split(",");
	ArrayList<String> nameArray = TeacherDAO.getNameArray(teacherInfo);
	String sylb[] = {"基础", "专业基础", "专业", "其他", "课程设计", "毕业论文", "毕业设计", "科研", "社会开发", "社会服务"};
	String syzlb[] = {"博士生", "硕士生", "本科生", "专科生", "其他"};
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link rel="stylesheet" type="text/css" href="css/global.css" />
<script src="js/jquery-1.9.1.js"></script>
<script src="js/jquery-ui-1.10.2.js"></script>
</head>
<body>
<div id="position">您现在所在页面：实验室管理&nbsp;&gt;&gt;&nbsp;实验管理&nbsp;&gt;&gt;&nbsp;查看实验信息</div>
<div class="detailInfoContent">
<script type="text/javascript">
String.prototype.trim = function(){ return this.replace(/(^\s*)|(\s*$)/g, "");}
function ShowErrMsg(Info)
{
	document.getElementById("showMsg").innerHTML = Info;
}
</script>
<%@ include file="showMsg.jsp" %>
<form action="ExperimentServlet" method="post" onsubmit="return submitfrm3(this);" >
<input type="hidden" name="id" value="<%=experiment.getId()%>" />
<table cellspacing=0 cellpadding=0>
  	<tr>
  		<td class="TD_Left">课程</td>
  		<td class="TD_Right"><%= experiment.getCourseID() + " " + CourseDAO.getName(experiment.getCourseID())%></td>
  	</tr>
  	<tr>
  		<td class="TD_Left">系别</td>
  		<td class="TD_Right"><%=DepartmentDAO.getName(experiment.getDepartmentID())%></td>
  	</tr>
  	<tr>
  		<td class="TD_Left">实验地点</td>
  		<td class="TD_Right"><%= LaboratoryDAO.getName(experiment.getLaboratoryID())%></td>
	</tr>
  	<tr>
  		<td class="TD_Left">实验时间</td>
		<td class="TD_Right">
		<%	String time[] = experiment.getTimeInfo().split("@");
			int count = 1;
			for(String tss : time){
				if(count!=1){ %>
				<br/>
		<% 		}
				String week[] = tss.split(",");
				int week_length = week.length;
				if(week[0].equals("0")){ %>
				<%= "第" + week[1] + "周 - 第" + week[2] + "周(" + week[3] + ")" %>
				<%	}else{ 
					String week_string = "第";
					for(int i=1; i<week_length-4; i++){
						week_string += week[i];
						if(i != week_length - 5){
							week_string += "、";
						}
					}
					week_string += "周";
				%>
				<%= week_string%>
				<%	}%>
				<%=  week[week_length-3] + "&nbsp;第" + String.format("%02d", Integer.parseInt(week[week_length-2])) + "节&nbsp;-&nbsp;第" + String.format("%02d", Integer.parseInt(week[week_length-1])) + "节" %>
		<%		count++;
				} %>
		</td>
	</tr>
  	<tr>
  		<td class="TD_Left">班级</td>
  		<td class="TD_Right">
  		<span id="classInfo_td_ltms"></span>
  		</td>
	</tr>
  	<tr>
  		<td class="TD_Left">教师</td>
		<td class="TD_Right">
		<%	String ttString = "";
		for(String tts : nameArray){
			ttString += tts + " ";
		}
		%>
		<%= ttString%>
		</td>
	</tr>
  	<tr>
		<td class="TD_Left">实验要求</td>
		<td class="TD_Right"><%= experiment.getRequirement()%></td>
	</tr>
	 <!-- 新增加的项 -->
	<tr>
		<td class="TD_Left">实验类别</td>
		<td class="TD_Right">  
		<% if(experiment.getSylb() == 1){ %> 基础<%} %>
		<% if(experiment.getSylb() == 2){ %> 专业基础<%} %>
		<% if(experiment.getSylb() == 3){ %> 专业<%} %>
		<% if(experiment.getSylb() == 4){ %> 其他<%} %>
		<% if(experiment.getSylb() == 5){ %> 课程设计<%} %>
		<% if(experiment.getSylb() == 6){ %> 毕业论文<%} %>
		<% if(experiment.getSylb() == 7){ %> 毕业设计<%} %>
		<% if(experiment.getSylb() == 8){ %> 科研<%} %>
		<% if(experiment.getSylb() == 9){ %> 社会开发<%} %>
		<% if(experiment.getSylb() == 10){ %> 社会服务<%} %>
		</td>
	</tr>
	
	<!-- 新增加的项 -->
	<tr>
		<td class="TD_Left">学时</td>
		<td class="TD_Right">  
	  		<%=experiment.getXs() %>
		</td>
	</tr>
	
	 <!-- 新增加的项 -->
	<tr>
		<td class="TD_Left">实验者类别</td>
		<td class="TD_Right">  
			<% if(experiment.getSyzlb() == 1){ %>博士生<%} %>
			<% if(experiment.getSyzlb() == 2){ %>硕士生<%} %>
			<% if(experiment.getSyzlb() == 3){ %>本科生<%} %>
			<% if(experiment.getSyzlb() == 4){ %>专科生<%} %>
			<% if(experiment.getSyzlb() == 5){ %>其他<%} %>
		</td>
	</tr>
  	<tr>
  		<td class="TD_Left">操作</td>
  		<td class="TD_Right"><input type="button" value="返回" onclick="javascript:history.go(-1)" class="form_btn" /></td>
  	</tr>
</table> 
</form> 
</div>
<script type="text/javascript">
var classInfo = "<%= experiment.getClassInfo()%>".split("@");
var class_info_td_string = "";
for(var i=0; i<classInfo.length-1; i++){
	class_info_td_string += classInfo[i++] + (classInfo[i] != "" ? "(" + classInfo[i] + ")<br/>" : "<br/>");
}
$("#classInfo_td_ltms").html(class_info_td_string);
</script>
</body>
</html>