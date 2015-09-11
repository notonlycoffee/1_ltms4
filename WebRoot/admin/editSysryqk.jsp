<%@ page language="java" import="java.util.*, com.ltms.model.*, java.sql.*,com.ltms.dao.*" pageEncoding="utf-8"%>
<%@page import="com.ltms.util.Utils"%>
<%
		int departmentID = ((Admin)request.getSession().getAttribute("admin")).getDepartmentID();
		int id = ((Admin)request.getSession().getAttribute("admin")).getRole();
		String admin_name = ((Admin)request.getSession().getAttribute("admin")).getName();
		String sysry_id = ((Admin)request.getSession().getAttribute("admin")).getId();
		Sys_Admin admin = AdminDAO.loadSysry(sysry_id);
		Map<String,String> map_whcd = AdminDAO.loadWhcd();
		Iterator it_whcd = map_whcd.keySet().iterator();
		Map<String,String> map_zyzc = AdminDAO.loadZyzc();
		Iterator it_zyzc = map_zyzc.keySet().iterator();
		Map<String,String> map_zyl = SysxxDAO.list_zyl();
		Iterator it_zy = map_zyl.keySet().iterator();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link rel="stylesheet" type="text/css" href="css/global.css" />
<script src="js/jquery.min.js"></script>
<script src="js/jquery.easing.min.js"></script>
<script src="js/base.js"></script>
<script src="js/jquery-1.9.1.js"></script>
<script src="js/jquery-ui-1.10.2.js"></script>
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

function getLa(key){ 
	$.ajax({url:"LaboratoryServlet?method=getAjax_zy&sign="+key,
		type:"GET",
		async:false,
		dataType:"text",
		success: function(text){
	 		$("#zy_ajax").html(text);
		}
	})
}

function choicetest(name,num){ 
	var choicearr = document.getElementsByName(name); 
	var a=0; 
	for(var i=0;i<choicearr.length;i++) 
	if(choicearr[i].checked){ 
		a=a+1; 
	} 
	if(a==num){ 
		for(var i=0;i<choicearr.length;i++) 
		if(!choicearr[i].checked) 
		choicearr[i].disabled='disabled'; 
		}else{ 
		for(var i=0;i<choicearr.length;i++) 
		choicearr[i].removeAttribute('disabled'); 
	} 
} 

</script>
<div id="position">您现在所在页面：实验室管理&nbsp;&gt;&gt;&nbsp;实验室人员情况</div>
<div class="detailInfoContent">
<%@ include file="showMsg.jsp" %>
<form action="AdminServlet" method="post" onsubmit="return submitfrm(this);" >
<input type="hidden" name="method" value="updateSysry" />
<input type="hidden" name="sysry_id" value="<%=sysry_id %>">
<input type="hidden" name="sy_name_id" value="<%=admin_name %>">
<table border=1 cellspacing=0 cellpadding=0 style="border:none;border-left:1px solid #CCCCCC;border-bottom:1px solid #CCCCCC;">
  	<tr>
  		<td class="TD_Left">基本情况</td>
  		<td class="TD_Right">
			<!-- 查dbf表查年月格式 -->
			出生年月<input type="text" name="birthday" value="<% if(admin.getBirthday() == null){ %><%} else{%><%=admin.getBirthday() %> <%} %>" style="width: 50px;"></input>
			人员类别
				<select name="rylb">
					<option value="0">请选择</option>
			   		<option value="1" <% if(admin.getRylb() == 1){ %> selected="selected" <% } %> >专任</option>
			   		<option value="2" <% if(admin.getRylb() == 2){ %> selected="selected" <% } %> >兼任</option>
		   		</select>
		   	性别<select name="sex">
		   		<option value="0">请选择</option>
			  	<option value="1" <% if(admin.getSex() == 1){ %> selected="selected" <% } %> >男</option>
			  	<option value="2" <% if(admin.getSex() == 2){ %> selected="selected" <% } %> >女</option>
			  </select><br/>
		</td>
  	</tr>
  	<tr>
  		<td class="TD_Left">文化程度</td>
  		<%  
  			String whcdMc = (admin.getWhcd()+"").trim();
  			if(whcdMc.trim().length() == 1){
  				whcdMc = "0" + whcdMc;
  			}
  		%>
  		
  		<td class="TD_Right"> 
  		<!--  
  			<%if(Utils.getWhcdMc(whcdMc) == null || Utils.getWhcdMc(whcdMc).trim().length() == 1 || "null".equals(Utils.getWhcdMc(whcdMc))){ %>未选<%} else{%><%=Utils.getWhcdMc(whcdMc) %> <%} %><br/>
  		-->
			<select name="whcd" id="whcd_id">
				<option value="0">请选择</option>
				<% while(it_whcd.hasNext()){
					String key = (String)it_whcd.next();
					String value = map_whcd.get(key);
				 %>
					<option value="<%=key %>" ><%=value %></option>
				<% } %>
			   
		   	</select>
		</td>
  	</tr>
  	<tr>
  		<td class="TD_Left">所学专业</td>
  		<td class="TD_Right">  <!-- 专业列表   需导入 -->
  			<% String zyMc = (admin.getSxzy()+"").trim();%>
  			<% if(Utils.getZyMc(zyMc) == null || Utils.getZyMc(zyMc).trim().length() == 0 || "null".equals(Utils.getZyMc(zyMc))){ %>未选<%} else{%><%=Utils.getZyMc(zyMc) %> <%} %><br/>
			<select name="ssxk_zyl" onchange=getLa(this.value)>
				<option value="0" selected="selected">请选择专业类</option>
				<% while(it_zy.hasNext()){ 
						String key = (String)it_zy.next();
						String value = (String)map_zyl.get(key);%>
						
					<option value="<%=key %>"><%=value %></option>
					
				<%} %>
			</select>
			<div id="zy_ajax">
				<select name="ssxk_zy">
					<option value="0">请选择专业类</option>
				</select>
			</div>
		</td>
  	</tr>
  	
  	<tr>
  		<td class="TD_Left">专业职务</td>
  		<td class="TD_Right"><!-- 需提供 -->
  			<% String zyZw = admin.getZyzw();System.out.println("zyzw is " + Utils.getZwMc(zyZw));%>
  			<!--
  			<% if(Utils.getZwMc(zyZw) == null || Utils.getZwMc(zyZw).trim().length() == 0 || "null".equals(Utils.getZwMc(zyZw))){ %>未选<%} else{%><%=Utils.getZwMc(zyZw) %><%} %><br/>
			-->
			<select name="zyzw" id="zyzw_id">
				<option value="0">请选择</option>
				<% while(it_zyzc.hasNext()){
					String key = (String)it_zyzc.next();
					String value = map_zyzc.get(key);
				 %>
					<option value="<%=key %>" ><%= value %></option>
				<% } %>
			   
		</td>
  	</tr>
  	
  	<tr>
  		<td class="TD_Left">专家类型</td>
  		<td class="TD_Right" style="width:450px;" onclick="choicetest('zjlx',2)"> <!-- 做下限制   最多只能选两个   还未完成 -->
  			<% 
  				int one = 0;
  				if((admin.getZjlx()+"").trim().length() == 1){
  					one = Integer.parseInt((admin.getZjlx()+"").trim().substring(0,1));
  				}
  				int two = 0 ;
  				if((admin.getZjlx()+"").trim().length() == 2){
  					two = Integer.parseInt((admin.getZjlx()+"").trim().substring(1,2));
  				}
  				
  			%>
			院士<input type="checkbox" name="zjlx" value="1" <% if(one == 1 || two == 1){ %>checked="checked" <%} %> ></input>
			长江学者<input type="checkbox" name="zjlx" value="2" <% if(one == 2 || two == 2){ %>checked="checked" <%} %> ></input>
			杰出青年基金获得者<input type="checkbox" name="zjlx" value="3" <% if(one == 3 || two == 3){ %>checked="checked" <%} %> ></input><br/>
			国家级教学名师<input type="checkbox" name="zjlx" value="4" <% if(one == 4 || two == 4){ %>checked="checked" <%} %> ></input>
			省级教学名师<input type="checkbox" name="zjlx" value="5" <% if(one == 5 || two == 5){ %>checked="checked" <%} %> ></input>
		</td>
  	</tr>
  	<tr>
  		<td class="TD_Left">进修培训时间（天）</td>
  		<td class="TD_Right">
			国内：      学历教育<input type="text" name="gn" value="<%=admin.getGnxljy() %>" style="width: 50px;"></input>
					非学历教育<input type="text" name="gnf" value="<%=admin.getGnfxljy() %>" style="width: 50px;"></input><br/>
			国外：	 学历教育<input type="text" name="gw" value="<%=admin.getGwxljy() %>" style="width: 50px;"></input>
					非学历教育<input type="text" name="gwf" value="<%=admin.getGwfxljy() %>" style="width: 50px;"></input>
		</td>
	</tr>
	<tr>
  		<td class="TD_Left">备注</td>
  		<td class="TD_Right"><textarea name="specialty" rows="4" cols="50"></textarea></td>
	</tr>
  	<tr>
  		<td class="TD_Left">操作</td>
  		<td class="TD_Right"><input type="submit" value="修改" class="form_btn"/>
  	</tr>
</table>  
</form>
</div>
注: 所有数据都必须填写(备注,专家类型和培训时间除外)<br/>
	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;出生年月格式需要固定。例如1993年10则填写为199310<br/>
	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;专家类型可写可不写,最多只能选择两种<br>
	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;备注可写可不写<br/>
	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;培训时间可写可不写<br/>
	
<script type="text/javascript">
	$("#zyzw_id").val("<%= admin.getZyzw()%>");
	$("#whcd_id").val("<%= whcdMc%>");
</script>
</body>
</html>