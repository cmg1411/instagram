function send_comment(id){
	var msg = $(".comment_contents_"+id).val();
	var comment = {
			'msg' : msg,
	        'id' : id
	}
	
	var comm = JSON.stringify(comment);
	
	if($.trim(comment) == '') {
		return false;
	}
	
	$('.comment_contents_'+id).val(null);
	
	
	$.ajax({      
        type:"POST",  
        contentType:"application/json; charset=utf-8",
        url:"/image/commentProc",
        data:comm,
        datatype:"json",
        success: function(data) {
        	//plusUl 변수에 createElement 를 사용해 생성할 엘리먼트를 담습니다.
            var plusUl = document.createElement('ul');
            // 추가할 plusUl 엘리먼트 안의 내용을 정해줍니다. ( 꼭 정해야 하는건 아닙니다. )
            plusUl.innerHTML =  "<li>"+data+" "+msg+"</li>";   
            // appendChild 로 이전에 정의한 plusUl 변수의 내용을 실제 추가합니다.
            document.getElementById('comment_form_'+id).appendChild(plusUl);
        }
    });  
}




function make_comment_box(image){
    let comment_box = `<div class="photo u-default-box">`;
	comment_box += `<header class="photo__header">`;	
	comment_box += `<img src="/upload/${image.user.profileImage}" />`;
	comment_box += `<div class="photo_user_info">`;
	comment_box += `<span class="photo__username">${image.user.username}</span>`;
	comment_box += `<span class="photo__location">${image.location}</span></div></header>`;
	comment_box += `<div class="photo_post_image">`;
	comment_box += `<img src="/upload/${image.postImage}" /></div>`;
	comment_box += `<div class="photo__info"><div class="photo__actions"><span class="photo__action">`;
	
	if(image.heart == true){
		comment_box += `<i onclick="onFeedLoad(${image.id})" id="${image.id}" class="fa fa-heart heart heart-clicked"></i>`;
	}else{
		comment_box += `<i onclick="onFeedLoad(${image.id})" id="${image.id}" class="fa fa-heart-o heart"></i>`;
	}
		
	comment_box += `</span> <span class="photo__action">`;
	comment_box += `<i class="fa fa-comment-o"></i></span></div>`;
	comment_box += `<span class="photo__likes" id="photo_likes_count_${image.id}">${image.likeCount}</span><span class="photo__likes"> likes</span><div class="photo_caption">`;
	comment_box += `<span class="photo__username">${image.user.username} </span>`;
	comment_box += `${image.caption}</div><div class="photo_tag">`;
    
	image.tags.forEach(function(tag){
		comment_box += `#${tag.name} `;
	});
	
	comment_box +=`</div>`;
	comment_box += `<ul class="photo__comments"><li class="photo__comment">`;
	comment_box += `<span class="photo__comment-author">serranoarevalo</span>`;
	comment_box += `i love this!</li><li class="photo__comment">`;
	comment_box += `<span class="photo__comment-author">serranoarevalo</span>`;
	comment_box += `i don't love this!</li></ul><span class="photo__date">${image.createDate}</span>`;
	comment_box += `<div class="photo__add-comment-container">`;
	comment_box += `<form class="comment__form" method="POST">`;
    comment_box += `<textarea placeholder="댓글 달기.."></textarea>`;
    comment_box += `<button class="comment__button qqq" type="submit">게시</button>`;
    comment_box += `</form></div >`;
    
    return comment_box;
}