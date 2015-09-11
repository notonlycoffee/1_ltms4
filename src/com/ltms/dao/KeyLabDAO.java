package com.ltms.dao;

import com.ltms.model.KeyLab;
import com.ltms.util.*;

import java.sql.*;
import java.util.ArrayList;

public class KeyLabDAO {
	public static void initFromRS(ResultSet rs, KeyLab keyLab){
		try {
			keyLab.setId(rs.getInt("id"));
			keyLab.setName(rs.getString("name"));
			keyLab.setGotoURL(rs.getString("gotoURL"));
			keyLab.setSort(rs.getInt("sort"));
			keyLab.setPic(rs.getString("pic"));
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}	
	
	public static ArrayList<KeyLab> list(){
		Connection conn = DatabaseUtil.getConn();
		ResultSet rs = null;
		String sql = "select * from _keyLab order by sort";
		PreparedStatement pstmt = DatabaseUtil.prepareStmt(conn, sql);
		ArrayList<KeyLab> keyLabList = new ArrayList<KeyLab>();
		try {
			rs = pstmt.executeQuery();
			while(rs.next()){
				KeyLab keyLab = new KeyLab();
				KeyLabDAO.initFromRS(rs, keyLab);
				keyLabList.add(keyLab);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DatabaseUtil.close(rs);
			DatabaseUtil.close(pstmt);
			DatabaseUtil.close(conn);
		}
		return keyLabList;
	}
	
	public static String getName(int id){
		Connection conn = DatabaseUtil.getConn();
		ResultSet rs = null;
		String sql = "select name from _keyLab where id = ?";
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
}
