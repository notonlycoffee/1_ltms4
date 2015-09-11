<%@ page language="java" import="java.util.*, com.ltms.model.*, java.sql.*" pageEncoding="utf-8"%>
<%
	int role = ((Admin)session.getAttribute("admin")).getRole();  // 0教务处 1教务员 2教师
	String id = ((Admin)session.getAttribute("admin")).getId();
	int departmentID = ((Admin)session.getAttribute("admin")).getDepartmentID();
	// 将过期日期设置为一个过去时间 
	response.setHeader("Expires", "Sat, 6 May 1995 12:00:00 GMT"); 
	// 设置 HTTP/1.1 no-cache 头 
	response.setHeader("Cache-Control", "no-store,no-cache,must-revalidate"); 
	// 设置 IE 扩展 HTTP/1.1 no-cache headers， 用户自己添加 
	response.addHeader("Cache-Control", "post-check=0, pre-check=0"); 
	// 设置标准 HTTP/1.0 no-cache header. 
	response.setHeader("Pragma", "no-cache"); 
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>实验室教学管理系统</title>
<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
<link rel="stylesheet" type="text/css" href="css/global.css" />
<script language=javascript>
function expand(el)
{
	childObj = document.getElementById("child" + el);

	if (childObj.style.display == 'none')
	{
		childObj.style.display = 'block';
	}
	else
	{
		childObj.style.display = 'none';
	}
	return;
}
</script>
</head>
<body>
<center>
<div id="nav">
<ul style="padding-top:3px;">	
	<li class="menu"><a href="javascript:void(0);" target="content" onclick=expand(1) >实验室管理</a></li>
	<li id=child1 style="">
	<ul>
		<li><a class="child" href="blank.html" target="content">首页</a></li>
		
		<% if(role == 4){ %>
		<li><a class="child" href="admin_glygl.jsp" target="content">实验室管理员管理</a></li>
		<% } %>
		
		<% if(role == 5 || role == 0){ %>
		<!-- li><a class="child" href="LaboratoryServlet?method=gly_glsys" target="content">配置实验室</a></li-->
			<% if(role == 5){ %>
				<li><a class="child" href="editSysryqk.jsp" target="content">管理员基本情况</a></li>
				<li><a class="child" href="LaboratoryServlet?method=list_admin_sys&id=<%= id%>" target="content">实验室列表</a></li>
				<li><a class="child" href="LaboratoryServlet?method=list_admin_sys&id=<%= id%>&sign=111" target="content">实验室信息</a></li>
				<li><a class="child" href="RegulationServlet?method=list" target="content">管理制度</a></li>
			<% } %>
			
		<% } %>
		
		<%	if(role != 2){	
				if(role != 3 && role != 4 && role != 5){%>
		<!--  li><a class="child" href="CourseServlet?method=list_syke" target="content">实验课程</a></li>-->
		<li><a class="child" href="CourseServlet?method=list" target="content">课程管理</a></li>
		<%		}
				if(role != 1 && role != 5 && role!=4){%>
		<li><a class="child" href="LaboratoryServlet?method=list&departmentID=<%= departmentID%>" target="content">实验室列表</a></li>
		<%		} 
			}%>
		<%	if(role == 0){	%>
		<li><a class="child" href="TeacherServlet?method=list" target="content">教师列表</a></li>
		<li><a class="child" href="ClassServlet?method=list" target="content">班级列表</a></li>
		<%	} %>
		
		
		<%	if(role == 1){	%>
		<li><a class="child" href="ExperimentServlet?method=list&state=1" target="content">课表管理</a></li>
		<%	}%>
		
		<%	if(role == 2){	%>
		<li><a class="child" href="ScheduleServlet?method=list" target="content">教学进度表管理</a></li>
		<!--  <li><a class="child" href="listSykc.jsp" target="content">实验课程管理</a></li>-->
		<%	}%>
	</ul>
	</li>
	
	<!--
	<li class="menu"><a href="javascript:void(0);" target="content" onclick=expand(2) >实验室管理统计</a></li>
	<li id=child2 style="">
	<ul>
		<li><a class="child" href="blank.html" target="content">效益统计</a></li>
	</ul>
	</li>
	-->
	
	<% if(role == 0 ){ %>	
	<li class="menu"><a href="javascript:void(0);" target="content" onclick=expand(3) >信息展示</a></li>
	<li id=child3 style="">
	<ul>
		<% if(role == 0){ %>
		<li><a class="child" href="NotificationServlet?method=list" target="content">通知公告</a></li>
		<li><a class="child" href="keyLab.jsp" target="content">省级示范中心</a></li>
		<li><a class="child" href="indexBanner.jsp" target="content">首页banner管理</a></li>
		<li><a class="child" href="link.jsp" target="content">友情链接</a></li>
		<% }%>

			
	</ul>
	</li>  
	<% }%>
	
	<li class="menu"><a href="javascript:void(0);" target="content" onclick=expand(4) >系统管理</a></li>
	<li id=child4 style="">
	<ul>
		<% if(role == 3){ %>
		<li><a class="child" href="admin_syszr.jsp" target="content">实验室主任管理</a></li>
		<li><a class="child" href="admin_sysfp.jsp" target="content">实验室分配</a></li>
		<!--  li><a class="child" href="LaboratoryServlet?method=checkLab" target="content">未填实验室</a></li>-->
		<% } %>
		
		
		<% if(role == 1 || role == 2 || role == 0 || role == 5 || role == 3|| role == 4){ %>
			<li><a class="child" href="systemConfig.jsp" target="content">学年配置</a></li>
		<% } %>
		
		
		<li><a class="child" href="modifyPassword.jsp" target="content">修改密码</a></li> 	
		<% if(role == 0){ %>
		<!--<li><a class="child" href="backupDatabase.jsp" target="content">数据库备份</a></li>  -->
		<li><a class="child" href="admin_de.jsp" target="content">教务员管理</a></li>
		<li><a class="child" href="admin_la.jsp" target="content">学院主管领导管理</a></li>
		<li><a class="child" href="LogServlet?method=list" target="content">系统日志</a></li>
		<li><a class="child" href="importData.jsp" target="content">数据导入</a></li>
		<li><a class="child" href="outPutData.jsp" target="content">数据导出</a></li>
		<li><a class="child" href="help/help.doc" target="content">系统帮助</a></li>
		<% }%>
			
		
		<!--  
		<% if(role == 1){ %>
		<li><a class="child" href="jwy_help.jsp" target="content">系统帮助</a></li>
		<%	} %>
		<% if(role == 2){ %>
		<li><a class="child" href="teacher_help.jsp" target="content">系统帮助</a></li>
		<%	} %>
		<% if(role == 3){ %>
		<li><a class="child" href=syszr_help.jsp" target="content">系统帮助</a></li>
		<%	} %>	
		-->
		<li><a class="child" href="logout.jsp" target="content" style="border-right:none">安全退出</a></li> 
	</ul>
	</li>	 	 			
</ul>
</div> 
</center>
</body>
</html>
