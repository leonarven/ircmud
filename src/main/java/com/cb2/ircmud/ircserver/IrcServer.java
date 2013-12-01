package com.cb2.ircmud.ircserver;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.io.IOException;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import com.cb2.ircmud.domain.Player;
import com.cb2.ircmud.ircserver.services.AuthService;
import com.cb2.ircmud.ircserver.services.ChannelService;
import com.github.rlespinasse.slf4j.spring.AutowiredLogger;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class IrcServer {

	private ServerSocket serverSocket;
	@Value("${config.server.name}")
	public String serverName;
	@Value("${config.server.port}")
	public int    serverPort;
	public final String VERSION = "0.02";
		
	@AutowiredLogger
	Logger logger;
	@Autowired
	AuthService authService;
	@Autowired
	ChannelService channels;
	@Autowired
	Environment env;

	@PostConstruct
	public void init() throws IOException {
		
		logger.info("Initializing IrcServer({}: {})",serverName, serverPort);

		logger.info("Initializing ServerSocket");
		serverSocket = new ServerSocket(serverPort);

		// Init channel Config.WorldChannel
		Channel worldChannel = new Channel( env.getProperty("config.server.WorldChannel"));
		channels.add(worldChannel);
		
        
       //TODO: No hard-coded admin accounts :P
  		authService.addAccount("admin", "password", Player.ACCESS_ADMIN);
	}
	
	
	public void run() {
		logger.info("Starting server loop");

		while (true) {
			try {
				Socket	   socket = serverSocket.accept();
				Connection connection = new Connection(socket);
				Thread	   thread = new Thread(connection);
				thread.start();
			} catch(IOException e) {
				logger.error("IOException at Server.run: {}", e.getMessage());
			}
		}
	}
	
	public boolean close() throws IOException {

		serverSocket.close();
	
		return false;
	}
	
	@PreDestroy
	protected void destroy(){
		try {
			close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}