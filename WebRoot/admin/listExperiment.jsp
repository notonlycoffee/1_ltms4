<%@ page language="java" import="java.util.*, com.ltms.model.*, java.sql.*,com.ltms.dao.*" pageEncoding="utf-8"%>
<%
	int role = ((Admin)request.getSession().getAttribute("admin")).getRole();
	int departmentID = ((Admin)request.getSession().getAttribute("admin")).getDepartmentID();
	List<Experiment> experimentList = (ArrayList<Experiment>)request.getAttribute("experimentList");	
	if(experimentList == null){
		request.getRequestDispatcher("ExperimentServlet?method=list&state=1").forward(request, response);
		return;
	}
	System.out.println("experimentList is " + experimentList.size());
	List<Laboratory> laboratoryList = (ArrayList<Laboratory>)session.getAttribute("laboratoryList");	
	if(laboratoryList == null){
		if(role == 1){
			laboratoryList = LaboratoryDAO.list(departmentID);
		}else{
			laboratoryList = LaboratoryDAO.list(1);
		}
		session.setAttribute("laboratoryList", laboratoryList);
	}
	List<Department> departmentList = (ArrayList<Department>)session.getAttribute("departmentList");	
	if(departmentList == null){
		departmentList = DepartmentDAO.list();
		session.setAttribute("departmentList", departmentList);
	}
	String weekday[] = {"周一", "周二", "周三", "周四", "周五"};
	String everyWeek[] = {"每周", "单周", "双周"};
	
	Calendar cal = Calendar.getInstance();
	int year = cal.get(Calendar.YEAR);
	String school_year_list[] = {(year - 3) + "-" + (year - 2) + "学年度",(year - 2) + "-" + (year - 1) + "学年度",(year - 1) + "-" + year + "学年度", year + "-" + (year + 1) + "学年度"};
	
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link rel="stylesheet" type="text/css" href="css/global.css" />
<script src="js/jquery.min.js"></script>
<script src="js/jquery.easing.min.js"></script>
<script src="js/base.js"></script>
<script src="js/jquery-1.9.1.js"></script>
<script src="js/jquery-ui-1.10.2.js"></script>
</head>
<body>
<div id="position">您现在所在页面：实验室管理&nbsp;&gt;&gt;&nbsp;<a href="ExperimentServlet?method=list&state=1" target="content">实验管理</a>
<span id="addInfo"><% if(role!=2){%><a href="addExperiment.jsp">添加实验信息</a><%} %></span></div>

<div id="content" style="text-align:left">
<script type="text/javascript">
String.prototype.trim = function(){ return this.replace(/(^\s*)|(\s*$)/g, "");}
function ShowErrMsg(Info)
{
	$("#showMsg").html(Info);
}

function submitfrm(frm)
{
	if(frm.laboratoryID && frm.laboratoryID.value == "0"){
		alert("请先选择一个实验室");
		frm.laboratoryID.focus();
		return false;
	}
	return true;
}

function getLa(departmentID){ 
	$.ajax({url:"LaboratoryServlet?method=getAjax&departmentID="+departmentID,
		type:"GET",
		async:false,
		dataType:"text",
		success: function(text){
	 		$("#la_ajax").html(text);
		}
	})
}
</script>
<%@ include file="showMsg.jsp" %>
<form action="ExperimentServlet" method="post" onsubmit="return submitfrm(this);" >
<input type="hidden" name="method" value="search" />
<table cellspacing=0 cellpadding=1>
  	<tr>
  		<th>系别</th>
  		<th>实验室</th>
  		<th>时间</th>
  		<th>操作</th>
  	</tr>
    <tr>
       	<td>
	   		<select id="departmentID" name="departmentID" onchange=getLa(this.value)>
	        <%for(Department d : departmentList){%>
				<option value="<%= d.getId()%>" <%= d.getId() == departmentID ? "selected" : ""%> ><%= d.getName()%></option>
	        <%}%>
	        </select>
	   	</td>
   		<td>
   		<div id="la_ajax">
   		<select name="laboratoryID" id="laboratoryID">
	   		<% for(Iterator<Laboratory> it = laboratoryList.iterator();it.hasNext();){
	   			Laboratory l = it.next();%> 
	   		 	<option value="<%=l.getId()%>"><%=l.getName()%></option>
	   		<%}%>
   		</select>
   		</div>
	   	</td>
	   	<td>
	   		<select name="weekday" id="weekday">
		   		<% for(int i=0; i<5; i++){%> 
				<option value="<%= weekday[i]%>"><%= weekday[i]%></option>
		   		<% }%>
	   		</select>
			</td>
    	<td><input type="submit" value="查询" class="form_btn"/></td>
    </tr>
</table>
</form>
<%
	String search = (String)request.getAttribute("search");
if(search != null) {
	int exitGroup = (Integer)request.getAttribute("exitGroup");	
	String searchWeekday = (String)request.getAttribute("weekday");
	int dID = (Integer)request.getAttribute("dID");
	int lID = (Integer)request.getAttribute("laboratoryID");
	String laboratoryName = LaboratoryDAO.getName(lID);
%>
<script type="text/javascript">
getLa(<%= dID%>);
$("#departmentID").val("<%= dID%>");
$("#laboratoryID").val("<%= lID%>");
$("#weekday").val("<%= searchWeekday%>");
</script>
	<font style="color:red">您所查询的时段是：
	<%= laboratoryName%>&nbsp;|&nbsp;<%= searchWeekday%>&nbsp;|&nbsp;该时段已安排实验数：<%= exitGroup%></font>
<% }%>

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
   	<%= ExperimentDAO.printList(experimentList)%>
</table>  

<!-- 分页--> 	
<% String baseUrl = "ExperimentServlet?method=list&state=1"; %>
<%@ include file="page.jsp" %>

<form action="ExperimentServlet" method="post" >
<input type="hidden" name="method" value="outport_to_excel" />
<table cellspacing=0 cellpadding=1>
	<tr>
		<%	if(role == 0){%>
		<th>系别</th>
		<%	} %>
		<th>学年度</th>
		<th>学期</th>
		<th>类别</th>
		<th>操作</th>
	</tr>
	<tr>
		<%	if(role == 0){%>
		<td>
		<select id="departmentID" name="departmentID" onchange=getLa(this.value)>
	        <%for(Department d : departmentList){%>
			<option value="<%= d.getId()%>"><%= d.getName()%></option>
	        <%}%>
        </select>
        </td>
		<%	}else{ %>
		<input type="hidden" name="departmentID" value="<%= departmentID%>" />
		<%	} %>
 		<td>
 		<select name="year" id="year">
 			<option value="0">-- 选择学年--</option>
		   	<option value="<%= school_year_list[0]%>" ><%= school_year_list[0]%></option>
			<option value="<%= school_year_list[1]%>" ><%= school_year_list[1]%></option>
			<option value="<%= school_year_list[2]%>" ><%= school_year_list[2]%></option>
			<option value="<%= school_year_list[3]%>" ><%= school_year_list[3]%></option>
 		
   		</select>
 		</td>
 		<td>
 		<select name="term" id="term">
 			<option value="0">-- 选择学期--</option>
	 		<option value="第一学期">第一学期</option>
	 		<option value="第二学期">第二学期</option>
   		</select>
 		</td>
 		<td style="text-align:right;line-height: 20px;">
 		按班级：<input type="radio" name="order_field" value="by_class" checked="checked"/><br />
 		按实验室：<input type="radio" name="order_field" value="by_la" />
 		</td>
  		<td><input type="submit" value="导出实验室课表" class="form_btn2"/></td>
	</tr>
</table>
</form>
</div>
注：导出实验课表建议使用IE8以上版本
</body>
</html>