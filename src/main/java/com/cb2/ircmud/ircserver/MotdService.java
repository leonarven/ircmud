package com.cb2.ircmud.ircserver;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public abstract class MotdService {

	@Autowired 
	IrcServer server;

	abstract ArrayList<IrcReply> getMotd(IrcUser user);
	
	public void sendMotd(IrcUser user) {
		ArrayList<IrcReply> replies = getMotd(user);
		for(IrcReply reply : replies)
			user.sendReply(reply);
	}
}
