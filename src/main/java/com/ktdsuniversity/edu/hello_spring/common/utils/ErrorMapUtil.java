package com.ktdsuniversity.edu.hello_spring.common.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.validation.BindingResult;

public final class ErrorMapUtil {

	public static Map<String, Object> getErrorMap(BindingResult bindingResult) {
		Map<String, Object> errorMap = new HashMap<>();
		
		bindingResult.getFieldErrors().forEach( error -> {
			
			String fileName = error.getField(); // 에러가 발생 한 위치(파일)
			String errorMessage = error.getDefaultMessage(); // 에러 발생 내용
			
			if (!errorMap.containsKey(fileName)) {
				errorMap.put(fileName, new ArrayList<>());
			}
			
			List<String> errorList = (List<String>)errorMap.get(fileName);
			errorList.add(errorMessage);
		});
		
		return errorMap;
	}
	
}
