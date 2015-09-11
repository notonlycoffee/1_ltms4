package com.ltms.servlet;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;

import com.jspsmart.upload.Request;
import com.jspsmart.upload.SmartUpload;
import com.ltms.dao.CourseDAO;
import com.ltms.dao.LogDAO;
import com.ltms.model.Admin;
import com.ltms.model.Course;
import com.ltms.model.Course_sy;
import com.ltms.util.DatabaseUtil;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import jxl.*;

public class CourseServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		this.doPost(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String method = request.getParameter("method");
		if (method == null) {
			SmartUpload mySmartUpload = new SmartUpload();
			mySmartUpload.initialize(getServletConfig(), request, response);
			try {
				mySmartUpload.setAllowedFilesList("xls,XLS");
				// 上载文件
				mySmartUpload.upload(); // 这句话执行后才能取得参数
			} catch (Exception e) {
				request.setAttribute("msg", "只能上传03版excel文件(后缀名为.xls)!");
				request.getRequestDispatcher("error.jsp").forward(request,
						response);
				return;
			}
			Request req = mySmartUpload.getRequest();
			method = req.getParameter("method");
			// if(method.equals("import_from_excel")) import_from_excel(request,
			// response, req, mySmartUpload);
			// else return;
		} else if (!"".equals(method)) {
			if (method.equals("list"))
				list(request, response);
			else if (method.equals("add"))
				add(request, response);
			else if (method.equals("update"))
				update(request, response);
			else if (method.equals("search"))
				search(request, response);
			else if (method.equals("search_syke"))
				search_syke(request, response);
			else if (method.equals("delete"))
				delete(request, response);
			else if (method.equals("list_syke"))
				list_syke(request, response);
			else if (method.equals("update_xgzy"))
				update_xgzy(request, response);
			else
				return;
		}
	}

	private void update_xgzy(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//获取数据
		String sykc_xgzy = request.getParameter("sykc_xgzy");
		String id = request.getParameter("id");
		String course_name = request.getParameter("course_name");
		System.out.println("xgzy is " + sykc_xgzy);
		System.out.println("id is " + id);
		System.out.println("name is " + course_name);
		//判断专业是否为空
		if("".equals(sykc_xgzy) || sykc_xgzy.trim().length() == 0){
			request.setAttribute("msg", "专业不能为空");
			request.getRequestDispatcher("editsykcgl.jsp?id="+id+"&course_name=" + course_name).forward(request, response);
			return ;
		}
		//更新数据
		CourseDAO.updata_xgzy(id,sykc_xgzy);
		
		request.setAttribute("msg", "修改成功");
		request.getRequestDispatcher("editsykcgl.jsp?id="+id+"&course_name=" + course_name).forward(request,response);
		
	}

	private void list_syke(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub

		int pageNo = 1;
		String strPageNo = request.getParameter("pageNo");
		int role = ((Admin) request.getSession().getAttribute("admin"))
				.getRole();
		if (strPageNo != null && !strPageNo.trim().equals("")) {
			try {
				pageNo = Integer.parseInt(strPageNo);
			} catch (NumberFormatException e) {
				pageNo = 1;
			}
		}
		if (pageNo <= 0)
			pageNo = 1;
		ArrayList<Course_sy> courseList = new ArrayList<Course_sy>();
		Connection conn = DatabaseUtil.getConn();
		ResultSet rsCount = null;
		PreparedStatement pstmt = null;
		Statement stmtCount = null;
		Statement stmt = DatabaseUtil.createStmt(conn);
		ResultSet rs = null;
		int totalPages = 0;
		int pageSize = 15; // 设置每页显示多少条
		try {
			stmtCount = DatabaseUtil.createStmt(conn);
			String countSQL = "select count(*) from _course_sy";
			String sql = "select * from _course_sy";
			int departmentID = 0;
			// if(role == 1){
			departmentID = ((Admin) request.getSession().getAttribute("admin"))
					.getDepartmentID();
			sql += " where departmentID = " + departmentID;
			countSQL += " where departmentID = " + departmentID;
			// }
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
			sql += " order by departmentID desc limit ?, ?";
			pstmt = DatabaseUtil.prepareStmt(conn, sql);
			pstmt.setInt(1, startPos);
			pstmt.setInt(2, pageSize);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				Course_sy t = new Course_sy();
				CourseDAO.initFromRS_course_sy(rs, t);
				courseList.add(t);
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
			request.setAttribute("courseList", courseList);
			request.getRequestDispatcher("course_sy.jsp").forward(request,
					response);
		}
	}

	public void list(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		int pageNo = 1;
		String strPageNo = request.getParameter("pageNo");
		int role = ((Admin) request.getSession().getAttribute("admin"))
				.getRole();
		if (strPageNo != null && !strPageNo.trim().equals("")) {
			try {
				pageNo = Integer.parseInt(strPageNo);
			} catch (NumberFormatException e) {
				pageNo = 1;
			}
		}
		if (pageNo <= 0)
			pageNo = 1;
		ArrayList<Course> courseList = new ArrayList<Course>();
		Connection conn = DatabaseUtil.getConn();
		ResultSet rsCount = null;
		PreparedStatement pstmt = null;
		Statement stmtCount = null;
		Statement stmt = DatabaseUtil.createStmt(conn);
		ResultSet rs = null;
		int totalPages = 0;
		int pageSize = 15; // 设置每页显示多少条
		try {
			stmtCount = DatabaseUtil.createStmt(conn);
			String countSQL = "select count(*) from _course";
			String sql = "select * from _course";
			int departmentID = 0;
			if (role == 1) {
				departmentID = ((Admin) request.getSession().getAttribute(
						"admin")).getDepartmentID();
				sql += " where departmentID = " + departmentID;
				countSQL += " where departmentID = " + departmentID;
			}
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
			sql += " order by departmentID desc limit ?, ?";
			pstmt = DatabaseUtil.prepareStmt(conn, sql);
			pstmt.setInt(1, startPos);
			pstmt.setInt(2, pageSize);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				Course t = new Course();
				CourseDAO.initFromRS(rs, t);
				courseList.add(t);
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
			request.setAttribute("courseList", courseList);
			request.getRequestDispatcher("course.jsp").forward(request,
					response);
		}
	}

	public void add(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String id = request.getParameter("id");
		String name = request.getParameter("name");
		String type = request.getParameter("type");
		int departmentID = Integer.parseInt(request
				.getParameter("departmentID"));
		Connection conn = DatabaseUtil.getConn();
		String sql = "insert into _course values(?, ?, ?, ?)";
		PreparedStatement pstmt = DatabaseUtil.prepareStmt(conn, sql);
		try {
			int i = 1;
			pstmt.setString(i++, id);
			pstmt.setString(i++, name);
			pstmt.setInt(i++, departmentID);
			pstmt.setString(i++, type);
			pstmt.executeUpdate();
			request.getSession().removeAttribute("courseList");
			request.setAttribute("msg", "添加成功");
			LogDAO.add(
					(String) request.getSession().getAttribute("admin_name"),
					"添加课程信息[ID " + id + "课程名 " + name + "]");
			request.getRequestDispatcher("course.jsp").forward(request,
					response);
		} catch (SQLException e) {
			e.printStackTrace();
			request.setAttribute("msg", "添加失败,请检查是否重复添加!");
			request.getRequestDispatcher("error.jsp")
					.forward(request, response);
			return;
		} finally {
			DatabaseUtil.close(pstmt);
			DatabaseUtil.close(conn);
		}
	}

	public void delete(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String id = request.getParameter("id");
		Connection conn = DatabaseUtil.getConn();
		String sql = "delete from _course where id = ?";
		PreparedStatement pstmt = DatabaseUtil.prepareStmt(conn, sql);
		try {
			pstmt.setString(1, id);
			pstmt.executeUpdate();
			request.getSession().removeAttribute("courseList");
			request.setAttribute("msg", "删除成功");
			LogDAO.add(
					(String) request.getSession().getAttribute("admin_name"),
					"删除课程信息[ID " + id + "]");
			request.getRequestDispatcher("course.jsp").forward(request,
					response);
		} catch (SQLException e) {
			e.printStackTrace();
			request.setAttribute("msg", "删除失败!");
			request.getRequestDispatcher("error.jsp")
					.forward(request, response);
			return;
		} finally {
			DatabaseUtil.close(pstmt);
			DatabaseUtil.close(conn);
		}
	}

	public void update(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String id = request.getParameter("id");
		String name = request.getParameter("name");
		String type = request.getParameter("type");
		Connection conn = DatabaseUtil.getConn();
		String sql = "update _course set name = ?, type = ? where id = ?";
		PreparedStatement pstmt = null;
		try {
			pstmt = DatabaseUtil.prepareStmt(conn, sql);
			int i = 1;
			pstmt.setString(i++, name);
			pstmt.setString(i++, type);
			pstmt.setString(i++, id);
			pstmt.executeUpdate();
			request.setAttribute("msg", "修改成功");
			LogDAO.add(
					(String) request.getSession().getAttribute("admin_name"),
					"修改课程信息[ID " + id + "课程名 " + name + "]");
			request.getRequestDispatcher("course.jsp").forward(request,
					response);
		} catch (SQLException e) {
			e.printStackTrace();
			request.setAttribute("msg", "修改课程信息失败!");
			request.getRequestDispatcher("error.jsp")
					.forward(request, response);
			return;
		} finally {
			DatabaseUtil.close(pstmt);
			DatabaseUtil.close(conn);
		}
	}

	public void import_from_excel(HttpServletRequest request,
			HttpServletResponse response, Request req, SmartUpload mySmartUpload)
			throws ServletException, IOException {
		String ext = "";
		String url = "upload/import_excel/"; // 应保证在根目录中有此目录的存在 上传图片路径
		String saveurl = "";
		com.jspsmart.upload.File file1 = mySmartUpload.getFiles().getFile(0);
		if (!file1.isMissing()) {
			try {
				ext = file1.getFileExt(); // 取得后缀名
				// 更改文件名，取得当前上传时间的毫秒数值
				Calendar calendar = Calendar.getInstance();
				String filename = String.valueOf(calendar.getTimeInMillis());
				saveurl = request.getSession().getServletContext().getRealPath(
						"/")
						+ url;
				saveurl += filename + "." + ext; // 文件绝对路径
				file1.saveAs(saveurl, mySmartUpload.SAVE_PHYSICAL);
			} catch (Exception e) {
				e.printStackTrace();
				request.setAttribute("msg", "上传excel文件失败!");
				request.getRequestDispatcher("error.jsp").forward(request,
						response);
				return;
			}
		}
		Connection conn = DatabaseUtil.getConn();
		PreparedStatement pstmt = null;
		try {
			InputStream is = new FileInputStream(saveurl);
			Workbook rwb = Workbook.getWorkbook(is);
			// 这里有两种方法获取sheet表,1为名字Sheet st =
			// rwb.getSheet("original")，二为下标，从0开始Sheet st = rwb.getSheet("0")
			Sheet st = rwb.getSheet(0);
			// 通用的获取cell值的方式,返回字符串
			// int columnum = st.getColumns(); // 得到列数
			int rownum = st.getRows(); // 得到行数
			System.out.println(rownum);
			/*
			 * Map<String, Integer> departmentMap = new HashMap<String,
			 * Integer>(); departmentMap.put("计算机科学与工程系", 1);
			 * departmentMap.put("物理与电子工程系", 2); departmentMap.put("生物学系", 3);
			 * departmentMap.put("中国语言文学系", 4); departmentMap.put("地理与旅游管理系",
			 * 5); departmentMap.put("化学系", 6); departmentMap.put("教育学系", 7);
			 * departmentMap.put("历史学系", 8); departmentMap.put("美术与设计系", 9);
			 * departmentMap.put("数学与统计学系", 10); departmentMap.put("体育学系", 11);
			 * departmentMap.put("外国语言文学系", 12); departmentMap.put("音乐学系", 13);
			 * departmentMap.put("政法系", 14); departmentMap.put("陶瓷学院", 15);
			 * departmentMap.put("陶瓷工艺系", 16); departmentMap.put("基础教育师资系", 17);
			 * departmentMap.put("管理系", 18); departmentMap.put("教务处", 19);
			 */

			int count = rownum / 20;
			String sql = "insert into _course values(?, ?, ?, ?)";
			for (int i = 0; i < 19; i++) {
				sql += ", (?, ?, ?, ?)";
			}
			pstmt = DatabaseUtil.prepareStmt(conn, sql);
			int row = 1;
			for (int k = 0; k < count && row < rownum; k++) {
				int pcount = 1;
				for (int i = 0; i < 20; i++) {
					String id = st.getCell(0, row).getContents(); // 课程id
																	// 10c21045
					String name = st.getCell(1, row).getContents(); // 课程名
																	// 办公室事务管理
					int departmentID = Integer.parseInt(st.getCell(2, row)
							.getContents()); // 开课单位 地理与旅游管理系
					String type = st.getCell(3, row).getContents();// 课程性质 限选课
					pstmt.setString(pcount++, id);
					pstmt.setString(pcount++, name);
					pstmt.setInt(pcount++, departmentID);
					pstmt.setString(pcount++, type);
					row++;
				}
				conn.setAutoCommit(false); // 事务开始
				pstmt.executeUpdate();
			}
			int left_count = (rownum - 1) % 20; // 不满20的记录
			if (left_count != 0) {
				sql = "insert into _course values(?, ?, ?, ?)";
				for (int i = 1; i < left_count; i++) {
					sql += ", (?, ?, ?, ?)";
				}
				pstmt = DatabaseUtil.prepareStmt(conn, sql);
				int pcount = 1;
				for (int i = 0; i < left_count; i++) {
					String id = st.getCell(0, row).getContents(); // 课程id
																	// 10c21045
					String name = st.getCell(1, row).getContents(); // 课程名
																	// 办公室事务管理
					int departmentID = Integer.parseInt(st.getCell(2, row)
							.getContents()); // 开课单位 地理与旅游管理系
					String type = st.getCell(3, row).getContents();// 课程性质 限选课
					pstmt.setString(pcount++, id);
					pstmt.setString(pcount++, name);
					pstmt.setInt(pcount++, departmentID);
					pstmt.setString(pcount++, type);
					row++;
				}
				pstmt.executeUpdate();
				conn.commit(); // 事务提交
				conn.setAutoCommit(true); // 设置成自动提交模式
			}
			// 关闭
			rwb.close();
			request.setAttribute("msg", "导入成功");
			request.getRequestDispatcher("importData.jsp").forward(request,
					response);
		} catch (jxl.read.biff.BiffException e) {
			request.setAttribute("msg", "无法识别该excel文件");
			request.getRequestDispatcher("error.jsp")
					.forward(request, response);
			e.printStackTrace();
		} catch (Exception e) {
			request.setAttribute("msg", "导入数据失败");
			request.getRequestDispatcher("error.jsp")
					.forward(request, response);
			e.printStackTrace();
		} finally {
			DatabaseUtil.close(pstmt);
			DatabaseUtil.close(conn);
		}
	}

	public void search(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		Connection conn = DatabaseUtil.getConn();
		PreparedStatement pstmt = null;
		String name = request.getParameter("name");
		String sql = "select * from _course where name like ?";
		int departmentID = ((Admin) request.getSession().getAttribute("admin"))
				.getDepartmentID(); // 管理员的话此值为0
		if (departmentID != 0) {
			sql += " and departmentID = " + departmentID;
		}
		ResultSet rs = null;
		try {
			pstmt = DatabaseUtil.prepareStmt(conn, sql);
			pstmt.setString(1, "%" + name + "%");
			rs = pstmt.executeQuery();
			ArrayList<Course> courseList = new ArrayList<Course>();
			while (rs.next()) {
				Course t = new Course();
				CourseDAO.initFromRS(rs, t);
				courseList.add(t);
			}
			request.setAttribute("courseList", courseList);
			request.setAttribute("search", "search");
			request.setAttribute("msg", "搜索结果如下");
			request.getRequestDispatcher("course.jsp").forward(request,
					response);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DatabaseUtil.close(rs);
			DatabaseUtil.close(pstmt);
			DatabaseUtil.close(conn);
		}
	}

	public void search_syke(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		Connection conn = DatabaseUtil.getConn();
		PreparedStatement pstmt = null;
		String name = request.getParameter("name");
		String sql = "select * from _course_sy where name like ?";
		int departmentID = ((Admin) request.getSession().getAttribute("admin"))
				.getDepartmentID(); // 管理员的话此值为0
		if (departmentID != 0) {
			sql += " and departmentID = " + departmentID;
		}
		ResultSet rs = null;
		try {
			pstmt = DatabaseUtil.prepareStmt(conn, sql);
			pstmt.setString(1, "%" + name + "%");
			rs = pstmt.executeQuery();
			ArrayList<Course_sy> courseList = new ArrayList<Course_sy>();
			while (rs.next()) {
				Course_sy t = new Course_sy();
				CourseDAO.initFromRS_course_sy(rs, t);
				courseList.add(t);
			}
			request.setAttribute("courseList", courseList);
			request.setAttribute("search", "search");
			request.setAttribute("msg", "搜索结果如下");
			request.getRequestDispatcher("course_sy.jsp").forward(request,response);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DatabaseUtil.close(rs);
			DatabaseUtil.close(pstmt);
			DatabaseUtil.close(conn);
		}
	}

}
