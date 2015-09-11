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

public class DBFJdbcOdbcTest {
	public static boolean add_dbf(String url, Map<Integer, String> map,
			String _currenTerm_) {
		
		//存放3:1的实验室
		ArrayList<Integer> arr = new ArrayList<Integer>();
		
		// 获取实验室信息
		List<Laboratory> sysList = LaboratoryDAO.load_laboratory();
		
		for(Laboratory dep : sysList){
			Sysxx loadSysxx = LaboratoryDAO.load_sysxxByTeam(dep.getId(),_currenTerm_);
			Sysqk sysqk = SysxxDAO.getSysqkByTeam(dep.getId(), _currenTerm_);
			Sysjf loadsysjf = SysxxDAO.loadsysjfByTeam(dep.getId(), _currenTerm_);
			
			if(loadSysxx.getId()!=0 && sysqk.getId()!=0 && loadsysjf.getId()!=0){
				arr.add(dep.getId());
			}
			
		}
		
		int cou = 1;
		
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
					if(arr.contains(list.get(i).getId())){
//						System.out.println("sssssssssss  " + list.get(i).getDepartmentID());
						
						String name = list.get(i).getName();
						// 根据id获取三个实验室相关表的信息
						Sysxx sysxx = LaboratoryDAO.load_sysxxByTeam(id,_currenTerm_);
						Sysqk sysqk = SysxxDAO.getSysqkByTeam(id, _currenTerm_);
						Sysjf sysjf = SysxxDAO.loadsysjfByTeam(id, _currenTerm_);
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
					System.out.println("list2");
					Schedule schedule = ScheduleDAO
							.getScheduleByExperimentID(list2.get(i2)
									.getId());
					
					List<ExItem> exltemList = ExItemDAO.loadExitem(schedule
							.getId());
					
					Experiment experiment = list2.get(i2);
					int laboratory_id = experiment.getLaboratoryID();
					Sysxx sysxx = LaboratoryDAO.load_sysxxByTeam(laboratory_id,_currenTerm_);
					if(sysxx.getSysdm() != null){
						
						if(experiment.getMxzy().length()>8){
							for (int i22 = 0; i22 < exltemList.size(); i22++) {
								ExItem exltem = exltemList.get(i22);
								System.out.println("exltem");
								int count = 1;
								System.out.println("sysdm is " + sysxx.getSysdm());
								pstm.setString(count++, sysxx.getSysdm());
								System.out.println("id is " + exltemList.get(i22).getId());
								pstm.setString(count++, map.get(exltemList.get(i22).getId()));
								System.out.println("value is " + map.get(exltemList.get(i22).getId()));
								System.out.println("itemname is " + exltem.getItemName());
								pstm.setString(count++, exltem.getItemName());
								System.out.println("xs is " + experiment.getXs());
								pstm.setInt(count++, experiment.getXs());
								System.out.println("sylb is " + experiment.getSylb());
								pstm.setString(count++, experiment.getSylb() + "");

								String type = ExItemDAO.getType(schedule.getId()).get(
										exltem.getId());
								int type_id = 5;
								System.out.println("type null is " + type);
								if(type == null){
									type_id = 5;
								}else{
									if (type.equals("演示"))
										type_id = 1;
									if (type.equals("验证"))
										type_id = 2;
									if (type.equals("综合"))
										type_id = 3;
									if (type.equals("设计"))
										type_id = 4;
								}
								
								System.out.println("type id is " + String.valueOf(type_id));
								pstm.setString(count++, String.valueOf(type_id));
								System.out.println("mxzy is " + experiment.getMxzy().substring(0,6));
								pstm.setString(count++, experiment.getMxzy().substring(0,6));
								pstm.setString(count++, "," + experiment.getMxzy());////////////////////////
								pstm.setBoolean(count++, false);
								pstm.setBoolean(count++, false);
								pstm.setBoolean(count++, true);
								pstm.setBoolean(count++, false);
								pstm.setBoolean(count++, false);
								pstm.setString(count++, "");
								System.out.println("map.get(exltemList.get(i22).getId()) is " + map.get(exltemList.get(i22).getId()));
								pstm.setString(count++, map.get(exltemList.get(i22).getId()));
								System.out.println("exit333");
								pstm.setBoolean(count++, false);
								pstm.executeUpdate();
								
							}
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
						String mxzyString = experiment.getMxzy();
						if(experiment.getMxzy().length()<8){
							mxzyString = "";
						}
						zy_Map.put(experiment.getId(), mxzyString);
						sy_Map.put(experiment.getId(), symes);
					}else{
						//获取对应的id值
						int id3 = map_id.get(experiment.getCourseID());
						//专业往后添加
						if(experiment.getMxzy().length() > 7 ){
							zy_Map.put(id3, zy_Map.get(id3) + experiment.getMxzy());
						}
						
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
				
				//存放已经存在的实验室人员的编号
				ArrayList<String> sysryList = new ArrayList<String>();
				
				// //增加人员信息
				pstm = conn
						.prepareStatement("insert into ryk values(?,?,?,?,?,?,?,?,?,?,"
								+ "?,?,?,?,?,?,?)");
				List<Laboratory> list4 = LaboratoryDAO.load_laboratory();
				for (int i4 = 0; i4 < list4.size(); i4++) {
					// 实验室id
					int id4 = list4.get(i4).getId();
					
					if(arr.contains(list.get(i4).getId())){
						System.out.println("sssssssssss  " + list.get(i4).getDepartmentID());
						//获取实验室代码
						//只获取有用的实验室
						Sysxx sysxx = LaboratoryDAO.load_sysxxByTeam(id4, _currenTerm_);
						String sys_dm = sysxx.getSysdm();
						if (sys_dm != null && !sys_dm.trim().equals("11111")) {
							// 获取对应的实验室管理人员id
							String sysry_id = SysxxDAO.getsysryById(id4);
							// 获取实验室管理人员的基本情况
							Sys_Admin sys_admin = SysxxDAO.loadsysry(sysry_id);
							if(sys_admin.getBh() != null){
								if(!sysryList.contains(sys_admin.getBh())){
									// 开始数据插入
									sysryList.add(sys_admin.getBh());
									int count4 = 1;
									pstm.setString(count4++, sys_admin.getBh());
									pstm.setString(count4++, sys_admin.getName());
									pstm.setString(count4++, sys_admin.getSex() + "");
									pstm.setString(count4++, sys_admin.getBirthday());
									pstm.setString(count4++, "");
									pstm.setString(count4++, sys_admin.getSxzy());
									pstm.setString(count4++, sys_admin.getZyzw());
									String whcdsString = sys_admin.getWhcd() + "";
									if(sys_admin.getWhcd() == 3 || sys_admin.getWhcd() == 4){
										whcdsString = "0" + sys_admin.getWhcd();
									}
									pstm.setString(count4++, whcdsString);
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
				}

				System.out.println("kaichu");
				// //增加开出信息
				pstm = conn
						.prepareStatement("insert into xmjl values(?,?,?,?,?,?,?,?,?,?,"
								+ "?,?,?,?,?,?,?,?,?)");
				List<Experiment> list5 = ExperimentDAO.loadbyterm(_currenTerm_);
				for (int i5 = 0; i5 < list5.size(); i5++) {

					System.out.println("enter i5 " + i5);
					// 根据课程id获取课程名称
					Experiment experiment = list5.get(i5);
					
					if(arr.contains(experiment.getLaboratoryID())){
//						System.out.println("idididididid is " + experiment.getDepartmentID());
						String course_name5 = CourseDAO.getName(experiment
								.getCourseID());
						// 获取实验室名称
						String laboratory_name5 = (LaboratoryDAO.load(experiment
								.getLaboratoryID())).getName();
						
						Sysxx sysxx = LaboratoryDAO.load_sysxxByTeam(experiment
								.getLaboratoryID(), _currenTerm_);
						
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
							System.out.println("enter m " + m);

							Schedule schedule_ = ScheduleDAO
									.getScheduleByExperimentID(list5.get(i5)
											.getId());
							List<ExItem> exltemList = ExItemDAO
									.loadExitem(schedule_.getId());
							System.out.println("mxzys5[m].length() is " +mxzys5[m].length());
							if(mxzys5[m].length()>7){
								for (int i55 = 0; i55 < exltemList.size(); i55++) {
									System.out.println("enter i55 " + i55);
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
									System.out.println("type id5 is " + type);
									
									int type_id = 5;
									
									if(type == null){
										type_id = 5;
									}else{
										if (type.equals("演示"))
											type_id = 1;
										if (type.equals("验证"))
											type_id = 2;
										if (type.equals("综合"))
											type_id = 3;
										if (type.equals("设计"))
											type_id = 4;
									}
									pstm.setString(count5++, type_id + "");
									System.out.println("mxzy is " + mxzys5[m]);
									pstm.setString(count5++, mxzys5[m].substring(0, 6)); // 实验者专业
									pstm.setString(count5++, experiment.getCourseID());
									pstm.setString(count5++, course_name5);
									String requirment = experiment.getRequirement();
									System.out.println("requirment is " + requirment);
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
									
									//如果课程没有打开过就没有人数纪录
									int studentNumber = ClassDAO.load(class_id).getStudentNumber();// //////
									pstm.setInt(count5++, studentNumber); // 实验人数
									pstm.setInt(count5++, studentNumber);
									pstm.setInt(count5++, 0);
									int groupNum = schedule.getGroupNum();
									
									int stduentN = ExperimentDAO.loadNumber(schedule.getId());
									
//									if(stduentN / groupNum == 0){
//										System.out.println("num is " + stduentN);
//										System.out.println("group is " + groupNum);
//										System.out.println("experiment is " + experiment.getId());
//									}
//									
//									//这个注意要去掉
//									if(stduentN == 0){
//										stduentN = groupNum * 10;
//									}
									int mzrs_ = stduentN / groupNum;
									if(mzrs_>99){
										mzrs_ = 99;
									}
									pstm.setInt(count5++, mzrs_); // 每组人数,长度最多只能为2
									pstm.setInt(count5++, schedule.getExTime());//实验学时数
									pstm.setString(count5++, sysxx.getSysdm());
									pstm.setString(count5++, "");
									pstm.setBoolean(count5++, false);
									System.out.println("before");
									pstm.executeUpdate();
									System.out.println("end");
								}
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
