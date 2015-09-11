package com.ltms.servlet;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import com.ltms.dao.LogDAO;
import com.ltms.dao.TeacherDAO;
import com.ltms.model.Log;
import com.ltms.model.Teacher;
import com.ltms.util.DatabaseUtil;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LogServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		this.doPost(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String method = request.getParameter("method");
		if (method == null || "".equals(method))
			return;
		if (method.equals("list"))
			search_log(request, response);
		else if (method.equals("search_log"))
			search_log(request, response);
		return;
	}


	public void search_log(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		int pageNo = 1;
		int sign = 0;
		String state_sign = request.getParameter("state_sign");
		String name = request.getParameter("name");
		String strPageNo = request.getParameter("pageNo");
		if (strPageNo != null && !strPageNo.trim().equals("")) {
			try {
				pageNo = Integer.parseInt(strPageNo);
			} catch (NumberFormatException e) {
				pageNo = 1;
			}
		}
		if (pageNo <= 0)
			pageNo = 1;
		ArrayList<Log> logList = new ArrayList<Log>();
		Connection conn = DatabaseUtil.getConn();
		ResultSet rsCount = null;
		PreparedStatement pstmt = null;
		Statement stmtCount = null;
		Statement stmt = DatabaseUtil.createStmt(conn);
		ResultSet rs = null;
		int totalPages = 0;
		int pageSize = 20; // 设置每页显示多少条
		try {
			
			if(name != null){
				if(state_sign!=null){
					byte[] b = name.getBytes("ISO-8859-1");
					name = new String(b,"utf-8");
				}
				
				//查询的是时候   sign是1
				sign = 1;
				stmtCount = DatabaseUtil.createStmt(conn);
				String sql = "select * from _log where operator like ? ";
				
				pstmt = DatabaseUtil.prepareStmt(conn, sql);
				pstmt.setString(1, "%" + name + "%");
				rs = pstmt.executeQuery();
				rs.last();  //到最后一行
				int totalRecords;
				totalRecords = rs.getRow();  //获取当前行号
				
				
				totalPages = (totalRecords + pageSize - 1) / pageSize;
				if (totalPages == 0) {
					totalPages = 1;
				}
				if (pageNo > totalPages)
					pageNo = totalPages;
				int startPos = (pageNo - 1) * pageSize;
				
				sql += " order by datetime desc limit ?, ?";
				pstmt = DatabaseUtil.prepareStmt(conn, sql);
				pstmt.setString(1, "%" + name + "%");
				pstmt.setInt(2, startPos);
				pstmt.setInt(3, pageSize);
				rs = pstmt.executeQuery();
				
				while (rs.next()) {
					Log t = new Log();
					LogDAO.initFromRS(rs, t);
					logList.add(t);
				}
				
				
			}else{
				name = "";
				stmtCount = DatabaseUtil.createStmt(conn);
				String countSQL = "select count(*) from _log";
				String sql = "select * from _log order by datetime desc";
				rsCount = DatabaseUtil.executeQuery(stmtCount, countSQL);
				int totalRecords;
				rsCount.next();
				totalRecords = rsCount.getInt(1);
				totalPages = (totalRecords + pageSize - 1) / pageSize;
				if (totalPages == 0) {
					totalPages = 1;
				}
				if (pageNo > totalPages)
					pageNo = totalPages;
				int startPos = (pageNo - 1) * pageSize;
				sql += " limit ?, ?";
				pstmt = DatabaseUtil.prepareStmt(conn, sql);
				pstmt.setInt(1, startPos);
				pstmt.setInt(2, pageSize);
				rs = pstmt.executeQuery();
				while (rs.next()) {
					Log t = new Log();
					LogDAO.initFromRS(rs, t);
					logList.add(t);
				}
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DatabaseUtil.close(stmtCount);
			DatabaseUtil.close(pstmt);
			DatabaseUtil.close(rsCount);
			DatabaseUtil.close(rs);
			DatabaseUtil.close(stmt);
			DatabaseUtil.close(conn);
			request.setAttribute("name", name);
			request.setAttribute("sign", String.valueOf(sign));
			request.setAttribute("pageNo", pageNo);
			request.setAttribute("totalPages", totalPages);
			request.setAttribute("logList", logList);
			request.getRequestDispatcher("listLog.jsp").forward(request,
					response);
		}
	}

	public void list(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		int pageNo = 1;
		String strPageNo = request.getParameter("pageNo");
		if (strPageNo != null && !strPageNo.trim().equals("")) {
			try {
				pageNo = Integer.parseInt(strPageNo);
			} catch (NumberFormatException e) {
				pageNo = 1;
			}
		}
		if (pageNo <= 0)
			pageNo = 1;
		ArrayList<Log> logList = new ArrayList<Log>();
		Connection conn = DatabaseUtil.getConn();
		ResultSet rsCount = null;
		PreparedStatement pstmt = null;
		Statement stmtCount = null;
		Statement stmt = DatabaseUtil.createStmt(conn);
		ResultSet rs = null;
		int totalPages = 0;
		int pageSize = 20; // 设置每页显示多少条
		try {
			stmtCount = DatabaseUtil.createStmt(conn);
			String countSQL = "select count(*) from _log";
			String sql = "select * from _log order by datetime desc";
			rsCount = DatabaseUtil.executeQuery(stmtCount, countSQL);
			int totalRecords;
			rsCount.next();
			totalRecords = rsCount.getInt(1);
			totalPages = (totalRecords + pageSize - 1) / pageSize;
			if (totalPages == 0) {
				totalPages = 1;
			}
			if (pageNo > totalPages)
				pageNo = totalPages;
			int startPos = (pageNo - 1) * pageSize;
			sql += " limit ?, ?";
			pstmt = DatabaseUtil.prepareStmt(conn, sql);
			pstmt.setInt(1, startPos);
			pstmt.setInt(2, pageSize);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				Log t = new Log();
				LogDAO.initFromRS(rs, t);
				logList.add(t);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DatabaseUtil.close(stmtCount);
			DatabaseUtil.close(pstmt);
			DatabaseUtil.close(rsCount);
			DatabaseUtil.close(rs);
			DatabaseUtil.close(stmt);
			DatabaseUtil.close(conn);
			request.setAttribute("pageNo", pageNo);
			request.setAttribute("totalPages", totalPages);
			request.setAttribute("logList", logList);
			request.getRequestDispatcher("listLog.jsp").forward(request,
					response);
		}
	}
}
