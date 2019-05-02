package com.edu.orchard.bots.messagetimer;

import java.util.Set;
import java.util.concurrent.ScheduledExecutorService;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import com.edu.orchard.bots.CommandBotHandler;

@Aspect
public class TtlAspect {

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	@Value("${telegram.bots.users.allowed}")
	private Set<Long> allowedUsers;

	@Autowired
	private CommandBotHandler commandBotHandler;

	@Autowired
	private ScheduledExecutorService ttlExecutorService;

	@AfterReturning(value = "@annotation(com.edu.orchard.bots.messagetimer.TimeToLive)", returning = "messageSet")
	public void deleteMessage(JoinPoint joinPoint, Set<Message> messageSet) throws Throwable {
		log.info("Deleting message...");
		TimeToLive ttlAnnotation = ((MethodSignature)joinPoint.getSignature()).getMethod().getAnnotation(TimeToLive.class);
		messageSet.parallelStream().forEach(message -> {
			ttlExecutorService.schedule(new Runnable() {
				public void run() {
					try {
						log.info("Executing scheduled deletion for message with id {}", message.getMessageId());
						DeleteMessage deleteMessage = new DeleteMessage();
						deleteMessage.setChatId(message.getChatId().toString());
						deleteMessage.setMessageId(message.getMessageId());
						commandBotHandler.execute(deleteMessage);
					} catch (TelegramApiException e) {
						log.error("Error trying to send the deletion message for messageId {}", message.getMessageId(), e);
					}
				}
			}, ttlAnnotation.ttl(), ttlAnnotation.timeUnit());
		});
		ttlExecutorService.shutdown();
	}
}
