package com.ktdsuniversity.edu.hello_spring.common.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect // AOP 컴포넌트로 명시
@Component
public class TimingAspect {

	private static final Logger logger = LoggerFactory.getLogger(TimingAspect.class);

	/**
	 * public 모든 반환타입 com.ktdsuniversity.edu.hello_spring 밑에 있는 모든 패키지 중에서 service
	 * 패키지 내부의 모든 패키지에서 클래스의 이름이 ServiceImpl로 끝나는 모든 클래스의 모든 메소드를 대상으로 함
	 */
	@Pointcut("execution(public * com.ktdsuniversity.edu.hello_spring..service..*ServiceImpl.*(..))")
	public void aroundTarget() {
		// AOP가 개입할 클래스 및 메소드를 명시만 해주고 기능은 하지 않음
	}

	/**
	 * @Pointcut(aroundTarget())으로 지정한 클래스.메소드가 실행될 때 공통으로 실행시킬 코드를 명시
	 * 
	 * @param pjp 원래 코드(클래스와 메소드의 정보를 포함)
	 * @return 원래 코드가 반환시킨 데이터 (컨트롤러에게 반환됨)
	 * @throws Throwable
	 */
	@Around("aroundTarget()")
	public Object timingAdvice(ProceedingJoinPoint pjp) throws Throwable {

		Object result = null;

		// 현재 시간을 구함
		long currentTime = System.currentTimeMillis(); // 현재 시간을 밀리세컨즈로 구해 Long 타입으로 반환

		// 원래 코드를 동작
		try {
			result = pjp.proceed();
		} catch (Throwable e) {
			throw e;
		} finally {
			// 종료 시간을 구함
			long endTime = System.currentTimeMillis();
			// 원래 코드가 실행되고 나서부터 종료 될 때 까지의 시간을 구함
			long proceedTime = endTime - currentTime;

			// 원래 실행되어야 할(원래 실행 됐던) 클래스의 이름과 메소드의 이름을 추출
			// 패키지를 포함한 클래스의 이름 추출
			String className = pjp.getTarget().getClass().getName();
			// 메소드의 이름 추출
			String methodName = pjp.getSignature().getName();
			if (logger.isInfoEnabled()) {
				logger.info("{}.{} 걸린시간: {}ms", className, methodName, proceedTime);
			}
		}
		return result;
	}

}
