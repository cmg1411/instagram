<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/core" prefix = "c" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<sec:authorize access="isAuthenticated()">
    <sec:authentication property="principal" var="principal" />
</sec:authorize>
   
    
<nav class="navigation">
  <a href="/">
    <img src="/images/logo.png" />
  </a>
  <input type="text" placeholder="Search">
  <div class="navigation__links">
  <div class="profile__overlay">
    <i class="fa fa-times"></i>
    <div class="profile__overlay-container">
      <a href="#" class="profile__overlay-link">Change password</a>
      <a href="#" class="profile__overlay-link" id="logout">Log Out</a>
      <a href="#" class="profile__overlay-link" id="cancel">Cancel</a>
    </div>
  </div>
    <a href="/image/explore" class="navigation__link">
        <i class="fa fa-compass"></i>
    </a>
    <a href="#" class="navigation__link like_popup">
        <i class="fa fa-heart-o"></i>
    </a>
    <a href="/user/${principal.user.id}" class="navigation__link">
        <i class="fa fa-user-o"></i>
    </a>
  </div>
</nav>

<!-- Modal 시작 -->
<div id="modal">
  <div id="like_popup_list">
	  <div class="like_popup_items">

	  </div>
	  <div class="like_popup_close">
	      <button type="button">닫기</button>
	  </div>
  </div>
</div>


<!-- Modal 끝 --> 