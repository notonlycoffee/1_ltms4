package com.ltms.dao;

import com.ltms.model.Log;
import com.ltms.util.*;

import java.sql.*;
import java.util.ArrayList;

public class LogDAO {
	public static void initFromRS(ResultSet rs, Log log){
		try {
			log.setId(rs.getInt("id"));
			log.setOperator(rs.getString("operator"));
			log.setEvent(rs.getString("event"));
			log.setDadatetime(rs.getTimestamp("datetime"));
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}	
	
	public static void add(String operator, String event){
		Connection conn = DatabaseUtil.getConn();
		String sql = "insert into _log values(null, ?, ?, now())";
		PreparedStatement pstmt = DatabaseUtil.prepareStmt(conn, sql);
		try {
			int i = 1;
			pstmt.setString(i++, operator);
			pstmt.setString(i++, event);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DatabaseUtil.close(pstmt);
			DatabaseUtil.close(conn);
		}
	}
	
	public static ArrayList<Log> list(){
		Connection conn = DatabaseUtil.getConn();
		ResultSet rs = null;
		String sql = "select * from _log";
		PreparedStatement pstmt = DatabaseUtil.prepareStmt(conn, sql);
		ArrayList<Log> logList = new ArrayList<Log>();
		try {
			rs = pstmt.executeQuery();
			while(rs.next()){
				Log log = new Log();
				LogDAO.initFromRS(rs, log);
				logList.add(log);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DatabaseUtil.close(rs);
			DatabaseUtil.close(pstmt);
			DatabaseUtil.close(conn);
		}
		return logList;
	}
	
	public static Log load(int id){
		Connection conn = DatabaseUtil.getConn();
		ResultSet rs = null;
		String sql = "select * from _log where id = ?";
		PreparedStatement pstmt = DatabaseUtil.prepareStmt(conn, sql);
		Log log = new Log();
		try {
			int i = 1;
			pstmt.setInt(i++, id);
			rs = pstmt.executeQuery();
			if(rs.next()){
				LogDAO.initFromRS(rs, log);
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
		}finally{
			DatabaseUtil.close(rs);
			DatabaseUtil.close(pstmt);
			DatabaseUtil.close(conn);
		}
		return log;
	}
}
