<%@ page language="java" import="java.util.*, com.ltms.model.*, java.sql.*,com.ltms.dao.*" pageEncoding="utf-8"%>
<%
	int role = ((Admin)request.getSession().getAttribute("admin")).getRole();
	int departmentID = ((Admin)request.getSession().getAttribute("admin")).getDepartmentID();
	
	String currenTerm_ = (String) request.getSession().getAttribute("currenTerm");
	String currenTerm__ = (String) request.getSession().getAttribute("currenTerm");
	if("".equals(currenTerm__) || currenTerm__.trim().length() == 0){
		request.setAttribute("msg", "请先在学年配置选择要导出数据的学年");
		request.getRequestDispatcher("showMsg.jsp").forward(request, response);
		return ;
	}
	List<Department> departmentList = null;
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
<script src="js/prototype.js" type="text/javascript"></script>
</head>
<body>
<div id="position">您现在所在页面：系统管理&nbsp;&gt;&gt;&nbsp;<a href="importData.jsp" target="content">数据导入</a></div>

<script type="text/javascript">
String.prototype.trim = function(){ return this.replace(/(^\s*)|(\s*$)/g, "");}
function ShowErrMsg(Info)
{
	$("showMsg").innerHTML = Info;
}
function submitfrm(frm)
{
	if(frm.excel_file.value != null && frm.excel_file.value.trim() == "")
	{
		 alert("请先上传excel文件");
		 frm.excel_file.focus();
		 return false;
	}
	return true;
}
</script>
<div id="content" style="text-align:left">
<%@ include file="showMsg.jsp" %>

<form action="DbfServlet" method="post" onsubmit="return submitfrm(this);">
<input type="hidden" name="method" value="output_sysk" />
<table cellspacing=0 cellpadding=1>
	<tr><th colspan="2">&nbsp;</th></tr>
	<tr>
		<td style="width: 200px;">生成所需的文件:&nbsp;</td>
		<td><input type="submit" value="生成" class="form_btn"/></td>
	</tr>
</table>
</form>

<form action="DbfServlet" method="post" onsubmit="return submitfrm(this);">
<input type="hidden" name="method" value="output_sxt" />
<table cellspacing=0 cellpadding=1>
	<tr><th colspan="2">&nbsp;</th></tr>
	<tr>
		<td style="width: 200px;">导出省系统文件:&nbsp;</td>
		<td><input type="submit" value="导出" class="form_btn"/></td>
	</tr>
</table>
</form>

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



注:  一定要先生成文件才能导出<br/>
&nbsp;&nbsp;&nbsp;&nbsp;必须等所有数据都录入完毕才能进行数据导出，否则会导出失败或数据出错
</body>
</html>