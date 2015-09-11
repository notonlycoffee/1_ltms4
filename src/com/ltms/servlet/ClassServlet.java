package com.ltms.servlet;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jspsmart.upload.Request;
import com.jspsmart.upload.SmartUpload;
import com.ltms.dao.ClassDAO;
import com.ltms.dao.DepartmentDAO;
import com.ltms.dao.LogDAO;
import com.ltms.dao.SystemConfigDAO;
import com.ltms.model.Admin;
import com.ltms.model.Department;
import com.ltms.model.SystemConfig;
import com.ltms.model._Class;
import com.ltms.util.DatabaseUtil;
import com.ltms.util.Encrypt;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jxl.Sheet;
import jxl.Workbook;

public class ClassServlet extends HttpServlet {
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
			if(method.equals("import_from_excel")) import_from_excel(request, response, req, mySmartUpload);
			else return;
		}else if(!"".equals(method)){
			if(method.equals("list")) list(request, response);
			else if(method.equals("add")) add(request, response);
			else if(method.equals("getAjax")) getAjax(request, response);
			else return;
		}
	}

	public void list(HttpServletRequest request, HttpServletResponse response)
	throws ServletException, IOException{
		int pageNo = 1;
		String strPageNo = request.getParameter("pageNo");
		int role = ((Admin)request.getSession().getAttribute("admin")).getRole();
		if(strPageNo != null && !strPageNo.trim().equals("")) {
			try {
				pageNo = Integer.parseInt(strPageNo);
			} catch (NumberFormatException e) {
				pageNo = 1;
			} 
		}
		if(pageNo <= 0) pageNo = 1;
		ArrayList<_Class> classList = new ArrayList<_Class>();
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
			
			String loginName = (String)request.getSession().getAttribute("admin_name");
			
			System.out.println("login name is " + loginName);
			System.out.println("role is " + role);
			
			int maxGrade ;
			
			Calendar c = Calendar.getInstance();
			int year = c.get(Calendar.YEAR);
			int month = c.get(Calendar.MONTH);  //从0开始
			if(month > 7){
				maxGrade = year - 3;
			}else{
				maxGrade = year - 3;
			}
			String countSQL = "select count(*) from _class where grade >= " + maxGrade;
			String sql = "select * from _class where grade >= " + maxGrade;
			int departmentID = 0;
			if(role != 0){
				departmentID = ((Admin)request.getSession().getAttribute("admin")).getDepartmentID();
				sql += " and departmentID = " + departmentID;
				countSQL += " and departmentID = " + departmentID;
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
			sql +=" order by departmentID, id limit ?, ?";
			pstmt = DatabaseUtil.prepareStmt(conn, sql);
			pstmt.setInt(1, startPos);
			pstmt.setInt(2, pageSize);
			rs = pstmt.executeQuery();
			
			rs.last();
			int count = rs.getRow();
			while(rs.next()) {
				_Class g = new _Class();
				ClassDAO.initFromRS(rs, g);
				classList.add(g);
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
			request.setAttribute("classList", classList);
			request.getRequestDispatcher("class.jsp").forward(request, response);
		}
	}
	
	public void add(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException{
		String returnPage = request.getParameter("returnPage");
		String id = request.getParameter("id");
		String name = request.getParameter("name");
		String studentNumber = request.getParameter("studentNumber");
		String departmentID = request.getParameter("departmentID");
		String grade = request.getParameter("grade");
		Connection conn = DatabaseUtil.getConn();
		String sql = "insert into _class values(?, ?, ?, ?, ?)";
		PreparedStatement pstmt = DatabaseUtil.prepareStmt(conn, sql);
		try {
			int i = 1;
			pstmt.setString(i++, id);
			pstmt.setString(i++, name);
			pstmt.setString(i++, studentNumber);
			pstmt.setString(i++, grade);
			pstmt.setString(i++, departmentID);
			pstmt.executeUpdate();	
			request.getSession().removeAttribute("classList");
			request.setAttribute("msg", "添加成功");
			LogDAO.add((String)request.getSession().getAttribute("admin_name"), "添加班级信息[ID " + id + "专业名 " + name + "]");
			request.getRequestDispatcher(returnPage + ".jsp").forward(request, response);
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
		String sql = "delete from _class where id = ?";
		PreparedStatement pstmt = DatabaseUtil.prepareStmt(conn, sql);
		try {
			pstmt.setString(1, id);
			pstmt.executeUpdate();	
			request.getSession().removeAttribute("classList");
			request.setAttribute("msg", "删除成功");
			LogDAO.add((String)request.getSession().getAttribute("admin_name"), "删除班级信息[ID " + id + "]");
			request.getRequestDispatcher("class.jsp").forward(request, response);
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

	public void modify(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException{
		String id = request.getParameter("id");
		String name = request.getParameter("name");
		String studentNumber = request.getParameter("studentNumber");
		String grade = request.getParameter("grade");
		String departmentID = request.getParameter("departmentID");
		Connection conn = DatabaseUtil.getConn();
		String sql = "update _class set name = ?, studentNumber = ?, departmentID = ?, grade = ? where id = ?";
			PreparedStatement pstmt = null;
			try {
				pstmt = DatabaseUtil.prepareStmt(conn, sql);	
				int i = 1;
				pstmt.setString(i++, name);
				pstmt.setString(i++, studentNumber);
				pstmt.setString(i++, departmentID);
				pstmt.setString(i++, grade);
				pstmt.setString(i++, id);
				pstmt.executeUpdate();
				request.setAttribute("msg", "修改成功");
				LogDAO.add((String)request.getSession().getAttribute("admin_name"), "修改班级信息[ID " + id + "专业名 " + name + "]");
				request.getRequestDispatcher("class.jsp").forward(request, response);
			}catch (SQLException e) {
				e.printStackTrace();
				request.setAttribute("msg", "修改班级信息失败!");
				request.getRequestDispatcher("error.jsp").forward(request, response);
				return;
			}finally{
				DatabaseUtil.close(pstmt);
				DatabaseUtil.close(conn);
			}	
	}
	
	public void search(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException{
		Connection conn = DatabaseUtil.getConn();
		PreparedStatement pstmt = null;
		String id = request.getParameter("id");
		String sql = "select * from _class where id like ?";
		int departmentID = ((Admin)request.getSession().getAttribute("admin")).getDepartmentID();  //管理员的话此值为0
//System.out.println(departmentID);
				if(departmentID != 0){
					sql += " and departmentID = " + departmentID;
				}
		ResultSet rs = null;
		try {
			pstmt = DatabaseUtil.prepareStmt(conn, sql);
			pstmt.setString(1, "%" + id + "%");
			rs = pstmt.executeQuery();
			ArrayList<_Class> classList = new ArrayList<_Class>();
			while(rs.next()) {
				_Class g = new _Class();
				ClassDAO.initFromRS(rs, g);
				classList.add(g);
			}	
			request.setAttribute("classList", classList);
			request.setAttribute("search", "search");
			request.setAttribute("msg", "搜索结果如下");
			request.getRequestDispatcher("class.jsp").forward(request, response);
		}catch (SQLException e) {
			e.printStackTrace();
		}finally{		
			DatabaseUtil.close(rs);
			DatabaseUtil.close(pstmt);
			DatabaseUtil.close(conn);	
		}
	}
	
	public void getAjax(HttpServletRequest request, HttpServletResponse response)
	throws ServletException, IOException{
		int departmentID = Integer.parseInt(request.getParameter("departmentID"));
		ArrayList<_Class> classList = ClassDAO.list(departmentID);
		response.setContentType("text/html;charset=utf-8");
		response.setCharacterEncoding("utf-8");
		PrintWriter out = response.getWriter(); 
		if(classList.size() == 0){
			out.println("<div class=\"no_result_msg\">暂无班级信息</div>");
			out.flush();
			out.close(); 
			return;
		}
		String de_name = DepartmentDAO.getName(departmentID);
		String istring = "<table cellspacing=0 cellpadding=0>";
		istring += "<tr><th>班级</th><th>专业名称</th><th>系别</th><th>人数</th><th>年级</th></tr>";
		for(_Class clazz : classList){
			istring += "<tr class='exTr'>";
			istring += "<td class='showTD' style='width:100px;'>" + clazz.getId() + "</td>";
			istring += "<td class='showTD' style='width:210px;text-align:left;'>" + clazz.getName() + "</td>";
			istring += " <td class='showTD' style='width:140px;text-align:center;'>" + de_name + "</td>";
			istring += "<td class='showTD' style='width:60px;'>" + clazz.getStudentNumber()+ "</td>";
			istring += "<td class='showTD' style='width:60px;'>" + clazz.getGrade() + "</td>";
			istring += "</tr>";
		}	
		istring += "</table>";
		out.println(istring);
		out.flush();
		out.close(); 
	}	
	
//	public void import_from_excel(HttpServletRequest request, HttpServletResponse response, Request req, SmartUpload mySmartUpload)
//	throws ServletException, IOException{
//		String ext = "";
//		String url = "upload/import_excel/";      //应保证在根目录中有此目录的存在  上传图片路径
//		String saveurl= "";
//		com.jspsmart.upload.File file1 = mySmartUpload.getFiles().getFile(0);
//		if(!file1.isMissing()){ 
//			try{
//				ext= file1.getFileExt();      //取得后缀名
//			    //更改文件名，取得当前上传时间的毫秒数值
//			    Calendar calendar = Calendar.getInstance();
//			    String filename = String.valueOf(calendar.getTimeInMillis()); 
//			    saveurl = request.getSession().getServletContext().getRealPath("/") + url;
//			    saveurl += filename + "." + ext;          //文件绝对路径
//			    file1.saveAs(saveurl,mySmartUpload.SAVE_PHYSICAL);
//			}catch (Exception e){
//				e.printStackTrace();
//				request.setAttribute("msg", "上传excel文件失败!");
//				request.getRequestDispatcher("error.jsp").forward(request, response);
//			  	return;
//			}
//		}
//		Connection conn = DatabaseUtil.getConn();
//		PreparedStatement pstmt = null;
//		ResultSet rs = null;
//		try
//		{	InputStream is = new FileInputStream(saveurl);
//		    Workbook rwb = Workbook.getWorkbook(is);
//		    //这里有两种方法获取sheet表,1为名字Sheet st = rwb.getSheet("original")，二为下标，从0开始Sheet st = rwb.getSheet("0")
//		    Sheet st = rwb.getSheet(0);
//		    //通用的获取cell值的方式,返回字符串
//		    //int columnum = st.getColumns(); //  得到列数 
//		    int rownum = st.getRows(); //  得到行数 
//		    
//		    Map<String, Integer> departmentMap = new HashMap<String, Integer>();
//		    List<Department> departmentList = DepartmentDAO.list();	
//		    for(Department dd : departmentList){
//		    	 departmentMap.put(dd.getName(), dd.getId());
//		    }
//			ArrayList<String> class_id_list = new ArrayList<String>();
//			String sql = "select id from _class";
//		    pstmt = DatabaseUtil.prepareStmt(conn, sql);
//		    rs = pstmt.executeQuery();
//			while(rs.next()){
//				class_id_list.add(rs.getString("id"));
//			}
//			conn.setAutoCommit(false);  //事务开始
//			for(int i=1; i<rownum; i++){
//				String id = st.getCell(3, i).getContents();   
//				String name = st.getCell(1, i).getContents()+st.getCell(2, i).getContents();				
//				int studentNumber = Integer.parseInt(st.getCell(7, i).getContents());
//				int grade = Integer.parseInt(st.getCell(5, i).getContents());
//				int departmentID = departmentMap.get(st.getCell(0, i).getContents());  
//				if(class_id_list.contains(id)){
//					sql = "update _class set name = ?, studentNumber = ?, grade = ?, departmentID = ? where id = ?";
//					pstmt = DatabaseUtil.prepareStmt(conn, sql);
//					int pi = 1;
//					pstmt.setString(pi++, name);
//					pstmt.setInt(pi++, studentNumber);
//					pstmt.setInt(pi++, grade);
//					pstmt.setInt(pi++, departmentID);
//					pstmt.setString(pi++, id);
//					pstmt.executeUpdate();
//				}else{
//					sql = "insert into _class values(?, ?, ?, ?, ?)";
//					pstmt = DatabaseUtil.prepareStmt(conn, sql);
//					int pi = 1;
//					pstmt.setString(pi++, id);
//					pstmt.setString(pi++, name);
//					pstmt.setInt(pi++, studentNumber);
//					pstmt.setInt(pi++, grade);
//					pstmt.setInt(pi++, departmentID);
//					pstmt.executeUpdate();
//				}
//			}
//			conn.commit();   //事务提交
//			conn.setAutoCommit(true);  //设置成自动提交模式
//		    //关闭
//		    rwb.close();
//			request.setAttribute("msg", "导入数据成功");
//			request.getRequestDispatcher("importData.jsp").forward(request, response);
//		}catch (jxl.read.biff.BiffException e) {
//			request.setAttribute("msg", "无法识别该excel文件");
//			request.getRequestDispatcher("error.jsp").forward(request, response);
//			e.printStackTrace();
//		}catch (Exception e) {
//			request.setAttribute("msg", "导入数据失败");
//			request.getRequestDispatcher("error.jsp").forward(request, response);
//			e.printStackTrace();
//		}finally{		
//			DatabaseUtil.close(pstmt);
//			DatabaseUtil.close(conn);	
//		}
//	}
	
	public void import_from_excel(HttpServletRequest request, HttpServletResponse response, Request req, SmartUpload mySmartUpload)
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
		    
		    Map<String, Integer> departmentMap = new HashMap<String, Integer>();
		    List<Department> departmentList = DepartmentDAO.list();	
		    for(Department dd : departmentList){
		    	//系的名字和id
		    	 departmentMap.put(dd.getName(), dd.getId());
		    }
		    
		    //所有班级id列表
			ArrayList<String> class_id_list = new ArrayList<String>();
			String sql = "select id from _class";
		    pstmt = DatabaseUtil.prepareStmt(conn, sql);
		    rs = pstmt.executeQuery();
			while(rs.next()){
				class_id_list.add(rs.getString("id"));
			}
			conn.setAutoCommit(false);  //事务开始
			for(int i=1; i<rownum; i++){
				String id = st.getCell(0, i).getContents();   
				String name = st.getCell(1, i).getContents();				
				int studentNumber = Integer.parseInt(st.getCell(3, i).getContents());
				int grade = Integer.parseInt(st.getCell(4, i).getContents());
				int departmentID = departmentMap.get(st.getCell(2, i).getContents());  
				if(class_id_list.contains(id)){
					sql = "update _class set name = ?, studentNumber = ?, grade = ?, departmentID = ? where id = ?";
					pstmt = DatabaseUtil.prepareStmt(conn, sql);
					int pi = 1;
					pstmt.setString(pi++, name);
					pstmt.setInt(pi++, studentNumber);
					pstmt.setInt(pi++, grade);
					pstmt.setInt(pi++, departmentID);
					pstmt.setString(pi++, id);
					pstmt.executeUpdate();
				}else{
					sql = "insert into _class values(?, ?, ?, ?, ?)";
					pstmt = DatabaseUtil.prepareStmt(conn, sql);
					int pi = 1;
					pstmt.setString(pi++, id);
					pstmt.setString(pi++, name);
					pstmt.setInt(pi++, studentNumber);
					pstmt.setInt(pi++, grade);
					pstmt.setInt(pi++, departmentID);
					pstmt.executeUpdate();
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
