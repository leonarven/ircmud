package com.cb2.ircmud;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cb2.ircmud.ircserver.IrcServer;
import com.cb2.ircmud.tests.TestDataGenerationService;
import com.github.rlespinasse.slf4j.spring.AutowiredLogger;

@Component
public class Ircmud {

	@Autowired
	IrcServer server;
	@Autowired
	TestDataGenerationService testDataGenerationService;
	@AutowiredLogger
	Logger logger;
	
	@PostConstruct
	public void init(){
	}
	
	public void main(String[] args) {
        logger.debug("main()");
        
        logger.debug("Generating the test world");
        testDataGenerationService.generateTestWorld();

		try {
			logger.info("Running Server");
			server.run();
		} finally {
			try {
				server.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		
	}

}
