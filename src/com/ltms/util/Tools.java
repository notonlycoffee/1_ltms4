package com.ltms.util;

import com.ltms.model.Admin;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.net.URLDecoder;
import java.sql.Timestamp;
import java.text.*;
import java.util.*;
import java.net.InetAddress;
import java.net.UnknownHostException; 

import javax.servlet.http.HttpServletRequest;

public class Tools {
	
	//创建日期格式
	private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	
	//创建中国的货币格式
	private static NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(Locale.CHINA);
	
	//FCKeditor内置的分页字符串
	public static String fckSeparator = "<div style=\"PAGE-BREAK-AFTER:always\"><span style=\"DISPLAY:none\">&nbsp;</span></div>";
	
	//取得指定图片的宽度和高度
	public static Map getPicWidthAndHeight(String filename){
		Map map = new HashMap();
		try{
			BufferedImage sourceImg = javax.imageio.ImageIO.read(new FileInputStream(filename));
			map.put("width",sourceImg.getWidth());
			map.put("height",sourceImg.getHeight());
			return map;
		}catch(Exception ex){
			ex.printStackTrace();
			return null;
		}
	}
	
	//synchronized是java关键字，保证在同一时刻最多只有一个线程执行被修饰的函数
	//取得系统时间来产生随机主文件名
	public synchronized static String getRndFilename(){
		return String.valueOf(System.currentTimeMillis());
	}
	
	//取得指定文件的文件扩展名
	public synchronized static String getFileExtName(String filename){
		int ext = filename.indexOf(".");
		return filename.substring(ext);
	}
	
	//验证上传文件的类型是否合法
	public synchronized static boolean isEnableUploadType(int fileType,String filename){
		String enableExtNames = null;
		int ext = filename.indexOf(".");					//获取"."出现的位置
		String fileExtName = filename.substring(ext).toLowerCase();//获取从"."开始后面的字符串
		if(fileType==1){									//图片文件类型
			enableExtNames = ".jpg,.gif,.png,.bmp";
		}else if(fileType==2){								//压缩文件类型
			enableExtNames = ".zip,.rar,.tar,.7z";
		}else if(fileType==3){								//多媒体文件类型
			enableExtNames = ".mp3,.wma,.swf,.flv";			
		}else if(fileType==4){								//office文件类型
			enableExtNames = ".doc,.docx,.xls,.xlsx,.ppt,.pptx,.mdb,.mdbx";		
		}else if(fileType==5){								//office文件类型
			enableExtNames = ".mp3,.wma,.swf,.flv,.doc,.docx,.xls,.xlsx,.ppt,.pptx,.mdb,.mdbx,.zip,.rar,.tar,.7z,.jpg,.gif,.png,.bmp, .txt";		
		}
		if(enableExtNames!=null){
			if(enableExtNames.indexOf(fileExtName)!=-1) return true;
			else return false;
		}else{
			return true;
		}
	}
	
	/** HTML代码的Escape处理方法 */
	public static String escape(String src){
		int i;
		char j;
		StringBuffer tmp = new StringBuffer();
		tmp.ensureCapacity(src.length()*6);
		for (i=0;i<src.length();i++){
			j = src.charAt(i);
			if (Character.isDigit(j) || Character.isLowerCase(j) || Character.isUpperCase(j)) 
				tmp.append(j);
			else if(j<256){
				tmp.append( "%" );
				if (j<16)tmp.append("0");
				tmp.append( Integer.toString(j,16));
			}else{
				tmp.append("%u");
				tmp.append(Integer.toString(j,16));
			}
		}
		return tmp.toString();
	}
	
	//HTML代码的UnEscape处理方法
	public static String unescape(String src){
		StringBuffer tmp = new StringBuffer();
		tmp.ensureCapacity(src.length());
		int lastPos = 0, pos = 0;
		char ch;
		while(lastPos<src.length()){
			pos = src.indexOf("%",lastPos);
			if(pos == lastPos){
				if(src.charAt(pos+1)=='u'){
					ch = (char)Integer.parseInt(src.substring(pos+2,pos+6),16);
					tmp.append(ch);
					lastPos = pos+6;
				}else{
					ch = (char)Integer.parseInt(src.substring(pos+1,pos+3),16);
					tmp.append(ch);
					lastPos = pos+3;
				}
			}else{
				if(pos == -1){
					tmp.append(src.substring(lastPos));
					lastPos = src.length();
				}else{
					tmp.append(src.substring(lastPos,pos));
					lastPos = pos;
				}
			}
		}
		return tmp.toString();
	}
	
	//为以逗号分隔的字符串的每个单元加入引号，如：aa,bb -->'aa','bb'
	public static String formatString(String src){
		StringBuffer result = new StringBuffer();
		result.append("");
		if(src!=null){
			String[] tmp = src.split(",");
			result.append("'"+tmp[0]+"'");
			for(int i=1;i<tmp.length;i++){
				result.append("'"+tmp[i]+"'");
			}
		}
		return result.toString();
	}
	
	//截取指定字节数的字符串，且确保汉字不被拆分
	public static String cutString(String text,int textMaxChar){
		int size,index;
		String result = null;
		if(textMaxChar<=0){
			result = text;
		}else{
			for(size=0,index=0;index<text.length()&&size<textMaxChar;index++){
				size += text.substring(index,index+1).getBytes().length;
			}
			result = text.substring(0,index);
		}
		return result;
	}
	
	//按yyyy-MM--dd格式格式化日期
	public static String formatDate(Date date){
		if(date==null){
			return "";
		}else{
			return dateFormat.format(date);
		}
	}
	
	//按yyyy-MM--dd hh:mm:ss格式化时间
	public static String formatDate(Timestamp time){
		SimpleDateFormat dateformat= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		if(time != null){
			return dateformat.format(time);
		}else{
			return "";
		}
	}
	
	//按yyyy-MM--dd格式化时间
	public static String formatDateList(Timestamp time){
		SimpleDateFormat dateformat= new SimpleDateFormat("yyyy-MM-dd");
		if(time != null){
			return dateformat.format(time);
		}else{
			return "";
		}
	}
	
	//对没有escape的HTML内容进行FCK分页处理，返回String[]
	public static String[] splitContent(String unEscapedHtml){
		if(unEscapedHtml==null){
			return null;
		}else{
			return unescape(unEscapedHtml).split(fckSeparator);
		}
	}
	
	//取得格式化后的中国货币字符串
	public static String formatCurrency(double money){
		return currencyFormat.format(money);
	}
	
	//url中文传参转换编码
	public static String setCoding(String coding, String codingType, int type){
		String getCoding = null;
		if(type==1){
			try{
				getCoding = URLEncoder.encode(coding,codingType);
			}catch(Exception e){
				e.printStackTrace();
			}		
		}
		if(type==2){
			try{
				getCoding = URLDecoder.decode(coding,codingType);
			}catch(Exception e){
				e.printStackTrace();
			}	
		}
		return getCoding;
	}
	
	//获取用户客户端IP
	public static String getIpAddress() throws UnknownHostException {   
        InetAddress address = InetAddress.getLocalHost();   
        return address.getHostAddress();   		
	}
	
	//获取用户客户端当前时间
	public static Timestamp getNowDate(){
		Date date = new Date();															//获得系统时间
		String nowTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);		//将时间格式转换成符合Timestamp要求的格式.
	    return Timestamp.valueOf(nowTime);												//把时间转换
	}
	
	//过滤掉html代码中的css和html标签获得纯文字
	public static String delHTMLTag(String htmlStr){   
        String regEx_style="<style[^>]*?>[\\s\\S]*?<\\/style>"; 						//定义style的正则表达式   
        String regEx_html="<[^>]+>"; 													//定义HTML标签的正则表达式   
           
        Pattern p_style=Pattern.compile(regEx_style,Pattern.CASE_INSENSITIVE);   
        Matcher m_style=p_style.matcher(htmlStr);   
        htmlStr = m_style.replaceAll(""); 												//过滤style标签   
           
        Pattern p_html=Pattern.compile(regEx_html,Pattern.CASE_INSENSITIVE);   
        Matcher m_html=p_html.matcher(htmlStr);   
        htmlStr=m_html.replaceAll(""); 													//过滤html标签   
          
        htmlStr=htmlStr.replace(" ","");  
        htmlStr=htmlStr.replaceAll("\\s*|\t|\r|\n","");  
        htmlStr=htmlStr.replace("“","");  
        htmlStr=htmlStr.replace("”","");  
        htmlStr=htmlStr.replaceAll("　","");  
            
        return htmlStr.trim(); 															//返回文本字符串   
    }   
	
	public static String getUrl(String urls,String path){
		String getUrl = "";
		try{
			URL url = new URL(urls);
			String urlName = url.getFile();
			int len1 = urlName.indexOf(path);
			int len2 = urlName.indexOf(".");
			getUrl = urlName.substring(len1+path.trim().length()+1,len2);	
		}catch(MalformedURLException e){ 
			e.printStackTrace();
		}
		return getUrl;
    }
	
	//列表分页
	public static StringBuffer page(int total,int pageTotal,int size,int pageNo,int prePageNo,int nextPageNo,String url){
		StringBuffer sb = new StringBuffer();
		if(total>0){																	//计算总页数
			pageTotal = total / size;
			if(total%size>0) pageTotal++;
		}
		if(pageNo>1){																	//计算上一页
			prePageNo = pageNo-1;
		}else{
			prePageNo = 1;
		}
		if(pageNo<pageTotal){															//计算下一页
			nextPageNo = pageNo+1;
		}else{
			nextPageNo = pageTotal;
		}
		if(url.indexOf("?")!=-1){
			url = url+"&";
		}else{
			url = url+"?";
		}
		sb.append("<div>当前第["+pageNo+"/"+pageTotal+"]页 [<a target='_self' href='"+url+"pageNo=1'>首页</a>]");
		if(pageNo==1){																	//第一页时，"上一页"无链接
			sb.append("[上一页] ");
		}else{
			sb.append("[<a target='_self' href='"+url+"pageNo="+prePageNo+"'>上一页</a>] ");
		}
		if(pageNo==pageTotal){															//最后一页时，"下一页"无链接
			sb.append("[下一页] ");
		}else{
			sb.append("[<a target='_self' href='"+url+"pageNo="+nextPageNo+"'>下一页</a>] ");
		}
		sb.append("[<a target='_self' href='"+url+"pageNo="+pageTotal+"'>尾页</a>]</div>");
		return sb;
	}
	
	//主函数
	public static void main(String[] args){
		System.out.println(escape(""));
	}
}
