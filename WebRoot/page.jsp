<%@ page language="java" pageEncoding="utf-8"%>
<%  int pageNo = (Integer)request.getAttribute("pageNo");
	int totalPages = (Integer)request.getAttribute("totalPages");%>
<div id="page">
<table cellpadding="0" cellspacing="5" >
	<tr>
		<td id="total">第<%=pageNo %>页，共<%= totalPages%>页</td>
		<td class="char"><a href="<%= baseUrl%>">第一页</a></td>
		<td class="char"><a href="<%= baseUrl%>&pageNo=<%=pageNo - 1 %>">上一页</a></td>
		
		<%for(int i=1; i<=10 && i <= totalPages; i++){%>
		<td class="num" ><a <% if(i == pageNo) out.println("id='selected_num'");%> href="<%= baseUrl%>&pageNo=<%= i%>"><%= i%></a></td>
		<%}%>	
		
		<%if(totalPages == 12){%>
		<td class="num" ><a <% if(11 == pageNo) out.println("id='selected_num'");%> href="<%= baseUrl%>&pageNo=11">11</a></td>
		<%}else if(totalPages == 13){%>
		<td class="num"><a <% if(11 == pageNo) out.println("id='selected_num'");%> href="<%= baseUrl%>&pageNo=11">11</a></td>
    	<td class="num"><a <% if(12 == pageNo) out.println("id='selected_num'");%> href="<%= baseUrl%>&pageNo=12">12</a></td>
		<%}else if(totalPages > 13 && (pageNo <= 10 || pageNo == totalPages)){%>
		<td style="border:none;">...</td>
		<%}else if(totalPages > 13 && pageNo == 11){%>
		<td class="num" ><a id='selected_num' href="<%= baseUrl%>&pageNo=11">11</a></td>
		<td style="border:none;">...</td>
		<%}else if(totalPages > 13 && pageNo > 11 && pageNo < totalPages){
			if(pageNo == 12){%>
			<td class="num" ><a	 href="<%= baseUrl%>&pageNo=11">11</a></td>
			<td class="num" ><a id='selected_num' href="<%= baseUrl%>&pageNo=12">12</a></td>
			<td style="border:none;">...</td>
			<%}else{%>
			<td style="border:none;">...</td>
      	 	<td class="num"><a id='selected_num' href="<%= baseUrl%>&pageNo=<%= pageNo%>"><%= pageNo%></a></td>
       			<%if(pageNo != totalPages-1){%>
				<td style="border:none;">...</td>
				<%}%>
			<%}%>
		<%}%>	
		
		<%if(totalPages > 10){%>
		<td class="num" ><a <% if(totalPages == pageNo) out.println("id='selected_num'");%> href="<%= baseUrl%>&pageNo=<%= totalPages%>"><%= totalPages%></a></td>
		<%}%>
		
		<td class="char"><a href="<%= baseUrl%>&pageNo=<%= pageNo+1 %>">下一页</a></td>
		<td class="char"><a href="<%= baseUrl%>&pageNo=<%= totalPages%>">最末页</a></td>
	</tr>
</table>
</div>    