<%@ page language="java" import="java.util.*,com.ltms.model.*, com.ltms.dao.*" pageEncoding="utf-8"%>
<%
	String currenTerm = (String)session.getAttribute("currenTerm");
	
	//if(currenTerm == null){
	//	currenTerm = SystemConfigDAO.getCurrenTerm();
		//request.getSession().setAttribute("currenTerm", currenTerm);
	//}
	String basepath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath();
	String site_name = "实验室教学管理平台";
%>