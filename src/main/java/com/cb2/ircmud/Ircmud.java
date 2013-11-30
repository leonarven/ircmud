package com.cb2.ircmud;

import java.io.IOException;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cb2.ircmud.ircserver.IrcServer;
import com.github.rlespinasse.slf4j.spring.AutowiredLogger;

@Component
public class Ircmud {
	
	public String globalServerName;
	public int    globalServerPort;
	
	
	@Autowired
	IrcServer server;
	@AutowiredLogger
	Logger logger;
	@Autowired
	Config config;
	
	@PostConstruct
	public void init(){
		globalServerName = config.ServerName;
		globalServerPort = config.ServerPort;
	}
	
	public void main(String[] args) {
        logger.debug("Ircmud::main()");

		
		switch(args.length) {
			case 0: break;
			case 1: 
				globalServerName = args[0];
				break;
			case 2:
				globalServerName = args[0];
				globalServerPort = Integer.parseInt(args[1]);
				break;
			default:
				System.out.println("Usage: Ircmud [servername [port]]");
		}

		try {
			logger.info("Creating Server");
			server.init(globalServerName, globalServerPort);

			server.run();
			
		} catch(IOException e) {
			
			System.err.println("ERROR: IOException at IrcMud.main:" + e.getMessage());
			e.printStackTrace();
			
		} finally {
			try {
				server.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		
	}

}
