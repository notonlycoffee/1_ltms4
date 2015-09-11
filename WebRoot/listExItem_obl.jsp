<%@ page language="java" pageEncoding="utf-8" %>
<%@ page import="java.util.*, com.ltms.model.*, com.ltms.dao.*, java.sql.*" %>
<%@ include file="global.jsp" %>
<%  
	ArrayList<ExItem> exItemList = (ArrayList<ExItem>)request.getAttribute("exItemList");
	int laboratoryID = (Integer)request.getAttribute("laboratoryID");
	String laboratoryName= LaboratoryDAO.getName(laboratoryID);
	ArrayList<Department> departmentList = (ArrayList<Department>)DepartmentDAO.list();
	String location = "实验教学";
	
	Calendar cal = Calendar.getInstance();
	int year = cal.get(Calendar.YEAR);
	String school_year_list[] = {(year - 3) + "-" + (year - 2) + "学年度",(year - 2) + "-" + (year - 1) + "学年度",(year - 1) + "-" + year + "学年度", year + "-" + (year + 1) + "学年度"};
	
	
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link href="css/base.css" rel="stylesheet" type="text/css" />
<link href="css/general.css" rel="stylesheet" type="text/css" />
<script src="<%= basepath%>/js/prototype.js" type="text/javascript"></script>
<title><%= location + " - " + site_name%></title>
<script type="text/javascript"> 
	function getLa(departmentID){ 
		var url = 'LaboratoryServlet';
		var params = 'method=getAjax2&departmentID='+departmentID;
		var myAjax = new Ajax.Request(
				url,
				{
					method: 'post', 
					parameters:params,
					onComplete:callback
				} );
	} 
	
	function callback(response_text){ 
		var is_text = response_text.responseText;
		$('la_ajax').innerHTML = is_text;
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
</script> 
</head>
<body>
<div class="contain">
<%@ include file="header.jsp" %>
<!-- ◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆-->   
<div class="stm_content">
	<div class="content">
		<p class="empty">&nbsp;</p>
		<div class="content_position">
			<p class="titleName">按实验室查看</p>				
			<p class="position_bg"><a href="index.jsp">首页</a><span>></span><a href="ExItemServlet?method=webList"><%= location%></a></p>
		</div>
		<div class="sort">
		<form action="ExItemServlet" method="post" onsubmit="return submitfrm(this);">
		<input type="hidden" name="method" value="list_obl" />
		<table>
		<tr>
             <td width="30%" class="td_bg">
	           <div id="de_list">
					<select id="departmentID" name="departmentID" onchange="getLa(this.value)" style="height:26px;width:200px;">
					<option value="0">--请选择系别--</option>
			        <%for(Department d : departmentList){%>
						<option value="<%= d.getId()%>"><%= d.getName()%></option>
			        <%}%>
			        </select>
				</div>
	        </td>
			<td width="30%" class="td_bg">
				<div id="la_ajax">
					<select id="laboratoryID" name="laboratoryID" style="height:26px;width:200px;">
					<option value="0">--请先选择系别--</option>
					</select>
				</div>
	        </td>
	        
	         <td>
			<select name="year_weblist" style="height:26px;width:200px;">
	   		<option value="0">-- 选择学年--</option>
		   	
		   	<option value="<%= school_year_list[0]%>" ><%= school_year_list[0]%></option>
			<option value="<%= school_year_list[1]%>" ><%= school_year_list[1]%></option>
			<option value="<%= school_year_list[2]%>" ><%= school_year_list[2]%></option>
			<option value="<%= school_year_list[3]%>" ><%= school_year_list[3]%></option>
			
	   		</select>
			</td>
			
			 <td>
			<select name="term_weblist" style="height:26px;width:100px;">
	   		<option value="0">-- 选择学期 --</option>
		   	<option value="第一学期" >第一学期</option>
			<option value="第二学期" >第二学期</option>
	   		</select>
			</td>
			
	        <td width="20%" class="td_bg">
	        	<input type="submit" value="查询" class="btn" style="width: 50px"/>
	        </td>
		</tr>
		</table>
		</form>
		</div>
		
		<div class="inform_content">
        	<div class="inform_line ge_width">
        </div>
        <div class="general_list" id="laboratory_list_ltms">
	        <%	if(exItemList.size() == 0){	%>
			<div class="no_result_msg">该实验室下暂无实验项目</div>
			<%	}else{	%>
			<ul>
				<%for(ExItem ei : exItemList){	%>
				<li><p><a href="ExItemServlet?method=view&id=<%= ei.getId()%>&scheduleID=<%= ei.getScheduleID()%>"><%= ei.getItemName()%></a></p><span>[<%= laboratoryName%>]</span></li>
				<%}%>
			</ul>
			<%	}	%>
		</div>
        <div class="sort_page" id="sort_page_ltms">
        	<!-- 分页--> 
        	<% String baseUrl = "ExItemServlet?method=list_obl&laboratoryID=" + laboratoryID; %>
			<%@ include file="page.jsp" %>
		</div>
		<%	if(exItemList.size() == 0){	%>
			<script type="text/javascript">
				$('sort_page_ltms').innerHTML = "";
			</script>
		<%	}%>
		</div>
<!-- ◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆--> 
        <%@ include file="footer.jsp" %>
		</div>
	</div>
</div>
</body>
</html>
