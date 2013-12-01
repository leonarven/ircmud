package com.cb2.ircmud;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;

import com.cb2.ircmud.domain.World;
import com.cb2.ircmud.ircserver.Channel;
import com.cb2.ircmud.ircserver.IrcReply;
import com.cb2.ircmud.ircserver.IrcReplyCode;
import com.cb2.ircmud.ircserver.IrcServer;
import com.cb2.ircmud.ircserver.IrcUser;


public class GameChannel extends Channel {
	private World world;
	
	@Autowired 
	IrcServer server;
	
	public GameChannel(String name) {
		super(name);
		
	}
	
	@Override
	public void sendReplyToAll(IrcReply reply) {
		
	}
	@Override
	public void sendReplyToAllExceptSender(IrcReply reply) {
		if (reply.getCommandName().equals("PRIVMSG")) {
			handlePrivateMessage(reply);
		}
	}
	
	
	public void handlePrivateMessage(IrcReply privMsg) {
		
	}
	
	public void sendWhoReply(IrcUser user) {
		// RPL_WHOREPLY 352
			for(IrcUser _user : channelMembers) {
				// TODO H/G => here/gone
				// TODO: H/G:n perään tulee henkilön statuksen indikoiva merkki (+/@)
				IrcReply whoReply = IrcReply.serverReply(IrcReplyCode.RPL_WHOREPLY, user.getNickname(), this.name, _user.getNickname(), _user.getHostname(), server.serverName, _user.getNickname(), "H", "0 0 " + _user.getRealname());
				user.sendReply(whoReply);
			}
			IrcReply whoEndReply = IrcReply.serverReply(IrcReplyCode.RPL_ENDOFWHO, user.getNickname(), this.name, "End of /WHO list.");
			user.sendReply(whoEndReply);
	}
	
	public void addMember(IrcUser user) {
		synchronized (channelMembers) {
			IrcReply joinReply = new IrcReply(user, "JOIN", this.name, "");
			user.sendReply(joinReply);
			
			if (this.topic != null) {
				IrcReply topicReply = IrcReply.serverReply(IrcReplyCode.RPL_TOPIC, user.getNickname(), this.name, this.topic);
				IrcReply topicDateReply = IrcReply.serverReply("333", user.getNickname(), this.name, "admin!admin@IrcMud", "0", "");

				user.sendReply(topicReply);
				user.sendReply(topicDateReply);
			} else {
				IrcReply topicReply = IrcReply.serverReply(IrcReplyCode.RPL_NOTOPIC, user.getNickname(), this.name, "No topic is set");
				user.sendReply(topicReply);
			}
			IrcReply namesEndReply = IrcReply.serverReply("366", user.getNickname(), this.name , "End of /NAMES list.");
			user.sendReply(namesEndReply);
			
			channelMembers.add(user);
		}
	}

	
	public void memberLeave(IrcUser user, String msg) {
		synchronized (channelMembers) {
			channelMembers.remove(user);
		}
	}
	
	public void memberQuit(IrcUser user, String msg) {
		synchronized (channelMembers) {
			channelMembers.remove(user);
		}
	}
	
	
	
}
