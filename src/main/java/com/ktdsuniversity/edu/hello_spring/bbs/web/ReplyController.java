package com.ktdsuniversity.edu.hello_spring.bbs.web;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttribute;

import com.ktdsuniversity.edu.hello_spring.bbs.service.ReplyService;
import com.ktdsuniversity.edu.hello_spring.bbs.vo.ModifyReplyVO;
import com.ktdsuniversity.edu.hello_spring.bbs.vo.ReplyListVO;
import com.ktdsuniversity.edu.hello_spring.bbs.vo.WriteReplyVO;
import com.ktdsuniversity.edu.hello_spring.common.utils.ErrorMapUtil;
import com.ktdsuniversity.edu.hello_spring.member.vo.MemberVO;

import jakarta.validation.Valid;

@RestController
public class ReplyController {

	@Autowired
	private ReplyService replyService;
	
	@GetMapping("/board/reply/{boardId}")
	public Map<String, Object> getAllReplies(@PathVariable int boardId) {
		ReplyListVO replyListVO = this.replyService.getAllReplies(boardId);
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("count", replyListVO.getReplyCnt());
		resultMap.put("replies", replyListVO.getReplyList());
		
		return resultMap;
	}
	
	@PostMapping("/board/reply/{boardId}")
	public Map<String, Object> doCreateNewReply(@PathVariable int boardId
												, @Valid WriteReplyVO writeReplyVO
												, BindingResult bindingResult
												, @SessionAttribute("_LOGIN_USER_") MemberVO loginMemberVO) {
		if (bindingResult.hasErrors()) {
			return ErrorMapUtil.getErrorMap(bindingResult);
		}
		
		writeReplyVO.setBoardId(boardId);
		writeReplyVO.setEmail(loginMemberVO.getEmail());
		boolean isCreated = this.replyService.createNewReply(writeReplyVO);
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("result", isCreated);
		
		return resultMap;
	}
	
	@GetMapping("/board/reply/delete/{replyId}")
	public Map<String, Object> doDeleteReply(@PathVariable int replyId
											, @SessionAttribute("_LOGIN_USER_") MemberVO loginMemberVO) {
		boolean isDeleted = this.replyService.deleteOneReply(replyId, loginMemberVO.getEmail());
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("result", isDeleted);
		return resultMap;
	}
	
	@PostMapping("/board/reply/modify/{replyId}")
	public Map<String, Object> doModifyReply(@PathVariable int replyId
											, @ModelAttribute ModifyReplyVO modifyReplyVO
											, @SessionAttribute("_LOGIN_USER_") MemberVO loginMemberVO) {
		modifyReplyVO.setReplyId(replyId);
		modifyReplyVO.setEmail(loginMemberVO.getEmail());
		boolean isModified = this.replyService.modifyOneReply(modifyReplyVO);
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("result", isModified);
		
		return resultMap;
	}

	@GetMapping("/board/reply/recommend/{replyId}")
	public Map<String, Object> doRecommendReply(@PathVariable int replyId
														, @SessionAttribute("_LOGIN_USER_") MemberVO loginMemberVO) {
		boolean isIncreased = this.replyService.recommendOneReply(replyId, loginMemberVO.getEmail());
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("result", isIncreased);
		
		return resultMap;
	}
	
}
