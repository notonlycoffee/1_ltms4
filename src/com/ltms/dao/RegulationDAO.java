package com.ltms.dao;

import com.ltms.model.Regulation;
import com.ltms.util.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RegulationDAO {
	public static void initFromRS(ResultSet rs, Regulation regulation){
		try {
			regulation.setId(rs.getInt("id"));
			regulation.setTitle(rs.getString("title"));
			regulation.setContent(rs.getString("content"));
			regulation.setDate(rs.getDate("date"));
			regulation.setAuthor(rs.getString("author"));
			regulation.setDepartmentID(rs.getInt("departmentID"));
			regulation.setLaboratoryID(rs.getInt("laboratoryID"));
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}	
	
	public static ArrayList<Regulation> list(){
		Connection conn = DatabaseUtil.getConn();
		ResultSet rs = null;
		String sql = "select * from _regulation order by date";
		PreparedStatement pstmt = DatabaseUtil.prepareStmt(conn, sql);
		ArrayList<Regulation> regulationList = new ArrayList<Regulation>();
		try {
			rs = pstmt.executeQuery();
			while(rs.next()){
				Regulation regulation = new Regulation();
				RegulationDAO.initFromRS(rs, regulation);
				regulationList.add(regulation);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DatabaseUtil.close(rs);
			DatabaseUtil.close(pstmt);
			DatabaseUtil.close(conn);
		}
		return regulationList;
	}
	
	public static ArrayList<Regulation> list(int count){
		Connection conn = DatabaseUtil.getConn();
		String sql = "select * from _regulation order by date desc limit 0, ?";
		PreparedStatement pstmt = DatabaseUtil.prepareStmt(conn, sql);
		ResultSet rs = null;
		ArrayList<Regulation> regulationList = new ArrayList<Regulation>();
		try {
			int i = 1;
			pstmt.setInt(i++, count);
			rs = pstmt.executeQuery();
			while(rs.next()){
				Regulation regulation = new Regulation();
				RegulationDAO.initFromRS(rs, regulation);
				regulationList.add(regulation);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DatabaseUtil.close(rs);
			DatabaseUtil.close(pstmt);
			DatabaseUtil.close(conn);
		}
		return regulationList;
	}
	
	public static Regulation load(int id){
		Connection conn = DatabaseUtil.getConn();
		ResultSet rs = null;
		String sql = "select * from _regulation where id = ?";
		PreparedStatement pstmt = DatabaseUtil.prepareStmt(conn, sql);
		Regulation regulation = new Regulation();
		try {
			int i = 1;
			pstmt.setInt(i++, id);
			rs = pstmt.executeQuery();
			if(rs.next()){
				RegulationDAO.initFromRS(rs, regulation);
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
		}finally{
			DatabaseUtil.close(rs);
			DatabaseUtil.close(pstmt);
			DatabaseUtil.close(conn);
		}
		return regulation;
	}
	
	public static int loadbyDepa(int departmentID){
		Connection conn = DatabaseUtil.getConn();
		ResultSet rs = null;
		String sql = "select count(*) from _regulation where departmentID = ?";
		PreparedStatement pstmt = DatabaseUtil.prepareStmt(conn, sql);
		int size = 0;
		try {
			pstmt.setInt(1, departmentID);
			rs = pstmt.executeQuery();
			if(rs.next()){
				size = rs.getInt(1);
			}
			
		} catch (SQLException ex) {
			ex.printStackTrace();
		}finally{
			DatabaseUtil.close(rs);
			DatabaseUtil.close(pstmt);
			DatabaseUtil.close(conn);
		}
		return size;
	}
	
	public static Map<Integer, String> getLaboratoryNameMap(ArrayList<Regulation> regulationList){
		Map<Integer, String> laboratoryNameMap = new HashMap<Integer, String>();
		Connection conn = DatabaseUtil.getConn();
		String sql = "";
		ResultSet rs = null;
		PreparedStatement pstmt = null;
		try {
			int listLen = regulationList.size();
			sql = "select id, name from _laboratory where id in (?";
			for(int i = 0; i< listLen; i++){
				sql += ", ?";
			}
			sql += ")";
			pstmt = DatabaseUtil.prepareStmt(conn, sql);
			pstmt.setString(1, "0");
			for(int i = 0; i< listLen; i++){
				pstmt.setInt(i+2, regulationList.get(i).getLaboratoryID());
			}
			rs = pstmt.executeQuery();
			while(rs.next()){
				laboratoryNameMap.put(rs.getInt("id"), rs.getString("name"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DatabaseUtil.close(rs);
			DatabaseUtil.close(pstmt);
			DatabaseUtil.close(conn);
		}
		return laboratoryNameMap;
	}
}
