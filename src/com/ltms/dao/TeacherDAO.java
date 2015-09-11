package com.ltms.dao;

import com.ltms.model.Teacher;
import com.ltms.util.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TeacherDAO {
	public static void initFromRS(ResultSet rs, Teacher teacher){
		try {
			teacher.setId(rs.getString("id"));
			teacher.setUnitID(rs.getInt("unitID"));
			teacher.setName(rs.getString("name"));
			teacher.setPassword(rs.getString("password"));
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}	
	
	public static ArrayList<Teacher> list(){
		Connection conn = DatabaseUtil.getConn();
		ResultSet rs = null;
		String sql = "select * from _teacher order by convert(name using gbk) asc";
		PreparedStatement pstmt = DatabaseUtil.prepareStmt(conn, sql);
		ArrayList<Teacher> teacherList = new ArrayList<Teacher>();
		try {
			rs = pstmt.executeQuery();
			while(rs.next()){
				Teacher teacher = new Teacher();
				TeacherDAO.initFromRS(rs, teacher);
				teacherList.add(teacher);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DatabaseUtil.close(rs);
			DatabaseUtil.close(pstmt);
			DatabaseUtil.close(conn);
		}
		return teacherList;
	}
	public static ArrayList<Teacher> list(int unitID){
		Connection conn = DatabaseUtil.getConn();
		ResultSet rs = null;
		String sql = "select * from _teacher where unitID = ? order by convert(name using gbk) asc";
		PreparedStatement pstmt = DatabaseUtil.prepareStmt(conn, sql);
		ArrayList<Teacher> teacherList = new ArrayList<Teacher>();
		try {
			int i = 1;
			pstmt.setInt(i++, unitID);
			rs = pstmt.executeQuery();
			while(rs.next()){
				Teacher teacher = new Teacher();
				TeacherDAO.initFromRS(rs, teacher);
				teacherList.add(teacher);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DatabaseUtil.close(rs);
			DatabaseUtil.close(pstmt);
			DatabaseUtil.close(conn);
		}
		return teacherList;
	}
	
	public static String getName(String id){
		Connection conn = DatabaseUtil.getConn();
		ResultSet rs = null;
		String sql = "select name from _teacher where id = ?";
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
	
	public static Teacher load(String id){
		Connection conn = DatabaseUtil.getConn();
		ResultSet rs = null;
		String sql = "select * from _teacher where id = ?";
		PreparedStatement pstmt = DatabaseUtil.prepareStmt(conn, sql);
		Teacher t = new Teacher();
		try {
			int i = 1;
			pstmt.setString(i++, id);
			rs = pstmt.executeQuery();
			if(rs.next()){
				initFromRS(rs, t);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DatabaseUtil.close(rs);
			DatabaseUtil.close(pstmt);
			DatabaseUtil.close(conn);
		}
		return t;
	}
	
	public static boolean isExist(String id){
		boolean exist = false;
		int teacherID = 0;
		try{
			teacherID = Integer.parseInt(id);
		}catch (NumberFormatException e) {
			teacherID = 0;
		}
		Connection conn = DatabaseUtil.getConn();
		ResultSet rs = null;
		String sql = "select * from _teacher where id = ?";
		PreparedStatement pstmt = DatabaseUtil.prepareStmt(conn, sql);
		try {
			int i = 1;
			pstmt.setInt(i++, teacherID);
			rs = pstmt.executeQuery();
			if(rs.next()){
				exist = true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DatabaseUtil.close(rs);
			DatabaseUtil.close(pstmt);
			DatabaseUtil.close(conn);
		}
		return exist;
	}
	
	public static Map<String, String> getNameMap(int unitID){
		Connection conn = DatabaseUtil.getConn();
		ResultSet rs = null;
		String sql = "select id, name from _teacher where unitID = ?";
		PreparedStatement pstmt = DatabaseUtil.prepareStmt(conn, sql);
		Map<String, String> nameMap = new HashMap<String, String>();
		try {
			int i = 1;
			pstmt.setInt(i++, unitID);
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
	
	public static Map<String, String> getNameMap(){
		Connection conn = DatabaseUtil.getConn();
		ResultSet rs = null;
		String sql = "select id, name from _teacher";
		PreparedStatement pstmt = DatabaseUtil.prepareStmt(conn, sql);
		Map<String, String> nameMap = new HashMap<String, String>();
		try {
			int i = 1;
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
	
	public static ArrayList<String> getNameArray(String[] id_array){
		Connection conn = DatabaseUtil.getConn();
		ResultSet rs = null;
		String sql = "select name from _teacher where id in(?";
		for(int i=1; i<id_array.length; i+=2){
			sql += ",?";
		}
		sql += ")";
		PreparedStatement pstmt = DatabaseUtil.prepareStmt(conn, sql);
		ArrayList<String> nameArray = new ArrayList<String>();
		try {
			int i = 1;
			pstmt.setString(i++, "0");
			for(int j=1; j<id_array.length; j+=2){
				pstmt.setString(i++, id_array[j]);
			}
			rs = pstmt.executeQuery();
			while(rs.next()){
				nameArray.add(rs.getString("name"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DatabaseUtil.close(rs);
			DatabaseUtil.close(pstmt);
			DatabaseUtil.close(conn);
		}
		return nameArray;
	}
}
