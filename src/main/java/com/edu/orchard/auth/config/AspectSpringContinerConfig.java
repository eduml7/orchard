package com.edu.orchard.auth.config;

import org.aspectj.lang.Aspects;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.edu.orchard.auth.AuthorizationAspect;

@Configuration
public class AspectSpringContinerConfig {

	/** 
     * This is needed to get hold of the instance of the aspect which is created outside of the spring container, 
     * and make it available for autowiring. 
     */
    @Bean
    AuthorizationAspect authorizationAspect()
    {
        final AuthorizationAspect aspect = Aspects.aspectOf(AuthorizationAspect.class);
        return aspect;
    }

}
