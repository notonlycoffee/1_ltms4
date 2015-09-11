<%@ page language="java" import="java.util.*, com.ltms.model.*, java.sql.*,com.ltms.dao.*" pageEncoding="utf-8"%>
<%
	int id = Integer.parseInt(request.getParameter("id"));
	Laboratory laboratory = LaboratoryDAO.load(id);
	String currenTerm = (String) request.getSession().getAttribute("_year_");
	Calendar cal = Calendar.getInstance();
	int year = cal.get(Calendar.YEAR);
	int month = cal.get(Calendar.MONTH);
	if(currenTerm == null){
		if(month<8){
			currenTerm = (year - 1) + "-" + year;
		}else{
			currenTerm = year + "-" + (year + 1);
		}
	}
	Sysqk sysqk = SysxxDAO.getSysqkByTeam(id,currenTerm);
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link rel="stylesheet" type="text/css" href="css/global.css" />
</head>
<body>
<script type="text/javascript">
String.prototype.trim = function(){ return this.replace(/(^\s*)|(\s*$)/g, "");}
function ShowErrMsg(Info)
{
	document.getElementById("showMsg").innerHTML = Info;
}
function submitfrm(frm)
{
	return true;
}

</script>
<div class="detailInfoContent">
<form action="LaboratoryServlet" method="post"  enctype="multipart/form-data" onsubmit="return submitfrm(this);" >
<table border=1 cellspacing=0 cellpadding=0 style="border:none;border-left:1px solid #CCCCCC;border-bottom:1px solid #CCCCCC;">
  	<tr>
  		<td class="TD_Left">实验室名称</td>  
  		<td class="TD_Right">
			<%=laboratory.getName() %>
		</td>
  	</tr>
  	<tr>
  		<td class="TD_Left">教室获奖与成果</td>
  		<td class="TD_Right">
			国家奖<%=sysqk.getJshjycg_gjj() %>
			省部奖<%=sysqk.getJshjycg_sbj() %>
			专利<%=sysqk.getJshjycg_zl() %>
			学生获奖<%=sysqk.getJshjycg_xshj() %>
		</td>
  	</tr>
  	<tr>
  		<td class="TD_Left" style="width:150px;">三大检索收录</td>
  		<td class="TD_Right">
			教学论文<%=sysqk.getSdjssl_jxlw() %>
			科研论文<%=sysqk.getSdjssl_kylw() %>
		</td>
  	</tr>
  	<tr>
  		<td class="TD_Left">核心刊物</td>
  		<td class="TD_Right">
			教学论文<%=sysqk.getHxkw_jxlw() %>
			科研论文<%=sysqk.getHxkw_kylw() %>
		</td>
  		
	</tr>
	<tr>
  		<td class="TD_Left">实验耗材</td>
  		<td class="TD_Right">
			<%=sysqk.getSyhc() %>
		</td>
	</tr>
	<tr>
  		<td class="TD_Left">科研项目数</td>
  		<td class="TD_Right">
			省部级以上<%=sysqk.getKyxms_sbjys() %>
			其他<%=sysqk.getKyxms_qt() %>
		</td>
  		
	</tr>
	<tr>
  		<td class="TD_Left">社会服务项目数</td>
  		<td class="TD_Right">
			<%=sysqk.getShfwxms() %>
		</td>
	</tr>
	<tr>
  		<td class="TD_Left">教研项目数</td>
  		<td class="TD_Right">
			省部级以上<%=sysqk.getJyxms_sbjys() %>
			其他<%=sysqk.getJyxms_qt() %>
		</td>
	</tr>
	<tr>
  		<td class="TD_Left">毕业设计及论文人数</td>
  		<td class="TD_Right">
			专科生<%=sysqk.getBysjjlwrs_zks() %>
			本科生<%=sysqk.getBysjjlwrs_bks() %>
			研究生<%=sysqk.getBysjjlwrs_yjs() %>
		</td>
	</tr>
	<tr>
  		<td class="TD_Left">开放实验情况</td>
  		<td class="TD_Right">
			校内实验个数   <%=sysqk.getKfsyqk_xnsygs() %>
			校外实验个数   <%=sysqk.getKfsyqk_xwsygs() %><br/>
			校内实验人数   <%=sysqk.getKfsyqk_xnsyrs() %>
			校外实验人数   <%=sysqk.getKfsyqk_xwsyrs() %><br/>
			校内人时数    	<%=sysqk.getKfsyqk_xnrss() %>
			校外人时数    	<%=sysqk.getKfsyqk_xwrss() %>
		</td>	
	</tr>
	<tr>
  		<td class="TD_Left">兼职人员数</td>
  		<td class="TD_Right">
			<%=sysqk.getJzrys() %>
		</td>
	</tr>
  	<tr>
  		<td class="TD_Left">操作</td>
  		<td><input type="button" value="返回" onclick="javascript:history.go(-1)" class="form_btn" /></td>
  	</tr>
</table>  
</form>
</div>
</body>
</html>