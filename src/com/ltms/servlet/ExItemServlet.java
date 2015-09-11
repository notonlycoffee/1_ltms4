package com.ltms.servlet;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.ltms.dao.ClassDAO;
import com.ltms.dao.CourseDAO;
import com.ltms.dao.DepartmentDAO;
import com.ltms.dao.ExItemDAO;
import com.ltms.dao.ExperimentDAO;
import com.ltms.dao.LaboratoryDAO;
import com.ltms.dao.ScheduleDAO;
import com.ltms.dao.TeacherDAO;
import com.ltms.model.ExItem;
import com.ltms.model.Experiment;
import com.ltms.model.Schedule;
import com.ltms.model._Class;
import com.ltms.util.DatabaseUtil;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ExItemServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		this.doPost(request, response);
	}
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		String method = request.getParameter("method");
		if(method == null || "".equals(method)) return;
		//进入实验教学页面
		if(method.equals("webList")) webList(request, response);
		else if(method.equals("webList_more")) webList_more(request, response);
		else if(method.equals("view")) view(request, response);
		//列出实验进度表
		else if(method.equals("list_obl")) list_obl(request, response);
		//else if(method.equals("删除")) delete(request, response);
		//else if(method.equals("edit")) edit(request, response);
		//else if(method.equals("modify")) modify(request, response);
		//else if(method.equals("search")) search(request, response);
		else return;
	}
	
	public void webList_more(HttpServletRequest request, HttpServletResponse response)
	throws ServletException, IOException{		
		int pageNo = 1;
		String strPageNo = request.getParameter("pageNo");
		if(strPageNo != null && !strPageNo.trim().equals("")) {
			try {
				pageNo = Integer.parseInt(strPageNo);
			} catch (NumberFormatException e) {
				pageNo = 1;
			} 
		}
		if(pageNo <= 0) pageNo = 1;
		ArrayList<ExItem> exItemList = new ArrayList<ExItem>();
		Map<Integer, String> laboratoryNameMap = new HashMap<Integer, String>();
		Connection conn = DatabaseUtil.getConn();
		ResultSet rsCount = null;
		PreparedStatement pstmt = null;
		Statement stmtCount = null;
		Statement stmt = DatabaseUtil.createStmt(conn);
		ResultSet rs = null;	
		int totalPages = 0;
		int pageSize = 15;  //设置每页显示多少条
		try {
			stmtCount = DatabaseUtil.createStmt(conn);
			String countSQL = "select count(*) from _exItem where itemName != ''";
			String sql = "select * from _exItem where itemName != '' order by id desc";
			rsCount = DatabaseUtil.executeQuery(stmtCount, countSQL);
			int totalRecords;
			rsCount.next();
			totalRecords = rsCount.getInt(1);
			totalPages = (totalRecords + pageSize - 1)/pageSize;
			if(totalPages == 0){
				totalPages = 1;
			}
			if(pageNo > totalPages) pageNo = totalPages;
			int startPos = (pageNo - 1) * pageSize; 
			sql +=" limit ?, ?";
			pstmt = DatabaseUtil.prepareStmt(conn, sql);
			pstmt.setInt(1, startPos);
			pstmt.setInt(2, pageSize);
			rs = pstmt.executeQuery();
			while(rs.next()) {
				ExItem t = new ExItem();
				ExItemDAO.initFromRS(rs, t);
				exItemList.add(t);
			}	
			//取对应实验室名
			int listLen = exItemList.size();
			sql = "select id, name from _laboratory where id in (?";
			for(int i = 0; i< listLen; i++){
				sql += ", ?";
			}
			sql += ")";
			pstmt = DatabaseUtil.prepareStmt(conn, sql);
			pstmt.setString(1, "0");
			for(int i = 0; i< listLen; i++){
				pstmt.setInt(i+2, exItemList.get(i).getLaboratoryID());
			}
			rs = pstmt.executeQuery();
			while(rs.next()){
				laboratoryNameMap.put(rs.getInt("id"), rs.getString("name"));
			}
		}catch (SQLException e) {
			e.printStackTrace();
		}finally{		
			DatabaseUtil.close(stmtCount);
			DatabaseUtil.close(pstmt);
			DatabaseUtil.close(rsCount);
			DatabaseUtil.close(rs);
			DatabaseUtil.close(stmt);
			DatabaseUtil.close(conn);	
			request.setAttribute("pageNo", pageNo);
			request.setAttribute("totalPages", totalPages);
			request.setAttribute("exItemList", exItemList);
			request.setAttribute("laboratoryNameMap", laboratoryNameMap);
			request.getRequestDispatcher("listExItem.jsp").forward(request, response);
		}
	}
	
	public void webList(HttpServletRequest request, HttpServletResponse response)
	throws ServletException, IOException{
		
		String year_weblist = request.getParameter("year_weblist");
		String term_weblist = request.getParameter("term_weblist");
		
		if(year_weblist.equals("0") || year_weblist.trim().length() == 0){
			year_weblist = "";
		}
		if(term_weblist.equals("0") || year_weblist.trim().length() == 0){
			term_weblist = "";
		}
		
		String term_ = year_weblist+term_weblist;
		
		
		int pageNo = 1;
		String strPageNo = request.getParameter("pageNo");
		if(strPageNo != null && !strPageNo.trim().equals("")) {
			try {
				pageNo = Integer.parseInt(strPageNo);
			} catch (NumberFormatException e) {
				pageNo = 1;
			} 
		}
		if(pageNo <= 0) pageNo = 1;
		ArrayList<ExItem> exItemList = new ArrayList<ExItem>();
		Map<Integer, String> laboratoryNameMap = new HashMap<Integer, String>();
		Connection conn = DatabaseUtil.getConn();
		ResultSet rsCount = null;
		PreparedStatement pstmt = null;
		Statement stmtCount = null;
		Statement stmt = DatabaseUtil.createStmt(conn);
		ResultSet rs = null;	
		int totalPages = 0;
		int pageSize = 15;  //设置每页显示多少条
		try {
			stmtCount = DatabaseUtil.createStmt(conn);
			String countSQL = "select count(*) from _exItem,_schedule where itemName != '' and term like %" + term_ + "%";
			String sql = "select * from _exItem,_schedule where itemName != '' and term like %" + term_ + "% order by id desc";
			rsCount = DatabaseUtil.executeQuery(stmtCount, countSQL);
			int totalRecords;
			rsCount.next();
			totalRecords = rsCount.getInt(1);
			totalPages = (totalRecords + pageSize - 1)/pageSize;
			if(totalPages == 0){
				totalPages = 1;
			}
			if(pageNo > totalPages) pageNo = totalPages;
			int startPos = (pageNo - 1) * pageSize; 
			sql +=" limit ?, ?";
			pstmt = DatabaseUtil.prepareStmt(conn, sql);
			pstmt.setInt(1, startPos);
			pstmt.setInt(2, pageSize);
			rs = pstmt.executeQuery();
			while(rs.next()) {
				ExItem t = new ExItem();
				ExItemDAO.initFromRS(rs, t);
				exItemList.add(t);
			}	
			//取对应实验室名
			int listLen = exItemList.size();
			sql = "select id, name from _laboratory where id in (?";
			for(int i = 0; i< listLen; i++){
				sql += ", ?";
			}
			sql += ")";
			pstmt = DatabaseUtil.prepareStmt(conn, sql);
			pstmt.setString(1, "0");
			for(int i = 0; i< listLen; i++){
				pstmt.setInt(i+2, exItemList.get(i).getLaboratoryID());
			}
			rs = pstmt.executeQuery();
			while(rs.next()){
				laboratoryNameMap.put(rs.getInt("id"), rs.getString("name"));
			}
		}catch (SQLException e) {
			e.printStackTrace();
		}finally{		
			DatabaseUtil.close(stmtCount);
			DatabaseUtil.close(pstmt);
			DatabaseUtil.close(rsCount);
			DatabaseUtil.close(rs);
			DatabaseUtil.close(stmt);
			DatabaseUtil.close(conn);	
			request.setAttribute("pageNo", pageNo);
			request.setAttribute("totalPages", totalPages);
			request.setAttribute("exItemList", exItemList);
			request.setAttribute("laboratoryNameMap", laboratoryNameMap);
			request.getRequestDispatcher("listExItem.jsp").forward(request, response);
		}
	}
	
	public void list_obl(HttpServletRequest request, HttpServletResponse response)
	throws ServletException, IOException{
		
		String year_weblist = request.getParameter("year_weblist");
		String term_weblist = request.getParameter("term_weblist");
		String _lID_ = request.getParameter("laboratoryID");
		
		System.out.println(year_weblist + " " + term_weblist + " " + _lID_);
		if(year_weblist == null || term_weblist == null || _lID_ == null 
				|| "null".equals(year_weblist) || "null".equals(term_weblist) || "null".equals(_lID_)){
			System.out.println("enter");
			year_weblist = (String) request.getSession().getAttribute("year_weblist");
			term_weblist = (String) request.getSession().getAttribute("term_weblist");
//			_lID_ = (String) request.getSession().getAttribute("laboratoryID");
		}else{
			request.getSession().setAttribute("year_weblist", year_weblist);
			request.getSession().setAttribute("term_weblist", term_weblist);
//			request.getSession().setAttribute("laboratoryID", _lID_);
		}
		System.out.println("id is " + _lID_);
		int laboratoryID = Integer.parseInt(_lID_);
		if(year_weblist.equals("0") || year_weblist.trim().length() == 0 || year_weblist == null){
			year_weblist = "";
		}
		if(term_weblist.equals("0") || term_weblist.trim().length() == 0 || term_weblist == null){
			term_weblist = "";
		}
		
		String term_ = year_weblist+term_weblist;
		System.out.println("term is " + term_);
		
		int pageNo = 1;
		String strPageNo = request.getParameter("pageNo");
		if(strPageNo != null && !strPageNo.trim().equals("")) {
			try {
				pageNo = Integer.parseInt(strPageNo);
			} catch (NumberFormatException e) {
				pageNo = 1;
			} 
		}
		if(pageNo <= 0) pageNo = 1;
		ArrayList<ExItem> exItemList = new ArrayList<ExItem>();
		Connection conn = DatabaseUtil.getConn();
		ResultSet rsCount = null;
		PreparedStatement pstmt = null;
		Statement stmtCount = null;
		Statement stmt = DatabaseUtil.createStmt(conn);
		ResultSet rs = null;	
		int totalPages = 0;
		int pageSize = 15;  //设置每页显示多少条
		try {
			stmtCount = DatabaseUtil.createStmt(conn);//
			String countSQL = "select count(*) from _exItem,_schedule where _exItem.scheduleID = _schedule.id and _exItem.itemName != '' and _exItem.laboratoryID = " + laboratoryID + " and _schedule.term like '%" + term_ +"%'";
			String sql = "select * from _exItem,_schedule where _exItem.scheduleID = _schedule.id and _exItem.itemName != '' and _schedule.term like ? and _exItem.laboratoryID = " + laboratoryID + " order by _exItem.id desc";
			rsCount = DatabaseUtil.executeQuery(stmtCount, countSQL);
			System.out.println("rscount is " + rsCount);
			int totalRecords;
			rsCount.next();
			totalRecords = rsCount.getInt(1);
			totalPages = (totalRecords + pageSize - 1)/pageSize;
			System.out.println("totalRecords is " + totalRecords);
			if(totalPages == 0){
				totalPages = 1;
			}
			if(pageNo > totalPages) pageNo = totalPages;
			int startPos = (pageNo - 1) * pageSize; 
			sql +=" limit ?, ?";
			pstmt = DatabaseUtil.prepareStmt(conn, sql);
			pstmt.setString(1, "%" + term_ + "%");
			pstmt.setInt(2, startPos);
			pstmt.setInt(3, pageSize);
			rs = pstmt.executeQuery();
			while(rs.next()) {
				ExItem t = new ExItem();
				ExItemDAO.initFromRS(rs, t);
				exItemList.add(t);
			}	
		}catch (SQLException e) {
			e.printStackTrace();
		}finally{		
			DatabaseUtil.close(stmtCount);
			DatabaseUtil.close(pstmt);
			DatabaseUtil.close(rsCount);
			DatabaseUtil.close(rs);
			DatabaseUtil.close(stmt);
			DatabaseUtil.close(conn);	
			request.setAttribute("pageNo", pageNo);
			request.setAttribute("totalPages", totalPages);
			request.setAttribute("exItemList", exItemList);
			request.setAttribute("laboratoryID", laboratoryID);
			request.getRequestDispatcher("listExItem_obl.jsp").forward(request, response);
		}
	}
	
	//读出一个实验项目  转到viewItem.jsp
	public void view(HttpServletRequest request, HttpServletResponse response)
	throws ServletException, IOException{
		int id = Integer.parseInt(request.getParameter("id"));
		int scheduleID =  Integer.parseInt(request.getParameter("scheduleID"));
		ExItem ei = ExItemDAO.load(id);
		if(ei == null){
			request.setAttribute("msg", "查看失败!");
			request.getRequestDispatcher("error.jsp").forward(request, response);
			return;
		}
		Schedule schedule = ScheduleDAO.load(scheduleID);
		Experiment experiment = ExperimentDAO.load(schedule.getExperimentID());
		String classID = schedule.getClassID();
//		System.out.println("id is " + classID);
		String message = "";
		String[] classes = classID.split("@");
//		System.out.println("length is " + classes.length);
		for(int i=0; i<classes.length; i++){
			_Class clazz = ClassDAO.load(classes[i]);
			
			if(classes[i].equals("theendofthestring")){
				break;
			}
			message += clazz.getId() + " " + clazz.getName();
		}
//		System.out.println("message is " + message);
//		_Class clazz = ClassDAO.load(classID);
		String teacherID = schedule.getTeacherID();
		request.setAttribute("laboratoryName", LaboratoryDAO.getName(experiment.getLaboratoryID()));
		request.setAttribute("department", DepartmentDAO.getName(experiment.getDepartmentID()));
		request.setAttribute("courseName", CourseDAO.getName(experiment.getCourseID()));
//		request.setAttribute("class", clazz.getId() + " " + clazz.getName());
		request.setAttribute("class", message);
		request.setAttribute("teacher", TeacherDAO.getName(teacherID));
		request.setAttribute("schedule", schedule);	
		request.setAttribute("experiment", experiment);
		request.setAttribute("exItem", ei);
		request.getRequestDispatcher("viewExItem.jsp").forward(request, response);
	}
}
