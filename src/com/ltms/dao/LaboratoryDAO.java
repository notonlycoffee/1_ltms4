package com.ltms.dao;

import com.ltms.model.Admin;
import com.ltms.model.Experiment;
import com.ltms.model.Laboratory;
import com.ltms.model.Sysxx;
import com.ltms.util.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LaboratoryDAO {
	public static void initFromRS(ResultSet rs, Laboratory laboratory){
		try {
			laboratory.setId(rs.getInt("id"));
			laboratory.setSpecialty(rs.getString("specialty"));
			laboratory.setName(rs.getString("name"));
			laboratory.setAddress(rs.getString("address"));
			laboratory.setType(rs.getString("type"));
			laboratory.setAdmin(rs.getString("admin"));
			laboratory.setCourse(rs.getString("course"));
			laboratory.setDepartmentID(rs.getInt("departmentID"));
			laboratory.setEquipment(rs.getString("equipment"));
			laboratory.setPic1(rs.getString("pic1"));
			laboratory.setPic2(rs.getString("pic2"));
			laboratory.setPic3(rs.getString("pic3"));
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}	
	
	public static void initFromRS_sysxx(ResultSet rs, Sysxx sysxx){
		try {
			sysxx.setId(rs.getInt("id"));
			sysxx.setJlnf(rs.getString("jlnf"));
			sysxx.setSsxk(rs.getString("ssxk"));
			sysxx.setSymj(rs.getString("symj"));
			sysxx.setSysdm(rs.getString("sysdm"));
			sysxx.setSyslb(rs.getInt("syslb"));
			sysxx.setSyslx(rs.getInt("syslx"));
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}	
	
	public static Laboratory load(int id){
		Connection conn = DatabaseUtil.getConn();
		String sql = "select * from _laboratory where id = ?";
		PreparedStatement pstmt = DatabaseUtil.prepareStmt(conn, sql);
		ResultSet rs = null;
		Laboratory laboratory = new Laboratory();
		try {
			pstmt.setInt(1, id);
			rs = pstmt.executeQuery();
			if(rs.next()){
				LaboratoryDAO.initFromRS(rs, laboratory);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DatabaseUtil.close(rs);
			DatabaseUtil.close(pstmt);
			DatabaseUtil.close(conn);
		}
		return laboratory;
	}
	
	public static int loadbyDepa(int departmentID){
		Connection conn = DatabaseUtil.getConn();
		ResultSet rs = null;
		String sql = "select count(*) from _laboratory where departmentID = ?";
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
	
	public static int loadJfByDepa(int departmentID){
		Connection conn = DatabaseUtil.getConn();
		ResultSet rs = null;
		String sql = "select count(*) from sysjf s ,_laboratory l where s.id = l.id and departmentID = ?";
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
	public static int loadJfByDepa(int departmentID,String term){
		Connection conn = DatabaseUtil.getConn();
		ResultSet rs = null;
		String sql = "select count(*) from sysjf s ,_laboratory l where s.id = l.id and departmentID = ? and term_year = ?";
		PreparedStatement pstmt = DatabaseUtil.prepareStmt(conn, sql);
		int size = 0;
		try {
			pstmt.setInt(1, departmentID);
			pstmt.setString(2, term);
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
	
	public static int loadQkByDepa(int departmentID){
		Connection conn = DatabaseUtil.getConn();
		ResultSet rs = null;
		String sql = "select count(*) from sysqk s ,_laboratory l where s.id = l.id and departmentID = ?";
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
	public static int loadQkByDepa(int departmentID,String term){
		Connection conn = DatabaseUtil.getConn();
		ResultSet rs = null;
		String sql = "select count(*) from sysqk s ,_laboratory l where s.id = l.id and departmentID = ? and term_year = ?";
		PreparedStatement pstmt = DatabaseUtil.prepareStmt(conn, sql);
		int size = 0;
		try {
			pstmt.setInt(1, departmentID);
			pstmt.setString(2, term);
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
	
	
	public static int loadXxByDepa(int departmentID){
		Connection conn = DatabaseUtil.getConn();
		ResultSet rs = null;
		String sql = "select count(*) from sysxx s ,_laboratory l where s.id = l.id and departmentID = ?";
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
	public static int loadXxByDepa(int departmentID,String term){
		Connection conn = DatabaseUtil.getConn();
		ResultSet rs = null;
		String sql = "select count(*) from sysxx s ,_laboratory l where s.id = l.id and departmentID = ? and term_year = ?";
		PreparedStatement pstmt = DatabaseUtil.prepareStmt(conn, sql);
		int size = 0;
		try {
			pstmt.setInt(1, departmentID);
			pstmt.setString(2, term);
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
	
	public static int loadRyByDepa(int departmentID){
		Connection conn = DatabaseUtil.getConn();
		ResultSet rs = null;
		String sql = "select count(*) from sysry s ,_teacher t where s.id = t.id and t.unitID = ?";
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
	
	public static Sysxx load_sysxx(int id){
		Connection conn = DatabaseUtil.getConn();
		String sql = "select * from sysxx where id = ?";
		PreparedStatement pstmt = DatabaseUtil.prepareStmt(conn, sql);
		ResultSet rs = null;
		Sysxx sysxx = new Sysxx();
		try {
			pstmt.setInt(1, id);
			rs = pstmt.executeQuery();
			if(rs.next()){
				LaboratoryDAO.initFromRS_sysxx(rs, sysxx);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DatabaseUtil.close(rs);
			DatabaseUtil.close(pstmt);
			DatabaseUtil.close(conn);
		}
		return sysxx;
	}
	
	public static Sysxx load_sysxxByTeam(int id,String team){
		Connection conn = DatabaseUtil.getConn();
		String sql = "select * from sysxx where id = ? and term_year = ?";
		team = team.substring(0,9);
		System.out.println("term is " + team);
		PreparedStatement pstmt = DatabaseUtil.prepareStmt(conn, sql);
		ResultSet rs = null;
		Sysxx sysxx = new Sysxx();
		try {
			pstmt.setInt(1, id);
			pstmt.setString(2, team);
			rs = pstmt.executeQuery();
			if(rs.next()){
				LaboratoryDAO.initFromRS_sysxx(rs, sysxx);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DatabaseUtil.close(rs);
			DatabaseUtil.close(pstmt);
			DatabaseUtil.close(conn);
		}
		return sysxx;
	}
	
	//获取所有实验室信息
	public static List<Laboratory> load_laboratory(){
		Connection conn = DatabaseUtil.getConn();
		String sql = "select * from _laboratory";
		PreparedStatement pstmt = DatabaseUtil.prepareStmt(conn, sql);
		ResultSet rs = null;
		List<Laboratory> list = new ArrayList<Laboratory>();
		
		try {
			rs = pstmt.executeQuery();
			while(rs.next()){
				Laboratory lab = new Laboratory();
				LaboratoryDAO.initFromRS(rs, lab);
				list.add(lab);
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
	public static List<Laboratory> load_laboratoryByDep(int dep){
		Connection conn = DatabaseUtil.getConn();
		String sql = "select * from _laboratory where departmentID = ?";
		PreparedStatement pstmt = DatabaseUtil.prepareStmt(conn, sql);
		ResultSet rs = null;
		List<Laboratory> list = new ArrayList<Laboratory>();
		
		try {
			pstmt.setInt(1, dep);
			rs = pstmt.executeQuery();
			while(rs.next()){
				Laboratory lab = new Laboratory();
				LaboratoryDAO.initFromRS(rs, lab);
				list.add(lab);
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
	
	public static ArrayList<Laboratory> list(){
		Connection conn = DatabaseUtil.getConn();
		ResultSet rs = null;
		String sql = "select * from _laboratory";
		PreparedStatement pstmt = DatabaseUtil.prepareStmt(conn, sql);
		ArrayList<Laboratory> laboratoryList = new ArrayList<Laboratory>();
		try {
			rs = pstmt.executeQuery();
			while(rs.next()){
				Laboratory laboratory = new Laboratory();
				LaboratoryDAO.initFromRS(rs, laboratory);
				laboratoryList.add(laboratory);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DatabaseUtil.close(rs);
			DatabaseUtil.close(pstmt);
			DatabaseUtil.close(conn);
		}
		return laboratoryList;
	}
	
	public static ArrayList<Laboratory> indexList(int count){
		Connection conn = DatabaseUtil.getConn();
		String sql = "select * from _laboratory limit ?, ?";
		PreparedStatement pstmt = DatabaseUtil.prepareStmt(conn, sql);
		ResultSet rs = null;
		ArrayList<Laboratory> laboratoryList = new ArrayList<Laboratory>();
		try {
			pstmt.setInt(1, (int)(Math.random()*1000%130));
			pstmt.setInt(2, count);
			rs = pstmt.executeQuery();
			while(rs.next()){
				Laboratory laboratory = new Laboratory();
				LaboratoryDAO.initFromRS(rs, laboratory);
				laboratoryList.add(laboratory);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DatabaseUtil.close(rs);
			DatabaseUtil.close(pstmt);
			DatabaseUtil.close(conn);
		}
		return laboratoryList;
	}
	
	public static ArrayList<Laboratory> list(int departmentID){
		Connection conn = DatabaseUtil.getConn();
		ResultSet rs = null;
		String sql = "select * from _laboratory where departmentID = ?";
		PreparedStatement pstmt = DatabaseUtil.prepareStmt(conn, sql);
		ArrayList<Laboratory> laboratoryList = new ArrayList<Laboratory>();
		try {
			int i = 1;
			pstmt.setInt(i++, departmentID);
			rs = pstmt.executeQuery();
			while(rs.next()){
				Laboratory laboratory = new Laboratory();
				LaboratoryDAO.initFromRS(rs, laboratory);
				laboratoryList.add(laboratory);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DatabaseUtil.close(rs);
			DatabaseUtil.close(pstmt);
			DatabaseUtil.close(conn);
		}
		return laboratoryList;
	}
	
	public static String getName(int id){
		Connection conn = DatabaseUtil.getConn();
		ResultSet rs = null;
		String sql = "select name from _laboratory where id = ?";
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
	
	public static int getMaxGroup(int id){
		Connection conn = DatabaseUtil.getConn();
		ResultSet rs = null;
		String sql = "select maxGroup from _laboratory where id = ?";
		PreparedStatement pstmt = DatabaseUtil.prepareStmt(conn, sql);
		int maxGroup = 1;
		try {
			int i = 1;
			pstmt.setInt(i++, id);
			rs = pstmt.executeQuery();
			if(rs.next()){
				maxGroup = rs.getInt("maxGroup");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DatabaseUtil.close(rs);
			DatabaseUtil.close(pstmt);
			DatabaseUtil.close(conn);
		}
		return maxGroup;
	}
	
	public static ArrayList<Laboratory> index_laboratory_pic(int count){
		Connection conn = DatabaseUtil.getConn();
		String sql = "select count(*) from _laboratory where pic1 is not null and pic1 != ''";
		PreparedStatement pstmt = DatabaseUtil.prepareStmt(conn, sql);
		ResultSet rs = null;
		ArrayList<Laboratory> picList = new ArrayList<Laboratory>();
		try {
			rs = pstmt.executeQuery();
			int size = 0;
			if(rs.next()){
				size = (rs.getInt(1) + count - 1) / count - 1;
				if(size < 0){
					size = 0;
				}
			}
			sql = "select * from _laboratory where pic1 is not null and pic1 != '' limit ?, ?";
			pstmt = DatabaseUtil.prepareStmt(conn, sql);
			int i = 1;
			pstmt.setInt(i++, (int)(Math.random()*1000%size) * count);
			pstmt.setInt(i++, count);
			rs = pstmt.executeQuery();
			while(rs.next()){
				Laboratory l = new Laboratory();
				initFromRS(rs, l);
				picList.add(l);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DatabaseUtil.close(rs);
			DatabaseUtil.close(pstmt);
			DatabaseUtil.close(conn);
		}
		return picList;
	}
	
	public static ArrayList<Experiment> getExperimentList(int id){
		Connection conn = DatabaseUtil.getConn();
		String sql = "select * from _experiment where laboratoryID = ? and state = 1";
		PreparedStatement pstmt = DatabaseUtil.prepareStmt(conn, sql);
		ResultSet rs = null;
		ArrayList<Experiment> experimentList = new ArrayList<Experiment>();
		try {
			pstmt.setInt(1, id);
			rs = pstmt.executeQuery();
			while(rs.next()){
				Experiment e = new Experiment();
				ExperimentDAO.initFromRS(rs, e);
				experimentList.add(e);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DatabaseUtil.close(rs);
			DatabaseUtil.close(pstmt);
			DatabaseUtil.close(conn);
		}
		return experimentList;
	}

	public static Map<Integer, String> loadSysfp(int departmentId) {
		// TODO Auto-generated method stub
		Map<Integer, String> hashmap = new HashMap<Integer, String>();
		
		Connection conn = DatabaseUtil.getConn();
		String sql = "select * from _sysfp";
		PreparedStatement pstmt = DatabaseUtil.prepareStmt(conn, sql);
		ResultSet rs = null;
		
		try {
			rs = pstmt.executeQuery();
			while(rs.next()){
				
				hashmap.put(rs.getInt("id_sys"), rs.getString("id_syszr"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DatabaseUtil.close(rs);
			DatabaseUtil.close(pstmt);
			DatabaseUtil.close(conn);
		}
		
//		hashmap.put(107, "2033");
		return hashmap;
	}
	
	public static Map<Integer, String> loadGlygl(int departmentId) {
		// TODO Auto-generated method stub
		Map<Integer, String> hashmap = new HashMap<Integer, String>();
		
		Connection conn = DatabaseUtil.getConn();
		String sql = "select * from glyfp";
		PreparedStatement pstmt = DatabaseUtil.prepareStmt(conn, sql);
		ResultSet rs = null;
		
		try {
			rs = pstmt.executeQuery();
			while(rs.next()){
				
				hashmap.put(rs.getInt("id_sys"), rs.getString("id_gly"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DatabaseUtil.close(rs);
			DatabaseUtil.close(pstmt);
			DatabaseUtil.close(conn);
		}
		
//		hashmap.put(107, "2033");
		return hashmap;
	}

	public static Map<String, String> loadSyazr(int departmentId, int i) {
		// TODO Auto-generated method stub
		Map<String, String> hashmap = new HashMap<String, String>();
		
		Connection conn = DatabaseUtil.getConn();
		String sql = "select * from _admin where departmentID = ? and  role = ?";
		PreparedStatement pstmt = DatabaseUtil.prepareStmt(conn, sql);
		ResultSet rs = null;
		List<Admin> list = new ArrayList<Admin>(); 
		try {
			pstmt.setInt(1, departmentId);
			pstmt.setInt(2, i);
			rs = pstmt.executeQuery();
			while(rs.next()){
				Admin a = new Admin();
				AdminDAO.initFromRS(rs, a);
				list.add(a);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DatabaseUtil.close(rs);
			DatabaseUtil.close(pstmt);
			DatabaseUtil.close(conn);
		}
		
		for(Admin a : list){
			hashmap.put(a.getId(), a.getName());
		}
		return hashmap;
	}

	public static boolean isExit_sysxx(int id) {
		// TODO Auto-generated method stub
		boolean b = false;
		Connection conn = DatabaseUtil.getConn();
		String sql = "select * from sysxx where id = ?";
		PreparedStatement pstmt = DatabaseUtil.prepareStmt(conn, sql);
		ResultSet rs = null;
		
		try {
			pstmt.setInt(1,id);
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
		
//		hashmap.put(107, "2033");
		return b;
	}

	
}
