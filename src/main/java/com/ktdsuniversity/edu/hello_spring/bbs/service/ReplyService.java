package com.ktdsuniversity.edu.hello_spring.bbs.service;

import com.ktdsuniversity.edu.hello_spring.bbs.vo.ModifyReplyVO;
import com.ktdsuniversity.edu.hello_spring.bbs.vo.ReplyListVO;
import com.ktdsuniversity.edu.hello_spring.bbs.vo.WriteReplyVO;

public interface ReplyService {

	public ReplyListVO getAllReplies(int boardId);
	
	public boolean createNewReply(WriteReplyVO writeReplyVO);
	
	public boolean deleteOneReply(int replyId, String email);
	
	public boolean modifyOneReply(ModifyReplyVO modifyReplyVO);
	
	public boolean recommendOneReply(int replyId, String email);
	
}
