package com.ltms.servlet;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import com.ltms.dao.LogDAO;
import com.ltms.dao.NotificationDAO;
import com.ltms.model.Admin;
import com.ltms.model.Notification;
import com.ltms.util.DatabaseUtil;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class NotificationServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		this.doPost(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String method = request.getParameter("method");
		if(method == null || "".equals(method)) return;
		if(method.equals("list")) list(request, response);
		else if(method.equals("add")) add(request, response);
		else if(method.equals("delete")) delete(request, response);
		else if(method.equals("update")) update(request, response);
		else if(method.equals("search")) search(request, response);
		else if(method.equals("webList")) webList(request, response);
		else if(method.equals("view")) view(request, response);
		else return;
	}

	public void list(HttpServletRequest request, HttpServletResponse response)
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
		ArrayList<Notification> notificationList = new ArrayList<Notification>();
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
			String countSQL = "select count(*) from _notification";
			String sql = "select * from _notification order by date desc";
			rsCount = DatabaseUtil.executeQuery(stmtCount, countSQL);
			int totalRecords;
			rsCount.next();
			totalRecords = rsCount.getInt(1);
			totalPages = (totalRecords + pageSize - 1)/pageSize;
			if(totalPages == 0){
				totalPages = 1;
			}
			if(pageNo > totalPages) pageNo = totalPages;
			int startPos = (pageNo-1) * pageSize; 
			sql +=" limit ?, ?";
			pstmt = DatabaseUtil.prepareStmt(conn, sql);
			pstmt.setInt(1, startPos);
			pstmt.setInt(2, pageSize);
			rs = pstmt.executeQuery();
			while(rs.next()) {
				Notification t = new Notification();
				NotificationDAO.initFromRS(rs, t);
				notificationList.add(t);
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
			request.setAttribute("notificationList", notificationList);
			request.getRequestDispatcher("listNotification.jsp").forward(request, response);
		}
	}
	
	public void add(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException{
		String title = request.getParameter("title");
		String content = request.getParameter("content");
		String author = request.getParameter("author") == null ? "" : request.getParameter("author");
		Connection conn = DatabaseUtil.getConn();
		String sql = "insert into _notification values(null, ?, ?, now(), ?)";
		PreparedStatement pstmt = DatabaseUtil.prepareStmt(conn, sql);
		try {
			int i = 1;
			pstmt.setString(i++, title);
			pstmt.setString(i++, content);
			pstmt.setString(i++, author);
			pstmt.executeUpdate();	
			request.setAttribute("msg", "添加成功");
			LogDAO.add((String)request.getSession().getAttribute("admin_name"), "添加通知公告文章[" + title + "]");
			request.getRequestDispatcher("NotificationServlet?method=list").forward(request, response);
		} catch (SQLException e) {
			e.printStackTrace();
			request.setAttribute("msg", "添加失败!");
			request.getRequestDispatcher("error.jsp").forward(request, response);
			return;
		}finally{
			DatabaseUtil.close(pstmt);
			DatabaseUtil.close(conn);
		}
	}
	

	public void delete(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException{
		int id = Integer.parseInt(request.getParameter("id"));
		int role = ((Admin)request.getSession().getAttribute("admin")).getRole();
		if(role == 0){
			Connection conn = DatabaseUtil.getConn();
			String sql = "delete from _notification where id = ?";
			PreparedStatement pstmt = DatabaseUtil.prepareStmt(conn, sql);
			try {
				pstmt.setInt(1, id);
				pstmt.executeUpdate();	
				request.setAttribute("msg", "删除成功");
				LogDAO.add((String)request.getSession().getAttribute("admin_name"), "删除通知公告[ID " + id + "]");
				request.getRequestDispatcher("NotificationServlet?method=list").forward(request, response);
			} catch (SQLException e) {
				e.printStackTrace();
				request.setAttribute("msg", "删除失败!");
				request.getRequestDispatcher("error.jsp").forward(request, response);
				return;
			}finally{
				DatabaseUtil.close(pstmt);
				DatabaseUtil.close(conn);
			}
		}else{
			request.setAttribute("msg", "对不起，您不是系统管理员，无法执行该操作!");
			request.getRequestDispatcher("error.jsp").forward(request, response);
			return;
		}
	}

	
	public void update(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException{
		int id = Integer.parseInt(request.getParameter("id"));
		int role = ((Admin)request.getSession().getAttribute("admin")).getRole();
		if(role == 0){
			String title = request.getParameter("title");
			String content = request.getParameter("content");
			String author = request.getParameter("author") == null ? "" : request.getParameter("author");
			Connection conn = DatabaseUtil.getConn();
			String sql = "update _notification set title = ?, content = ?, author = ? where id = ?";
				PreparedStatement pstmt = null;
				try {
					pstmt = DatabaseUtil.prepareStmt(conn, sql);					
					int i = 1;
					pstmt.setString(i++, title);
					pstmt.setString(i++, content);
					pstmt.setString(i++, author);
					pstmt.setInt(i++, id);
					pstmt.executeUpdate();	
					request.setAttribute("id", id);
					request.setAttribute("msg", "修改成功");
					LogDAO.add((String)request.getSession().getAttribute("admin_name"), "修改通知公告[" + title + "]");
					request.getRequestDispatcher("editNotification.jsp").forward(request, response);
				}catch (SQLException e) {
					e.printStackTrace();
					request.setAttribute("msg", "修改失败!");
					request.getRequestDispatcher("error.jsp").forward(request, response);
					return;
				}finally{
					DatabaseUtil.close(pstmt);
					DatabaseUtil.close(conn);
				}
		}else{
			request.setAttribute("msg", "对不起，您不是系统管理员，无法执行该操作!");
			request.getRequestDispatcher("error.jsp").forward(request, response);
			return;
		}
	}
	
	
	public void search(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException{
		Connection conn = DatabaseUtil.getConn();
		PreparedStatement pstmt = null;
		String keyword = request.getParameter("keyword");
		String sql = "select * from _notification where title like ? or content like ? or author like ? order by date desc";
		ResultSet rs = null;
		try {
			pstmt = DatabaseUtil.prepareStmt(conn, sql);
			int i = 1;
			pstmt.setString(i++, "%" + keyword + "%");
			pstmt.setString(i++, "%" + keyword + "%");
			pstmt.setString(i++, "%" + keyword + "%");
			rs = pstmt.executeQuery();
			ArrayList<Notification> notificationList = new ArrayList<Notification>();
			while(rs.next()) {
				Notification t = new Notification();
				NotificationDAO.initFromRS(rs, t);
				notificationList.add(t);
			}	
			request.setAttribute("notificationList", notificationList);
			request.setAttribute("search", "search");
			request.setAttribute("msg", "搜索结果如下");
			request.getRequestDispatcher("listNotification.jsp").forward(request, response);
		}catch (SQLException e) {
			e.printStackTrace();
		}finally{		
			DatabaseUtil.close(rs);
			DatabaseUtil.close(pstmt);
			DatabaseUtil.close(conn);	
		}
	}
	
	// 显示前台列表
	public void webList(HttpServletRequest request, HttpServletResponse response)
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
		ArrayList<Notification> notificationList = new ArrayList<Notification>();
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
			String countSQL = "select count(*) from _notification";
			String sql = "select * from _notification order by date desc";
			rsCount = DatabaseUtil.executeQuery(stmtCount, countSQL);
			int totalRecords;
			rsCount.next();
			totalRecords = rsCount.getInt(1);
			totalPages = (totalRecords + pageSize - 1)/pageSize;
			if(totalPages == 0){
				totalPages = 1;
			}
			if(pageNo > totalPages) pageNo = totalPages;
			int startPos = (pageNo-1) * pageSize; 
			sql +=" limit ?, ?";
			pstmt = DatabaseUtil.prepareStmt(conn, sql);
			pstmt.setInt(1, startPos);
			pstmt.setInt(2, pageSize);
			rs = pstmt.executeQuery();
			while(rs.next()) {
				Notification t = new Notification();
				NotificationDAO.initFromRS(rs, t);
				notificationList.add(t);
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
			request.setAttribute("notificationList", notificationList);
			request.getRequestDispatcher("listNotification.jsp").forward(request, response);
		}
	}
	
	public void view(HttpServletRequest request, HttpServletResponse response)
	throws ServletException, IOException{
		try{
			int id = Integer.parseInt(request.getParameter("id"));
			Notification notification = NotificationDAO.load(id);
			request.setAttribute("notification", notification);
			request.getRequestDispatcher("viewNotification.jsp").forward(request, response);
		}catch(Exception e){
			e.printStackTrace();
			request.setAttribute("msg", "浏览失败!");
			request.getRequestDispatcher("error.jsp").forward(request, response);
			return;
		}
	}	
}
