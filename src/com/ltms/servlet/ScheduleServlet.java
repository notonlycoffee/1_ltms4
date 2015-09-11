package com.ltms.servlet;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.Signature;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.pdf.AcroFields;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfCopyFields;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.ltms.dao.ClassDAO;
import com.ltms.dao.CourseDAO;
import com.ltms.dao.DepartmentDAO;
import com.ltms.dao.ExItemDAO;
import com.ltms.dao.ExperimentDAO;
import com.ltms.dao.LaboratoryDAO;
import com.ltms.dao.ScheduleDAO;
import com.ltms.dao.TeacherDAO;
import com.ltms.model.Admin;
import com.ltms.model.ExItem;
import com.ltms.model.Experiment;
import com.ltms.model.Schedule;
import com.ltms.model.Teacher;
import com.ltms.model._Class;
import com.ltms.util.DatabaseUtil;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ScheduleServlet extends HttpServlet {
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
		//else if(method.equals("删除")) delete(request, response);
		else if(method.equals("edit")) edit(request, response);
		else if(method.equals("modify")) modify(request, response);
		//else if(method.equals("search")) search(request, response);
		else if(method.equals("update_from_existence")) update_from_existence(request, response);
		else if(method.equals("export_pdf")) export_pdf(request, response);
		else return;
	}
	
	public void list(HttpServletRequest request, HttpServletResponse response)
	throws ServletException, IOException{
		
		String currenTerm_ = (String) request.getSession().getAttribute("currenTerm");
		////////
		if("".equals(currenTerm_) || currenTerm_.trim().length() == 0){
			
			request.setAttribute("msg", "请先选择学年学期");
			request.getRequestDispatcher("showMsg.jsp").forward(request, response);
			return ;
		}
		////////
		//System.out.println("cu is " + currenTerm_);
		Teacher teacher = ((Teacher)request.getSession().getAttribute("teacher"));
		String teacherID = teacher.getId();  
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
		ArrayList<Schedule> scheduleList = new ArrayList<Schedule>();
		Map<String, String> classNameMap = new HashMap<String, String>();
		Map<Integer, String> courseNameMap = new HashMap<Integer, String>();
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
			String countSQL = "select count(*) from _schedule where teacherID = " + teacherID;
			String sql = "select * from _schedule where teacherID = " + teacherID + " and term like ?";
			rsCount = DatabaseUtil.executeQuery(stmtCount, countSQL);
			int totalRecords;
			rsCount.next();
			totalRecords = rsCount.getInt(1);
			totalPages = (totalRecords + pageSize - 1)/pageSize;
			if(totalPages == 0){
				totalPages = 1;
			}
			if(pageNo > totalPages) pageNo = totalPages;
			int startPos = (pageNo - 1) * pageSize; 
			sql +=" limit ?, ?";
			pstmt = DatabaseUtil.prepareStmt(conn, sql);
			pstmt.setString(1, "%" + currenTerm_ + "%");
			pstmt.setInt(2, startPos);
			pstmt.setInt(3, pageSize);
			rs = pstmt.executeQuery();
			while(rs.next()) {
				Schedule t = new Schedule();
				ScheduleDAO.initFromRS(rs, t);
				scheduleList.add(t);
			}	
			int listLen = scheduleList.size();
			//取对应班级ID - 班级名 Map
			sql = "select id, name from _class where id in (?";
			for(int i = 0; i< listLen; i++){
				sql += ", ?";
			}
			sql += ")";
			pstmt = DatabaseUtil.prepareStmt(conn, sql);
			pstmt.setString(1, "0");
			for(int i = 0; i< listLen; i++){
				pstmt.setString(i+2, scheduleList.get(i).getClassID());
			}
			rs = pstmt.executeQuery();
			while(rs.next()){
				classNameMap.put(rs.getString("id"), rs.getString("name"));
			}
			
			//取对应课程ID - 课程名 Map
			sql = "select e.id, c.name from _course c join _experiment e where e.courseID = c.id and e.id in(?";
			for(int i = 0; i< listLen; i++){
				sql += ", ?";
			}
			sql += ")";
			pstmt = DatabaseUtil.prepareStmt(conn, sql);
			pstmt.setInt(1, 0);
			for(int i = 0; i< listLen; i++){
				pstmt.setInt(i+2, scheduleList.get(i).getExperimentID());
			}
			rs = pstmt.executeQuery();
			while(rs.next()){
				courseNameMap.put(rs.getInt("id"), rs.getString("name"));
			}
			
//System.out.println(courseNameMap.toString());
//System.out.println(classNameMap.toString());
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
			request.setAttribute("scheduleList", scheduleList);
			request.setAttribute("courseNameMap", courseNameMap);
			request.setAttribute("classNameMap", classNameMap);
			request.getRequestDispatcher("listSchedule.jsp").forward(request, response);
		}
	}

	public void delete(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException{
		String id = request.getParameter("id");
		Connection conn = DatabaseUtil.getConn();
		String sql = "delete from _schedule where id = ?";
		PreparedStatement pstmt = DatabaseUtil.prepareStmt(conn, sql);
		try {
			pstmt.setString(1, id);
			pstmt.executeUpdate();	
			request.setAttribute("msg", "删除成功");
			request.getRequestDispatcher("listSchedule.jsp").forward(request, response);
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
	
	//读出一个进度表  转到editSchedule.jsp
	public void edit(HttpServletRequest request, HttpServletResponse response)
	throws ServletException, IOException{
		int id = Integer.parseInt(request.getParameter("id"));
		Schedule schedule = ScheduleDAO.load(id);
		if(schedule == null){
			request.setAttribute("msg", "编辑失败!");
			request.getRequestDispatcher("error.jsp").forward(request, response);
			return;
		}
		int experimentID = schedule.getExperimentID();
		Experiment e = ExperimentDAO.load(experimentID);
		Set<String> classid_set = new HashSet<String>();
		Set<String> classname_set = new HashSet<String>();
		
		String[] classID = (schedule.getClassID()).split("@");
		int classId_ ;
    	if(schedule.getClassID().endsWith("theendofthestring")){
    		classId_ = (classID.length)-1;
    	}else{
    		classId_ = classID.length;
    	}
//		System.out.println("schedule.getClassID() is " + schedule.getClassID());
//		System.out.println("(classID.length)-1 is " + ((classID.length)-1));
		for(int i=0; i<classId_; i++){
			classid_set.add(classID[i]);
		}
		
		for(int i=0; i<classId_; i++){
			
			classname_set.add((ClassDAO.load(classID[i])).getName().trim());
			//System.out.println("zy is " + (ClassDAO.load(classID[i])).getName() + "**");
		}
		String teacherID = schedule.getTeacherID();
		//取对应实验进度项目
		ArrayList<ExItem> exItemList = ExItemDAO.list(id);
		//取实验进度type
		Map<Integer, String> typeMap = ExItemDAO.getType(id);
		
		request.setAttribute("experiment", e);
		request.setAttribute("exItemList", exItemList);
		request.setAttribute("laboratoryName", LaboratoryDAO.getName(e.getLaboratoryID()));
		request.setAttribute("requirement", e.getRequirement());
		request.setAttribute("department", DepartmentDAO.getName(e.getDepartmentID()));
		request.setAttribute("type", e.getType());
		request.setAttribute("term", e.getTerm());
		request.setAttribute("class_id", classid_set);///////////
		request.setAttribute("class_name", classname_set);///////////
		request.setAttribute("teacher", TeacherDAO.load(teacherID));
		request.setAttribute("schedule", schedule);			
		request.setAttribute("id", id);
		request.setAttribute("typeMap", typeMap);
		request.getRequestDispatcher("editSchedule.jsp").forward(request, response);
	}
	private boolean signature = true;
	
	private void check(String type){
		if("综合".equals(type)){
			signature = false;
		}
	}
	public void modify(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException{
		int id = Integer.parseInt(request.getParameter("id"));
		String teacherID = ScheduleDAO.getTeacherID(id);  // 不是这个老师不能修改
		Teacher teacher = ((Teacher)request.getSession().getAttribute("teacher"));
		if(teacher == null || !teacherID.equals(teacher.getId())){
			request.setAttribute("msg", "您无权限修改该进度表!");
			request.getRequestDispatcher("error.jsp").forward(request, response);
			return;
		}
		String student_count = request.getParameter("student_count");
		try {
			Integer.parseInt(student_count);
		} catch (NumberFormatException e1) {
			// TODO Auto-generated catch block
			request.setAttribute("msg", "人数必须是数字");
			request.getRequestDispatcher("ScheduleServlet?method=edit&id="+id).forward(request, response);
		}
		
		ArrayList<ExItem> exItemList = ExItemDAO.list(id);
		String groupNum = request.getParameter("groupNum");
		String purpose = request.getParameter("purpose");
		String material = request.getParameter("material");
		String tGrade = request.getParameter("tGrade");
		String tTerm = request.getParameter("tTerm");
		String demand = request.getParameter("demand");
		String techWeek = request.getParameter("techWeek");
		String weekTime = request.getParameter("weekTime");
		String totalTime = request.getParameter("totalTime");
		String theoTime = request.getParameter("theoTime");
		String exTime = request.getParameter("exTime");
		String week1 = request.getParameter("week1");
		String item1 = request.getParameter("item1");
		String type1 = request.getParameter("type1");
		check(type1);
		String comment1 = request.getParameter("comment1");
		String week2 = request.getParameter("week2");
		String item2 = request.getParameter("item2");
		String type2 = request.getParameter("type2");
		check(type2);
		String comment2 = request.getParameter("comment2");
		String week3 = request.getParameter("week3");
		String item3 = request.getParameter("item3");
		String type3 = request.getParameter("type3");
		check(type3);
		String comment3 = request.getParameter("comment3");
		String week4 = request.getParameter("week4");
		String item4 = request.getParameter("item4");
		String type4 = request.getParameter("type4");
		check(type4);
		String comment4 = request.getParameter("comment4");
		String week5 = request.getParameter("week5");
		String item5 = request.getParameter("item5");
		String type5 = request.getParameter("type5");
		check(type5);
		String comment5 = request.getParameter("comment5");
		String week6 = request.getParameter("week6");
		String item6 = request.getParameter("item6");
		String type6 = request.getParameter("type6");
		check(type6);
		String comment6 = request.getParameter("comment6");
		String week7 = request.getParameter("week7");
		String item7 = request.getParameter("item7");
		String type7 = request.getParameter("type7");
		check(type7);
		String comment7 = request.getParameter("comment7");
		String week8 = request.getParameter("week8");
		String item8 = request.getParameter("item8");
		String type8 = request.getParameter("type8");
		check(type8);
		String comment8 = request.getParameter("comment8");
		String week9 = request.getParameter("week9");
		String item9 = request.getParameter("item9");
		String type9 = request.getParameter("type9");
		check(type9);
		String comment9 = request.getParameter("comment9");
		String week10 = request.getParameter("week10");
		String item10 = request.getParameter("item10");
		String type10 = request.getParameter("type10");
		check(type10);
		String comment10 = request.getParameter("comment10");
		String week11 = request.getParameter("week11");
		String item11 = request.getParameter("item11");
		String type11 = request.getParameter("type11");
		check(type11);
		String comment11 = request.getParameter("comment11");
		String week12 = request.getParameter("week12");
		String item12 = request.getParameter("item12");
		String type12 = request.getParameter("type12");
		check(type12);
		String comment12 = request.getParameter("comment12");
		String week13 = request.getParameter("week13");
		String item13 = request.getParameter("item13");
		String type13 = request.getParameter("type13");
		check(type13);
		String comment13 = request.getParameter("comment13");
		String week14 = request.getParameter("week14");
		String item14 = request.getParameter("item14");
		String type14 = request.getParameter("type14");
		check(type14);
		String comment14 = request.getParameter("comment14");
		String week15 = request.getParameter("week15");
		String item15 = request.getParameter("item15");
		String type15 = request.getParameter("type15");
		check(type15);
		String comment15 = request.getParameter("comment15");
		String week16 = request.getParameter("week16");
		String item16 = request.getParameter("item16");
		String type16 = request.getParameter("type16");
		check(type16);
		String comment16 = request.getParameter("comment16");
		String week17 = request.getParameter("week17");
		String item17 = request.getParameter("item17");
		String type17 = request.getParameter("type17");
		check(type17);
		String comment17 = request.getParameter("comment17");
		String week18 = request.getParameter("week18");
		String item18 = request.getParameter("item18");
		String type18 = request.getParameter("type18");
		check(type18);
		String comment18 = request.getParameter("comment18");
		String week19 = request.getParameter("week19");
		String item19 = request.getParameter("item19");
		String type19 = request.getParameter("type19");
		check(type19);
		String comment19 = request.getParameter("comment19");
		String week20 = request.getParameter("week20");
		String item20 = request.getParameter("item20");
		String type20 = request.getParameter("type20");
		check(type20);
		String comment20 = request.getParameter("comment20");

		if(signature){
			request.setAttribute("msg", "至少要有一个实验是综合类型的!");
			request.getRequestDispatcher("error.jsp").forward(request, response);
			signature = true;
			return;
		}
		
		Connection conn = DatabaseUtil.getConn();
		String sql = "update _schedule set groupNum = ?," +
				" purpose = ?, material = ?, tGrade = ?, tTerm = ?," +
				" demand = ?, techWeek = ?, weekTime = ?, totalTime = ?, theoTime = ?, exTime = ?" +
				" where id = ?";
		PreparedStatement pstmt = null;
		try {
			conn.setAutoCommit(false);  //事务开始
			pstmt = DatabaseUtil.prepareStmt(conn, sql);					
			int i = 1;
			pstmt.setString(i++, groupNum);
			pstmt.setString(i++, purpose);
			pstmt.setString(i++, material);
			pstmt.setString(i++, tGrade);
			pstmt.setString(i++, tTerm);
			pstmt.setString(i++, demand);
			pstmt.setString(i++, techWeek);
			pstmt.setString(i++, weekTime);
			pstmt.setString(i++, totalTime);
			pstmt.setString(i++, theoTime);
			pstmt.setString(i++, exTime);
			pstmt.setInt(i++, id);
			pstmt.executeUpdate();	
			
			sql = "update _exItem set week = ?, itemName = ?, comment = ? ,type = ? where id = ?";
			pstmt = DatabaseUtil.prepareStmt(conn, sql);	
			i = 1;
			pstmt.setString(i++, week1);
			pstmt.setString(i++, item1);
			pstmt.setString(i++, comment1);
			pstmt.setString(i++, type1);
			pstmt.setInt(i++, exItemList.get(0).getId());
			pstmt.executeUpdate();
			i = 1;
			pstmt.setString(i++, week2);
			pstmt.setString(i++, item2);
			pstmt.setString(i++, comment2);
			pstmt.setString(i++, type2);
			pstmt.setInt(i++, exItemList.get(1).getId());
			pstmt.executeUpdate();
			i = 1;
			pstmt.setString(i++, week3);
			pstmt.setString(i++, item3);
			pstmt.setString(i++, comment3);
			pstmt.setString(i++, type3);
			pstmt.setInt(i++, exItemList.get(2).getId());
			pstmt.executeUpdate();
			i = 1;
			pstmt.setString(i++, week4);
			pstmt.setString(i++, item4);
			pstmt.setString(i++, comment4);
			pstmt.setString(i++, type4);
			pstmt.setInt(i++, exItemList.get(3).getId());
			pstmt.executeUpdate();
			i = 1;
			pstmt.setString(i++, week5);
			pstmt.setString(i++, item5);
			pstmt.setString(i++, comment5);
			pstmt.setString(i++, type5);
			pstmt.setInt(i++, exItemList.get(4).getId());
			pstmt.executeUpdate();
			i = 1;
			pstmt.setString(i++, week6);
			pstmt.setString(i++, item6);
			pstmt.setString(i++, comment6);
			pstmt.setString(i++, type6);
			pstmt.setInt(i++, exItemList.get(5).getId());
			pstmt.executeUpdate();
			i = 1;
			pstmt.setString(i++, week7);
			pstmt.setString(i++, item7);
			pstmt.setString(i++, comment7);
			pstmt.setString(i++, type7);
			pstmt.setInt(i++, exItemList.get(6).getId());
			pstmt.executeUpdate();
			i = 1;
			pstmt.setString(i++, week8);
			pstmt.setString(i++, item8);
			pstmt.setString(i++, comment8);
			pstmt.setString(i++, type8);
			pstmt.setInt(i++, exItemList.get(7).getId());
			pstmt.executeUpdate();
			i = 1;
			pstmt.setString(i++, week9);
			pstmt.setString(i++, item9);
			pstmt.setString(i++, comment9);
			pstmt.setString(i++, type9);
			pstmt.setInt(i++, exItemList.get(8).getId());
			pstmt.executeUpdate();
			i = 1;
			pstmt.setString(i++, week10);
			pstmt.setString(i++, item10);
			pstmt.setString(i++, comment10);
			pstmt.setString(i++, type10);
			pstmt.setInt(i++, exItemList.get(9).getId());
			pstmt.executeUpdate();
			
			if(exItemList.size() > 11){
				i = 1;
				pstmt.setString(i++, week11);
				pstmt.setString(i++, item11);
				pstmt.setString(i++, comment11);
				pstmt.setString(i++, type11);
				pstmt.setInt(i++, exItemList.get(10).getId());
				pstmt.executeUpdate();
				i = 1;
				pstmt.setString(i++, week12);
				pstmt.setString(i++, item12);
				pstmt.setString(i++, comment12);
				pstmt.setString(i++, type12);
				pstmt.setInt(i++, exItemList.get(11).getId());
				pstmt.executeUpdate();
				i = 1;
				pstmt.setString(i++, week13);
				pstmt.setString(i++, item13);
				pstmt.setString(i++, comment13);
				pstmt.setString(i++, type13);
				pstmt.setInt(i++, exItemList.get(12).getId());
				pstmt.executeUpdate();
				i = 1;
				pstmt.setString(i++, week14);
				pstmt.setString(i++, item14);
				pstmt.setString(i++, comment14);
				pstmt.setString(i++, type14);
				pstmt.setInt(i++, exItemList.get(13).getId());
				pstmt.executeUpdate();
				i = 1;
				pstmt.setString(i++, week15);
				pstmt.setString(i++, item15);
				pstmt.setString(i++, comment15);
				pstmt.setString(i++, type15);
				pstmt.setInt(i++, exItemList.get(14).getId());
				pstmt.executeUpdate();
				i = 1;
				pstmt.setString(i++, week16);
				pstmt.setString(i++, item16);
				pstmt.setString(i++, comment16);
				pstmt.setString(i++, type16);
				pstmt.setInt(i++, exItemList.get(15).getId());
				pstmt.executeUpdate();
				i = 1;
				pstmt.setString(i++, week17);
				pstmt.setString(i++, item17);
				pstmt.setString(i++, comment17);
				pstmt.setString(i++, type17);
				pstmt.setInt(i++, exItemList.get(16).getId());
				pstmt.executeUpdate();
				i = 1;
				pstmt.setString(i++, week18);
				pstmt.setString(i++, item18);
				pstmt.setString(i++, comment18);
				pstmt.setString(i++, type18);
				pstmt.setInt(i++, exItemList.get(17).getId());
				pstmt.executeUpdate();
				i = 1;
				pstmt.setString(i++, week19);
				pstmt.setString(i++, item19);
				pstmt.setString(i++, comment19);
				pstmt.setString(i++, type19);
				pstmt.setInt(i++, exItemList.get(18).getId());
				pstmt.executeUpdate();
				i = 1;
				pstmt.setString(i++, week20);
				pstmt.setString(i++, item20);
				pstmt.setString(i++, comment20);
				pstmt.setString(i++, type20);
				pstmt.setInt(i++, exItemList.get(19).getId());
				pstmt.executeUpdate();
			}else{
//				sql = "update _exItem set week = ?, itemName = ?, comment = ? ,type = ? where id = ?";
				sql = "insert into _exItem(scheduleID, week, itemName, comment, laboratoryID, type) values(?,?,?,?,?,?)";
				pstmt = DatabaseUtil.prepareStmt(conn, sql);	
				i = 1;
				pstmt.setInt(i++, exItemList.get(0).getScheduleID());
				pstmt.setString(i++, week11);
				pstmt.setString(i++, item11);
				pstmt.setString(i++, comment11);
				pstmt.setInt(i++, exItemList.get(0).getLaboratoryID());
				pstmt.setString(i++, type11);
				pstmt.executeUpdate();
				pstmt = DatabaseUtil.prepareStmt(conn, sql);	
				i = 1;
				pstmt.setInt(i++, exItemList.get(0).getScheduleID());
				pstmt.setString(i++, week12);
				pstmt.setString(i++, item12);
				pstmt.setString(i++, comment12);
				pstmt.setInt(i++, exItemList.get(0).getLaboratoryID());
				pstmt.setString(i++, type12);
				pstmt.executeUpdate();
				pstmt = DatabaseUtil.prepareStmt(conn, sql);	
				i = 1;
				pstmt.setInt(i++, exItemList.get(0).getScheduleID());
				pstmt.setString(i++, week13);
				pstmt.setString(i++, item13);
				pstmt.setString(i++, comment13);
				pstmt.setInt(i++, exItemList.get(0).getLaboratoryID());
				pstmt.setString(i++, type13);
				pstmt.executeUpdate();
				pstmt = DatabaseUtil.prepareStmt(conn, sql);	
				i = 1;
				pstmt.setInt(i++, exItemList.get(0).getScheduleID());
				pstmt.setString(i++, week14);
				pstmt.setString(i++, item14);
				pstmt.setString(i++, comment14);
				pstmt.setInt(i++, exItemList.get(0).getLaboratoryID());
				pstmt.setString(i++, type14);
				pstmt.executeUpdate();
				pstmt = DatabaseUtil.prepareStmt(conn, sql);	
				i = 1;
				pstmt.setInt(i++, exItemList.get(0).getScheduleID());
				pstmt.setString(i++, week15);
				pstmt.setString(i++, item15);
				pstmt.setString(i++, comment15);
				pstmt.setInt(i++, exItemList.get(0).getLaboratoryID());
				pstmt.setString(i++, type15);
				pstmt.executeUpdate();
				pstmt = DatabaseUtil.prepareStmt(conn, sql);	
				i = 1;
				pstmt.setInt(i++, exItemList.get(0).getScheduleID());
				pstmt.setString(i++, week16);
				pstmt.setString(i++, item16);
				pstmt.setString(i++, comment16);
				pstmt.setInt(i++, exItemList.get(0).getLaboratoryID());
				pstmt.setString(i++, type16);
				pstmt.executeUpdate();
				pstmt = DatabaseUtil.prepareStmt(conn, sql);	
				i = 1;
				pstmt.setInt(i++, exItemList.get(0).getScheduleID());
				pstmt.setString(i++, week17);
				pstmt.setString(i++, item17);
				pstmt.setString(i++, comment17);
				pstmt.setInt(i++, exItemList.get(0).getLaboratoryID());
				pstmt.setString(i++, type17);
				pstmt.executeUpdate();
				pstmt = DatabaseUtil.prepareStmt(conn, sql);	
				i = 1;
				pstmt.setInt(i++, exItemList.get(0).getScheduleID());
				pstmt.setString(i++, week18);
				pstmt.setString(i++, item18);
				pstmt.setString(i++, comment18);
				pstmt.setInt(i++, exItemList.get(0).getLaboratoryID());
				pstmt.setString(i++, type18);
				pstmt.executeUpdate();
				pstmt = DatabaseUtil.prepareStmt(conn, sql);	
				i = 1;
				pstmt.setInt(i++, exItemList.get(0).getScheduleID());
				pstmt.setString(i++, week19);
				pstmt.setString(i++, item19);
				pstmt.setString(i++, comment19);
				pstmt.setInt(i++, exItemList.get(0).getLaboratoryID());
				pstmt.setString(i++, type19);
				pstmt.executeUpdate();
				pstmt = DatabaseUtil.prepareStmt(conn, sql);	
				i = 1;
				pstmt.setInt(i++, exItemList.get(0).getScheduleID());
				pstmt.setString(i++, week20);
				pstmt.setString(i++, item20);
				pstmt.setString(i++, comment20);
				pstmt.setInt(i++, exItemList.get(0).getLaboratoryID());
				pstmt.setString(i++, type20);
				pstmt.executeUpdate();
			}
			
			//这里是要修改number   
			sql = "update number set count = ? where schedule_id = ?";
			pstmt = DatabaseUtil.prepareStmt(conn, sql);	
			i = 1;
			pstmt.setInt(i++, Integer.parseInt(student_count));
			pstmt.setInt(i++, id);
			pstmt.executeUpdate();
			
		
			conn.commit();   //事务提交
			conn.setAutoCommit(true);  //设置成自动提交模式
			request.setAttribute("id", id);
			request.setAttribute("msg", "修改成功");
			request.getRequestDispatcher("ScheduleServlet?method=edit&id=" + id).forward(request, response);
		}catch (SQLException e) {
			e.printStackTrace();
			try {
				conn.rollback();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			request.setAttribute("msg", "修改失败!");
			request.getRequestDispatcher("error.jsp").forward(request, response);
			return;
		}finally{
			DatabaseUtil.close(pstmt);
			DatabaseUtil.close(conn);
		}	
	}
	
	public void update_from_existence(HttpServletRequest request, HttpServletResponse response)
	throws ServletException, IOException{
	int id = Integer.parseInt(request.getParameter("id"));
	int exist_id = Integer.parseInt(request.getParameter("exist_id"));
	Schedule exist_schedule = ScheduleDAO.load(exist_id);
	ArrayList<ExItem> exItemList = ExItemDAO.list(id);
	ArrayList<ExItem> exist_exItemList = ExItemDAO.list(exist_id);
	Connection conn = DatabaseUtil.getConn();
	String sql = "update _schedule set groupNum = ?," +
			" purpose = ?, material = ?, tGrade = ?, tTerm = ?," +
			" demand = ?, techWeek = ?, weekTime = ?, totalTime = ?, theoTime = ?, exTime = ?" +
			" where id = ?";
		PreparedStatement pstmt = null;
		try {
			conn.setAutoCommit(false);  //事务开始
			pstmt = DatabaseUtil.prepareStmt(conn, sql);					
			int i = 1;
			pstmt.setInt(i++, exist_schedule.getGroupNum());
			pstmt.setString(i++, exist_schedule.getPurpose());
			pstmt.setString(i++, exist_schedule.getMaterial());
			pstmt.setString(i++, exist_schedule.gettGrade());
			pstmt.setString(i++, exist_schedule.gettTerm());
			pstmt.setString(i++, exist_schedule.getDemand());
			pstmt.setInt(i++, exist_schedule.getTechWeek());
			pstmt.setInt(i++, exist_schedule.getWeekTime());
			pstmt.setInt(i++, exist_schedule.getTotalTime());
			pstmt.setInt(i++, exist_schedule.getTheoTime());
			pstmt.setInt(i++, exist_schedule.getExTime());
			pstmt.setInt(i++, id);
			pstmt.executeUpdate();	
			
			sql = "update _exItem set week = ?, itemName = ?, comment = ? where id = ?";
			pstmt = DatabaseUtil.prepareStmt(conn, sql);	
			i = 1;
			pstmt.setString(i++, exist_exItemList.get(0).getWeek());
			pstmt.setString(i++, exist_exItemList.get(0).getItemName());
			pstmt.setString(i++, exist_exItemList.get(0).getComment());
			pstmt.setInt(i++, exItemList.get(0).getId());
			pstmt.executeUpdate();
			i = 1;
			pstmt.setString(i++, exist_exItemList.get(1).getWeek());
			pstmt.setString(i++, exist_exItemList.get(1).getItemName());
			pstmt.setString(i++, exist_exItemList.get(1).getComment());
			pstmt.setInt(i++, exItemList.get(1).getId());
			pstmt.executeUpdate();
			i = 1;
			pstmt.setString(i++, exist_exItemList.get(2).getWeek());
			pstmt.setString(i++, exist_exItemList.get(2).getItemName());
			pstmt.setString(i++, exist_exItemList.get(2).getComment());
			pstmt.setInt(i++, exItemList.get(2).getId());
			pstmt.executeUpdate();
			i = 1;
			pstmt.setString(i++, exist_exItemList.get(3).getWeek());
			pstmt.setString(i++, exist_exItemList.get(3).getItemName());
			pstmt.setString(i++, exist_exItemList.get(3).getComment());
			pstmt.setInt(i++, exItemList.get(3).getId());
			pstmt.executeUpdate();
			i = 1;
			pstmt.setString(i++, exist_exItemList.get(4).getWeek());
			pstmt.setString(i++, exist_exItemList.get(4).getItemName());
			pstmt.setString(i++, exist_exItemList.get(4).getComment());
			pstmt.setInt(i++, exItemList.get(4).getId());
			pstmt.executeUpdate();
			i = 1;
			pstmt.setString(i++, exist_exItemList.get(5).getWeek());
			pstmt.setString(i++, exist_exItemList.get(5).getItemName());
			pstmt.setString(i++, exist_exItemList.get(5).getComment());
			pstmt.setInt(i++, exItemList.get(5).getId());
			pstmt.executeUpdate();
			i = 1;
			pstmt.setString(i++, exist_exItemList.get(6).getWeek());
			pstmt.setString(i++, exist_exItemList.get(6).getItemName());
			pstmt.setString(i++, exist_exItemList.get(6).getComment());
			pstmt.setInt(i++, exItemList.get(6).getId());
			pstmt.executeUpdate();
			i = 1;
			pstmt.setString(i++, exist_exItemList.get(7).getWeek());
			pstmt.setString(i++, exist_exItemList.get(7).getItemName());
			pstmt.setString(i++, exist_exItemList.get(7).getComment());
			pstmt.setInt(i++, exItemList.get(7).getId());
			pstmt.executeUpdate();
			i = 1;
			pstmt.setString(i++, exist_exItemList.get(8).getWeek());
			pstmt.setString(i++, exist_exItemList.get(8).getItemName());
			pstmt.setString(i++, exist_exItemList.get(8).getComment());
			pstmt.setInt(i++, exItemList.get(8).getId());
			pstmt.executeUpdate();
			i = 1;
			pstmt.setString(i++, exist_exItemList.get(9).getWeek());
			pstmt.setString(i++, exist_exItemList.get(9).getItemName());
			pstmt.setString(i++, exist_exItemList.get(9).getComment());
			pstmt.setInt(i++, exItemList.get(9).getId());
			pstmt.executeUpdate();
			conn.commit();   //事务提交
			conn.setAutoCommit(true);  //设置成自动提交模式
			request.setAttribute("id", id);
			request.setAttribute("msg", "导入数据成功");
			request.getRequestDispatcher("ScheduleServlet?method=edit&id=" + id).forward(request, response);
		}catch (SQLException e) {
			e.printStackTrace();
			request.setAttribute("msg", "导入数据失败!");
			request.getRequestDispatcher("error.jsp").forward(request, response);
			return;
		}finally{
			DatabaseUtil.close(pstmt);
			DatabaseUtil.close(conn);
		}	
	}
	
	public void export_pdf(HttpServletRequest request, HttpServletResponse response)
	throws ServletException, IOException{
		int id = Integer.parseInt(request.getParameter("id"));
		Schedule schedule = ScheduleDAO.load(id);
		Experiment e = ExperimentDAO.load(schedule.getExperimentID());
		String classID = schedule.getClassID();
		String laboratoryName = LaboratoryDAO.getName(e.getLaboratoryID());
		String departmentName = DepartmentDAO.getName(e.getDepartmentID());
		
		Set<String> classid_set = new HashSet<String>();
		Set<String> classname_set = new HashSet<String>();
		
		
//		int classId_ ;
//    	if(schedule.getClassID().endsWith("theendofthestring")){
//    		classId_ = (classID.length)-1;
//    	}else{
//    		classId_ = classID.length;
//    	}
////		System.out.println("schedule.getClassID() is " + schedule.getClassID());
////		System.out.println("(classID.length)-1 is " + ((classID.length)-1));
//		for(int i=0; i<classId_; i++){
//			classid_set.add(classID[i]);
//		}
//		
//		for(int i=0; i<classId_; i++){
//			
//			classname_set.add((ClassDAO.load(classID[i])).getName());
//		}
		String[] classID2 = (schedule.getClassID()).split("@");
		int classId_ ;
    	if(schedule.getClassID().endsWith("theendofthestring")){
    		classId_ = (classID2.length)-1;
    	}else{
    		classId_ = classID2.length;
    	}
		
//		System.out.println("schedule.getClassID() is " + schedule.getClassID());
//		System.out.println("(classID.length)-1 is " + ((classID.length)-1));
		for(int i=0; i<classId_; i++){
			classid_set.add(classID2[i]);
		}
		
		for(int i=0; i<classId_; i++){
			classname_set.add((ClassDAO.load(classID2[i])).getName().trim());
		}
		
		response.reset();
		//------------------------------------------------------------------------------------添加pdf逻辑
		ByteArrayOutputStream ba = new ByteArrayOutputStream();
		ByteArrayOutputStream totalba = new ByteArrayOutputStream();
		String path = request.getSession().getServletContext().getRealPath("/");
		try {
			/* 打开已经存在的pdf模板 */
			String TemplatePDF = path + "\\upload\\schedule\\schedule_sample.pdf";
			/* 使用中文字体 */
			BaseFont bf = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H",BaseFont.NOT_EMBEDDED);
			BaseFont FontChinese = new Font(bf, 8, Font.NORMAL).getBaseFont();
			PdfReader reader = null;
			PdfStamper stamp = null;
			AcroFields form = null;
			PdfCopyFields copy = new PdfCopyFields(totalba);
			reader = new PdfReader(TemplatePDF);
			/* 将要生成的目标PDF文件名称 */
			ba = new ByteArrayOutputStream();
			stamp = new PdfStamper(reader, ba);
			/* 取出报表模板中的所有字段 */
			form = stamp.getAcroFields();
			/* 为字段赋值,注意字段名称是区分大小写的 */
			form.setFieldProperty("department_name", "textfont", FontChinese, null);
			form.setField("department_name", departmentName);
			form.setFieldProperty("term", "textfont", FontChinese, null);
			form.setField("term", e.getTerm());
			form.setFieldProperty("laboratory_name", "textfont", FontChinese, null);
			form.setField("laboratory_name", laboratoryName);
			form.setFieldProperty("course_name", "textfont", FontChinese, null);
			form.setField("course_name", CourseDAO.getName(e.getCourseID()));
			form.setFieldProperty("class_name", "textfont", FontChinese, null);
			form.setField("class_name", classname_set.toString());
			System.out.println("zu is " + classname_set.toString());
			form.setFieldProperty("class_id", "textfont", FontChinese, null);
			form.setField("class_id", classid_set.toString());
			form.setField("stu_num", ExperimentDAO.loadNumber(id)+"");
			form.setField("group", schedule.getGroupNum() + "");
			form.setFieldProperty("ex_requirement", "textfont", FontChinese, null);
			form.setField("ex_requirement", e.getRequirement());
			form.setFieldProperty("ex_type", "textfont", FontChinese, null);
			form.setField("ex_type", e.getType());
			String check = "√";
			form.setFieldProperty("purpose", "textfont", FontChinese, null);
			form.setField("purpose", schedule.getPurpose());
			form.setFieldProperty("material", "textfont", FontChinese, null);
			form.setField("material", schedule.getMaterial());
			form.setField("techWeek", schedule.getTechWeek() + "");
			form.setField("weekTime", schedule.getWeekTime() + "");
			form.setField("totalTime", schedule.getTotalTime() + "");
			form.setField("theoTime", schedule.getTheoTime() + "");
			form.setField("exTime", schedule.getExTime() + "");
			String tGrade = schedule.gettGrade();
			if(tGrade.equals("一")){
				form.setFieldProperty("tGrade1", "textfont", FontChinese, null);
				form.setField("tGrade1", check);
			}else if(tGrade.equals("二")){
				form.setFieldProperty("tGrade2", "textfont", FontChinese, null);
				form.setField("tGrade2", check);
			}else if(tGrade.equals("三")){
				form.setFieldProperty("tGrade3", "textfont", FontChinese, null);
				form.setField("tGrade3", check);
			}else if(tGrade.equals("四")){
				form.setFieldProperty("tGrade4", "textfont", FontChinese, null);
				form.setField("tGrade4", check);
			}
			form.setFieldProperty("tTerm" + schedule.gettTerm(), "textfont", FontChinese, null);
			form.setField("tTerm" + schedule.gettTerm(), check);
			
			if(schedule.getDemand().equals("考试")){
				form.setFieldProperty("demand1", "textfont", FontChinese, null);
				form.setField("demand1", check);
			}else{
				form.setFieldProperty("demand2", "textfont", FontChinese, null);
				form.setField("demand2", check);
			}
			
			ArrayList<ExItem> exItemList = ExItemDAO.list(id);
			Map<Integer, String> typeMap = ExItemDAO.getType(id);
			
			form.setFieldProperty("week1", "textfont", FontChinese, null);
			form.setField("week1", exItemList.get(0).getWeek());
			form.setFieldProperty("itemName1", "textfont", FontChinese, null);
			form.setField("itemName1", exItemList.get(0).getItemName());
			
			form.setFieldProperty("itemType1", "textfont", FontChinese, null);
			form.setField("itemType1",typeMap.get(exItemList.get(0).getId()));
			
			form.setFieldProperty("comment1", "textfont", FontChinese, null);
			form.setField("comment1", exItemList.get(0).getComment());
			
			form.setFieldProperty("week2", "textfont", FontChinese, null);
			form.setField("week2", exItemList.get(1).getWeek());
			form.setFieldProperty("itemName2", "textfont", FontChinese, null);
			form.setField("itemName2", exItemList.get(1).getItemName());
			
			form.setFieldProperty("itemType2", "textfont", FontChinese, null);
			form.setField("itemType2",typeMap.get(exItemList.get(1).getId()));
			
			form.setFieldProperty("comment2", "textfont", FontChinese, null);
			form.setField("comment2", exItemList.get(1).getComment());
			form.setFieldProperty("week3", "textfont", FontChinese, null);
			form.setField("week3", exItemList.get(2).getWeek());
			form.setFieldProperty("itemName3", "textfont", FontChinese, null);
			form.setField("itemName3", exItemList.get(2).getItemName());
			
			form.setFieldProperty("itemType3", "textfont", FontChinese, null);
			form.setField("itemType3",typeMap.get(exItemList.get(2).getId()));
			
			form.setFieldProperty("comment3", "textfont", FontChinese, null);
			form.setField("comment3", exItemList.get(2).getComment());
			
			form.setFieldProperty("week4", "textfont", FontChinese, null);
			form.setField("week4", exItemList.get(3).getWeek());
			form.setFieldProperty("itemName4", "textfont", FontChinese, null);
			form.setField("itemName4", exItemList.get(3).getItemName());
			
			form.setFieldProperty("itemType4", "textfont", FontChinese, null);
			form.setField("itemType4",typeMap.get(exItemList.get(3).getId()));
			
			form.setFieldProperty("comment4", "textfont", FontChinese, null);
			
			form.setField("comment4", exItemList.get(3).getComment());
			form.setFieldProperty("week5", "textfont", FontChinese, null);
			form.setField("week5", exItemList.get(4).getWeek());
			form.setFieldProperty("itemName5", "textfont", FontChinese, null);
			form.setField("itemName5", exItemList.get(4).getItemName());
			
			form.setFieldProperty("itemType5", "textfont", FontChinese, null);
			form.setField("itemType5",typeMap.get(exItemList.get(4).getId()));
			
			form.setFieldProperty("comment5", "textfont", FontChinese, null);
			
			form.setField("comment5", exItemList.get(4).getComment());
			form.setFieldProperty("week6", "textfont", FontChinese, null);
			form.setField("week6", exItemList.get(5).getWeek());
			form.setFieldProperty("itemName6", "textfont", FontChinese, null);
			form.setField("itemName6", exItemList.get(5).getItemName());
			
			form.setFieldProperty("itemType6", "textfont", FontChinese, null);
			form.setField("itemType6",typeMap.get(exItemList.get(5).getId()));
			
			form.setFieldProperty("comment6", "textfont", FontChinese, null);
			
			form.setField("comment6", exItemList.get(5).getComment());
			form.setFieldProperty("week7", "textfont", FontChinese, null);
			form.setField("week7", exItemList.get(6).getWeek());
			form.setFieldProperty("itemName7", "textfont", FontChinese, null);
			form.setField("itemName7", exItemList.get(6).getItemName());
			
			form.setFieldProperty("itemType7", "textfont", FontChinese, null);
			form.setField("itemType7",typeMap.get(exItemList.get(6).getId()));
			
			form.setFieldProperty("comment7", "textfont", FontChinese, null);
			form.setField("comment7", exItemList.get(6).getComment());
			
			
			form.setFieldProperty("week8", "textfont", FontChinese, null);
			form.setField("week8", exItemList.get(7).getWeek());
			form.setFieldProperty("itemName8", "textfont", FontChinese, null);
			form.setField("itemName8", exItemList.get(7).getItemName());
			
			form.setFieldProperty("itemType8", "textfont", FontChinese, null);
			form.setField("itemType8",typeMap.get(exItemList.get(7).getId()));
			
			form.setFieldProperty("comment8", "textfont", FontChinese, null);
			form.setField("comment8", exItemList.get(7).getComment());
			form.setFieldProperty("week9", "textfont", FontChinese, null);
			form.setField("week9", exItemList.get(8).getWeek());
			form.setFieldProperty("itemName9", "textfont", FontChinese, null);
			form.setField("itemName9", exItemList.get(8).getItemName());
			
			form.setFieldProperty("itemType9", "textfont", FontChinese, null);
			form.setField("itemType9",typeMap.get(exItemList.get(8).getId()));
			
			form.setFieldProperty("comment9", "textfont", FontChinese, null);
			form.setField("comment9", exItemList.get(8).getComment());
			
			form.setFieldProperty("week10", "textfont", FontChinese, null);
			form.setField("week10", exItemList.get(9).getWeek());
			form.setFieldProperty("itemName10", "textfont", FontChinese, null);
			form.setField("itemName10", exItemList.get(9).getItemName());
			
			form.setFieldProperty("itemType10", "textfont", FontChinese, null);
			form.setField("itemType10",typeMap.get(exItemList.get(9).getId()));
			
			form.setFieldProperty("comment10", "textfont", FontChinese, null);
			form.setField("comment10", exItemList.get(9).getComment());
			
			form.setFieldProperty("week11", "textfont", FontChinese, null);
			form.setField("week11", exItemList.get(10).getWeek());
			form.setFieldProperty("itemName11", "textfont", FontChinese, null);
			form.setField("itemName11", exItemList.get(10).getItemName());
			
			form.setFieldProperty("itemType11", "textfont", FontChinese, null);
			form.setField("itemType11",typeMap.get(exItemList.get(10).getId()));
			
			form.setFieldProperty("comment11", "textfont", FontChinese, null);
			form.setField("comment11", exItemList.get(10).getComment());
			
			form.setFieldProperty("week12", "textfont", FontChinese, null);
			form.setField("week12", exItemList.get(11).getWeek());
			form.setFieldProperty("itemName12", "textfont", FontChinese, null);
			form.setField("itemName12", exItemList.get(11).getItemName());
			
			form.setFieldProperty("itemType12", "textfont", FontChinese, null);
			form.setField("itemType12",typeMap.get(exItemList.get(11).getId()));
			
			form.setFieldProperty("comment12", "textfont", FontChinese, null);
			form.setField("comment12", exItemList.get(11).getComment());
			form.setFieldProperty("week13", "textfont", FontChinese, null);
			form.setField("week13", exItemList.get(12).getWeek());
			form.setFieldProperty("itemName13", "textfont", FontChinese, null);
			form.setField("itemName13", exItemList.get(12).getItemName());
			
			form.setFieldProperty("itemType13", "textfont", FontChinese, null);
			form.setField("itemType13",typeMap.get(exItemList.get(12).getId()));
			
			form.setFieldProperty("comment13", "textfont", FontChinese, null);
			form.setField("comment13", exItemList.get(12).getComment());
			
			form.setFieldProperty("week14", "textfont", FontChinese, null);
			form.setField("week14", exItemList.get(13).getWeek());
			form.setFieldProperty("itemName14", "textfont", FontChinese, null);
			form.setField("itemName14", exItemList.get(13).getItemName());
			
			form.setFieldProperty("itemType14", "textfont", FontChinese, null);
			form.setField("itemType14",typeMap.get(exItemList.get(13).getId()));
			
			form.setFieldProperty("comment14", "textfont", FontChinese, null);
			
			form.setField("comment14", exItemList.get(13).getComment());
			form.setFieldProperty("week15", "textfont", FontChinese, null);
			form.setField("week15", exItemList.get(14).getWeek());
			form.setFieldProperty("itemName15", "textfont", FontChinese, null);
			form.setField("itemName15", exItemList.get(14).getItemName());
			
			form.setFieldProperty("itemType15", "textfont", FontChinese, null);
			form.setField("itemType15",typeMap.get(exItemList.get(14).getId()));
			
			form.setFieldProperty("comment15", "textfont", FontChinese, null);
			
			form.setField("comment15", exItemList.get(14).getComment());
			form.setFieldProperty("week16", "textfont", FontChinese, null);
			form.setField("week16", exItemList.get(15).getWeek());
			form.setFieldProperty("itemName16", "textfont", FontChinese, null);
			form.setField("itemName16", exItemList.get(15).getItemName());
			
			form.setFieldProperty("itemType16", "textfont", FontChinese, null);
			form.setField("itemType16",typeMap.get(exItemList.get(15).getId()));
			
			form.setFieldProperty("comment16", "textfont", FontChinese, null);
			
			form.setField("comment16", exItemList.get(15).getComment());
			form.setFieldProperty("week17", "textfont", FontChinese, null);
			form.setField("week17", exItemList.get(16).getWeek());
			form.setFieldProperty("itemName17", "textfont", FontChinese, null);
			form.setField("itemName17", exItemList.get(16).getItemName());
			
			form.setFieldProperty("itemType17", "textfont", FontChinese, null);
			form.setField("itemType17",typeMap.get(exItemList.get(16).getId()));
			
			form.setFieldProperty("comment17", "textfont", FontChinese, null);
			form.setField("comment17", exItemList.get(16).getComment());
			
			
			form.setFieldProperty("week18", "textfont", FontChinese, null);
			form.setField("week18", exItemList.get(17).getWeek());
			form.setFieldProperty("itemName18", "textfont", FontChinese, null);
			form.setField("itemName18", exItemList.get(17).getItemName());
			
			form.setFieldProperty("itemType18", "textfont", FontChinese, null);
			form.setField("itemType18",typeMap.get(exItemList.get(17).getId()));
			
			form.setFieldProperty("comment18", "textfont", FontChinese, null);
			form.setField("comment18", exItemList.get(17).getComment());
			form.setFieldProperty("week19", "textfont", FontChinese, null);
			form.setField("week19", exItemList.get(18).getWeek());
			form.setFieldProperty("itemName19", "textfont", FontChinese, null);
			form.setField("itemName19", exItemList.get(18).getItemName());
			
			form.setFieldProperty("itemType19", "textfont", FontChinese, null);
			form.setField("itemType19",typeMap.get(exItemList.get(18).getId()));
			
			form.setFieldProperty("comment19", "textfont", FontChinese, null);
			form.setField("comment19", exItemList.get(18).getComment());
			
			form.setFieldProperty("week20", "textfont", FontChinese, null);
			form.setField("week20", exItemList.get(19).getWeek());
			form.setFieldProperty("itemName20", "textfont", FontChinese, null);
			form.setField("itemName20", exItemList.get(19).getItemName());
			
			form.setFieldProperty("itemType20", "textfont", FontChinese, null);
			form.setField("itemType20",typeMap.get(exItemList.get(19).getId()));
			
			form.setFieldProperty("comment20", "textfont", FontChinese, null);
			form.setField("comment20", exItemList.get(19).getComment());
			
			stamp.setFormFlattening(false);
			reader.close();
			stamp.close();
			copy.addDocument(new PdfReader(ba.toByteArray()));
			ba.close();
			copy.close();
			reader = new PdfReader(totalba.toByteArray());
			stamp = new PdfStamper(reader, ba);
			form = stamp.getAcroFields();
			stamp.close();
			reader.close();
		} catch (DocumentException de) {
			de.printStackTrace();
		}
		// setting some response headers
		response.setHeader("Expires", "0");
		response.setHeader("Cache-Control", "must-revalidate, post-check=0, pre-check=0");
		response.setHeader("Pragma", "public");
		response.setContentType("application/pdf");
		/* 如果想出来让IE提示你是打开还是保存的对话框，加上下面这句就可以了 */
		//response.setHeader("Content-disposition", "attachment; filename=schedule.pdf");
		response.setContentLength(ba.size());
		try {
			ServletOutputStream out = response.getOutputStream();
			ba.writeTo(out);
			out.flush();
			out.close();
			ba.close();
			totalba.close();
		} catch (IOException ex) {
			ex.printStackTrace();
			System.err.println("A Document error:" + ex.getMessage());
		} finally {
			if (ba != null) ba.close();
			if (totalba != null) totalba.close();
		}
	}
}
