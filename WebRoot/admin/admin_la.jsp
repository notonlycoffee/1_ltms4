<%@ page language="java" import="java.util.*, com.ltms.model.*, java.sql.*,com.ltms.dao.*" pageEncoding="utf-8"%>
<%  
	List<Admin> adminList = AdminDAO.list(3);	
	List<Department> departmentList = (ArrayList<Department>)session.getAttribute("departmentList");
	if(departmentList == null){
		departmentList = DepartmentDAO.list();
		session.setAttribute("departmentList", departmentList);
	}
	List<Teacher> teacherList = TeacherDAO.list(departmentList.get(0).getId());
	Map<Integer, String> de_nameMap = DepartmentDAO.getNameMap();
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
<div id="position">您现在所在页面：系统管理&nbsp;&gt;&gt;&nbsp;<a class="child" href="admin_la.jsp" target="content">系主任管理</a></div>
<div id="content" style="text-align:left">
<script type="text/javascript">
String.prototype.trim = function(){ return this.replace(/(^\s*)|(\s*$)/g, "");}
function ShowErrMsg(Info)
{
	$("#showMsg").html(Info);
}
function submitfrm(frm)
{
	if(frm.teacherID && frm.teacherID.value=="0")
	{
		 alert("请先选择一个教师");
		 frm.teacherID.focus();
		 return false;
	}
	return true;
}
//ajax部分
function getTe(departmentID){
	$.ajax({url:"TeacherServlet?method=getAjax&unitID="+departmentID,
		type:"GET",
		async:false,
		dataType:"text",
		success: function(text){
	 		$("#ajax_te").html(text);
		}
	})
}
</script>
<%@ include file="showMsg.jsp" %>
<form action="AdminServlet" method="post" onsubmit="return submitfrm(this);" >
<input type="hidden" name="method" value="add" />
<input type="hidden" name="role" value="3" />
<table cellspacing=0 cellpadding=1>
  	<tr>
  		<th>系别</th>
  		<th>教师</th>
  		<th>操作</th>
  	</tr>
    <tr>
   		<td>
		<select id="departmentID" name="departmentID" onchange="getTe(this.value)" style="width:200px;height:23px;margin:0;">
			<%	for(Department d : departmentList){%>
			<option value="<%= d.getId()%>"><%= d.getName()%></option>
			<%	}%>
		</select>
		</td>
   		<td>
   		<div id="ajax_te">
   		<select id="teacherID" name="teacherID" style="width:100px;height:23px;margin:0;">
			<%	for(Teacher t: teacherList){%>
			<option value="<%= t.getId()%>" ><%= t.getName()%></option>
			<%	}%>
		</select>
   		</div>
		</td>
   		<td><input type="submit" value="设置为系主管领导" class="form_btn2"/></td>
    </tr>
</table>
</form>

<script type="text/javascript">
    function linkok(url){
	    question = confirm("是否确认删除？");
	    if (question){
	   		window.location.href = url;
	    }
    }
</script>
<!-- 显示所有教务员账号 -->
<table border=1 cellspacing=0 cellpadding=0 >
	<tr>
		<th>系别</th>
		<th>工号</th>
		<th>姓名</th>
		<th>操作</th>
	</tr>
    <%	for(Admin admin : adminList){ %>
   	<tr class="exTr">
   		<td style="width:200px;"><%= de_nameMap.get(admin.getDepartmentID())%></td>
   	    <td style="width:100px;"><%= admin.getId()%></td>
   	    <td style="width:200px;"><%= admin.getName()%></td>
   		<td>
		<input type="button" value="删除" onclick="javascript:linkok('AdminServlet?method=delete&id=<%= admin.getId()%>&role=<%= admin.getRole()%>')" class="form_btn"/>
		<input type="button" value="重置密码" onclick="javascript:window.location.href='AdminServlet?method=reset_password&id=<%= admin.getId()%>&role=<%= admin.getRole()%>'" class="form_btn2"/>
		</td>
   	</tr>   	
   <% }%>  
</table>
</div>

<div class="ps_foot">
<strong>注：</strong><br/>
默认密码为123456
</div>	

</body>
</html>

