package com.edu.orchard.auth.config;

import org.aspectj.lang.annotation.Pointcut;

public class JoinPointConfig {

	@Pointcut("@annotation(com.edu.orchard.auth.Securized)")
	public void botAuthUserMessagingExecution() {
	}
}
