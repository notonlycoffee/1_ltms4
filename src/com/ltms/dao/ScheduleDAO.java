package com.ltms.dao;

import com.ltms.model.Schedule;
import com.ltms.util.*;

import java.sql.*;
import java.util.ArrayList;

public class ScheduleDAO {
	public static void initFromRS(ResultSet rs, Schedule schedule){
		try {
			schedule.setId(rs.getInt("id"));
			schedule.setExperimentID(rs.getInt("experimentID"));
			schedule.setClassID(rs.getString("classID"));
			schedule.setTeacherID(rs.getString("teacherID"));
			schedule.setGroupNum(rs.getInt("groupNum"));
			schedule.setPurpose(rs.getString("purpose"));
			schedule.setMaterial(rs.getString("material"));
			schedule.settGrade(rs.getString("tGrade"));
			schedule.settTerm(rs.getString("tTerm"));
			schedule.setDemand(rs.getString("demand"));
			schedule.setTechWeek(rs.getInt("techWeek"));
			schedule.setWeekTime(rs.getInt("weekTime"));
			schedule.setTotalTime(rs.getInt("totalTime"));
			schedule.setExTime(rs.getInt("exTime"));
			schedule.setTerm(rs.getString("term"));
			schedule.setTheoTime(rs.getInt("theoTime"));
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}	
	
	public static ArrayList<Schedule> list(){
		Connection conn = DatabaseUtil.getConn();
		ResultSet rs = null;
		String sql = "select * from _schedule";
		PreparedStatement pstmt = DatabaseUtil.prepareStmt(conn, sql);
		ArrayList<Schedule> scheduleList = new ArrayList<Schedule>();
		try {
			rs = pstmt.executeQuery();
			while(rs.next()){
				Schedule schedule = new Schedule();
				ScheduleDAO.initFromRS(rs, schedule);
				scheduleList.add(schedule);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DatabaseUtil.close(rs);
			DatabaseUtil.close(pstmt);
			DatabaseUtil.close(conn);
		}
		return scheduleList;
	}
	
	public static Schedule getScheduleByExperimentID(int experimentID){
		Connection conn = DatabaseUtil.getConn();
		ResultSet rs = null;
		String sql = "select * from _schedule where experimentID = ?";
		PreparedStatement pstmt = DatabaseUtil.prepareStmt(conn, sql);
		Schedule schedule = new Schedule();
		try {
			pstmt.setInt(1, experimentID);
			rs = pstmt.executeQuery();
			if(rs.next()){
				ScheduleDAO.initFromRS(rs, schedule);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DatabaseUtil.close(rs);
			DatabaseUtil.close(pstmt);
			DatabaseUtil.close(conn);
		}
		return schedule;
	}
	
	public static ArrayList<Schedule> list(String teacherID){
		Connection conn = DatabaseUtil.getConn();
		ResultSet rs = null;
		String sql = "select * from _schedule where teacherID = ?";
		PreparedStatement pstmt = DatabaseUtil.prepareStmt(conn, sql);
		ArrayList<Schedule> scheduleList = new ArrayList<Schedule>();
		try {
			int i = 1;
			pstmt.setString(i++, teacherID);
			rs = pstmt.executeQuery();
			while(rs.next()){
				Schedule schedule = new Schedule();
				ScheduleDAO.initFromRS(rs, schedule);
				scheduleList.add(schedule);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DatabaseUtil.close(rs);
			DatabaseUtil.close(pstmt);
			DatabaseUtil.close(conn);
		}
		return scheduleList;
	}
	
	public static String getTeacherID(int id){
		String teacherID = "";
		Connection conn = DatabaseUtil.getConn();
		String sql = "select teacherID from _schedule where id = ?";
		PreparedStatement pstmt = DatabaseUtil.prepareStmt(conn, sql);
		ResultSet rs = null;
		try {
			int i = 1;
			pstmt.setInt(i++, id);
			rs = pstmt.executeQuery();
			if(rs.next()){
				teacherID = rs.getString("teacherID");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DatabaseUtil.close(rs);
			DatabaseUtil.close(pstmt);
			DatabaseUtil.close(conn);
		}
		return teacherID;
	}

	public static Schedule load(int id) {
		Connection conn = DatabaseUtil.getConn();
		String sql = "select * from _schedule where id = ?";
		PreparedStatement pstmt = DatabaseUtil.prepareStmt(conn, sql);
		ResultSet rs = null;
		Schedule schedule = new Schedule();
		try {
			int i = 1;
			pstmt.setInt(i++, id);
			rs = pstmt.executeQuery();
			if(rs.next()){
				initFromRS(rs, schedule);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DatabaseUtil.close(rs);
			DatabaseUtil.close(pstmt);
			DatabaseUtil.close(conn);
		}
		return schedule;
	}
}
