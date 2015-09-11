package com.ltms.dao;

import com.ltms.model.ExItem;
import com.ltms.util.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExItemDAO {
	public static void initFromRS(ResultSet rs, ExItem exItem){
		try {
			exItem.setId(rs.getInt("id"));
			exItem.setScheduleID(rs.getInt("scheduleID"));
			exItem.setWeek(rs.getString("week"));
			exItem.setItemName(rs.getString("itemName"));
			exItem.setComment(rs.getString("comment"));
			exItem.setLaboratoryID(rs.getInt("laboratoryID"));
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}	
	
	public static ArrayList<ExItem> list(int scheduleID){
		Connection conn = DatabaseUtil.getConn();
		ResultSet rs = null;
		String sql = "select * from _exItem where ScheduleID = ?";
		PreparedStatement pstmt = DatabaseUtil.prepareStmt(conn, sql);
		ArrayList<ExItem> exItemList = new ArrayList<ExItem>();
		try {
			int i = 1;
			pstmt.setInt(i++, scheduleID);
			rs = pstmt.executeQuery();
			while(rs.next()){
				ExItem exItem = new ExItem();
				ExItemDAO.initFromRS(rs, exItem);
				exItemList.add(exItem);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DatabaseUtil.close(rs);
			DatabaseUtil.close(pstmt);
			DatabaseUtil.close(conn);
		}
		return exItemList;
	}
	
	public static Map<Integer, String> getType(int scheduleID){
		Connection conn = DatabaseUtil.getConn();
		ResultSet rs = null;
		String sql = "select * from _exItem where ScheduleID = ?";
		PreparedStatement pstmt = DatabaseUtil.prepareStmt(conn, sql);
		
		Map<Integer, String> typeMap = new HashMap<Integer, String>();
		
		try {
			int i = 1;
			pstmt.setInt(i++, scheduleID);
			rs = pstmt.executeQuery();
			while(rs.next()){
				int id = rs.getInt("id");
				String type = rs.getString("type");
				typeMap.put(id, type);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DatabaseUtil.close(rs);
			DatabaseUtil.close(pstmt);
			DatabaseUtil.close(conn);
		}
		return typeMap;
	}
	
	public static ArrayList<ExItem> indexList(int size){
		
		Calendar c = Calendar.getInstance();
		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH);  //从0开始
		String term_ = "";
		if(month > 7){
			//今年-明年  第一学期  2011-2012学年度第一学期
			term_ = year+"-"+(year+1)+"学年度第一学期";
		}else{
			//去年-今年 第二学期
			term_ = (year-1)+"-"+year+"学年度第二学期";
		}
		
		Connection conn = DatabaseUtil.getConn();
		ResultSet rs = null;
//		String sql = "select * from _exItem,_schedule where _exItem.scheduleID = _schedule.id and _exItem.itemName != '' and _schedule.term like ? order by _exItem.id desc limit 0, ?";
		String sql = "select * from _exItem where itemName != '' order by id desc limit 0, ?";
		PreparedStatement pstmt = DatabaseUtil.prepareStmt(conn, sql);
		ArrayList<ExItem> exItemList = new ArrayList<ExItem>();
		try {
			int i = 1;
//			pstmt.setString(i++, "%" + term_ + "%");
			pstmt.setInt(i++, size);
			rs = pstmt.executeQuery();
			while(rs.next()){
				ExItem exItem = new ExItem();
				ExItemDAO.initFromRS(rs, exItem);
				exItemList.add(exItem);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DatabaseUtil.close(rs);
			DatabaseUtil.close(pstmt);
			DatabaseUtil.close(pstmt);
			DatabaseUtil.close(conn);
		}
		return exItemList;
	}
	
	public static Map<Integer, String> getLaboratoryNameMap(ArrayList<ExItem> exItemList){
		Map<Integer, String> laboratoryNameMap = new HashMap<Integer, String>();
		Connection conn = DatabaseUtil.getConn();
		String sql = "";
		ResultSet rs = null;
		PreparedStatement pstmt = null;
		try {
			int listLen = exItemList.size();
			sql = "select id, name from _laboratory where id in (?";
			for(int i = 0; i< listLen; i++){
				sql += ", ?";
			}
			sql += ")";
			pstmt = DatabaseUtil.prepareStmt(conn, sql);
			pstmt.setString(1, "0");
			for(int i = 0; i< listLen; i++){
				pstmt.setInt(i+2, exItemList.get(i).getLaboratoryID());
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

	public static ExItem load(int id) {
		Connection conn = DatabaseUtil.getConn();
		String sql = "select * from _exItem where id = ?";
		PreparedStatement pstmt = DatabaseUtil.prepareStmt(conn, sql);
		ResultSet rs = null;
		ExItem exItem = new ExItem();
		try {
			int i = 1;
			pstmt.setInt(i++, id);
			rs = pstmt.executeQuery();
			if(rs.next()){
				initFromRS(rs, exItem);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DatabaseUtil.close(rs);
			DatabaseUtil.close(pstmt);
			DatabaseUtil.close(conn);
		}
		return exItem;
	}
	
	public static List<ExItem> loadExitem(int scheduleID) {
		Connection conn = DatabaseUtil.getConn();
		String sql = "select * from _exItem where scheduleID = ? and itemName != ''"; 
		PreparedStatement pstmt = DatabaseUtil.prepareStmt(conn, sql);
		ResultSet rs = null;
		List<ExItem> list = new ArrayList<ExItem>();
		try {
			int i = 1;
			pstmt.setInt(i++, scheduleID);
			rs = pstmt.executeQuery();
			while(rs.next()){
				ExItem exItem = new ExItem();
				initFromRS(rs, exItem);
				list.add(exItem);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DatabaseUtil.close(rs);
			DatabaseUtil.close(pstmt);
			DatabaseUtil.close(conn);
		}
		return list;
	}

	public static Integer loadXmByDepa(int id, String term) {
		// TODO Auto-generated method stub
		
		Connection conn = DatabaseUtil.getConn();
		ResultSet rs = null;
		String sql = "select count(*) from _exitem e,_schedule s,_laboratory l where e.laboratoryID = l.id and e.scheduleID = s.id and s.term like '%" + term +"%' and l.departmentID = ? and itemName != ''";
		PreparedStatement pstmt = DatabaseUtil.prepareStmt(conn, sql);
		int size = 0;
		try {
			pstmt.setInt(1, id);
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
}
