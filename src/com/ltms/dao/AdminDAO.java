package com.ltms.dao;

import com.ltms.model.Admin;
import com.ltms.model.Sys_Admin;
import com.ltms.util.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdminDAO {
	public static void initFromRS(ResultSet rs, Admin admin){
		try {
			admin.setId(rs.getString("id"));
			admin.setName(rs.getString("name"));
			admin.setPassword(rs.getString("password"));
			admin.setDepartmentID(rs.getInt("departmentID"));
			admin.setRole(rs.getInt("role"));
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void initFromRS_sysry(ResultSet rs, Sys_Admin admin){
		try {
			admin.setId(rs.getString("id"));
			admin.setBh(rs.getString("bh"));
			admin.setBirthday(rs.getString("birthday"));
			admin.setBz(rs.getString("bz"));
			admin.setGnfxljy(rs.getInt("gnfxljy"));
			admin.setGnxljy(rs.getInt("gnxljy"));
			admin.setGwfxljy(rs.getInt("gwfxljy"));
			admin.setGwxljy(rs.getInt("gwxljy"));
			admin.setName(rs.getString("name"));
			admin.setRylb(rs.getInt("rylb"));
			admin.setSex(rs.getInt("sex"));
			admin.setSxzy(rs.getString("sxzy"));
			admin.setWhcd(rs.getInt("whcd"));
			admin.setZjlx(rs.getInt("zjlx"));
			admin.setZyzw(rs.getString("zyzw"));
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	
	//根据id判断是否存在
	public static boolean isExist(String id){
		boolean exist = false;
		Connection conn = DatabaseUtil.getConn();
		ResultSet rs = null;
		String sql = "select * from _admin where id = ?";
		PreparedStatement pstmt = DatabaseUtil.prepareStmt(conn, sql);
		try {
			int i = 1;
			pstmt.setString(i++, id);
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
	
	public static boolean isExist(String id,int role){
		boolean exist = false;
		Connection conn = DatabaseUtil.getConn();
		ResultSet rs = null;
		String sql = "select * from _admin where id = ? and role = ?";
		PreparedStatement pstmt = DatabaseUtil.prepareStmt(conn, sql);
		try {
			int i = 1;
			pstmt.setString(i++, id);
			pstmt.setInt(i++, role);
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
	
	//获取文化程度
	public static Map<String, String> loadWhcd(){
		Map<String, String> map = new HashMap<String, String>();
		
		Connection conn = DatabaseUtil.getConn();
		ResultSet rs = null;
		String sql = "select * from whcd";
		PreparedStatement pstmt = DatabaseUtil.prepareStmt(conn, sql);
		try {
			rs = pstmt.executeQuery();
			while(rs.next()){
				map.put(rs.getString("bh"), rs.getString("value"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DatabaseUtil.close(rs);
			DatabaseUtil.close(pstmt);
			DatabaseUtil.close(conn);
		}
		
		
		return map;
	}
	//获取专业职称
	public static Map<String, String> loadZyzc(){
		Map<String, String> map = new HashMap<String, String>();
		
		Connection conn = DatabaseUtil.getConn();
		ResultSet rs = null;
		String sql = "select * from zyzc";
		PreparedStatement pstmt = DatabaseUtil.prepareStmt(conn, sql);
		try {
			rs = pstmt.executeQuery();
			while(rs.next()){
				map.put(rs.getString("bh"), rs.getString("value"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DatabaseUtil.close(rs);
			DatabaseUtil.close(pstmt);
			DatabaseUtil.close(conn);
		}
		
		
		return map;
	}
	
	public static Sys_Admin loadSysry(String id){
		Connection conn = DatabaseUtil.getConn();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "";
		
		Sys_Admin admin = new Sys_Admin();
		
		sql = "select * from sysry where id = ?";
		pstmt = DatabaseUtil.prepareStmt(conn, sql);
		try {
			pstmt.setString(1, id);
			rs = pstmt.executeQuery();
			if(rs.next()){
				AdminDAO.initFromRS_sysry(rs,admin);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return admin;
	}
	
	//根据id和role判定是否存在
	public static boolean isExistbyLoing(String id,String role){
		String[] strings = role.split("_");
		role = strings[0];
		String role_id = strings[1];
		boolean exist = false;
		Connection conn = DatabaseUtil.getConn();
		ResultSet rs = null;
		String sql = null;
		if(role.equals("administrator")){
			 sql = "select * from _admin where id = ? and role = ?";
		}else{
			 sql = "select * from _admin where id = ? and role = 5";
		}
		
		PreparedStatement pstmt = DatabaseUtil.prepareStmt(conn, sql);
		try {
			int i = 1;
			pstmt.setString(i++, id);
			if(Integer.parseInt(role_id)!=5){
				pstmt.setString(i++, role_id);
			}
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
	
	//根据id和role判定是否存在
	public static boolean isExist(String id,String role){
		boolean exist = false;
		Connection conn = DatabaseUtil.getConn();
		ResultSet rs = null;
		String sql = null;
		if(role.equals("administrator")){
			 sql = "select * from _admin where id = ? and role < 5";
		}else{
			 sql = "select * from _admin where id = ? and role = 5";
		}
		
		PreparedStatement pstmt = DatabaseUtil.prepareStmt(conn, sql);
		try {
			int i = 1;
			pstmt.setString(i++, id);
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
	
	public static Admin load(String id,String role,int roleNum){
		Connection conn = DatabaseUtil.getConn();
		ResultSet rs = null;
		String sql = null;
		if(role.equals("administrator")){
			 sql = "select * from _admin where id = ? and role = " + roleNum;
		}else{
			 sql = "select * from _admin where id = ? and role = 5";
		}
		PreparedStatement pstmt = DatabaseUtil.prepareStmt(conn, sql);
		Admin admin = new Admin();
		try {
			int i = 1;
			pstmt.setString(i++, id);
			rs = pstmt.executeQuery();
			if(rs.next()){
				AdminDAO.initFromRS(rs, admin);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DatabaseUtil.close(rs);
			DatabaseUtil.close(pstmt);
			DatabaseUtil.close(conn);
		}
		return admin;
	}
	
	public static Admin load(String id){
		Connection conn = DatabaseUtil.getConn();
		ResultSet rs = null;
		String sql = "select * from _admin where id = ?";
		PreparedStatement pstmt = DatabaseUtil.prepareStmt(conn, sql);
		Admin admin = new Admin();
		try {
			int i = 1;
			pstmt.setString(i++, id);
			rs = pstmt.executeQuery();
			if(rs.next()){
				AdminDAO.initFromRS(rs, admin);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DatabaseUtil.close(rs);
			DatabaseUtil.close(pstmt);
			DatabaseUtil.close(conn);
		}
		return admin;
	}
	
	public static ArrayList<Admin> list(){
		Connection conn = DatabaseUtil.getConn();
		ResultSet rs = null;
		String sql = "select * from _admin where id > 2";
		PreparedStatement pstmt = DatabaseUtil.prepareStmt(conn, sql);
		ArrayList<Admin> adminList = new ArrayList<Admin>();
		try {
			rs = pstmt.executeQuery();
			while(rs.next()){
				Admin admin = new Admin();
				AdminDAO.initFromRS(rs, admin);
				adminList.add(admin);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DatabaseUtil.close(rs);
			DatabaseUtil.close(pstmt);
			DatabaseUtil.close(conn);
		}
		return adminList;
	}
	
	//列出系的实验室主任
	public static List<Admin>listSyszr(int departmentID){
		
		List<Admin> list = new ArrayList<Admin>();
		
		Connection conn = DatabaseUtil.getConn();
		ResultSet rs = null;
		String sql = "select * from _admin where role = ? and departmentID = ? ";
		PreparedStatement pstmt = DatabaseUtil.prepareStmt(conn, sql);
		try {
			pstmt.setInt(1, 4);
			pstmt.setInt(2, departmentID);
			rs = pstmt.executeQuery();
			while(rs.next()){
				Admin admin = new Admin();
				AdminDAO.initFromRS(rs, admin);
				list.add(admin);
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
	public static ArrayList<Admin> list(int role){
		Connection conn = DatabaseUtil.getConn();
		ResultSet rs = null;
		String sql = "select * from _admin where role = ? order by departmentID";
		PreparedStatement pstmt = DatabaseUtil.prepareStmt(conn, sql);
		ArrayList<Admin> adminList = new ArrayList<Admin>();
		try {
			pstmt.setInt(1, role);
			rs = pstmt.executeQuery();
			while(rs.next()){
				Admin admin = new Admin();
				AdminDAO.initFromRS(rs, admin);
				adminList.add(admin);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DatabaseUtil.close(rs);
			DatabaseUtil.close(pstmt);
			DatabaseUtil.close(conn);
		}
		return adminList;
	}

	public static boolean checkSysry(String sysryId) {
		// TODO Auto-generated method stub
		boolean b = false;
		Connection conn = DatabaseUtil.getConn();
		ResultSet rs = null;
		String sql = "select * from sysry where id = ?";
		PreparedStatement pstmt = DatabaseUtil.prepareStmt(conn, sql);
		ArrayList<Admin> adminList = new ArrayList<Admin>();
		try {
			pstmt.setString(1, sysryId);
			rs = pstmt.executeQuery();
			if(rs.next()){
				b = true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DatabaseUtil.close(rs);
			DatabaseUtil.close(pstmt);
			DatabaseUtil.close(conn);
		}
		
		
		return b;
	}

	public static void updateSysry(Sys_Admin sysAdmin) {
		// TODO Auto-generated method stub
		
		Connection conn = DatabaseUtil.getConn();
		String sql = "update sysry set sex = ?, birthday = ?, rylb = ?, whcd = ?, sxzy = ?, zyzw = ?, zjlx = ?, gnxljy = ?, " +
				"gnfxljy = ?, gwxljy = ?, gwfxljy = ?, bz = ? where id = ?";
		PreparedStatement pstmt = DatabaseUtil.prepareStmt(conn, sql);
		try {
			int i = 1;
			pstmt.setInt(i++, sysAdmin.getSex());
			pstmt.setString(i++, sysAdmin.getBirthday());
			pstmt.setInt(i++, sysAdmin.getRylb());
			pstmt.setInt(i++, sysAdmin.getWhcd());
			pstmt.setString(i++, sysAdmin.getSxzy());
			pstmt.setString(i++, sysAdmin.getZyzw());
			pstmt.setInt(i++, sysAdmin.getZjlx());
			pstmt.setInt(i++, sysAdmin.getGnxljy());
			pstmt.setInt(i++, sysAdmin.getGnfxljy());
			pstmt.setInt(i++, sysAdmin.getGwxljy());
			pstmt.setInt(i++, sysAdmin.getGwfxljy());
			pstmt.setString(i++, sysAdmin.getBz());
			pstmt.setString(i++, sysAdmin.getId());
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DatabaseUtil.close(pstmt);
			DatabaseUtil.close(conn);
		}
		
	}

	public static void insertSysry(Sys_Admin sysAdmin) {
		// TODO Auto-generated method stub
		
		Connection conn = DatabaseUtil.getConn();
		String sql = "insert into sysry values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		PreparedStatement pstmt = DatabaseUtil.prepareStmt(conn, sql);
		try {
			int i = 1;
			pstmt.setString(i++, sysAdmin.getId());
			pstmt.setString(i++, sysAdmin.getName());
			pstmt.setString(i++, sysAdmin.getBh());
			pstmt.setInt(i++, sysAdmin.getSex());
			pstmt.setString(i++, sysAdmin.getBirthday());
			pstmt.setInt(i++, sysAdmin.getRylb());
			pstmt.setInt(i++, sysAdmin.getWhcd());
			pstmt.setString(i++, sysAdmin.getSxzy());
			pstmt.setString(i++, sysAdmin.getZyzw());
			pstmt.setInt(i++, sysAdmin.getZjlx());
			pstmt.setInt(i++, sysAdmin.getGnxljy());
			pstmt.setInt(i++, sysAdmin.getGnfxljy());
			pstmt.setInt(i++, sysAdmin.getGwxljy());
			pstmt.setInt(i++, sysAdmin.getGwfxljy());
			pstmt.setString(i++, sysAdmin.getBz());
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DatabaseUtil.close(pstmt);
			DatabaseUtil.close(conn);
		}
		
	}
}
