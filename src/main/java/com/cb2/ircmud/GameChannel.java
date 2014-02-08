package com.cb2.ircmud;

import org.springframework.beans.factory.annotation.Autowired;

import com.cb2.ircmud.communication.CommunicationService;
import com.cb2.ircmud.ircserver.Channel;
import com.cb2.ircmud.ircserver.IrcReply;
import com.cb2.ircmud.ircserver.IrcReplyCode;
import com.cb2.ircmud.ircserver.IrcServer;
import com.cb2.ircmud.ircserver.IrcUser;


public class GameChannel extends Channel {

	@Autowired 
	IrcServer server;
	@Autowired
	CommunicationService communication;
	
	public GameChannel(String name) {
		super(name);
		
	}
	
	@Override
	public void messageToChannelReceived(IrcUser sender, String message) {
		communication.handleChannelMessage(sender, message);
	}
	
	
	
	@Override
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
	
	@Override
	public void addMember(IrcUser user) {
		synchronized (channelMembers) {
			IrcReply joinReply = new IrcReply(user, "JOIN", this.name, "");
			user.sendReply(joinReply);
			//sendReplyToAll(joinReply);
			
			if (this.topic != null) {
				IrcReply topicReply = IrcReply.serverReply(IrcReplyCode.RPL_TOPIC, user.getNickname(), this.name, this.topic);
				IrcReply topicDateReply = IrcReply.serverReply("333", user.getNickname(), this.name, "admin!admin@IrcMud", "0", "");

				user.sendReply(topicReply);
				user.sendReply(topicDateReply);
			} else {
				IrcReply topicReply = IrcReply.serverReply(IrcReplyCode.RPL_NOTOPIC, user.getNickname(), this.name, "No topic is set");

				user.sendReply(topicReply);
			}

			String userlist = user.getNickname() + " Story";
			IrcReply namesReply = IrcReply.serverReply(IrcReplyCode.RPL_NAMREPLY, user.getNickname(), "@", this.name, userlist.trim());
			IrcReply namesEndReply = IrcReply.serverReply("366", user.getNickname(), this.name , "End of /NAMES list.");

			user.sendReply(namesReply);
			user.sendReply(namesEndReply);
			
			channelMembers.add(user);
		}
	}

	@Override
	public void memberLeave(IrcUser user, String msg) {
		synchronized (channelMembers) {
			channelMembers.remove(user);
		}
		user.sendReply(new IrcReply(user, "PART", this.getName(), msg));
	}
	
	@Override
	public void memberQuit(IrcUser user, String msg) {
		synchronized (channelMembers) {
			channelMembers.remove(user);
		}
		user.sendReply(new IrcReply(user, "QUIT", msg));
	}
	
	
	
}
