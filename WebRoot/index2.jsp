<%@ page language="java" pageEncoding="utf-8" %>
<%@ page import="java.util.*, com.ltms.model.*, java.sql.*, com.ltms.dao.*,com.ltms.util.*" %>
<%@ include file="global.jsp" %>
<%
	ArrayList<Notification> notificationList = NotificationDAO.list(9);
	ArrayList<Regulation> regulationList = RegulationDAO.list(7);
	ArrayList<ExItem> exItemList = ExItemDAO.indexList(10);
	ArrayList<Laboratory> laboratorymList = LaboratoryDAO.indexList(10);
	Map<Integer, String> departmentNameMap = DepartmentDAO.getNameMap();
	Map<Integer, String> laboratoryNameMap = ExItemDAO.getLaboratoryNameMap(exItemList);
	Map<Integer, String> laboratoryNameMap2 = RegulationDAO.getLaboratoryNameMap(regulationList);
	ArrayList<KeyLab> keyLabList = KeyLabDAO.list();
	List<Link> linkList = (ArrayList<Link>)session.getAttribute("linkList");	
	if(linkList == null){
		linkList = LinkDAO.list();
		session.setAttribute("linkList", linkList);
	}	
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link href="<%= basepath%>/css/base.css" rel="stylesheet" type="text/css" />
<link href="<%= basepath%>/css/home.css" rel="stylesheet" type="text/css" />
<script src="js/jquery.min.js"></script>
<script src="<%= basepath%>/js/jquery.easing.min.js"></script>
<script src="<%= basepath%>/js/base.js"></script>
<script src="<%= basepath%>/js/swfobject.js"></script>
<title><%= site_name%></title>
</head>
<body>
<div class="contain">
<%@ include file="header.jsp" %>
<!-- ◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆-->   
<div class="stm_content">
	<div class="content">
		<p class="home_empty">&nbsp;</p>
		<div class="home">
<!-- ◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆-->   		
		<div class="home_banner_slide"> 
			<ul>
			<%	List<IndexBanner> indexBannerList = IndexBannerDAO.list();	
				int i = 1;
				for(IndexBanner ib : indexBannerList){%>
				<li style="<%= i == 1 ? "display:block" : ""%>"><a href="<%= ib.getUrl()%>" target="new_to_ib_<%= ib.getId()%>"><img src="<%= basepath%>/<%= ib.getBanner()%>" height="188" width="955"/></a></li>
			<%	i++;
				}%>
			</ul>
		</div>
<!-- ◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆-->   
		<div class="news_photo"> 
			<div class="slide_wrap" id="focus">
				<ul class="pic123"><!--显示最多是6条-->
				<%	List<Laboratory> picList = LaboratoryDAO.index_laboratory_pic(6);
					for(Laboratory l : picList){%>
					<li><a href="viewLaboratory.jsp?id=<%= l.getId()%>"><img src="<%= basepath%>/<%= l.getPic1()%>" alt="<%= l.getName()%>" /></a></li>
				<%	}%>
				</ul>
			</div>
		</div>
<!-- ◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆-->   
		<div class="notice">
			<div class="scan_title">
				<h2>实验室通知</h2>
				<a href="<%= basepath%>/NotificationServlet?method=webList">more</a>
			</div>
			<ul>
				<%	for(Notification notification : notificationList){	%>
				<li><a href="<%= basepath%>/NotificationServlet?method=view&id=<%= notification.getId()%>">&nbsp;<%= notification.getTitle()%></a><span><%= notification.getDate()%></span></li>
				<%	}%>
			</ul>
        </div>
<!-- ◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆--> 
        <div class="clear"></div>
        <div class="sys_scan">
	        	<div class="scan_list">
	            <div class="scan_title">
	            	<h1>实验室一览</h1>
	            	<a href="<%= basepath%>/LaboratoryServlet?method=webList">more</a>
	            </div>
	            <ul>
	            	<%	for(Laboratory l : laboratorymList){	%>
					<li><a href="<%= basepath%>/viewLaboratory.jsp?id=<%= l.getId()%>"><%= l.getName()%></a><span>[<%= departmentNameMap.get(l.getDepartmentID())%>]</span></li>
					<%	}%>
				</ul>
			</div>
<!-- ◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆--> 
			<div class="scan_list ml5">
	            <div class="scan_title">
					<h1>实验教学</h1>
	            	<a href="<%= basepath%>/ExItemServlet?method=webList_more">more</a>
	            </div>
	          	<ul>
					<%	for(ExItem ei : exItemList){	%>
					<li><a href="<%= basepath%>/ExItemServlet?method=view&id=<%= ei.getId()%>&scheduleID=<%= ei.getScheduleID()%>"><%= ei.getItemName()%></a><span>[<%= laboratoryNameMap.get(ei.getLaboratoryID())%>]</span></li>
				 	<%}%>
				</ul>
			</div>
<!-- ◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆--> 		
			<div class="manage">
				<div class="scan_title">
	            	<h1>实验室管理制度</h1>
	            	<a href="<%= basepath%>/RegulationServlet?method=webList">more</a>
	            </div>
	         	<ul>
	         		<%	for(Regulation regulation : regulationList){%>
	         		<li><a href="<%= basepath%>/RegulationServlet?method=view&id=<%= regulation.getId() %>">&nbsp;[<%= laboratoryNameMap2.get(regulation.getLaboratoryID())%>]<%= regulation.getTitle()%></a><span><%= regulation.getDate()%></span></li>
					<%	}%>
				</ul>
			</div>
        </div>
<!-- ◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆--> 
		<div class="example">
			<div class="scan_title">
				<h2>省级示范中心</h2>
			</div>
			<ul class="line">
				<%	for(KeyLab keyLab : keyLabList){	%>
				<li><a href="<%= keyLab.getGotoURL() %>" target="new_to_keylab_ltms">
				<%	if(keyLab.getPic() != null && !"".equals(keyLab.getPic())){ %>
					<img src="<%= basepath + "/" + keyLab.getPic()%>" />
				<%	}else{ %>
					<img src="<%= basepath + "/images/example_center.png"%>" />
				<%	}%>
				<h1><%= keyLab.getName()%></h1></a></li>
				<%	}%>
			</ul>
        </div>
<!-- ◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆--> 
		<div class="clear"></div>
		<div class="connect">
			<p class="friend_connect">友情链接</p>
			<p class="ml15">
				<%	for(Link link : linkList){	%>
				<a target="_newlink<%= link.getId()%>" href="<%= link.getUrl()%>"><%= link.getName()%></a>
				<%	}%>  
			</p>
		</div>
        <%@ include file="footer.jsp" %>
		</div>
	</div>
</div>


</div>
</body>
</html>
