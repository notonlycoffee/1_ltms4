$(document).ready(function(){
	extr_function();
})

function extr_function(){
	var js_extrs = $(".exTr");
	for(var jii=0; jii<js_extrs.length; jii++){
		if(jii%2 == 1){
			js_extrs.eq(jii).css("background-color", "#E3EFFB");
		}
	}
	
	$(".exTr").mouseenter(function(){
		$(this).css("background-color", "#F9EFD0");
		$(this).css("cursor","pointer");
	})
	 
	$(".exTr").mouseleave(function(){
		var num = $(this).index();
		if(num%2 == 1){
			$(this).css("background-color", "white");
		}else{
			$(this).css("background-color", "#E3EFFB");
		}
	});
}