package com.ltms.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.ltms.util.DatabaseUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class SyllabusServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		this.doPost(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		String method = request.getParameter("method");
		if(method == null || "".equals(method)) return;
		if(method.equals("weblist")) weblist(request, response);
		else if(method.equals("getAjax")) getAjax(request, response);
		else if(method.equals("getAjax2")) getAjax2(request, response);
		else return;
	}

	private void weblist(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		int laboratoryID = Integer.parseInt(request.getParameter("laboratoryID"));
		int departmentID = Integer.parseInt(request.getParameter("departmentID"));
		
		String year_weblist = request.getParameter("year_weblist");
		String term_weblist = request.getParameter("term_weblist");
		
		if(year_weblist.equals("0") || year_weblist.trim().length() == 0){
			year_weblist = "";
		}
		if(term_weblist.equals("0") || year_weblist.trim().length() == 0){
			term_weblist = "";
		}
		
		String term_ = year_weblist+term_weblist;
		
		String sql = "select * from _experiment where laboratoryID = " + laboratoryID;
		sql = "select e.id, s.id as s_id, l.name as l_name, c.name as c_name, e.classInfo, e.timeInfo, e.term from _laboratory as l, _course as c, _experiment as e, _schedule as s where e.laboratoryID = l.id and e.courseID = c.id and e.id = s.experimentID and e.state = 1 and l.id = ?" + " and e.term like ?";
		sql += " order by e.id desc";
		StringBuffer syllabusString = new StringBuffer();
		StringBuffer monString = new StringBuffer();
		StringBuffer tueString = new StringBuffer();
		StringBuffer wedString = new StringBuffer();
		StringBuffer thuString = new StringBuffer();
		StringBuffer friString = new StringBuffer();
		Connection conn = DatabaseUtil.getConn();
		PreparedStatement pstmt = DatabaseUtil.prepareStmt(conn, sql);
		ResultSet rs = null;
		try {
			pstmt.setInt(1, laboratoryID);
			pstmt.setString(2, "%" + term_ + "%" );
			rs = pstmt.executeQuery();
			if(rs.isAfterLast() == rs.isBeforeFirst()){
				syllabusString.append("<div class=\"no_result_msg\"class=\"select\">暂无实验信息</div>");
			}else{
				/*syllabusString.append("\n");
				syllabusString.append("<ul id=\"out_table_ltms\" class=\"week\">");
				monString.append("<li><a href=\"#\">星期一</a></li>");
				tueString.append("<li><a href=\"#\">星期二</a></li>");
				wedString.append("<li><a href=\"#\">星期三</a></li>");
				thuString.append("<li><a href=\"#\">星期四</a></li>");
				friString.append("<li><a href=\"#\">星期五</a></li>");*/
				/*syllabusString.append("<table id=\"out_table_ltms\">\n");
				monString.append("<tr>\n<td class=\"week\">周一</td>\n<td style=\"border-top: none;\">\n");
				tueString.append("<tr>\n<td class=\"week\">周二</td>\n<td style=\"border-top: none;\">\n\n");
				wedString.append("<tr>\n<td class=\"week\">周三</td>\n<td style=\"border-top: none;\">\n\n");
				thuString.append("<tr>\n<td class=\"week\">周四</td>\n<td style=\"border-top: none;\">\n\n");
				friString.append("<tr>\n<td class=\"week\">周五</td>\n<td style=\"border-top: none;\">\n\n");*/
				StringBuffer mon12 = new StringBuffer();
				StringBuffer mon34 = new StringBuffer();
				StringBuffer mon56 = new StringBuffer();
				StringBuffer mon78 = new StringBuffer();
				StringBuffer mon910 = new StringBuffer();
				StringBuffer tue12 = new StringBuffer();
				StringBuffer tue34 = new StringBuffer();
				StringBuffer tue56 = new StringBuffer();
				StringBuffer tue78 = new StringBuffer();
				StringBuffer tue910 = new StringBuffer();
				StringBuffer wed12 = new StringBuffer();
				StringBuffer wed34 = new StringBuffer();
				StringBuffer wed56 = new StringBuffer();
				StringBuffer wed78 = new StringBuffer();
				StringBuffer wed910 = new StringBuffer();
				StringBuffer thu12 = new StringBuffer();
				StringBuffer thu34 = new StringBuffer();
				StringBuffer thu56 = new StringBuffer();
				StringBuffer thu78 = new StringBuffer();
				StringBuffer thu910 = new StringBuffer();
				StringBuffer fri12 = new StringBuffer();
				StringBuffer fri34 = new StringBuffer();
				StringBuffer fri56 = new StringBuffer();
				StringBuffer fri78 = new StringBuffer();
				StringBuffer fri910 = new StringBuffer();
				while(rs.next()){
					String classInfo[] = rs.getString("classInfo").split("@");
					String class_string = classInfo[0];
					
					int _length_ = classInfo.length;
					if(rs.getString("classInfo").endsWith("theendofthestring")){
						_length_--;
					}
					
					for(int j=2; j<_length_; j++){
						class_string += "&nbsp;" + classInfo[j++];
					}
					String time[] = rs.getString("timeInfo").split("@");
					int coun1t = 1;
					for(String tss : time){
						String week_string = "";
						if(coun1t!=1) week_string += "&nbsp;,&nbsp;";
						String week[] = tss.split(",");
						int week_length = week.length;
						if(week[0].equals("0")){
							week_string += "第" + week[1] + "周 - 第" + week[2] + "周(" + week[3] + ")";
						}else{
							week_string += "第";
							for(int ij=1; ij<week_length-4; ij++){
								week_string += week[ij];
								if(ij != week_length - 5){
									week_string += "、";
								}
							}
							week_string += "周";
						}
						String syllabus_string = "<a target=\"_new\" href=\"ScheduleServlet?method=export_pdf&id=" + rs.getInt("s_id") + "\">" + rs.getString("c_name")+"<br/>\n";
						String syllabus_lName ="<a target=\"_new\" href=\"ScheduleServlet?method=export_pdf&id=" + rs.getInt("s_id") + "\">"+ rs.getString("l_name")+"<br/>\n";
						String syllabus_cString = class_string + "<br/>\n";
						String syllabus_wString =  week_string + "<br/><br/>\n";
						/*String syllabus_string = "<p><a target=\"_new\" href=\"ScheduleServlet?method=export_pdf&id=" + rs.getInt("s_id") + "\">" + rs.getString("c_name") + "&nbsp;|&nbsp" + rs.getString("l_name") + "&nbsp;|&nbsp" + class_string + "&nbsp;|&nbsp" + week_string + "&nbsp;第" + String.format("%02d", Integer.parseInt(week[week_length-2])) + "节&nbsp;-&nbsp;第" + String.format("%02d", Integer.parseInt(week[week_length-1])) 
						+ "节" + "&nbsp;|&nbsp" +  rs.getString("term") + "</a></p><br/>\n";*/
						if(week[week_length-3].equals("周一")){
							if(week[week_length-2].equals("1") || week[week_length-2].equals("2")){
								mon12.append(syllabus_string);
								mon12.append(syllabus_lName);
								mon12.append(syllabus_cString);
								mon12.append(syllabus_wString);
							}else if(week[week_length-2].equals("3") || week[week_length-2].equals("4")){
								mon34.append(syllabus_string);
								mon34.append(syllabus_lName);
								mon34.append(syllabus_cString);
								mon34.append(syllabus_wString);
							}else if(week[week_length-2].equals("5") || week[week_length-2].equals("6")){
								mon56.append(syllabus_string);
								mon56.append(syllabus_lName);
								mon56.append(syllabus_cString);
								mon56.append(syllabus_wString);
							}else if(week[week_length-2].equals("7") || week[week_length-2].equals("8")){
								mon78.append(syllabus_string);
								mon78.append(syllabus_lName);
								mon78.append(syllabus_cString);
								mon78.append(syllabus_wString);
							}else if(week[week_length-2].equals("9") || week[week_length-2].equals("10")){
								mon910.append(syllabus_string);
								mon910.append(syllabus_lName);
								mon910.append(syllabus_cString);
								mon910.append(syllabus_wString);
							}  
						}else if(week[week_length-3].equals("周二")){
							if(week[week_length-2].equals("1") || week[week_length-2].equals("2")){
								tue12.append(syllabus_string);
								tue12.append(syllabus_lName);
								tue12.append(syllabus_cString);
								tue12.append(syllabus_wString);
							}else if(week[week_length-2].equals("3") || week[week_length-2].equals("4")){
								tue34.append(syllabus_string);
								tue34.append(syllabus_lName);
								tue34.append(syllabus_cString);
								tue34.append(syllabus_wString);
							}else if(week[week_length-2].equals("5") || week[week_length-2].equals("6")){
								tue56.append(syllabus_string);
								tue56.append(syllabus_lName);
								tue56.append(syllabus_cString);
								tue56.append(syllabus_wString);
							}else if(week[week_length-2].equals("7") || week[week_length-2].equals("8")){
								tue78.append(syllabus_string);
								tue78.append(syllabus_lName);
								tue78.append(syllabus_cString);
								tue78.append(syllabus_wString);
							}else if(week[week_length-2].equals("9") || week[week_length-2].equals("10")){
								tue910.append(syllabus_string);
								tue910.append(syllabus_lName);
								tue910.append(syllabus_cString);
								tue910.append(syllabus_wString);
							}  
						}else if(week[week_length-3].equals("周三")){
							if(week[week_length-2].equals("1") || week[week_length-2].equals("2")){
								wed12.append(syllabus_string);
								wed12.append(syllabus_lName);
								wed12.append(syllabus_cString);
								wed12.append(syllabus_wString);
							}else if(week[week_length-2].equals("3") || week[week_length-2].equals("4")){
								wed34.append(syllabus_string);
								wed34.append(syllabus_lName);
								wed34.append(syllabus_cString);
								wed34.append(syllabus_wString);
							}else if(week[week_length-2].equals("5") || week[week_length-2].equals("6")){
								wed56.append(syllabus_string);
								wed56.append(syllabus_lName);
								wed56.append(syllabus_cString);
								wed56.append(syllabus_wString);
							}else if(week[week_length-2].equals("7") || week[week_length-2].equals("8")){
								wed78.append(syllabus_string);
								wed78.append(syllabus_lName);
								wed78.append(syllabus_cString);
								wed78.append(syllabus_wString);
							}else if(week[week_length-2].equals("9") || week[week_length-2].equals("10")){
								wed910.append(syllabus_string);
								wed910.append(syllabus_lName);
								wed910.append(syllabus_cString);
								wed910.append(syllabus_wString);
							}  
						}else if(week[week_length-3].equals("周四")){
							if(week[week_length-2].equals("1") || week[week_length-2].equals("2")){
								thu12.append(syllabus_string);
								thu12.append(syllabus_lName);
								thu12.append(syllabus_cString);
								thu12.append(syllabus_wString);
							}else if(week[week_length-2].equals("3") || week[week_length-2].equals("4")){
								thu34.append(syllabus_string);
								thu34.append(syllabus_lName);
								thu34.append(syllabus_cString);
								thu34.append(syllabus_wString);
							}else if(week[week_length-2].equals("5") || week[week_length-2].equals("6")){
								thu56.append(syllabus_string);
								thu56.append(syllabus_lName);
								thu56.append(syllabus_cString);
								thu56.append(syllabus_wString);
							}else if(week[week_length-2].equals("7") || week[week_length-2].equals("8")){
								thu78.append(syllabus_string);
								thu78.append(syllabus_lName);
								thu78.append(syllabus_cString);
								thu78.append(syllabus_wString);
							}else if(week[week_length-2].equals("9") || week[week_length-2].equals("10")){
								thu910.append(syllabus_string);
								thu910.append(syllabus_lName);
								thu910.append(syllabus_cString);
								thu910.append(syllabus_wString);
							}  
						}else if(week[week_length-3].equals("周五")){
							if(week[week_length-2].equals("1") || week[week_length-2].equals("2")){
								fri12.append(syllabus_string);
								fri12.append(syllabus_lName);
								fri12.append(syllabus_cString);
								fri12.append(syllabus_wString);
							}else if(week[week_length-2].equals("3") || week[week_length-2].equals("4")){
								fri34.append(syllabus_string);
								fri34.append(syllabus_lName);
								fri34.append(syllabus_cString);
								fri34.append(syllabus_wString);
							}else if(week[week_length-2].equals("5") || week[week_length-2].equals("6")){
								fri56.append(syllabus_string);
								fri56.append(syllabus_lName);
								fri56.append(syllabus_cString);
								fri56.append(syllabus_wString);
							}else if(week[week_length-2].equals("7") || week[week_length-2].equals("8")){
								fri78.append(syllabus_string);
								fri78.append(syllabus_lName);
								fri78.append(syllabus_cString);
								fri78.append(syllabus_wString);
							}else if(week[week_length-2].equals("9") || week[week_length-2].equals("10")){
								fri910.append(syllabus_string);
								fri910.append(syllabus_lName);
								fri910.append(syllabus_cString);
								fri910.append(syllabus_wString);
							}  
						}
					}
				}
				/*
				monString.append("<table border=\"0\" cellspacing=\"0\" cellpadding=\"0\">");
				monString.append("<tr>");
				monString.append("<th class=\"new_title\">一，二节</th>");
				monString.append("<th class=\"new_title\">三，四节</th>");
				monString.append("<th class=\"new_title\">五，六节</th>");
				monString.append("<th class=\"new_title\">七，八节</th>");
				monString.append("<th class=\"last_title\">九，十节</th>");
				monString.append("</tr>");
				monString.append("<tr>");
				monString.append("<td>" + mon12.toString() + "</td>\n");
				monString.append("<td>" + mon34.toString() + "</td>\n");
				monString.append("<td>" + mon56.toString() + "</td>\n");
				monString.append("<td>" + mon78.toString() + "</td>\n");
				monString.append("<td class=\"last\">" + mon910.toString() + "</td>\n");
				monString.append("</tr>");
				monString.append("</table>");
				*/
				monString.append("<table class=\"in_table_ltms\">\n");
				monString.append("<tr>\n");
				monString.append("<p style=\"color:#A5A8A7;text-align: center;font-family:\"微软雅黑\";\">周一</p>");
				monString.append("<td class=\"in_table_ltms_time\">1、2节</td>\n");
				monString.append("<td class=\"in_table_ltms_ex\">" + mon12.toString() + "&nbsp;</td>\n");
				monString.append("</tr>\n");
				monString.append("<tr>\n");
				monString.append("<td class=\"in_table_ltms_time\">3、4节</td>\n");
				monString.append("<td class=\"in_table_ltms_ex\">" + mon34.toString() + "&nbsp;</td>\n");
				monString.append("</tr>\n");
				monString.append("<tr>\n");
				monString.append("<td class=\"in_table_ltms_time\">5、6节</td>\n");
				monString.append("<td class=\"in_table_ltms_ex\">" + mon56.toString() + "&nbsp;</td>\n");
				monString.append("</tr>\n");
				monString.append("<tr>\n");
				monString.append("<td class=\"in_table_ltms_time\">7、8节</td>\n");
				monString.append("<td class=\"in_table_ltms_ex\">" + mon78.toString() + "&nbsp;</td>\n");
				monString.append("</tr>\n");
				monString.append("<tr>\n");
				monString.append("<td class=\"in_table_ltms_time\">9、10节</td>\n");
				monString.append("<td class=\"in_table_ltms_ex\">" + mon910.toString() + "&nbsp;</td>\n");
				monString.append("</tr>\n");
				monString.append("</table>\n");
				monString.append("</td>\n</tr>\n");
				
				tueString.append("<table class=\"in_table_ltms\">\n");
				tueString.append("<tr>\n");
				tueString.append("<p style=\"color:#A5A8A7;text-align: center;font-family:\"微软雅黑\";\">周二</p>");
				tueString.append("<td class=\"in_table_ltms_time\">1、2节</td>\n");
				tueString.append("<td class=\"in_table_ltms_ex\">" + tue12.toString() + "&nbsp;</td>\n");
				tueString.append("</tr>\n");
				tueString.append("<tr>\n");
				tueString.append("<td class=\"in_table_ltms_time\">3、4节</td>\n");
				tueString.append("<td class=\"in_table_ltms_ex\">" + tue34.toString() + "&nbsp;</td>\n");
				tueString.append("</tr>\n");
				tueString.append("<tr>\n");
				tueString.append("<td class=\"in_table_ltms_time\">5、6节</td>\n");
				tueString.append("<td class=\"in_table_ltms_ex\">" + tue56.toString() + "&nbsp;</td>\n");
				tueString.append("</tr>\n");
				tueString.append("<tr>\n");
				tueString.append("<td class=\"in_table_ltms_time\">7、8节</td>\n");
				tueString.append("<td class=\"in_table_ltms_ex\">" + tue78.toString() + "&nbsp;</td>\n");
				tueString.append("</tr>\n");
				tueString.append("<tr>\n");
				tueString.append("<td class=\"in_table_ltms_time\">9、10节</td>\n");
				tueString.append("<td class=\"in_table_ltms_ex\">" + tue910.toString() + "&nbsp;</td>\n");
				tueString.append("</tr>\n");
				tueString.append("</table>\n");
				tueString.append("</td>\n</tr>\n");
				tueString.append("</td>\n</tr>\n");
				
				wedString.append("<table class=\"in_table_ltms\">\n");
				wedString.append("<tr>\n");
				wedString.append("<p style=\"color:#A5A8A7;text-align: center;font-family:\"微软雅黑\";\">周三</p>");
				wedString.append("<td class=\"in_table_ltms_time\">1、2节</td>\n");
				wedString.append("<td class=\"in_table_ltms_ex\">" + wed12.toString() + "&nbsp;</td>\n");
				wedString.append("</tr>\n");
				wedString.append("<tr>\n");
				wedString.append("<td class=\"in_table_ltms_time\">3、4节</td>\n");
				wedString.append("<td class=\"in_table_ltms_ex\">" + wed34.toString() + "&nbsp;</td>\n");
				wedString.append("</tr>\n");
				wedString.append("<tr>\n");
				wedString.append("<td class=\"in_table_ltms_time\">5、6节</td>\n");
				wedString.append("<td class=\"in_table_ltms_ex\">" + wed56.toString() + "&nbsp;</td>\n");
				wedString.append("</tr>\n");
				wedString.append("<tr>\n");
				wedString.append("<td class=\"in_table_ltms_time\">7、8节</td>\n");
				wedString.append("<td class=\"in_table_ltms_ex\">" + wed78.toString() + "&nbsp;</td>\n");
				wedString.append("</tr>\n");
				wedString.append("<tr>\n");
				wedString.append("<td class=\"in_table_ltms_time\">9、10节</td>\n");
				wedString.append("<td class=\"in_table_ltms_ex\">" + wed910.toString() + "&nbsp;</td>\n");
				wedString.append("</tr>\n");
				wedString.append("</table>\n");
				wedString.append("</td>\n</tr>\n");
				wedString.append("</td>\n</tr>\n");
				
				thuString.append("<table class=\"in_table_ltms\">\n");
				thuString.append("<tr>\n");
				thuString.append("<p style=\"color:#A5A8A7;text-align: center;font-family:\"微软雅黑\";\">周四</p>");
				thuString.append("<td class=\"in_table_ltms_time\">1、2节</td>\n");
				thuString.append("<td class=\"in_table_ltms_ex\">" + thu12.toString() + "&nbsp;</td>\n");
				thuString.append("</tr>\n");
				thuString.append("<tr>\n");
				thuString.append("<td class=\"in_table_ltms_time\">3、4节</td>\n");
				thuString.append("<td class=\"in_table_ltms_ex\">" + thu34.toString() + "&nbsp;</td>\n");
				thuString.append("</tr>\n");
				thuString.append("<tr>\n");
				thuString.append("<td class=\"in_table_ltms_time\">5、6节</td>\n");
				thuString.append("<td class=\"in_table_ltms_ex\">" + thu56.toString() + "&nbsp;</td>\n");
				thuString.append("</tr>\n");
				thuString.append("<tr>\n");
				thuString.append("<td class=\"in_table_ltms_time\">7、8节</td>\n");
				thuString.append("<td class=\"in_table_ltms_ex\">" + thu78.toString() + "&nbsp;</td>\n");
				thuString.append("</tr>\n");
				thuString.append("<tr>\n");
				thuString.append("<td class=\"in_table_ltms_time\">9、10节</td>\n");
				thuString.append("<td class=\"in_table_ltms_ex\">" + thu910.toString() + "&nbsp;</td>\n");
				thuString.append("</tr>\n");
				thuString.append("</table>\n");
				thuString.append("</td>\n</tr>\n");
				thuString.append("</td>\n</tr>\n");
				
				friString.append("<table class=\"in_table_ltms\">\n");
				friString.append("<tr>\n");
				friString.append("<p style=\"color:#A5A8A7;text-align: center;font-family:\"微软雅黑\";\">周五</p>");
				friString.append("<td class=\"in_table_ltms_time\">1、2节</td>\n");
				friString.append("<td class=\"in_table_ltms_ex\">" + fri12.toString() + "&nbsp;</td>\n");
				friString.append("</tr>\n");
				friString.append("<tr>\n");
				friString.append("<td class=\"in_table_ltms_time\">3、4节</td>\n");
				friString.append("<td class=\"in_table_ltms_ex\">" + fri34.toString() + "&nbsp;</td>\n");
				friString.append("</tr>\n");
				friString.append("<tr>\n");
				friString.append("<td class=\"in_table_ltms_time\">5、6节</td>\n");
				friString.append("<td class=\"in_table_ltms_ex\">" + fri56.toString() + "&nbsp;</td>\n");
				friString.append("</tr>\n");
				friString.append("<tr>\n");
				friString.append("<td class=\"in_table_ltms_time\">7、8节</td>\n");
				friString.append("<td class=\"in_table_ltms_ex\">" + fri78.toString() + "&nbsp;</td>\n");
				friString.append("</tr>\n");
				friString.append("<tr>\n");
				friString.append("<td class=\"in_table_ltms_time\">9、10节</td>\n");
				friString.append("<td class=\"in_table_ltms_ex\">" + fri910.toString() + "&nbsp;</td>\n");
				friString.append("</tr>\n");
				friString.append("</table>\n");
				friString.append("</td>\n</tr>\n");
				friString.append("</td>\n</tr>\n");
				syllabusString.append(monString.toString());
				syllabusString.append(tueString.toString());
				syllabusString.append(wedString.toString());
				syllabusString.append(thuString.toString());
				syllabusString.append(friString.toString());
				syllabusString.append("</table>\n");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DatabaseUtil.close(rs);
			DatabaseUtil.close(pstmt);
			DatabaseUtil.close(conn);
		}
		request.setAttribute("syllabusString", syllabusString.toString());
		request.setAttribute("laboratoryID", laboratoryID);
		request.setAttribute("departmentID", departmentID);
		request.setAttribute("searched", "searched");
		request.getRequestDispatcher("syllabus.jsp").forward(request, response);
	}
	
	public void getAjax(HttpServletRequest request, HttpServletResponse response)
	throws ServletException, IOException{
		Connection conn = DatabaseUtil.getConn();
		PreparedStatement pstmt = null;
		int departmentID = Integer.parseInt(request.getParameter("departmentID"));
		String sql = "select * from _laboratory where departmentID = ?";
		ResultSet rs = null;
		try {
			pstmt = DatabaseUtil.prepareStmt(conn, sql);
			pstmt.setInt(1, departmentID);
			rs = pstmt.executeQuery();
			response.setContentType("text/html;charset=utf-8");
			response.setCharacterEncoding("utf-8");
			PrintWriter out = response.getWriter(); 
			StringBuffer ilastring = new StringBuffer();
			ilastring.append("<select id='laboratoryID' name='laboratoryID' class ='select'>");
			while(rs.next()) {
				ilastring.append("<option value='" + rs.getInt("id") + "'>" + rs.getString("name") + "</option>");
			}
			if(ilastring.toString().equals("<select id='laboratoryID' name='laboratoryID'  class='select'>")){
				ilastring.append("<option value='0'>该系别暂无实验室信息</option>");
			}
			ilastring.append("</select>");
			out.print(ilastring.toString());
			out.flush();
			out.close(); 
		}catch (SQLException e) {
			e.printStackTrace();
		}finally{		
			DatabaseUtil.close(rs);
			DatabaseUtil.close(pstmt);
			DatabaseUtil.close(conn);	
		}
	}	
	
	public void getAjax2(HttpServletRequest request, HttpServletResponse response)
	throws ServletException, IOException{
		Connection conn = DatabaseUtil.getConn();
		PreparedStatement pstmt = null;
		int departmentID = Integer.parseInt(request.getParameter("departmentID"));
		String sql = "select * from _teacher where departmentID = ?";
		ResultSet rs = null;
		try {
			pstmt = DatabaseUtil.prepareStmt(conn, sql);
			pstmt.setInt(1, departmentID);
			rs = pstmt.executeQuery();
			response.setContentType("text/html;charset=utf-8");
			response.setCharacterEncoding("utf-8");
			PrintWriter out = response.getWriter(); 
			StringBuffer ilastring = new StringBuffer();
			ilastring.append("<select name='teacherID' id='teacherID' class='select'><option value='0'>--选择教师--</option>");
			while(rs.next()) {
				ilastring.append("<option value='" + rs.getInt("id") + "'>" + rs.getString("name") + "</option>");
			}
			ilastring.append("</select>");
			out.print(ilastring.toString());
			out.flush();
			out.close(); 
		}catch (SQLException e) {
			e.printStackTrace();
		}finally{		
			DatabaseUtil.close(rs);
			DatabaseUtil.close(pstmt);
			DatabaseUtil.close(conn);	
		}
	}	
}
