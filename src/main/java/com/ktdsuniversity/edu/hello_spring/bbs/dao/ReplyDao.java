package com.ktdsuniversity.edu.hello_spring.bbs.dao;

import java.util.List;

import com.ktdsuniversity.edu.hello_spring.bbs.vo.ModifyReplyVO;
import com.ktdsuniversity.edu.hello_spring.bbs.vo.ReplyVO;
import com.ktdsuniversity.edu.hello_spring.bbs.vo.WriteReplyVO;

public interface ReplyDao {

	public String NAMESPACE = "com.ktdsuniversity.edu.hello_spring.bbs.dao.ReplyDao";
	
	public List<ReplyVO> selectAllReply(int boardId);
	
	public ReplyVO selectOneReply(int replyId);
	
	public int insertNewReply(WriteReplyVO writeReplyVO);
	
	public int deleteOneReply(int replyId);
	
	public int updateOneReply(ModifyReplyVO modifyReplyVO);
	
	public int updateRecommendCnt(int replyId);
	
}
