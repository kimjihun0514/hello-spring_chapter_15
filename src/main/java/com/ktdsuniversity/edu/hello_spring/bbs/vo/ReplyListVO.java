package com.ktdsuniversity.edu.hello_spring.bbs.vo;

import java.util.List;

public class ReplyListVO {
	
	private int replyCnt;

	private List<ReplyVO> replyList;

	public int getReplyCnt() {
		return replyCnt;
	}

	public void setReplyCnt(int replyCnt) {
		this.replyCnt = replyCnt;
	}

	public List<ReplyVO> getReplyList() {
		return replyList;
	}

	public void setReplyList(List<ReplyVO> replyList) {
		this.replyList = replyList;
	}
	
	
	
}
