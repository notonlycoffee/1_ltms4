package com.ltms.servlet;

import java.io.IOException;

import com.ltms.dao.AdminDAO;
import com.ltms.dao.LogDAO;
import com.ltms.dao.SystemConfigDAO;
import com.ltms.dao.TeacherDAO;
import com.ltms.model.Admin;
import com.ltms.model.Teacher;
import com.ltms.util.Encrypt;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		this.doPost(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String role = request.getParameter("role");
		String[] strings = role.split("_");
		role = strings[0];
		if(role.equals("administrator")) adminLogin(request, response);
		if(role.equals("sysgly")) adminLogin(request, response);
		else if(role.equals("teacher")) teacherLogin(request, response);
	}
		
	public void adminLogin(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException {	
		String id = request.getParameter("id");
		String password = request.getParameter("password");
		String role_ = request.getParameter("role");
		if(id == null || password == null || id.trim().equals("") 
				|| password.trim().equals("")){
			request.setAttribute("error", "用户名或密码不能为空!");
			request.getRequestDispatcher("login.jsp").forward(request, response);
			return;
		}
//		System.out.println("role is " + role_);
		boolean adminExist = AdminDAO.isExistbyLoing(id,role_);
		if(adminExist){
			String[] strings = role_.split("_");
			role_ = strings[0];
			int role = Integer.parseInt(strings[1]);
			Admin admin = AdminDAO.load(id,role_,role);
			String pwd = Encrypt.encrypt(password);	
			if(pwd.equals(admin.getPassword())){
				request.getSession().setAttribute("admin", admin);
				String currenTerm = SystemConfigDAO.getCurrenTerm(admin.getName(),admin.getRole());
				request.getSession().setAttribute("currenTerm", currenTerm);
				request.getSession().setAttribute("admin_name", admin.getName());
				LogDAO.add(admin.getName(), "登陆系统");
				response.sendRedirect("admin/index.jsp");
				return;
			}else{
				request.setAttribute("error", "用户名或密码错误!");
				request.getRequestDispatcher("login.jsp").forward(request, response);	
				return;
			}
		}else{
			request.setAttribute("error", "用户不存在!");
			request.getRequestDispatcher("login.jsp").forward(request, response);	
			return; 
		}
	}
	
	public void teacherLogin(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {	
		String id = request.getParameter("id");
		String password = request.getParameter("password");
		if(id == null || password == null || id.trim().equals("") 
				|| password.trim().equals("")){
			request.setAttribute("error", "用户名或密码不能为空!");
			request.getRequestDispatcher("login.jsp").forward(request, response);
			return;
		}
		boolean teacherExist = TeacherDAO.isExist(id);
		if(teacherExist){
			Teacher teacher = TeacherDAO.load(id);
			String pwd = Encrypt.encrypt(password);	
			if(pwd.equals(teacher.getPassword())){
				Admin admin = AdminDAO.load("teacher");
				request.getSession().setAttribute("admin", admin);
				request.getSession().setAttribute("admin_name", teacher.getName() + "老师");
				request.getSession().setAttribute("teacher", teacher);
				String currenTerm = SystemConfigDAO.getCurrenTerm(teacher.getName() + "老师",2);
				request.getSession().setAttribute("currenTerm", currenTerm);
				LogDAO.add(teacher.getName() + "老师", "登陆系统");
				response.sendRedirect("admin/index.jsp");
				return;
			}else{
				request.setAttribute("error", "用户名或密码错误!");
				request.getRequestDispatcher("login.jsp").forward(request, response);
				return;
			}
		}else{
			request.setAttribute("error", "用户不存在!");
			request.getRequestDispatcher("login.jsp").forward(request, response);	
			return;
		}
	}
}
