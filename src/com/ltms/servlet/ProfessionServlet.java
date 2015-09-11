package com.ltms.servlet;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jxl.Sheet;
import jxl.Workbook;

import com.jspsmart.upload.Request;
import com.jspsmart.upload.SmartUpload;
import com.ltms.dao.DepartmentDAO;
import com.ltms.model.Department;
import com.ltms.util.DatabaseUtil;

public class ProfessionServlet extends HttpServlet {
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
				mySmartUpload.setAllowedFilesList("xls,XLS");
			//上载文件 
				mySmartUpload.upload();  //这句话执行后才能取得参数
			} catch (Exception e){
				request.setAttribute("msg", "只能上传03版excel文件(后缀名为.xls)!");
				request.getRequestDispatcher("error.jsp").forward(request, response);
			  	return;
			}
			Request req = mySmartUpload.getRequest();
			method = req.getParameter("method");
			if(method.equals("import_zyl")) import_zyl(request, response, req, mySmartUpload,1);
			if(method.equals("import_zy")) import_zyl(request, response, req, mySmartUpload,2);
			if(method.equals("import_whcd")) import_whcd(request, response, req, mySmartUpload,1);
			if(method.equals("import_zyzc")) import_whcd(request, response, req, mySmartUpload,2);
			else return;
		}else if(!"".equals(method)){
//			if(method.equals("list")) list(request, response);
//			else if(method.equals("add")) add(request, response);
//			else if(method.equals("getAjax")) getAjax(request, response);
//			else return;
		}
	}
	public void import_zyl(HttpServletRequest request, HttpServletResponse response, Request req, SmartUpload mySmartUpload,int num_sign)
	throws ServletException, IOException{
		String ext = "";
		String url = "upload/import_excel/";      //应保证在根目录中有此目录的存在  上传图片路径
		String saveurl= "";
		com.jspsmart.upload.File file1 = mySmartUpload.getFiles().getFile(0);
		if(!file1.isMissing()){ 
			try{
				ext= file1.getFileExt();      //取得后缀名
			    //更改文件名，取得当前上传时间的毫秒数值
			    Calendar calendar = Calendar.getInstance();
			    String filename = String.valueOf(calendar.getTimeInMillis()); 
			    saveurl = request.getSession().getServletContext().getRealPath("/") + url;
			    saveurl += filename + "." + ext;          //文件绝对路径
			    file1.saveAs(saveurl,mySmartUpload.SAVE_PHYSICAL);
			}catch (Exception e){
				e.printStackTrace();
				request.setAttribute("msg", "上传excel文件失败!");
				request.getRequestDispatcher("error.jsp").forward(request, response);
			  	return;
			}
		}
		Connection conn = DatabaseUtil.getConn();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{	InputStream is = new FileInputStream(saveurl);
		    Workbook rwb = Workbook.getWorkbook(is);
		    //这里有两种方法获取sheet表,1为名字Sheet st = rwb.getSheet("original")，二为下标，从0开始Sheet st = rwb.getSheet("0")
		    Sheet st = rwb.getSheet(0);
		    //通用的获取cell值的方式,返回字符串
		    //int columnum = st.getColumns(); //  得到列数 
		    int rownum = st.getRows(); //  得到行数 
		    
		    String sql = null;
		    if(num_sign == 1){
		    	sql = "delete from zyl";
		    }else if(num_sign == 2){
		    	sql = "delete from zy";
		    }
			pstmt = DatabaseUtil.prepareStmt(conn, sql);
			pstmt.executeUpdate();
			conn.setAutoCommit(false);  //事务开始
			for(int i=1; i<rownum; i++){  //这里写固定了    
				String dm = st.getCell(0, i).getContents();   
				String mc = st.getCell(1, i).getContents();	
				String sign = st.getCell(2, i).getContents();   
				
			    if(num_sign == 1){
			    	sql = "insert into zyl values(?, ?, ?)";
			    }else if(num_sign == 2){
			    	sql = "insert into zy values(?, ?, ?)";
			    }
			    
				pstmt = DatabaseUtil.prepareStmt(conn, sql);
				int pi = 1;
				pstmt.setString(pi++, dm);
				pstmt.setString(pi++, mc);
				pstmt.setString(pi++, sign);
				pstmt.executeUpdate();
				if(dm.trim().length()<1 || dm.trim().length()<1 ){
					break;
				}
			}
			conn.commit();   //事务提交
			conn.setAutoCommit(true);  //设置成自动提交模式
		    //关闭
		    rwb.close();
			request.setAttribute("msg", "导入数据成功");
			request.getRequestDispatcher("importData.jsp").forward(request, response);
		}catch (jxl.read.biff.BiffException e) {
			request.setAttribute("msg", "无法识别该excel文件");
			request.getRequestDispatcher("error.jsp").forward(request, response);
			e.printStackTrace();
		}catch (Exception e) {
			request.setAttribute("msg", "导入数据失败");
			request.getRequestDispatcher("error.jsp").forward(request, response);
			e.printStackTrace();
		}finally{		
			DatabaseUtil.close(pstmt);
			DatabaseUtil.close(conn);	
		}
	}

	public void import_whcd(HttpServletRequest request, HttpServletResponse response, Request req, SmartUpload mySmartUpload,int num_sign)
	throws ServletException, IOException{
		String ext = "";
		String url = "upload/import_excel/";      //应保证在根目录中有此目录的存在  上传图片路径
		String saveurl= "";
		com.jspsmart.upload.File file1 = mySmartUpload.getFiles().getFile(0);
		if(!file1.isMissing()){ 
			try{
				ext= file1.getFileExt();      //取得后缀名
			    //更改文件名，取得当前上传时间的毫秒数值
			    Calendar calendar = Calendar.getInstance();
			    String filename = String.valueOf(calendar.getTimeInMillis()); 
			    saveurl = request.getSession().getServletContext().getRealPath("/") + url;
			    saveurl += filename + "." + ext;          //文件绝对路径
			    file1.saveAs(saveurl,mySmartUpload.SAVE_PHYSICAL);
			}catch (Exception e){
				e.printStackTrace();
				request.setAttribute("msg", "上传excel文件失败!");
				request.getRequestDispatcher("error.jsp").forward(request, response);
			  	return;
			}
		}
		Connection conn = DatabaseUtil.getConn();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{	InputStream is = new FileInputStream(saveurl);
		    Workbook rwb = Workbook.getWorkbook(is);
		    //这里有两种方法获取sheet表,1为名字Sheet st = rwb.getSheet("original")，二为下标，从0开始Sheet st = rwb.getSheet("0")
		    Sheet st = rwb.getSheet(0);
		    //通用的获取cell值的方式,返回字符串
		    //int columnum = st.getColumns(); //  得到列数 
		    int rownum = st.getRows(); //  得到行数 
		    
		    String sql = null;
		    if(num_sign == 1){
		    	sql = "delete from whcd";
		    }else if(num_sign == 2){
		    	sql = "delete from zyzc";
		    }
			pstmt = DatabaseUtil.prepareStmt(conn, sql);
			pstmt.executeUpdate();
			conn.setAutoCommit(false);  //事务开始
			for(int i=1; i<rownum; i++){  //这里写固定了    
				String bh = st.getCell(0, i).getContents();   
				String value = st.getCell(1, i).getContents();	
				
			    if(num_sign == 1){
			    	sql = "insert into whcd values(?, ?)";
			    }else if(num_sign == 2){
			    	sql = "insert into zyzc values(?, ?)";
			    }
			    
				pstmt = DatabaseUtil.prepareStmt(conn, sql);
				int pi = 1;
				pstmt.setString(pi++, bh);
				pstmt.setString(pi++, value);
				pstmt.executeUpdate();
				if(bh.trim().length()<1 || value.trim().length()<1 ){
					break;
				}
			}
			conn.commit();   //事务提交
			conn.setAutoCommit(true);  //设置成自动提交模式
		    //关闭
		    rwb.close();
			request.setAttribute("msg", "导入数据成功");
			request.getRequestDispatcher("importData.jsp").forward(request, response);
		}catch (jxl.read.biff.BiffException e) {
			request.setAttribute("msg", "无法识别该excel文件");
			request.getRequestDispatcher("error.jsp").forward(request, response);
			e.printStackTrace();
		}catch (Exception e) {
			request.setAttribute("msg", "导入数据失败");
			request.getRequestDispatcher("error.jsp").forward(request, response);
			e.printStackTrace();
		}finally{		
			DatabaseUtil.close(pstmt);
			DatabaseUtil.close(conn);	
		}
	}
}
