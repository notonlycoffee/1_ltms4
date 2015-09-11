package com.ltms.servlet;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ltms.dao.DepartmentDAO;
import com.ltms.dao.ExperimentDAO;
import com.ltms.dao.LogDAO;
import com.ltms.dao.TeacherDAO;
import com.ltms.model.Admin;
import com.ltms.model.Experiment;
import com.ltms.util.DatabaseUtil;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;

public class ExperimentServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		this.doPost(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String method = request.getParameter("method");
		//System.out.println(method);
		if(method == null || "".equals(method)) return;
		if(method.equals("list")) list(request, response);
		if(method.equals("index_list_obl")) index_list_obl(request, response);
		if(method.equals("list_music")) list_music(request, response);
		else if(method.equals("add")) add(request, response);
		else if(method.equals("add_music")) add_music(request, response);
		else if(method.equals("delete")) delete(request, response);
		else if(method.equals("delete_music")) delete_music(request, response);
		else if(method.equals("update")) update(request, response);
		else if(method.equals("update_music")) update_music(request, response);
		else if(method.equals("search")) search(request, response);
		else if(method.equals("outport_to_excel")) outport_to_excel(request, response);
		else return;
	}

	public void list(HttpServletRequest request, HttpServletResponse response)
	throws ServletException, IOException{
		int state = 1;
		
		String currenTerm_ = (String) request.getSession().getAttribute("currenTerm");
		String currenTerm__ = (String) request.getSession().getAttribute("currenTerm");
		if("".equals(currenTerm__) || currenTerm__.trim().length() == 0){
			
			request.setAttribute("msg", "请先选择学年学期");
			request.getRequestDispatcher("showMsg.jsp").forward(request, response);
			return ;
		}
		
		try{
			Integer.parseInt(request.getParameter("state"));
		}catch (Exception e) {
			state = 1;
		}
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
		ArrayList<Experiment> experimentList = new ArrayList<Experiment>();
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
			String countSQL = "select count(*) from _experiment where state = " + state;
			String sql = "select * from _experiment where state = " + state;
			int departmentID = 0;
			if(role == 1){
				departmentID = ((Admin)request.getSession().getAttribute("admin")).getDepartmentID();
				sql += " and departmentID = " + departmentID + " and term like ?";
				countSQL += " and departmentID = " + departmentID + " and term like '"+ currenTerm_ +"'";
			}
			rsCount = DatabaseUtil.executeQuery(stmtCount, countSQL);
			int totalRecords;
			rsCount.next();
			totalRecords = rsCount.getInt(1);
			System.out.println("all is " + totalRecords);
			totalPages = (totalRecords + pageSize - 1)/pageSize;
			if(totalPages == 0){
				totalPages = 1;
			}
			if(pageNo > totalPages) pageNo = totalPages;
			int startPos = (pageNo-1) * pageSize; 
			sql +=" order by departmentID, term desc, id desc limit ?, ?";
			pstmt = DatabaseUtil.prepareStmt(conn, sql);
			pstmt.setString(1, "%" + currenTerm_ + "%");
			pstmt.setInt(2, startPos);
			pstmt.setInt(3, pageSize);
			rs = pstmt.executeQuery();
			while(rs.next()) {
				Experiment e = new Experiment();
				ExperimentDAO.initFromRS(rs, e);
				experimentList.add(e);
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
			request.setAttribute("experimentList", experimentList);
			request.getRequestDispatcher("listExperiment.jsp").forward(request, response);
		}
	}
	
	public void list_music(HttpServletRequest request, HttpServletResponse response)
	throws ServletException, IOException{
		int state = 0;
		String teacherID = request.getParameter("teacherID");
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
		ArrayList<Experiment> experimentList = new ArrayList<Experiment>();
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
			String countSQL = "select count(*) from _experiment where state = " + state + " and teacherInfo like '0," + teacherID + "%'";
			String sql = "select * from _experiment where state = " + state + " and teacherInfo like '0," + teacherID + "%'";
			int departmentID = 0;
			if(role == 1){
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
			sql +=" order by departmentID, term desc, id desc limit ?, ?";
			pstmt = DatabaseUtil.prepareStmt(conn, sql);
			pstmt.setInt(1, startPos);
			pstmt.setInt(2, pageSize);
			rs = pstmt.executeQuery();
			while(rs.next()) {
				Experiment e = new Experiment();
				ExperimentDAO.initFromRS(rs, e);
				experimentList.add(e);
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
			request.setAttribute("experimentList", experimentList);
			request.getRequestDispatcher("listExperiment_music.jsp").forward(request, response);
		}
	}
	
	public void add(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException{
		
		String courseID = request.getParameter("courseID");
		String classInfo = request.getParameter("classInfo");
//		System.out.println("classInfo is " + classInfo);
		String teacherInfo = request.getParameter("teacherInfo");
		String timeInfo = request.getParameter("timeInfo");
		String departmentID = request.getParameter("departmentID");
		String laboratoryID = request.getParameter("laboratoryID");
		String type = request.getParameter("type");
		String requirement = request.getParameter("requirement");
		String term = (String)request.getSession().getAttribute("currenTerm");
		int state = Integer.parseInt(request.getParameter("state"));
		//添加的数据
		String symc = request.getParameter("symc");
//		System.out.println("symc is " + symc);
		String syzzy = request.getParameter("ssxk_zy");
//		System.out.println("syzzy is " + syzzy);
		String ssxk_sign = request.getParameter("ssxk");
//		System.out.println("ssxk is " + ssxk);
		String mxzy = request.getParameter("mxzy");
//		System.out.println("mxzy is " + mxzy);
		int sylb = Integer.parseInt(request.getParameter("sylb"));
//		System.out.println("sylb is " + sylb);
		int syzlb = Integer.parseInt(request.getParameter("syzlb"));
//		System.out.println("syzlb is " + syzlb);
		String course_sy_name = request.getParameter("course_sy_name");
		
		String ssxk = ExperimentDAO.load_zydm(ssxk_sign);
		
		int xs = 0;
		try {
			xs = Integer.parseInt(request.getParameter("xs"));
		} catch (Exception e) {
			// TODO: handle exception
			request.setAttribute("msg", "学时必须是数字");
			request.getRequestDispatcher("addExperiment.jsp").forward(request, response);
			return;
		}
		
		if("".equals(symc) || symc.trim().length() == 0 || syzzy.trim().length() == 0 || "".equals(syzzy) || "".equals(ssxk)
				 || ssxk.trim().length() == 0 || "".equals(mxzy) || mxzy.trim().length() == 0){
			request.setAttribute("msg", "所有数据都要填写");
			request.getRequestDispatcher("addExperiment.jsp").forward(request, response);
			return;
		}
			
		int count_mxzy;
		String[] strings_mxzy = mxzy.split(",");
		count_mxzy = strings_mxzy.length;
//		System.out.println("classinfo is " + classInfo);
		int count_classInfo;
		String[] strings_classInfo  = classInfo.split("@");
//		System.out.println("length is " + strings_classInfo.length);
//		for(String st : strings_classInfo){
//			System.out.println(st);
//		}
		count_classInfo = strings_classInfo.length;
		count_classInfo--;
		if((count_classInfo/2) != count_mxzy){
			request.setAttribute("msg", "班级选择和面向专业需对应");
			request.getRequestDispatcher("addExperiment.jsp").forward(request, response);
			return;
		}
		Experiment experiment = new Experiment();
		experiment.setCourseID(courseID);
		experiment.setClassInfo(classInfo);
		experiment.setTeacherInfo(teacherInfo);
		experiment.setDepartmentID(Integer.parseInt(departmentID));
		experiment.setTimeInfo(timeInfo);
		experiment.setLaboratoryID(Integer.parseInt(laboratoryID));
		experiment.setType(type);
		experiment.setRequirement(requirement);
		experiment.setTerm(term);
		experiment.setState(state);
		
		experiment.setSymc(symc);
		experiment.setSyzzy(syzzy);
		experiment.setSsxk(ssxk);
		experiment.setMxzy(mxzy);
		experiment.setSylb(sylb);
		experiment.setXs(xs);
		experiment.setSyzlb(syzlb);
		
		if(ExperimentDAO.add(experiment) != 0){
			//把实验课程添加到数据库中
			ExperimentDAO.addSykc(experiment);
			request.getSession().removeAttribute("experimentList");
			request.setAttribute("msg", "添加成功");
			LogDAO.add((String)request.getSession().getAttribute("admin_name"), "添加实验信息[课程ID " + courseID + "]");
			request.getRequestDispatcher("listExperiment.jsp").forward(request, response);
		}else{
			request.setAttribute("msg", "添加失败!");
			request.getRequestDispatcher("error.jsp").forward(request, response);
			return;
		}
	}
	
	public void add_music(HttpServletRequest request, HttpServletResponse response)
	throws ServletException, IOException{
		String courseID = request.getParameter("courseID");
		String classInfo = request.getParameter("classInfo");
		String teacherInfo = request.getParameter("teacherInfo");
		String timeInfo = request.getParameter("timeInfo");
		String departmentID = request.getParameter("departmentID");
		String laboratoryID = request.getParameter("laboratoryID");
		String type = request.getParameter("type");
		String requirement = request.getParameter("requirement");
		String term = (String)request.getSession().getAttribute("currenTerm");
		int state = Integer.parseInt(request.getParameter("state"));
		
		//添加的数据
		String symc = request.getParameter("symc");
//		System.out.println("symc is " + symc);
		String syzzy = request.getParameter("ssxk_zy");
//		System.out.println("syzzy is " + syzzy);
		String ssxk_sign = request.getParameter("ssxk");
//		System.out.println("ssxk is " + ssxk);
		String mxzy = request.getParameter("mxzy");
//		System.out.println("mxzy is " + mxzy);
		int sylb = Integer.parseInt(request.getParameter("sylb"));
//		System.out.println("sylb is " + sylb);
		int syzlb = Integer.parseInt(request.getParameter("syzlb"));
//		System.out.println("syzlb is " + syzlb);
		String course_sy_name = request.getParameter("course_sy_name");
		
		String ssxk = ExperimentDAO.load_zydm(ssxk_sign);
		
		int xs = 0;
		try {
			xs = Integer.parseInt(request.getParameter("xs"));
			System.out.println("xs is " + xs);
		} catch (Exception e) {
			// TODO: handle exception
			request.setAttribute("msg", "学时必须是数字");
			request.getRequestDispatcher("addExperiment.jsp").forward(request, response);
			return;
		}
		
		if("".equals(symc) || symc.trim().length() == 0 || syzzy.trim().length() == 0 || "".equals(syzzy) || "".equals(ssxk)
				 || ssxk.trim().length() == 0 || "".equals(mxzy) || mxzy.trim().length() == 0){
			request.setAttribute("msg", "所有数据都要填写");
			request.getRequestDispatcher("addExperiment.jsp").forward(request, response);
			return;
		}
		
		Experiment experiment = new Experiment();
		experiment.setCourseID(courseID);
		experiment.setClassInfo(classInfo);
		experiment.setTeacherInfo(teacherInfo);
		experiment.setDepartmentID(Integer.parseInt(departmentID));
		experiment.setTimeInfo(timeInfo);
		experiment.setLaboratoryID(Integer.parseInt(laboratoryID));
		experiment.setType(type);
		experiment.setRequirement(requirement);
		experiment.setTerm(term);
		experiment.setState(state);
		
		experiment.setSymc(symc);
		experiment.setSyzzy(syzzy);
		experiment.setSsxk(ssxk);
		experiment.setMxzy(mxzy);
		experiment.setSylb(sylb);
		experiment.setXs(xs);
		experiment.setSyzlb(syzlb);
		
		if(ExperimentDAO.add(experiment) != 0){
			//把实验课程添加到数据库中
			ExperimentDAO.addSykc(experiment);
			request.getSession().removeAttribute("experimentList");
			request.setAttribute("msg", "添加成功");
			LogDAO.add((String)request.getSession().getAttribute("admin_name"), "添加实验信息[课程ID " + courseID + "]");
			request.getRequestDispatcher("listExperiment_music.jsp").forward(request, response);
		}else{
			request.setAttribute("msg", "添加失败!");
			request.getRequestDispatcher("error.jsp").forward(request, response);
			return;
		}
		}

	public void delete(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException{
		int id = Integer.parseInt(request.getParameter("id"));
		int departmentID = Integer.parseInt(request.getParameter("departmentID"));
		Admin admin = ((Admin)request.getSession().getAttribute("admin"));
//		if(admin.getRole() == 2){
//			request.setAttribute("msg", "您没有权限进行该操作!");
//			request.getRequestDispatcher("error.jsp").forward(request, response);
//			return;
//		}
//		if(admin.getRole() != 0 && admin.getDepartmentID() != departmentID){
//			request.setAttribute("msg", "您不是该系任课老师, 无法删除该系实验信息!");
//			request.getRequestDispatcher("error.jsp").forward(request, response);
//			return;
//		}
		if(ExperimentDAO.delete(id)){
			request.getSession().removeAttribute("experimentList");
			request.setAttribute("msg", "删除成功");
			LogDAO.add((String)request.getSession().getAttribute("admin_name"), "删除实验信息[ID " + id + "]");
			request.getRequestDispatcher("listExperiment.jsp").forward(request, response);
		}else{
			request.setAttribute("msg", "删除失败!");
			request.getRequestDispatcher("error.jsp").forward(request, response);
			return;
		}
	}
	
	public void delete_music(HttpServletRequest request, HttpServletResponse response)
	throws ServletException, IOException{
		int id = Integer.parseInt(request.getParameter("id"));
		if(ExperimentDAO.delete(id)){
			request.getSession().removeAttribute("experimentList");
			request.setAttribute("msg", "删除成功");
			LogDAO.add((String)request.getSession().getAttribute("admin_name"), "删除实验信息[ID " + id + "]");
			request.getRequestDispatcher("listExperiment_music.jsp").forward(request, response);
		}else{
			request.setAttribute("msg", "删除失败!");
			request.getRequestDispatcher("error.jsp").forward(request, response);
			return;
		}
	}

	public void update(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException{
		String id = request.getParameter("id"); 
		String courseID = request.getParameter("courseID");
		String classInfo = request.getParameter("classInfo");
		String teacherInfo = request.getParameter("teacherInfo");
		String timeInfo = request.getParameter("timeInfo");
		String departmentID = request.getParameter("departmentID");
		String laboratoryID = request.getParameter("laboratoryID");
		String type = request.getParameter("type");
		String requirement = request.getParameter("requirement");
		
//		System.out.println("timeinfo is " + timeInfo);
//		System.out.println("teacherinfo is " + teacherInfo);
		
		//添加的数据
		String symc = request.getParameter("symc");
//		System.out.println("symc is " + symc);
		String syzzy = request.getParameter("ssxk_zy");
//		System.out.println("syzzy is " + syzzy);
		String ssxk_sign = request.getParameter("ssxk");
//		System.out.println("ssxk is " + ssxk);
		String mxzy = request.getParameter("mxzy");
//		System.out.println("mxzy is " + mxzy);
		int sylb = Integer.parseInt(request.getParameter("sylb"));
//		System.out.println("sylb is " + sylb);
		int syzlb = Integer.parseInt(request.getParameter("syzlb"));
//		System.out.println("syzlb is " + syzlb);
		String course_sy_name = request.getParameter("course_sy_name");
		
		String ssxk = ExperimentDAO.load_zydm(ssxk_sign);
		
		
		
		int xs = 0;
		try {
			xs = Integer.parseInt(request.getParameter("xs"));
			System.out.println("xs is " + xs);
		} catch (Exception e) {
			// TODO: handle exception
			request.setAttribute("msg", "学时必须是数字");
			request.getRequestDispatcher("editExperiment.jsp?id=" + Integer.parseInt(id)).forward(request, response);
			return;
		}
		
		if("".equals(symc) || symc.trim().length() == 0 || syzzy.trim().length() == 0 || "".equals(syzzy) || "".equals(ssxk)
				 || ssxk.trim().length() == 0 || "".equals(mxzy) || mxzy.trim().length() == 0){
			request.setAttribute("msg", "所有数据都要填写");
			request.getRequestDispatcher("editExperiment.jsp?id=" + Integer.parseInt(id)).forward(request, response);
			return;
		}
		
		
		int count_mxzy;
		String[] strings_mxzy = mxzy.split(",");
//		System.out.println("mxzy is " + mxzy);
		count_mxzy = strings_mxzy.length;
		
		int count_classInfo;
		String[] strings_classInfo  = classInfo.split("@");
		System.out.println("classinfo is " + classInfo);
//		System.out.println("classInfo is " + classInfo);
		count_classInfo = strings_classInfo.length;
//		System.out.println(strings_classInfo.toString());
//		System.out.println(strings_mxzy.toString());
//		System.out.println("count_classInfo is " + count_classInfo);
//		System.out.println("count_mxzy is " + count_mxzy);
		
//		System.out.println("length is " + strings_classInfo.length);
//		for(String st : strings_classInfo){
//			System.out.println(st);
//		}
		if(classInfo.contains("@@")){
			count_classInfo++;
		}
		if((count_classInfo/2) != count_mxzy){
			request.setAttribute("msg", "班级选择和面向专业需对应");
			request.getRequestDispatcher("editExperiment.jsp?id=" + id).forward(request, response);
			return;
		}
		
		int state = Integer.parseInt(request.getParameter("state"));
		Experiment experiment = new Experiment();
		experiment.setId(Integer.parseInt(id));
		experiment.setCourseID(courseID);
		experiment.setClassInfo(classInfo);
		experiment.setTeacherInfo(teacherInfo);
		experiment.setDepartmentID(Integer.parseInt(departmentID));
		experiment.setTimeInfo(timeInfo);
		experiment.setLaboratoryID(Integer.parseInt(laboratoryID));
		experiment.setType(type);
		experiment.setRequirement(requirement);
		experiment.setState(state);
		
		experiment.setSymc(symc);
		experiment.setSyzzy(syzzy);
		experiment.setSsxk(ssxk);
		experiment.setMxzy(mxzy);
		experiment.setSylb(sylb);
		experiment.setXs(xs);
		experiment.setSyzlb(syzlb);
		
		if(ExperimentDAO.update(experiment)){
			request.getSession().removeAttribute("experimentList");
			request.setAttribute("msg", "修改成功");
			LogDAO.add((String)request.getSession().getAttribute("admin_name"), "修改实验信息[ID " + id + "]");
			request.getRequestDispatcher("editExperiment.jsp?id=" + id).forward(request, response);
		}else{
			request.setAttribute("msg", "修改失败!");
			request.getRequestDispatcher("editExperiment.jsp?id=" + id).forward(request, response);
			return;
		}
	}
	
	public void update_music(HttpServletRequest request, HttpServletResponse response)
	throws ServletException, IOException{
		String id = request.getParameter("id"); 
		String courseID = request.getParameter("courseID");
		String classInfo = request.getParameter("classInfo");
		String teacherInfo = request.getParameter("teacherInfo");
		String timeInfo = request.getParameter("timeInfo");
		String departmentID = request.getParameter("departmentID");
		String laboratoryID = request.getParameter("laboratoryID");
		String type = request.getParameter("type");
		String requirement = request.getParameter("requirement");
		int state = Integer.parseInt(request.getParameter("state"));
		
		//添加的数据
		String symc = request.getParameter("symc");
//		System.out.println("symc is " + symc);
		String syzzy = request.getParameter("ssxk_zy");
//		System.out.println("syzzy is " + syzzy);
		String ssxk_sign = request.getParameter("ssxk");
//		System.out.println("ssxk is " + ssxk);
		String mxzy = request.getParameter("mxzy");
//		System.out.println("mxzy is " + mxzy);
		int sylb = Integer.parseInt(request.getParameter("sylb"));
//		System.out.println("sylb is " + sylb);
		int syzlb = Integer.parseInt(request.getParameter("syzlb"));
//		System.out.println("syzlb is " + syzlb);
		String course_sy_name = request.getParameter("course_sy_name");
		
		String ssxk = ExperimentDAO.load_zydm(ssxk_sign);
		
		int xs = 0;
		try {
			xs = Integer.parseInt(request.getParameter("xs"));
			System.out.println("xs is " + xs);
		} catch (Exception e) {
			// TODO: handle exception
			request.setAttribute("msg", "学时必须是数字");
			request.getRequestDispatcher("editExperiment.jsp?id=" + Integer.parseInt(id)).forward(request, response);
			return;
		}
		
		if("".equals(symc) || symc.trim().length() == 0 || syzzy.trim().length() == 0 || "".equals(syzzy) || "".equals(ssxk)
				 || ssxk.trim().length() == 0 || "".equals(mxzy) || mxzy.trim().length() == 0){
			request.setAttribute("msg", "所有数据都要填写");
			request.getRequestDispatcher("editExperiment.jsp?id=" + Integer.parseInt(id)).forward(request, response);
			return;
		}
		
		
		Experiment experiment = new Experiment();
		experiment.setId(Integer.parseInt(id));
		experiment.setCourseID(courseID);
		experiment.setClassInfo(classInfo);
		experiment.setTeacherInfo(teacherInfo);
		experiment.setDepartmentID(Integer.parseInt(departmentID));
		experiment.setTimeInfo(timeInfo);
		experiment.setLaboratoryID(Integer.parseInt(laboratoryID));
		experiment.setType(type);
		experiment.setRequirement(requirement);
		experiment.setState(state);
		
		experiment.setSymc(symc);
		experiment.setSyzzy(syzzy);
		experiment.setSsxk(ssxk);
		experiment.setMxzy(mxzy);
		experiment.setSylb(sylb);
		experiment.setXs(xs);
		experiment.setSyzlb(syzlb);
		
		if(ExperimentDAO.update(experiment)){
			request.getSession().removeAttribute("experimentList");
			request.setAttribute("msg", "修改成功");
			LogDAO.add((String)request.getSession().getAttribute("admin_name"), "修改实验信息[ID " + id + "]");
			request.getRequestDispatcher("editExperiment_music.jsp?id=" + id).forward(request, response);
		}else{
			request.setAttribute("msg", "修改失败!");
			request.getRequestDispatcher("editExperiment_music.jsp?id=" + id).forward(request, response);
			return;
		}
	}
	
	public void search(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException{
		String laboratoryID = request.getParameter("laboratoryID");
		String departmentID = request.getParameter("departmentID");
		String weekday = request.getParameter("weekday");
		String currenTerm_ = (String) request.getSession().getAttribute("currenTerm");
		int state = 1;
		Connection conn = DatabaseUtil.getConn();
		String sql = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		sql = "select * from _experiment where laboratoryID = ?  and timeInfo like ? and term like ? and state = ? order by departmentID, term desc, id desc";
		pstmt = DatabaseUtil.prepareStmt(conn, sql);
		try {
			int i = 1;
			pstmt.setString(i++, laboratoryID);
			pstmt.setString(i++, "%" + weekday + "%");
			pstmt.setString(i++, "%" + currenTerm_ + "%");
			pstmt.setInt(i++, state);
			rs = pstmt.executeQuery();	
			List<Experiment> experimentList = new ArrayList<Experiment>();
			int exitGroup = 0;
			while(rs.next()){
				Experiment e = new Experiment();
				ExperimentDAO.initFromRS(rs, e);
				experimentList.add(e);
				exitGroup ++;
			}
			request.setAttribute("experimentList", experimentList);
			request.setAttribute("search", "search");
			request.setAttribute("exitGroup",exitGroup);
			request.setAttribute("laboratoryID",Integer.parseInt(laboratoryID));
			request.setAttribute("dID",Integer.parseInt(departmentID));
			request.setAttribute("weekday",weekday);
			request.getRequestDispatcher("listExperiment.jsp").forward(request, response);
		} catch (SQLException e) {
			e.printStackTrace();
			return;
		}finally{
			DatabaseUtil.close(rs);
			DatabaseUtil.close(pstmt);
			DatabaseUtil.close(conn);
		}
	}
	
	public void outport_to_excel(HttpServletRequest request, HttpServletResponse response)
	throws ServletException, IOException{
		String order_field = request.getParameter("order_field");
		if("by_class".equals(order_field)){
			outport_by_class(request, response);
		}else{
			outport_by_la(request, response);
		}
	}

	public void outport_by_class(HttpServletRequest request, HttpServletResponse response)
	throws ServletException, IOException{
		Connection conn = DatabaseUtil.getConn();
		ResultSet rs = null;
		ResultSet rs2 = null;
		PreparedStatement pstmt = null;
		try {
			InputStream in = new FileInputStream(request.getSession().getServletContext().getRealPath("/") + "upload\\experiment_example.xls");
			Workbook work = new HSSFWorkbook(in);
			int departmentID = Integer.parseInt(request.getParameter("departmentID"));
			
			//教师的名字
			Map<String, String> te_map = TeacherDAO.getNameMap();
			//系的名字
			String de_name = DepartmentDAO.getName(departmentID);
			
			String year = request.getParameter("year");
			String term = request.getParameter("term");
			
			Row row;
			Cell cell;
			String sql = "select e.id, l.name as l_name, co.name as co_name, e.classInfo, e.teacherInfo, e.timeInfo," +
					" e.term, e.requirement, e.type from _laboratory as l, _course as co, " +
					" _experiment as e where e.laboratoryID = l.id and e.courseID = co.id and e.term = ? and e.state = 1 and e.departmentID = ?";
			sql +=" order by e.id desc";
			pstmt = DatabaseUtil.prepareStmt(conn, sql);
			pstmt.setString(1, year + term);
			pstmt.setInt(2, departmentID);
			rs = pstmt.executeQuery();
			if(rs.isAfterLast() == rs.isBeforeFirst()){
				request.setAttribute("msg", "暂无实验信息!");
				request.getRequestDispatcher("error.jsp").forward(request, response);
			}else{
				Sheet sheet;
				int min_grade = Integer.parseInt(year.substring(0, 4));
				int grades[] = {min_grade-3, min_grade-2, min_grade-1, min_grade};
//				//对应年级-excel表序列号的Map
//				Map<Integer, Integer> sheetMaps = new HashMap<Integer, Integer>();
//				sheetMaps.put(grades[0], 0);
//				sheetMaps.put(grades[1], 1);
//				sheetMaps.put(grades[2], 2);
//				sheetMaps.put(grades[3], 3);
				//对应周几加的权数
				Map<String, Integer> weekdayMaps = new HashMap<String, Integer>();
				weekdayMaps.put("周一", 0);
				weekdayMaps.put("周二", 1);
				weekdayMaps.put("周三", 2);
				weekdayMaps.put("周四", 3);
				weekdayMaps.put("周五", 4);
				Map<Integer, Integer> timeMaps = new HashMap<Integer, Integer>();
				timeMaps.put(1, 0);
				timeMaps.put(2, 0);
				timeMaps.put(3, 1);
				timeMaps.put(4, 1);
				timeMaps.put(5, 2);
				timeMaps.put(6, 2);
				timeMaps.put(7, 3);
				timeMaps.put(8, 3);
				timeMaps.put(9, 4);
				timeMaps.put(10, 4);
				//对应年级-excel列数的Map
				Map<String, Integer> rowMap0 = new HashMap<String, Integer>();
				Map<String, Integer> rowMap1 = new HashMap<String, Integer>();
				Map<String, Integer> rowMap2 = new HashMap<String, Integer>();
				Map<String, Integer> rowMap3 = new HashMap<String, Integer>();
				ArrayList<Map<String, Integer>> rowMaps = new ArrayList<Map<String, Integer>>();
				rowMaps.add(rowMap0);
				rowMaps.add(rowMap1);
				rowMaps.add(rowMap2);
				rowMaps.add(rowMap3);
				//保存cellStyle
				CellStyle titleStyle1 = work.getSheetAt(0).getRow(1).getCell(2).getCellStyle();
				CellStyle titleStyle2 = work.getSheetAt(0).getRow(2).getCell(2).getCellStyle();
				CellStyle contentStyle = work.getSheetAt(0).getRow(3).getCell(2).getCellStyle();
				//...
				ArrayList<ArrayList<Integer>> heightMaps = new ArrayList<ArrayList<Integer>>();
				ArrayList<Integer> grade_counts = new ArrayList<Integer>();
				//创建所有excel列
				for(int gi=0; gi<4; gi++){
					sheet = work.getSheetAt(gi);
					work.setSheetName(gi, grades[gi]+"");
					sql = "select * from _class where grade = ? and departmentID = ?";
					pstmt = DatabaseUtil.prepareStmt(conn, sql);
					pstmt.setInt(1, grades[gi]);
					pstmt.setInt(2, departmentID);
					rs2 = pstmt.executeQuery();
					int colNumer = 2;
					boolean isFirst = true;
					int grade_count = 0;
					while(rs2.next()){
						rowMaps.get(gi).put(rs2.getString("id"), colNumer);
						grade_count ++;
						if(isFirst){
							row = sheet.getRow(1);
							cell = row.getCell(2);
							cell.setCellValue(rs2.getString("id"));
							row = sheet.getRow(2);
							cell = row.getCell(2);
							cell.setCellValue(rs2.getString("name"));
							sheet.setColumnWidth(colNumer, 25*256);
							isFirst = false;
						}else{
							//创建colNumer列
							for(int i=1; i<28; i++){
								cell = sheet.getRow(i).createCell(colNumer);
								cell.setCellStyle(contentStyle);
							}
							sheet.setColumnWidth(colNumer, 25*256);
							cell = sheet.getRow(1).getCell(colNumer);
							cell.setCellStyle(titleStyle1);
							cell.setCellValue(rs2.getString("id"));
							cell = sheet.getRow(2).getCell(colNumer);
							cell.setCellStyle(titleStyle2);
							cell.setCellValue(rs2.getString("name"));
						}
						colNumer++;
					}
					grade_counts.add(grade_count);
					ArrayList<Integer> tt_list = new ArrayList<Integer>(grade_count*25);
					for(int v=0; v<grade_count*25; v++){	
						tt_list.add(0);
					}
					heightMaps.add(tt_list);
					sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, (1+grade_counts.get(gi))));
					row = sheet.getRow(0);
					cell = row.getCell(0);
					//韩山师范学院2013-2014学年第1学期计算机系实验室课表（2011级）
					cell.setCellValue("韩山师范学院" + year + term + de_name + "实验室课表（" +  grades[gi] + "级）");
					CellStyle normalStyle = cell.getCellStyle();
					normalStyle.setBorderBottom((short) 1);
					normalStyle.setBorderLeft((short) 1);
					normalStyle.setBorderRight((short) 1);
					normalStyle.setBorderTop((short) 1);
					normalStyle.setAlignment(CellStyle.ALIGN_CENTER);
					cell.setCellStyle(normalStyle);
					for(int i=1; i<(2+grade_counts.get(gi));i++){
						cell = row.createCell(i);
						cell.setCellStyle(normalStyle);
					}
				}
				while(rs.next()){
					String time[] = rs.getString("timeInfo").split("@");
					for(String tss : time){
						String week[] = tss.split(",");
						String week_string = "";
						int week_length = week.length;
						if(week[0].equals("0")){
							week_string += "第" + week[1] + "周 - 第" + week[2] + "周(" + week[3] + ")";
						}else{ 
							week_string += "第";
							for(int ij=1; ij<week_length-4; ij++){
								week_string += week[ij];
								if(ij != week_length - 5){
									week_string += "、";
								}
							}
							week_string += "周";
						}
						String teacher[] = rs.getString("teacherInfo").split(",");
						String teacher_string = te_map.get(teacher[1]);
						for(int j=2; j<teacher.length-1; j++){
							teacher_string += " " + te_map.get(teacher[++j]);
						}
						String classInfo[] = rs.getString("classInfo").split("@");
						for(int j=0; j<classInfo.length-1; j+=2){
							String syllabus_string = "";
							syllabus_string += rs.getString("co_name") + "\r\n" + 
													 rs.getString("l_name") + "\r\n" +
													 teacher_string + "\r\n" + 
													 week_string;
							int sheetNum = Integer.parseInt(classInfo[j].substring(0, 4)) - (min_grade-3);
							//第几行
							int rr_num = (weekdayMaps.get(week[week_length-3])*5) + timeMaps.get(Integer.parseInt(week[week_length-2])); 
							//第几列
							int cc_num = (rowMaps.get(sheetNum).get(classInfo[j]));
							sheet = work.getSheetAt(sheetNum);
							row = sheet.getRow(3 + rr_num);
							cell = row.getCell(cc_num);
							int index_c = (grade_counts.get(sheetNum))*(rr_num) + cc_num - 2;
							int c_h = heightMaps.get(sheetNum).get(index_c);
							if(c_h == 0){
								cell.setCellValue(syllabus_string);
							}else{
								cell.setCellValue(cell.getStringCellValue()+"\r\n"+syllabus_string);
							}
							heightMaps.get(sheetNum).set(index_c, c_h+1);
						}
					}
				}
				//处理每行行高
				for(int gi=0; gi<4; gi++){
					sheet = work.getSheetAt(gi);
					//处理每行行高
					for(int i=3; i<28; i++){
						int max_h = 0;
						int grade_c = grade_counts.get(gi);
						for(int j=0; j<grade_c; j++){
							int hh = heightMaps.get(gi).get((i-3)*grade_c+j);
							if(hh > max_h){
								max_h = hh;
							}
						}
						if(max_h == 0){
							sheet.getRow(i).setHeightInPoints(20.0f);
						}else{
							sheet.getRow(i).setHeightInPoints(55.0f*max_h);
						}
					}
//					int all_row = rowMaps.get(gi).size();
//					for(int i=1; i<all_row+1; i++){
//						sheet.autoSizeColumn(i);
//					}
				}
				ByteArrayOutputStream ba = new ByteArrayOutputStream();
				work.write(ba);
				String fileName = year + term + de_name + "实验室课表"; // 2013-2014学年度第一学期计算机系实验室课表
				response.setHeader("Expires", "0");
				response.setHeader("Cache-Control", "must-revalidate, post-check=0, pre-check=0");
				response.setHeader("Pragma", "public");
				response.setContentType("application/msexcel");
				response.setHeader("Content-disposition","attachment; filename=" + new String((fileName).getBytes("gbk"),"iso8859-1") + ".xls" );  
				/* 如果想出来让IE提示你是打开还是保存的对话框，加上下面这句就可以了 */
				//response.setHeader("Content-disposition", "attachment; filename=schedule.pdf");
				response.setContentLength(ba.size());
				try{
					ServletOutputStream out = response.getOutputStream();
					ba.writeTo(out);
					out.flush();
					out.close();
					ba.close();
				}catch (IOException ex) {
					ex.printStackTrace();
				}finally {
					if (ba != null) ba.close();
				}
			}
		} catch (FileNotFoundException e) {
			System.out.println("文件路径错误");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("文件输入流错误");
			e.printStackTrace();
		} catch (Exception e){
			e.printStackTrace();
			request.setAttribute("msg", "导出数据时发生错误!");
			request.getRequestDispatcher("error.jsp").forward(request, response);
		}finally{
			DatabaseUtil.close(rs);
			DatabaseUtil.close(rs2);
			DatabaseUtil.close(pstmt);
			DatabaseUtil.close(conn);
		}
	}
	
	public void outport_by_la(HttpServletRequest request, HttpServletResponse response)
	throws ServletException, IOException{
		Connection conn = DatabaseUtil.getConn();
		ResultSet rs = null;
		ResultSet rs2 = null;
		ResultSet rs3 = null;
		PreparedStatement pstmt = null;
		try {
			InputStream in = new FileInputStream(request.getSession().getServletContext().getRealPath("/") + "upload\\experiment_example2.xls");
			Workbook work = new HSSFWorkbook(in);
			int departmentID = Integer.parseInt(request.getParameter("departmentID"));
			Map<String, String> te_map = TeacherDAO.getNameMap();
			String de_name = DepartmentDAO.getName(departmentID);
			String year = request.getParameter("year");
			String term = request.getParameter("term");
			
			Row row;
			Cell cell;
			String sql = "select e.id, l.name as l_name, co.name as co_name, e.classInfo, e.teacherInfo, e.timeInfo," +
					" e.term, e.requirement, e.type from _laboratory as l, _course as co, " +
					" _experiment as e where e.laboratoryID = l.id and e.courseID = co.id and e.term = ? and e.state = 1 and e.departmentID = ?";
			sql +=" order by e.id desc";
			pstmt = DatabaseUtil.prepareStmt(conn, sql);
			pstmt.setString(1, year + term);
			pstmt.setInt(2, departmentID);
			rs = pstmt.executeQuery();
			if(rs.isAfterLast() == rs.isBeforeFirst()){
				request.setAttribute("msg", "暂无实验信息!");
				request.getRequestDispatcher("error.jsp").forward(request, response);
			}else{
				Sheet sheet;
				Sheet sheet_o = work.getSheetAt(0);
				int sheet_num = 1; //总共有几个表
				int sheet_size = 5;  //一张表存放几个实验室
				sql = "select id from _laboratory where departmentID = ?";
				pstmt = DatabaseUtil.prepareStmt(conn, sql);
				pstmt.setInt(1, departmentID);
				rs3 = pstmt.executeQuery();
				rs3.last();
				sheet_num = (rs3.getRow()+sheet_size-1)/sheet_size;
				//对应周几加的权数
				Map<String, Integer> weekdayMaps = new HashMap<String, Integer>();
				weekdayMaps.put("周一", 0);
				weekdayMaps.put("周二", 1);
				weekdayMaps.put("周三", 2);
				weekdayMaps.put("周四", 3);
				weekdayMaps.put("周五", 4);
				Map<Integer, Integer> timeMaps = new HashMap<Integer, Integer>();
				timeMaps.put(1, 0);
				timeMaps.put(2, 0);
				timeMaps.put(3, 1);
				timeMaps.put(4, 1);
				timeMaps.put(5, 2);
				timeMaps.put(6, 2);
				timeMaps.put(7, 3);
				timeMaps.put(8, 3);
				timeMaps.put(9, 4);
				timeMaps.put(10, 4);
				//对应年级-excel列数的Map
				ArrayList<Map<String, Integer>> rowMaps = new ArrayList<Map<String, Integer>>();
				Map<String, Integer> sheetMaps = new HashMap<String, Integer>();
				//保存cellStyle
				CellStyle titleStyle = work.getSheetAt(0).getRow(1).getCell(2).getCellStyle();
				CellStyle contentStyle = work.getSheetAt(0).getRow(2).getCell(2).getCellStyle();
				//...
				ArrayList<ArrayList<Integer>> heightMaps = new ArrayList<ArrayList<Integer>>();
				//创建所有excel列
				for(int gi=0; gi<sheet_num; gi++){
					if(gi == 0){
						sheet = work.getSheetAt(gi);
						work.setSheetName(0, "1");
					}else{
						sheet = work.createSheet((gi+1)+"");
						for(int i=0; i<27; i++){
							row = sheet.createRow(i);
							Row row_o = sheet_o.getRow(i);
							for(int j=0; j<3; j++){
								cell = row.createCell(j);
								cell.setCellStyle(row_o.getCell(j).getCellStyle());
								cell.setCellValue(row_o.getCell(j).getStringCellValue());
							}
						}
						sheet.setColumnWidth(0, 4*256);
						sheet.setColumnWidth(1, 5*256);
					}
					sql = "select * from _laboratory where departmentID = ? limit ?, ?";
					pstmt = DatabaseUtil.prepareStmt(conn, sql);
					pstmt.setInt(1, departmentID);
					pstmt.setInt(2, gi*sheet_size);
					pstmt.setInt(3, sheet_size);
					rs2 = pstmt.executeQuery();
					int colNumer = 2;
					boolean isFirst = true;
					Map<String, Integer> rowMap = new HashMap<String, Integer>();
					rowMaps.add(rowMap);
					while(rs2.next()){
						rowMap.put(rs2.getString("name"), colNumer);
						sheetMaps.put(rs2.getString("name"), gi);
						if(isFirst){
							row = sheet.getRow(1);
							cell = row.getCell(2);
							cell.setCellValue(rs2.getString("name"));
							sheet.setColumnWidth(colNumer, 25*256);
							isFirst = false;
						}else{
							//创建colNumer列
							for(int i=1; i<27; i++){
								cell = sheet.getRow(i).createCell(colNumer);
								cell.setCellStyle(contentStyle);
							}
							sheet.setColumnWidth(colNumer, 25*256);
							cell = sheet.getRow(1).getCell(colNumer);
							cell.setCellStyle(titleStyle);
							cell.setCellValue(rs2.getString("name"));
						}
						colNumer++;
					}
					int grade_count = rowMap.size();
					ArrayList<Integer> tt_list = new ArrayList<Integer>(grade_count*25);
					for(int v=0; v<grade_count*25; v++){	
						tt_list.add(0);
					}
					heightMaps.add(tt_list);
					sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, sheet_size+1));
					row = sheet.getRow(0);
					cell = row.getCell(0);
					row.setHeightInPoints(22.5f);
					//韩山师范学院2013-2014学年第1学期计算机系实验室课表
					cell.setCellValue("韩山师范学院" + year + term + de_name + "实验室课表");
					CellStyle normalStyle = work.getSheetAt(0).getRow(0).getCell(0).getCellStyle();
					normalStyle.setBorderBottom((short) 1);
					normalStyle.setBorderLeft((short) 1);
					normalStyle.setBorderRight((short) 1);
					normalStyle.setBorderTop((short) 1);
					normalStyle.setAlignment(CellStyle.ALIGN_CENTER);
					cell.setCellStyle(normalStyle);
					for(int i=1; i<(2+sheet_size);i++){
						cell = row.createCell(i);
						cell.setCellStyle(normalStyle);
					}
					//设置第一行高度
					sheet.getRow(1).setHeightInPoints(40.0f);
					for(int wi=2; wi<27; wi+=5){
						sheet.addMergedRegion(new CellRangeAddress(wi, wi+4, 0, 0));
					}
				}
				while(rs.next()){
					String time[] = rs.getString("timeInfo").split("@");
					for(String tss : time){
						String week[] = tss.split(",");
						String week_string = "";
						int week_length = week.length;
						if(week[0].equals("0")){
							week_string += "第" + week[1] + "周 - 第" + week[2] + "周(" + week[3] + ")";
						}else{ 
							week_string += "第";
							for(int ij=1; ij<week_length-4; ij++){
								week_string += week[ij];
								if(ij != week_length - 5){
									week_string += "、";	
								}
							}
							week_string += "周";
						}
						String teacher[] = rs.getString("teacherInfo").split(",");
						String teacher_string = te_map.get(teacher[1]);
						for(int j=2; j<teacher.length-1; j++){
							teacher_string += " " + te_map.get(teacher[++j]);
						}
						String classInfo[] = rs.getString("classInfo").split("@");
						String class_string = classInfo[0];
						for(int j=2; j<classInfo.length-1; j++){
							class_string += " " + classInfo[j++];
						}
						String syllabus_string = "";
						syllabus_string += rs.getString("co_name") + "\r\n" + 
										 class_string + "\r\n" +
										 teacher_string + "\r\n" + 
										 week_string;
						int sheetNum = sheetMaps.get(rs.getString("l_name"));
						//第几行
						int rr_num = (weekdayMaps.get(week[week_length-3])*5) + timeMaps.get(Integer.parseInt(week[week_length-2])); 
						//第几列
						int cc_num = (rowMaps.get(sheetNum).get(rs.getString("l_name")));
						sheet = work.getSheetAt(sheetNum);
						// ....
						row = sheet.getRow(2 + rr_num);
						cell = row.getCell(cc_num);
						int index_c = (rowMaps.get(sheetNum).size())*(rr_num) + cc_num - 2;
						int c_h = heightMaps.get(sheetNum).get(index_c);
						if(c_h == 0){
							cell.setCellValue(syllabus_string);
						}else{
							cell.setCellValue(cell.getStringCellValue()+"\r\n"+syllabus_string);
						}
						heightMaps.get(sheetNum).set(index_c, c_h+1);
					}
				}
				//处理每行行高
				for(int gi=0; gi<sheet_num; gi++){
					sheet = work.getSheetAt(gi);
					for(int i=2; i<27; i++){
						int max_h = 0;
						int grade_c = rowMaps.get(gi).size();
						for(int j=0; j<grade_c; j++){
							int hh = heightMaps.get(gi).get((i-2)*grade_c+j);
							if(hh > max_h){
								max_h = hh;
							}
						}
						if(max_h == 0){
							sheet.getRow(i).setHeightInPoints(20.0f);
						}else{
							sheet.getRow(i).setHeightInPoints(55.0f*max_h);
						}
					}
				}
				ByteArrayOutputStream ba = new ByteArrayOutputStream();
				work.write(ba);
				String fileName = year + term + de_name + "实验室课表"; // 2013-2014学年度第一学期计算机系实验室课表
				response.setHeader("Expires", "0");
				response.setHeader("Cache-Control", "must-revalidate, post-check=0, pre-check=0");
				response.setHeader("Pragma", "public");
				response.setContentType("application/msexcel");
				response.setHeader("Content-disposition","attachment; filename=" + new String((fileName).getBytes("gbk"),"iso8859-1") + ".xls" );  
				/* 如果想出来让IE提示你是打开还是保存的对话框，加上下面这句就可以了 */
				//response.setHeader("Content-disposition", "attachment; filename=schedule.pdf");
				response.setContentLength(ba.size());
				try{
					ServletOutputStream out = response.getOutputStream();
					ba.writeTo(out);
					out.flush();
					out.close();
					ba.close();
				}catch (IOException ex) {
					ex.printStackTrace();
				}finally {
					if (ba != null) ba.close();
				}
			}
		} catch (FileNotFoundException e) {
			System.out.println("文件路径错误");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("文件输入流错误");
			e.printStackTrace();
		} catch (Exception e){
			e.printStackTrace();
			request.setAttribute("msg", "导出数据时发生错误!");
			request.getRequestDispatcher("error.jsp").forward(request, response);
		}finally{
			DatabaseUtil.close(rs);
			DatabaseUtil.close(rs2);
			DatabaseUtil.close(pstmt);
			DatabaseUtil.close(conn);
		}
	}
	
	public void index_list_obl(HttpServletRequest request, HttpServletResponse response)
	throws ServletException, IOException{
		response.setContentType("text/html;charset=utf-8");
		response.setCharacterEncoding("utf-8");
		PrintWriter out = response.getWriter(); 
		String ex_string = ExperimentDAO.index_list_obl(Integer.parseInt(request.getParameter("laboratoryID")), java.net.URLDecoder.decode(request.getParameter("currenTerm"),"UTF-8"), Integer.parseInt(request.getParameter("pageNo")), Integer.parseInt(request.getParameter("pageSize")));
		out.println(ex_string);
		out.flush();
		out.close(); 
	}
}
