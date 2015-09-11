<%@ page language="java" import="java.util.*, com.ltms.model.*, java.sql.*,com.ltms.dao.*" pageEncoding="utf-8"%>
<%@ include file="global.jsp" %>
<%
	int id = Integer.parseInt(request.getParameter("id"));
	Laboratory laboratory = LaboratoryDAO.load(id);
	String location = "查看实验室信息 - 实验室一览";
	Calendar cal = Calendar.getInstance();
	int year = cal.get(Calendar.YEAR);
	String school_year_list[] = {(year - 3) + "-" + (year - 2),(year - 2) + "-" + (year - 1),(year - 1) + "-" + year, year + "-" + (year + 1)};
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link href="css/base.css" rel="stylesheet" type="text/css" />
<link href="css/general.css" rel="stylesheet" type="text/css" />
<script src="js/jquery.min.js"></script>
<script src="js/jquery.easing.min.js"></script>
<script src="js/base.js"></script>
<title><%= location + " - " + site_name%></title>
<script type="text/javascript">

function setYear(key){ 
	$.ajax({url:"LaboratoryServlet?method=setYear&year="+key,
		type:"GET",
		async:false,
		dataType:"text",
		success: function(text){
		}
	})
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
				<p class="titleName">实验室详细信息</p>
				<p class="position_bg"><a href="index.jsp">首页</a><span>></span><a href="LaboratoryServlet?method=webList">实验室一览</a><span>></span><span>查看实验室信息</span></p>
			</div>
			<div class="inform_content"> 
				<span class="general_name">
				<select id="_year_" name="_year_" onchange="setYear(this.value)" style="height:26px;width:200px;">
					<option value="0">--请选择年度--</option>
					<option value="<%= school_year_list[0]%>" ><%= school_year_list[0]%></option>
					<option value="<%= school_year_list[1]%>" ><%= school_year_list[1]%></option>
					<option value="<%= school_year_list[2]%>" ><%= school_year_list[2]%></option>
					<option value="<%= school_year_list[3]%>" ><%= school_year_list[3]%></option>
		        </select>
		        </span>
		    <span><a href="<%= basepath + "/LaboratoryServlet?method=webList"%>" class="return"><img src="images/return.gif" /></a></span>
			<div class="clear"></div>
			<div class="subject_content2">
			<table>
			<tr>
				<td class="basic">基本信息</td>
				<td>
				<ul>
					<span>系别</span>：<%= DepartmentDAO.getName(laboratory.getDepartmentID())%>
                </ul>
				<ul>
                	<span>实验室名称</span>：<%= laboratory.getName()%>
                </ul>
                <ul>
                	<span>实验室地址</span>：<%= laboratory.getAddress()%>
                </ul>
                <ul>
                	<span>实验室负责人</span>：<%= laboratory.getAdmin()%>
                </ul>
                <ul>
                	<span>实验室类型</span>：<%= laboratory.getType()%>
                </ul>
                <ul>
                	<span>开课专业</span>：<%= laboratory.getSpecialty()%>
                </ul>
                <ul>
                	<span>开设课程</span>：<%= laboratory.getCourse()%>
                </ul>
                <ul>
                	<span>实验室设备</span>：<%= laboratory.getEquipment() == null ? "" : laboratory.getEquipment()%>
                </ul>
                </td>
            </tr>
            <tr>
            	<td class="infor">实验室照片</td>
				<td class="picList">
				<% if((laboratory.getPic1() != null && !"".equals(laboratory.getPic1())) || (laboratory.getPic2() != null && !"".equals(laboratory.getPic2())) || (laboratory.getPic1() != null && !"".equals(laboratory.getPic3()))){ %>
				<% if(laboratory.getPic1() != null && !"".equals(laboratory.getPic1())){%><span class="img_pos"><a href="<%= basepath + "/" + laboratory.getPic1()%>" target="_blank"><img src="<%= basepath + "/" + laboratory.getPic1()%>" /></a></span><%}%>
				<% if(laboratory.getPic2() != null && !"".equals(laboratory.getPic2())){%><span class="img_pos"><a href="<%= basepath + "/" + laboratory.getPic2()%>" target="_blank"><img src="<%= basepath + "/" + laboratory.getPic2()%>" /></a></span><%}%>
				<% if(laboratory.getPic3() != null && !"".equals(laboratory.getPic3())){%><span class="img_pos"><a href="<%= basepath + "/" + laboratory.getPic3()%>" target="_blank"><img src="<%= basepath + "/" + laboratory.getPic3()%>" /></a></span><%}%>
				<%}else{ %>
					<span style="padding-left:10px;">暂无图片</span>
				<%} %>
				</td>
            </tr>
            <tr>
				<td class="infor">实验安排</td>
				<td class="a3">
				<ul class="anPaiList">
				<%	int totalPages = ExperimentDAO.getTotalPages(id, currenTerm, 10); 
					String ex_string = ExperimentDAO.index_list_obl(id, currenTerm, 1, 10);
					for(int ii=1; ii<=totalPages; ii++){
				%>
					<li><%= ii%></li>
				<%	}%>
				</ul>
				<div class="contentBox">
					<%= ex_string%>
				</div>
				<%	if(ex_string.equals("<span style=\"padding-left:10px;\">暂无实验安排</span>")){%>
				<script type="text/javascript"> 
					$(".anPaiList").css("display", "none");
					$(".contentBox").css("margin-top", "0");
				</script>
				<%	} %>
				</td>
            </tr>
            <tr>
				<td class="infor">实验室信息</td>
				<td class="a3">
					<%-- <input type="button" value="查看基本属性" onclick="javascript:window.location.href='viewSysxx.jsp?id=<%= laboratory.getId() %>'" class="form_btn" style="width: 100px"/>
					<input type="button" value="查看基本情况" onclick="javascript:window.location.href='viewSysqk.jsp?id=<%= laboratory.getId() %>'" class="form_btn" style="width: 100px"/>
					<input type="button" value="查看经费情况" onclick="javascript:window.location.href='viewSysjf.jsp?id=<%= laboratory.getId() %>'" class="form_btn" style="width: 100px"/>
 --%>			<input type="button" value="查看基本属性" onclick="javascript:window.open('viewSysxx.jsp?id=<%= laboratory.getId() %>')" class="form_btn" style="width: 100px"/>
				    <input type="button" value="查看基本情况" onclick="javascript:window.open('viewSysqk.jsp?id=<%= laboratory.getId() %>')" class="form_btn" style="width: 100px"/>
				    <input type="button" value="查看经费情况" onclick="javascript:window.open('viewSysjf.jsp?id=<%= laboratory.getId() %>')" class="form_btn" style="width: 100px"/>
				</td>
            </tr>
          </table>
        </div>
      </div>
			<%@ include file="footer.jsp" %>
		</div>
	</div>
</div>
<span id="ex_term" style="display:none"><%= currenTerm%></span>
<span id="ex_id" style="display:none"><%= id%></span>
</body>
</html>