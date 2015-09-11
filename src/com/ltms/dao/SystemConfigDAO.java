package com.ltms.dao;

import java.sql.*;

import com.ltms.model.SystemConfig;
import com.ltms.util.DatabaseUtil;

public class SystemConfigDAO {
	public static void initFromRS(ResultSet rs, SystemConfig systemConfig){
		try {
			systemConfig.setYear(rs.getString("year"));
			systemConfig.setTerm(rs.getString("term"));
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}	
	
	public static SystemConfig load(String name,int role){
		Connection conn = DatabaseUtil.getConn();
		String sql = "select * from _systemConfig where user_name = ? and role = ?";
		PreparedStatement pstmt = DatabaseUtil.prepareStmt(conn, sql);
		ResultSet rs = null;
		SystemConfig systemConfig = new SystemConfig();
		try {
			int i = 1;
			pstmt.setString(i++, name);
			pstmt.setInt(i++, role);
			rs = pstmt.executeQuery();
			if(rs.next()){
				SystemConfigDAO.initFromRS(rs, systemConfig);
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
		}finally{
			DatabaseUtil.close(rs);
			DatabaseUtil.close(pstmt);
			DatabaseUtil.close(conn);
		}
		return systemConfig;
	}
	
	public static String getCurrenTerm(String name,int role){
		Connection conn = DatabaseUtil.getConn();
		String sql = "select * from _systemConfig where user_name = ? and role = ?";
		PreparedStatement pstmt = DatabaseUtil.prepareStmt(conn, sql);
		ResultSet rs = null;
		String currenTerm = "";
		try {
			int i = 1;
			pstmt.setString(i++, name);
			pstmt.setInt(i++, role);
			rs = pstmt.executeQuery();
			if(rs.next()){
				currenTerm = rs.getString("year") + rs.getString("term");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DatabaseUtil.close(rs);
			DatabaseUtil.close(pstmt);
			DatabaseUtil.close(conn);
		}
		return currenTerm;
	}
	public static String getJxzs_fjsj(String name,int role){
		Connection conn = DatabaseUtil.getConn();
		String sql = "select * from jxzs_fjsj where user_name = ? and role = ?";
		PreparedStatement pstmt = DatabaseUtil.prepareStmt(conn, sql);
		ResultSet rs = null;
		String currenTerm = "";
		try {
			int i = 1;
			pstmt.setString(i++, name);
			pstmt.setInt(i++, role);
			rs = pstmt.executeQuery();
			if(rs.next()){
				currenTerm = rs.getString("jxzs") +"_"+ rs.getString("fjsj");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DatabaseUtil.close(rs);
			DatabaseUtil.close(pstmt);
			DatabaseUtil.close(conn);
		}
		return currenTerm;
	}
}
