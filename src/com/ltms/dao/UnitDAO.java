package com.ltms.dao;

import com.ltms.model.Unit;
import com.ltms.util.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class UnitDAO {
	public static void initFromRS(ResultSet rs, Unit unit){
		try {
			unit.setId(rs.getInt("id"));
			unit.setName(rs.getString("name"));
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static ArrayList<Unit> list(){
		Connection conn = DatabaseUtil.getConn();
		ResultSet rs = null;
		String sql = "select * from _unit";
		PreparedStatement pstmt = DatabaseUtil.prepareStmt(conn, sql);
		ArrayList<Unit> unitList = new ArrayList<Unit>();
		try {
			rs = pstmt.executeQuery();
			while(rs.next()){
				Unit unit = new Unit();
				UnitDAO.initFromRS(rs, unit);
				unitList.add(unit);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DatabaseUtil.close(rs);
			DatabaseUtil.close(pstmt);
			DatabaseUtil.close(conn);
		}
		return unitList;
	}
	
	public static String getName(int id){
		Connection conn = DatabaseUtil.getConn();
		ResultSet rs = null;
		String sql = "select name from _unit where id = ?";
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
		String sql = "select * from _unit";
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
