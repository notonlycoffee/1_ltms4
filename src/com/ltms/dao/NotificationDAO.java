package com.ltms.dao;

import com.ltms.model.Notification;
import com.ltms.util.*;

import java.sql.*;
import java.util.ArrayList;

public class NotificationDAO {
	public static void initFromRS(ResultSet rs, Notification notification){
		try {
			notification.setId(rs.getInt("id"));
			notification.setTitle(rs.getString("title"));
			notification.setContent(rs.getString("content"));
			notification.setDate(rs.getDate("date"));
			notification.setAuthor(rs.getString("author"));
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}	
	
	public static ArrayList<Notification> list(){
		Connection conn = DatabaseUtil.getConn();
		ResultSet rs = null;
		String sql = "select * from _notification order by date";
		PreparedStatement pstmt = DatabaseUtil.prepareStmt(conn, sql);
		ArrayList<Notification> notificationList = new ArrayList<Notification>();
		try {
			rs = pstmt.executeQuery();
			while(rs.next()){
				Notification notification = new Notification();
				NotificationDAO.initFromRS(rs, notification);
				notificationList.add(notification);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DatabaseUtil.close(rs);
			DatabaseUtil.close(pstmt);
			DatabaseUtil.close(conn);
		}
		return notificationList;
	}
	
	public static ArrayList<Notification> list(int count){
		Connection conn = DatabaseUtil.getConn();
		String sql = "select * from _notification order by date desc limit 0, ?";
		PreparedStatement pstmt = DatabaseUtil.prepareStmt(conn, sql);
		ResultSet rs = null;
		ArrayList<Notification> notificationList = new ArrayList<Notification>();
		try {
			int i = 1;
			pstmt.setInt(i++, count);
			rs = pstmt.executeQuery();
			while(rs.next()){
				Notification notification = new Notification();
				NotificationDAO.initFromRS(rs, notification);
				notificationList.add(notification);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DatabaseUtil.close(rs);
			DatabaseUtil.close(pstmt);
			DatabaseUtil.close(conn);
		}
		return notificationList;
	}
	
	public static Notification load(int id){
		Connection conn = DatabaseUtil.getConn();
		ResultSet rs = null;
		String sql = "select * from _notification where id = ?";
		PreparedStatement pstmt = DatabaseUtil.prepareStmt(conn, sql);
		Notification notification = new Notification();
		try {
			int i = 1;
			pstmt.setInt(i++, id);
			rs = pstmt.executeQuery();
			if(rs.next()){
				NotificationDAO.initFromRS(rs, notification);
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
		}finally{
			DatabaseUtil.close(rs);
			DatabaseUtil.close(pstmt);
			DatabaseUtil.close(conn);
		}
		return notification;
	}
}
