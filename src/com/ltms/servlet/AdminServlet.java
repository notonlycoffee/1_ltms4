package com.ltms.servlet;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.ltms.dao.AdminDAO;
import com.ltms.dao.LogDAO;
import com.ltms.dao.TeacherDAO;
import com.ltms.model.Admin;
import com.ltms.model.Sys_Admin;
import com.ltms.model.Teacher;
import com.ltms.util.DatabaseUtil;
import com.ltms.util.Encrypt;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AdminServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		this.doPost(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String method = request.getParameter("method");
		if (method == null || "".equals(method))
			return;
		if (method.equals("add"))
			add(request, response);
		else if (method.equals("delete"))
			delete(request, response);
		else if (method.equals("update"))
			update(request, response);
		else if (method.equals("reset_password"))
			reset_password(request, response);
		else if (method.equals("addsyszr"))
			addsyszr(request, response);
		else if (method.equals("updateSysry"))
			updateSysry(request, response);
		else
			return;
	}

	private void updateSysry(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String sysry_id_ = ((Admin)request.getSession().getAttribute("admin")).getId();
		Sys_Admin admin_ = AdminDAO.loadSysry(sysry_id_);
		
		
		// 获取数据
		String sysry_id = request.getParameter("sysry_id");
		String name = request.getParameter("sy_name_id");
		if(name.length()>8){
			request.setAttribute("msg", "名字过长");
			request.getRequestDispatcher("editSysryqk.jsp").forward(request, response);
			return ;
		}
		String birthday = request.getParameter("birthday");
		int rylb = Integer.parseInt(request.getParameter("rylb"));
		int sex = Integer.parseInt(request.getParameter("sex"));
		int whcd = Integer.parseInt(request.getParameter("whcd"));
		//所學專業 
		String ssxk_zy = request.getParameter("ssxk_zy");
		String zyzw = request.getParameter("zyzw");
		//專家類型
		String[] zjlxs = request.getParameterValues("zjlx");
		int gn = Integer.parseInt(request.getParameter("gn"));
		int gnf = Integer.parseInt(request.getParameter("gnf"));
		int gw = Integer.parseInt(request.getParameter("gw"));
		int gwf = Integer.parseInt(request.getParameter("gwf"));
		if(gn>999 || gnf>999 || gw>999 || gwf>999){
			request.setAttribute("msg", "进修培训时间最多填999");
			request.getRequestDispatcher("editSysryqk.jsp").forward(request, response);
			return ;
		}
		String bz = request.getParameter("specialty");
		
		if(ssxk_zy.trim().length() == 1 || "".equals(ssxk_zy)){
			ssxk_zy = admin_.getSxzy();
		}
		 
		// 检验数据
		if("".equals(birthday) || birthday.trim().length() == 0 || rylb == 0 || sex == 0 || whcd == 0 || zyzw.trim().length()==1 || "".equals(zyzw)){
			request.setAttribute("msg", "填齐该填的数据");
			request.getRequestDispatcher("editSysryqk.jsp").forward(request, response);
			return ;
		}
		//检验出生年月 
		if(birthday.trim().length() == 6){
			try {
				Integer.parseInt(birthday.trim());
			} catch (Exception e) {
				// TODO: handle exception
				request.setAttribute("msg", "年月格式错误");
				request.getRequestDispatcher("editSysryqk.jsp").forward(request, response);
				return ;
			}
		}else{
			request.setAttribute("msg", "年月格式错误");
			request.getRequestDispatcher("editSysryqk.jsp").forward(request, response);
			return ;
		}
		
		//专家类型
		String zjlx = "";
		System.out.println("zjlx is " + zjlx);
		if(zjlxs != null){
			for(int i=0; i<zjlxs.length; i++){
				zjlx += zjlxs[i];
			}
		}
		int zjlx_num ;
		String zjlx_term = zjlx.trim();
		if("".equals(zjlx) || zjlx.trim().length() == 0){
			zjlx_num = 0;
		}else{
			zjlx_num = Integer.parseInt(zjlx.trim());
		}
		
		
		//封装数据
		Sys_Admin sys_admin = new Sys_Admin();
		sys_admin.setBh(sysry_id);
		sys_admin.setBirthday(birthday);
		sys_admin.setBz(bz);
		sys_admin.setGnfxljy(gnf);
		sys_admin.setGnxljy(gn);
		sys_admin.setGwfxljy(gwf);
		sys_admin.setGwxljy(gw);
		sys_admin.setId(sysry_id);
		sys_admin.setName(name);
		sys_admin.setRylb(rylb);
		sys_admin.setSex(sex);
		sys_admin.setSxzy(ssxk_zy);
		sys_admin.setWhcd(whcd);
		sys_admin.setZjlx(zjlx_num);
		sys_admin.setZyzw(zyzw);
		
		
		
		boolean b = AdminDAO.checkSysry(sysry_id);
		
		if(b){
			//有，更新
			AdminDAO.updateSysry(sys_admin);
		}else{
			//没有，插入
			AdminDAO.insertSysry(sys_admin);
		}
		
			LogDAO.add((String) request.getSession().getAttribute(
					"admin_name"), "以实验室人员的角色，更新个人情况");
		
		request.setAttribute("msg", "修改成功");
		request.getRequestDispatcher("editSysryqk.jsp").forward(request, response);
	}

	private void addsyszr(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub

		String id = request.getParameter("teacherID");
		if (AdminDAO.isExist(id)) {
			request.setAttribute("msg", "该教师已经被设置为管理员!");
			request.getRequestDispatcher("admin_syszr.jsp")
					.forward(request, response);
			return;
		} else {
			int departmentID = Integer.parseInt(request
					.getParameter("departmentID"));
			int role = Integer.parseInt(request.getParameter("role"));
			String name = TeacherDAO.getName(id);
			Connection conn = DatabaseUtil.getConn();
			String sql = "insert into _admin values(?, ?, ?, ?, ?)";
			PreparedStatement pstmt = DatabaseUtil.prepareStmt(conn, sql);
			try {
				int i = 1;
				pstmt.setString(i++, id);
				pstmt.setString(i++, name);
				pstmt.setString(i++, Encrypt.encrypt("123456")); // 默认密码MD5加密
				pstmt.setInt(i++, role);
				pstmt.setInt(i++, departmentID);
				pstmt.executeUpdate();
				request.getSession().removeAttribute("adminList");
				request.setAttribute("msg", "添加成功");
				if (role == 1) {
					LogDAO.add((String) request.getSession().getAttribute(
							"admin_name"), "增加教务员账号[" + id + "," + name + "]");
					request.getRequestDispatcher("admin_de.jsp").forward(
							request, response);
				} else if (role == 4) {
					LogDAO
							.add((String) request.getSession().getAttribute(
									"admin_name"), "增加实验室主任账号[" + id + ","
									+ name + "]");
					request.getRequestDispatcher("admin_syszr.jsp").forward(
							request, response);
				} else if (role == 3) {
					LogDAO.add((String) request.getSession().getAttribute(
							"admin_name"), "增加系主管领导账号[" + id + "," + name + "]");
					request.getRequestDispatcher("admin_la.jsp").forward(
							request, response);
				}else if (role == 5) {
					LogDAO.add((String) request.getSession().getAttribute(
					"admin_name"), "实验室管理员账号[" + id + "," + name + "]");
					request.getRequestDispatcher("admin_la.jsp").forward(
					request, response);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				DatabaseUtil.close(pstmt);
				DatabaseUtil.close(conn);
			}
		}

	}

	public void add(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		int role = Integer.parseInt(request.getParameter("role"));
		
		if (((Admin) request.getSession().getAttribute("admin")).getRole() != 0) {
			request.setAttribute("msg", "对不起，您不是系统管理员，无法创建管理员账号!");
			request.getRequestDispatcher("error.jsp")
					.forward(request, response);
			return;
		}
		String id = request.getParameter("teacherID");
		if (AdminDAO.isExist(id,role)) {
			request.setAttribute("msg", "该教师已经被设置为管理员!");
			request.getRequestDispatcher("admin_la.jsp")
					.forward(request, response);
			return;
		} else {
			int departmentID = Integer.parseInt(request
					.getParameter("departmentID"));
			
			String name = TeacherDAO.getName(id);
			Connection conn = DatabaseUtil.getConn();
			String sql = "insert into _admin values(?, ?, ?, ?, ?)";
			PreparedStatement pstmt = DatabaseUtil.prepareStmt(conn, sql);
			try {
				int i = 1;
				pstmt.setString(i++, id);
				pstmt.setString(i++, name);
				pstmt.setString(i++, Encrypt.encrypt("123456")); // 默认密码MD5加密
				pstmt.setInt(i++, role);
				pstmt.setInt(i++, departmentID);
				pstmt.executeUpdate();
				request.getSession().removeAttribute("adminList");
				request.setAttribute("msg", "添加成功");
				if (role == 1) {
					LogDAO.add((String) request.getSession().getAttribute(
							"admin_name"), "增加教务员账号[" + id + "," + name + "]");
					request.getRequestDispatcher("admin_de.jsp").forward(
							request, response);
				} else {
					LogDAO
							.add((String) request.getSession().getAttribute(
									"admin_name"), "增加系主管领导账号[" + id + ","
									+ name + "]");
					request.getRequestDispatcher("admin_la.jsp").forward(
							request, response);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				DatabaseUtil.close(pstmt);
				DatabaseUtil.close(conn);
			}
		}
	}

	public void delete(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String id = request.getParameter("id");
		Admin admin = (Admin) request.getSession().getAttribute("admin");
		int role = Integer.parseInt(request.getParameter("role"));
		if (id.equals("admin") || id.equals("teacher")) {
			request.setAttribute("msg", "不能删除管理员账号!");
			request.getRequestDispatcher("error.jsp")
					.forward(request, response);
			return;
		} else {
			Connection conn = DatabaseUtil.getConn();
			String sql = "delete from _admin where id = ?";
			String sql1 = "delete from _sysfp where id_syszr = ?";
			PreparedStatement pstmt = DatabaseUtil.prepareStmt(conn, sql);
			PreparedStatement pstmt1 = DatabaseUtil.prepareStmt(conn, sql1);
			try {
				pstmt.setString(1, id);
				pstmt1.setString(1, id);
				pstmt.executeUpdate();
				pstmt1.executeUpdate();
				request.getSession().removeAttribute("adminList");
				request.setAttribute("msg", "删除成功");
				if (role == 1) {
					LogDAO.add((String) request.getSession().getAttribute(
							"admin_name"), "删除教务员账号[" + id + "]");
					request.getRequestDispatcher("admin_de.jsp").forward(
							request, response);
				} else if (role == 3) {
					LogDAO.add((String) request.getSession().getAttribute(
							"admin_name"), "删除系主管领导账号[" + id + "]");
					request.getRequestDispatcher("admin_la.jsp").forward(
							request, response);
				} else if (role == 4) {
					LogDAO.add((String) request.getSession().getAttribute(
							"admin_name"), "删除实验室主任账号[" + id + "]");
					request.getRequestDispatcher("admin_syszr.jsp").forward(
							request, response);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				DatabaseUtil.close(pstmt);
				DatabaseUtil.close(conn);
			}
		}
	}

	public void update(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		Admin admin = (Admin) request.getSession().getAttribute("admin");
		String oldpwd = request.getParameter("oldpwd");
		String newpwd = request.getParameter("newpwd");
		String newpwd1 = request.getParameter("newpwd1");
		Connection conn = DatabaseUtil.getConn();
		String pwd = Encrypt.encrypt(oldpwd); // 将旧密码MD5加密
		if (admin.getRole() == 2) {
			Teacher teacher = (Teacher) request.getSession().getAttribute(
					"teacher");
			if (pwd.equals(teacher.getPassword())) {
				if (!newpwd.trim().equals(newpwd1)) {
					request.setAttribute("msg", "您输入的两次新密码不一致!");
					request.getRequestDispatcher("error.jsp").forward(request,
							response);
					return;
				} else {
					String sql = "update _teacher set password = ? where id = ?";
					PreparedStatement pstmt = null;
					try {
						pstmt = DatabaseUtil.prepareStmt(conn, sql);
						String newpwd2 = Encrypt.encrypt(newpwd);
						; // 将新密码MD5加密
						pstmt.setString(1, newpwd2);
						pstmt.setString(2, teacher.getId());
						pstmt.executeUpdate();
						LogDAO.add((String) request.getSession().getAttribute(
								"admin_name"), "修改密码");
						response.sendRedirect("logout.jsp");
						return;
					} catch (SQLException e) {
						request.setAttribute("msg", "修改密码失败!");
						request.getRequestDispatcher("error.jsp").forward(
								request, response);
						return;
					} finally {
						DatabaseUtil.close(pstmt);
						DatabaseUtil.close(conn);
					}
				}
			} else {
				request.setAttribute("msg", "原密码错误!");
				request.getRequestDispatcher("error.jsp").forward(request,
						response);
				return;
			}
		} else {
			if (pwd.equals(admin.getPassword())) {
				if (!newpwd.trim().equals(newpwd1)) {
					request.setAttribute("msg", "您输入的两次新密码不一致!");
					request.getRequestDispatcher("error.jsp").forward(request,
							response);
					return;
				} else {
					String sql = "update _admin set password = ? where id = ?";
					PreparedStatement pstmt = null;
					try {
						pstmt = DatabaseUtil.prepareStmt(conn, sql);
						String newpwd2 = Encrypt.encrypt(newpwd);
						; // 将新密码MD5加密
						pstmt.setString(1, newpwd2);
						pstmt.setString(2, admin.getId());
						pstmt.executeUpdate();
						LogDAO.add((String) request.getSession().getAttribute(
								"admin_name"), "修改密码");
						response.sendRedirect("logout.jsp");
						return;
					} catch (SQLException e) {
						request.setAttribute("msg", "修改密码失败!");
						request.getRequestDispatcher("error.jsp").forward(
								request, response);
						return;
					} finally {
						DatabaseUtil.close(pstmt);
						DatabaseUtil.close(conn);
					}
				}
			} else {
				request.setAttribute("msg", "原密码错误!");
				request.getRequestDispatcher("error.jsp").forward(request,
						response);
				return;
			}
		}
	}

	public void reset_password(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String id = request.getParameter("id");
		int role = Integer.parseInt(request.getParameter("role"));
		String default_password = "123456";
		Admin admin = (Admin) request.getSession().getAttribute("admin");
		Connection conn = DatabaseUtil.getConn();
		String sql = "update _admin set password = ? where id = ?";
		PreparedStatement pstmt = DatabaseUtil.prepareStmt(conn, sql);
		try {
			int i = 1;
			pstmt.setString(i++, Encrypt.encrypt(default_password));
			pstmt.setString(i++, id);
			pstmt.executeUpdate();
			request.getSession().removeAttribute("adminList");
			request.setAttribute("msg", "重置密码成功");
			if (role == 1) {
				LogDAO.add((String) request.getSession().getAttribute(
						"admin_name"), "重置教务员[" + id + "]密码");
				request.getRequestDispatcher("admin_de.jsp").forward(request,
						response);
			} else if (role == 3) {
				LogDAO.add((String) request.getSession().getAttribute(
						"admin_name"), "重置系主管[" + id + "]密码");
				request.getRequestDispatcher("admin_la.jsp").forward(request,
						response);
			} else if (role == 4) {
				LogDAO.add((String) request.getSession().getAttribute(
						"admin_name"), "重置实验室主任[" + id + "]密码");
				request.getRequestDispatcher("admin_syszr.jsp").forward(
						request, response);
			}else if(role == 5){
				LogDAO.add((String) request.getSession().getAttribute(
				"admin_name"), "重置实验室管理员[" + id + "]密码");
				request.getRequestDispatcher("admin_glygl.jsp").forward(
				request, response);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DatabaseUtil.close(pstmt);
			DatabaseUtil.close(conn);
		}
	}
}
