package com.edu.orchard.auth;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class AuthorizationAspect {

	 @Before("com.edu.orchard.auth.config.JoinPointConfig.botAuthUserMessagingExecution()")
	//@Before("@annotation(com.edu.orchard.auth.Securized)")
	public void before(JoinPoint joinPoint) throws Throwable {
		joinPoint.getTarget();
		System.out.println("asp-ect");
	}
}
