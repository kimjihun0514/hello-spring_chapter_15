$().ready(function() {

	var email = $(".member-menu").data("email");
	var boardId = $(".grid-view-board").data("board-id");


	console.log("로그인 이메일 : " + email);
	console.log("조회중인 게시글 번호 : " + boardId);

	loadReplies(boardId);

	$("#btn-save-reply").on("click", function() {
		createReply(boardId);
	});
	
	$("#btn-cancel-reply").on("click", function() {
		$(".txt-reply").val("");
		$(".txt-reply").removeData("modify-reply-id");
		$(".txt-reply").removeData("parent-reply-id");
	});
	
});

function loadReplies(boardId) {

	var email = $(".member-menu").data("email");
	
	// 기존의 댓글은 모두 화면에서 제거
	$(".reply-items").html("");
	
	$.get(`/board/reply/${boardId}`, {}, function(repliesResult) {
		
		var count = repliesResult.count; // 댓글 수
		var replies = repliesResult.replies; // [{}...{}...{}] 댓글 정보 배열
		
		for (var i in replies) {
			var reply = replies[i];
			
			var template = $("#reply-template").html();
			template = template.replace("{replyId}", reply.replyId)
							   .replace("{marginLeft}", (reply.level - 1) * 1)
							   .replace("{authorName}", reply.memberVO.name)
							   .replace("{authorEmail}", reply.email)
							   .replace("{recommendCount}", reply.recommendCnt)
							   .replace("{crtDt}", reply.crtDt)
							   .replace("{mdfyDt}", reply.mdfyDt)
							   .replace("{content}", reply.content);
			
			var replyDom = $(template);
			if (reply.crtDt === reply.mdfyDt) {
				replyDom.find(".mdfydt").remove();
			}
			
			if (email === reply.email) {
				replyDom.find(".other-reply").remove();
				replyDom.find(".my-reply")
						.find(".modify-reply")
						.on("click", function () {
							modifyReply( $(this) );
						});
				replyDom.find(".my-reply")
						.find(".delete-reply")
						.on("click", function () {
							deleteReply( $(this) );
						});
				replyDom.find(".my-reply")
						.find(".re-reply")
						.on("click", function () {
							reReply( $(this) );
						});
			}
			else {
				replyDom.find(".my-reply").remove();
				replyDom.find(".other-reply")
						.find(".recommend-reply")
						.on("click", function () {
							recommendReply($(this));
						});
				replyDom.find(".other-reply")
						.find(".re-reply")
						.on("click", function () {
							reReply( $(this) );
						});
			}
							   
			$(".reply-items").append(replyDom);
		}
		
	});

}

function createReply(boardId) {

	$(".write-reply").find("div.error").remove();
	
	// 댓글 등록 Url
	var url = `/board/reply/${boardId}`;
	
	var modifyReplyId = $(".txt-reply").data("modify-reply-id");
	if (modifyReplyId) {
		// 수정을 위한 클릭일 경우(modifyReplyId가 있을 경우) url 재 할당
		url = `/board/reply/modify/${modifyReplyId}`;
		
	}
	
	var params = { content: $(".txt-reply").val() };
	
	var parentReplyId = $(".txt-reply").data("parent-reply-id");
	if (parentReplyId) {
		// 대댓글일 경우 parentReplyId를 할당
		params.parentReplyId = parentReplyId;
	}
	
	$.post(url,
		params
		, function(createResult) {
			/*
			 * { "result": true }
			 */
			console.log(createResult);

			// result의 값이 true라면 댓글의 목록을 갱신
			if (createResult.result) {
				$(".txt-reply").val("");
				// 수정일 경우에는 modify-reply-id를 삭제
				$(".txt-reply").removeData("modify-reply-id");
				// 대댓글 달기 완료 후 data 삭제
				$(".txt-reply").removeData("parent-reply-id");
				loadReplies(boardId);
			}
			else {
				// 에러가 존재하는 케이스
				// 키가 무엇인지 모른다)

				// key 변수에 createResult에 있는 키의 값이 반복 할당한다.
				var errorDiv = $("<div class='error'></div>");
				var errorUl = $("<ul></ul>");
				errorDiv.append(errorUl);

				// JSON (Object Literal)을 반복
				for (var key in createResult) {
					// createResult[key]를 반복.
					for (var i in createResult[key]) {
						var errorLi = $("<li></li>");
						errorLi.text(createResult[key][i]);
						errorUl.append(errorLi);
					}
				}
				$(".txt-reply").before(errorDiv);
			}
		}
	);

}

function modifyReply(clickedModifyButton) {
	var replyId = clickedModifyButton.closest(".reply")
									 .data("reply-id");
	
	var replyContent = clickedModifyButton.closest(".reply")
									      .find(".content")
									      .text();
	$(".txt-reply").val(replyContent);
	$(".txt-reply").removeData("parent-reply-id");
	// <textarea class="txt-reply" data-modify-reply-id="">
	$(".txt-reply").data("modify-reply-id", replyId);
	$(".txt-reply").focus();
}

function deleteReply(clickedDeleteButton) {
	var replyId = clickedDeleteButton.closest(".reply")
									 .data("reply-id");
	
	if (confirm("정말 삭제할까요?")) {
		$.get(`/board/reply/delete/${replyId}`
			, {}
			, function(deleteResult) {
				if (deleteResult.result) {
					var boardId = $(".grid-view-board").data("board-id");
					loadReplies(boardId);
				}
			});
	}
	 
}

function recommendReply(clickedRecommendButton) {
	var replyId = clickedRecommendButton.closest(".reply")
										 .data("reply-id");
	$.get(`/board/reply/recommend/${replyId}`
		, {}
		, function(recommendResult) {
			if (recommendResult.result) {
				var boardId = $(".grid-view-board").data("board-id");
				loadReplies(boardId);
			}
			else if (recommendResult.error) {
				alert(recommendResult.error);
			}
		});
	
}

function reReply(clickedReReplyButton) {
	var replyId = clickedReReplyButton.closest(".reply")
									  .data("reply-id");
	
	$(".txt-reply").data("parent-reply-id", replyId);
	$(".txt-reply").removeData("modify-reply-id");
	$(".txt-reply").val("");
	$(".txt-reply").focus();
	
}
