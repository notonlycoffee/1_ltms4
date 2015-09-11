package com.ltms.util;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.ltms.model.Admin;

public class LoginFilter implements Filter {

	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain arg2) throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
	    HttpServletResponse res = (HttpServletResponse) response;
	    req.setCharacterEncoding("utf-8");
	    HttpSession session = req.getSession(true);
	    //从session里取的管理员信息
	    Admin admin = (Admin)session.getAttribute("admin");	    
	    //判断如果没有取到管理员信息,就跳转到登陆页面
	    if (admin == null) {
	      //跳转到登陆页面
	      req.getRequestDispatcher("logout.jsp").forward(request, response);
	    }
	    else {
	      //已经登陆,继续此次请求
	    	arg2.doFilter(req,res);
	    }	    
	}

	public void init(FilterConfig request) throws ServletException {
	}
	

	public void destroy() {
	}
}
