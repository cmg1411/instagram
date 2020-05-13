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

async function onFeedLoad(imageId){
	let msg = await like(imageId);
	let likeCount = $("#photo_likes_count_"+imageId).text();

	if(msg === "like"){
		$("#photo_likes_count_"+imageId).text(Number(likeCount)+1);
		$("#"+imageId).toggleClass("heart-clicked fa-heart fa-heart-o");	
	}else if(msg === "unlike"){
		$("#photo_likes_count_"+imageId).text(Number(likeCount)-1);
		$("#"+imageId).toggleClass("heart-clicked fa-heart fa-heart-o");	
	}else{
		alert(msg);
	}
}


async function like(imageId){
	 let response = await fetch(`/image/like/${imageId}`, {
		 method:"POST"
	 });
	 let msg = await response.text();
	 
	 return msg;
}