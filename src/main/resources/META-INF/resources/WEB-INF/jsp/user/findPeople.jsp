<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <meta http-equiv="X-UA-Compatible" content="ie=edge">
  <title>instagram</title>
  <link href="https://maxcdn.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css"
    rel="stylesheet">
  <link rel="shortcut icon" href="/images/favicon.ico">
  <link rel="stylesheet" href="/css/styles.css">
</head>
<body>

<%@ include file="../include/nav.jsp" %>

    <main id="explore">
  
    <ul class="explore__users u-default-box">
    
      <c:forEach var="user" items="${userList}" varStatus="status">
	      <li class="explore__user">
	        
	        <div class="explore__content">
	          <a href="/user/${user.id}"><img src="/upload/${user.profileImage}" onerror="this.onerror=null; this.src='/images/avatar.jpg'"/></a>
	          <div class="explore__info">
	            <a href="/user/${user.id}"><span class="explore__username">${user.username}</span></a>
	            <a href="/user/${user.id}"><span>${user.name}</span></a>
	          </div>
	        </div>
	        
	        <div id="follow_item_${status.count}">
		        <c:if test="${principal.user.id ne follow.toUser.id}">

		        </c:if>
	        </div>
	        
	      </li>
      </c:forEach>
      
    </ul>
  </main>
  
  <%@ include file="../include/footer.jsp" %>
  <script src="/js/follow.js"></script>
</body>
</html>