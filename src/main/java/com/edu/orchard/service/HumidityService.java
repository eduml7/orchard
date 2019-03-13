package com.edu.orchard.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class HumidityService {

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	public void processHumidityInfo(Integer level) {
		if (level > 500) {
			log.info("No water needed");
		} else {
			log.info("Please, water me");
		}
	}
}
