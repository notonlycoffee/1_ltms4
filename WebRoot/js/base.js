

$(document).ready(function(){
	 
	$("a").mouseenter(function(){
		if($(this).text()!=''){
			$(this).attr("title",$(this).text());
		}
	})
	
	$(".anPaiList li").eq(0).addClass("moveOn");
	
	$(".anPaiList li").mouseenter(function(){
	    $(this).parent().children("li").removeClass("moveOn");
		$(this).parent().children("li").css("cursor","pointer");
	    $(this).addClass("moveOn");
	    var num = $(this).index();
	    ex_ajax($("#ex_id").text(), encodeURI(encodeURI($("#ex_term").text())), num+1, 10);
	 })

	function ex_ajax(laboratoryID, currenTerm, pageNo, pageSize){
		$.ajax({url:"ExperimentServlet?method=index_list_obl&laboratoryID="+laboratoryID+"&currenTerm="+currenTerm+"&pageNo="+pageNo+"&pageSize="+pageSize,
			type:"GET",
			async:false,
			dataType:"text",
			success: function(text){
				$(".contentBox").html(text);
			}
		})
	}
	
	//修改新闻内容页的图片宽度
	function changeImgSize(){
		var images=$("#inform_content_ltms img");
		for(var i=0;i<images.length;i++){
			if($(images).eq(i).width()>850){
				var hw = $(images).eq(i).height() / $(images).eq(i).width();
				$(images).eq(i).width(850);
				$(images).eq(i).height(850*hw);
				$(images).eq(i).css("display", "block");
			}
		}
		var tabs=$("#inform_content_ltms table");
		for(var i=0;i<tabs.length;i++){
			var hw = $(tabs).eq(i).height() / $(tabs).eq(i).width();
			if($(tabs).eq(i).width()>850){
				$(tabs).eq(i).width(850);
				$(tabs).eq(i).height(850*hw);
				$(tabs).eq(i).css("display", "block");
			}
		}
	}	
	changeImgSize();

	var imgNum=1;
	function a1(){
	   var bannerList=$(".home_banner_slide ul li");
	   var listMaxNum=$(bannerList).length;
	   $(bannerList).fadeOut("fast"); 
	    $(bannerList).eq(imgNum).fadeIn("slow");
	   imgNum++;
	   if(imgNum==listMaxNum){imgNum=0;}
	   //alert(liNum)
	}
	setInterval(a1,10000);
 
	function noticeChangge() {
	    var _wrap = $('ul.line'); //定义滚动区域
	    var _interval = 2000; //定义滚动间隙时间
	    var _moving; //需要清除的动画
	    _wrap.hover(function() {
	      clearInterval(_moving); //当鼠标在滚动区域中时,停止滚动
	    },
	    function() {
	      _moving = setInterval(function() {
	        var _field = _wrap.find('li:first'); //此变量不可放置于函数起始处,li:first取值是变化的
	        var _h = _field.height(); //取得每次滚动高度
	        _field.animate({
	          marginTop: -_h + 'px'
	        },
	        600,
	        function() { //通过取负margin值,隐藏第一行
	          _field.css('marginTop', 0).appendTo(_wrap); //隐藏后,将该行的margin值置零,并插入到最后,实现无缝滚动
	        })
	      },
	      _interval) //滚动间隔时间取决于_interval
	    }).trigger('mouseleave'); //函数载入时,模拟执行mouseleave,即自动滚动
	}
	noticeChangge();
 
	// flash
	var href=$("ul.pic123 li a");
	var images=$("ul.pic123 li a img");
	var imagesAddress="";
	var hrefAddress="";
	for(var a=0;a<$(images).length;a++){
		if(imagesAddress!=""){
			imagesAddress+=","
			}
		if(hrefAddress!=""){
			hrefAddress+=","
			}
		imagesAddress+=$(images).eq(a).attr("src");
		hrefAddress+=$(href).eq(a).attr("href");
		}
	var so = new SWFObject("focus.swf", "focus", "422", "248", "7", "#625F5B");
	so.addParam('wmode','transparent');
	so.addVariable("picurl",imagesAddress);
	//so.addVariable("pictext","图片一,图片二,图片三");
	so.addVariable("piclink",hrefAddress);
	so.addVariable("pictime","5");
	so.addVariable("borderwidth","422");
	so.addVariable("borderheight","268");
	so.addVariable("borderw","false");
	so.addVariable("buttondisplay","true");
	so.addVariable("textheight","20");
	so.write("focus");

})