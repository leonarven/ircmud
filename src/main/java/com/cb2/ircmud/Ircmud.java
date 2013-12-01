package com.cb2.ircmud;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cb2.ircmud.ircserver.IrcServer;
import com.github.rlespinasse.slf4j.spring.AutowiredLogger;

@Component
public class Ircmud {

	@Autowired
	IrcServer server;
	@AutowiredLogger
	Logger logger;
	
	@PostConstruct
	public void init(){
	}
	
	public void main(String[] args) {
        logger.debug("main()");

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
