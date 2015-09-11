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
import java.util.Map;

import com.jspsmart.upload.Request;
import com.jspsmart.upload.SmartUpload;
import com.ltms.dao.LogDAO;
import com.ltms.dao.TeacherDAO;
import com.ltms.dao.UnitDAO;
import com.ltms.model.Admin;
import com.ltms.model.Teacher;
import com.ltms.model.Unit;
import com.ltms.util.DatabaseUtil;
import com.ltms.util.Encrypt;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jxl.Sheet;
import jxl.Workbook;

public class TeacherServlet extends HttpServlet {
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
			if (method.equals("import_from_excel"))
				import_from_excel(request, response, req, mySmartUpload);
			else
				return;
		} else if (!"".equals(method)) {
			if (method.equals("list"))
				list(request, response);
			else if (method.equals("search"))
				search(request, response);
			else if (method.equals("delete"))
				delete(request, response);
			else if (method.equals("update_"))
				update_(request, response);
			else if (method.equals("add"))
				add(request, response);
			else if (method.equals("getAjax"))
				getAjax(request, response);
			else if (method.equals("get_jq_Ajax"))
				get_jq_Ajax(request, response);
			else if (method.equals("reset_password"))
				reset_password(request, response);
			else
				return;
		}
	}

	private void update_(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		String id = request.getParameter("id");
		String unitID = request.getParameter("unitID");
		Connection conn = DatabaseUtil.getConn();
		String sql = "update _teacher set unitID = ? where id = ?";
		PreparedStatement pstmt = DatabaseUtil.prepareStmt(conn, sql);
		try {
			pstmt.setString(1, unitID);
			pstmt.setString(2, id);
			pstmt.executeUpdate();
			request.getSession().removeAttribute("teacherList");
			request.setAttribute("msg", "修改成功");
			LogDAO.add(
					(String) request.getSession().getAttribute("admin_name"),
					"修改教师信息[ID " + id + "]");
			request.getRequestDispatcher("teacher.jsp").forward(request,
					response);
		} catch (SQLException e) {
			e.printStackTrace();
			request.setAttribute("msg", "修改失败!");
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
		System.out.println("id is " + id);
		String unitID = request.getParameter("unitID");
		System.out.println("unitID is " + unitID);
		Connection conn = DatabaseUtil.getConn();
		String sql = "delete from _teacher where unitID = ? and id = ?";
		PreparedStatement pstmt = DatabaseUtil.prepareStmt(conn, sql);
		try {
			pstmt.setString(1, unitID);
			pstmt.setString(2, id);
			pstmt.executeUpdate();
			request.getSession().removeAttribute("teacherList");
			request.setAttribute("msg", "删除成功");
			LogDAO.add(
					(String) request.getSession().getAttribute("admin_name"),
					"删除教师信息[ID " + id + "]");
			request.getRequestDispatcher("teacher.jsp").forward(request,
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
		ArrayList<Teacher> teacherList = new ArrayList<Teacher>();
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
			String countSQL = "select count(*) from _teacher";
			String sql = "select * from _teacher";
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
			sql += " order by unitID desc limit ?, ?";
			pstmt = DatabaseUtil.prepareStmt(conn, sql);
			pstmt.setInt(1, startPos);
			pstmt.setInt(2, pageSize);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				Teacher t = new Teacher();
				TeacherDAO.initFromRS(rs, t);
				teacherList.add(t);
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
			request.setAttribute("teacherList", teacherList);
			request.getRequestDispatcher("teacher.jsp").forward(request,
					response);
		}
	}

	public void reset_password(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String id = request.getParameter("id");
		String default_password = "123456";
		Admin admin = (Admin) request.getSession().getAttribute("admin");
		if (admin.getRole() == 0) {
			Connection conn = DatabaseUtil.getConn();
			String sql = "update _teacher set password = ? where id = ?";
			PreparedStatement pstmt = DatabaseUtil.prepareStmt(conn, sql);
			try {
				int i = 1;
				pstmt.setString(i++, Encrypt.encrypt(default_password));
				pstmt.setString(i++, id);
				pstmt.executeUpdate();
				request.getSession().removeAttribute("teacherList");
				request.setAttribute("msg", "重置密码成功");
				LogDAO.add((String) request.getSession().getAttribute(
						"admin_name"), "重置教师[" + id + "]密码");
				request.getRequestDispatcher("teacher.jsp").forward(request,
						response);
			} catch (SQLException e) {
				e.printStackTrace();
				request.setAttribute("msg", "重置密码失败!");
				request.getRequestDispatcher("error.jsp").forward(request,
						response);
				return;
			} finally {
				DatabaseUtil.close(pstmt);
				DatabaseUtil.close(conn);
			}
		} else {
			request.setAttribute("msg", "您不是该系教务员,无法执行该操作!");
			request.getRequestDispatcher("error.jsp")
					.forward(request, response);
			return;
		}
	}

	public void add(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		Connection conn = DatabaseUtil.getConn();
		PreparedStatement pstmt = null;
		String name = request.getParameter("name");
		String number = request.getParameter("number");
		int unitID = Integer.parseInt(request.getParameter("unitID"));
		String sql = "insert into _teacher values(?,?,?,?)";
		String default_password = "123456";
		ResultSet rs = null;
		try {
			pstmt = DatabaseUtil.prepareStmt(conn, sql);
			pstmt.setString(1, number);
			pstmt.setString(2, name);
			pstmt.setInt(3, unitID);
			pstmt.setString(4, Encrypt.encrypt(default_password));
			pstmt.executeUpdate();

			request.setAttribute("msg", "添加成功");
			request.getRequestDispatcher("teacher.jsp").forward(request,
					response);
		} catch (SQLException e) {
			e.printStackTrace();
			request.setAttribute("msg", "添加失败");
			request.getRequestDispatcher("teacher.jsp").forward(request,
					response);
		} finally {
			DatabaseUtil.close(rs);
			DatabaseUtil.close(pstmt);
			DatabaseUtil.close(conn);
		}
	}

	public void search(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		Connection conn = DatabaseUtil.getConn();
		PreparedStatement pstmt = null;
		String name = request.getParameter("name");
		int unitID = Integer.parseInt(request.getParameter("unitID"));
		String sql = "select * from _teacher where name like ?";
		if (unitID != 0) {
			sql += " and unitID = " + unitID;
		}
		ResultSet rs = null;
		try {
			pstmt = DatabaseUtil.prepareStmt(conn, sql);
			pstmt.setString(1, "%" + name + "%");
			rs = pstmt.executeQuery();
			ArrayList<Teacher> teacherList = new ArrayList<Teacher>();
			while (rs.next()) {
				Teacher t = new Teacher();
				TeacherDAO.initFromRS(rs, t);
				teacherList.add(t);
			}
			request.setAttribute("teacherList", teacherList);
			request.setAttribute("search", "search");
			request.setAttribute("msg", "搜索结果如下");
			request.getRequestDispatcher("teacher.jsp").forward(request,
					response);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DatabaseUtil.close(rs);
			DatabaseUtil.close(pstmt);
			DatabaseUtil.close(conn);
		}
	}

	public void getAjax(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		int unitID = Integer.parseInt(request.getParameter("unitID"));
		ArrayList<Teacher> teacherList = TeacherDAO.list(unitID);
		response.setContentType("text/html;charset=utf-8");
		response.setCharacterEncoding("utf-8");
		PrintWriter out = response.getWriter();
		String istring = "<select id='teacherID' name='teacherID' style='width:100px;height:23px;margin:0;'>";
		for (Teacher t : teacherList) {
			istring += "<option value='" + t.getId() + "'>" + t.getName()
					+ "</option>";
		}
		if (teacherList.size() == 0) {
			istring += "<option value='0'>该单位暂无教师数据</option>";
		}
		istring += "</select>";
		out.println(istring);
		out.flush();
		out.close();
	}

	public void get_jq_Ajax(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		int unitID = Integer.parseInt(request.getParameter("unitID"));
		int num = Integer.parseInt(request.getParameter("num"));
		ArrayList<Teacher> teacherList = TeacherDAO.list(unitID);
		response.setContentType("text/html;charset=utf-8");
		response.setCharacterEncoding("utf-8");
		PrintWriter out = response.getWriter();
		String istring = "<select id='o_teacherID" + num
				+ "' name='o_teacherID" + num
				+ "' style='width:100px;height:23px;margin:0;'>";
		istring += "<option value='0'></option>";
		for (Teacher t : teacherList) {
			istring += "<option value='" + unitID + "," + t.getId() + "'>"
					+ t.getName() + "</option>";
		}
		if (teacherList.size() == 0) {
			istring += "<option value='0'>该单位暂无教师数据</option>";
		}
		istring += "</select>";
		out.println(istring);
		out.flush();
		out.close();
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
		ResultSet rs = null;
		try {
			InputStream is = new FileInputStream(saveurl);
			Workbook rwb = Workbook.getWorkbook(is);
			// 这里有两种方法获取sheet表,1为名字Sheet st =
			// rwb.getSheet("original")，二为下标，从0开始Sheet st = rwb.getSheet("0")
			Sheet st = rwb.getSheet(0);
			// 通用的获取cell值的方式,返回字符串
			// int columnum = st.getColumns(); // 得到列数
			int rownum = st.getRows(); // 得到行数

			Map<String, Integer> unitMap = new HashMap<String, Integer>();
			ArrayList<Unit> unitList = UnitDAO.list();
			for (Unit uu : unitList) {
				// 所有系的名字和id
				unitMap.put(uu.getName(), uu.getId());
			}
			int size = unitList.size();

			ArrayList<String> te_id_list = new ArrayList<String>();
			String sql = "select id from _teacher";
			pstmt = DatabaseUtil.prepareStmt(conn, sql);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				// 所有老师的id
				te_id_list.add(rs.getString("id"));
			}
			conn.setAutoCommit(false); // 事务开始
			for (int i = 1; i < rownum; i++) {
				String unitName = st.getCell(1, i).getContents();
				if (!"".equals(unitName)) {
					if (unitMap.get(unitName) == null) {
						unitMap.put(unitName, ++size);
						sql = "insert into _unit values(?, ?)";
						pstmt = DatabaseUtil.prepareStmt(conn, sql);
						pstmt.setInt(1, size);
						pstmt.setString(2, unitName);
						pstmt.executeUpdate();
					}
					String id = st.getCell(0, i).getContents();
					String name = st.getCell(2, i).getContents();
					int unitID = unitMap.get(unitName);
					if (te_id_list.contains(id)) {
						sql = "update _teacher set name = ?, unitID = ? where id = ?";
						int pi = 1;
						pstmt = DatabaseUtil.prepareStmt(conn, sql);
						pstmt.setString(pi++, name);
						pstmt.setInt(pi++, unitID);
						pstmt.setString(pi++, id);
						pstmt.executeUpdate();
					} else {
						sql = "insert into _teacher values(?, ?, ?, ?)";
						int pi = 1;
						pstmt = DatabaseUtil.prepareStmt(conn, sql);
						pstmt.setString(pi++, id);
						pstmt.setString(pi++, name);
						pstmt.setInt(pi++, unitID);
						pstmt.setString(pi++, Encrypt.encrypt("123456"));
						pstmt.executeUpdate();
					}
				}
			}
			conn.commit(); // 事务提交
			conn.setAutoCommit(true); // 设置成自动提交模式
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
}
