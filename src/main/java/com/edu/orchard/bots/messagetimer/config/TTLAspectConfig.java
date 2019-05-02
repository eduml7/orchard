package com.edu.orchard.bots.messagetimer.config;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import org.aspectj.lang.Aspects;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.edu.orchard.bots.messagetimer.TtlAspect;

@Configuration
public class TTLAspectConfig {

	/** 
     * This is needed to get hold of the instance of the aspect which is created outside of the spring container, 
     * and make it available for autowiring. 
     */
    @Bean
    TtlAspect ttlAspect() {
        return Aspects.aspectOf(TtlAspect.class);
    }

    @Bean
    ScheduledExecutorService ttlExecutorService (){
    	return Executors.newScheduledThreadPool(5);
    }
}
