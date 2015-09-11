package com.ltms.servlet;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;

import com.jspsmart.upload.Request;
import com.jspsmart.upload.SmartUpload;
import com.ltms.dao.LogDAO;
import com.ltms.util.DatabaseUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class IndexBannerServlet extends HttpServlet {
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
			else return;
		}
	}
	
	public void add(HttpServletRequest request, HttpServletResponse response, Request req, SmartUpload mySmartUpload)
	throws ServletException, IOException{
		String to_url = "".equals(req.getParameter("url")) ? "javascript:void(0)" : req.getParameter("url");
		String pic = "";
		int sort = 10;
		try{
			sort = Integer.parseInt(req.getParameter("sort"));
		}catch(NumberFormatException e){
			sort = 10;
		}
		String ext = "";
		String url = "upload/index_banner/";      //应保证在根目录中有此目录的存在  上传图片路径
		//初始化mySmartUpload
		com.jspsmart.upload.File file1 = mySmartUpload.getFiles().getFile(0);
		//选中删除图片,则删除图片;未选中则判断有无上传图片,有上传图片则处理上传图片
		if(!file1.isMissing()){ 
			try{
				ext= file1.getFileExt();      //取得后缀名
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
		String sql = "insert into _indexBanner values(null, ?, ?, ?)";
		PreparedStatement pstmt = DatabaseUtil.prepareStmt(conn, sql);
		try {
			int i = 1;
			pstmt.setString(i++, pic);
			pstmt.setString(i++, to_url);
			pstmt.setInt(i++, sort);	
			pstmt.executeUpdate();	
			request.setAttribute("msg", "添加成功");
			request.getRequestDispatcher("indexBanner.jsp").forward(request, response);
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
		String sql = "delete from _indexBanner where id = ?";
		
		//获取banner的路径
		String sql_ = "select * from _indexBanner where id = ?";
		
		PreparedStatement pstmt = DatabaseUtil.prepareStmt(conn, sql_);
		try {
			
			pstmt = DatabaseUtil.prepareStmt(conn, sql_);
			pstmt.setString(1, id);
			ResultSet rs = pstmt.executeQuery();
			String bannerString = "";
			if(rs.next()){
				bannerString = rs.getString("banner");
			}
			System.out.println("banner is " + bannerString);
			
			String path = request.getSession().getServletContext().getRealPath("/") + bannerString;
			System.out.println("path is " + path);
			
			File file = new File(path);
			if(file.exists()){
				file.delete();
			}
			
			pstmt = DatabaseUtil.prepareStmt(conn, sql);
			pstmt.setString(1, id);
			pstmt.executeUpdate();	
			request.setAttribute("msg", "删除成功");
			request.getRequestDispatcher("indexBanner.jsp").forward(request, response);
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
		String to_url = "".equals(req.getParameter("url")) ? "javascript:void(0)" : req.getParameter("url");
		String pic = req.getParameter("old_pic");
		int sort = 10;
		try{
			sort = Integer.parseInt(req.getParameter("sort"));
		}catch(NumberFormatException e){
			sort = 10;
		}
		String ext = "";
		String url = "upload/index_banner/";      //应保证在根目录中有此目录的存在  上传图片路径
		//初始化mySmartUpload
		com.jspsmart.upload.File file1 = mySmartUpload.getFiles().getFile(0);
		if(!file1.isMissing()){ 
			try{
				ext= file1.getFileExt();      //取得后缀名
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
		String sql = "update _indexBanner set url = ?, sort = ?, banner = ? where id = ?";
		PreparedStatement pstmt = null;
		try {
			pstmt = DatabaseUtil.prepareStmt(conn, sql);					
			int i = 1;
			pstmt.setString(i++, to_url);
			pstmt.setInt(i++, sort);
			pstmt.setString(i++, pic);
			pstmt.setString(i++, id);
			pstmt.executeUpdate();
			request.setAttribute("msg", "修改成功");
			request.getRequestDispatcher("indexBanner.jsp").forward(request, response);
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
}
