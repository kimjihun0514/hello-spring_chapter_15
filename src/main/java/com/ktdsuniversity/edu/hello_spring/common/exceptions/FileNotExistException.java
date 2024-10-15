package com.ktdsuniversity.edu.hello_spring.common.exceptions;

public class FileNotExistException extends RuntimeException {

	private static final long serialVersionUID = -3321624745595131158L;

	public FileNotExistException (String message) {
		super(message);
	}
	
}
