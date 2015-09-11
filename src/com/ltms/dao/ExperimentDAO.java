package com.ltms.dao;

import com.ltms.model.ExItem;
import com.ltms.model.Experiment;
import com.ltms.model.Laboratory;
import com.ltms.model.Schedule;
import com.ltms.model.Sysxx;
import com.ltms.util.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ExperimentDAO {
	public static void initFromRS(ResultSet rs, Experiment experiment){
		try {
			experiment.setId(rs.getInt("id"));
			experiment.setTimeInfo(rs.getString("timeInfo"));
			experiment.setCourseID(rs.getString("courseID"));
			experiment.setClassInfo(rs.getString("classInfo"));
			experiment.setTeacherInfo(rs.getString("teacherInfo"));
			experiment.setDepartmentID(rs.getInt("departmentID"));
			experiment.setLaboratoryID(rs.getInt("laboratoryID"));
			experiment.setRequirement(rs.getString("requirement"));
			experiment.setType(rs.getString("type"));
			experiment.setTerm(rs.getString("term"));
			experiment.setState(rs.getInt("state"));
			experiment.setSymc(rs.getString("symc"));
			experiment.setSyzzy(rs.getString("syzzy"));
			experiment.setSsxk(rs.getString("ssxk"));
			experiment.setMxzy(rs.getString("mxzy"));
			experiment.setSylb(rs.getInt("sylb"));
			experiment.setXs(rs.getInt("xs"));
			experiment.setSyzlb(rs.getInt("syzlb"));
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}	
	
	public static ArrayList<Experiment> list(int state){
		Connection conn = DatabaseUtil.getConn();
		ResultSet rs = null;
		String sql = "select * from _experiment where state = ?";
		PreparedStatement pstmt = DatabaseUtil.prepareStmt(conn, sql);
		ArrayList<Experiment> experimentList = new ArrayList<Experiment>();
		try {
			pstmt.setInt(1, state);
			rs = pstmt.executeQuery();
			while(rs.next()){
				Experiment experiment = new Experiment();
				ExperimentDAO.initFromRS(rs, experiment);
				experimentList.add(experiment);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DatabaseUtil.close(rs);
			DatabaseUtil.close(pstmt);
			DatabaseUtil.close(conn);
		}
		return experimentList;
	}
	
	public static Experiment load(int id){
		Connection conn = DatabaseUtil.getConn();
		ResultSet rs = null;
		String sql = "select * from _experiment where id = ?";
		PreparedStatement pstmt = DatabaseUtil.prepareStmt(conn, sql);
		Experiment e = new Experiment();
		try {
			int i = 1;
			pstmt.setInt(i++, id);
			rs = pstmt.executeQuery();
			if(rs.next()){
				ExperimentDAO.initFromRS(rs, e);
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
		}finally{
			DatabaseUtil.close(rs);
			DatabaseUtil.close(pstmt);
			DatabaseUtil.close(conn);
		}
		return e;
	}
	
	public static List<Experiment> load(){
		Connection conn = DatabaseUtil.getConn();
		ResultSet rs = null;
		String sql = "select * from _experiment";
		List<Experiment> list = new ArrayList<Experiment>();
		PreparedStatement pstmt = DatabaseUtil.prepareStmt(conn, sql);
		
		try {
			rs = pstmt.executeQuery();
			while(rs.next()){
				Experiment e = new Experiment();
				ExperimentDAO.initFromRS(rs, e);
				list.add(e);
			}
			
		} catch (SQLException ex) {
			ex.printStackTrace();
		}finally{
			DatabaseUtil.close(rs);
			DatabaseUtil.close(pstmt);
			DatabaseUtil.close(conn);
		}
		return list;
	}
	
	public static int loadbyDepa(int departmentID, String term){
		Connection conn = DatabaseUtil.getConn();
		ResultSet rs = null;
		String sql = "select count(*) from _experiment where departmentID = ? and term like ?";
		PreparedStatement pstmt = DatabaseUtil.prepareStmt(conn, sql);
		int size = 0;
		try {
			pstmt.setInt(1, departmentID);
			pstmt.setString(2, "%" + term +"%");
			rs = pstmt.executeQuery();
			if(rs.next()){
				size = rs.getInt(1);
			}
			
		} catch (SQLException ex) {
			ex.printStackTrace();
		}finally{
			DatabaseUtil.close(rs);
			DatabaseUtil.close(pstmt);
			DatabaseUtil.close(conn);
		}
		return size;
	}
	
	public static List<Experiment> loadbyterm(String term){
		Connection conn = DatabaseUtil.getConn();
		ResultSet rs = null;
		String sql = "select * from _experiment where term = ?";
		List<Experiment> list = new ArrayList<Experiment>();
		PreparedStatement pstmt = DatabaseUtil.prepareStmt(conn, sql);
		
		try {
			pstmt.setString(1, term);
			rs = pstmt.executeQuery();
			while(rs.next()){
				Experiment e = new Experiment();
				ExperimentDAO.initFromRS(rs, e);
				list.add(e);
			}
			
		} catch (SQLException ex) {
			ex.printStackTrace();
		}finally{
			DatabaseUtil.close(rs);
			DatabaseUtil.close(pstmt);
			DatabaseUtil.close(conn);
		}
		return list;
	}
	
	public static int getScheduleID(int id){
		Connection conn = DatabaseUtil.getConn();
		ResultSet rs = null;
		String sql = "select id from _schedule where experimentID = ?";
		PreparedStatement pstmt = DatabaseUtil.prepareStmt(conn, sql);
		int scheduleID = 0;
		try {
			int i = 1;
			pstmt.setInt(i++, id);
			rs = pstmt.executeQuery();
			if(rs.next()){
				scheduleID = rs.getInt("id");
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
		}finally{
			DatabaseUtil.close(rs);
			DatabaseUtil.close(pstmt);
			DatabaseUtil.close(conn);
		}
		return scheduleID;
	}
	
	
	public static String load_zydm(String sign){
		Connection conn = DatabaseUtil.getConn();
		ResultSet rs = null;
		String sql = "select * from zyl where sign = ?";
		PreparedStatement pstmt = DatabaseUtil.prepareStmt(conn, sql);
		int scheduleID = 0;
		String dm = "";
		try {
			int i = 1;
			pstmt.setString(i++, sign);
			rs = pstmt.executeQuery();
			rs.next();
			dm = rs.getString("dm");
		} catch (SQLException ex) {
			ex.printStackTrace();
		}finally{
			DatabaseUtil.close(rs);
			DatabaseUtil.close(pstmt);
			DatabaseUtil.close(conn);
		}
		return dm;
	}
	
	public static void addSykc(Experiment e){
		Connection conn = DatabaseUtil.getConn();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		boolean sign = false; //假设不存在
		try {
			//检查课程是否存在
			String sql = "select * from _course_sy where id = ?";
			pstmt = DatabaseUtil.prepareStmt(conn, sql);
			int i = 1;
			pstmt.setString(i++, e.getCourseID());
			rs = pstmt.executeQuery();
			if (rs.next()) {
				sign = true;
			}
			String course_name = "";
			//不存在
			if (!sign) {
				//根据id获取课程的名字
				sql = "select * from _course where id = ?";
				pstmt = DatabaseUtil.prepareStmt(conn, sql);
				pstmt.setString(1, e.getCourseID());
				rs = pstmt.executeQuery();
				rs.next();
				course_name = rs.getString("name");
				System.out.println("course_name is " + course_name);

				//插入课程

				String sql4 = "insert into _course_sy values(?, ?, ?)";
				pstmt = DatabaseUtil.prepareStmt(conn, sql4);
				i = 1;
				pstmt.setString(i++, e.getCourseID());
				pstmt.setString(i++, course_name);
				pstmt.setInt(i++, e.getDepartmentID());
				pstmt.executeUpdate();
				
				//插入_course_sy_mes记录
				String sql5 = "insert into _course_sy_mes values(?, ?)";
				pstmt = DatabaseUtil.prepareStmt(conn, sql5);
				i = 1;
				pstmt.setString(i++, e.getCourseID());
				pstmt.setString(i++, "");
				pstmt.executeUpdate();
			}
		} catch (Exception e2) {
			// TODO: handle exception
		}finally{
			DatabaseUtil.close(rs);
			DatabaseUtil.close(pstmt);
			DatabaseUtil.close(conn);
		}
		
		
		
		
	}
	
	
	public static int add(Experiment e){
		int isSuccess = 0;
		Connection conn = DatabaseUtil.getConn();
		String sql = "insert into _experiment values(null, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		PreparedStatement pstmt = DatabaseUtil.prepareStmt(conn, sql, PreparedStatement.RETURN_GENERATED_KEYS);
		try {
			conn.setAutoCommit(false);   //事务开始
			int i = 1;
			pstmt.setString(i++, e.getCourseID());
			pstmt.setString(i++, e.getTeacherInfo());
			pstmt.setString(i++, e.getTimeInfo());
			pstmt.setInt(i++, e.getDepartmentID());
			pstmt.setInt(i++, e.getLaboratoryID());
			pstmt.setString(i++, e.getClassInfo());
			pstmt.setString(i++, e.getRequirement());
			pstmt.setString(i++, e.getType());
			pstmt.setString(i++, e.getTerm());
			pstmt.setInt(i++, e.getState());
			//添加的数据
			pstmt.setString(i++, e.getSymc());
			pstmt.setInt(i++, e.getSylb());
			pstmt.setInt(i++, e.getXs());
			pstmt.setString(i++, e.getSyzzy());
			pstmt.setInt(i++, e.getSyzlb());
			pstmt.setString(i++, e.getSsxk());
			pstmt.setString(i++, e.getMxzy());
			
			pstmt.executeUpdate();	
			ResultSet rs = pstmt.getGeneratedKeys();
			rs.next();
			int experimentID = rs.getInt(1);
			
			String sql2 = "insert into _schedule values(null, ?, ?, ?, 1, '', '', '一', '1', '考试', 0, 0, 0, 0, 0, ?)";
			pstmt = DatabaseUtil.prepareStmt(conn, sql2, Statement.RETURN_GENERATED_KEYS);
			i = 1;
			pstmt.setInt(i++, experimentID);
			
			String[] strings = e.getTeacherInfo().split(",");
			pstmt.setString(i++, strings[1]);
			
			//获取classID
			String[] classInfo = e.getClassInfo().split("@");
			String class_string = classInfo[0];
			int _length_ = classInfo.length;
			if(e.getClassInfo().endsWith("theendofthestring")){
				_length_--;
			}
			for(int j=2; j<_length_; j++){
				class_string += "@" + classInfo[j++];
			}
			System.out.println("classid is " + class_string);
			pstmt.setString(i++, class_string);
			pstmt.setString(i++, e.getTerm());
			pstmt.executeUpdate();	
			ResultSet sKey = pstmt.getGeneratedKeys();
			sKey.next();
			int scheduleID = sKey.getInt(1);
			
			//插入number   
			String sql6 = "insert into number values(?,?)";
			pstmt = DatabaseUtil.prepareStmt(conn, sql6);
			i = 1;
			pstmt.setInt(i++, scheduleID);
			pstmt.setInt(i++, 0);
			pstmt.executeUpdate();	
			
//			//插入type   
//			String sql7 = "insert into type_id values(?,?)";
//			pstmt = DatabaseUtil.prepareStmt(conn, sql6);
//			i = 1;
//			pstmt.setInt(i++, scheduleID);
//			pstmt.setInt(i++, 0);
//			pstmt.executeUpdate();	
			
			String sql3 = "insert into _exItem(scheduleID, week, itemName, comment, laboratoryID) values" +
					"(?, '', '', '', ?), (?, '', '', '', ?), (?, '', '', '', ?), (?, '', '', '', ?), (?, '', '', '', ?)," +
					" (?, '', '', '', ?), (?, '', '', '', ?), (?, '', '', '', ?), (?, '', '', '', ?), (?, '', '', '', ?)";
			pstmt = DatabaseUtil.prepareStmt(conn, sql3);
			i = 1;
			pstmt.setInt(i++, scheduleID);
			pstmt.setInt(i++, e.getLaboratoryID());
			pstmt.setInt(i++, scheduleID);
			pstmt.setInt(i++, e.getLaboratoryID());
			pstmt.setInt(i++, scheduleID);
			pstmt.setInt(i++, e.getLaboratoryID());
			pstmt.setInt(i++, scheduleID);
			pstmt.setInt(i++, e.getLaboratoryID());
			pstmt.setInt(i++, scheduleID);
			pstmt.setInt(i++, e.getLaboratoryID());
			pstmt.setInt(i++, scheduleID);
			pstmt.setInt(i++, e.getLaboratoryID());
			pstmt.setInt(i++, scheduleID);
			pstmt.setInt(i++, e.getLaboratoryID());
			pstmt.setInt(i++, scheduleID);
			pstmt.setInt(i++, e.getLaboratoryID());
			pstmt.setInt(i++, scheduleID);
			pstmt.setInt(i++, e.getLaboratoryID());
			pstmt.setInt(i++, scheduleID);
			pstmt.setInt(i++, e.getLaboratoryID());
			pstmt.executeUpdate();	
			conn.commit();   //事务提交
			conn.setAutoCommit(true);  //设置成自动提交模式
			isSuccess = scheduleID;
		} catch (SQLException ex) {
			try {
				conn.rollback();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				
			}
			ex.printStackTrace();
		}finally{
			
			DatabaseUtil.close(pstmt);
			DatabaseUtil.close(conn);
		}
		return isSuccess;
	}
	
	public static boolean update(Experiment e){ 
		boolean isSuccess = false;              
		Connection conn = DatabaseUtil.getConn();
		String sql = null;
		PreparedStatement pstmt = null;
		sql = "update _experiment set teacherInfo = ?, timeInfo = ?, laboratoryID = ?, classInfo = ?, requirement = ?, type = ?, state = ?, symc = ?, " +
				"sylb = ?, xs = ?, syzzy = ?, syzlb = ?, ssxk = ?, mxzy = ? where id = ?";
		pstmt = DatabaseUtil.prepareStmt(conn, sql);
		try {
			conn.setAutoCommit(false);  //事务开始
			int i = 1;
			System.out.println("teacherinfo is " + e.getTeacherInfo());
			pstmt.setString(i++, e.getTeacherInfo());
			pstmt.setString(i++, e.getTimeInfo());
			pstmt.setInt(i++, e.getLaboratoryID());
			pstmt.setString(i++, e.getClassInfo());
			pstmt.setString(i++, e.getRequirement());
			pstmt.setString(i++, e.getType());
			pstmt.setInt(i++, e.getState());
			
			pstmt.setString(i++, e.getSymc());
			pstmt.setInt(i++, e.getSylb());
			pstmt.setInt(i++, e.getXs());
			pstmt.setString(i++, e.getSyzzy());
			pstmt.setInt(i++, e.getSyzlb());
			pstmt.setString(i++, e.getSsxk());
			pstmt.setString(i++, e.getMxzy());
			
			pstmt.setInt(i++, e.getId());
			
			pstmt.executeUpdate();
			
			sql = "update _schedule set teacherID = ?, classID = ? where experimentID = ?";
			pstmt = DatabaseUtil.prepareStmt(conn, sql);
			i = 1;
			
			String[] strings = e.getTeacherInfo().split(",");
//			System.out.println(strings.length);
//			for(String s : strings){
//				System.out.println("s is " + s);
//			}
//			System.out.println("id is " + strings[1]);
			pstmt.setString(i++, strings[1]);
			
			//获取classID
			String[] classInfo = e.getClassInfo().split("@");
			String class_string = classInfo[0];
			int _length_ = classInfo.length;
			if(e.getClassInfo().endsWith("theendofthestring")){
				_length_--;
			}
			for(int j=2; j<_length_; j++){
				class_string += "@" + classInfo[j++];
			}
			System.out.println("classid is " + class_string);
			pstmt.setString(i++, class_string);
			pstmt.setInt(i++, e.getId());
			pstmt.executeUpdate();
			conn.commit();   //事务提交
			conn.setAutoCommit(true);  //设置成自动提交模式
			isSuccess = true;
		}catch(SQLException ex) {
			ex.printStackTrace();
		}finally{
			DatabaseUtil.close(pstmt);
			DatabaseUtil.close(conn);
		}
		return isSuccess;
	}
	
	public static boolean delete(int id){
		boolean isSuccess = false;
		Connection conn = DatabaseUtil.getConn();
		String sql = "";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		
		int scheduleID = 0;
		try {
			conn.setAutoCommit(false);  //事务开始
			
			
			//获取要删除的实验的课程id
			sql = "select * from _experiment where id = ?";
			pstmt = DatabaseUtil.prepareStmt(conn, sql);
			pstmt.setInt(1,id );
			rs = pstmt.executeQuery();
			rs.next();
			String course_id = rs.getString("courseID");
			
			sql = "delete from _experiment where id = ?";
			pstmt = DatabaseUtil.prepareStmt(conn, sql);
			pstmt.setInt(1, id);
			pstmt.executeUpdate();
			sql = "select id from _schedule where experimentID = ?";
			pstmt = DatabaseUtil.prepareStmt(conn, sql);
			pstmt.setInt(1, id);
			rs = pstmt.executeQuery();
			if(rs.next()){
				scheduleID = rs.getInt("id");
			}
			sql = "delete from _schedule where experimentID = ?";
			pstmt = DatabaseUtil.prepareStmt(conn, sql);
			pstmt.setInt(1, id);
			pstmt.executeUpdate();
			sql = "delete from _exItem where scheduleID = ?";
			pstmt = DatabaseUtil.prepareStmt(conn, sql);
			pstmt.setInt(1, scheduleID);
			pstmt.executeUpdate();
			
			//删除number   音乐系未改
			sql = "delete from number where schedule_id = ?";
			pstmt = DatabaseUtil.prepareStmt(conn, sql);
			pstmt.setInt(1, scheduleID);
			pstmt.executeUpdate();
			
			
			//判断实验列表中是否还有该课程
			sql = "select * from _experiment where courseID = ?";
			pstmt = DatabaseUtil.prepareStmt(conn, sql);
			pstmt.setString(1,course_id );
			rs = pstmt.executeQuery();
			//没有课程的话，把course_sy和course_sy_mes两个表删掉
			if(!rs.next()){
				sql = "delete from _course_sy where id = ?";
				pstmt = DatabaseUtil.prepareStmt(conn, sql);
				pstmt.setString(1, course_id);
				pstmt.executeUpdate();
				
				sql = "delete from _course_sy_mes where id = ?";
				pstmt = DatabaseUtil.prepareStmt(conn, sql);
				pstmt.setString(1, course_id);
				pstmt.executeUpdate();
			}
			
			conn.commit();   //事务提交
			conn.setAutoCommit(true);  //设置成自动提交模式
			isSuccess = true;
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DatabaseUtil.close(pstmt);
			DatabaseUtil.close(conn);
		}
		return isSuccess;
	}
	
	public static ArrayList<Experiment> list(int departmentID, int state){
		Connection conn = DatabaseUtil.getConn();
		ResultSet rs = null;
		String sql = "select * from _experiment where departmentID = ? and state = ? order by id desc";
		PreparedStatement pstmt = DatabaseUtil.prepareStmt(conn, sql);
		ArrayList<Experiment> experimentList = new ArrayList<Experiment>();
		try {
			int i = 1;
			pstmt.setInt(i++, departmentID);
			pstmt.setInt(i++, state);
			rs = pstmt.executeQuery();
			while(rs.next()){
				Experiment experiment = new Experiment();
				ExperimentDAO.initFromRS(rs, experiment);
				experimentList.add(experiment);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DatabaseUtil.close(rs);
			DatabaseUtil.close(pstmt);
			DatabaseUtil.close(conn);
		}
		return experimentList;
	}
	
	public static String printList(List<Experiment> experimentList){
		String tr_string = "";
		Map<Integer, String> stringMap = new HashMap<Integer, String>();
		Connection conn = DatabaseUtil.getConn();
		String sql = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		sql = "select e.id, l.name as l_name, c.name as c_name, e.classInfo, e.timeInfo, e.term from _laboratory as l, _course as c, _experiment as e where e.laboratoryID = l.id and e.courseID = c.id and e.id in(?";
		int size = experimentList.size();
		for(int i=0; i<size; i++){
			sql += ", ?";
		}
		sql += ") order by e.id desc";
		pstmt = DatabaseUtil.prepareStmt(conn, sql);
		try {
			int i = 1;
			pstmt.setInt(i++, 0);
			for(Experiment experiment : experimentList){
				pstmt.setInt(i++, experiment.getId());
			}
			rs = pstmt.executeQuery();
			while(rs.next()){
				String classInfo[] = rs.getString("classInfo").split("@");
				String class_string = classInfo[0];
				int _length_ = classInfo.length;
				if(rs.getString("classInfo").endsWith("theendofthestring")){
					_length_--;
				}
				for(int j=2; j<_length_; j++){
					class_string += "<br/>&nbsp;" + classInfo[j++];
				}
				
//				String temsString = rs.getString("c_name");
//				if(temsString.trim().length() == 0 || temsString.trim().equals("")){
//					temsString = "null";
//				}
//				System.out.println("tems is " + temsString);
//				
//				String map_string = "<td class=\"exTd1\" style=\"text-align:left;\">&nbsp;" + temsString + "&nbsp;</td><td class=\"exTd2\" style=\"text-align:left;\">&nbsp;" + 
				String map_string = "<td class=\"exTd1\" style=\"text-align:left;\">&nbsp;" + rs.getString("c_name") + "&nbsp;</td><td class=\"exTd2\" style=\"text-align:left;\">&nbsp;" + 
				rs.getString("l_name") + "&nbsp;</td><td class=\"exTd3\" style=\"text-align:left;\">&nbsp;" + class_string + "&nbsp;</td>";
				
				map_string += "<td class=\"exTd6\">";
				String temessString = rs.getString("timeInfo");
				
				if(rs.getString("timeInfo") == null || rs.getString("timeInfo").trim().length() == 0 ||
						rs.getString("timeInfo").trim().equals("")){
					temessString = "0,0,0,0,0,0,0";
//					System.out.println("#222");
				}
				
				
				String time[] = temessString.split("@");
//				System.out.println("timeinfo is " + rs.getString("timeInfo"));
				int count = 1;
//				System.out.println("time length is " + time.length);
				for(String tss : time){
//					System.out.println("tss is " + tss);
					
					if(tss == null || tss.trim().length() == 0 ||
							tss.trim().equals("")){
						tss = "0,0,0,0,0,0,0";
//						System.out.println("#333");
					}
					
					if(count!=1) map_string += "<br/>";
					String week[] = tss.split(",");
					int week_length = week.length;
//					System.out.println("length is " + week_length);   
//					map_string += "&nbsp;第" + week[week_length-6] + "周&nbsp;-&nbsp;第" + week[week_length-5] + "周&nbsp;&nbsp;" + week[week_length-4] + "<br/>";
					map_string += week[week_length-3] + "&nbsp;第" + String.format("%02d", Integer.parseInt(week[week_length-2])) + "节&nbsp;-&nbsp;第" + String.format("%02d", Integer.parseInt(week[week_length-1])) + "节&nbsp;";
					
					count++;
				}
				map_string += "</td><td class=\"exTd7\">&nbsp;" + rs.getString("term") + "&nbsp;</td>\n";
				stringMap.put(rs.getInt("id"), map_string);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DatabaseUtil.close(rs);
			DatabaseUtil.close(pstmt);
			DatabaseUtil.close(conn);
		}
		for(Experiment experiment : experimentList){
			tr_string += "<tr class=\"exTr\">\n";
			tr_string += stringMap.get(experiment.getId());
			tr_string += "<td>\n";
			tr_string += "<input type=\"button\" value=\"查看\" onClick=\"javascript:window.location.href='viewExperiment.jsp?id=" + experiment.getId() + "'\" class=\"form_btn\"/>\n";
			tr_string += "<input type=\"button\" value=\"修改\" onClick=\"javascript:window.location.href='editExperiment.jsp?id=" + experiment.getId() + "'\" class=\"form_btn\"/>\n";
			tr_string += "<input type=\"button\" value=\"删除\" onClick=\"javascript:linkok('ExperimentServlet?method=delete&id=" + experiment.getId() + "&departmentID=" + experiment.getDepartmentID() + "')\" class=\"form_btn\"/>\n";
			tr_string += "</td>\n";
			tr_string += "</tr>\n";
		}
		return tr_string;
	}
	
	public static String printList2(List<Experiment> experimentList){
		String tr_string = "";
		Map<Integer, String> stringMap = new HashMap<Integer, String>();
		Connection conn = DatabaseUtil.getConn();
		String sql = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		sql = "select e.id, l.name as l_name, c.name as c_name, e.classInfo, e.teacherInfo, e.timeInfo, e.term from _laboratory as l, _course as c, _experiment as e where e.laboratoryID = l.id and e.courseID = c.id and e.id in(?";
		int size = experimentList.size();
		for(int i=0; i<size; i++){
			sql += ", ?";
		}
		sql += ") order by e.id desc";
		pstmt = DatabaseUtil.prepareStmt(conn, sql);
		try {
			int i = 1;
			pstmt.setInt(i++, 0);
			for(Experiment experiment : experimentList){
				pstmt.setInt(i++, experiment.getId());
			}
			rs = pstmt.executeQuery();
			while(rs.next()){
				String classInfo[] = rs.getString("classInfo").split("@");
				String class_string = classInfo[0];
				for(int j=2; j<classInfo.length-1; j++){
					class_string += "<br/>&nbsp;" + classInfo[j++];
				}
				String map_string = "<td class=\"exTd1\" style=\"text-align:left;\">&nbsp;" + rs.getString("c_name") + "&nbsp;</td><td class=\"exTd2\" style=\"text-align:left;\">&nbsp;" + 
				rs.getString("l_name") + "&nbsp;</td><td class=\"exTd3\" style=\"text-align:left;\">&nbsp;" + class_string + "&nbsp;</td>" +
				"<td class=\"exTd6\">";
				
				String time[] = rs.getString("timeInfo").split("@");
				int count = 1;
				for(String tss : time){
					if(count!=1) map_string += "<br/>";
					String week[] = tss.split(",");
					int week_length = week.length;
					map_string += week[week_length-3] + "&nbsp;第" + String.format("%02d", Integer.parseInt(week[week_length-2])) + "节&nbsp;-&nbsp;第" + String.format("%02d", Integer.parseInt(week[week_length-1])) + "节&nbsp;";
					count++;
				}
				map_string += "</td><td class=\"exTd7\">&nbsp;" + rs.getString("term") + "&nbsp;</td>\n";
				
				stringMap.put(rs.getInt("id"), map_string);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DatabaseUtil.close(rs);
			DatabaseUtil.close(pstmt);
			DatabaseUtil.close(conn);
		}
		for(Experiment experiment : experimentList){
			String teacherID = (experiment.getTeacherInfo().split(","))[1];
			tr_string += "<tr class=\"exTr\">\n";
			tr_string += stringMap.get(experiment.getId());
			tr_string += "<td>\n";
			tr_string += "<input type=\"button\" value=\"查看\" onClick=\"javascript:window.location.href='viewExperiment.jsp?id=" + experiment.getId() + "'\" class=\"form_btn\"/>\n";
			tr_string += "<input type=\"button\" value=\"修改\" onClick=\"javascript:window.location.href='editExperiment_music.jsp?id=" + experiment.getId() + "'\" class=\"form_btn\"/>\n";
			tr_string += "<input type=\"button\" value=\"删除\" onClick=\"javascript:linkok('ExperimentServlet?method=delete_music&id=" + experiment.getId() + "&teacherID=" + teacherID + "')\" class=\"form_btn\"/>\n";
			tr_string += "</td>\n";
			tr_string += "</tr>\n";
		}
		return tr_string;
	}
	
	public static String index_printList(List<Experiment> experimentList){
		if(experimentList.size() == 0){
			return "<span style=\"padding-left:10px;\">暂无实验安排</span>\n";
		}
		StringBuffer tr_string = new StringBuffer();
		Connection conn = DatabaseUtil.getConn();
		String sql = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		sql = "select e.id, s.id as s_id, l.name as l_name, c.name as c_name, e.classInfo, e.timeInfo, e.term from _laboratory as l, _course as c, _experiment as e, _schedule as s where e.laboratoryID = l.id and e.courseID = c.id and e.id = s.experimentID and e.id in(?";
		int size = experimentList.size();
		for(int i=0; i<size; i++){
			sql += ", ?";
		}
		sql += ") order by e.id desc";
		pstmt = DatabaseUtil.prepareStmt(conn, sql);
		try {
			int i = 1;
			pstmt.setInt(i++, 0);
			for(Experiment experiment : experimentList){
				pstmt.setInt(i++, experiment.getId());
			}
			rs = pstmt.executeQuery();
			int count = 0;
			int page = (size + 9) / 10;
			tr_string.append("<ul class=\"anPaiList\">\n");
			for(int k=0; k<page; k++){
				if(k == 0){
					tr_string.append("<li class=\"moveOn\">1</li>\n");
				}else{
					tr_string.append("<li>" + (k+1) +"</li>\n");
				}
			}
			tr_string.append("</ul>\n");
			tr_string.append("<div class=\"contentBox\">\n");
			while(rs.next()){
				String classInfo[] = rs.getString("classInfo").split("@");
				String class_string = classInfo[0];
				for(int j=2; j<classInfo.length-1; j++){
					class_string += "&nbsp;" + classInfo[j++];
				}
				String time[] = rs.getString("timeInfo").split("@");
				String week_string = "";
				int coun1t = 1;
				for(String tss : time){
					if(coun1t!=1) week_string += "&nbsp;,&nbsp;";
					String week[] = tss.split(",");
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
					week_string +=  "&nbsp;" + week[week_length-3] + "&nbsp;第" + String.format("%02d", Integer.parseInt(week[week_length-2])) + "节&nbsp;-&nbsp;第" + String.format("%02d", Integer.parseInt(week[week_length-1])) 
					+ "节";
					coun1t++;
				}
				
				if(count > 0 && count%10 == 0){
					tr_string.append("</ul>\n</div>\n");
				}
				if(count == 0){
					tr_string.append("<div class=\"message show\">\n<ul>\n");
				}else if(count%10 == 0){
					tr_string.append("<div class=\"message\">\n<ul>\n");
				}
				tr_string.append("<li><a target=\"_new\" href=\"ScheduleServlet?method=export_pdf&id=" + rs.getInt("s_id") + "\">" + rs.getString("c_name") + "&nbsp;|&nbsp" + rs.getString("l_name") + "&nbsp;|&nbsp" + class_string + "&nbsp;|&nbsp" + week_string + "&nbsp;|&nbsp" +  rs.getString("term") + "</a></li>\n");
				count ++;
			}
			tr_string.append("</ul>\n</div>\n");
			tr_string.append("</div>\n");
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DatabaseUtil.close(rs);
			DatabaseUtil.close(pstmt);
			DatabaseUtil.close(conn);
		}
		return tr_string.toString();
	}
	
	public static int getTotalPages(int laboratoryID, String term, int pageSize){
		Connection conn = DatabaseUtil.getConn();
		ResultSet rsCount = null;
		PreparedStatement pstmt = null;
		int totalPages = 0;
		try {
			String countSQL = "select count(*) from _experiment where term = ? and laboratoryID = ?";
			pstmt = DatabaseUtil.prepareStmt(conn, countSQL);
			int i = 1;
			pstmt.setString(i++, term);
			pstmt.setInt(i++, laboratoryID);
			rsCount = pstmt.executeQuery();
			int totalRecords;
			rsCount.next();
			totalRecords = rsCount.getInt(1);
			totalPages = (totalRecords + pageSize - 1) / pageSize;
			if(totalPages == 0){
				totalPages = 1;
			}
		}catch (SQLException e) {
			e.printStackTrace();
		}finally{		
			DatabaseUtil.close(pstmt);
			DatabaseUtil.close(rsCount);
			DatabaseUtil.close(conn);	
		}
		return totalPages;
	}
	
	public static String index_list_obl(int laboratoryID, String term, int pageNo, int pageSize){
		Connection conn = DatabaseUtil.getConn();
		ResultSet rs = null;
		PreparedStatement pstmt = null;
		StringBuffer ex_string = new StringBuffer();
		try {
			int startPos = (pageNo-1) * pageSize; 
			String sql = "select distinct e.id, s.id as s_id, l.name as l_name, c.name as c_name, e.classInfo, e.timeInfo, e.term from _laboratory as l, _course as c, _experiment as e, _schedule as s where e.laboratoryID = l.id and e.courseID = c.id and e.id = s.experimentID and e.term = ? and e.laboratoryID = ? and e.state = 1 order by e.id desc limit ?, ?";
			pstmt = DatabaseUtil.prepareStmt(conn, sql);
			int i = 1;
			pstmt.setString(i++, term);
			pstmt.setInt(i++, laboratoryID);
			pstmt.setInt(i++, startPos);
			pstmt.setInt(i++, pageSize);
			rs = pstmt.executeQuery();
			if(rs.isAfterLast() == rs.isBeforeFirst()){
				ex_string.append("<span style=\"padding-left:10px;\">暂无实验安排</span>");
			}else{
				ex_string.append("<div class=\"message\">\n<ul>\n");
				while(rs.next()) {
					String classInfo[] = rs.getString("classInfo").split("@");
					String class_string = classInfo[0];
					for(int j=2; j<classInfo.length-1; j++){
						class_string += "&nbsp;" + classInfo[j++];
					}
					String time[] = rs.getString("timeInfo").split("@");
					String week_string = "";
					int count = 1;
					for(String tss : time){
						if(count!=1) week_string += "&nbsp;,&nbsp;";
						String week[] = tss.split(",");
						int week_length = week.length;
						if(week[0].equals("0")){
							week_string += "第" + week[1] + "周 - 第" + week[2] + "周 (" + week[3] + ")";
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
						week_string +=  "&nbsp;" + week[week_length-3] + "&nbsp;第" + String.format("%02d", Integer.parseInt(week[week_length-2])) + "节&nbsp;-&nbsp;第" + String.format("%02d", Integer.parseInt(week[week_length-1])) 
						+ "节";
						count++;
					}
					ex_string.append("<li><a target=\"_new\" href=\"ScheduleServlet?method=export_pdf&id=" + rs.getInt("s_id") + "\">" + rs.getString("c_name") + "&nbsp;|&nbsp" + class_string + "&nbsp;|&nbsp" + week_string + "&nbsp;|&nbsp" +  rs.getString("term") + "</a></li>\n");
				}	
				ex_string.append("</ul>\n</div>\n");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DatabaseUtil.close(conn);
			DatabaseUtil.close(pstmt);
			DatabaseUtil.close(rs);
		}
		return ex_string.toString();
	}
	
	//统一给序号赋值
	public static Map<Integer, String> updateXh(){
		 int count = 1;
		 int j=0;
		 Map<Integer, String> map = new HashMap<Integer, String>();
	  	 List<Laboratory> list =  LaboratoryDAO.load_laboratory();
			for (int i = 0; i < list.size(); i++) {
				Sysxx sysxx = LaboratoryDAO.load_sysxx(list.get(i).getId());
				String sysdm = sysxx.getSysdm();
				if(sysdm!=null && !"".equals(sysdm) && sysdm.trim().length() !=0){
					int num_sysdm = Integer.parseInt(sysdm);
					//获取数字型的实验序号
					num_sysdm = num_sysdm*1000;
					//获取一个实验室id的不同实验
					List<Experiment> experimentList = ExperimentDAO.load_experiment(list.get(i).getId());
					count = 1;
					
					for(j=0; j<experimentList.size(); j++){
						
						Experiment experiment = experimentList.get(j);
						
						Schedule schedule = ScheduleDAO
						.getScheduleByExperimentID(experiment.getId());
				
						List<ExItem> exltemList = ExItemDAO.loadExitem(schedule
								.getId());
						for (int i22 = 0; i22 < exltemList.size(); i22++) {
							
							count++;
							//根据递增逐渐添加序号数据  添加到map集合
							map.put(exltemList.get(i22).getId(),String.valueOf((num_sysdm+count)));
							System.out.println("id is " + exltemList.get(i22).getId());
							System.out.println("num is " + num_sysdm);
							System.out.println("count is " + count);
//							System.out.println("sysdm is " + num_sysdm+count);///////
						}
						
						
					}
				}
				
			}
			System.out.println("exit");
			return map;
	}
	
	public static ArrayList<Experiment> list_kssy(String course_id){
		Connection conn = DatabaseUtil.getConn();
		ResultSet rs = null;
		String sql = "select * from _experiment where courseID = ?";
		PreparedStatement pstmt = DatabaseUtil.prepareStmt(conn, sql);
		ArrayList<Experiment> experimentList = new ArrayList<Experiment>();
		try {
			pstmt.setString(1, course_id);
			rs = pstmt.executeQuery();
			while(rs.next()){
				Experiment experiment = new Experiment();
				ExperimentDAO.initFromRS(rs, experiment);
				experimentList.add(experiment);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DatabaseUtil.close(rs);
			DatabaseUtil.close(pstmt);
			DatabaseUtil.close(conn);
		}
		return experimentList;
	}
	
	private static List<Experiment> load_experiment(int id) {
		// TODO Auto-generated method stub
		
		Connection conn = DatabaseUtil.getConn();
		ResultSet rs = null;
		String sql = "select * from _experiment where laboratoryID = ?";
		PreparedStatement pstmt = DatabaseUtil.prepareStmt(conn, sql);
		ArrayList<Experiment> experimentList = new ArrayList<Experiment>();
		try {
			int i = 1;
			pstmt.setInt(i++, id);
			rs = pstmt.executeQuery();
			while(rs.next()){
				Experiment experiment = new Experiment();
				ExperimentDAO.initFromRS(rs, experiment);
				experimentList.add(experiment);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DatabaseUtil.close(rs);
			DatabaseUtil.close(pstmt);
			DatabaseUtil.close(conn);
		}
		return experimentList;
		
	}

	public static int loadNumber(int id) {
		// TODO Auto-generated method stub
		
		Connection conn = DatabaseUtil.getConn();
		ResultSet rs = null;
		String sql = "select * from number where schedule_id = ?";
		PreparedStatement pstmt = DatabaseUtil.prepareStmt(conn, sql);
		int count = 0;
		try {
			int i = 1;
			pstmt.setInt(i++, id);
			rs = pstmt.executeQuery();
			if(rs.next()){
				count = rs.getInt("count");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DatabaseUtil.close(rs);
			DatabaseUtil.close(pstmt);
			DatabaseUtil.close(conn);
		}
		return count;
	}
	
	
}
