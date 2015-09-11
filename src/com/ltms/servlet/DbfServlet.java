package com.ltms.servlet;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.filechooser.FileSystemView;

import com.ltms.dao.DepartmentDAO;
import com.ltms.dao.ExItemDAO;
import com.ltms.dao.ExperimentDAO;
import com.ltms.dao.LaboratoryDAO;
import com.ltms.dao.RegulationDAO;
import com.ltms.dao.SysxxDAO;
import com.ltms.dbf.DBFJdbcOdbcTest;
import com.ltms.model.Department;

public class DbfServlet extends HttpServlet {
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		this.doPost(request, response);

	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		String method = request.getParameter("method");
		System.out.println("method is " +  method);
		if (method.equals("output_sysk"))
			output_sysk(request, response);
		else if(method.equals("mesCount"))
			mesCount(request,response);
		else if(method.equals("output_sxt"))
			output_sxt(request,response);
		else
			return;

	}

	private void output_sxt(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//在桌面建立一个文件夹   dbf
//		System.out.println("enter");
//		FileSystemView fsv = FileSystemView.getFileSystemView();
//		System.out.println("333334");
//		File homeDirectory = fsv.getHomeDirectory();
//		System.out.println(homeDirectory.getAbsolutePath());
//		File dbfFile = new File(homeDirectory, "dbf");
		
		File dbfFile = new File("C:\\", "dbf");
		
		if(!dbfFile.exists()){
			try {
				dbfFile.mkdir();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				//return
				request.setAttribute("msg", "创建文件失败");
				request.getRequestDispatcher("outPutData.jsp").forward(request, response);
			}
		}
		System.out.println("3333");
		String url_copy = request.getSession().getServletContext().getRealPath(
		"/");
		url_copy += "testdbf\\copy\\";

		System.out.println("copy is " + url_copy);
		
		// 把copy文件夹的文件拷贝到dbf文件夹
		File copyfile = new File(url_copy);
		String[] files = copyfile.list();
		System.out.println("length is " + files.length);
		try {
			for (int j = 0; j < files.length; j++) {
				copyfile = new File(url_copy + files[j]);
				if (copyfile.isFile()) {
					FileInputStream input = new FileInputStream(copyfile);
					FileOutputStream output = new FileOutputStream(dbfFile.getAbsolutePath() + "\\"
							+ (copyfile.getName()).toString());
					
					System.out.println("dbf is " + dbfFile.getAbsolutePath() + "\\" + (copyfile.getName()).toString());
					byte[] b = new byte[1024];
					int len;
					while ((len = input.read(b)) != -1) {
						System.out.println("len is " + len);
						output.write(b, 0, len);
					}
					output.flush();
					output.close();
					input.close();
				}
			}
			System.out.println("exit");
			request.setAttribute("msg", "成功导到C盘dbf文件夹");
			request.getRequestDispatcher("outPutData.jsp").forward(request, response);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}

	private void mesCount(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		String year_weblist = request.getParameter("year_weblist");
		year_weblist = year_weblist.substring(0, 9);
//		System.out.println("year_weblist " + year_weblist);
		String term_weblist = request.getParameter("term_weblist");
		String term_ = year_weblist+term_weblist;
		
		ArrayList<Department> departmentList = (ArrayList<Department>)DepartmentDAO.list();
		Map<Integer,Integer> kaikeMap = new HashMap<Integer, Integer>();
		Map<Integer,Integer> zhiduMap = new HashMap<Integer, Integer>();
		Map<Integer,Integer> shiyanshiMap = new HashMap<Integer, Integer>();
		Map<Integer,Integer> xiangmuMap = new HashMap<Integer, Integer>();
		
		//实验室经费
		Map<Integer, Integer> sysjfMap = new HashMap<Integer, Integer>();
		//实验室情况
		Map<Integer, Integer> sysqkMap = new HashMap<Integer, Integer>();
		//实验室信息
		Map<Integer, Integer> sysxxMap = new HashMap<Integer, Integer>();
		//实验室人员
		Map<Integer, Integer> sysRyMap = new HashMap<Integer, Integer>();
		
		Map<Integer, Integer> xxMap = new HashMap<Integer, Integer>();
		
		for(Department dep : departmentList){
			kaikeMap.put(dep.getId(), ExperimentDAO.loadbyDepa(dep.getId(),term_));
			zhiduMap.put(dep.getId(), RegulationDAO.loadbyDepa(dep.getId()));
			shiyanshiMap.put(dep.getId(), LaboratoryDAO.loadbyDepa(dep.getId()));
			
			sysjfMap.put(dep.getId(), LaboratoryDAO.loadJfByDepa(dep.getId(),year_weblist));
			sysqkMap.put(dep.getId(), LaboratoryDAO.loadQkByDepa(dep.getId(),year_weblist));
			sysxxMap.put(dep.getId(), LaboratoryDAO.loadXxByDepa(dep.getId(),year_weblist));
			
			sysRyMap.put(dep.getId(), LaboratoryDAO.loadRyByDepa(dep.getId()));
			
			xiangmuMap.put(dep.getId(), ExItemDAO.loadXmByDepa(dep.getId(),term_));
			
			xxMap.put(dep.getId(), sysjfMap.get(dep.getId()) + sysqkMap.get(dep.getId()) + sysxxMap.get(dep.getId()));
		}
		
		
		
		//标记
		request.setAttribute("sign", "sign");
		//实验开课记录
		request.setAttribute("kaikeMap", kaikeMap);
		//实验教学项目
		request.setAttribute("xiangmuMap",xiangmuMap);
		//管理制度
		request.setAttribute("zhiduMap",zhiduMap);
		//实验室个数
		request.setAttribute("shiyanshiMap",shiyanshiMap);
		//实验室信息数
		request.setAttribute("xinxiMap",xxMap);
		request.setAttribute("term_", term_);
		request.getRequestDispatcher("/mesCount.jsp").forward(request, response);
	}

	private void output_sysk(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		System.out.println("enter output_sysk");
		String url_test = request.getSession().getServletContext().getRealPath(
				"/");
		url_test += "testdbf\\test\\";
		String url_copy = request.getSession().getServletContext().getRealPath(
				"/");
		url_copy += "testdbf\\copy\\";
		// 删除copy文件夹的内容
		File file = new File(url_copy);
		// 列出所有文件
		String[] files = file.list();
		for (int i = 0; i < files.length; i++) {
			file = new File(url_copy + files[i]);
			while (file.exists()) {
				//删除原有的文件
				file.delete();
			}
		}
		// 把test文件夹的文件拷贝到copy文件夹
		file = new File(url_test);
		files = file.list();

		try {
			for (int j = 0; j < files.length; j++) {
				file = new File(url_test + files[j]);
				if (file.isFile()) {
					FileInputStream input = new FileInputStream(file);
					FileOutputStream output = new FileOutputStream(url_copy
							+ (file.getName()).toString());
					byte[] b = new byte[1024];
					int len;
					while ((len = input.read(b)) != -1) {
						output.write(b, 0, len);
					}
					output.flush();
					output.close();
					input.close();
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// 往copy文件夹里面的文件写数据
		
		
		 //统一给实验室赋值dm
        SysxxDAO.updateDm();
        //统一给实验赋值序号
		Map<Integer, String> map = ExperimentDAO.updateXh();

		String _currenTerm_ = (String)request.getSession().getAttribute("currenTerm");
		
		boolean b = DBFJdbcOdbcTest.add_dbf(url_copy, map,_currenTerm_);
		
		if(b){
			request.setAttribute("msg", "数据已生成");
			request.getRequestDispatcher("outPutData.jsp").forward(request, response);
		}else{
			request.setAttribute("msg", "生成数据失败");
			request.getRequestDispatcher("outPutData.jsp").forward(request, response);
		}
	}

}
