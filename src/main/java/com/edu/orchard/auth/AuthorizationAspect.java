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
import org.telegram.telegrambots.meta.api.objects.Update;

import com.edu.orchard.bots.CommandBotHandler;

@Aspect
public class AuthorizationAspect {

	@Value("${telegram.bots.users.allowed}")
	private Set<Long> allowedUsers;

	@Autowired
	private CommandBotHandler commandBotHandler;

	@Around("com.edu.orchard.auth.config.JoinPointConfig.botAuthUserMessagingExecution()")
	public void before(ProceedingJoinPoint joinPoint) throws Throwable {

		Optional<Long> chatId = getChatId(joinPoint.getArgs());

		if (chatId.isPresent() && allowedUsers.contains(chatId.get())) {
			joinPoint.proceed();
		} else {
			commandBotHandler.execute(
					new SendMessage().setChatId(chatId.get().toString())
					.setText("You are not allowed to talk to me, sorry"));
		}
	}

	private <T> Optional<Long> getChatId(T[] args) {
		return Arrays.stream(args)
				.filter(ar -> ar instanceof Chat || ar instanceof Update)
				.map(ar -> {
					if (ar instanceof Chat) {
						return Optional.of(((Chat) ar).getId());
					} else {
						return Optional.of(((Update) ar).getMessage().getChat().getId());
					}
				})
				.findFirst()
				.orElse(Optional.empty());
	}
}
