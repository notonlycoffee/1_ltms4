<%@ page language="java" import="java.util.*, com.ltms.model.*, java.sql.*,com.ltms.dao.*" pageEncoding="utf-8"%>
<%
	int departmentID = ((Admin)request.getSession().getAttribute("admin")).getDepartmentID();
	List<Laboratory> laboratoryList = LaboratoryDAO.list(departmentID);;	
	int id = Integer.parseInt(request.getParameter("id"));
	Laboratory laboratory = LaboratoryDAO.load(id);
	String currenTerm = (String) request.getSession().getAttribute("currenTerm");
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
<div id="position">您现在所在页面：实验室管理&nbsp;&gt;&gt;&nbsp;修改实验室基本情况</div>
<div class="detailInfoContent">
<%@ include file="showMsg.jsp" %>
<form action="LaboratoryServlet" method="post" onsubmit="return submitfrm(this);" >
<input type="hidden" name="method" value="updata_sysqk" />
<input type="hidden" name="id" value="<%=id %>" />
<table border=1 cellspacing=0 cellpadding=0 style="border:none;border-left:1px solid #CCCCCC;border-bottom:1px solid #CCCCCC;">
  	<tr>
  		<td class="TD_Left">实验室名称</td> 
  		<td class="TD_Right">
			<%=laboratory.getName() %>
		</td>
  	</tr>
  	<tr>
  		<td class="TD_Left">教师获奖与成果</td>
  		<td class="TD_Right">
			国家奖<input type="text" name="jshjycg_gjj" value="<%=sysqk.getJshjycg_gjj() %>" style="width: 50px;"></input>
			省部奖<input type="text" name="jshjycg_sbj" value="<%=sysqk.getJshjycg_sbj() %>" style="width: 50px;"></input>
			专利<input type="text" name="jshjycg_zl" value="<%=sysqk.getJshjycg_zl() %>" style="width: 50px;"></input>
			学生获奖<input type="text" name="jshjycg_xshj" value="<%=sysqk.getJshjycg_xshj() %>" style="width: 50px;"></input>
		</td>
  	</tr>
  	<tr>
  		<td class="TD_Left" style="width:150px;">三个检索收录</td>
  		<td class="TD_Right">
			教学论文<input type="text" name="sdjssl_jxlw" value="<%=sysqk.getSdjssl_jxlw() %>" style="width: 50px;"></input>
			科研论文<input type="text" name="sdjssl_kylw" value="<%=sysqk.getSdjssl_kylw() %>" style="width: 50px;"></input>
		</td>
  	</tr>
  	<tr>
  		<td class="TD_Left">核心刊物</td>
  		<td class="TD_Right">
			教学论文<input type="text" name="hxkw_jxlw" value="<%=sysqk.getHxkw_jxlw() %>" style="width: 50px;"></input>
			科研论文<input type="text" name="hxkw_kylw" value="<%=sysqk.getHxkw_kylw() %>" style="width: 50px;"></input>
		</td>
  		
	</tr>
	<tr>
  		<td class="TD_Left">实验耗材</td>
  		<td class="TD_Right">
			<input type="text" name="syhc" value="<%=sysqk.getSyhc() %>" style="width: 50px;"></input>
		</td>
	</tr>
	<tr>
  		<td class="TD_Left">科研项目数</td>
  		<td class="TD_Right">
			省部级以上<input type="text" name="kyxms_sbjys" value="<%=sysqk.getKyxms_sbjys() %>" style="width: 50px;"></input>
			其他<input type="text" name="kyxms_qt" value="<%=sysqk.getKyxms_qt() %>" style="width: 50px;"></input>
		</td>
  		
	</tr>
	<tr>
  		<td class="TD_Left">社会服务项目数</td>
  		<td class="TD_Right">
			<input type="text" name="shfwxms" value="<%=sysqk.getShfwxms() %>" style="width: 50px;"></input>
		</td>
	</tr>
	<tr>
  		<td class="TD_Left">教研项目数</td>
  		<td class="TD_Right">
			省部级以上<input type="text" name="jyxms_sbjys" value="<%=sysqk.getJyxms_sbjys() %>" style="width: 50px;"></input>
			其他<input type="text" name="jyxms_qt" value="<%=sysqk.getJyxms_qt() %>" style="width: 50px;"></input>
		</td>
	</tr>
	<tr>
  		<td class="TD_Left">毕业设计及论文人数</td>
  		<td class="TD_Right">
			专科生<input type="text" name="bysjjlwrs_zks" value="<%=sysqk.getBysjjlwrs_zks() %>" style="width: 50px;"></input>
			本科生<input type="text" name="bysjjlwrs_bks" value="<%=sysqk.getBysjjlwrs_bks() %>" style="width: 50px;"></input>
			研究生<input type="text" name="bysjjlwrs_yjs" value="<%=sysqk.getBysjjlwrs_yjs() %>" style="width: 50px;"></input>
		</td>
	</tr>
	<tr>
  		<td class="TD_Left">开放实验情况</td>
  		<td class="TD_Right">
			校内实验个数<input type="text" name="kfsyqk_xnsygs" value="<%=sysqk.getKfsyqk_xnsygs() %>" style="width: 50px;"></input>
			校外实验个数<input type="text" name="kfsyqk_xwsygs" value="<%=sysqk.getKfsyqk_xwsygs() %>" style="width: 50px;"></input><br/>
			校内实验人数<input type="text" name="kfsyqk_xnsyrs" value="<%=sysqk.getKfsyqk_xnsyrs() %>" style="width: 50px;"></input>
			校外实验人数<input type="text" name="kfsyqk_xwsyrs" value="<%=sysqk.getKfsyqk_xwsyrs() %>" style="width: 50px;"></input><br/>
			校内人时数    		 <input type="text" name="kfsyqk_xnrss" value="<%=sysqk.getKfsyqk_xnrss() %>" style="width: 50px;"></input>
			校外人时数    		 <input type="text" name="kfsyqk_xwrss" value="<%=sysqk.getKfsyqk_xwrss() %>" style="width: 50px;"></input>
		</td>	
	</tr>
	<tr>
  		<td class="TD_Left">兼职人员数</td>
  		<td class="TD_Right">
			<input type="text" name="jzrys" value="<%=sysqk.getJzrys() %>" style="width: 50px;"></input>
		</td>
	</tr>
  	<tr>
  		<td class="TD_Left">操作</td>
  		<td class="TD_Right"><input type="submit" value="修改" class="form_btn"/>
  	</tr>
</table>  
</form>
注：所有填空都是以个数为单位且内容可以选填<br/>
  &nbsp;&nbsp;&nbsp;&nbsp;即使没有数据可以录入，也要点击一下修改按钮
  &nbsp;&nbsp;&nbsp;&nbsp;填写的一定要是本学年本实验室的数据
</div>
</body>
</html>