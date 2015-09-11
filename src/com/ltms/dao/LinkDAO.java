package com.ltms.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.ltms.model.Link;
import com.ltms.util.DatabaseUtil;

public class LinkDAO {
	public static void initFromRS(ResultSet rs, Link link){
		try {
			link.setId(rs.getInt("id"));
			link.setName(rs.getString("name"));
			link.setUrl(rs.getString("url"));
			link.setSort(rs.getInt("sort"));
			link.setLogoPath(rs.getString("logoPath"));
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static ArrayList<Link> list(){
		Connection conn = DatabaseUtil.getConn();
		ResultSet rs = null;
		String sql = "select * from _link order by sort";
		PreparedStatement pstmt = DatabaseUtil.prepareStmt(conn, sql);
		ArrayList<Link> linkList = new ArrayList<Link>();
		try {
			rs = pstmt.executeQuery();
			while(rs.next()){
				Link link = new Link();
				LinkDAO.initFromRS(rs, link);
				linkList.add(link);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DatabaseUtil.close(rs);
			DatabaseUtil.close(pstmt);
			DatabaseUtil.close(conn);
		}
		return linkList;
	}
}
