package com.ktdsuniversity.edu.hello_spring.common.exceptions;

import com.ktdsuniversity.edu.hello_spring.member.vo.RegistMemberVO;

public class AlreadyUseException extends RuntimeException {
	
	private static final long serialVersionUID = 356545585441520026L;
	
	private RegistMemberVO registMemberVO;
	
	public AlreadyUseException (RegistMemberVO registMemberVO, String message) {
		super(message);
		this.registMemberVO = registMemberVO;
	}

	public RegistMemberVO getRegistMemberVO() {
		return registMemberVO;
	}
	
}
