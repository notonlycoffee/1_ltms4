<%@ page language="java" import="java.util.*, com.ltms.model.*, java.sql.*,com.ltms.dao.*" pageEncoding="utf-8"%>
<%
	int role = ((Admin)request.getSession().getAttribute("admin")).getRole();
	int departmentID = ((Admin)request.getSession().getAttribute("admin")).getDepartmentID();
	Experiment experiment = (Experiment)request.getAttribute("experiment");
	Teacher teacher = (Teacher)request.getAttribute("teacher");
	HashSet<String> class_id = (HashSet)request.getAttribute("class_id");
	HashSet<String> class_name = (HashSet)request.getAttribute("class_name");
	Schedule schedule = (Schedule)request.getAttribute("schedule");
	List<ExItem> exItemList = (ArrayList<ExItem>)request.getAttribute("exItemList");
	String laboratoryName = (String)request.getAttribute("laboratoryName");
	String requirement = (String)request.getAttribute("requirement");
	String type = (String)request.getAttribute("type");
	String term = (String)request.getAttribute("term");
	System.out.println("length is " + exItemList.size());
	Map<Integer,String> typeMap = (Map)request.getAttribute("typeMap");
	String type_sy[] = {"演示", "验证", "综合", "设计"};
	String department = (String)request.getAttribute("department");
	String tGrade[] = {"一", "二", "三", "四"};
	String tTerm [] = {"1", "2", "3", "4", "5", "6", "7", "8"};
	String demand[] = {"考试", "考查"};
	List<Schedule> scheduleList = ScheduleDAO.list(schedule.getTeacherID());
	Map<Integer, String> nameMap = CourseDAO.getNameMap(scheduleList);
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link rel="stylesheet" type="text/css" href="css/global.css" />
<link rel="stylesheet" type="text/css" href="css/schedule.css" />
<link rel="stylesheet" href="css/jquery-ui.css" />
<script src="js/jquery.min.js"></script>
<script src="js/jquery.easing.min.js"></script>
<script src="js/base.js"></script>
<script src="js/jquery-1.9.1.js"></script>
<script src="js/jquery-ui-1.10.2.js"></script>
</head>


<body>
<div id="position">您现在所在页面：实验室管理&nbsp;&gt;&gt;&nbsp;<a href="ScheduleServlet?method=list" target="content">教学进度表管理</a>&nbsp;&gt;&gt;&nbsp;修改教学进度表</div>
<div class="scheduleContent">
<script type="text/javascript">
String.prototype.trim = function(){ return this.replace(/(^\s*)|(\s*$)/g, "");}
function ShowErrMsg(Info)
{
	document.getElementById("showMsg").innerHTML = Info;
}
function ValidateNumber(e, pnumber){
	if (!/^\d+$/.test(pnumber)){
	$(e).val(/^\d+/.exec($(e).val()));
	}
	return false;
}
function submitfrm(frm){
	if($("#xss").val() == 0){
		ShowErrMsg("学生数必填且不为0");
		alert("学生数必填且不为0");
		return false;
	}
	if($("#fzs").val() == 0){
		ShowErrMsg("分组数必填且不为0");
		alert("分组数必填且不为0");
		return false;
	}
	if($("#zouxss").val() == 0){
		ShowErrMsg("周学时数必填且不为0");
		alert("周学时数必填且不为0");
		return false;
	}
	if($("#jxzs").val() == 0){
		ShowErrMsg("教学周数必填且不为0");
		alert("教学周数必填且不为0");
		return false;
	}
	if($("#zongxss").val() == 0){
		ShowErrMsg("总学时数必填且不为0");
		alert("总学时数必填且不为0");
		return false;
	}
	if($("#syss").val() == 0){
		ShowErrMsg("实验时数必填且不为0");
		alert("实验时数必填且不为0");
		return false;
	}
}
</script>

<%@ include file="showMsg.jsp" %>
<div style="width:900px;">
<span class="schedule_title">
	韩山师范学院&nbsp;<span style="text-decoration:underline;"><%= department%></span>
</span>
<span class="schedule_title">
	<span style="text-decoration:underline;"><%= term%></span>实验教学进度计划表
</span>
</div>
<form action="ScheduleServlet" method="post" onsubmit="return submitfrm(this);">
<input type="hidden" name="method" value="modify" />
<input type="hidden" name="id" value="<%= request.getParameter("id")%>" />
<table cellspacing=0 cellpadding=1 id="schedule_table">
	<tr>
		<td class="sc_td1">实验室</td><td class="sc_td2"><%= laboratoryName%></td>
		<td class="sc_td1">课程名称</td><td class="sc_td2"><%= nameMap.get(schedule.getExperimentID())%></td>
		<td class="sc_td1">授课专业</td><td class="sc_td2"><%=class_name%></td>
	</tr>
	<tr>
		<td>班级</td><td><%= class_id%></td>
		<td class="required_td">学生数</td><td><input type="text"  id="xss"  name="student_count" onkeyup="return ValidateNumber($(this),value)" size="2" value="<%= ExperimentDAO.loadNumber(schedule.getId())%>" style="height:40px;line-height:40px;width:99%;border:none;text-align:center;" /></td>
		<td class="required_td">分组数</td><td><input type="text" id="fzs" name="groupNum" onkeyup="return ValidateNumber($(this),value)" size="2" value="<%= schedule.getGroupNum()%>" style="height:40px;line-height:40px;width:99%;border:none;text-align:center;" /></td>
	</tr>
	<tr>
		<td colspan="2">实验要求(必修\选修\其他)</td><td colspan="4"><%= requirement %></td>
	</tr>
	<tr>
		<td class="required_td">实验目的要求</td>
		<td colspan="5"><textarea name="purpose" cols="50" rows="10" style="width:99%;height:160px;border:none;" ><%= schedule.getPurpose()%></textarea></td>
	</tr>
	<tr>
		<td class="required_td">教学安排</td>
		<td colspan="2" style="padding:0;border:none;">
		<table cellspacing=0 cellpadding=1 id="jxap_table">
			<tr>
				<td style="width:25%;">开设年级</td>
				<td style="width:25%;">
				<select name="tGrade" class="jxap_table_td">
					<% for(int i=0; i<tGrade.length; i++){%>	
						<option value="<%= tGrade[i]%>"  <% if(tGrade[i].equals(schedule.gettGrade())) out.println("selected");%>><%= tGrade[i]%></option>
					<% }%>
				</select>
				</td>
				<td style="width:25%;">开设学期</td>
				<td style="width:25%;">
				<select name="tTerm" class="jxap_table_td">
					<% for(int i=0; i<tTerm.length; i++){%>	
						<option value="<%= tTerm[i]%>"  <% if(tTerm[i].equals(schedule.gettTerm())) out.println("selected");%>><%= tTerm[i]%></option>
					<% }%>
				</select>
				</td>
			</tr>
			<tr>
				<td>考试/考查</td>
				<td>
				<select name="demand" class="jxap_table_td">
				<% for(int i=0; i<demand.length; i++){%>	
					<option value="<%= demand[i]%>"  <% if(demand[i].equals(schedule.getDemand())) out.println("selected");%>><%= demand[i]%></option>
				<% }%>
			</select>
				</td>
				<td>教学周数</td>
				<td><input type="text" id="jxzs" onkeyup="return ValidateNumber($(this),value)" name="techWeek" value="<%= schedule.getTechWeek()%>" class="jxap_table_td" /></td>
			</tr>
			<tr>
				<td>周学时数</td><td><input type="text"  id="zouxss" onkeyup="return ValidateNumber($(this),value)"name="weekTime" value="<%= schedule.getWeekTime()%>" class="jxap_table_td" /></td>
				<td>总学时数</td><td><input type="text" id="zongxss" onkeyup="return ValidateNumber($(this),value)"name="totalTime" value="<%= schedule.getTotalTime()%>" class="jxap_table_td" /></td>
			</tr>
			<tr>
				<td>理论时数</td><td><input type="text" onkeyup="return ValidateNumber($(this),value)"name="theoTime" value="<%= schedule.getTheoTime()%>" class="jxap_table_td" /></td>
				<td>实验时数</td><td><input type="text"  id="syss" onkeyup="return ValidateNumber($(this),value)"name="exTime" value="<%= schedule.getExTime()%>" class="jxap_table_td" /></td>
			</tr>
		</table>
		</td>
		<td class="required_td">使用教材及上学期<br/>已完成的章节及内容</td>
		<td colspan="2"><textarea name="material" cols="20" rows="10" style="width:99%;height:170px;border:none;" ><%= schedule.getMaterial()%></textarea></td>
	</tr>
	<tr>
		<td class="required_td">实验项目与进度</td>
		<td colspan="5" style="padding:0;border:none;">
		<table cellspacing=0 cellpadding=1 id="syxm_table">
			<tr>
				<td style="width:5%">周次</td><td style="width:35%">实验项目名称</td><td style="width:10%">实验类型</td><td style="width:50%">实验内容</td>
			</tr>
			<tr>
				<td><input type="text" name="week1" value="<%= exItemList.get(0).getWeek()%>" class="syxm_table_td_week"/></td>
				<td><input type="text" name="item1" value="<%= exItemList.get(0).getItemName()%>" /></td>
				
				<td>
					<select name="type1" id="type1">
						<% for(int i=0; i<4; i++){%> 
							<option value="<%= type_sy[i]%>"><%= type_sy[i]%></option>
						<% }%>
					</select>
				</td>
				
				<td><input type="text" name="comment1" value="<%= exItemList.get(0).getComment()%>" /></td>
			</tr>
			<tr>
				<td><input type="text" name="week2" value="<%= exItemList.get(1).getWeek()%>" class="syxm_table_td_week"/></td>
				<td><input type="text" name="item2" value="<%= exItemList.get(1).getItemName()%>" /></td>
				
				<td>
					<select name="type2" id="type2">
						<% for(int i=0; i<4; i++){%> 
							<option value="<%= type_sy[i]%>"><%= type_sy[i]%></option>
						<% }%>
					</select>
				</td>
				
				<td><input type="text" name="comment2" value="<%= exItemList.get(1).getComment()%>" /></td>
			</tr>
			<tr>
				<td><input type="text" name="week3" value="<%= exItemList.get(2).getWeek()%>" class="syxm_table_td_week"/></td>
				<td><input type="text" name="item3" value="<%= exItemList.get(2).getItemName()%>" /></td>
				
				<td>
					<select name="type3" id="type3">
						<% for(int i=0; i<4; i++){%> 
							<option value="<%= type_sy[i]%>"><%= type_sy[i]%></option>
						<% }%>
					</select>
				</td>
				
				<td><input type="text" name="comment3" value="<%= exItemList.get(2).getComment()%>" /></td>
			</tr>
			<tr>
				<td><input type="text" name="week4" value="<%= exItemList.get(3).getWeek()%>" class="syxm_table_td_week"/></td>
				<td><input type="text" name="item4" value="<%= exItemList.get(3).getItemName()%>" /></td>
				
				<td>
					<select name="type4" id="type4">
						<% for(int i=0; i<4; i++){%> 
							<option value="<%= type_sy[i]%>"><%= type_sy[i]%></option>
						<% }%>
					</select>
				</td>
				
				<td><input type="text" name="comment4" value="<%= exItemList.get(3).getComment()%>" /></td>
			</tr>
			<tr>
				<td><input type="text" name="week5" value="<%= exItemList.get(4).getWeek()%>" class="syxm_table_td_week"/></td>
				<td><input type="text" name="item5" value="<%= exItemList.get(4).getItemName()%>" /></td>
				
				<td>
					<select name="type5" id="type5">
						<% for(int i=0; i<4; i++){%> 
							<option value="<%= type_sy[i]%>"><%= type_sy[i]%></option>
						<% }%>
					</select>
				</td>
				
				<td><input type="text" name="comment5" value="<%= exItemList.get(4).getComment()%>" /></td>
			</tr>
			<tr>
				<td><input type="text" name="week6" value="<%= exItemList.get(5).getWeek()%>" class="syxm_table_td_week"/></td>
				<td><input type="text" name="item6" value="<%= exItemList.get(5).getItemName()%>" /></td>
				
				<td>
					<select name="type6" id="type6">
						<% for(int i=0; i<4; i++){%> 
							<option value="<%= type_sy[i]%>"><%= type_sy[i]%></option>
						<% }%>
					</select>
				</td>
				
				<td><input type="text" name="comment6" value="<%= exItemList.get(5).getComment()%>" /></td>
			</tr>
			<tr>
				<td><input type="text" name="week7" value="<%= exItemList.get(6).getWeek()%>" class="syxm_table_td_week"/></td>
				<td><input type="text" name="item7" value="<%= exItemList.get(6).getItemName()%>" /></td>
				
				<td>
					<select name="type7" id="type7">
						<% for(int i=0; i<4; i++){%> 
							<option value="<%= type_sy[i]%>"><%= type_sy[i]%></option>
						<% }%>
					</select>
				</td>
				
				<td><input type="text" name="comment7" value="<%= exItemList.get(6).getComment()%>" /></td>
			</tr>
			<tr>
				<td><input type="text" name="week8" value="<%= exItemList.get(7).getWeek()%>" class="syxm_table_td_week"/></td>
				<td><input type="text" name="item8" value="<%= exItemList.get(7).getItemName()%>" /></td>
				
				<td>
					<select name="type8" id="type8">
						<% for(int i=0; i<4; i++){%> 
							<option value="<%= type_sy[i]%>"><%= type_sy[i]%></option>
						<% }%>
					</select>
				</td>
				
				<td><input type="text" name="comment8" value="<%= exItemList.get(7).getComment()%>" /></td>
			</tr>
			<tr>
				<td><input type="text" name="week9" value="<%= exItemList.get(8).getWeek()%>" class="syxm_table_td_week"/></td>
				<td><input type="text" name="item9" value="<%= exItemList.get(8).getItemName()%>" /></td>
				
				<td>
					<select name="type9" id="type9">
						<% for(int i=0; i<4; i++){%> 
							<option value="<%= type_sy[i]%>"><%= type_sy[i]%></option>
						<% }%>
					</select>
				</td>
				
				<td><input type="text" name="comment9" value="<%= exItemList.get(8).getComment()%>" /></td>
			</tr>
			<tr>
				<td><input type="text" name="week10" value="<%= exItemList.get(9).getWeek()%>" class="syxm_table_td_week"/></td>
				<td><input type="text" name="item10" value="<%= exItemList.get(9).getItemName()%>" /></td>
				
				<td>
					<select name="type10" id="type10">
						<% for(int i=0; i<4; i++){%> 
							<option value="<%= type_sy[i]%>"><%= type_sy[i]%></option>
						<% }%>
					</select>
				</td>
				
				<td><input type="text" name="comment10" value="<%= exItemList.get(9).getComment()%>" /></td>
			</tr>
			
			<% if(exItemList.size() > 11){ %>
				<!-- 添加的项 -->
			<tr>
				<td><input type="text" name="week11" value="<%= exItemList.get(10).getWeek()%>" class="syxm_table_td_week"/></td>
				<td><input type="text" name="item11" value="<%= exItemList.get(10).getItemName()%>" /></td>
				
				<td>
					<select name="type11" id="type11">
						<% for(int i=0; i<4; i++){%> 
							<option value="<%= type_sy[i]%>"><%= type_sy[i]%></option>
						<% }%>
					</select>
				</td>
				
				<td><input type="text" name="comment11" value="<%= exItemList.get(10).getComment()%>" /></td>
			</tr>
			<tr>
				<td><input type="text" name="week12" value="<%= exItemList.get(11).getWeek()%>" class="syxm_table_td_week"/></td>
				<td><input type="text" name="item12" value="<%= exItemList.get(11).getItemName()%>" /></td>
				
				<td>
					<select name="type12" id="type12">
						<% for(int i=0; i<4; i++){%> 
							<option value="<%= type_sy[i]%>"><%= type_sy[i]%></option>
						<% }%>
					</select>
				</td>
				
				<td><input type="text" name="comment12" value="<%= exItemList.get(11).getComment()%>" /></td>
			</tr>
			<tr>
				<td><input type="text" name="week13" value="<%= exItemList.get(12).getWeek()%>" class="syxm_table_td_week"/></td>
				<td><input type="text" name="item13" value="<%= exItemList.get(12).getItemName()%>" /></td>
				
				<td>
					<select name="type13" id="type13">
						<% for(int i=0; i<4; i++){%> 
							<option value="<%= type_sy[i]%>"><%= type_sy[i]%></option>
						<% }%>
					</select>
				</td>
				
				<td><input type="text" name="comment13" value="<%= exItemList.get(12).getComment()%>" /></td>
			</tr>
			<tr>
				<td><input type="text" name="week14" value="<%= exItemList.get(13).getWeek()%>" class="syxm_table_td_week"/></td>
				<td><input type="text" name="item14" value="<%= exItemList.get(13).getItemName()%>" /></td>
				
				<td>
					<select name="type14" id="type14">
						<% for(int i=0; i<4; i++){%> 
							<option value="<%= type_sy[i]%>"><%= type_sy[i]%></option>
						<% }%>
					</select>
				</td>
				
				<td><input type="text" name="comment14" value="<%= exItemList.get(13).getComment()%>" /></td>
			</tr>
			<tr>
				<td><input type="text" name="week15" value="<%= exItemList.get(14).getWeek()%>" class="syxm_table_td_week"/></td>
				<td><input type="text" name="item15" value="<%= exItemList.get(14).getItemName()%>" /></td>
				
				<td>
					<select name="type15" id="type15">
						<% for(int i=0; i<4; i++){%> 
							<option value="<%= type_sy[i]%>"><%= type_sy[i]%></option>
						<% }%>
					</select>
				</td>
				
				<td><input type="text" name="comment15" value="<%= exItemList.get(14).getComment()%>" /></td>
			</tr>
			<tr>
				<td><input type="text" name="week16" value="<%= exItemList.get(15).getWeek()%>" class="syxm_table_td_week"/></td>
				<td><input type="text" name="item16" value="<%= exItemList.get(15).getItemName()%>" /></td>
				
				<td>
					<select name="type16" id="type16">
						<% for(int i=0; i<4; i++){%> 
							<option value="<%= type_sy[i]%>"><%= type_sy[i]%></option>
						<% }%>
					</select>
				</td>
				
				<td><input type="text" name="comment16" value="<%= exItemList.get(15).getComment()%>" /></td>
			</tr>
			<tr>
				<td><input type="text" name="week17" value="<%= exItemList.get(16).getWeek()%>" class="syxm_table_td_week"/></td>
				<td><input type="text" name="item17" value="<%= exItemList.get(16).getItemName()%>" /></td>
				
				<td>
					<select name="type17" id="type17">
						<% for(int i=0; i<4; i++){%> 
							<option value="<%= type_sy[i]%>"><%= type_sy[i]%></option>
						<% }%>
					</select>
				</td>
				
				<td><input type="text" name="comment17" value="<%= exItemList.get(16).getComment()%>" /></td>
			</tr>
			<tr>
				<td><input type="text" name="week18" value="<%= exItemList.get(17).getWeek()%>" class="syxm_table_td_week"/></td>
				<td><input type="text" name="item18" value="<%= exItemList.get(17).getItemName()%>" /></td>
				
				<td>
					<select name="type18" id="type18">
						<% for(int i=0; i<4; i++){%> 
							<option value="<%= type_sy[i]%>"><%= type_sy[i]%></option>
						<% }%>
					</select>
				</td>
				
				<td><input type="text" name="comment18" value="<%= exItemList.get(17).getComment()%>" /></td>
			</tr>
			<tr>
				<td><input type="text" name="week19" value="<%= exItemList.get(18).getWeek()%>" class="syxm_table_td_week"/></td>
				<td><input type="text" name="item19" value="<%= exItemList.get(18).getItemName()%>" /></td>
				
				<td>
					<select name="type19" id="type19">
						<% for(int i=0; i<4; i++){%> 
							<option value="<%= type_sy[i]%>"><%= type_sy[i]%></option>
						<% }%>
					</select>
				</td>
				
				<td><input type="text" name="comment19" value="<%= exItemList.get(18).getComment()%>" /></td>
			</tr>
			<tr>
				<td><input type="text" name="week20" value="<%= exItemList.get(19).getWeek()%>" class="syxm_table_td_week"/></td>
				<td><input type="text" name="item20" value="<%= exItemList.get(19).getItemName()%>" /></td>
				
				<td>
					<select name="type20" id="type20">
						<% for(int i=0; i<4; i++){%> 
							<option value="<%= type_sy[i]%>"><%= type_sy[i]%></option>
						<% }%>
					</select>
				</td>
				
				<td><input type="text" name="comment20" value="<%= exItemList.get(19).getComment()%>" /></td>
			</tr>
			<%}else{ %>
				<!-- 添加的项 -->
			<tr>
				<td><input type="text" name="week11" value="" class="syxm_table_td_week"/></td>
				<td><input type="text" name="item11" value="" /></td>
				
				<td>
					<select name="type11" id="type11">
						<% for(int i=0; i<4; i++){%> 
							<option value="<%= type_sy[i]%>"><%= type_sy[i]%></option>
						<% }%>
					</select>
				</td>
				
				<td><input type="text" name="comment11" value="" /></td>
			</tr>
			<tr>
				<td><input type="text" name="week12" value="" class="syxm_table_td_week"/></td>
				<td><input type="text" name="item12" value="" /></td>
				
				<td>
					<select name="type12" id="type12">
						<% for(int i=0; i<4; i++){%> 
							<option value="<%= type_sy[i]%>"><%= type_sy[i]%></option>
						<% }%>
					</select>
				</td>
				
				<td><input type="text" name="comment12" value="" /></td>
			</tr>
			<tr>
				<td><input type="text" name="week13" value="" class="syxm_table_td_week"/></td>
				<td><input type="text" name="item13" value="" /></td>
				
				<td>
					<select name="type13" id="type13">
						<% for(int i=0; i<4; i++){%> 
							<option value="<%= type_sy[i]%>"><%= type_sy[i]%></option>
						<% }%>
					</select>
				</td>
				
				<td><input type="text" name="comment13" value="" /></td>
			</tr>
			<tr>
				<td><input type="text" name="week14" value="" class="syxm_table_td_week"/></td>
				<td><input type="text" name="item14" value="" /></td>
				
				<td>
					<select name="type14" id="type14">
						<% for(int i=0; i<4; i++){%> 
							<option value="<%= type_sy[i]%>"><%= type_sy[i]%></option>
						<% }%>
					</select>
				</td>
				
				<td><input type="text" name="comment14" value="" /></td>
			</tr>
			<tr>
				<td><input type="text" name="week15" value="" class="syxm_table_td_week"/></td>
				<td><input type="text" name="item15" value="" /></td>
				
				<td>
					<select name="type15" id="type15">
						<% for(int i=0; i<4; i++){%> 
							<option value="<%= type_sy[i]%>"><%= type_sy[i]%></option>
						<% }%>
					</select>
				</td>
				
				<td><input type="text" name="comment15" value="" /></td>
			</tr>
			<tr>
				<td><input type="text" name="week16" value="" class="syxm_table_td_week"/></td>
				<td><input type="text" name="item16" value="" /></td>
				
				<td>
					<select name="type16" id="type16">
						<% for(int i=0; i<4; i++){%> 
							<option value="<%= type_sy[i]%>"><%= type_sy[i]%></option>
						<% }%>
					</select>
				</td>
				
				<td><input type="text" name="comment16" value="" /></td>
			</tr>
			<tr>
				<td><input type="text" name="week17" value="" class="syxm_table_td_week"/></td>
				<td><input type="text" name="item17" value="" /></td>
				
				<td>
					<select name="type17" id="type17">
						<% for(int i=0; i<4; i++){%> 
							<option value="<%= type_sy[i]%>"><%= type_sy[i]%></option>
						<% }%>
					</select>
				</td>
				
				<td><input type="text" name="comment17" value="" /></td>
			</tr>
			<tr>
				<td><input type="text" name="week18" value="" class="syxm_table_td_week"/></td>
				<td><input type="text" name="item18" value="" /></td>
				
				<td>
					<select name="type18" id="type18">
						<% for(int i=0; i<4; i++){%> 
							<option value="<%= type_sy[i]%>"><%= type_sy[i]%></option>
						<% }%>
					</select>
				</td>
				
				<td><input type="text" name="comment18" value="" /></td>
			</tr>
			<tr>
				<td><input type="text" name="week19" value="" class="syxm_table_td_week"/></td>
				<td><input type="text" name="item19" value="" /></td>
				
				<td>
					<select name="type19" id="type19">
						<% for(int i=0; i<4; i++){%> 
							<option value="<%= type_sy[i]%>"><%= type_sy[i]%></option>
						<% }%>
					</select>
				</td>
				
				<td><input type="text" name="comment19" value="" /></td>
			</tr>
			<tr>
				<td><input type="text" name="week20" value="" class="syxm_table_td_week"/></td>
				<td><input type="text" name="item20" value="" /></td>
				
				<td>
					<select name="type20" id="type20">
						<% for(int i=0; i<4; i++){%> 
							<option value="<%= type_sy[i]%>"><%= type_sy[i]%></option>
						<% }%>
					</select>
				</td>
				
				<td><input type="text" name="comment20" value="" /></td>
			</tr>
			<%} %>
		</table>
		</td>
	</tr>
	<tr>
		<td>操作</td>
		<td colspan="5" >
		<input type="submit" name="method" value="修改" class="form_btn"/>
  		<input type="button" value="返回教学进度表管理" onclick="javascript:window.location.href='ScheduleServlet?method=list'" class="form_btn2"/>
		</td>
	</tr>
</table>
</form>
<span>项目名称可以填写<font color="red">50</font>个字。实验目的要求可以填写<font color="red">200</font>个字。
教材内容可以填写<font color="red">200</font>个字。实验内容可以填写<font color="red">100</font>个字。</span>

<form action="ScheduleServlet" method="post">
<input type="hidden" name="method" value="update_from_existence" />
<input type="hidden" name="id" value="<%= schedule.getId()%>" />
<table cellspacing=0 cellpadding=1 >
	<tr>
		<th>已存在进度表</th>
		<th>操作</th>
	</tr>
	<tr>
  		<td>
		<select name="exist_id" id="exist_id">
			<%	for(Schedule ex_s : scheduleList){ %>
			<option value="<%= ex_s.getId()%>"><%= nameMap.get(ex_s.getExperimentID()) + "(" + ex_s.getTerm() + ")"%></option>
			<% } %>
		</select>
		</td>
  		<td><input type="submit" value="导入数据" class="form_btn2"/></td>
	</tr>
</table>
</form>

</div> <!-- scheduleContent 结束 -->
<div class="ps_foot">
<strong>注：</strong><br/>
1.仅高亮部分可修改<br/>
2.可从已有进度表导入可修改部分数据<br/>
</div>	

<script type="text/javascript">
	$("#type1").val("<%= typeMap.get(exItemList.get(0).getId()).trim()%>");
	$("#type2").val("<%= typeMap.get(exItemList.get(1).getId())%>");
	$("#type3").val("<%= typeMap.get(exItemList.get(2).getId())%>");
	$("#type4").val("<%= typeMap.get(exItemList.get(3).getId())%>");
	$("#type5").val("<%= typeMap.get(exItemList.get(4).getId())%>");
	$("#type6").val("<%= typeMap.get(exItemList.get(5).getId())%>");
	$("#type7").val("<%= typeMap.get(exItemList.get(6).getId())%>");
	$("#type8").val("<%= typeMap.get(exItemList.get(7).getId())%>");
	$("#type9").val("<%= typeMap.get(exItemList.get(8).getId())%>");
	$("#type10").val("<%= typeMap.get(exItemList.get(9).getId())%>");
	<% if(exItemList.size() > 11){ %>
		$("#type11").val("<%= typeMap.get(exItemList.get(10).getId()).trim()%>");
		$("#type12").val("<%= typeMap.get(exItemList.get(11).getId())%>");
		$("#type13").val("<%= typeMap.get(exItemList.get(12).getId())%>");
		$("#type14").val("<%= typeMap.get(exItemList.get(13).getId())%>");
		$("#type15").val("<%= typeMap.get(exItemList.get(14).getId())%>");
		$("#type16").val("<%= typeMap.get(exItemList.get(15).getId())%>");
		$("#type17").val("<%= typeMap.get(exItemList.get(16).getId())%>");
		$("#type18").val("<%= typeMap.get(exItemList.get(17).getId())%>");
		$("#type19").val("<%= typeMap.get(exItemList.get(18).getId())%>");
		$("#type20").val("<%= typeMap.get(exItemList.get(19).getId())%>");
	<%} %>
</script>

</body>
</html>