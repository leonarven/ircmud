package com.cb2.ircmud.ircserver.services;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cb2.ircmud.ircserver.Connection;
import com.cb2.ircmud.ircserver.ConnectionHandler;
import com.cb2.ircmud.ircserver.IrcServer;

@Component
public class ConnectionWriteService implements Runnable, ConnectionHandler {
	private Thread workerThread = null;
	@Autowired
	IrcServer server;
	
	@PostConstruct
	protected void init() {
		workerThread = new Thread(this);
		workerThread.start();
	}
	
	@Override
	public void handleConnection(Connection con) {
		con.asynchronousHandleOutQueue();
	}

	@Override
	public void run() {
		//TODO: Maybe timer?
		while (true) {
			server.forEachConnection(this);
		}
	}

}
