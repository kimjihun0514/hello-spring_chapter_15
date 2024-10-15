<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Login</title>
<link rel="stylesheet" type="text/css" href="/css/login.css" />
<script type="text/javascript" src="/js/jquery-3.7.1.min.js"></script>
<script type="text/javascript" src="/js/member/memberlogin.js"></script>
</head>
<body>

	<h1>로그인</h1>
	<form:form modelAttribute="loginMemberVO" method="post" action="/member/login">

		<div class="grid">
			<label for="email">이메일</label>
			<div>
				<input id="email" type="email" name="email" value="${loginMemberVO.email}" />
				<input type="hidden" name="nextUrl" />
				<form:errors path="email" element="div" cssClass="error" />
				<c:if test="${not empty message}"> 
					<div class="error">${message}</div>
				</c:if>
			</div>
			<label for="password">비밀번호</label>
			<div>
				<input id="password" type="password" name="password" />
				<form:errors path="password" element="div" cssClass="error" />
			</div>
			
			<div class="btn-group">
				<div class="right-align">
	            	<a class="regist" href="/member/regist">회원가입</a>
	          	</div>
				<div class="right-align">
					<input id="btn-regist" type="submit" value="로그인" />
				</div>
			</div>
		</div>
	</form:form>

</body>
</html>