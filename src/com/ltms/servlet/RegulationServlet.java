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

import com.ltms.dao.LaboratoryDAO;
import com.ltms.dao.LogDAO;
import com.ltms.dao.RegulationDAO;
import com.ltms.model.Admin;
import com.ltms.model.Regulation;
import com.ltms.util.DatabaseUtil;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class RegulationServlet extends HttpServlet {
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
		else if(method.equals("webList_obl")) webList_obl(request, response);
		else if(method.equals("webList_obd")) webList_obd(request, response);
		else if(method.equals("view")) view(request, response);
		else return;
	}

	public void list(HttpServletRequest request, HttpServletResponse response)
	throws ServletException, IOException{
		Admin admin = ((Admin)request.getSession().getAttribute("admin"));
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
		Map<Integer, String> laboratoryNameMap = new HashMap<Integer, String>();
		ArrayList<Regulation> regulationList = new ArrayList<Regulation>();
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
			String countSQL = "select count(*) from _regulation where departmentID = " + admin.getDepartmentID();
			String sql = "";
			if(admin.getRole() == 0){
				sql = "select * from _regulation order by date desc";
			}else{
				sql = "select * from _regulation where departmentID = " + admin.getDepartmentID() + " order by date desc";
			}
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
				Regulation t = new Regulation();
				RegulationDAO.initFromRS(rs, t);
				regulationList.add(t);
			}	
			//取对应实验室名
			int listLen = regulationList.size();
			sql = "select id, name from _laboratory where id in (?";
			for(int i = 0; i< listLen; i++){
				sql += ", ?";
			}
			sql += ")";
			pstmt = DatabaseUtil.prepareStmt(conn, sql);
			pstmt.setString(1, "0");
			for(int i = 0; i< listLen; i++){
				pstmt.setInt(i+2, regulationList.get(i).getLaboratoryID());
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
			request.setAttribute("laboratoryNameMap", laboratoryNameMap);
			request.setAttribute("regulationList", regulationList);
			request.getRequestDispatcher("listRegulation.jsp").forward(request, response);
		}
	}
	
	public void add(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException{
			String title = request.getParameter("title");
			String content = request.getParameter("content");
			int departmentID = Integer.parseInt(request.getParameter("departmentID"));
			int laboratoryID = Integer.parseInt(request.getParameter("laboratoryID"));
 			String author = request.getParameter("author");
			Connection conn = DatabaseUtil.getConn();
			String sql = "insert into _regulation values(null, ?, ?, now(), ?, ?, ?)";
			PreparedStatement pstmt = DatabaseUtil.prepareStmt(conn, sql);
			try {
				int i = 1;
				pstmt.setString(i++, title);
				pstmt.setString(i++, content);
				pstmt.setString(i++, author);
				pstmt.setInt(i++, departmentID);
				pstmt.setInt(i++, laboratoryID);
				pstmt.executeUpdate();	
				request.setAttribute("msg", "添加成功");
				LogDAO.add((String)request.getSession().getAttribute("admin_name"), "添加管理制度文章[" + title + "]");
				request.getRequestDispatcher("RegulationServlet?method=list").forward(request, response);
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
		int departmentID = Integer.parseInt(request.getParameter("departmentID"));
		Admin admin = (Admin)request.getSession().getAttribute("admin");
		// 本系教务员才能删除本系实验室管理制度
		if(admin.getRole() == 0 || admin.getDepartmentID() == departmentID){
			Connection conn = DatabaseUtil.getConn();
			String sql = "delete from _regulation where id = ?";
			PreparedStatement pstmt = DatabaseUtil.prepareStmt(conn, sql);
			try {
				pstmt.setInt(1, id);
				pstmt.executeUpdate();	
				request.setAttribute("msg", "删除成功");
				LogDAO.add((String)request.getSession().getAttribute("admin_name"), "删除管理制度[ID " + id + "]");
				request.getRequestDispatcher("RegulationServlet?method=list").forward(request, response);
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
			request.setAttribute("msg", "对不起，您无权限执行该操作!");
			request.getRequestDispatcher("error.jsp").forward(request, response);
			return;
		}
	}

	
	public void update(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException{
		int id = Integer.parseInt(request.getParameter("id"));
		int departmentID = Integer.parseInt(request.getParameter("departmentID"));
		Admin admin = (Admin)request.getSession().getAttribute("admin");
		// 本系教务员才能修改本系实验室管理制度
		if(admin.getRole() == 0 || admin.getDepartmentID() == departmentID){
			String title = request.getParameter("title");
			String content = request.getParameter("content");
			int laboratoryID = Integer.parseInt(request.getParameter("laboratoryID"));
			String author = request.getParameter("author");
			Connection conn = DatabaseUtil.getConn();
			String sql = "update _regulation set title = ?, content = ?, author = ?, laboratoryID = ? where id = ?";
				PreparedStatement pstmt = null;
				try {
					pstmt = DatabaseUtil.prepareStmt(conn, sql);					
					int i = 1;
					pstmt.setString(i++, title);
					pstmt.setString(i++, content);
					pstmt.setString(i++, author);
					pstmt.setInt(i++, laboratoryID);
					pstmt.setInt(i++, id);
					pstmt.executeUpdate();	
					request.setAttribute("id", id);
					request.setAttribute("msg", "修改成功");
					LogDAO.add((String)request.getSession().getAttribute("admin_name"), "修改管理制度内容[" + title + "]");
					request.getRequestDispatcher("editRegulation.jsp").forward(request, response);
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
			request.setAttribute("msg", "对不起，您无权限执行该操作!");
			request.getRequestDispatcher("error.jsp").forward(request, response);
			return;
		}
	}
	
	
	public void search(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException{
		Connection conn = DatabaseUtil.getConn();
		PreparedStatement pstmt = null;
		Admin admin = ((Admin)request.getSession().getAttribute("admin"));
		String keyword = request.getParameter("keyword");
		String sql = "";
		if(admin.getRole() == 0){
			sql = "select * from _regulation where (title like ? or content like ? or author like ?) order by date desc";
		}else{
			sql = "select * from _regulation where departmentID = " + admin.getDepartmentID() + " and (title like ? or content like ? or author like ?) order by date desc";
		}
		ResultSet rs = null;
		try {
			pstmt = DatabaseUtil.prepareStmt(conn, sql);
			int k = 1;
			pstmt.setString(k++, "%" + keyword + "%");
			pstmt.setString(k++, "%" + keyword + "%");
			pstmt.setString(k++, "%" + keyword + "%");
			rs = pstmt.executeQuery();
			Map<Integer, String> laboratoryNameMap = new HashMap<Integer, String>();
			ArrayList<Regulation> regulationList = new ArrayList<Regulation>();
			while(rs.next()) {
				Regulation t = new Regulation();
				RegulationDAO.initFromRS(rs, t);
				regulationList.add(t);
			}	
			//取对应实验室名
			int listLen = regulationList.size();
			sql = "select id, name from _laboratory where id in (?";
			for(int i = 0; i< listLen; i++){
				sql += ", ?";
			}
			sql += ")";
			pstmt = DatabaseUtil.prepareStmt(conn, sql);
			pstmt.setString(1, "0");
			for(int i = 0; i< listLen; i++){
				pstmt.setInt(i+2, regulationList.get(i).getLaboratoryID());
			}
			rs = pstmt.executeQuery();
			while(rs.next()){
				laboratoryNameMap.put(rs.getInt("id"), rs.getString("name"));
			}
			request.setAttribute("search", "search");
			request.setAttribute("laboratoryNameMap", laboratoryNameMap);
			request.setAttribute("regulationList", regulationList);
			request.setAttribute("msg", "搜索结果如下");
			request.getRequestDispatcher("listRegulation.jsp").forward(request, response);
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
		Map<Integer, String> laboratoryNameMap = new HashMap<Integer, String>();
		ArrayList<Regulation> regulationList = new ArrayList<Regulation>();
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
			String countSQL = "select count(*) from _regulation";
			String sql = "select * from _regulation order by date desc";
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
				Regulation t = new Regulation();
				RegulationDAO.initFromRS(rs, t);
				regulationList.add(t);
			}	
			//取对应实验室名
			int listLen = regulationList.size();
			sql = "select id, name from _laboratory where id in (?";
			for(int i = 0; i< listLen; i++){
				sql += ", ?";
			}
			sql += ")";
			pstmt = DatabaseUtil.prepareStmt(conn, sql);
			pstmt.setString(1, "0");
			for(int i = 0; i< listLen; i++){
				pstmt.setInt(i+2, regulationList.get(i).getLaboratoryID());
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
			request.setAttribute("laboratoryNameMap", laboratoryNameMap);
			request.setAttribute("regulationList", regulationList);
			request.getRequestDispatcher("listRegulation.jsp").forward(request, response);
		}
	}
	
	//显示某个实验室的管理制度列表
	public void webList_obl(HttpServletRequest request, HttpServletResponse response)
	throws ServletException, IOException{
		int laboratoryID = Integer.parseInt(request.getParameter("laboratoryID"));
		ArrayList<Regulation> regulationList = new ArrayList<Regulation>();
		Connection conn = DatabaseUtil.getConn();
		PreparedStatement pstmt = null;
		ResultSet rs = null;	
		try {
			String sql = "select * from _regulation where laboratoryID = ? order by date desc";
			pstmt = DatabaseUtil.prepareStmt(conn, sql);
			pstmt.setInt(1, laboratoryID);
			rs = pstmt.executeQuery();
			while(rs.next()) {
				Regulation t = new Regulation();
				RegulationDAO.initFromRS(rs, t);
				regulationList.add(t);
			}	
		}catch (SQLException e) {
			e.printStackTrace();
		}finally{		
			DatabaseUtil.close(pstmt);
			DatabaseUtil.close(rs);
			DatabaseUtil.close(conn);	
			request.setAttribute("laboratoryName", LaboratoryDAO.getName(laboratoryID));
			request.setAttribute("regulationList", regulationList);
			request.getRequestDispatcher("listRegulation_obl.jsp").forward(request, response);
		}
	}
	
	// 显示某个系别的管理制度列表
	public void webList_obd(HttpServletRequest request, HttpServletResponse response)
	throws ServletException, IOException{
		int departmentID = Integer.parseInt(request.getParameter("departmentID"));
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
		Map<Integer, String> laboratoryNameMap = new HashMap<Integer, String>();
		ArrayList<Regulation> regulationList = new ArrayList<Regulation>();
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
			String countSQL = "select count(*) from _regulation where departmentID = " + departmentID;
			String sql = "select * from _regulation where departmentID = ? order by laboratoryID desc, date desc limit ?, ?";
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
			pstmt = DatabaseUtil.prepareStmt(conn, sql);
			pstmt.setInt(1, departmentID);
			pstmt.setInt(2, startPos);
			pstmt.setInt(3, pageSize);
			rs = pstmt.executeQuery();
			while(rs.next()) {
				Regulation t = new Regulation();
				RegulationDAO.initFromRS(rs, t);
				regulationList.add(t);
			}	
			//取对应实验室名
			int listLen = regulationList.size();
			sql = "select id, name from _laboratory where id in (?";
			for(int i = 0; i< listLen; i++){
				sql += ", ?";
			}
			sql += ")";
			pstmt = DatabaseUtil.prepareStmt(conn, sql);
			pstmt.setString(1, "0");
			for(int i = 0; i< listLen; i++){
				pstmt.setInt(i+2, regulationList.get(i).getLaboratoryID());
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
			request.setAttribute("departmentID", departmentID);
			request.setAttribute("totalPages", totalPages);
			request.setAttribute("laboratoryNameMap", laboratoryNameMap);
			request.setAttribute("regulationList", regulationList);
			request.getRequestDispatcher("listRegulation_obd.jsp").forward(request, response);
		}
	}
	
	public void view(HttpServletRequest request, HttpServletResponse response)
	throws ServletException, IOException{
		try{
			int id = Integer.parseInt(request.getParameter("id"));
			Regulation regulation = RegulationDAO.load(id);
			request.setAttribute("regulation", regulation);
			request.getRequestDispatcher("viewRegulation.jsp").forward(request, response);
		}catch(Exception e){
			e.printStackTrace();
			request.setAttribute("msg", "浏览失败!");
			request.getRequestDispatcher("error.jsp").forward(request, response);
			return;
		}
	}	
	
}
