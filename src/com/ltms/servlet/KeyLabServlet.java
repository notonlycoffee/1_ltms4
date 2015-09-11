package com.ltms.servlet;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;

import com.jspsmart.upload.Request;
import com.jspsmart.upload.SmartUpload;
import com.ltms.dao.KeyLabDAO;
import com.ltms.dao.LogDAO;
import com.ltms.model.KeyLab;
import com.ltms.util.DatabaseUtil;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class KeyLabServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		this.doPost(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String method = request.getParameter("method");
		if(method == null){
			SmartUpload mySmartUpload = new SmartUpload();
			mySmartUpload.initialize(getServletConfig(),request,response);
			try {
				mySmartUpload.setAllowedFilesList("jpg,jpeg,gif,png,bmp,BMP,JPG,JPEG,GIF,PNG");
			//上载文件 
				mySmartUpload.upload();  //这句话执行后才能取得参数
			} catch (Exception e){
				request.setAttribute("msg", "只允许上传jpg、gif、png以及bmp类型的图片文件!");
				request.getRequestDispatcher("error.jsp").forward(request, response);
			  	return;
			}
			Request req = mySmartUpload.getRequest();
			method = req.getParameter("method");
			if(method.equals("update")) update(request, response, req, mySmartUpload);
			else if(method.equals("add")) add(request, response, req, mySmartUpload);
			else return;
		}else if(!"".equals(method)){
			if(method.equals("delete")) delete(request, response);
			else if(method.equals("webList")) webList(request, response);
			else return;
		}
	}

	public void add(HttpServletRequest request, HttpServletResponse response, Request req, SmartUpload mySmartUpload)
			throws ServletException, IOException{
			String name = req.getParameter("name");
			String gotoURL = req.getParameter("gotoURL");
			String pic = "";
			int sort = 10;
			try{
				sort = Integer.parseInt(req.getParameter("sort"));
			}catch(NumberFormatException e){
				sort = 10;
			}
			String ext = "";
			String url = "upload/keylab_pic/";      //应保证在根目录中有此目录的存在  上传图片路径
			//初始化mySmartUpload
			com.jspsmart.upload.File file1 = mySmartUpload.getFiles().getFile(0);
			//选中删除图片,则删除图片;未选中则判断有无上传图片,有上传图片则处理上传图片
			if(!file1.isMissing()){ 
				try{
					ext= file1.getFileExt();      //取得后缀名
					int file_size = file1.getSize();     //取得文件的大小  
					String saveurl= "";
				    //更改文件名，取得当前上传时间的毫秒数值
				    Calendar calendar = Calendar.getInstance();
				    String filename = String.valueOf(calendar.getTimeInMillis()); 
				    saveurl = request.getSession().getServletContext().getRealPath("/") + url;
				    saveurl += filename + "." + ext;          //文件绝对路径
				    file1.saveAs(saveurl,mySmartUpload.SAVE_PHYSICAL);
				    pic = url + filename + "." + ext;
				}catch (Exception e){
					e.printStackTrace();
					request.setAttribute("msg", "上传图片失败!");
					request.getRequestDispatcher("error.jsp").forward(request, response);
				  	return;
				}
			}
			Connection conn = DatabaseUtil.getConn();
			String sql = "insert into _keyLab values(null, ?, ?, ?, ?)";
			PreparedStatement pstmt = DatabaseUtil.prepareStmt(conn, sql);
			try {
				int i = 1;
				pstmt.setString(i++, name);
				pstmt.setString(i++, gotoURL);
				pstmt.setInt(i++, sort);	
				pstmt.setString(i++, pic);
				pstmt.executeUpdate();	
				request.getSession().removeAttribute("keyLabList");
				request.setAttribute("msg", "添加成功");
				LogDAO.add((String)request.getSession().getAttribute("admin_name"), "添加省级示范中心链接信息[名称 " + name + "]");
				request.getRequestDispatcher("keyLab.jsp").forward(request, response);
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
		String id = request.getParameter("id");
		Connection conn = DatabaseUtil.getConn();
		String sql = "delete from _keyLab where id = ?";
		PreparedStatement pstmt = DatabaseUtil.prepareStmt(conn, sql);
		try {
			pstmt.setString(1, id);
			pstmt.executeUpdate();	
			request.getSession().removeAttribute("keyLabList");
			request.setAttribute("msg", "删除成功");
			LogDAO.add((String)request.getSession().getAttribute("admin_name"), "删除省级示范中心链接信息[ID " + id + "]");
			request.getRequestDispatcher("keyLab.jsp").forward(request, response);
		} catch (SQLException e) {
			e.printStackTrace();
			request.setAttribute("msg", "删除失败!");
			request.getRequestDispatcher("error.jsp").forward(request, response);
			return;
		}finally{
			DatabaseUtil.close(pstmt);
			DatabaseUtil.close(conn);
		}
	}

	public void update(HttpServletRequest request, HttpServletResponse response, Request req, SmartUpload mySmartUpload)
			throws ServletException, IOException{
		String id = req.getParameter("id");
		String name = req.getParameter("name");
		String gotoURL = req.getParameter("gotoURL");
		String pic = req.getParameter("old_pic");
		int sort = 10;
		try{
			sort = Integer.parseInt(req.getParameter("sort"));
		}catch(NumberFormatException e){
			sort = 10;
		}
		String ext = "";
		String url = "upload/keylab_pic/";      //应保证在根目录中有此目录的存在  上传图片路径
		//初始化mySmartUpload
		com.jspsmart.upload.File file1 = mySmartUpload.getFiles().getFile(0);
		//选中删除图片,则删除图片;未选中则判断有无上传图片,有上传图片则处理上传图片
		if(req.getParameter("is_del") != null){
			String saveurl= request.getSession().getServletContext().getRealPath("/") + pic;
			java.io.File fileDel = new java.io.File(saveurl);
			if(fileDel.exists()){
				fileDel.delete();
		    }
			pic = "";
		}else{
			if(!file1.isMissing()){ 
				try{
					ext= file1.getFileExt();      //取得后缀名
					int file_size = file1.getSize();     //取得文件的大小  
					String saveurl= "";
				    //更改文件名，取得当前上传时间的毫秒数值
				    Calendar calendar = Calendar.getInstance();
				    String filename = String.valueOf(calendar.getTimeInMillis()); 
				    saveurl = request.getSession().getServletContext().getRealPath("/") + url;
				    saveurl += filename + "." + ext;          //文件绝对路径
				    file1.saveAs(saveurl,mySmartUpload.SAVE_PHYSICAL);
				    pic = url + filename + "." + ext;
				}catch (Exception e){
					e.printStackTrace();
					request.setAttribute("msg", "上传图片失败!");
					request.getRequestDispatcher("error.jsp").forward(request, response);
				  	return;
				}
			}
		}
		Connection conn = DatabaseUtil.getConn();
		String sql = "update _keyLab set name = ?, gotoURL = ?, sort = ?, pic = ? where id = ?";
			PreparedStatement pstmt = null;
			try {
				pstmt = DatabaseUtil.prepareStmt(conn, sql);					
				int i = 1;
				pstmt.setString(i++, name);
				pstmt.setString(i++, gotoURL);
				pstmt.setInt(i++, sort);
				pstmt.setString(i++, pic);
				pstmt.setString(i++, id);
				pstmt.executeUpdate();
				request.setAttribute("msg", "修改成功");
				request.getSession().removeAttribute("keyLabList");
				LogDAO.add((String)request.getSession().getAttribute("admin_name"), "修改省级示范中心链接信息[名称 " + name + "]");
				request.getRequestDispatcher("keyLab.jsp").forward(request, response);
			}catch (SQLException e) {
				e.printStackTrace();
				request.setAttribute("msg", "修改失败!");
				request.getRequestDispatcher("error.jsp").forward(request, response);
				return;
			}finally{
				DatabaseUtil.close(pstmt);
				DatabaseUtil.close(conn);
			}	
	}
	
	// 显示省级示范中心网站列表
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
		ArrayList<KeyLab> keyLabList = new ArrayList<KeyLab>();
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
			String countSQL = "select count(*) from _keyLab";
			String sql = "select * from _keyLab order by sort";
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
				KeyLab kl = new KeyLab();
				KeyLabDAO.initFromRS(rs, kl);
				keyLabList.add(kl);
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
			request.setAttribute("keyLabList", keyLabList);
			request.getRequestDispatcher("listKeyLab.jsp").forward(request, response);
		}
	}
}
