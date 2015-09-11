package com.ltms.dao;

import com.ltms.model.SystemConfig;
import com.ltms.model._Class;
import com.ltms.util.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class ClassDAO {
	public static void initFromRS(ResultSet rs, _Class clazz){
		try {
			clazz.setId(rs.getString("id"));
			clazz.setName(rs.getString("name"));
			clazz.setGrade(rs.getInt("grade"));
			clazz.setStudentNumber(rs.getInt("studentNumber"));
			clazz.setDepartmentID(rs.getInt("departmentID"));
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}	
	
	public static ArrayList<_Class> list(){
		Connection conn = DatabaseUtil.getConn();
		ResultSet rs = null;
		
		int maxGrade ;
		
		Calendar c = Calendar.getInstance();
		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH);  //从0开始
		if(month > 7){
			maxGrade = year - 3;
		}else{
			maxGrade = year - 4;
		}
		
		String sql = "select * from _class where grade >= ? order by id";
		PreparedStatement pstmt = DatabaseUtil.prepareStmt(conn, sql);
		ArrayList<_Class> classList = new ArrayList<_Class>();
		try {
			int i = 1;
			pstmt.setInt(i++, maxGrade);
			rs = pstmt.executeQuery();
			while(rs.next()){
				_Class clazz = new _Class();
				ClassDAO.initFromRS(rs, clazz);
				classList.add(clazz);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DatabaseUtil.close(rs);
			DatabaseUtil.close(pstmt);
			DatabaseUtil.close(conn);
		}
		return classList;
	}
	
	public static ArrayList<_Class> list(int departmentID){
		Connection conn = DatabaseUtil.getConn();
		ResultSet rs = null;
		int maxGrade ;
		Calendar c = Calendar.getInstance();
		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH);  //从0开始
		if(month > 7){
			maxGrade = year - 4;
		}else{
			maxGrade = year - 4;
		}
		
		String sql = "select * from _class where departmentID = ? and grade >= ? order by id";
		PreparedStatement pstmt = DatabaseUtil.prepareStmt(conn, sql);
		ArrayList<_Class> classList = new ArrayList<_Class>();
		try {
			int i = 1;
			pstmt.setInt(i++, departmentID);
			pstmt.setInt(i++, maxGrade);
			rs = pstmt.executeQuery();
			while(rs.next()){
				_Class clazz = new _Class();
				ClassDAO.initFromRS(rs, clazz);
				classList.add(clazz);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DatabaseUtil.close(rs);
			DatabaseUtil.close(pstmt);
			DatabaseUtil.close(conn);
		}
		return classList;
	}
	
	public static String getName(String id){
		Connection conn = DatabaseUtil.getConn();
		ResultSet rs = null;
		String sql = "select name from _class where id = ?";
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
	
	public static _Class load(String id){
		Connection conn = DatabaseUtil.getConn();
		ResultSet rs = null;
		String sql = "select * from _class where id = ?";
		PreparedStatement pstmt = DatabaseUtil.prepareStmt(conn, sql);
		_Class c = new _Class();
		try {
			int i = 1;
			pstmt.setString(i++, id);
			rs = pstmt.executeQuery();
			if(rs.next()){
				initFromRS(rs, c);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DatabaseUtil.close(rs);
			DatabaseUtil.close(pstmt);
			DatabaseUtil.close(conn);
		}
		return c;
	}
	
	public static Map<String, String> getNameMap(int departmentID){
		Connection conn = DatabaseUtil.getConn();
		ResultSet rs = null;
		String sql = "select id, name from _class where departmentID = ?";
		PreparedStatement pstmt = DatabaseUtil.prepareStmt(conn, sql);
		Map<String, String> nameMap = new HashMap<String, String>();
		try {
			int i = 1;
			pstmt.setInt(i++, departmentID);
			rs = pstmt.executeQuery();
			while(rs.next()){
				nameMap.put(rs.getString("id"), rs.getString("name"));
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
}
