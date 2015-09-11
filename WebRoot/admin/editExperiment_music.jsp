<%@ page language="java" import="java.util.*, com.ltms.model.*, java.sql.*,com.ltms.dao.*" pageEncoding="utf-8"%>
<%@page import="com.ltms.util.StringUtil"%>
<%  
	int id = Integer.parseInt(request.getParameter("id"));
	Experiment experiment = ExperimentDAO.load(id);
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
<% 	Teacher teacher = (Teacher)request.getSession().getAttribute("teacher");
	int departmentID = teacher.getUnitID();
	String teacherName = teacher.getName();
	List<_Class> classList = ClassDAO.list(departmentID);	
	List<Laboratory> laboratoryList = LaboratoryDAO.list(departmentID);	
	List<Course> courseList = CourseDAO.list(departmentID);	
	List<Teacher> teacherList = TeacherDAO.list(departmentID);	
	List<Unit> unitList = UnitDAO.list();
	String weekday[] = {"周一", "周二", "周三", "周四", "周五"};
	String everyWeek[] = {"每周", "单周", "双周"};
	char groupNum[] = {'A', 'B', 'C', 'D', 'E'};
	String requirement[] = {"必修", "选修", "其他"};
	String type[] = {"演示", "验证", "综合", "设计"};
	String sylb[] = {"基础", "专业基础", "专业", "其他", "课程设计", "毕业论文", "毕业设计", "科研", "社会开发", "社会服务"};
	String syzlb[] = {"博士生", "硕士生", "本科生", "专科生", "其他"};
	String c_option_string = "";
	for(_Class c : classList){
		c_option_string += "<option value='" + c.getId() + "'>" + c.getId() + " " + c.getName() + "</option>";
	}
	String t_option_string = "";
	for(Teacher t : teacherList){
		t_option_string += "<option value='" + t.getId() + "'>" + t.getName() + "</option>";
	}
	
	Map<String,String> map_zyl = SysxxDAO.list_zyl();
	Iterator it_zy = map_zyl.keySet().iterator();
	
%>
<div id="position">您现在所在页面：实验室管理&nbsp;&gt;&gt;&nbsp;<a href="ExperimentServlet?method=list_music&teacherID=<%= teacher.getId()%>" target="content">实验管理</a>&nbsp;&gt;&gt;&nbsp;修改实验信息</div>
<div class="detailInfoContent">
<script type="text/javascript">

function add_mxzy(value){
	
	var zy = document.getElementById("ssxk_mxzy").value;
	//alert( zy+ "-" + value + ",")
	var old_mes = document.getElementById("mxzy_textarea").value;
	document.getElementById("mxzy_textarea").value = old_mes + zy + "-" + value + ",";
	var select_xz = document.getElementById("select_xz");
	select_xz.selectedIndex = 0;
}

var timeCount = 0;       // 变动------------------
var classCount = 1;
var l_teacherCount = 0;
var o_teacherCount = 0;
String.prototype.trim = function(){ return this.replace(/(^\s*)|(\s*$)/g, "");}
function ShowErrMsg(Info)
{
	document.getElementById("showMsg").innerHTML = Info;
}
function submitfrm(frm)
{
	
	var sclassInfo = ""
	var steacherInfo = ""
	var stimeInfo = ""                     // 变动------------------
	var ctt = 1;                     	   // 变动------------------

		
	for(var cn=1; cn<=classCount; cn++){
		if($("#classID"+cn).length>0 && $("#classID"+cn).val() != 0){
			sclassInfo += $("#classID"+cn).val() + "@" + $("#groupInfo"+cn).val() + "@";
		}
	}
	sclassInfo += "theendofthestring";
	frm.classInfo.value = sclassInfo;
	steacherInfo += "0,<%= teacher.getId()%>,"
	for(var cn=1; cn<=l_teacherCount; cn++){
		if($("#l_teacherID"+cn).length>0 && $("#l_teacherID"+cn).val() != 0){
			steacherInfo += "0," + $("#l_teacherID"+cn).val() + ",";
		}
	}
	frm.teacherInfo.value = steacherInfo;
	if(frm.teacherInfo && frm.teacherInfo.value.trim()==""){
		ShowErrMsg("请选择一个教师");
		frm.l_teacherID1.focus();
		return false;
	}

	 // 变动------------------
	for(;ctt<=timeCount; ctt++){
		if(!(document.getElementById("is_del_time"+ctt).checked)) break;
	}
	if(ctt==timeCount+1){
		ShowErrMsg("至少要保留一个实验时间段,请重新选择!");
		frm.is_del_time1.focus();
		return false;
	}
	for(var cnt=1; cnt<=timeCount; cnt++){
		if(!(document.getElementById("is_del_time"+cnt).checked)){
			if(stimeInfo!='') stimeInfo +="@";
			if(document.getElementById("is_sep_week"+cnt).checked){
				stimeInfo +="1"
				for(var swn=1; swn <= 20; swn++){
					if(document.getElementById("sep_week" + cnt + "_num" + swn).checked == true){
						stimeInfo += "," + swn;
					}
				}
				stimeInfo += ",每周," + document.getElementById("weekday"+cnt).value + "," + document.getElementById("startTime"+cnt).value + "," +document.getElementById("endTime"+cnt).value;
			}else if(document.getElementById("is_conti_week"+cnt).checked){
				stimeInfo += "0," + document.getElementById("startWeek"+cnt).value + "," + document.getElementById("endWeek"+cnt).value
				+ "," + document.getElementById("isEveryWeek"+cnt).value + "," + document.getElementById("weekday"+cnt).value + 
				"," + document.getElementById("startTime"+cnt).value + "," +document.getElementById("endTime"+cnt).value;
			}
		}
	}
	frm.timeInfo.value = stimeInfo; 
    // 变动------------------	
	
	return true;
}

//变动------------------
function add_time(){
	timeCount ++;
	var num = timeCount;
	var curvediv = document.createElement("div");
	curvediv.setAttribute("id", "timeInfo"+num); 
	curvediv.setAttribute("class", "timeInfo"); 
	var icstring = "";
	icstring += "<div id=\"conti_week" + num + "\">";
	icstring += "第<select name=\"startWeek" + num + "\" id=\"startWeek" + num + "\">";
	for(var i=1; i<=20; i++){
		icstring += "<option value=\"" + i + "\">" + i + "</option>";
	}
	icstring += "</select>周-&nbsp;第<select name=\"endWeek" + num + "\" id=\"endWeek" + num + "\">";
	for(var i=1; i<=20; i++){
		icstring += "<option value=\"" + i + "\">" + i + "</option>";
	}
	icstring += "</select>周(<select name=\"isEveryWeek" + num + "\" id=\"isEveryWeek" + num + "\">";
	<% for(int i=0; i<3; i++){%> 
		icstring += "<option value=\"<%= everyWeek[i]%>\"><%= everyWeek[i]%></option>";
	<% }%>
	icstring += "</select>)不连续周数：<input type=\"checkbox\" id=\"is_sep_week" + num + "\" name=\"is_sep_week" + num + "\" onchange=\"show_sep_week_div(" + num + ")\"/>";
	icstring += "</div><div id=\"sep_week" + num + "\" style=\"display:none;\">";
	for(var swn=1; swn<=20; swn++){
		icstring += swn + "<input type=\"checkbox\" name=\"sep_week" + num + "_num" + swn + "\" id=\"sep_week" + num + "_num" + swn + "\"/>";
		if(swn==11) icstring += "<br/>";
	}
	icstring += "<br/>连续周数：<input type=\"checkbox\" id=\"is_conti_week" + num + "\" name=\"is_conti_week" + num + "\" checked=\"checked\" onchange=\"show_conti_week_div(" + num + ")\"/></div>";
	icstring += "<select name=\"weekday" + num + "\" id=\"weekday" + num + "\">";
	<% for(int i=0; i<5; i++){%> 
		icstring += "<option value=\"<%= weekday[i]%>\"><%= weekday[i]%></option>";
	<% }%>
	icstring += "</select>第<select name=\"startTime" + num + "\" id=\"startTime" + num + "\">";
	for(var i=1; i<=10; i++){
		icstring += "<option value=\"" + i + "\">" + i + "</option>";
	}
	icstring += "</select>节&nbsp;-&nbsp;第<select name=\"endTime" + num + "\" id=\"endTime" + num + "\">";
	for(var i=1; i<=10; i++){
		icstring += "<option value=\"" + i + "\">" + i + "</option>";
	}
	icstring += "</select>节";
	icstring += "<br/>删除该实验时间段：<input type=\"checkbox\" id=\"is_del_time" + num + "\" name=\"is_del_time" + num + "\" onchange=\"del_time_div(" + num + ")\" />";
	curvediv.innerHTML = icstring;	
	document.getElementById("more_time").appendChild(curvediv); 
	var cleardiv = document.createElement("div");
	cleardiv.setAttribute("class", "clear"); 
	document.getElementById("more_time").appendChild(cleardiv); 
	$("#endWeek"+num).val("18");
	$("#endTime"+num).val("2");
}
// 变动------------------

function add_class(){
	classCount ++;
	var num = classCount;
	var curvediv = document.createElement("div");
	curvediv.setAttribute("class", "ex_class_td"); 
	var icstring = "";
	icstring += "<select name=\"classID" + num + "\" id=\"classID" + num + "\">"
	icstring += "<option value=\"0\">选择班级</option>";
	icstring += "<%= c_option_string%>";
	icstring += "</select>&nbsp;";	
	icstring += "备注：&nbsp;";	
	icstring += "<input name=\"groupInfo" + num +"\" id=\"groupInfo" + num + "\" class=\"groupInfo_textarea\">";
	icstring += "";
	curvediv.innerHTML = icstring;	
	document.getElementById("more_class_info_ltms").appendChild(curvediv); 
	var cleardiv = document.createElement("div");
	cleardiv.setAttribute("class", "clear"); 
	document.getElementById("more_class_info_ltms").appendChild(cleardiv); 
}

function add_teacher(){
	l_teacherCount ++;
	var num = l_teacherCount;
	var curvese = document.createElement("span");
	var icstring = "<select name='l_teacherID" + num + "' id='l_teacherID" + num + "'><option value=\"0\">选择教师</option>";
	icstring += "<%= t_option_string%>";
	icstring += "</select>";
	curvese.innerHTML = icstring;	
	document.getElementById("more_teacher_info_ltms").appendChild(curvese); 
}
function get_ajax_te(num, unitID){
	$.ajax({url:"TeacherServlet?method=get_jq_Ajax&unitID="+unitID+"&num="+num,
			type:"GET",
			async:false,
			dataType:"text",
			success: function(text){
		 		document.getElementById("o_teacher_div" + num).innerHTML = text;
			}
			})
}

function submit_form(state){
	$("#state").val(state);
	$("form").first().submit();
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

function getLa_second(key){ 
	$.ajax({url:"LaboratoryServlet?method=getAjax_mxzy&sign="+key,
		type:"GET",
		async:false,
		dataType:"text",
		success: function(text){
	 		$("#mxzy_ajax").html(text);
		}
	})
}

</script>
<%@ include file="showMsg.jsp" %>

<form action="ExperimentServlet" method="post" onsubmit="return submitfrm(this);" >
<input type="hidden" name="method" value="update_music" />
<input type="hidden" name="id" value="<%=experiment.getId()%>" />
<input type="hidden" name="courseID" value="<%= experiment.getCourseID()%>" />
<input type="hidden" name="departmentID" value="<%=experiment.getDepartmentID()%>" />
<input type="hidden" name="classInfo" value="" />
<input type="hidden" name="teacherInfo" value="" />
<input type="hidden" name="timeInfo" value="" />
<input type="hidden" name="state" id="state" value="" />
<table border=1 cellspacing=0 cellpadding=0 style="border:none;border-left:1px solid #ccc;border-bottom:1px solid #ccc;">
  	<tr>
  		<td class="TD_Left">课程</td>
  		<td class="TD_Right"><%= experiment.getCourseID() + " " + CourseDAO.getName(experiment.getCourseID())%></td>
  	</tr>
  	<tr>
  		<td class="TD_Left">系别</td>
  		<td class="TD_Right">音乐系</td>
  	</tr>
  	<tr>
  		<td class="TD_Left">实验地点</td>
  		<td class="TD_Right">  <!-- 实验室 -->
    		<select name="laboratoryID" id="laboratoryID">
	   		<% for(Iterator<Laboratory> it = laboratoryList.iterator();it.hasNext();){
	   			   			Laboratory l = it.next();%> 
	   		 	<option value="<%=l.getId()%>"><%=l.getName()%></option>
	   		<% }%>
	   		</select>
		</td>
	</tr>
	 <!-- 新增加的项 -->
	<tr>
  		<td class="TD_Left">实验名称</td>
  		<td class="TD_Right"> 
    		<input type="text" name="symc" value="<%=experiment.getSymc() %>"></input>
		</td>
	</tr>
	
    <tr>
		<td class="TD_Left">实验时间</td>
		<td class="TD_Right">
			<div id="more_time">
			</div>
			<input type="button" value="添加实验时间" onclick="javascript:add_time()" class="form_btn2" />
   		</td>
 	</tr>
  	<tr>
  		<td class="TD_Left">班级</td>
  		<td class="TD_Right">
    	<div class="ex_class_td">
	    	<select name="classID1" id="classID1">
		   		<%= c_option_string%>
	   		</select>
	   		备注：
	   		<input name="groupInfo1" id="groupInfo1" class="groupInfo_textarea" />
	   		<input type="button" value="添加班级" onclick="javascript:add_class()" class="form_btn2"/>
	   	</div>
	   	<div class="clear"></div>
	   	<div id="more_class_info_ltms">
	   	</div>
	   	</td>
	</tr>
  	<tr>
  		<td class="TD_Left">教师</td>
		<td class="TD_Right">  
	   	<div id="more_teacher_info_ltms">
	   	<span id="music_teacher_ltms"><%= teacherName%></span>
	   	
	   	</div>
	   	<input type="button" value="添加教师" onclick="javascript:add_teacher()" class="form_btn2"/>
		</td>
  	</tr>
  	<tr>
		<td class="TD_Left">实验要求</td>
		<td class="TD_Right">  
  		<select name="requirement" id="requirement">
			<% for(int i=0; i<3; i++){%> 
				<option value="<%= requirement[i]%>"><%= requirement[i]%></option>
			<%}%>
		</select>
		</td>
	</tr>
	<tr>
		<td class="TD_Left">实验类型</td>
		<td class="TD_Right">  
  		<select name="type" id="type">
			<% for(int i=0; i<4; i++){%> 
				<option value="<%= type[i]%>"><%= type[i]%></option>
			<% }%>
		</select>
		</td>
	</tr>
	 <!-- 新增加的项 -->
	<tr>
		<td class="TD_Left">实验类别</td>
		<td class="TD_Right">  
  		<select name="sylb">
			<% for(int i=0; i<10; i++){%> 
				<option value="<%= (i+1)%>"><%= sylb[i]%></option>
			<% }%>
		</select>
		</td>
	</tr>
	
	<!-- 新增加的项 -->
	<tr>
		<td class="TD_Left">学时</td>
		<td class="TD_Right">  
	  		<input type="text" name="xs" style="50px" id="xueshi" value="<%=experiment.getXs() %>"></input>
		</td>
	</tr>
	
	 <!-- 新增加的项 -->
	<tr>
		<td class="TD_Left">实验者专业</td>
		<td class="TD_Right">
			<select name="ssxk_zyl" onchange=getLa(this.value)>
				<option value="" selected="selected">请选择专业类</option>
				<% while(it_zy.hasNext()){ 
						String key = (String)it_zy.next();
						String value = (String)map_zyl.get(key);%>
						
					<option value="<%=key %>"><%=value %></option>
					
				<%} %>
			</select>
			<div id="zy_ajax">
				<select name="ssxk_zy">
					<option value="">请选择专业类</option>
				</select>
			</div>
		</td>
	</tr>
	
	 <!-- 新增加的项 -->
	<tr>
		<td class="TD_Left">实验者类别</td>
		<td class="TD_Right">  
  		<select name="syzlb">
			<% for(int i=0; i<5; i++){%> 
				<option value="<%= (i+1)%>"><%= syzlb[i]%></option>
			<% }%>
		</select>
		</td>
	</tr>
	
	<tr>
		<td class="TD_Left">所属学科</td>
		<td class="TD_Right">  
	  		<select name="ssxk">
				<option value="" selected="selected">请选择专业类</option>
				<% 
					it_zy = map_zyl.keySet().iterator();
					while(it_zy.hasNext()){ 
						String key = (String)it_zy.next();
						String value = (String)map_zyl.get(key);%>
							
					<option value="<%=key %>"><%=value %></option>
						
				<%} %>
			</select>
		</td>
	</tr>
	 <!-- 新增加的项 -->
	<tr>
		<td class="TD_Left">面向专业</td>
		<td class="TD_Right">
			<!-- 要设置成不可写   只能读 -->
			<textarea name="mxzy" rows="4" cols="35" id="mxzy_textarea"><%=experiment.getMxzy() %></textarea><br/>
			<select name="ssxk_zyl" onchange=getLa_second(this.value)>
				<option value="" selected="selected">请选择专业类</option>
				<% 	
					it_zy = map_zyl.keySet().iterator();
					while(it_zy.hasNext()){ 
						String key = (String)it_zy.next();
						String value = (String)map_zyl.get(key);%>
						
					<option value="<%=key %>"><%=value %></option>
					
				<%} %>
			</select>
			<div id="mxzy_ajax">
				<select name="ssxk_mxzy" id="ssxk_mxzy">
					<option value="" id="mxzy_id">请选择专业类</option>
				</select>
			</div>
			<select name="xz" id="select_xz" onchange=add_mxzy(this.value)>
				<option >请选择</option>
				<option value="1">必修</option>
				<option value="2">选修</option>
				<option value="3">其他</option>
			</select>
		</td>
	</tr>
	
  	<tr>
  		<td class="TD_Left">操作</td>
  		<td class="TD_Right"> 
  		<input type="button" value="保存" onclick="javascript:submit_form(0)" class="form_btn"/>
  		<input type="button" value="提交" onclick="javascript:submit_form(1)" class="form_btn"/>
  		<input type="button" value="返回实验列表" onclick="javascript:window.location.href='ExperimentServlet?method=list_music&teacherID=<%= teacher.getId()%>'" class="form_btn2" />
  		</td>
  	</tr>
</table>  
</form>
</div>
<div class="ps_foot">
<strong>注：</strong><br/>
1.第一个教师负责填写教学进度表<br/>
2.添加完班级或者教师后如果不需要班级或者教师信息请留空即可<br/>
3.班级备注中请勿使用字符@<br/>
4.选择连续周数/不连续周数后若选择框没改变请点击一下页面任一空白处<br/>
5.点击保存后仍可修改实验信息, 点击提交后将无法更改实验信息,要更改实验信息请联系本系教务员
6.实验者专业里面的专业是对应于省系统的，如果没有学校对应的专业可以选择相似专业。<br/>
7.面向专业是对应于省系统数据，专业相似即可。选择过程必须是按照从上到下选择三个下拉列表，否则选择不成功
</div>
<script type="text/javascript">
$("#laboratoryID").val("<%= experiment.getLaboratoryID()%>");
$("#requirement").val("<%= experiment.getRequirement()%>");
$("#type").val("<%= experiment.getType()%>");
var classInfo = "<%= experiment.getClassInfo()%>".split("@");
var teacherInfo = "<%= experiment.getTeacherInfo()%>".split(",");
var timeInfo = "<%= experiment.getTimeInfo()%>".split("@");

for(var sss=0; sss<timeInfo.length; sss++){
	var snum = sss+1;
	add_time();
	var week = timeInfo[sss].split(",");
	var week_length = week.length;

	//处理周数信息
	if(week[0] == "0"){
		$("#sep_week"+snum).css("display", "none");
		$("#conti_week"+snum).css("display", "block");
		$("#is_conti_week"+snum).attr("checked", "checked");
		$("#startWeek"+snum).val(week[1]);
		$("#endWeek"+snum).val(week[2]);
	}else{
		$("#conti_week"+snum).css("display", "none");
		$("#sep_week"+snum).css("display", "block");
		$("#is_sep_week"+snum).attr("checked", "checked");
		for(var ii=1; ii<week.length-3; ii++){
			$("#sep_week"+snum+"_num"+week[ii]).attr("checked", "checked");
		}
	}
	$("#isEveryWeek"+snum).val(week[week_length-4]);
	$("#weekday"+snum).val(week[week_length-3]);
	$("#startTime"+snum).val(week[week_length-2]);
	$("#endTime"+snum).val(week[week_length-1]);
}

//处理班级信息
$("#classID1").val(classInfo[0]);
$("#groupInfo1").val(classInfo[1]);
for(var ii=2; ii<classInfo.length-1; ii++){
	add_class();
	var num = ii/2+1;
	$("#classID"+num).val(classInfo[ii++]);
	$("#groupInfo"+num).val(classInfo[ii]);
}
//处理教师信息
if(teacherInfo[0] != "0"){
	add_teacher_other();
	$("#te_unit1").val(teacherInfo[0]);
	$("#o_teacherID1").val(teacherInfo[0] + "," + teacherInfo[1]);
	for(var ii=2; ii<teacherInfo.length-1; ii++){
		add_teacher_other();
		var num = ii/2+1;
		$("#te_unit"+num).val(teacherInfo[ii]);
		$("#o_teacherID"+num).val(teacherInfo[ii++] + "," + teacherInfo[ii]);
	}
}else{
	var l_num = 0;
	var o_num = 0;
	for(var ii=2; ii<teacherInfo.length-1; ii++){
		var num = ii/2+1;
		if(teacherInfo[ii] == "0"){
			l_num++;
			add_teacher();
			$("#l_teacherID"+l_num).val(teacherInfo[++ii]);
		}else{
			o_num++;
			add_teacher_other();
			get_ajax_te(o_num, teacherInfo[ii]);
			$("#te_unit"+o_num).val(teacherInfo[ii++]);
			$("#o_teacherID"+o_num).val(teacherInfo[ii-1] + "," +teacherInfo[ii]);
		}
	}
}

function del_time_div(id){
	document.getElementById("timeInfo"+id).style.display = "none";
}

function show_sep_week_div(id){
	document.getElementById("conti_week"+id).style.display = "none";
	document.getElementById("sep_week"+id).style.display = "";
	document.getElementById("is_conti_week"+id).checked = ""
}

function show_conti_week_div(id){
	document.getElementById("sep_week"+id).style.display = "none";
	document.getElementById("conti_week"+id).style.display = "";
	document.getElementById("is_sep_week"+id).checked = ""
}
</script>
</body>
</html>