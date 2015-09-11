+<%@ page language="java" import="java.util.*, com.ltms.model.*, java.sql.*,com.ltms.dao.*" pageEncoding="utf-8"%>
<%
	int role = ((Admin)request.getSession().getAttribute("admin")).getRole();
	int departmentID = ((Admin)request.getSession().getAttribute("admin")).getDepartmentID();
	int id = Integer.parseInt(request.getParameter("id"));
	Laboratory laboratory = LaboratoryDAO.load(id);
	String basepath = request.getContextPath();
	String currenTerm = (String) request.getSession().getAttribute("currenTerm");
	Sysjf sysjf = SysxxDAO.loadsysjfByTeam(id,currenTerm);
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link rel="stylesheet" type="text/css" href="css/global.css" />
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
</script>
<div id="position">您现在所在页面：实验室管理&nbsp;&gt;&gt;&nbsp;<a href="LaboratoryServlet?method=list&departmentID=<%= departmentID%>" target="content">实验室管理</a>&nbsp;&gt;&gt;&nbsp;修改实验室信息</div>
<div class="detailInfoContent">
<%@ include file="showMsg.jsp" %>
<form action="LaboratoryServlet" method="post" onsubmit="return submitfrm(this);" >
<input type="hidden" name="id" value="<%=laboratory.getId()%>" />
<input type="hidden" name="method" value="update_sysjf" />
<table border=1 cellspacing=0 cellpadding=0 style="border:none;border-left:1px solid #CCCCCC;border-bottom:1px solid #CCCCCC;">
  	<tr>
  		<td class="TD_Left">实验室名称</td>
  		<td class="TD_Right">
  			<%=laboratory.getName() %>
  		</td>
  	</tr>
  	<tr>
  		<td class="TD_Left" style="width:150px;">仪器设备购置经费合计</td>
  		<td class="TD_Right">
			<input type="text" name="yqsbgzjfhj" value="<%=sysjf.getYqsbgzjfhj() %>" style="width: 50px;"></input>
		</td>
  	</tr>
  	<tr>
  		<td class="TD_Left">其中教学仪器购置经费</td>
  		<!-- 要限制小于上面的合计 -->
  		<td class="TD_Right">
			<input type="text" name="qzjxyqgzjf" value="<%=sysjf.getQzjxyqgzjf() %>" style="width: 50px;"></input>
		</td>
	</tr>
	<tr>
  		<td class="TD_Left">仪器设备维护经费合计</td>
  		<td class="TD_Right">
			<input type="text" name="yqsbwhjfhj" value="<%=sysjf.getYqsbwhjfhj() %>" style="width: 50px;"></input>
		</td>
	</tr>
	<tr>
  		<td class="TD_Left">其中教学仪器维护经费</td>
  		<!-- 要限制小于上面的合计 -->
  		<td class="TD_Right">
			<input type="text" name="qzjxyqwhjf" value="<%=sysjf.getQzjxyqwhjf() %>" style="width: 50px;"></input>
		</td>
	</tr>
	<tr>
  		<td class="TD_Left">实验教学运行经费合计</td>
  		<td class="TD_Right">
			<input type="text" name="syjxyxjfhj" value="<%=sysjf.getSyjxyxjfhj() %>" style="width: 50px;"></input>
		</td>
	</tr>
	<tr>
  		<td class="TD_Left">其中教学实验年材耗费</td>
  		<!-- 要限制小于上面的合计 -->
  		<td class="TD_Right">
			<input type="text" name="qzjxsynchf" value="<%=sysjf.getQzjxsynchf() %>" style="width: 50px;"></input>
		</td>
	</tr>
	<tr>
  		<td class="TD_Left">实验室建设经费</td>
  		<td class="TD_Right">
			<input type="text" name="sysjsjf" value="<%=sysjf.getSysjsjf() %>" style="width: 50px;"></input>
		</td>
	</tr>
	<tr>
  		<td class="TD_Left">实验教学研究改革经费</td>
  		<td class="TD_Right">
			<input type="text" name="syjxyjggjf" value="<%=sysjf.getSyjxyjggjf() %>" style="width: 50px;"></input>
		</td>
	</tr>
	<tr>
  		<td class="TD_Left">其他经费</td>
  		<td class="TD_Right">
			<input type="text" name="qtjf" value="<%=sysjf.getQtjf() %>" style="width: 50px;"></input>
		</td>
	</tr>
  	<tr>
  		<td class="TD_Left">操作</td>
  		<td class="TD_Right"><input type="submit" value="修改" class="form_btn"/>
  	</tr>
</table>  
</form>
</div>
注:	所有数据单位都是万元，可以带有两位小数，多出小数系统会四舍五入。<br/>&nbsp;&nbsp;&nbsp;
	其中教学仪器购置经费    一定要小于    仪器设备购置经费合计<br/>&nbsp;&nbsp;&nbsp;
	其中教学仪器维护经费    一定要小于    仪器设备维护经费合计<br/>&nbsp;&nbsp;&nbsp;
	其中教学实验年材耗费    一定要小于    实验教学运行经费合计<br/>&nbsp;&nbsp;&nbsp;
	即使没有数据可以录入，也要点击一下修改按钮
</body>
</html>