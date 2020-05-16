<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">

<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <meta http-equiv="X-UA-Compatible" content="ie=edge">
  <title>Profile | Instagram</title>
  <link href="https://maxcdn.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css" rel="stylesheet">
  <link rel="shortcut icon" href="/images/favicon.ico">
  <link rel="stylesheet" href="/css/styles.css">
</head>

<body>
  <%@ include file="../include/nav.jsp" %>
  <main id="change">
	    <div class="edit-profile__container u-default-box">
      
      <header class="edit-profile__header">
        <div class="fucker-container">
          <img src="/upload/${user.profileImage}" />
        </div>
        <!-- master comments -->
        <h1 class="edit-profile__username">${user.username}</h1>
      </header>

												<!-- method="PUT" 왜안대는지 모르겠음 -->
       <form:form action="/user/passProc" method="POST" class="edit-profile__form">
        <div class="edit-profile__row">
          <label class="edit-profile__label" for="pass">이전 비밀번호</label>
          <input id="pass" name="pass" type="password">
        </div>
        <div class="edit-profile__row">
          <label class="edit-profile__label" for="npass1">새 비밀번호</label>
          <input id="npass1" name="npass1" type="password">
        </div>
        <div class="edit-profile__row">
          <label class="edit-profile__label" for="npass1">새 비밀번호 확인</label>
          <input id="npass2" name="npass2" type="password">
        </div>
        <div class="edit-profile__row">
          <span></span>
          <input style="background-color:#3897F0;" id="submit" type="submit" value="비밀번호 변경">
          <span></span>
          <h1>비밀번호를 바꾸면 로그아웃 됩니다. 다시 로그인해주세요.</h1>
        </div>
      </form:form>

    </div>
  </main>

  <%@ include file="../include/footer.jsp" %>

</body>
</html>