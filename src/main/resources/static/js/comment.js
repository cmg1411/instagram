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
