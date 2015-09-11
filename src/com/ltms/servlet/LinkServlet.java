package com.ltms.servlet;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.ltms.util.DatabaseUtil;

public class LinkServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		this.doPost(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String method = request.getParameter("method");
		if(method == null || "".equals(method)) return;
		if(method.equals("add")) add(request, response); 
		else if(method.equals("update")) update(request, response);
		else if(method.equals("delete")) delete(request, response);
		else return;
	}
	
	public void add(HttpServletRequest request, HttpServletResponse response)
	throws ServletException, IOException {
		Connection conn = DatabaseUtil.getConn();	
		String name = request.getParameter("name");
		String linkURL = request.getParameter("url");
		int sort = 10;
		try{
			sort = Integer.parseInt(request.getParameter("sort"));
		}catch(NumberFormatException e){
			sort = 10;
		}
		
		boolean result = false;
		if(name == null || linkURL == null || name.trim().equals("") || linkURL.trim().equals("")){
			request.setAttribute("msg", "友情链接名或者友情链接网址不能为空!");
			request.getRequestDispatcher("error.jsp").forward(request, response);
			return;
		}else{
			String sql = "insert into _link values(null, ?, ?, ?, '')";
			PreparedStatement pstmt = DatabaseUtil.prepareStmt(conn, sql);
			try {
				int i = 1;
				pstmt.setString(i++, name);
				pstmt.setString(i++, linkURL);
				pstmt.setInt(i++, sort);
				pstmt.executeUpdate();	
				result = true;
			} catch (SQLException e) {
				e.printStackTrace();
			}finally{
				DatabaseUtil.close(pstmt);
				DatabaseUtil.close(conn);
			}
		}
		if(result){
			request.setAttribute("msg", "添加成功!");
			request.getRequestDispatcher("link.jsp").forward(request, response);
			return;
		}else{
			request.setAttribute("msg", "添加友情链接失败!");
			request.getRequestDispatcher("error.jsp").forward(request, response);
			return;
		}
	}
	
	public void update(HttpServletRequest request, HttpServletResponse response)
	throws ServletException, IOException {
		Connection conn = DatabaseUtil.getConn();
		String id = request.getParameter("id");
		String name = request.getParameter("name");
		String url = request.getParameter("url");
		String strSort = request.getParameter("sort");
		int sort = 0;
		try{
			sort = Integer.parseInt(strSort);
		}catch(NumberFormatException e){
			sort = 10;
		}
		if(url == null || url.trim().equals("")){
			request.setAttribute("msg", "友情链接网址不能为空!");
			request.getRequestDispatcher("error.jsp").forward(request, response);
			return;	
		}else{
			String sql = "update _link set url = ?,sort = ?, name = ? where id = ?";
			PreparedStatement pstmt = DatabaseUtil.prepareStmt(conn, sql);
			try {
				int i = 1;
				pstmt.setString(i++, url);
				pstmt.setInt(i++, sort);
				pstmt.setString(i++, name);
				pstmt.setString(i++, id);
				pstmt.executeUpdate();
			} catch (SQLException e) {
				request.setAttribute("msg", "修改失败!");
				request.getRequestDispatcher("error.jsp").forward(request, response);
				return;
			}finally{
				DatabaseUtil.close(pstmt);
				DatabaseUtil.close(conn);
			}
			request.setAttribute("msg", "修改成功!");
			request.getRequestDispatcher("link.jsp").forward(request, response);
			return;
		}
	}
			
	public void delete(HttpServletRequest request, HttpServletResponse response)
	throws ServletException, IOException {
		Connection conn = DatabaseUtil.getConn();
		String id = request.getParameter("id");
		Statement stmt = DatabaseUtil.createStmt(conn);
		try {
			stmt.executeUpdate("delete from _link where id = '" + id + "'");
		} catch (SQLException e) {
			request.setAttribute("msg", "删除失败!");
			request.getRequestDispatcher("error.jsp").forward(request, response);
			return;
		}finally{
			DatabaseUtil.close(stmt);
			DatabaseUtil.close(conn);
		}
		request.setAttribute("msg", "删除成功!");
		request.getRequestDispatcher("link.jsp").forward(request, response);
		return;
	}
}
