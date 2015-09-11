<%@ page language="java" import="java.util.*, com.ltms.model.*, java.sql.*,com.ltms.dao.*" pageEncoding="utf-8"%>
<%
	int role = ((Admin)request.getSession().getAttribute("admin")).getRole();
	int departmentID = ((Admin)request.getSession().getAttribute("admin")).getDepartmentID();
	String id = request.getParameter("id");
	String course_name = new String(request.getParameter("course_name").getBytes("ISO-8859-1"),"utf-8");
	String xgzy = CourseDAO.load_course_sy_mes(id);
	String basepath = request.getContextPath();
	
	Map<String,String> map = SysxxDAO.list_zyl();
	Iterator it = map.keySet().iterator();
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
<script type="text/javascript">
String.prototype.trim = function(){ return this.replace(/(^\s*)|(\s*$)/g, "");}
function ShowErrMsg(Info)
{
	document.getElementById("showMsg").innerHTML = Info;
}
function submitfrm(frm)
{
	return true;
}

function getLa(key){ 
	$.ajax({url:"LaboratoryServlet?method=getAjax_sykc&sign="+key,
		type:"GET",
		async:false,
		dataType:"text",
		success: function(text){
	 		$("#zy_ajax").html(text);
		}
	})
}

function add_mxzy(value){
	var old_mes = document.getElementById("sykc_xgzy").value;
	document.getElementById("sykc_xgzy").value = old_mes + value + ",";
	var select_xz = document.getElementById("select_xz");
	select_xz.selectedIndex = 0;
}

</script>
<div id="position">您现在所在页面：实验室管理&nbsp;&gt;&gt;&nbsp;<a href="LaboratoryServlet?method=list&departmentID=<%= departmentID%>" target="content">实验室管理</a>&nbsp;&gt;&gt;&nbsp;修改实验室信息</div>
<div class="detailInfoContent">
<%@ include file="showMsg.jsp" %>
<form action="CourseServlet" method="post" onsubmit="return submitfrm(this);" >
<input type="hidden" name="method" value="update_xgzy" />
<input type="hidden" name="id" value="<%= id%>" />
<input type="hidden" name="course_name" value="<%=course_name %>" />
<table border=1 cellspacing=0 cellpadding=0 style="border:none;border-left:1px solid #CCCCCC;border-bottom:1px solid #CCCCCC;">
  	<tr>
  		<td class="TD_Left">课程名字</td>
  		<td class="TD_Right"><%=course_name %></td>
  	</tr>
  	<tr>
  		<td class="TD_Left">课程编号</td>
  		<td class="TD_Right"><%= id%></td>
  	</tr>
  	<tr>
  		<td class="TD_Left" style="width:150px;">相关专业</td>
  		<td class="TD_Right" style="width:450px;">
			<textarea name="sykc_xgzy" rows="4" cols="35" id="sykc_xgzy"><%= xgzy%></textarea><br/>
			<select name="ssxk_zyl" onchange=getLa(this.value)>
				<option value="" selected="selected">请选择专业类</option>
				<% while(it.hasNext()){ 
						String key = (String)it.next();
						String value = (String)map.get(key);%>
						
					<option value="<%=key %>"><%=value %></option>
					
				<%} %>
			</select>
			<div id="zy_ajax">
				<select name="ssxk_zy" onchange=add_mxzy(this.value)>
					<option value="">请选择专业类</option>
				</select>
			</div>
		</td>
  	</tr>
  	<tr>
  		<td class="TD_Left">操作</td>
  		<td class="TD_Right"><input type="submit" value="修改" class="form_btn"/>
  	</tr>
</table>  
</form>
</div>
</body>
</html>