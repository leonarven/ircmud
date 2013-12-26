package com.cb2.ircmud.ircserver.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cb2.ircmud.ircserver.Connection;
import com.cb2.ircmud.ircserver.ConnectionHandler;
import com.cb2.ircmud.ircserver.IrcServer;

@Component
public class ConnectionReadService {
	@Autowired
	IrcServer server;
	
	public void addConnection(Connection con) {
		con.initAsynchronousRead();
	}

}
