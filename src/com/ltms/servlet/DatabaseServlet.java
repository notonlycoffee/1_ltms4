package com.ltms.servlet;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Calendar;
import java.util.Date;

import com.jspsmart.upload.Request;
import com.jspsmart.upload.SmartUpload;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class DatabaseServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		this.doPost(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String method = request.getParameter("method");
		if(method != null && !"".equals(method)){
			if(method.equals("backup")) backup(request, response);
		}else{
			SmartUpload mySmartUpload = new SmartUpload();
			mySmartUpload.initialize(getServletConfig(),request,response);
			try {
				mySmartUpload.setAllowedFilesList("sql, SQL");
			//上载文件 
				mySmartUpload.upload();  //这句话执行后才能取得参数
			} catch (Exception e){
				request.setAttribute("msg", "只允许上传sql类型的数据库备份文件!");
				request.getRequestDispatcher("error.jsp").forward(request, response);
			  	return;
			}
			Request req = mySmartUpload.getRequest();
			method = req.getParameter("method");
			if(method.equals("load")) load(request, response, req, mySmartUpload); 
			else return;
		}
	}

	public void backup(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException{
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		String filename = "backup" + c.get(Calendar.YEAR) + String.valueOf(c.get(Calendar.MONTH) + 1) + c.get(Calendar.DAY_OF_MONTH) + 
		c.get(Calendar.HOUR_OF_DAY) + c.get(Calendar.MINUTE) + c.get(Calendar.SECOND) + ".sql";
//System.out.println(command);
		try {
			try { 
				Runtime rt = Runtime.getRuntime();   
//  数据库名czscfdb要改!
				Process child = rt.exec("mysqldump -hlocalhost -uroot -proot ltms");
				InputStream in = child.getInputStream();// 控制台的输出信息作为输入流
				InputStreamReader xx = new InputStreamReader(in, "utf8");// 设置输出流编码为utf8。这里必须是utf8，否则从流中读入的是乱码
				String inStr; 
				StringBuffer sb = new StringBuffer(""); 
				String outStr; 
				// 组合控制台输出信息字符串 
				BufferedReader br = new BufferedReader(xx); 
				while ((inStr = br.readLine()) != null) { 
				sb.append(inStr + "\r\n"); 
				} 
				outStr = sb.toString();//备份出来的内容是一个字条串
				// 要用来做导入用的sql目标文件： 
				ByteArrayOutputStream ba = new ByteArrayOutputStream();
				OutputStreamWriter writer = new OutputStreamWriter(ba, "utf8"); 
				writer.write(outStr);
				response.setHeader("Expires", "0");
				response.setHeader("Cache-Control", "must-revalidate, post-check=0, pre-check=0");
				response.setHeader("Pragma", "public");
				/* 如果想出来让IE提示你是打开还是保存的对话框，加上下面这句就可以了 */
				response.setHeader("Content-disposition", "attachment; filename=" + filename);
				response.setContentLength(ba.size());
				try {
					ServletOutputStream out = response.getOutputStream();
					ba.writeTo(out);
					out.flush();
					out.close();
					ba.close();
				} catch (IOException ex) {
					ex.printStackTrace();
				} finally {
					if (ba != null) ba.close();
				}
			} catch (Exception e) { 
				e.printStackTrace(); 
			}
		}catch (Exception e) { 
			e.printStackTrace(); 
		}
	}
	
	public void load(HttpServletRequest request, HttpServletResponse response, Request req, SmartUpload mySmartUpload)
	throws ServletException, IOException{
		String file_pathname = "";
		String ext = "";
		String url = "upload/database_backup/";      //应保证在根目录中有此目录的存在  上传图片路径
		boolean flag = false;
		com.jspsmart.upload.File myFile = mySmartUpload.getFiles().getFile(0);
	    if (myFile.isMissing()){
		   	request.setAttribute("msg", "请先选择要恢复的文件!");
			request.getRequestDispatcher("databaseBackup.jsp.jsp").forward(request, response);
		  	return;
	    }else{   // else开始
	    	ext = myFile.getFileExt();      //取得后缀名
			Calendar calendar = Calendar.getInstance();
			String filename = String.valueOf(calendar.getTimeInMillis()); 
			file_pathname = request.getSession().getServletContext().getRealPath("/") + url;
			file_pathname += filename+"."+ext;          //保存路径
			try { 
				myFile.saveAs(file_pathname, mySmartUpload.SAVE_PHYSICAL);
				java.io.File file = new java.io.File(file_pathname);        //读入刚才上传的文件
				Runtime rt = Runtime.getRuntime(); 
//  数据库名czscfdb要改!
				Process child = rt.exec("mysql -uroot -proot ltms"); 
				OutputStream out = child.getOutputStream();//控制台的输入信息作为输出流 
				String inStr; 
				StringBuffer sb = new StringBuffer(""); 
				String outStr; 
				BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), "utf8")); 
				while ((inStr = br.readLine()) != null) { 
				sb.append(inStr + "\r\n"); 
				} 
				outStr = sb.toString(); 
				OutputStreamWriter writer = new OutputStreamWriter(out, "utf8"); 
				writer.write(outStr); 
				// 注：这里如果用缓冲方式写入文件的话，会导致中文乱码，用flush()方法则可以避免 
				writer.flush(); 
				out.close(); 
				br.close(); 
				writer.close(); 
				flag = true;
			} catch (Exception e) { 
				e.printStackTrace(); 
			}
	    }
		if(flag){
			request.setAttribute("msg", "恢复成功!");
			request.getRequestDispatcher("databaseBackup.jsp").forward(request, response);
		}else{
			request.setAttribute("msg", "恢复失败!");
			request.getRequestDispatcher("databaseBackup.jsp").forward(request, response);
		}
		
	}
}
