<%@ page language="java" import="java.util.*, com.ltms.util.StringUtil, com.ltms.model.*, java.sql.*,com.ltms.dao.*" pageEncoding="utf-8"%>
<%
	String basepath = request.getContextPath();
	List<Schedule> scheduleList = (ArrayList<Schedule>)request.getAttribute("scheduleList");	
	Map<String, String> classNameMap = (HashMap<String, String>)request.getAttribute("classNameMap");
	Map<Integer, String> courseNameMap = (HashMap<Integer, String>)request.getAttribute("courseNameMap");
	if(scheduleList == null || classNameMap == null || courseNameMap == null){
		request.getRequestDispatcher("ScheduleServlet?method=list").forward(request, response);
		return;
	}
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link rel="stylesheet" type="text/css" href="css/global.css" />
<script src="js/jquery.easing.min.js"></script>
<script src="js/base.js"></script>
</head>
<body>
<div id="position">您现在所在页面：实验室管理&nbsp;&gt;&gt;&nbsp;<a href="ScheduleServlet?method=list" target="content">教学进度表管理</a></div>
<div id="content" style="text-align:left">
<script type="text/javascript">
String.prototype.trim = function(){ return this.replace(/(^\s*)|(\s*$)/g, "");}
function ShowErrMsg(Info)
{
	$("#showMsg").html(Info);
}
function submitfrm(frm)
{
	if(frm.keyword && frm.keyword.value.trim() == ""){
		ShowErrMsg("请输入搜索关键字");
		frm.keyword.focus();
		return false;
	}
	return true;
}
</script>
<%@ include file="showMsg.jsp" %>
<!--  
<form action="ScheduleServlet" method="post" onsubmit="return submitfrm(this);" >
<input type="hidden" name="method" value="search" />
<table cellspacing=0 cellpadding=1 >
  	<tr>
  		<th>关键字</th>
  		<th>操作</th>
  	</tr>
    <tr>
   		<td><input type="text" name="keyword" /></td>
    	<td><input type="submit" value="查询" class="form_btn"/></td>
    </tr>
</table>
</form>-->
<!-- 修改  删除   //////////////////////////////////////////////////////////////////////////////////////////////////  -->
<table cellspacing=0 cellpadding=0 >
  	<tr>
  		<th>课程名称</th>
  		<th>班级</th>
  		<th>学期</th>
  		<th>状态</th>
  		<th>操作</th>
  	</tr>
    <%for(Schedule schedule : scheduleList){
    	Set<String> classid_set = new HashSet<String>();
    	//System.out.println("sss" + schedule.getClassID());
    	//System.out.println("sss111" + schedule.getExperimentID());
    	
    	String[] classID = (schedule.getClassID()).split("@@");
    	int classId_ ;
    	if(schedule.getClassID().endsWith("theendofthestring")){
    		classId_ = (classID.length)-1;
    	}else{
    		classId_ = classID.length;
    	}
		for(int i=0; i<classId_; i++){
			classid_set.add(classID[i]);
		}
		
		//Set set =  courseNameMap.keySet();
		//Iterator it = set.iterator();
		//System.out.print("length is " + set.size());
		//while(it.hasNext()){
			//String key = (String)it.next();
		//	System.out.println("key is " + key);
			//System.out.println("value is " + courseNameMap.get(key));
			
		//}
		
    	%>
    	<tr class="exTr">
   		 	<td><%= courseNameMap.get(schedule.getExperimentID())%></td>
    	    <td><%= classid_set%></td>
    	    <td><%= schedule.getTerm() %></td>
    	    <td><% if(schedule.getTechWeek() == 0 || schedule.getWeekTime() == 0 || schedule.getTotalTime() == 0 || schedule.getTheoTime() == 0 || schedule.getExTime() == 0){%>未完成<%}else { %>已完成<%} %></td>
    	    <td style="width:120px;">
    	    <input type="button" value="查看" onclick="javascript:window.open('ScheduleServlet?method=export_pdf&id=<%= schedule.getId()%>')" class="form_btn"/>
    	    <input type="button" value="修改" onclick="javascript:window.location.href='ScheduleServlet?method=edit&id=<%= schedule.getId()%>'" class="form_btn"/>
			</td>
    	</tr>	
	<% }%>
</table>  
<!-- 分页--> 	
<% String baseUrl = "ScheduleServlet?method=list"; %>
<%@ include file="page.jsp" %>
</div>
<div class="ps_foot">
<strong>注：</strong><br/>
查看PDF或下载PDF教学进度表请点击查看按钮，修改进度表请点击修改按钮<br/>
</div>	
</body>
</html>