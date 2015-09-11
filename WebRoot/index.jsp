<%@ page language="java" pageEncoding="utf-8" %>
<%@ page import="java.util.*, com.ltms.model.*, java.sql.*, com.ltms.dao.*,com.ltms.util.*" %>
<%@ include file="global.jsp" %>
<%
	ArrayList<Notification> notificationList = NotificationDAO.list(4);
	ArrayList<Regulation> regulationList = RegulationDAO.list(7);
	ArrayList<ExItem> exItemList = ExItemDAO.indexList(10);
	ArrayList<Laboratory> laboratorymList = LaboratoryDAO.indexList(10);
	Map<Integer, String> departmentNameMap = DepartmentDAO.getNameMap();
	Map<Integer, String> laboratoryNameMap = ExItemDAO.getLaboratoryNameMap(exItemList);
	Map<Integer, String> laboratoryNameMap2 = RegulationDAO.getLaboratoryNameMap(regulationList);
	ArrayList<KeyLab> keyLabList = KeyLabDAO.list();
	List<Link> linkList = (ArrayList<Link>)session.getAttribute("linkList");	
	if(linkList == null){
    linkList = LinkDAO.list();
		session.setAttribute("linkList", linkList);
	}	
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<link rel="stylesheet" href="css/style.css" type="text/css"/>
<script src="js/jquery.min.js"></script>
<script src="js/jquery.easing.min.js"></script>
<script type="text/javascript" src="js/jquery.js" ></script>
<script type="text/javascript">
		$(document).ready(function(){
		var slide_num = $(".slide-content img").size();
		$(".btn-l").click(function(){
			$(".slide-content div").stop(true,true);
			if($(".slide-content div").css("left") == '0px'){
				$(".slide-content div").animate({left:-(slide_num-1)*284});
			}else if($(".slide-content div").css("left") == -(slide_num-1)*284+'px'){
				$(".slide-content div").animate({left:-(slide_num-2)*284});
			}else{
				$(".slide-content div").animate({left:$(".slide-content div").position().left+284});
			}
		});
		$(".btn-r").click(function(){
			$(".slide-content div").stop(true,true);
			if($(".slide-content div").css("left") == '0px'){
				$(".slide-content div").animate({left:-284});
			}else if($(".slide-content div").css("left") == -(slide_num-1)*284+'px'){
				$(".slide-content div").animate({left:0});
			}else{
				$(".slide-content div").animate({left:$(".slide-content div").position().left-284});
			}
		});	
		$(".down-pop .down-content").click(function(){
			$(".slide-content div").stop(true,true);
			$(".down-hide").fadeIn();
		});	
		$(".down-pop").mouseleave(function(){
			$(".slide-content div").stop(true,true);
			$(".down-hide").fadeOut();
		});			
		
		$(".down-pop .down-hide div").click(function(){
			$(".down-content div").text($(this).text());
			
			$("input[name='user_type']").val($(this).text());
			if($("input[name='user_type']").val() == '系统管理员'){
				$("input[name='role']").val("administrator_0");
				$(".slide-content div").stop(true,true);
				$(".down-hide").fadeOut();
				}
			if($("input[name='user_type']").val() == '学院主管领导'){
				$("input[name='role']").val("administrator_3");
				$(".slide-content div").stop(true,true);
				$(".down-hide").fadeOut();
				}
			if($("input[name='user_type']").val() == '实验室主任'){
				$("input[name='role']").val("administrator_4");
				$(".slide-content div").stop(true,true);
				$(".down-hide").fadeOut();
				}
			if($("input[name='user_type']").val() == '实验室管理员'){
				$("input[name='role']").val("sysgly_5");
				$(".slide-content div").stop(true,true);
				$(".down-hide").fadeOut();
				}
			if($("input[name='user_type']").val() == '教务员'){
				$("input[name='role']").val("administrator_1");
				$(".slide-content div").stop(true,true);
				$(".down-hide").fadeOut();
				}
			if($("input[name='user_type']").val() == '教师'){
				$("input[name='role']").val("teacher_2");
				$(".slide-content div").stop(true,true);
				$(".down-hide").fadeOut();
				}
		});
	});
</script>
<script type="text/javascript">
		String.prototype.trim = function(){ return this.replace(/(^\s*)|(\s*$)/g, "");}
		function ShowErrMsg(Info)
		{
			document.getElementById("showMsg").innerHTML = Info; 
		}
		function submitfrm(frm)
		{
			if(frm.id.value.trim()=="")
			{
				alert("用户名不能为空，请输入");
				 frm.id.focus();
				 return false;
			}
			if(frm.userpwd.value.trim()=="")
			{
				alert("密码不能为空，请输入");
				 frm.userpwd.focus();
				 return false;
			}
			if(frm.role.value.trim()=="")
			{
				alert("角色不能为空，请选择");
				 return false;
			}
			return true;
		}
</script>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title><%= site_name%></title>
</head>

<body>
	<div id="head">
    	<div class="title">
        <img src="imgs/title.png" />
        <!-- 右上角小导航开始
            <ul class="nav">
            	<li><a href="index.jsp">首页</a></li>
            </ul>  -->
            <!--右上角小导航结束-->
        </div>
        <!--主导航开始-->
        <div class="navbar">
        	<ul>
        		<li><a href="index.jsp">首页</a></li><img src="imgs/navbar_br.png" />
            	<li><a href="LaboratoryServlet?method=webList">实验室</a></li><img src="imgs/navbar_br.png" />
                <li><a href="listExItem.jsp">实验教学</a></li><img src="imgs/navbar_br.png" />
                <li><a href="NotificationServlet?method=webList">通知公告</a></li><img src="imgs/navbar_br.png" />
                <li><a href="RegulationServlet?method=webList">管理制度</a></li><img src="imgs/navbar_br.png" />
                <li><a href="syllabus.jsp">实验课表</a></li><img src="imgs/navbar_br.png" />
                <li><a href="mesCount.jsp">统计分析</a></li>
            </ul>
        </div>
        <!--主导航结束-->
    </div>
    <div id="content">
    	<!--内容左开始-->
    	<div class="left">
        	<div class="slide">
				<div class="btn-l">
    				<img src="imgs/btn-l.png" width="27" height="34" alt="上一页" />
    			</div>
    			<div class="slide-content">
    				<div>
                    <%	List<Laboratory> picList = LaboratoryDAO.index_laboratory_pic(6);
					for(Laboratory l : picList){%>
                    <img src="<%= basepath%>/<%= l.getPic1()%>" width="284" height="168" />
				<%	}%>
        			</div>
    			</div>
				<div class="btn-r">
    				<img src="imgs/btn-r.png" width="27" height="34" alt="上一页" />
    			</div>    
			</div>
        	<div class="message">
            	<p class="ms_title">实验室通知<span class="more"><a href="<%= basepath%>/NotificationServlet?method=webList">more<img src="imgs/more.png" /></a></span></p>
                <p class="ms_img"><img src="imgs/ms_title.png" /></p>
                <div class="clear"></div>
            	<!-- <ul>
            	<li><a href="#">关于规范实验室管理员录入经..&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;2014-10-01</a></li>
                <li><a href="#">关于实验室管理员填报完整数..&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;2014-10-01</a></li>
                <li><a href="#">关于填报一门课程在多个实验..&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;2014-10-01</a></li>
                <li><a href="#">关于实验室管理员填报数据的..&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;2014-10-01</a></li>
            	</ul> -->
            	<ul>
				<%	for(Notification notification : notificationList){	
					String titleLength = notification.getTitle();
					if(notification.getTitle().length() > 13){
						titleLength = notification.getTitle().substring(0,13);
					}
					//System.out.println("length is " + notification.getTitle().length() );
					//System.out.println("titleLength is " + titleLength );
				%>
				<li><a href="<%= basepath%>/NotificationServlet?method=view&id=<%= notification.getId()%>">&nbsp;<%=titleLength%></a>...&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span style="padding-right: 5px"><%= notification.getDate()%></span></li>
				<%	}%>
				</ul>
           </div>
        </div>
        <!--内容左结束-->
        <!--内容右开始-->
        <div class="right">
        <div id="showMsg"><%=	(String)request.getAttribute("error") != null ? (String)request.getAttribute("error") : "" %></div>
       	  <form  id="form1" name="form1" method="post" action="LoginServlet" onsubmit="return submitfrm(this);">
            	<img class="icon" src="imgs/user.gif" /><img src="imgs/login_bor.png" />
            	<input class="input" id="id" type="text" name="id" placeholder="用户名"/>
                <p class="r_login_bbor"><img src="imgs/login_bot_bor.png" /></p>
            <img class="icon" src="imgs/key.gif" /><img src="imgs/login_bor.png" />
            <input class="input"  id="userpwd"  type="password" name="password" placeholder="密码" />
            <p class="r_login_bbor"><img src="imgs/login_bot_bor.png" /></p>
                <div class="down-pop">
					<div class="down-ico1"></div>
    				<div class="down-content">
    					<div>角色选择</div>
    				</div>
    				<div class="down-ico2"></div>
    				<div class="down-hide">
    					<div>系统管理员</div>
    					<div>学院主管领导</div>
        				<div>实验室主任</div>
                        <div>实验室管理员</div>
                        <div>教务员</div>
                        <div>教师</div>
    				</div>
    				<input value="" type="hidden" name="user_type" />
                    <input value="" type="hidden" name="role" value="0" />
				</div>
                <p class="r_login_bbor"><img src="imgs/login_bot_bor.png" /></p>
            <input class="submit" type="submit" value="登陆" id="button1" name="Button1" />
            </form>
        </div>
        <!--内容更右结束-->
        <div class="clear"></div>
        <!--底部开始-->
        <div class="cont_bottom">
        	<ul>
				<%	
					int i = 0;
					for(KeyLab keyLab : keyLabList){	
					i++;
				%>
				<li><a href="<%= keyLab.getGotoURL() %>" target="new_to_keylab_ltms">
				<%	if(keyLab.getPic() != null && !"".equals(keyLab.getPic())){ %>
				<%	}else{ %>
				<%	}%>
				<%= keyLab.getName()%></a></li>
				<%System.out.println("size is " + keyLabList.size()); %>
				<% if(i != keyLabList.size()){
				%>
				<%System.out.println("i is " + i); %>
				<p class="line2">|</p>
				<%
				} %>
				
				
				<%	}%> 
			</ul>
        </div>
        <!--底部结束-->
    </div>
    <!-- 页脚开始 -->
    <%@ include file="footer.jsp" %>
    <!-- 页脚结束 -->
</body>
</html>
