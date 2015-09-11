package com.ltms.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.ltms.model.Laboratory;
import com.ltms.model.Sys_Admin;
import com.ltms.model.Sysjf;
import com.ltms.model.Sysqk;
import com.ltms.util.DatabaseUtil;

public class SysxxDAO {

	private static void loadSysqk(ResultSet rs, Sysqk sysqk) {
		// TODO Auto-generated method stub
		try {
			sysqk.setBysjjlwrs_bks(rs.getInt("bysjjlwrs_bks"));
			sysqk.setBysjjlwrs_yjs(rs.getInt("bysjjlwrs_yjs"));
			sysqk.setBysjjlwrs_zks(rs.getInt("bysjjlwrs_zks"));
			sysqk.setHxkw_jxlw(rs.getInt("hxkw_jxlw"));
			sysqk.setHxkw_kylw(rs.getInt("hxkw_kylw"));
			sysqk.setId(rs.getInt("id"));
			sysqk.setJshjycg_gjj(rs.getInt("jshjycg_gjj"));
			sysqk.setJshjycg_sbj(rs.getInt("jshjycg_sbj"));
			sysqk.setJshjycg_xshj(rs.getInt("jshjycg_xshj"));
			sysqk.setJshjycg_zl(rs.getInt("jshjycg_zl"));
			sysqk.setJyxms_qt(rs.getInt("jyxms_qt"));
			sysqk.setJyxms_sbjys(rs.getInt("jyxms_sbjys"));
			sysqk.setJzrys(rs.getInt("jzrys"));
			sysqk.setKfsyqk_xnrss(rs.getInt("kfsyqk_xnrss"));
			sysqk.setKfsyqk_xnsygs(rs.getInt("kfsyqk_xnsygs"));
			sysqk.setKfsyqk_xnsyrs(rs.getInt("kfsyqk_xnsyrs"));
			sysqk.setKfsyqk_xwrss(rs.getInt("kfsyqk_xwrss"));
			sysqk.setKfsyqk_xwsygs(rs.getInt("kfsyqk_xwsygs"));
			sysqk.setKfsyqk_xwsyrs(rs.getInt("kfsyqk_xwsyrs"));
			sysqk.setKyxms_qt(rs.getInt("kyxms_qt"));
			sysqk.setKyxms_sbjys(rs.getInt("kyxms_sbjys"));
			sysqk.setSdjssl_jxlw(rs.getInt("sdjssl_jxlw"));
			sysqk.setSdjssl_kylw(rs.getInt("sdjssl_kylw"));
			sysqk.setShfwxms(rs.getInt("shfwxms"));
			sysqk.setSyhc(rs.getInt("syhc"));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public static void loadSysjf(ResultSet rs , Sysjf sysjf){
		
		try {
			sysjf.setId(rs.getInt("id"));
			sysjf.setQtjf(rs.getDouble("qtjf"));
			sysjf.setQzjxsynchf(rs.getDouble("qzjxsynchf"));
			sysjf.setQzjxyqgzjf(rs.getDouble("qzjxyqgzjf"));
			sysjf.setQzjxyqwhjf(rs.getDouble("qzjxyqwhjf"));
			sysjf.setSyjxyjggjf(rs.getDouble("syjxyjggjf"));
			sysjf.setSyjxyxjfhj(rs.getDouble("syjxyxjfhj"));
			sysjf.setSysjsjf(rs.getDouble("sysjsjf"));
			sysjf.setYqsbgzjfhj(rs.getDouble("yqsbgzjfhj"));
			sysjf.setYqsbwhjfhj(rs.getDouble("yqsbwhjfhj"));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	//获取专业
	public static Map<String, String> list_zy(String sign){
		Connection conn = DatabaseUtil.getConn();
		ResultSet rs = null;
		String sql = "select * from zy where sign = ? order by convert(mc using gbk) asc";
		PreparedStatement pstmt = DatabaseUtil.prepareStmt(conn, sql);
		Map<String, String> map = new LinkedHashMap<String, String>();
		try {
			pstmt.setString(1, sign);
			rs = pstmt.executeQuery();
			while(rs.next()){
				String key = rs.getString("dm");
				String value = rs.getString("mc");
				map.put(key, value);
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
	//获取专业类
	public static Map<String, String> list_zyl(){
		Connection conn = DatabaseUtil.getConn();
		ResultSet rs = null;
		String sql = "select * from zyl";
		PreparedStatement pstmt = DatabaseUtil.prepareStmt(conn, sql);
		Map<String, String> map = new HashMap<String, String>();
		try {
			rs = pstmt.executeQuery();
			while(rs.next()){
				String key = rs.getString("sign");
				String value = rs.getString("mc");
				map.put(key, value);
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
	
	//根据id获取实验室情况
	public static Sysqk getSysqk(int id){
		Sysqk sysqk = new Sysqk();
		
		
		Connection conn = DatabaseUtil.getConn();
		ResultSet rs = null;
		String sql = "select * from sysqk where id = ?";
		PreparedStatement pstmt = DatabaseUtil.prepareStmt(conn, sql);
		try {
			pstmt.setInt(1, id);
			rs = pstmt.executeQuery();
			while(rs.next()){
				SysxxDAO.loadSysqk(rs,sysqk);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DatabaseUtil.close(rs);
			DatabaseUtil.close(pstmt);
			DatabaseUtil.close(conn);
		}
		
		return sysqk;
	}
	
	public static Sysqk getSysqkByTeam(int id,String team){
		Sysqk sysqk = new Sysqk();
		Connection conn = DatabaseUtil.getConn();
		ResultSet rs = null;
		team = team.substring(0,9);
		String sql = "select * from sysqk where id = ? and term_year = ?";
		PreparedStatement pstmt = DatabaseUtil.prepareStmt(conn, sql);
		try {
			pstmt.setInt(1, id);
			pstmt.setString(2, team);
			rs = pstmt.executeQuery();
			while(rs.next()){
				SysxxDAO.loadSysqk(rs,sysqk);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DatabaseUtil.close(rs);
			DatabaseUtil.close(pstmt);
			DatabaseUtil.close(conn);
		}
		
		return sysqk;
	}
	
	public static String getsysryById(int id){
		
		
		Connection conn = DatabaseUtil.getConn();
		ResultSet rs = null;
		String sql = "select * from glyfp where id_sys = ?";
		String name = "";
		PreparedStatement pstmt = DatabaseUtil.prepareStmt(conn, sql);
		try {
			pstmt.setInt(1, id);
			rs = pstmt.executeQuery();
			if(rs.next()){
				name = rs.getString("id_gly");
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
	
	//根据id判断是否已存在数据
	public static boolean checkSysqk(int id){
		
		boolean b = false;
		Connection conn = DatabaseUtil.getConn();
		ResultSet rs = null;
		String sql = "select * from sysqk where id = ?";
		PreparedStatement pstmt = DatabaseUtil.prepareStmt(conn, sql);
		try {
			pstmt.setInt(1, id);
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
	
	//根据id判断是否已存在数据
	public static boolean checkSysqkByTeam(int id,String team){
		
		boolean b = false;
		Connection conn = DatabaseUtil.getConn();
		ResultSet rs = null;
		String sql = "select * from sysqk where id = ? and term_year = ?";
		PreparedStatement pstmt = DatabaseUtil.prepareStmt(conn, sql);
		try {
			pstmt.setInt(1, id);
			pstmt.setString(2, team);
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
	
	//添加实验室情况
	public static void addSysqk(Sysqk sysqk,String team){
		
		Connection conn = DatabaseUtil.getConn();
		String sql = "insert into sysqk values(?, ?, ?, ?, ?," +
											 " ?, ?, ?, ?, ?, " +
											 " ?, ?, ?, ?, ?," +
											 " ?, ?, ?, ?, ?, " +
											 " ?, ?, ?, ?, ?, ?)";
		PreparedStatement pstmt = DatabaseUtil.prepareStmt(conn, sql);
		try {
			int i = 1;
			pstmt.setInt(i++, sysqk.getId());
			pstmt.setInt(i++, sysqk.getJshjycg_gjj());
			pstmt.setInt(i++, sysqk.getJshjycg_sbj());
			pstmt.setInt(i++, sysqk.getJshjycg_zl());
			pstmt.setInt(i++, sysqk.getJshjycg_xshj());
			pstmt.setInt(i++, sysqk.getSdjssl_jxlw());
			pstmt.setInt(i++, sysqk.getSdjssl_kylw());
			pstmt.setInt(i++, sysqk.getHxkw_jxlw());
			pstmt.setInt(i++, sysqk.getHxkw_kylw());
			pstmt.setInt(i++, sysqk.getSyhc());
			pstmt.setInt(i++, sysqk.getKyxms_sbjys());
			pstmt.setInt(i++, sysqk.getKyxms_qt());
			pstmt.setInt(i++, sysqk.getShfwxms());
			pstmt.setInt(i++, sysqk.getJyxms_sbjys());
			pstmt.setInt(i++, sysqk.getJyxms_qt());
			pstmt.setInt(i++, sysqk.getBysjjlwrs_zks());
			pstmt.setInt(i++, sysqk.getBysjjlwrs_bks());
			pstmt.setInt(i++, sysqk.getBysjjlwrs_yjs());
			pstmt.setInt(i++, sysqk.getKfsyqk_xnsygs());
			pstmt.setInt(i++, sysqk.getKfsyqk_xwsygs());
			pstmt.setInt(i++, sysqk.getKfsyqk_xnsyrs());
			pstmt.setInt(i++, sysqk.getKfsyqk_xwsyrs());
			pstmt.setInt(i++, sysqk.getKfsyqk_xnrss());
			pstmt.setInt(i++, sysqk.getKfsyqk_xwrss());
			pstmt.setInt(i++, sysqk.getJzrys());
			pstmt.setString(i++, team);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DatabaseUtil.close(pstmt);
			DatabaseUtil.close(conn);
		}
		
	}
	
	
	//更新实验室情况
	public static void updateSysqk(Sysqk sysqk,String team){
		
		Connection conn = DatabaseUtil.getConn();
		String sql = "delete from sysqk where id = ? and term_year = ?";
		PreparedStatement pstmt = DatabaseUtil.prepareStmt(conn, sql);
		try {
			int i = 1;
			pstmt.setInt(i++, sysqk.getId());
			pstmt.setString(i++, team);
			pstmt.executeUpdate();
			
			sql = "insert into sysqk values(?, ?, ?, ?, ?," +
											 " ?, ?, ?, ?, ?, " +
											 " ?, ?, ?, ?, ?," +
											 " ?, ?, ?, ?, ?, " +
											 " ?, ?, ?, ?, ?, ?)";
			
			pstmt = DatabaseUtil.prepareStmt(conn, sql);
			
			i=1;
			pstmt.setInt(i++, sysqk.getId());
			pstmt.setInt(i++, sysqk.getJshjycg_gjj());
			pstmt.setInt(i++, sysqk.getJshjycg_sbj());
			pstmt.setInt(i++, sysqk.getJshjycg_zl());
			pstmt.setInt(i++, sysqk.getJshjycg_xshj());
			pstmt.setInt(i++, sysqk.getSdjssl_jxlw());
			pstmt.setInt(i++, sysqk.getSdjssl_kylw());
			pstmt.setInt(i++, sysqk.getHxkw_jxlw());
			pstmt.setInt(i++, sysqk.getHxkw_kylw());
			pstmt.setInt(i++, sysqk.getSyhc());
			pstmt.setInt(i++, sysqk.getKyxms_sbjys());
			pstmt.setInt(i++, sysqk.getKyxms_qt());
			pstmt.setInt(i++, sysqk.getShfwxms());
			pstmt.setInt(i++, sysqk.getJyxms_sbjys());
			pstmt.setInt(i++, sysqk.getJyxms_qt());
			pstmt.setInt(i++, sysqk.getBysjjlwrs_zks());
			pstmt.setInt(i++, sysqk.getBysjjlwrs_bks());
			pstmt.setInt(i++, sysqk.getBysjjlwrs_yjs());
			pstmt.setInt(i++, sysqk.getKfsyqk_xnsygs());
			pstmt.setInt(i++, sysqk.getKfsyqk_xwsygs());
			pstmt.setInt(i++, sysqk.getKfsyqk_xnsyrs());
			pstmt.setInt(i++, sysqk.getKfsyqk_xwsyrs());
			pstmt.setInt(i++, sysqk.getKfsyqk_xnrss());
			pstmt.setInt(i++, sysqk.getKfsyqk_xwrss());
			pstmt.setInt(i++, sysqk.getJzrys());
			pstmt.setString(i++, team);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DatabaseUtil.close(pstmt);
			DatabaseUtil.close(conn);
		}
		
	}
	
	//获取实验室经费i情况
	public static Sysjf loadsysjfByTeam(int id,String team){
		Sysjf sysjf = new Sysjf();
		team = team.substring(0,9);
		Connection conn = DatabaseUtil.getConn();
		ResultSet rs = null;
		String sql = "select * from sysjf where id = ? and term_year = ?";
		PreparedStatement pstmt = DatabaseUtil.prepareStmt(conn, sql);
		try {
			pstmt.setInt(1, id);
			pstmt.setString(2, team);
			rs = pstmt.executeQuery();
			while(rs.next()){
				SysxxDAO.loadSysjf(rs,sysjf);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DatabaseUtil.close(rs);
			DatabaseUtil.close(pstmt);
			DatabaseUtil.close(conn);
		}
		
		return sysjf;
	}
	
	public static Sysjf loadsysjf(int id ){
		Sysjf sysjf = new Sysjf();
		
		Connection conn = DatabaseUtil.getConn();
		ResultSet rs = null;
		String sql = "select * from sysjf where id = ?";
		PreparedStatement pstmt = DatabaseUtil.prepareStmt(conn, sql);
		try {
			pstmt.setInt(1, id);
			rs = pstmt.executeQuery();
			while(rs.next()){
				SysxxDAO.loadSysjf(rs,sysjf);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DatabaseUtil.close(rs);
			DatabaseUtil.close(pstmt);
			DatabaseUtil.close(conn);
		}
		
		return sysjf;
	}
	
	
	public static boolean checkSysjf(int id ,String team){
		boolean b = false;
		Connection conn = DatabaseUtil.getConn();
		ResultSet rs = null;
		String sql = "select * from sysjf where id = ? and term_year = ?";
		PreparedStatement pstmt = DatabaseUtil.prepareStmt(conn, sql);
		try {
			pstmt.setInt(1, id);
			pstmt.setString(2, team);
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
	public static boolean checkSysxx(int id ,String team){
		boolean b = false;
		Connection conn = DatabaseUtil.getConn();
		ResultSet rs = null;
		String sql = "select * from sysxx where id = ? and term_year = ?";
		PreparedStatement pstmt = DatabaseUtil.prepareStmt(conn, sql);
		try {
			pstmt.setInt(1, id);
			pstmt.setString(2, team);
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
	
	//更新实验室经费情况
	public static void updateSysjf(Sysjf sysjf,String team){
		
		Connection conn = DatabaseUtil.getConn();
		String sql = "delete from sysjf where id = ? and term_year = ?";
		PreparedStatement pstmt = DatabaseUtil.prepareStmt(conn, sql);
		try {
			int i = 1;
			pstmt.setInt(i++, sysjf.getId());
			pstmt.setString(i++, team);
			pstmt.executeUpdate();
			
			sql = "insert into sysjf values(?, ?, ?, ?, ?," +
			 " ?, ?, ?, ?, ?, ?)";
			pstmt = DatabaseUtil.prepareStmt(conn, sql);
			i = 1;
			pstmt.setInt(i++, sysjf.getId());
			pstmt.setDouble(i++, sysjf.getYqsbgzjfhj());
			pstmt.setDouble(i++, sysjf.getQzjxyqgzjf());
			pstmt.setDouble(i++, sysjf.getYqsbwhjfhj());
			pstmt.setDouble(i++, sysjf.getQzjxyqwhjf());
			pstmt.setDouble(i++, sysjf.getSyjxyxjfhj());
			pstmt.setDouble(i++, sysjf.getQzjxsynchf());
			pstmt.setDouble(i++, sysjf.getSysjsjf());
			pstmt.setDouble(i++, sysjf.getSyjxyjggjf());
			pstmt.setDouble(i++, sysjf.getQtjf());
			pstmt.setString(i++, team);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DatabaseUtil.close(pstmt);
			DatabaseUtil.close(conn);
		}
	}
	
	//插入实验室经费情况
public static void addSysjf(Sysjf sysjf,String team){
		
		Connection conn = DatabaseUtil.getConn();
		String sql = "insert into sysjf values(?, ?, ?, ?, ?," +
											 " ?, ?, ?, ?, ?, ?)";
		PreparedStatement pstmt = DatabaseUtil.prepareStmt(conn, sql);
		try {
			int i = 1;
			pstmt.setInt(i++, sysjf.getId());
			pstmt.setDouble(i++, sysjf.getYqsbgzjfhj());
			pstmt.setDouble(i++, sysjf.getQzjxyqgzjf());
			pstmt.setDouble(i++, sysjf.getYqsbwhjfhj());
			pstmt.setDouble(i++, sysjf.getQzjxyqwhjf());
			pstmt.setDouble(i++, sysjf.getSyjxyxjfhj());
			pstmt.setDouble(i++, sysjf.getQzjxsynchf());
			pstmt.setDouble(i++, sysjf.getSysjsjf());
			pstmt.setDouble(i++, sysjf.getSyjxyjggjf());
			pstmt.setDouble(i++, sysjf.getQtjf());
			pstmt.setString(i++, team);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DatabaseUtil.close(pstmt);
			DatabaseUtil.close(conn);
		}
		
	}

	//统一给所有实验室的代码赋值
	public static void updateDm(){
		 int begin_count = 10000;
	  	 
	  	 Connection conn = DatabaseUtil.getConn();
		 String sql = "update sysxx set sysdm = ? where id = ?";
		 PreparedStatement pstmt = DatabaseUtil.prepareStmt(conn, sql);
		
	  	 List<Laboratory> list =  LaboratoryDAO.load_laboratory();
	  	 
	     try {
			for (int i = 0; i < list.size(); i++) {
				//    	 sysdm+(begin_count+i);\
				pstmt.setString(1, String.valueOf(begin_count+i));
				pstmt.setInt(2, list.get(i).getId());
				pstmt.executeUpdate();
			}
		} catch (Exception e) {
			// TODO: handle exception
		}finally{
			DatabaseUtil.close(pstmt);
			DatabaseUtil.close(conn);
		}
    }

	public static Sys_Admin loadsysry(String sysryId) {
		// TODO Auto-generated method stub
		Connection conn = DatabaseUtil.getConn();
		ResultSet rs = null;
		String sql = "select * from sysry where id = ?";
		Sys_Admin admin = new Sys_Admin();
		PreparedStatement pstmt = DatabaseUtil.prepareStmt(conn, sql);
		try {
			pstmt.setString(1, sysryId);
			rs = pstmt.executeQuery();
			if(rs.next()){
				AdminDAO.initFromRS_sysry(rs, admin);
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
}
