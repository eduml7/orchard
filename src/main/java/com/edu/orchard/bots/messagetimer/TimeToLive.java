package com.edu.orchard.bots.messagetimer;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

/**
 * This annotation gives a TTL to a published Telegram message
 * 
 * @author edu
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface TimeToLive {

	/**
	 * The time from now to delay execution
	 * @return
	 */
	long ttl() default 1;

	/**
	 * Time unit for ttl field
	 * @return
	 */
	TimeUnit timeUnit() default TimeUnit.MINUTES;
}
