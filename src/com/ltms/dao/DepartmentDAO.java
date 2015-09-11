package com.ltms.dao;

import com.ltms.model.Department;
import com.ltms.util.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DepartmentDAO {
	public static void initFromRS(ResultSet rs, Department department){
		try {
			department.setId(rs.getInt("id"));
			department.setName(rs.getString("name"));
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static ArrayList<Department> list(){
		Connection conn = DatabaseUtil.getConn();
		ResultSet rs = null;
		String sql = "select * from _department";
		PreparedStatement pstmt = DatabaseUtil.prepareStmt(conn, sql);
		ArrayList<Department> departmentList = new ArrayList<Department>();
		try {
			rs = pstmt.executeQuery();
			while(rs.next()){
				Department department = new Department();
				DepartmentDAO.initFromRS(rs, department);
				departmentList.add(department);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DatabaseUtil.close(rs);
			DatabaseUtil.close(pstmt);
			DatabaseUtil.close(conn);
		}
		return departmentList;
	}
	
	public static String getName(int id){
		Connection conn = DatabaseUtil.getConn();
		ResultSet rs = null;
		String sql = "select name from _department where id = ?";
		PreparedStatement pstmt = DatabaseUtil.prepareStmt(conn, sql);
		String name = "";
		try {
			int i = 1;
			pstmt.setInt(i++, id);
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
		String sql = "select * from _department";
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
}
