package com.ktdsuniversity.edu.hello_spring.bbs.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ktdsuniversity.edu.hello_spring.bbs.dao.ReplyDao;
import com.ktdsuniversity.edu.hello_spring.bbs.service.ReplyService;
import com.ktdsuniversity.edu.hello_spring.bbs.vo.ModifyReplyVO;
import com.ktdsuniversity.edu.hello_spring.bbs.vo.ReplyListVO;
import com.ktdsuniversity.edu.hello_spring.bbs.vo.ReplyVO;
import com.ktdsuniversity.edu.hello_spring.bbs.vo.WriteReplyVO;
import com.ktdsuniversity.edu.hello_spring.common.exceptions.PageNotFoundException;

@Service
public class ReplyServiceImpl implements ReplyService{

	@Autowired
	private ReplyDao replyDao; 
	
	@Override
	public ReplyListVO getAllReplies(int boardId) {
		ReplyListVO replyListVO = new ReplyListVO();
		List<ReplyVO> replyList = this.replyDao.selectAllReply(boardId);
		replyListVO.setReplyList(replyList);
		replyListVO.setReplyCnt(replyList.size());
		
		return replyListVO;
	}

	@Transactional
	@Override
	public boolean createNewReply(WriteReplyVO writeReplyVO) {
		return this.replyDao.insertNewReply(writeReplyVO) > 0;
	}

	@Transactional
	@Override
	public boolean deleteOneReply(int replyId, String email) {
		ReplyVO replyVO = this.replyDao.selectOneReply(replyId);
		if (!replyVO.getEmail().equals(email)) {
			throw new PageNotFoundException("잘못된 접근입니다.");
		}
		return this.replyDao.deleteOneReply(replyId) > 0;
	}

	@Transactional
	@Override
	public boolean modifyOneReply(ModifyReplyVO modifyReplyVO) {
		ReplyVO replyVO = this.replyDao.selectOneReply(modifyReplyVO.getReplyId());
		if (!replyVO.getEmail().equals(modifyReplyVO.getEmail())) {
			throw new PageNotFoundException("잘못된 접근입니다.");
		}
		return this.replyDao.updateOneReply(modifyReplyVO) > 0;
	}

	@Transactional
	@Override
	public boolean recommendOneReply(int replyId, String email) {
		ReplyVO replyVO = this.replyDao.selectOneReply(replyId);
		if (replyVO.getEmail().equals(email)) {
			throw new PageNotFoundException("자신의 댓글은 추천할 수 없습니다.");
		}
		return this.replyDao.updateRecommendCnt(replyId) > 0;
	}

	
	
}
