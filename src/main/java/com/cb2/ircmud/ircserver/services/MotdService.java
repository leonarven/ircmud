package com.cb2.ircmud.ircserver.services;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cb2.ircmud.ircserver.IrcReply;
import com.cb2.ircmud.ircserver.IrcServer;
import com.cb2.ircmud.ircserver.IrcUser;

@Service
public abstract class MotdService {

	@Autowired 
	IrcServer server;

	public abstract ArrayList<IrcReply> getMotd(IrcUser user);
	
	public void sendMotd(IrcUser user) {
		ArrayList<IrcReply> replies = getMotd(user);
		for(IrcReply reply : replies)
			user.sendReply(reply);
	}
}
