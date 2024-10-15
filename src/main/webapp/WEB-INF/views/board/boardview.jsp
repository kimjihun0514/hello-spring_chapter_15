<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8" />
<title>${boardVO.id}번 게시글 조회</title>
<link rel="stylesheet" type="text/css" href="/css/common.css" />
<script type="text/javascript" src="/js/jquery-3.7.1.min.js"></script>
<script type="text/javascript" src="/js/board/reply/reply.js"></script>
</head>
<body>
	<jsp:include page="../member/membermenu.jsp"></jsp:include>
	<h1>${boardVO.id}번게시글 조회</h1>
	<div class="grid grid-view-board" data-board-id="${boardVO.id}">
		<label for="subject">제목</label>
		<div>${boardVO.subject}</div>
		<label for="email">작성자</label>
		<div>${boardVO.memberVO.name}(${boardVO.memberVO.email})</div>
		<label for="viewCnt">조회수</label>
		<div>${boardVO.viewCnt}</div>
		<label for="originFileName">첨부파일</label>
		<div>
			<a href="/board/file/download/${boardVO.id}">
				${boardVO.originFileName} </a>
		</div>
		<label for="crtDt">등록일</label>
		<div>${boardVO.crtDt}</div>
		<label for="mdfyDt">수정일</label>
		<div>${boardVO.mdfyDt}</div>
		<label for="content">내용</label>
		<div>${boardVO.content}</div>

		<div class="btn-group">
			<div class="right-align">
				<a href="/board/list">목록으로</a>
				<c:if test="${sessionScope._LOGIN_USER_.email eq boardVO.email}">
					<a href="/board/modify/${boardVO.id}">수정</a>
					<a href="/board/delete/${boardVO.id}">삭제</a>
				</c:if>
			</div>
		</div>


		<div class="replies">
			<div class="reply-items"></div>
			<div class="write-reply">
				<textarea class="txt-reply"></textarea>
				<button id="btn-save-reply">등록</button>
				<button id="btn-cancel-reply">취소</button>
			</div>
		</div>

	</div>
	<template id="reply-template">
		<div class="reply" 
			data-reply-id="{replyId}"
			style="margin-left: {marginLeft}rem">
			<div class="author">
				{authorName} ({authorEmail})
			</div>
			<div class="recommand-count">
				추천 수 : {recommendCount}
			</div>
			<div class="date-time">
				<span class="crtdt">등록 : {crtDt}</span> 
				<!-- 등록시간과 수정시간이 다를 때 에만 수정시간 노출 -->
				<span class="mdfydt">수정	: {mdfyDt}</span>
			</div>
			<pre class="content">{content}</pre>
			<!-- 로그인 한 사용자가 작정한 댓글인 경우에만 노출 -->
			<div class="my-reply">
				<span class="modify-reply">수정</span> 
				<span class="delete-reply">삭제</span>
				<span class="re-reply">답변하기</span>
			</div>
			<!-- 다른 사람이 작성한 댓글인 경우에만 노출 -->
			<div class="other-reply">
				<span class="recommend-reply">추천하기</span> 
				<span class="re-reply">답변하기</span>
			</div>
		</div>
	</template>
</body>
</html>
