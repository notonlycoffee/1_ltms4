<%@ page language="java" import="java.util.*, com.ltms.model.*, java.sql.*,com.ltms.dao.DepartmentDAO" pageEncoding="utf-8"%>
<%@page import="com.ltms.dao.SystemConfigDAO"%>
<%
	int role = ((Admin)session.getAttribute("admin")).getRole();  // 0教务处 1教务员 2教师
	String admin_name = (String)session.getAttribute("admin_name");  // 0教务处 1教务员 2教师
	String[] string = new String[2];
	string[0] = "";
	string[1] = "";
	String zf = (String)request.getSession().getAttribute("jxzs_fjsj");
	//System.out.print(zf);
	if(zf != null && !zf.trim().equals("")){
		string = zf.split("_");
	}
	
	
	Calendar cal = Calendar.getInstance();
	int year = cal.get(Calendar.YEAR);
	String school_year_list[] = {(year - 3) + "-" + (year - 2) + "学年度",(year - 2) + "-" + (year - 1) + "学年度",(year - 1) + "-" + year + "学年度", year + "-" + (year + 1) + "学年度"};
	SystemConfig systemConfig = SystemConfigDAO.load(admin_name,role);
	if(systemConfig.getYear() == null){
	  systemConfig.setTerm("");
	  systemConfig.setYear("");
	}
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link rel="stylesheet" type="text/css" href="css/global.css" />

<script language=javascript> 
	function winclose() { 
	
	window.parent.frames[0].location.reload();//刷新 
	
	window.close();//关闭 
} 
</script> 

</head>
<body onload=winclose()>
<div id="position">您现在所在页面：系统管理&nbsp;&gt;&gt;&nbsp;<a href="systemConfig.jsp" target="content">系统配置</a></div>
<div id="content" style="text-align:left">
<script type="text/javascript">
String.prototype.trim = function(){ return this.replace(/(^\s*)|(\s*$)/g, "");}
function ShowErrMsg(Info)
{
	document.getElementById("showMsg").innerHTML = Info;
}
</script>



<%@ include file="showMsg.jsp" %>
<form action="SystemConfigServlet" method="post" >
<input type="hidden" name="method" value="update" />
<table border=1 cellspacing=0 cellpadding=0 style="border:none;border-right:1px solid #CCCCCC;border-bottom:1px solid #CCCCCC;">
  	<tr>
  		<td class="TD_Left">当前学年</td>
  		<td class="TD_Right" style="text-align:left;">
		<select name="year" id="year" style="width:200px;">
			<option value="无"> 未选择...</option>
			<option value="<%= school_year_list[0]%>" <%= systemConfig.getYear().equals(school_year_list[0]) ? "selected" : "" %> ><%= school_year_list[0]%></option>
			<option value="<%= school_year_list[1]%>" <%= systemConfig.getYear().equals(school_year_list[1]) ? "selected" : "" %> ><%= school_year_list[1]%></option>
			<option value="<%= school_year_list[2]%>" <%= systemConfig.getYear().equals(school_year_list[2]) ? "selected" : "" %> ><%= school_year_list[2]%></option>
			<option value="<%= school_year_list[3]%>" <%= systemConfig.getYear().equals(school_year_list[3]) ? "selected" : "" %> ><%= school_year_list[3]%></option>
		</select>
		</td>
  	</tr>
  	<%if(role != 5){ %>
	  	<tr>
	  		<td class="TD_Left">当前学期</td>
	  		<td class="TD_Right" style="text-align:left;">
			<select name="term" id="term" style="width:200px;">
			    <option value="无"> 未选择...</option>
				<option value="第一学期" <%= systemConfig.getTerm().equals("第一学期") ? "selected" : "" %> >第一学期</option>
				<option value="第二学期" <%= systemConfig.getTerm().equals("第二学期") ? "selected" : "" %> >第二学期</option>
			</select>
			</td>
	  	</tr>
  	<%}else{ %>
  		<input type="hidden" name="term" value="整一学年">
  	<%} %>
  	
	<tr>
  		<td class="TD_Left">操作</td>
  		<td class="TD_Right" style="text-align:left;"><input type="submit" value="修改" style="margin:0;" class="form_btn"/></td>
  	</tr>
</table>  
</form>

<form action="SystemConfigServlet" method="post" >
<input type="hidden" name="method" value="updateSysConf" />
<table border=1 cellspacing=0 cellpadding=0 style="border:none;border-right:1px solid #CCCCCC;border-bottom:1px solid #CCCCCC;">
	
  	<%if(role == 0){ %>
  	<tr>
  		<td class="TD_Left" style="width: 130px">学期教学周数</td>
  		<td class="TD_Right" style="text-align:left;">
  			<input type="text" name="jxzs" value="<%=string[0] %>"/>
		</td>
  	</tr>
  	<%} %>
  	<%if(role == 0){ %>
	  	<tr>
	  		<td class="TD_Left">开学放假时间</td>
	  		<td class="TD_Right" style="text-align:left;">
	  			<input type="text" name="fjsj" value="<%=string[1] %>"/>
			</td>
	  	</tr>
  	<%} %>
  	
	<tr>
  		<td class="TD_Left">操作</td>
  		<td class="TD_Right" style="text-align:left;"><input type="submit" value="修改" style="margin:0;" class="form_btn"/></td>
  	</tr>
</table>  
</form>
</div>

</body>
</html>