package com.ltms.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.jspsmart.upload.Request;
import com.jspsmart.upload.SmartUpload;
import com.ltms.dao.AdminDAO;
import com.ltms.dao.DepartmentDAO;
import com.ltms.dao.LaboratoryDAO;
import com.ltms.dao.LogDAO;
import com.ltms.dao.SysxxDAO;
import com.ltms.dao.TeacherDAO;
import com.ltms.model.Admin;
import com.ltms.model.Laboratory;
import com.ltms.model.Sysjf;
import com.ltms.model.Sysqk;
import com.ltms.model.Sysxx;
import com.ltms.util.DatabaseUtil;
import com.ltms.util.Encrypt;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LaboratoryServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		this.doPost(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String method = request.getParameter("method");
		if(method == null){
			SmartUpload mySmartUpload = new SmartUpload();
			mySmartUpload.initialize(getServletConfig(),request,response);
			try {
				mySmartUpload.setAllowedFilesList("jpg,jpeg,gif,png,bmp,BMP,JPG,JPEG,GIF,PNG");
			//上载文件 
				mySmartUpload.upload();  //这句话执行后才能取得参数
			} catch (Exception e){
				request.setAttribute("msg", "只允许上传jpg、gif、png以及bmp类型的图片文件!");
				request.getRequestDispatcher("error.jsp").forward(request, response);
			  	return;
			}
			Request req = mySmartUpload.getRequest();
			method = req.getParameter("method");
			if(method.equals("update")) update(request, response, req, mySmartUpload);
			else return;
		}else if(!"".equals(method)){
			if(method.equals("list")) list(request, response);
			else if(method.equals("add")) add(request, response);
			else if(method.equals("delete")) delete(request, response);
			else if(method.equals("search")) search(request, response);
			else if(method.equals("webList")) webList(request, response);
			else if(method.equals("getAjax")) getAjax(request, response);
			else if(method.equals("getAjax2")) getAjax2(request, response);
			else if(method.equals("getIndexAjax")) getIndexAjax(request, response);
			else if(method.equals("updateSysfp")) updateSysfp(request,response);
			else if(method.equals("list_syszr_sys"))list_syszr_sys(request,response);
			else if(method.equals("update_glygl_admin")) update_glygl_admin(request,response);
			else if(method.equals("getAjax_zy")) getAjax_zy(request,response);
			else if(method.equals("getAjax_sykc")) getAjax_sykc(request,response);
			else if(method.equals("getAjax_mxzy")) getAjax_mxzy(request,response);
			else if(method.equals("gly_glsys")) gly_glsys(request,response);
			else if(method.equals("list_admin_sys")) list_admin_sys(request,response);
			else if(method.equals("updata_sysxx")) updata_sysxx(request,response);
			else if(method.equals("updata_sysqk")) updata_sysqk(request,response);
			else if(method.equals("update_sysjf")) update_sysjf(request,response);
			else if(method.equals("list_glzd")) list_glzd(request,response);
			else if(method.equals("setYear")) setYear(request,response);
			else if(method.equals("checkLab")) checkLab(request,response);
			else return;
		}
	}
	
	private void checkLab(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String currenTerm = (String) request.getSession().getAttribute("currenTerm");
		if("".equals(currenTerm) || currenTerm.trim().length() == 0){
			
			request.setAttribute("msg", "请先选择学年学期");
			request.getRequestDispatcher("showMsg.jsp").forward(request, response);
			return ;
		}
		currenTerm = currenTerm.substring(0,9);
		int depID = ((Admin)request.getSession().getAttribute("admin")).getDepartmentID();
		//存放不是3:1的实验室
		ArrayList<Laboratory> arr = new ArrayList<Laboratory>();
		
		// 获取实验室信息
		List<Laboratory> sysList = LaboratoryDAO.load_laboratoryByDep(depID);
		
		for(Laboratory dep : sysList){
			Sysxx loadSysxx = LaboratoryDAO.load_sysxxByTeam(dep.getId(),currenTerm);
			Sysqk sysqk = SysxxDAO.getSysqkByTeam(dep.getId(), currenTerm);
			Sysjf loadsysjf = SysxxDAO.loadsysjfByTeam(dep.getId(), currenTerm);
			
			if(loadSysxx.getId()!=0 && sysqk.getId()!=0 && loadsysjf.getId()!=0){}
			else arr.add(dep);
		}
		
		request.setAttribute("DepArr", arr);
		request.getRequestDispatcher("checkLab.jsp").forward(request, response);
	}

	private void setYear(HttpServletRequest request,
			HttpServletResponse response) {
		// TODO Auto-generated method stub
		String year = request.getParameter("year");
		Calendar cal = Calendar.getInstance();
		
		if(year.equals("0")){
			int year_ = cal.get(Calendar.YEAR);
			int month_ = cal.get(Calendar.MONTH);
			if(month_<8)year = (year_ - 1) + "-" + year_;
			else year = year_ + "-" + (year_+1);
		}
		request.getSession().setAttribute("_year_", year);
		System.out.println("year is " + request.getSession().getAttribute("_year_"));
	}

	private void update_sysjf(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String currenTerm = (String) request.getSession().getAttribute("currenTerm");
		currenTerm = currenTerm.substring(0,9);
		
		int id;
		double yqsbgzjfhj;
		double qzjxyqgzjf;
		double yqsbwhjfhj;
		
		double qzjxyqwhjf;
		double syjxyxjfhj;
		double qzjxsynchf;
		
		double sysjsjf;
		double syjxyjggjf;
		double qtjf;
		
		try {
			id = Integer.parseInt(request.getParameter("id"));
			yqsbgzjfhj = Double.parseDouble(request.getParameter("yqsbgzjfhj"));
			qzjxyqgzjf = Double.parseDouble(request.getParameter("qzjxyqgzjf"));
			yqsbwhjfhj = Double.parseDouble(request.getParameter("yqsbwhjfhj"));
			qzjxyqwhjf = Double.parseDouble(request.getParameter("qzjxyqwhjf"));
			syjxyxjfhj = Double.parseDouble(request.getParameter("syjxyxjfhj"));
			qzjxsynchf = Double.parseDouble(request.getParameter("qzjxsynchf"));
			sysjsjf = Double.parseDouble(request.getParameter("sysjsjf"));
			syjxyjggjf = Double.parseDouble(request.getParameter("syjxyjggjf"));
			qtjf = Double.parseDouble(request.getParameter("qtjf"));
		} catch (Exception e) {
			// TODO: handle exception
			request.setAttribute("msg", "格式必须是数字");
			request.getRequestDispatcher("editSysjf.jsp").forward(request, response);
			return ;
		}
		
		if(String.valueOf(yqsbgzjfhj).length()>8 || String.valueOf(qzjxyqgzjf).length()>8 
				||String.valueOf(yqsbwhjfhj).length()>8
				||String.valueOf(qzjxyqwhjf).length()>8
				||String.valueOf(syjxyxjfhj).length()>8
				||String.valueOf(qzjxsynchf).length()>8
				||String.valueOf(sysjsjf).length()>8
				||String.valueOf(syjxyjggjf).length()>8
				||String.valueOf(qtjf).length()>8){
					request.setAttribute("msg", "填写数字长度最多8位");
					request.getRequestDispatcher("editSysjf.jsp").forward(request, response);
					return ;
				}
		
		//检验数据
		if(yqsbgzjfhj<qzjxyqgzjf){
			request.setAttribute("msg", "教学仪器购置经费须小于仪器设备购置经费合计");
			request.getRequestDispatcher("editSysjf.jsp").forward(request, response);
			return ;
		}
		if(yqsbwhjfhj<qzjxyqwhjf){
			request.setAttribute("msg", "教学仪器维护经费须小于仪器设备维护经费合计");
			request.getRequestDispatcher("editSysjf.jsp").forward(request, response);
			return ;
		}
		if(syjxyxjfhj<qzjxsynchf){
			request.setAttribute("msg", "教学实验年材耗费须小于实验教学运行经费合计");
			request.getRequestDispatcher("editSysjf.jsp").forward(request, response);
			return ;
		}
		
		//封装数据
		Sysjf sysjf = new Sysjf();
		sysjf.setId(id);
		sysjf.setQtjf(qtjf);
		sysjf.setQzjxsynchf(qzjxsynchf);
		sysjf.setQzjxyqgzjf(qzjxyqgzjf);
		sysjf.setQzjxyqwhjf(qzjxyqwhjf);
		sysjf.setSyjxyjggjf(syjxyjggjf);
		sysjf.setSyjxyxjfhj(syjxyxjfhj);
		sysjf.setSysjsjf(sysjsjf);
		sysjf.setYqsbgzjfhj(yqsbgzjfhj);
		sysjf.setYqsbwhjfhj(yqsbwhjfhj);
		
		//判断是否存在
		boolean sign = SysxxDAO.checkSysjf(id,currenTerm);
		if(sign){
			//存在就更新
			SysxxDAO.updateSysjf(sysjf,currenTerm);
		}else{
			//不存在就插入
			SysxxDAO.addSysjf(sysjf,currenTerm);
		}
		request.setAttribute("msg", "修改成功");
		request.getRequestDispatcher("editSysjf.jsp").forward(request, response);
		
	}

	private void updata_sysqk(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String currenTerm = (String) request.getSession().getAttribute("currenTerm");
		currenTerm = currenTerm.substring(0,9);
		
		Sysqk sysqk = new Sysqk();
		try {
			// TODO Auto-generated method stub
			//获取数据
			int id = Integer.parseInt(request.getParameter("id"));
			int jshjycg_gjj = Integer.parseInt(request
					.getParameter("jshjycg_gjj"));
			int jshjycg_sbj = Integer.parseInt(request
					.getParameter("jshjycg_sbj"));
			int jshjycg_zl = Integer.parseInt(request
					.getParameter("jshjycg_zl"));
			int jshjycg_xshj = Integer.parseInt(request
					.getParameter("jshjycg_xshj"));
			if(jshjycg_gjj>99 || jshjycg_sbj>9 || jshjycg_zl>99 || jshjycg_xshj>99){
				request.setAttribute("msg", "教师获奖与成果的值最多为2位数");
				request.getRequestDispatcher("editSysqk.jsp").forward(request, response);
				return ;
			}
			int sdjssl_jxlw = Integer.parseInt(request
					.getParameter("sdjssl_jxlw"));
			int sdjssl_kylw = Integer.parseInt(request
					.getParameter("sdjssl_kylw"));
			int hxkw_jxlw = Integer.parseInt(request.getParameter("hxkw_jxlw"));
			int hxkw_kylw = Integer.parseInt(request.getParameter("hxkw_kylw"));
			if(sdjssl_jxlw>999 || sdjssl_kylw>999){
				request.setAttribute("msg", "三个检索收录的值最多为3位数");
				request.getRequestDispatcher("editSysqk.jsp").forward(request, response);
				return ;
			}
			if(hxkw_jxlw>999 || hxkw_kylw>999){
				request.setAttribute("msg", "核心刊物的值最多为3位数");
				request.getRequestDispatcher("editSysqk.jsp").forward(request, response);
				return ;
			}
			int syhc = Integer.parseInt(request.getParameter("syhc"));
			if(syhc>99){
				request.setAttribute("msg", "实验耗材的值最多为2位数");
				request.getRequestDispatcher("editSysqk.jsp").forward(request, response);
				return ;
			}
			int kyxms_sbjys = Integer.parseInt(request
					.getParameter("kyxms_sbjys"));
			if(kyxms_sbjys>99){
				request.setAttribute("msg", "科研项目数省部级以上的值最多为2位数");
				request.getRequestDispatcher("editSysqk.jsp").forward(request, response);
				return ;
			}
			int kyxms_qt = Integer.parseInt(request.getParameter("kyxms_qt"));
			if(kyxms_qt>99){
				request.setAttribute("msg", "科研项目数其他的值最多为2位数");
				request.getRequestDispatcher("editSysqk.jsp").forward(request, response);
				return ;
			}
			int shfwxms = Integer.parseInt(request.getParameter("shfwxms"));
			if(shfwxms>999){
				request.setAttribute("msg", "社会服务项目数的值最多为3位数");
				request.getRequestDispatcher("editSysqk.jsp").forward(request, response);
				return ;
			}
			int jyxms_sbjys = Integer.parseInt(request
					.getParameter("jyxms_sbjys"));
			int jyxms_qt = Integer.parseInt(request.getParameter("jyxms_qt"));
			if(jyxms_sbjys>999 || jyxms_qt>999){
				request.setAttribute("msg", "教研项目数的值最多为3位数");
				request.getRequestDispatcher("editSysqk.jsp").forward(request, response);
				return ;
			}
			int bysjjlwrs_zks = Integer.parseInt(request
					.getParameter("bysjjlwrs_zks"));
			int bysjjlwrs_bks = Integer.parseInt(request
					.getParameter("bysjjlwrs_bks"));
			int bysjjlwrs_yjs = Integer.parseInt(request
					.getParameter("bysjjlwrs_yjs"));
			if(bysjjlwrs_zks>9999 || bysjjlwrs_bks>9999 || bysjjlwrs_yjs>9999){
				request.setAttribute("msg", "毕业设计及论文人数的值最多为4位数");
				request.getRequestDispatcher("editSysqk.jsp").forward(request, response);
				return ;
			}
			int kfsyqk_xnsygs = Integer.parseInt(request
					.getParameter("kfsyqk_xnsygs"));
			int kfsyqk_xwsygs = Integer.parseInt(request
					.getParameter("kfsyqk_xwsygs"));
			int kfsyqk_xnsyrs = Integer.parseInt(request
					.getParameter("kfsyqk_xnsyrs"));
			int kfsyqk_xwsyrs = Integer.parseInt(request
					.getParameter("kfsyqk_xwsyrs"));
			
			int kfsyqk_xnrss = Integer.parseInt(request
					.getParameter("kfsyqk_xnrss"));
			int kfsyqk_xwrss = Integer.parseInt(request
					.getParameter("kfsyqk_xwrss"));
			if(kfsyqk_xnsygs>999999 || kfsyqk_xwsygs>999999 || kfsyqk_xnsyrs>999999 || kfsyqk_xwsyrs>999999
					 || kfsyqk_xnrss>999999 || kfsyqk_xwrss>999999){
				request.setAttribute("msg", "开放实验情况的值最多为6位数");
				request.getRequestDispatcher("editSysqk.jsp").forward(request, response);
				return ;
			}
			int jzrys = Integer.parseInt(request.getParameter("jzrys"));
			if(jzrys > 999){
				request.setAttribute("msg", "兼职人员数的值最多为3位数");
				request.getRequestDispatcher("editSysqk.jsp").forward(request, response);
				return ;
			}
			
			//封装数据
			sysqk.setBysjjlwrs_bks(bysjjlwrs_bks);
			sysqk.setBysjjlwrs_yjs(bysjjlwrs_yjs);
			sysqk.setBysjjlwrs_zks(bysjjlwrs_zks);
			sysqk.setHxkw_jxlw(hxkw_jxlw);
			sysqk.setHxkw_kylw(hxkw_kylw);
			sysqk.setId(id);
			sysqk.setJshjycg_gjj(jshjycg_gjj);
			sysqk.setJshjycg_sbj(jshjycg_sbj);
			sysqk.setJshjycg_xshj(jshjycg_xshj);
			sysqk.setJshjycg_zl(jshjycg_zl);
			sysqk.setJyxms_qt(jyxms_qt);
			sysqk.setJyxms_sbjys(jyxms_sbjys);
			sysqk.setJzrys(jzrys);
			sysqk.setKfsyqk_xnrss(kfsyqk_xnrss);
			sysqk.setKfsyqk_xnsygs(kfsyqk_xnsygs);
			sysqk.setKfsyqk_xnsyrs(kfsyqk_xnsyrs);
			sysqk.setKfsyqk_xwrss(kfsyqk_xwrss);
			sysqk.setKfsyqk_xwsygs(kfsyqk_xwsygs);
			sysqk.setKfsyqk_xwsyrs(kfsyqk_xwsyrs);
			sysqk.setKyxms_qt(kyxms_qt);
			sysqk.setKyxms_sbjys(kyxms_sbjys);
			sysqk.setSdjssl_jxlw(sdjssl_jxlw);
			sysqk.setSdjssl_kylw(sdjssl_kylw);
			sysqk.setShfwxms(shfwxms);
			sysqk.setSyhc(syhc);
			
			//判断是否存在
			boolean sign = SysxxDAO.checkSysqkByTeam(id, currenTerm);
			if(sign){
				//存在就更新
				SysxxDAO.updateSysqk(sysqk,currenTerm);
			}else{
				//不存在就插入
				SysxxDAO.addSysqk(sysqk,currenTerm);
			}
			request.setAttribute("msg", "修改成功");
			request.getRequestDispatcher("editSysqk.jsp").forward(request, response);
			
		} catch (Exception e) {
			// TODO: handle exception
			request.setAttribute("msg", "数据必填且都为数字");
			request.getRequestDispatcher("editSysqk.jsp").forward(request, response);
			return ;
		}
		
		
		
		
		
		
	}

	private void updata_sysxx(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String currenTerm = (String) request.getSession().getAttribute("currenTerm");
		currenTerm = currenTerm.substring(0,9);
		
		Connection conn = DatabaseUtil.getConn();
		PreparedStatement pstmt = null;
		ResultSet rs = null;	
		
		//获取数据
		int id = Integer.parseInt(request.getParameter("id"));
//		System.out.println("id is " + id);
		String sysdm = request.getParameter("sysdm");
//		System.out.println("sysdm is " + sysdm);
		int syslb = Integer.parseInt(request.getParameter("syslb"));
//		System.out.println("syslb is " + syslb);
		int syslx = Integer.parseInt(request.getParameter("syslx"));
//		System.out.println("syslx is " + syslx);
		String jlnf = request.getParameter("jlnf");
//		System.out.println("jlnf is " + jlnf);
		String symj = request.getParameter("symj");
//		System.out.println("symj is " + symj);
		String ssxk = request.getParameter("ssxk_zy");
//		System.out.println("ssxk_zy is " + ssxk);
		String xk_ = request.getParameter("xk_");
		if("".equals(ssxk) || ssxk.trim().length() == 0){
			ssxk = xk_;
		}
		
		if("".equals(sysdm.trim()) || "".equals(jlnf.trim()) || "".equals(symj.trim()) || 
				sysdm.length() == 0 || jlnf.length() == 0 || symj.length() == 0 ){
			request.setAttribute("msg", "所有数据都必须填写");
			request.getRequestDispatcher("editSysxx.jsp?id=" + id).forward(request, response);
			return ;
		}
		
		if(symj.length() > 8 ){
			request.setAttribute("msg", "实验室面积最多8位数");
			request.getRequestDispatcher("editSysxx.jsp?id=" + id).forward(request, response);
			return ;
		}
		
		if(sysdm.length()!=5){
			request.setAttribute("msg", "代码必须是五位字符");
			request.getRequestDispatcher("editSysxx.jsp?id=" + id).forward(request, response);
			return;
		}
		if(ssxk.trim().equals("null") || ssxk.trim().length() == 0){
			request.setAttribute("msg", "所属学科必填");
			request.getRequestDispatcher("editSysxx.jsp?id=" + id).forward(request, response);
			return;
		}
		
		try {
			String term_sysdm = sysdm.substring(3, 5);
			Integer.parseInt(term_sysdm);
		} catch (Exception e) {
			// TODO: handle exception
			request.setAttribute("msg", "第三级必须是数字");
			request.getRequestDispatcher("editSysxx.jsp?id=" + id).forward(request, response);
			return;
		}
		
		
		try {
			int year = Integer.parseInt(jlnf);
			System.out.println("year is " + year);
			if(year<1900){
				request.setAttribute("msg", "年份不能小于1900");
				request.getRequestDispatcher("editSysxx.jsp?id=" + id).forward(request, response);
				return;
			}
			//获取当前系统年份
			Calendar c = Calendar.getInstance();
			int year_term  = c.get(Calendar.YEAR);
			if(year>year_term){
				request.setAttribute("msg", "年份不能大于当前年份");
				request.getRequestDispatcher("editSysxx.jsp?id=" + id).forward(request, response);
				return;
			}
		} catch (Exception e) {
			// TODO: handle exception
			request.setAttribute("msg", "年份格式错误");
			request.getRequestDispatcher("editSysxx.jsp?id=" + id).forward(request, response);
			return;
		}
		
		
		try {
			int form_symj = Integer.parseInt(symj);
		} catch (Exception e) {
			// TODO: handle exception
			request.setAttribute("msg", "面积填写不合法");
			request.getRequestDispatcher("editSysxx.jsp?id=" + id).forward(request, response);
			return;
		}
		
		
		//判断表中是否已经存在数据
		boolean sign = SysxxDAO.checkSysxx(id, currenTerm);
		String sql = "";
		//存在
		if(sign){
			//更新
			sql = "update sysxx set sysdm = ?, syslb = ?, syslx = ?, jlnf = ?, symj = ?, ssxk = ? where id = ? and term_year = ?";
		}else{
			//插入
			sql = "insert into sysxx(sysdm, syslb, syslx, jlnf, symj, ssxk, id, term_year) values(?, ?, ?, ?, ?, ?, ?, ?)";
		}
		pstmt = DatabaseUtil.prepareStmt(conn, sql);
		int i = 1;
		try {
			pstmt.setString(i++, sysdm);
			pstmt.setInt(i++, syslb);
			pstmt.setInt(i++, syslx);
			pstmt.setString(i++, jlnf);
			pstmt.setString(i++, symj);
			pstmt.setString(i++, ssxk);
			pstmt.setInt(i++, id);
			pstmt.setString(i++, currenTerm);
			pstmt.executeUpdate();
			request.setAttribute("msg", "修改成功");
			request.getRequestDispatcher("editSysxx.jsp?id=" + id).forward(request, response);
		} catch (SQLException e) {
			e.printStackTrace();
			request.setAttribute("msg", "添加失败!");
			request.getRequestDispatcher("error.jsp").forward(request, response);
			return;
		}finally{
			DatabaseUtil.close(pstmt);
			DatabaseUtil.close(conn);
		}
		
	}

	private void gly_glsys(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		String id_gly = ((Admin)request.getSession().getAttribute("admin")).getId();
		
		//获取实验室id
		
		//获取实验室记录
		Connection conn = DatabaseUtil.getConn();
		PreparedStatement pstmt = null;
		ResultSet rs = null;	
		int sys_id = 0;
		String sql = "select _laboratory.*,glyfp.* from _laboratory,glyfp where glyfp.id_sys =" +
				" _laboratory.id and id_gly = ? ";
		try {
			pstmt = DatabaseUtil.prepareStmt(conn, sql);
			int i = 1;
			pstmt.setString(i++, id_gly);
			rs = pstmt.executeQuery();
			rs.next();
			sys_id = rs.getInt("id");
			
			
		}catch (SQLException e) {
			e.printStackTrace();
		}finally{		
			DatabaseUtil.close(pstmt);
			DatabaseUtil.close(rs);
			DatabaseUtil.close(conn);	
			//实验室id
			request.setAttribute("id", sys_id);
			request.getRequestDispatcher("editLaboratory.jsp").forward(request, response);
		}
		
	}

	private void update_glygl_admin(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		int id = Integer.parseInt(request.getParameter("id"));
		String id_gly = request.getParameter("id_gly");
		String save_update = request.getParameter("save_update");
		int id_department = Integer.parseInt(request.getParameter("id_department"));
		String id_gly_old = request.getParameter("id_syszr");
		
//		if(AdminDAO.isExist(id_gly)){
//			request.setAttribute("msg", "该教师已经被设置为管理员!");
//			request.getRequestDispatcher("admin.jsp").forward(request, response);
//			return;
//		}else{
			
			int sign = 0 ;
			Connection conn = DatabaseUtil.getConn();
			String sql = "";
			if(save_update.equals("save")){
				sign = 0;
				sql = "insert into glyfp(id_gly, id_department, id_sys) values(?, ?, ?)";
			}else if(save_update.equals("update")){
				sign = 1;
				sql = "update glyfp set id_gly = ?, id_department = ? where id_sys = ? and id_gly = ?";
			}
			PreparedStatement pstmt = null;
			PreparedStatement pstmt_gly = null;
			try {
				
				pstmt = DatabaseUtil.prepareStmt(conn, sql);	
				int i = 1;
				pstmt.setString(i++, id_gly);
				pstmt.setInt(i++, id_department);
				pstmt.setInt(i++, id);
				if(sign == 1){
					pstmt.setString(i++, id_gly_old);
				}
				pstmt.executeUpdate();
				
				//把新加入的管理员添加到_admin表中
				if(!AdminDAO.isExist(id_gly, "gly")){
					String name = TeacherDAO.getName(id_gly);
					String sql_gly = "insert into _admin values(?, ?, ?, ?, ?)";
					pstmt_gly = DatabaseUtil.prepareStmt(conn, sql_gly);
					int i_gly = 1;
					pstmt_gly.setString(i_gly++, id_gly);
					pstmt_gly.setString(i_gly++, name);
					pstmt_gly.setString(i_gly++, Encrypt.encrypt("123456"));  //默认密码MD5加密
					pstmt_gly.setInt(i_gly++, 5);
					pstmt_gly.setInt(i_gly++, id_department);
					pstmt_gly.executeUpdate();	
				}
				
				
//				if(sign == 1){
//					String sql_gly_delete = "delete from _admin where id = ?";
//					PreparedStatement pstmt_gly_delete = DatabaseUtil.prepareStmt(conn, sql_gly_delete);
//					pstmt_gly_delete.setString(1, id_gly_old);
//					pstmt_gly_delete.executeUpdate();	
//				}
				
				
				request.setAttribute("msg", "修改成功");
				LogDAO.add((String)request.getSession().getAttribute("admin_name"), "修改实验室管理员[ID " + id + "]");
				request.getRequestDispatcher("admin_glygl.jsp").forward(request, response);
			}catch (SQLException e) {
				e.printStackTrace();
				request.setAttribute("msg", "修改实验室对应的管理员失败!");
				request.getRequestDispatcher("error.jsp").forward(request, response);
				return;
			}finally{
				DatabaseUtil.close(pstmt);
				DatabaseUtil.close(pstmt_gly);
				DatabaseUtil.close(conn);
			}	
			
//		}
		
		
	}

	private void list_syszr_sys(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		//////////////////
		String currenTerm = (String) request.getSession().getAttribute("currenTerm");
		if("".equals(currenTerm) || currenTerm.trim().length() == 0){
			
			request.setAttribute("msg", "请先选择学年学期");
			request.getRequestDispatcher("showMsg.jsp").forward(request, response);
			return ;
		}
		currenTerm = currenTerm.substring(0,9);
		int depID = ((Admin)request.getSession().getAttribute("admin")).getDepartmentID();
		//存放不是3:1的实验室
		ArrayList<Laboratory> arr = new ArrayList<Laboratory>();
		ArrayList arrID = new ArrayList();
		// 获取实验室信息
		List<Laboratory> sysList = LaboratoryDAO.load_laboratoryByDep(depID);
		
		for(Laboratory dep : sysList){
			Sysxx loadSysxx = LaboratoryDAO.load_sysxxByTeam(dep.getId(),currenTerm);
			Sysqk sysqk = SysxxDAO.getSysqkByTeam(dep.getId(), currenTerm);
			Sysjf loadsysjf = SysxxDAO.loadsysjfByTeam(dep.getId(), currenTerm);
			if(loadSysxx.getId()!=0 && sysqk.getId()!=0 && loadsysjf.getId()!=0){}
			else {
//				System.out.println("arr is " + dep);
//				System.out.println("dep.getId is " + dep.getId());
				arr.add(dep);
				arrID.add(dep.getId());
			}
		}
		request.setAttribute("arrID", arrID);
		request.setAttribute("DepArr", arr);
		//////////////////
		
		int pageNo = 1;
		String strPageNo = request.getParameter("pageNo");
		int departmentId = ((Admin)request.getSession().getAttribute("admin")).getDepartmentID();
		if(strPageNo != null && !strPageNo.trim().equals("")) {
			try {
				pageNo = Integer.parseInt(strPageNo);
			} catch (NumberFormatException e) {
				pageNo = 1;
			} 
		}
		if(pageNo <= 0) pageNo = 1;
		
		ArrayList<Laboratory> laboratoryList = new ArrayList<Laboratory>();
		Map<Integer, String> sysfpMap = null;
		Map<String, String> syszrMap = null;
		Connection conn = DatabaseUtil.getConn();
		ResultSet rsCount = null;
		PreparedStatement pstmt = null;
		Statement stmtCount = null;
		Statement stmt = DatabaseUtil.createStmt(conn);
		ResultSet rs = null;	
		int totalPages = 0;
		int pageSize = 15;  //设置每页显示多少条
		String id_syszr = request.getParameter("id_syszr");
		
		try {
			stmtCount = DatabaseUtil.createStmt(conn);
			String countSQL = "select count(*) from _laboratory where departmentID = " + departmentId;
			String sql = "select _sysfp.*,_laboratory.* from _sysfp,_laboratory" +
					" where _sysfp.id_sys = _laboratory.id and id_syszr = " + id_syszr;
			rsCount = DatabaseUtil.executeQuery(stmtCount, countSQL);
			int totalRecords;
			rsCount.next();
			totalRecords = rsCount.getInt(1);
			totalPages = (totalRecords + pageSize - 1)/pageSize;
			if(totalPages == 0){
				totalPages = 1;
			}
			if(pageNo > totalPages) pageNo = totalPages;
			int startPos = (pageNo-1) * pageSize; 
			sql +=" order by id desc limit ?, ?";
			pstmt = DatabaseUtil.prepareStmt(conn, sql);
			pstmt.setInt(1, startPos);
			pstmt.setInt(2, pageSize);
			rs = pstmt.executeQuery();
			while(rs.next()) {
				Laboratory l = new Laboratory();
				LaboratoryDAO.initFromRS(rs, l);
				laboratoryList.add(l);
			}	
			
			
			//获取实验室id和对应的管理员id的map集合
			sysfpMap = LaboratoryDAO.loadGlygl(departmentId);
			
			//获取key为管理员id号，value为管理员名字的map集合      系别和角色
			syszrMap = LaboratoryDAO.loadSyazr(departmentId,5);
				
		}catch (SQLException e) {
			e.printStackTrace();
		}finally{		
			DatabaseUtil.close(stmtCount);
			DatabaseUtil.close(pstmt);
			DatabaseUtil.close(rsCount);
			DatabaseUtil.close(rs);
			DatabaseUtil.close(stmt);
			DatabaseUtil.close(conn);	
			request.setAttribute("pageNo", pageNo);
			request.setAttribute("totalPages", totalPages);
			request.setAttribute("syszrMap", syszrMap);
			request.setAttribute("sysfpMap", sysfpMap);
			request.setAttribute("laboratoryList", laboratoryList);
			request.getRequestDispatcher("admin_glygl.jsp").forward(request, response);
		}
	}

	private void updateSysfp(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		int id = Integer.parseInt(request.getParameter("id"));
		String id_syszr = request.getParameter("id_syszr");
		String save_update = request.getParameter("save_update");
		int id_department = Integer.parseInt(request.getParameter("id_department"));
		Connection conn = DatabaseUtil.getConn();
		String sql = "";
//		System.out.println("save_update is " + save_update);
		if(save_update.equals("save")){
//			System.out.println("enter");
			sql = "insert into _sysfp(id_syszr, id_department, id_sys) values(?, ?, ?)";
		}else if(save_update.equals("update")){
			sql = "update _sysfp set id_syszr = ?, id_department = ? where id_sys = ?";
		}
		PreparedStatement pstmt = null;
		try {
			pstmt = DatabaseUtil.prepareStmt(conn, sql);	
			int i = 1;
			pstmt.setString(i++, id_syszr);
//			System.out.println(id_syszr);
			pstmt.setInt(i++, id_department);
//			System.out.println(id_department);
			pstmt.setInt(i++, id);
//			System.out.println(id);
			pstmt.executeUpdate();
			request.setAttribute("msg", "修改成功");
			LogDAO.add((String)request.getSession().getAttribute("admin_name"), "修改实验室对应的实验室主任[ID " + id + "]");
			request.getRequestDispatcher("admin_sysfp.jsp").forward(request, response);
		}catch (SQLException e) {
			e.printStackTrace();
			request.setAttribute("msg", "修改实验室对应的实验室主任失败!");
			request.getRequestDispatcher("error.jsp").forward(request, response);
			return;
		}finally{
			DatabaseUtil.close(pstmt);
			DatabaseUtil.close(conn);
		}	
		
	}

	public void list(HttpServletRequest request, HttpServletResponse response)
	throws ServletException, IOException{
		/////////////////////////
		String currenTerm = (String) request.getSession().getAttribute("currenTerm");
		if("".equals(currenTerm) || currenTerm.trim().length() == 0){
			
			request.setAttribute("msg", "请先选择学年学期");
			request.getRequestDispatcher("showMsg.jsp").forward(request, response);
			return ;
		}
		currenTerm = currenTerm.substring(0,9);
		int depID = ((Admin)request.getSession().getAttribute("admin")).getDepartmentID();
		//存放不是3:1的实验室
		ArrayList<Laboratory> arr = new ArrayList<Laboratory>();
		ArrayList arrID = new ArrayList();
		// 获取实验室信息
		List<Laboratory> sysList = LaboratoryDAO.load_laboratoryByDep(depID);
		
		for(Laboratory dep : sysList){
			Sysxx loadSysxx = LaboratoryDAO.load_sysxxByTeam(dep.getId(),currenTerm);
			Sysqk sysqk = SysxxDAO.getSysqkByTeam(dep.getId(), currenTerm);
			Sysjf loadsysjf = SysxxDAO.loadsysjfByTeam(dep.getId(), currenTerm);
			if(loadSysxx.getId()!=0 && sysqk.getId()!=0 && loadsysjf.getId()!=0){}
			else {
//				System.out.println("arr is " + dep);
//				System.out.println("dep.getId is " + dep.getId());
				arr.add(dep);
				arrID.add(dep.getId());
			}
		}
		request.setAttribute("arrID", arrID);
		request.setAttribute("DepArr", arr);
		/////////////////////
		int departmentID = 1;
		try{
			departmentID = Integer.parseInt(request.getParameter("departmentID"));
		}catch (Exception e) {
			departmentID = 1;
		}
		if(departmentID == 0){
			departmentID = 1;
		}
		int pageNo = 1;
		String strPageNo = request.getParameter("pageNo");
		String sign = request.getParameter("lc");
//		System.out.println("sign = " + sign);
		int departmentId = ((Admin)request.getSession().getAttribute("admin")).getDepartmentID();
		if(strPageNo != null && !strPageNo.trim().equals("")) {
			try {
				pageNo = Integer.parseInt(strPageNo);
			} catch (NumberFormatException e) {
				pageNo = 1;
			} 
		}
		if(pageNo <= 0) pageNo = 1;
		ArrayList<Laboratory> laboratoryList = new ArrayList<Laboratory>();
		Map<Integer, String> sysfpMap = null;
		Map<String, String> syszrMap = null;
		Connection conn = DatabaseUtil.getConn();
		ResultSet rsCount = null;
		PreparedStatement pstmt = null;
		Statement stmtCount = null;
		Statement stmt = DatabaseUtil.createStmt(conn);
		ResultSet rs = null;	
		int totalPages = 0;
		int pageSize = 15;  //设置每页显示多少条
		try {
			stmtCount = DatabaseUtil.createStmt(conn);
			String countSQL = "select count(*) from _laboratory where departmentID = " + departmentID;
			String sql = "select * from _laboratory where departmentID = " + departmentID;
			rsCount = DatabaseUtil.executeQuery(stmtCount, countSQL);
			int totalRecords;
			rsCount.next();
			totalRecords = rsCount.getInt(1);
			totalPages = (totalRecords + pageSize - 1)/pageSize;
			if(totalPages == 0){
				totalPages = 1;
			}
			if(pageNo > totalPages) pageNo = totalPages;
			int startPos = (pageNo-1) * pageSize; 
			sql +=" order by id desc limit ?, ?";
			pstmt = DatabaseUtil.prepareStmt(conn, sql);
			pstmt.setInt(1, startPos);
			pstmt.setInt(2, pageSize);
			rs = pstmt.executeQuery();
			while(rs.next()) {
				Laboratory l = new Laboratory();
				LaboratoryDAO.initFromRS(rs, l);
				laboratoryList.add(l);
			}	
			
			//获取实验室id和对应的实验室主任id的map集合
			sysfpMap = LaboratoryDAO.loadSysfp(departmentId);
			
			//获取key为实验室主任id号，value为实验室主任名字的map集合      系别和角色
			syszrMap = LaboratoryDAO.loadSyazr(departmentId,4);
				
		}catch (SQLException e) {
			e.printStackTrace();
		}finally{		
			DatabaseUtil.close(stmtCount);
			DatabaseUtil.close(pstmt);
			DatabaseUtil.close(rsCount);
			DatabaseUtil.close(rs);
			DatabaseUtil.close(stmt);
			DatabaseUtil.close(conn);	
			request.setAttribute("pageNo", pageNo);
			request.setAttribute("totalPages", totalPages);
			request.setAttribute("syszrMap", syszrMap);
			request.setAttribute("sysfpMap", sysfpMap);
			request.setAttribute("laboratoryList", laboratoryList);
			if(sign!=null)
				request.getRequestDispatcher("admin_sysfp.jsp").forward(request, response);
			else
				request.getRequestDispatcher("listLaboratory.jsp").forward(request, response);
		}
	}

	
	public void list_admin_sys(HttpServletRequest request, HttpServletResponse response)
	throws ServletException, IOException{
		
		String currenTerm_ = (String) request.getSession().getAttribute("currenTerm");
		String currenTerm__ = (String) request.getSession().getAttribute("currenTerm");
		if("".equals(currenTerm__) || currenTerm__.trim().length() == 0){
			
			request.setAttribute("msg", "请先选择学年学期");
			request.getRequestDispatcher("showMsg.jsp").forward(request, response);
			return ;
		}
		//////////////////
		currenTerm__ = currenTerm__.substring(0,9);
		int depID = ((Admin)request.getSession().getAttribute("admin")).getDepartmentID();
		//存放不是3:1的实验室
		ArrayList<Laboratory> arr = new ArrayList<Laboratory>();
		ArrayList arrID = new ArrayList();
		// 获取实验室信息
		List<Laboratory> sysList = LaboratoryDAO.load_laboratoryByDep(depID);
		
		for(Laboratory dep : sysList){
			Sysxx loadSysxx = LaboratoryDAO.load_sysxxByTeam(dep.getId(),currenTerm__);
			Sysqk sysqk = SysxxDAO.getSysqkByTeam(dep.getId(), currenTerm__);
			Sysjf loadsysjf = SysxxDAO.loadsysjfByTeam(dep.getId(), currenTerm__);
			if(loadSysxx.getId()!=0 && sysqk.getId()!=0 && loadsysjf.getId()!=0){}
			else {
//				System.out.println("arr is " + dep);
//				System.out.println("dep.getId is " + dep.getId());
				arr.add(dep);
				arrID.add(dep.getId());
			}
		}
		request.setAttribute("arrID", arrID);
		request.setAttribute("DepArr", arr);
		/////////////////
		String sign = "";
		sign = request.getParameter("sign");
		String id = request.getParameter("id");
		ArrayList<Laboratory> laboratoryList = new ArrayList<Laboratory>();
		Connection conn = DatabaseUtil.getConn();
		PreparedStatement pstmt = null;
		ResultSet rs = null;	
		String sql = "select _laboratory.*,glyfp.* from _laboratory,glyfp where glyfp.id_sys =" +
		" _laboratory.id and id_gly = ? ";
		
		pstmt = DatabaseUtil.prepareStmt(conn, sql);
		try {
			pstmt.setString(1, id);
			rs = pstmt.executeQuery();
			while(rs.next()) {
				Laboratory l = new Laboratory();
				LaboratoryDAO.initFromRS(rs, l);
				laboratoryList.add(l);
			}	
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			DatabaseUtil.close(rs);
			DatabaseUtil.close(pstmt);
			DatabaseUtil.close(conn);
			request.setAttribute("laboratoryList", laboratoryList);
			if(sign!=null){
				request.getRequestDispatcher("listLaboratory_sysxx.jsp").forward(request, response);
			}else{
				request.getRequestDispatcher("listLaboratory.jsp").forward(request, response);
			}
			
		}
	}
	
	public void list_glzd(HttpServletRequest request, HttpServletResponse response)
	throws ServletException, IOException{
		String id = request.getParameter("id");
		ArrayList<Laboratory> laboratoryList = new ArrayList<Laboratory>();
		Connection conn = DatabaseUtil.getConn();
		PreparedStatement pstmt = null;
		ResultSet rs = null;	
		String sql = "select _laboratory.*,glyfp.* from _laboratory,glyfp where glyfp.id_sys =" +
		" _laboratory.id and id_gly = ? ";
		
		pstmt = DatabaseUtil.prepareStmt(conn, sql);
		try {
			pstmt.setString(1, id);
			rs = pstmt.executeQuery();
			while(rs.next()) {
				Laboratory l = new Laboratory();
				LaboratoryDAO.initFromRS(rs, l);
				laboratoryList.add(l);
			}	
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			DatabaseUtil.close(rs);
			DatabaseUtil.close(pstmt);
			DatabaseUtil.close(conn);
			request.setAttribute("laboratoryList", laboratoryList);
			request.getRequestDispatcher("addRegulation.jsp").forward(request, response);
			
		}
	}
	
	
	public void add(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException{
		String name = request.getParameter("name");
		String address = request.getParameter("address");
		String departmentID = request.getParameter("departmentID");
		if(name.length()>50){
			request.setAttribute("msg", "实验室名称过长");
			request.getRequestDispatcher("listLaboratory.jsp").forward(request, response);
		}
		Connection conn = DatabaseUtil.getConn();
		String sql = "insert into _laboratory(id, address, departmentID, name) values(null, ?, ?, ?)";
		PreparedStatement pstmt = DatabaseUtil.prepareStmt(conn, sql);
		try {
			int i = 1;
			pstmt.setString(i++, address);
			pstmt.setString(i++, departmentID);
			pstmt.setString(i++, name);
			pstmt.executeUpdate();	
			request.setAttribute("msg", "添加成功");
			LogDAO.add((String)request.getSession().getAttribute("admin_name"), "添加实验室信息[名称 " + name + "]");
			request.getRequestDispatcher("listLaboratory.jsp").forward(request, response);
		} catch (SQLException e) {
			e.printStackTrace();
			request.setAttribute("msg", "添加失败!");
			request.getRequestDispatcher("error.jsp").forward(request, response);
			return;
		}finally{
			DatabaseUtil.close(pstmt);
			DatabaseUtil.close(conn);
		}
	}

	public void delete(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException{
		String id_string = request.getParameter("id");
		int id = Integer.parseInt(id_string);
		Connection conn = DatabaseUtil.getConn();
		String sql = "delete from sysxx where id = ?";
		PreparedStatement pstmt = DatabaseUtil.prepareStmt(conn, sql);
		int scheduleID = 0;
		try {
			conn.setAutoCommit(false);  //事务开始
			//删除sysxx
			pstmt.setInt(1, id);
			pstmt.executeUpdate();	
			//删除sysqk
			sql = "delete from sysqk where id = ?";
			pstmt = DatabaseUtil.prepareStmt(conn, sql);
			pstmt.setInt(1, id);
			pstmt.executeUpdate();	
			//删除sysjf
			sql = "delete from sysjf where id = ?";
			pstmt = DatabaseUtil.prepareStmt(conn, sql);
			pstmt.setInt(1, id);
			pstmt.executeUpdate();	
			//删除glyfp里面所有相关id的实验室
			sql = "delete from glyfp where id_sys = ?";
			pstmt = DatabaseUtil.prepareStmt(conn, sql);
			pstmt.setInt(1, id);
			pstmt.executeUpdate();	
			//删除sysfp中的数据
			sql = "delete from _sysfp where id_sys = ?";
			pstmt = DatabaseUtil.prepareStmt(conn, sql);
			pstmt.setInt(1, id);
			pstmt.executeUpdate();	
			//获取要被删除的experiment的id
			ResultSet rs = null;
			sql = "select * from _experiment where laboratoryID = ?";
			pstmt = DatabaseUtil.prepareStmt(conn, sql);
			pstmt.setInt(1, id);
			rs = pstmt.executeQuery();
			if(rs.next()){
				int experiment_id = rs.getInt("id");
				//调用ExperimentDAO的delete方法同时删除  experiment  course_sy  course_sy_mes
				
				//获取要删除的实验的课程id
				sql = "select * from _experiment where id = ?";
				pstmt = DatabaseUtil.prepareStmt(conn, sql);
				pstmt.setInt(1,experiment_id );
				rs = pstmt.executeQuery();
				rs.next();
				String course_id = rs.getString("courseID");
				
				sql = "delete from _experiment where id = ?";
				pstmt = DatabaseUtil.prepareStmt(conn, sql);
				pstmt.setInt(1, experiment_id);
				pstmt.executeUpdate();
				sql = "select id from _schedule where experimentID = ?";
				pstmt = DatabaseUtil.prepareStmt(conn, sql);
				pstmt.setInt(1, experiment_id);
				rs = pstmt.executeQuery();
				if(rs.next()){
					scheduleID = rs.getInt("id");
				}
				sql = "delete from _schedule where experimentID = ?";
				pstmt = DatabaseUtil.prepareStmt(conn, sql);
				pstmt.setInt(1, experiment_id);
				pstmt.executeUpdate();
				sql = "delete from _exItem where scheduleID = ?";
				pstmt = DatabaseUtil.prepareStmt(conn, sql);
				pstmt.setInt(1, scheduleID);
				pstmt.executeUpdate();
				
		
				
				//判断实验列表中是否还有该课程
				sql = "select * from _experiment where courseID = ?";
				pstmt = DatabaseUtil.prepareStmt(conn, sql);
				pstmt.setString(1,course_id );
				rs = pstmt.executeQuery();
				//没有课程的话，把course_sy和course_sy_mes两个表删掉
				if(!rs.next()){
					sql = "delete from _course_sy where id = ?";
					pstmt = DatabaseUtil.prepareStmt(conn, sql);
					pstmt.setString(1, course_id);
					pstmt.executeUpdate();
					
					sql = "delete from _course_sy_mes where id = ?";
					pstmt = DatabaseUtil.prepareStmt(conn, sql);
					pstmt.setString(1, course_id);
					pstmt.executeUpdate();
				}
				
				
			}
			//删除laboratory
			
			sql = "delete from _laboratory where id = ?";
			pstmt = DatabaseUtil.prepareStmt(conn, sql);
			pstmt.setInt(1, id);
			pstmt.executeUpdate();	
			
			conn.commit();   //事务提交
			conn.setAutoCommit(true);  //设置成自动提交模式
			request.setAttribute("msg", "删除成功");
			LogDAO.add((String)request.getSession().getAttribute("admin_name"), "删除实验室信息[ID " + id + "]");
			request.getRequestDispatcher("listLaboratory.jsp").forward(request, response);
			
		} catch (SQLException e) {
			e.printStackTrace();
			try {
				conn.rollback();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			request.setAttribute("msg", "删除失败!");
			request.getRequestDispatcher("error.jsp").forward(request, response);
			return;
		}finally{
			DatabaseUtil.close(pstmt);
			DatabaseUtil.close(conn);
		}
	}

	public void update(HttpServletRequest request, HttpServletResponse response, Request req, SmartUpload mySmartUpload)
			throws ServletException, IOException{
		int id = Integer.parseInt(req.getParameter("id"));
		String admin = req.getParameter("admin");
		String sysmc_ = req.getParameter("sysmc_");
		String sysdz_ = req.getParameter("sysdz_");
		String type = req.getParameter("type");
		String specialty = req.getParameter("specialty");
		String course = req.getParameter("course");
		String equipment = req.getParameter("equipment");
		Connection conn = DatabaseUtil.getConn();
		String pic1 = req.getParameter("old_pic1");
		String pic2 = req.getParameter("old_pic2");
		String pic3 = req.getParameter("old_pic3");
//System.out.println(pic1 + "-" +pic2 + "-" + pic3);
		
		long file_size_max = 5242880;
		String ext = "";
		String url = "upload/laboratory_pic/";      //应保证在根目录中有此目录的存在  上传图片路径
		//初始化mySmartUpload
		com.jspsmart.upload.File file1 = mySmartUpload.getFiles().getFile(0);
		com.jspsmart.upload.File file2 = mySmartUpload.getFiles().getFile(1);
		com.jspsmart.upload.File file3 = mySmartUpload.getFiles().getFile(2);
		
		//选中删除图片,则删除图片;未选中则判断有无上传图片,有上传图片则处理上传图片
		if(req.getParameter("del_pic1") != null){
			String saveurl= request.getSession().getServletContext().getRealPath("/") + pic1;
			java.io.File fileDel = new java.io.File(saveurl);
			if(fileDel.exists()){
				fileDel.delete();
		    }
			pic1 = "";
		}else{
			if(!file1.isMissing()){ 
				try{
					ext= file1.getFileExt();      //取得后缀名
					int file_size = file1.getSize();     //取得文件的大小  
					String saveurl= "";
					if(file_size <= file_size_max){
					    //更改文件名，取得当前上传时间的毫秒数值
					    Calendar calendar = Calendar.getInstance();
					    String filename = String.valueOf(calendar.getTimeInMillis()); 
					    saveurl = request.getSession().getServletContext().getRealPath("/") + url;
					    saveurl += filename + "." + ext;          //文件绝对路径
					    file1.saveAs(saveurl,mySmartUpload.SAVE_PHYSICAL);
					    pic1 = url + filename + "." + ext;
					}else{    //判断是否文件过大结束
						request.setAttribute("msg", "上传图片大小不能超过" + file_size_max/1048576 + "M!");
						request.getRequestDispatcher("error.jsp").forward(request, response); 
					  	return;
					} 
				}catch (Exception e){
					e.printStackTrace();
					request.setAttribute("msg", "上传图片失败!");
					request.getRequestDispatcher("error.jsp").forward(request, response);
				  	return;
				}
			}
		}
		
		if(req.getParameter("del_pic2") != null){
			String saveurl= request.getSession().getServletContext().getRealPath("/") + pic2;
			java.io.File fileDel = new java.io.File(saveurl);
			if(fileDel.exists()){
				fileDel.delete();
		    }
			pic2 = "";
		}else{
			if(!file2.isMissing()){ 
				try{
					ext= file2.getFileExt();      //取得后缀名
					int file_size = file2.getSize();     //取得文件的大小  
					String saveurl= "";
					if(file_size <= file_size_max){
					    //更改文件名，取得当前上传时间的毫秒数值
					    Calendar calendar = Calendar.getInstance();
					    String filename = String.valueOf(calendar.getTimeInMillis()); 
					    saveurl = request.getSession().getServletContext().getRealPath("/") + url;
					    saveurl += filename + "." + ext;          //文件绝对路径
					    file2.saveAs(saveurl,mySmartUpload.SAVE_PHYSICAL);
					    pic2 = url + filename + "." + ext;
					}else{    //判断是否文件过大结束
						request.setAttribute("msg", "上传图片大小不能超过" + file_size_max/1048576 + "M!");
						request.getRequestDispatcher("error.jsp").forward(request, response); 
					  	return;
					} 
				}catch (Exception e){
					e.printStackTrace();
					request.setAttribute("msg", "上传图片失败!");
					request.getRequestDispatcher("error.jsp").forward(request, response);
				  	return;
				}
			}
		}
		
		if(req.getParameter("del_pic3") != null){
			String saveurl= request.getSession().getServletContext().getRealPath("/") + pic3;
			java.io.File fileDel = new java.io.File(saveurl);
			if(fileDel.exists()){
				fileDel.delete();
		    }
			pic3 = "";
		}else{
			if(!file3.isMissing()){ 
				try{
					ext= file3.getFileExt();      //取得后缀名
					int file_size = file3.getSize();     //取得文件的大小  
					String saveurl= "";
					if(file_size <= file_size_max){
					    //更改文件名，取得当前上传时间的毫秒数值
					    Calendar calendar = Calendar.getInstance();
					    String filename = String.valueOf(calendar.getTimeInMillis()); 
					    saveurl = request.getSession().getServletContext().getRealPath("/") + url;
					    saveurl += filename + "." + ext;          //文件绝对路径
					    file3.saveAs(saveurl,mySmartUpload.SAVE_PHYSICAL);
					    pic3 = url + filename + "." + ext;
					}else{    //判断是否文件过大结束
						request.setAttribute("msg", "上传图片大小不能超过" + file_size_max/1048576 + "M!");
						request.getRequestDispatcher("error.jsp").forward(request, response); 
					  	return;
					} 
				}catch (Exception e){
					e.printStackTrace();
					request.setAttribute("msg", "上传图片失败!");
					request.getRequestDispatcher("error.jsp").forward(request, response);
				  	return;
				}
			}
		}
		
		String sql = "update _laboratory set equipment = ?, admin = ?, type = ?, specialty = ?, course = ?," +
				" pic1 = ?, pic2 = ?, pic3 = ?, name = ?, address = ? where id = ?";
		PreparedStatement pstmt = null;
		try {
			pstmt = DatabaseUtil.prepareStmt(conn, sql);	
			int i = 1;
			pstmt.setString(i++, equipment);
			pstmt.setString(i++, admin);
			pstmt.setString(i++, type);
			pstmt.setString(i++, specialty);
			pstmt.setString(i++, course);
			pstmt.setString(i++, pic1);
			pstmt.setString(i++, pic2);
			pstmt.setString(i++, pic3);
			pstmt.setString(i++, sysmc_);
			pstmt.setString(i++, sysdz_);
			pstmt.setInt(i++, id);
			pstmt.executeUpdate();
			request.setAttribute("msg", "修改成功");
			LogDAO.add((String)request.getSession().getAttribute("admin_name"), "修改实验室信息[ID " + id + "]");
			request.getRequestDispatcher("editLaboratory.jsp?id="+id).forward(request, response);
		}catch (SQLException e) {
			e.printStackTrace();
			request.setAttribute("msg", "修改实验室信息失败!");
			request.getRequestDispatcher("error.jsp").forward(request, response);
			return;
		}finally{
			DatabaseUtil.close(pstmt);
			DatabaseUtil.close(conn);
		}	
	}
	
	public void search(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException{
		Connection conn = DatabaseUtil.getConn();
		PreparedStatement pstmt = null;
		String name = request.getParameter("name");
		int departmentID = Integer.parseInt(request.getParameter("departmentID"));
		String sql = "select * from _laboratory where name like ?";
		if(departmentID != 0){
			sql += " and departmentID = " + departmentID;
		}
		if(name.equals("") && departmentID == 0){
			request.getRequestDispatcher("LaboratoryServlet?method=list&departmentID=1").forward(request, response);
			return;
		}
		ResultSet rs = null;
		try {
			pstmt = DatabaseUtil.prepareStmt(conn, sql);
			pstmt.setString(1, "%" + name + "%");
			rs = pstmt.executeQuery();
			ArrayList<Laboratory> laboratoryList = new ArrayList<Laboratory>();
			while(rs.next()) {
				Laboratory l = new Laboratory();
				LaboratoryDAO.initFromRS(rs, l);
				laboratoryList.add(l);
			}	
			request.setAttribute("laboratoryList", laboratoryList);
			request.setAttribute("search", "search");
			request.setAttribute("msg", "搜索结果如下");
			request.getRequestDispatcher("listLaboratory.jsp").forward(request, response);
			return;
		}catch (SQLException e) {
			e.printStackTrace();
		}finally{		
			DatabaseUtil.close(rs);
			DatabaseUtil.close(pstmt);
			DatabaseUtil.close(conn);	
		}
	}
	
	public void webList(HttpServletRequest request, HttpServletResponse response)
	throws ServletException, IOException{
		int pageNo = 1;
		String strPageNo = request.getParameter("pageNo");
		if(strPageNo != null && !strPageNo.trim().equals("")) {
			try {
				pageNo = Integer.parseInt(strPageNo);
			} catch (NumberFormatException e) {
				pageNo = 1;
			} 
		}
		if(pageNo <= 0) pageNo = 1;
		ArrayList<Laboratory> laboratoryList = new ArrayList<Laboratory>();
		Connection conn = DatabaseUtil.getConn();
		ResultSet rsCount = null;
		PreparedStatement pstmt = null;
		Statement stmtCount = null;
		Statement stmt = DatabaseUtil.createStmt(conn);
		ResultSet rs = null;	
		int totalPages = 0;
		int pageSize = 15;  //设置每页显示多少条
		try {
			stmtCount = DatabaseUtil.createStmt(conn);
			String countSQL = "select count(*) from _laboratory";
			String sql = "select * from _laboratory";
			rsCount = DatabaseUtil.executeQuery(stmtCount, countSQL);
			int totalRecords;
			rsCount.next();
			totalRecords = rsCount.getInt(1);
			totalPages = (totalRecords + pageSize - 1)/pageSize;
			if(totalPages == 0){
				totalPages = 1;
			}
			if(pageNo > totalPages) pageNo = totalPages;
			int startPos = (pageNo-1) * pageSize; 
			sql +=" order by departmentID desc limit ?, ?";
			pstmt = DatabaseUtil.prepareStmt(conn, sql);
			pstmt.setInt(1, startPos);
			pstmt.setInt(2, pageSize);
			rs = pstmt.executeQuery();
			while(rs.next()) {
				Laboratory l = new Laboratory();
				LaboratoryDAO.initFromRS(rs, l);
				laboratoryList.add(l);
			}	
		}catch (SQLException e) {
			e.printStackTrace();
		}finally{		
			DatabaseUtil.close(stmtCount);
			DatabaseUtil.close(pstmt);
			DatabaseUtil.close(rsCount);
			DatabaseUtil.close(rs);
			DatabaseUtil.close(stmt);
			DatabaseUtil.close(conn);	
			request.setAttribute("pageNo", pageNo);
			request.setAttribute("totalPages", totalPages);
			request.setAttribute("laboratoryList", laboratoryList);
			request.getRequestDispatcher("listLaboratory.jsp").forward(request, response);
		}
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
			String istring = "<select id='laboratoryID' name='laboratoryID' >";
			while(rs.next()) {
				istring += "<option value='" + rs.getInt("id") + "'>" + rs.getString("name") + "</option>";
			}	
			if(istring.equals("<select id='laboratoryID' name='laboratoryID'>")){
				istring += "<option value='0'>该系别暂无实验室信息</option>";
			}
			istring += "</select>";
			out.println(istring);
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
	
	public void getAjax_zy(HttpServletRequest request, HttpServletResponse response)
	throws ServletException, IOException{
		System.out.println("enter");
		String sign = request.getParameter("sign");
		Map<String, String> map = SysxxDAO.list_zy(sign);
		Iterator it = map.keySet().iterator();
		System.out.println("333");
		response.setContentType("text/html;charset=utf-8");
		response.setCharacterEncoding("utf-8");
		PrintWriter out = response.getWriter();
		String istring = "<select name='ssxk_zy'>";
		istring += "<option value='0'>请选择专业类</option>";
		while(it.hasNext()) {
			String key = (String) it.next();
			String value = map.get(key);
			istring += "<option value='" + key + "'>" + value + "</option>";
		}	
		istring += "</select>";
		out.println(istring);
		out.flush();
		out.close(); 
		System.out.println("66666");
	}	
	
	public void getAjax_sykc(HttpServletRequest request, HttpServletResponse response)
	throws ServletException, IOException{
		String sign = request.getParameter("sign");
		Map<String, String> map = SysxxDAO.list_zy(sign);
		Iterator it = map.keySet().iterator();
		
		response.setContentType("text/html;charset=utf-8");
		response.setCharacterEncoding("utf-8");
		PrintWriter out = response.getWriter();
		String istring = "<select name='ssxk_mxzy' id='ssxk_mxzy' onchange=add_mxzy(this.value)>";
		while(it.hasNext()) {
			String key = (String) it.next();
			String value = map.get(key);
			istring += "<option value='" + key + "' id='mxzy_id'>" + value + "</option>";
		}	
		istring += "</select>";
		out.println(istring);
		out.flush();
		out.close(); 
	}	
	
	public void getAjax_mxzy(HttpServletRequest request, HttpServletResponse response)
	throws ServletException, IOException{
		String sign = request.getParameter("sign");
		Map<String, String> map = SysxxDAO.list_zy(sign);
		Iterator it = map.keySet().iterator();
		
		response.setContentType("text/html;charset=utf-8");
		response.setCharacterEncoding("utf-8");
		PrintWriter out = response.getWriter();
		String istring = "<select name='ssxk_mxzy' id='ssxk_mxzy'>";
		while(it.hasNext()) {
			String key = (String) it.next();
			String value = map.get(key);
			istring += "<option value='" + key + "' id='mxzy_id'>" + value + "</option>";
		}	
		istring += "</select>";
		out.println(istring);
		out.flush();
		out.close(); 
	}	
	
	public void getAjax2(HttpServletRequest request, HttpServletResponse response)
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
			String istring = "<select id='laboratoryID' name='laboratoryID' class='select'>";
			while(rs.next()) {
				istring += "<option value='" + rs.getInt("id") + "'>" + rs.getString("name") + "</option>";
			}	
			if(istring.equals("<select id='laboratoryID' name='laboratoryID' class='select'>")){
				istring += "<option value='0'>该教学单位暂无实验室信息</option>";
			}
			istring += "</select>";
			out.println(istring);
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
	
	public void getIndexAjax(HttpServletRequest request, HttpServletResponse response)
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
			
			///////////////////////////////////////////////////////////////////////////////////////
			List<Laboratory> laboratoryList = new ArrayList<Laboratory>();
			while(rs.next()) {
				Laboratory l = new Laboratory();
				LaboratoryDAO.initFromRS(rs, l);
				laboratoryList.add(l);
			}	
			request.setAttribute("laboratoryList", laboratoryList);
			request.getRequestDispatcher("listLaboratory.jsp").forward(request, response);
			//////////////////////////////////////////////////////////////////////////////////////
			
			
//			response.setContentType("text/html;charset=utf-8");
//			response.setCharacterEncoding("utf-8");
//			PrintWriter out = response.getWriter();
//			String istring = "<ul>";
//			String de_name = DepartmentDAO.getName(departmentID);
//			while(rs.next()) {
//				istring += "<tr><td class=\"new_title\"><a href=\"viewLaboratory.jsp?id="+ rs.getInt("id") + "\">" + rs.getString("name")+ "</a></td><td>" + de_name + "</td></tr>";
//				/*"<li><p><a href=\"viewLaboratory.jsp?id=" + rs.getInt("id") + "\">" + rs.getString("name") + "</a></p><span>[" + de_name + "]</span></li>"*/;
//			}	
//			istring += "</ul>";
//			if(istring.equals("<ul></ul>")){
//				istring = "<div class=\"no_result_msg\">该系别暂无实验室信息</div>";
//			}
//			out.println(istring);
//			out.flush();
//			out.close(); 
		}catch (SQLException e) {
			e.printStackTrace();
		}finally{		
			DatabaseUtil.close(rs);
			DatabaseUtil.close(pstmt);
			DatabaseUtil.close(conn);	
		}
	}	
}
