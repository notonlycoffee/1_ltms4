package com.ltms.servlet;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Calendar;

import com.ltms.dao.LogDAO;
import com.ltms.dao.SystemConfigDAO;
import com.ltms.model.Admin;
import com.ltms.util.DatabaseUtil;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class SystemConfigServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		this.doPost(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String method = request.getParameter("method");
		if(method == null || "".equals(method)) return;
		else if(method.equals("update")) update(request, response);
		else if(method.equals("updateSysConf")) updateSysConf(request, response);
		else return;
	}
	
	public void update(HttpServletRequest request, HttpServletResponse response)
	throws ServletException, IOException{
		String term = request.getParameter("term");
		String year = request.getParameter("year");
		
		//获取当前的使用者id和role
		int role = ((Admin)request.getSession().getAttribute("admin")).getRole();
		String loginName = (String)request.getSession().getAttribute("admin_name");
		
		Connection conn = DatabaseUtil.getConn();
		PreparedStatement pstmt = null;
		
		String currenTerm__ = (String) request.getSession().getAttribute("currenTerm");
		
		
		try {
			
			if("".equals(currenTerm__) || currenTerm__.trim().length() == 0){
				
				
				//之前没记录的    这里是插入
				String sql = "insert into _systemConfig values(?,?,?,?)";
				pstmt = DatabaseUtil.prepareStmt(conn, sql);
				int i = 1;
				pstmt.setString(i++, loginName);
				pstmt.setInt(i++, role);
				pstmt.setString(i++, year);
				pstmt.setString(i++, term);
				pstmt.executeUpdate();	
				
			}else{
				String sql = "update _systemConfig set year = ?, term = ? where user_name = ? and role = ?";
				pstmt = DatabaseUtil.prepareStmt(conn, sql);
				int i = 1;
				pstmt.setString(i++, year);
				pstmt.setString(i++, term);
				
				pstmt.setString(i++, loginName);
				pstmt.setInt(i++, role);
				
				pstmt.executeUpdate();	
				
			}
			
			//更新session里面的记录
			String currenTerm = SystemConfigDAO.getCurrenTerm(loginName,role);
			request.getSession().setAttribute("currenTerm", currenTerm);
			request.setAttribute("msg", "设置成功!");
			LogDAO.add((String)request.getSession().getAttribute("admin_name"), "修改系统配置");
			request.getRequestDispatcher("systemConfig.jsp").forward(request, response);
			return;
		} catch (SQLException e) {
			e.printStackTrace();
			request.setAttribute("msg", "设置失败!");
			request.getRequestDispatcher("systemConfig.jsp").forward(request, response);
			return;
		}finally{
			DatabaseUtil.close(pstmt);
			DatabaseUtil.close(conn);
		}
	}
	public void updateSysConf(HttpServletRequest request, HttpServletResponse response)
	throws ServletException, IOException{
		String jxzs = request.getParameter("jxzs");
		String fjsj = request.getParameter("fjsj");
		
		//获取当前的使用者id和role
		int role = ((Admin)request.getSession().getAttribute("admin")).getRole();
		String loginName = (String)request.getSession().getAttribute("admin_name");
		
		Connection conn = DatabaseUtil.getConn();
		PreparedStatement pstmt = null;
		
		String jxzs_fjsj = (String) request.getSession().getAttribute("jxzs_fjsj");
		if(jxzs_fjsj == null)jxzs_fjsj="";
		
		try {
			
			if("".equals(jxzs_fjsj) || jxzs_fjsj.trim().length() == 0){
				
				
				//之前没记录的    这里是插入
				String sql = "insert into jxzs_fjsj values(?,?,?,?)";
				pstmt = DatabaseUtil.prepareStmt(conn, sql);
				int i = 1;
				pstmt.setString(i++, loginName);
				pstmt.setInt(i++, role);
				pstmt.setString(i++, jxzs);
				pstmt.setString(i++, fjsj);
				pstmt.executeUpdate();	
				
			}else{
				String sql = "update jxzs_fjsj set jxzs = ?, fjsj = ? where user_name = ? and role = ?";
				pstmt = DatabaseUtil.prepareStmt(conn, sql);
				int i = 1;
				pstmt.setString(i++, jxzs);
				pstmt.setString(i++, fjsj);
				
				pstmt.setString(i++, loginName);
				pstmt.setInt(i++, role);
				
				pstmt.executeUpdate();	
				
			}
			
			//更新session里面的记录
			String currenTerm = SystemConfigDAO.getJxzs_fjsj(loginName,role);
			request.getSession().setAttribute("jxzs_fjsj", jxzs_fjsj);
			request.setAttribute("msg", "设置成功!");
			LogDAO.add((String)request.getSession().getAttribute("admin_name"), "修改系统配置_jxzs_fjsj");
			request.getRequestDispatcher("systemConfig.jsp").forward(request, response);
			return;
		} catch (SQLException e) {
			e.printStackTrace();
			request.setAttribute("msg", "设置失败!");
			request.getRequestDispatcher("systemConfig.jsp").forward(request, response);
			return;
		}finally{
			DatabaseUtil.close(pstmt);
			DatabaseUtil.close(conn);
		}
	}
}
