<%@ page language="java" import="java.util.*, com.ltms.model.*, java.sql.*,com.ltms.dao.*" pageEncoding="utf-8"%>
<%@page import="com.ltms.util.Utils"%>
<%
	int role = ((Admin)request.getSession().getAttribute("admin")).getRole();
	int departmentID = ((Admin)request.getSession().getAttribute("admin")).getDepartmentID();
	String me = request.getParameter("id");
	int id;
	if(me == null){
		id = (Integer)request.getAttribute("id");
	}else{
		id = Integer.parseInt(me);
	}
	
	Laboratory laboratory = LaboratoryDAO.load(id);
	String currenTerm = (String) request.getSession().getAttribute("currenTerm");
	Sysxx sysxx = LaboratoryDAO.load_sysxxByTeam(id,currenTerm);
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
	$.ajax({url:"LaboratoryServlet?method=getAjax_zy&sign="+key,
		type:"GET",
		async:false,
		dataType:"text",
		success: function(text){
	 		$("#zy_ajax").html(text);
		}
	})
}
</script>
<div id="position">您现在所在页面：实验室管理&nbsp;&gt;&gt;&nbsp;实验室情况</div>
<div class="detailInfoContent">
<%@ include file="showMsg.jsp" %>
<form action="LaboratoryServlet" method="post" onsubmit="return submitfrm(this);" >
<input type="hidden" name="method" value="updata_sysxx" />
<input type="hidden" name="id" value="<%=id %>">
<input type="hidden" name="sysdm" value="11111">
<input type="hidden" name="xk_" value="<%= sysxx.getSsxk() %>">
<table border=1 cellspacing=0 cellpadding=0 style="border:none;border-left:1px solid #CCCCCC;border-bottom:1px solid #CCCCCC;">
  	<tr>
  		<td class="TD_Left">系别</td>
  		<td class="TD_Right"><%= DepartmentDAO.getName(laboratory.getDepartmentID())%></td>
  	</tr>
  	<tr>
  		<td class="TD_Left">实验室名称</td>
  		<td class="TD_Right"><%= laboratory.getName()%></td>
  	</tr>
  	<tr>
  		<td class="TD_Left">实验室地址</td>
  		<td class="TD_Right"><%= laboratory.getAddress()%></td>
  	</tr>
  	<!--tr>
  		<td class="TD_Left" style="width:150px;">实验室代码</td>
  		<td class="TD_Right" style="width:450px;"><input type="text" name="sysdm" value="<%=sysxx.getSysdm() %>" style="width: 60px"/></td>
  	</tr-->
  	<tr>
  		<td class="TD_Left">实验室类型</td>
  		<td class="TD_Right">
			<select name="syslx">
				<option value="1" <% if(sysxx.getSyslx() == 1){%> selected="selected"<%} %>>科学</option>
				<option value="2" <% if(sysxx.getSyslx() == 2){%> selected="selected"<%} %>>教研</option>
				<option value="3" <% if(sysxx.getSyslx() == 3){%> selected="selected"<%} %>>其他</option>
			</select>
		</td>
	</tr>
	<tr>
  		<td class="TD_Left">实验室类别</td>
  		<td class="TD_Right">
			<select name="syslb">
				<option value="1" <% if(sysxx.getSyslb() == 1){%> selected="selected"<%} %>>国家级实验教学示范中心</option>
				<option value="2" <% if(sysxx.getSyslb() == 2){%> selected="selected"<%} %>>省级实验教学示范中心</option>
				<option value="3" <% if(sysxx.getSyslb() == 3){%> selected="selected"<%} %>>按平台建设的校   院（系）实验室</option>
				<option value="4" <% if(sysxx.getSyslb() == 4){%> selected="selected"<%} %>>其他类型实验室</option>
			</select>
		</td>
	</tr>
	<tr>
  		<td class="TD_Left">建立年份</td>
  		<td class="TD_Right" style="width:450px;"><input type="text" name="jlnf" value="<% if(sysxx.getJlnf() == null){ %><%}else{ %><%=sysxx.getJlnf() %><%} %>" style="width: 60px"/></td>
	</tr>
	<tr>
  		<td class="TD_Left">使用面积<br/>(平方米)</td>
  		<td class="TD_Right" style="width:450px;"><input type="text" name="symj" value="<% if(sysxx.getSymj() == null){ %><%}else{ %><%=sysxx.getSymj() %><%} %>" style="width: 60px"/></td>
	</tr>
	<tr>
  		<td class="TD_Left">所属学科</td>
  		<td class="TD_Right">
  		<% String ssxk = (sysxx.getSsxk()+"").trim();
  			%>
  			<% if(Utils.getZyMc(ssxk) == null || Utils.getZyMc(ssxk).trim().length() == 0 || "null".equals(Utils.getZyMc(ssxk))){ %>未选<%} else{%><%=Utils.getZyMc(ssxk) %> <%} %><br/>
  			
			<select name="ssxk_zyl" onchange=getLa(this.value)>
				<option value="" selected="selected">请选择专业类</option>
				<% while(it.hasNext()){ 
						String key = (String)it.next();
						String value = (String)map.get(key);%>
						
					<option value="<%=key %>"><%=value %></option>
					
				<%} %>
			</select>
			<div id="zy_ajax">
				<select name="ssxk_zy">
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
注：（1）建立年份只需要填写那一年即可，不能比1900早或者比现在后<br/>
	&nbsp;&nbsp;&nbsp;&nbsp;（2）代码需有5位字符且分为三级，第一位为一级，2,3位为二级，4,5位为三级，
		 一，二级可以使字母或者数字，三级只能是数字。
	
</body>
</html>