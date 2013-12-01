package com.cb2.ircmud.communication;

import org.springframework.stereotype.Service;

import com.cb2.ircmud.domain.Player;
import com.cb2.ircmud.ircserver.Channel;
import com.cb2.ircmud.ircserver.IrcReply;

@Service
public class CommunicationService {

	public void handle(IrcReply msg) {
		
		
	}
	
	public Player getPlayer(IrcReply privMsg) {
		
		return null;
	}
	
	public Channel getChannel(IrcReply privMsg) {
		
		return null;
	}
}
