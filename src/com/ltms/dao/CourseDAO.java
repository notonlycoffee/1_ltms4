package com.ltms.dao;

import com.ltms.model.Course;
import com.ltms.model.Course_sy;
import com.ltms.model.Experiment;
import com.ltms.model.Schedule;
import com.ltms.util.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class CourseDAO {
	public static void initFromRS(ResultSet rs, Course course){
		try {
			course.setId(rs.getString("id"));
			course.setName(rs.getString("name"));
			course.setDepartmentID(rs.getInt("departmentID"));
			course.setType(rs.getString("type"));
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}	 
	
	public static void initFromRS_course_sy(ResultSet rs, Course_sy course){
		try {
			course.setId(rs.getString("id"));
			course.setName(rs.getString("name"));
			course.setDepartmentID(rs.getInt("departmentID"));
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}	 
	

	
	public static ArrayList<Course> list(){
		Connection conn = DatabaseUtil.getConn();
		ResultSet rs = null;
		String sql = "select * from _course";
		PreparedStatement pstmt = DatabaseUtil.prepareStmt(conn, sql);
		ArrayList<Course> courseList = new ArrayList<Course>();
		try {
			rs = pstmt.executeQuery();
			while(rs.next()){
				Course course = new Course();
				CourseDAO.initFromRS(rs, course);
				courseList.add(course);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DatabaseUtil.close(rs);
			DatabaseUtil.close(pstmt);
			DatabaseUtil.close(conn);
		}
		return courseList;
	}
	
	
	public static ArrayList<Course_sy> load_course_sy(int departmentID){
		Connection conn = DatabaseUtil.getConn();
		ResultSet rs = null;
		String sql = "select _course_sy.*,_course_sy_mes.* from _course_sy,_course_sy_mes " +
				"where _course_sy.id = _course_sy_mes.id and departmentID = ?";
		PreparedStatement pstmt = DatabaseUtil.prepareStmt(conn, sql);
		ArrayList<Course_sy> course_sy_List = new ArrayList<Course_sy>();
		try {
			pstmt.setInt(1, departmentID);
			rs = pstmt.executeQuery();
			while(rs.next()){
				Course_sy course_sy = new Course_sy();
				CourseDAO.initFromRS_course_sy(rs, course_sy);
				course_sy_List.add(course_sy);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DatabaseUtil.close(rs);
			DatabaseUtil.close(pstmt);
			DatabaseUtil.close(conn);
		}
		return course_sy_List;
	}
	
	
	public static ArrayList<Course_sy> load_course_sy(){
		Connection conn = DatabaseUtil.getConn();
		ResultSet rs = null;
		String sql = "select * from _course_sy";
		PreparedStatement pstmt = DatabaseUtil.prepareStmt(conn, sql);
		ArrayList<Course_sy> course_sy_List = new ArrayList<Course_sy>();
		try {
			rs = pstmt.executeQuery();
			while(rs.next()){
				Course_sy course_sy = new Course_sy();
				CourseDAO.initFromRS_course_sy(rs, course_sy);
				course_sy_List.add(course_sy);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DatabaseUtil.close(rs);
			DatabaseUtil.close(pstmt);
			DatabaseUtil.close(conn);
		}
		return course_sy_List;
	}
	
	public static Map<String, String> load_course_sy_mes(){
		Connection conn = DatabaseUtil.getConn();
		ResultSet rs = null;
		String sql = "select * from _course_sy_mes";
		Map<String, String> map = new HashMap<String, String>();
		PreparedStatement pstmt = DatabaseUtil.prepareStmt(conn, sql);
		try {
			rs = pstmt.executeQuery();
			while(rs.next()){
				map.put(rs.getString("id"), rs.getString("xgzy"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DatabaseUtil.close(rs);
			DatabaseUtil.close(pstmt);
			DatabaseUtil.close(conn);
		}
		return map;
	}
	
	public static String load_course_sy_mes(String course_id){
		Connection conn = DatabaseUtil.getConn();
		ResultSet rs = null;
		String sql = "select * from _course_sy_mes where id = ?";
		PreparedStatement pstmt = DatabaseUtil.prepareStmt(conn, sql);
		ArrayList<Course_sy> course_sy_List = new ArrayList<Course_sy>();
		String xgzy = "";
		try {
			pstmt.setString(1, course_id);
			rs = pstmt.executeQuery();
			rs.next();
			xgzy = rs.getString("xgzy");
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DatabaseUtil.close(rs);
			DatabaseUtil.close(pstmt);
			DatabaseUtil.close(conn);
		}
		return xgzy;
	}
	
	public static ArrayList<Course> list(int departmentID){
		Connection conn = DatabaseUtil.getConn();
		ResultSet rs = null;
		String sql = "select * from _course where departmentID = ?";
		PreparedStatement pstmt = DatabaseUtil.prepareStmt(conn, sql);
		ArrayList<Course> courseList = new ArrayList<Course>();
		try {
			int i = 1;
			pstmt.setInt(i++, departmentID);
			rs = pstmt.executeQuery();
			while(rs.next()){
				Course course = new Course();
				CourseDAO.initFromRS(rs, course);
				courseList.add(course);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DatabaseUtil.close(rs);
			DatabaseUtil.close(pstmt);
			DatabaseUtil.close(conn);
		}
		return courseList;
	}
	
	public static String getName(String id){
		Connection conn = DatabaseUtil.getConn();
		ResultSet rs = null;
		String sql = "select name from _course where id = ?";
		PreparedStatement pstmt = DatabaseUtil.prepareStmt(conn, sql);
		String name = "";
		try {
			int i = 1;
			pstmt.setString(i++, id);
			rs = pstmt.executeQuery();
			if(rs.next()){
				name = rs.getString("name");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DatabaseUtil.close(rs);
			DatabaseUtil.close(pstmt);
			DatabaseUtil.close(conn);
		}
		return name;
	}
	
	public static Map<Integer, String> getNameMap(){
		Connection conn = DatabaseUtil.getConn();
		ResultSet rs = null;
		String sql = "select * from _course";
		PreparedStatement pstmt = DatabaseUtil.prepareStmt(conn, sql);
		Map<Integer, String> nameMap = new HashMap<Integer, String>();
		try {
			rs = pstmt.executeQuery();
			while(rs.next()){
				nameMap.put(rs.getInt("id"), rs.getString("name"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DatabaseUtil.close(rs);
			DatabaseUtil.close(pstmt);
			DatabaseUtil.close(conn);
		}
		return nameMap;
	}
	
	public static Map<Integer, String> getNameMap(List<Schedule> scheduleList){
		Connection conn = DatabaseUtil.getConn();
		ResultSet rs = null;
		String sql = "select c.name, e.id from _course c join _experiment e on c.id = e.courseID where e.id in (?";
		for(int i=0; i<scheduleList.size(); i++){
			sql += ", ?";
		}
		sql += ")";
		PreparedStatement pstmt = DatabaseUtil.prepareStmt(conn, sql);
		Map<Integer, String> nameMap = new HashMap<Integer, String>();
		try {
			int i = 1;
			pstmt.setInt(i++, 0);
			for(Schedule sc : scheduleList){
				pstmt.setInt(i++, sc.getExperimentID());
			}
			rs = pstmt.executeQuery();
			while(rs.next()){
				nameMap.put(rs.getInt("e.id"), rs.getString("c.name"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DatabaseUtil.close(rs);
			DatabaseUtil.close(pstmt);
			DatabaseUtil.close(conn);
		}
		return nameMap;
	}

	public static void updata_xgzy(String id, String sykcXgzy) {
		// TODO Auto-generated method stub
		Connection conn = DatabaseUtil.getConn();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "update _course_sy_mes set xgzy = ? where id = ?";
		
		pstmt = DatabaseUtil.prepareStmt(conn, sql);
		int i = 1;
		
		try {
			pstmt.setString(i++, sykcXgzy);
			pstmt.setString(i++, id);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			DatabaseUtil.close(rs);
			DatabaseUtil.close(pstmt);
			DatabaseUtil.close(rs);
		}
		
	}
	
}
