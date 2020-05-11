/*$(document).ready(function() {
  $(".heart").click(function(e) {
	console.log(e.target.id);
	console.log($(this).attr("id"));
	
	let imageId = $(this).attr("id");  	
    let msg = like(imageId);
    
    if(msg === "ok"){
    	$(target).toggleClass("heart-clicked fa-heart fa-heart-o");
    }
  })
});*/

function onFeedLoad(imageId){
	let msg = like(imageId);
	if(msg === "ok"){
	  $("#"+imageId).toggleClass("heart-clicked fa-heart fa-heart-o");
	}else{
		alert('오류');
	}
}


aysnc function like(imageId){
	 let response = await fetch("/image/like/{imageId}", {
		 method:"POST";
	 });
	 let msg = await response.text();
	 
	 return msg;
}