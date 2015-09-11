package com.ltms.dbf;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.ltms.dao.ClassDAO;
import com.ltms.dao.CourseDAO;
import com.ltms.dao.DepartmentDAO;
import com.ltms.dao.ExItemDAO;
import com.ltms.dao.ExperimentDAO;
import com.ltms.dao.LaboratoryDAO;
import com.ltms.dao.RegulationDAO;
import com.ltms.dao.ScheduleDAO;
import com.ltms.dao.SysxxDAO;
import com.ltms.model.Course_sy;
import com.ltms.model.Department;
import com.ltms.model.ExItem;
import com.ltms.model.Experiment;
import com.ltms.model.Laboratory;
import com.ltms.model.Schedule;
import com.ltms.model.Sys_Admin;
import com.ltms.model.Sysjf;
import com.ltms.model.Sysqk;
import com.ltms.model.Sysxx;

public class Copy_2_of_DBFJdbcOdbcTest {
	public static boolean add_dbf(String url, Map<Integer, String> map,
			String _currenTerm_) {
		
		ArrayList<Department> departmentList = (ArrayList<Department>)DepartmentDAO.list();
		Map<Integer,Integer> shiyanshiMap = new HashMap<Integer, Integer>();
		
		//实验室经费
		Map<Integer, Integer> sysjfMap = new HashMap<Integer, Integer>();
		//实验室情况
		Map<Integer, Integer> sysqkMap = new HashMap<Integer, Integer>();
		//实验室信息
		Map<Integer, Integer> sysxxMap = new HashMap<Integer, Integer>();
		
		Map<Integer, Integer> xxMap = new HashMap<Integer, Integer>();
		
		for(Department dep : departmentList){
			shiyanshiMap.put(dep.getId(), LaboratoryDAO.loadbyDepa(dep.getId()));
			sysjfMap.put(dep.getId(), LaboratoryDAO.loadJfByDepa(dep.getId()));
			sysqkMap.put(dep.getId(), LaboratoryDAO.loadQkByDepa(dep.getId()));
			sysxxMap.put(dep.getId(), LaboratoryDAO.loadXxByDepa(dep.getId()));
			xxMap.put(dep.getId(), sysjfMap.get(dep.getId()) + sysqkMap.get(dep.getId()) + sysxxMap.get(dep.getId()));
		}
		int cou = 1;
		//存放3:1的系别
		ArrayList<Integer> arr = new ArrayList<Integer>();
		for(Department dep : departmentList){
			int sygs = shiyanshiMap.get(dep.getId());
//			System.out.println("sygs is " + sygs);
			int zdgs = xxMap.get(dep.getId());
//			System.out.println("zdgs is " + zdgs);
			
			if(sygs * 3 == zdgs && sygs!=0 && zdgs !=0){
				arr.add(dep.getId());
				System.out.println("depatament is " + dep.getId());
			}
			
		}
		

		
		Connection conn = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		String DB_URL = "jdbc:odbc:Driver={Microsoft FoxPro VFP Driver (*.dbf)};"
				+ "UID=;"
				+ "Deleted=Yes;"
				+ "Null=Yes;"
				+ "Collate=Machine;"
				+ "BackgroundFetch=Yes;"
				+ "Exclusive=No;"
				+ "SourceType=DBF;"
				+ "SourceDB=" + url; // 文件所在的路径

		try {
			Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");

//			System.out.println("enter odbc");
			try {
				

				// 获取实验室信息
				List<Laboratory> list = LaboratoryDAO.load_laboratory();
				for (int i = 0; i < list.size(); i++) {
					// 现获取实验室记录
					int id = list.get(i).getId();
//					System.out.println("hhhhhhhh  " + list.get(i).getDepartmentID());
//					if(arr.contains(list.get(i).getDepartmentID())){
					if(arr.contains(list.get(i).getDepartmentID())){
						System.out.println("sssssssssss  " + list.get(i).getDepartmentID());
						
						String name = list.get(i).getName();
						// 根据id获取三个实验室相关表的信息
						Sysxx sysxx = LaboratoryDAO.load_sysxx(id);
						Sysqk sysqk = SysxxDAO.getSysqk(id);
						Sysjf sysjf = SysxxDAO.loadsysjf(id);
						// 添加实验室表信息
						System.out.println("i is " + i);
						
						// 选出有真正使用的实验室
						int jj = 1;
						if (sysxx.getSysdm() != null && !sysxx.getSysdm().trim().equals("11111")) {
							
							
							System.out.println("jj is " + jj);
							jj++;
							conn = DriverManager.getConnection(DB_URL);
							pstm = conn
							.prepareStatement("insert into sysk values(?,?,?,?,?,?,?,?,?,?,"
									+ "?,?,?,?,?,?,?,?,?,?,"
									+ "?,?,?,?,?,?,?,?,?,?,"
									+ "?,?,?,?,?,?,?,?,?,?,?,?)");
							
							int count = 1;
							pstm.setString(count++, sysxx.getSysdm());
							pstm.setString(count++, name);// ///
							pstm.setString(count++, String
									.valueOf(sysxx.getSyslb()));
							pstm.setString(count++, sysxx.getJlnf());
							pstm.setInt(count++, Integer.parseInt(sysxx.getSymj()));
							pstm.setString(count++, String
									.valueOf(sysxx.getSyslx()));
							pstm.setString(count++, sysxx.getSsxk());

							pstm.setInt(count++, sysqk.getJshjycg_gjj());
							pstm.setInt(count++, sysqk.getJshjycg_sbj());
							pstm.setInt(count++, sysqk.getJshjycg_zl());
							pstm.setInt(count++, sysqk.getJshjycg_xshj());
							pstm.setInt(count++, sysqk.getSdjssl_jxlw());
							pstm.setInt(count++, sysqk.getSdjssl_kylw());
							pstm.setInt(count++, sysqk.getHxkw_jxlw());
							pstm.setInt(count++, sysqk.getHxkw_kylw());
							pstm.setInt(count++, sysqk.getSyhc());
							pstm.setInt(count++, sysqk.getKyxms_sbjys());
							pstm.setInt(count++, sysqk.getKyxms_qt());
							pstm.setInt(count++, sysqk.getShfwxms());
							pstm.setInt(count++, sysqk.getJyxms_sbjys());
							pstm.setInt(count++, sysqk.getJyxms_qt());
							pstm.setInt(count++, sysqk.getBysjjlwrs_zks());
							pstm.setInt(count++, sysqk.getBysjjlwrs_bks());
							pstm.setInt(count++, sysqk.getBysjjlwrs_yjs());
							pstm.setInt(count++, sysqk.getKfsyqk_xnsygs());
							pstm.setInt(count++, sysqk.getKfsyqk_xwsygs());
							pstm.setInt(count++, sysqk.getKfsyqk_xnsyrs());
							pstm.setInt(count++, sysqk.getKfsyqk_xwsyrs());
							pstm.setInt(count++, sysqk.getKfsyqk_xnrss());
							pstm.setInt(count++, sysqk.getKfsyqk_xwrss());
							pstm.setInt(count++, sysqk.getJzrys());
//							System.out.println("center");
							double all = sysjf.getYqsbgzjfhj()
									+ sysjf.getQzjxyqgzjf() + sysjf.getYqsbwhjfhj()
									+ sysjf.getQzjxyqwhjf() + sysjf.getSyjxyxjfhj()
									+ sysjf.getQzjxsynchf() + sysjf.getSysjsjf()
									+ sysjf.getSyjxyjggjf() + sysjf.getQtjf();
							pstm.setDouble(count++, all);
							pstm.setDouble(count++, sysjf.getYqsbgzjfhj());
							pstm.setDouble(count++, sysjf.getQzjxyqgzjf());
							pstm.setDouble(count++, sysjf.getYqsbwhjfhj());
							pstm.setDouble(count++, sysjf.getQzjxyqwhjf());
							pstm.setDouble(count++, sysjf.getSyjxyxjfhj());
							pstm.setDouble(count++, sysjf.getQzjxsynchf());
							pstm.setDouble(count++, sysjf.getSysjsjf());
							pstm.setDouble(count++, sysjf.getSyjxyjggjf());
							pstm.setDouble(count++, sysjf.getQtjf());
//							System.out.println("before");
							pstm.setBoolean(count++, false);
//							System.out.println("after");
							pstm.executeUpdate();
							
							if(pstm != null){
								pstm.close();
							}
							if(conn != null){
								conn.close();
							}
							
							System.out.println("cou is " + cou++);
						}
						System.out.println("end if");
					}
					System.out.println("end big if");
					
				}

				System.out.println("1111111");
				
				
				conn = DriverManager.getConnection(DB_URL);
				
				// 添加实验信息
				pstm = conn
						.prepareStatement("insert into syxmk values(?,?,?,?,?,?,?,?,?,?,"
								+ "?,?,?,?,?,?)");
				List<Experiment> list2 = ExperimentDAO.loadbyterm(_currenTerm_);
				for (int i2 = 0; i2 < list2.size(); i2++) {
					
					Schedule schedule = ScheduleDAO
							.getScheduleByExperimentID(list2.get(i2)
									.getId());
					
					List<ExItem> exltemList = ExItemDAO.loadExitem(schedule
							.getId());
					
					Experiment experiment = list2.get(i2);
					int laboratory_id = experiment.getLaboratoryID();
					Sysxx sysxx = LaboratoryDAO.load_sysxx(laboratory_id);
					if(sysxx.getSysdm() != null){
						
						for (int i22 = 0; i22 < exltemList.size(); i22++) {
							ExItem exltem = exltemList.get(i22);
							
							int count = 1;
							pstm.setString(count++, sysxx.getSysdm());
//							System.out.println("id is " + exltemList.get(i22).getId());
							pstm.setString(count++, map.get(exltemList.get(i22).getId()));
//							System.out.println("value is " + map.get(exltemList.get(i22).getId()));
							pstm.setString(count++, exltem.getItemName());
							pstm.setInt(count++, experiment.getXs());
							pstm.setString(count++, experiment.getSylb() + "");

							String type = ExItemDAO.getType(schedule.getId()).get(
									exltem.getId());
							int type_id = 5;
							if (type.equals("演示"))
								type_id = 1;
							if (type.equals("验证"))
								type_id = 2;
							if (type.equals("综合"))
								type_id = 3;
							if (type.equals("设计"))
								type_id = 4;
							pstm.setString(count++, String.valueOf(type_id));
							pstm.setString(count++, experiment.getSsxk());
							pstm.setString(count++, "," + experiment.getMxzy());////////////////////////
							pstm.setBoolean(count++, false);
							pstm.setBoolean(count++, false);
							pstm.setBoolean(count++, true);
							pstm.setBoolean(count++, false);
							pstm.setBoolean(count++, false);
							pstm.setString(count++, "");
							pstm.setString(count++, map.get(exltemList.get(i22).getId()));
//							System.out.println("exit333");
							pstm.setBoolean(count++, false);
							pstm.executeUpdate();
							
						}
						
					}
					
				}
				
				System.out.println("ssw555555555555555555555555555555555555555555555555555");
				
				// //增加课程信息
				pstm = conn
						.prepareStatement("insert into kck values(?,?,?,?,?)");
				
				//获取所有实验课程
				List<Experiment> list3 = ExperimentDAO.loadbyterm(_currenTerm_);
				//对实验课程判断归类
				Map<Integer, String> id_Map = new HashMap<Integer, String>();
				Map<Integer, String> zy_Map = new HashMap<Integer, String>();
				Map<Integer, String> sy_Map = new HashMap<Integer, String>();
				Map<String, Integer> map_id = new HashMap<String, Integer>();
				
				for(Experiment experiment : list3){
					List<ExItem> exltemList = ExItemDAO.loadExitem(experiment
							.getId());
					String symes = "";
					for(ExItem e : exltemList){
						symes += map.get(e.getId()) + ",";
						System.out.println("sisis is " + map.get(e.getId()));
					}
					
					//判断集合是否已经存有这个课程
					if(!(id_Map.containsValue(experiment.getCourseID()))){
						//获取课程对应的实验项目
						
						id_Map.put(experiment.getId(), experiment.getCourseID());
						map_id.put(experiment.getCourseID(), experiment.getId());
						zy_Map.put(experiment.getId(), experiment.getMxzy());
						sy_Map.put(experiment.getId(), symes);
					}else{
						//获取对应的id值
						int id3 = map_id.get(experiment.getCourseID());
						//专业往后添加
						zy_Map.put(id3, zy_Map.get(id3) + experiment.getMxzy());
						//实验项目往后添加
						sy_Map.put(id3, sy_Map.get(id3) + symes);
					}
					
				}
				
				Set set = id_Map.keySet();
				Iterator iterator = set.iterator();
				while(iterator.hasNext()){
					Integer id333 = (Integer) iterator.next();
					
					//获取课程id
					String courseId3 = id_Map.get(id333);
					//获取课程名字
					String course_name = CourseDAO.getName(courseId3);
					//获取专业
					String zyString = zy_Map.get(id333);
					//获取实验项目
					String syString = sy_Map.get(id333);
					
					int count = 1;
					pstm.setString(count++, courseId3);
					pstm.setString(count++, course_name);
					pstm.setString(count++, syString);
					pstm.setString(count++, zyString);
					pstm.setBoolean(count++, false);
					pstm.executeUpdate();
					
				}
				System.out.println("44444444444444");
				// //增加人员信息
				pstm = conn
						.prepareStatement("insert into ryk values(?,?,?,?,?,?,?,?,?,?,"
								+ "?,?,?,?,?,?,?)");
				List<Laboratory> list4 = LaboratoryDAO.load_laboratory();
				for (int i4 = 0; i4 < list4.size(); i4++) {
					// 实验室id
					int id4 = list4.get(i4).getId();
					
					if(arr.contains(list.get(i4).getDepartmentID())){
						System.out.println("sssssssssss  " + list.get(i4).getDepartmentID());
						//获取实验室代码
						//只获取有用的实验室
						Sysxx sysxx = LaboratoryDAO.load_sysxx(id4);
						String sys_dm = sysxx.getSysdm();
						if (sys_dm != null && !sys_dm.trim().equals("11111")) {
							// 获取对应的实验室管理人员id
							String sysry_id = SysxxDAO.getsysryById(id4);
							// 获取实验室管理人员的基本情况
							Sys_Admin sys_admin = SysxxDAO.loadsysry(sysry_id);
							if(sys_admin.getBh() != null){
								// 开始数据插入
								int count4 = 1;
								pstm.setString(count4++, sys_admin.getBh());
								pstm.setString(count4++, sys_admin.getName());
								pstm.setString(count4++, sys_admin.getSex() + "");
								pstm.setString(count4++, sys_admin.getBirthday());
								pstm.setString(count4++, "");
								pstm.setString(count4++, sys_admin.getSxzy());
								pstm.setString(count4++, sys_admin.getZyzw());
								pstm.setString(count4++, sys_admin.getWhcd() + "");
								String zjlx_ = "00";
								if(sys_admin.getZjlx() != 0){
									zjlx_ = sys_admin.getZjlx() + "";
								}
								pstm.setString(count4++, zjlx_);
								pstm.setInt(count4++, sys_admin.getGnxljy());
								pstm.setInt(count4++, sys_admin.getGnfxljy());
								pstm.setInt(count4++, sys_admin.getGwxljy());
								pstm.setInt(count4++, sys_admin.getGwfxljy());
								pstm.setString(count4++, sys_dm);
								pstm.setString(count4++, sys_admin.getRylb() + "");
								pstm.setString(count4++, sys_admin.getBz());
								pstm.setBoolean(count4++, false);
								pstm.executeUpdate();
							}
						}
					}
				}

				System.out.println("kaichu");
				// //增加开出信息
				pstm = conn
						.prepareStatement("insert into xmjl values(?,?,?,?,?,?,?,?,?,?,"
								+ "?,?,?,?,?,?,?,?,?)");
				List<Experiment> list5 = ExperimentDAO.loadbyterm(_currenTerm_);
				for (int i5 = 0; i5 < list5.size(); i5++) {

					// 根据课程id获取课程名称
					Experiment experiment = list5.get(i5);
					
					if(arr.contains(experiment.getDepartmentID())){
//						System.out.println("idididididid is " + experiment.getDepartmentID());
						String course_name5 = CourseDAO.getName(experiment
								.getCourseID());
						// 获取实验室名称
						String laboratory_name5 = (LaboratoryDAO.load(experiment
								.getLaboratoryID())).getName();
						Sysxx sysxx = LaboratoryDAO.load_sysxx(experiment
								.getLaboratoryID());
						
						// 根据实验id获取进度表里面的内容
						Schedule schedule = ScheduleDAO
								.getScheduleByExperimentID(experiment.getId());
						
						// 获取专业    班级
						String mxzy5 = experiment.getMxzy();
						String classInfo5 = schedule.getClassID();
						String[] mxzys5 = mxzy5.split(",");
						//未处理
						String[] classInfos5 = classInfo5.split("@");
						
						for (int m = 0; m < mxzys5.length; m++) {

							Schedule schedule_ = ScheduleDAO
									.getScheduleByExperimentID(list5.get(i5)
											.getId());
							List<ExItem> exltemList = ExItemDAO
									.loadExitem(schedule_.getId());
							for (int i55 = 0; i55 < exltemList.size(); i55++) {
								ExItem exltem = exltemList.get(i55);
								/////////////////
								String class_id = classInfos5[m];
								// 添加数据
								int count5 = 1;
								pstm.setString(count5++, map
										.get(exltemList.get(i55).getId()));
								pstm.setBoolean(count5++, false);
								pstm.setBoolean(count5++, false);
								pstm.setBoolean(count5++, false);
								pstm.setString(count5++, experiment.getSylb() + "");
								String type = ExItemDAO.getType(schedule.getId())
										.get(exltem.getId());
								int type_id = 5;
								if (type.equals("演示"))
									type_id = 1;
								if (type.equals("验证"))
									type_id = 2;
								if (type.equals("综合"))
									type_id = 3;
								if (type.equals("设计"))
									type_id = 4;
								pstm.setString(count5++, type_id + "");
								pstm.setString(count5++, mxzys5[m].substring(0, 6)); // 实验者专业
								pstm.setString(count5++, experiment.getCourseID());
								pstm.setString(count5++, course_name5);
								String requirment = experiment.getRequirement();
								int num_requirment = 1;
								if (requirment.equals("必修")) {
									num_requirment = 1;
								}
								if (requirment.equals("选修")) {
									num_requirment = 2;
								}
								if (requirment.equals("其他")) {
									num_requirment = 3;
								}
								pstm.setString(count5++, num_requirment + "");
								pstm
										.setString(count5++, experiment.getSyzlb()
												+ "");
								int studentNumber = ClassDAO.load(class_id)
										.getStudentNumber();// //////
								pstm.setInt(count5++, studentNumber); // 实验人数
								pstm.setInt(count5++, studentNumber);
								pstm.setInt(count5++, 0);
								int groupNum = schedule.getGroupNum();
								pstm.setInt(count5++, (studentNumber / groupNum)); // 每组人数
								pstm.setInt(count5++, schedule.getWeekTime());
								pstm.setString(count5++, sysxx.getSysdm());
								pstm.setString(count5++, "");
								pstm.setBoolean(count5++, false);
								pstm.executeUpdate();
							}
						}
					}
					
					
					
					
				}
				System.out.println("end");
				return true;
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				System.out.println(e.toString());
				return false;
			} finally {
				if (rs != null) {
					rs.close();
				}
				if (pstm != null) {
					pstm.close();
				}
				if (conn != null) {
					conn.close();
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

}
