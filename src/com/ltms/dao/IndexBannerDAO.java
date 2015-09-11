package com.ltms.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.ltms.model.IndexBanner;
import com.ltms.util.DatabaseUtil;

public class IndexBannerDAO {
	public static void initFromRS(ResultSet rs, IndexBanner indexBanner){
		try {
			indexBanner.setId(rs.getInt("id"));
			indexBanner.setUrl(rs.getString("url"));
			indexBanner.setSort(rs.getInt("sort"));
			indexBanner.setBanner(rs.getString("banner"));
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static ArrayList<IndexBanner> list(){
		Connection conn = DatabaseUtil.getConn();
		ResultSet rs = null;
		String sql = "select * from _indexBanner order by sort";
		PreparedStatement pstmt = DatabaseUtil.prepareStmt(conn, sql);
		ArrayList<IndexBanner> indexBannerList = new ArrayList<IndexBanner>();
		try {
			rs = pstmt.executeQuery();
			while(rs.next()){
				IndexBanner indexBanner = new IndexBanner();
				IndexBannerDAO.initFromRS(rs, indexBanner);
				indexBannerList.add(indexBanner);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DatabaseUtil.close(rs);
			DatabaseUtil.close(pstmt);
			DatabaseUtil.close(conn);
		}
		return indexBannerList;
	}
}
