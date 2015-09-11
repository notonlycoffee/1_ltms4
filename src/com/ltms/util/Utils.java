package com.ltms.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.ltms.model.Admin;

public class Utils {

	//获取专业名称
	public static String getZyMc(String dm){
		String mc="null";
		
		Connection conn = DatabaseUtil.getConn();
		ResultSet rs = null;
		String sql = "select * from zy where dm = ?";
		PreparedStatement pstmt = DatabaseUtil.prepareStmt(conn, sql);
		try {
			pstmt.setString(1, dm);
			rs = pstmt.executeQuery();
			if(rs.next()){
				mc = rs.getString("mc");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DatabaseUtil.close(rs);
			DatabaseUtil.close(pstmt);
			DatabaseUtil.close(conn);
		}
		
		return mc;
	}
	
	//获取专业名称
	public static String getZyl(String dm){
		String mc="";
		System.out.println("www");
		Connection conn = DatabaseUtil.getConn();
		ResultSet rs = null;
		String sql = "select * from zyl where dm = ?";
		PreparedStatement pstmt = DatabaseUtil.prepareStmt(conn, sql);
		try {
			pstmt.setString(1, dm);
			rs = pstmt.executeQuery();
			if(rs.next()){
				mc = rs.getString("mc");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DatabaseUtil.close(rs);
			DatabaseUtil.close(pstmt);
			DatabaseUtil.close(conn);
		}
		System.out.println("mc is " + mc);
		return mc;
	}
	
	//获取文化程度名称
	public static String getWhcdMc(String bh){
		String value = "null";
		
		Connection conn = DatabaseUtil.getConn();
		ResultSet rs = null;
		String sql = "select * from whcd where bh = ?";
		PreparedStatement pstmt = DatabaseUtil.prepareStmt(conn, sql);
		try {
			pstmt.setString(1, bh);
			rs = pstmt.executeQuery();
			if(rs.next()){
				value = rs.getString("value");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DatabaseUtil.close(rs);
			DatabaseUtil.close(pstmt);
			DatabaseUtil.close(conn);
		}
		
		return value;
		
	}
	
	
	//获取专业职务名称
	public static String getZwMc(String bh){
		String value = "null";
		
		Connection conn = DatabaseUtil.getConn();
		ResultSet rs = null;
		String sql = "select * from zyzc where bh = ?";
		PreparedStatement pstmt = DatabaseUtil.prepareStmt(conn, sql);
		try {
			pstmt.setString(1, bh);
			rs = pstmt.executeQuery();
			if(rs.next()){
				value = rs.getString("value");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DatabaseUtil.close(rs);
			DatabaseUtil.close(pstmt);
			DatabaseUtil.close(conn);
		}
		
		return value;
		
	}
}
