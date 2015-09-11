<%@ page language="java" import="java.util.*, com.ltms.model.*, java.sql.*,com.ltms.dao.LaboratoryDAO,com.ltms.dao.DepartmentDAO" pageEncoding="utf-8"%>
<%  
	List<Laboratory> DepArr = (ArrayList<Laboratory>)request.getAttribute("DepArr");
	Map<Integer, String> nameMap = (HashMap<Integer, String>)session.getAttribute("nameMap");	
	if(nameMap == null){
		nameMap = DepartmentDAO.getNameMap();
		session.setAttribute("nameMap", nameMap);
	}
	
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
<div id="position">您现在所在页面：系统管理&nbsp;&gt;&gt;&nbsp;<a href="#" target="content">实验室列表</a></div>
<div id="content" style="text-align:left">
<script type="text/javascript">
String.prototype.trim = function(){ return this.replace(/(^\s*)|(\s*$)/g, "");}
</script>
<%@ include file="showMsg.jsp" %>
<table cellspacing=0 cellpadding=0>
  	<tr>
  		<th>系别</th>
  		<th>实验室名称</th>
  		<th>实验室地址</th>
  	</tr>
    <% for(Laboratory laboratory : DepArr){%>
   	<tr class="exTr">
   		<td class="showTD" style="width:20%;text-align:left;padding:0 8px;"><%= nameMap.get(laboratory.getDepartmentID())%></td>
   	    <td class="showTD" style="width:30%;text-align:left;"><%= laboratory.getName()%></td>
   	    <td class="showTD" style="width:30%;text-align:left;"><%= laboratory.getAddress()%></td>
   	    <%}%>
   	</tr>
</table>

</div>
</body>
</html>