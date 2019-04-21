package com.edu.orchard.auth;

import java.util.Arrays;
import java.util.Optional;
import java.util.Set;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;

import com.edu.orchard.bots.CommandBotHandler;

@Aspect
public class AuthorizationAspect {

	@Value("${telegram.bots.users.allowed}")
	private Set<Long> allowedUsers;

	@Autowired
	private CommandBotHandler commandBotHandler;

	@Around("com.edu.orchard.auth.config.JoinPointConfig.botAuthUserMessagingExecution()")
	public void before(ProceedingJoinPoint joinPoint) throws Throwable {
		Object[] args = joinPoint.getArgs();

		Optional<Long> chatId = Arrays.stream(args).filter(ar -> ar instanceof Chat).map(ar -> ((Chat) ar).getId())
				.findFirst();

		if (allowedUsers.contains(chatId.get())) {
			joinPoint.proceed();
		} else {
			commandBotHandler.execute(new SendMessage().setChatId(chatId.get().toString())
					.setText("You are not allowed to talk to me, sorry"));
		}
	}
}
