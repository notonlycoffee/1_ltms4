<%@ page language="java" import="java.util.*, com.ltms.model.*, java.sql.*,com.ltms.dao.*" pageEncoding="utf-8"%>
<%
	int id = Integer.parseInt(request.getParameter("id"));
	Laboratory laboratory = LaboratoryDAO.load(id);
	String basepath = request.getContextPath();
	String currenTerm = (String) request.getSession().getAttribute("_year_");
	Calendar cal = Calendar.getInstance();
	int year = cal.get(Calendar.YEAR);
	int month = cal.get(Calendar.MONTH);
	if(currenTerm == null){
		if(month<8){
			currenTerm = (year - 1) + "-" + year;
		}else{
			currenTerm = year + "-" + (year + 1);
		}
	}
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
<div class="detailInfoContent">
<form action="LaboratoryServlet" method="post"  enctype="multipart/form-data" onsubmit="return submitfrm(this);" >
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
			<%=sysjf.getYqsbgzjfhj() %>
		</td>
  	</tr>
  	<tr>
  		<td class="TD_Left">其中教学仪器购置经费</td>
  		<!-- 要限制小于上面的合计 -->
  		<td class="TD_Right">
			<%=sysjf.getQzjxyqgzjf() %>
		</td>
	</tr>
	<tr>
  		<td class="TD_Left">仪器设备维护经费合计</td>
  		<td class="TD_Right">
			<%=sysjf.getYqsbwhjfhj() %>
		</td>
	</tr>
	<tr>
  		<td class="TD_Left">其中教学仪器维护经费</td>
  		<!-- 要限制小于上面的合计 -->
  		<td class="TD_Right">
			<%=sysjf.getQzjxyqwhjf() %>
		</td>
	</tr>
	<tr>
  		<td class="TD_Left">实验教学运行经费合计</td>
  		<td class="TD_Right">
			<%=sysjf.getSyjxyxjfhj() %>
		</td>
	</tr>
	<tr>
  		<td class="TD_Left">其中教学实验年材耗费</td>
  		<!-- 要限制小于上面的合计 -->
  		<td class="TD_Right">
			<%=sysjf.getQzjxsynchf() %>
		</td>
	</tr>
	<tr>
  		<td class="TD_Left">实验室建设经费</td>
  		<td class="TD_Right">
			<%=sysjf.getSysjsjf() %>
		</td>
	</tr>
	<tr>
  		<td class="TD_Left">实验教学研究改革经费</td>
  		<td class="TD_Right">
			<%=sysjf.getSyjxyjggjf() %>
		</td>
	</tr>
	<tr>
  		<td class="TD_Left">其他经费</td>
  		<td class="TD_Right">
			<%=sysjf.getQtjf() %>
		</td>
	</tr>
  	<tr>
  		<td class="TD_Left">操作</td>
  		<td><input type="button" value="返回" onclick="javascript:history.go(-1)" class="form_btn" /></td>
  	</tr>
</table>  
</form>
</div>
注:	所有数据单位都是万元，可以带有两位小数，多出小数系统会自动四舍五入。<br/>&nbsp;&nbsp;&nbsp;
	其中教学仪器购置经费    一定要小于    仪器设备购置经费合计<br/>&nbsp;&nbsp;&nbsp;
	其中教学仪器维护经费    一定要小于    仪器设备维护经费合计<br/>&nbsp;&nbsp;&nbsp;
	其中教学实验年材耗费    一定要小于    实验教学运行经费合计<br/>&nbsp;&nbsp;&nbsp;
</body>
</html>