package com.cb2.ircmud.ircserver;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.annotation.Value;

@Configurable
public class Channel {

	protected ArrayList<IrcUser> channelMembers = new ArrayList<IrcUser>();
	protected String topic;
	protected String name;
	public String mode;
	@Value("$(config.server.WorldChannelPrefix)")
	protected String worldChannelPrefix;
	
	@Autowired 
	IrcServer server;
	
	
	public Channel(String name) {
		this.name = name;
		this.mode = "+stn";
	}
	
	public String getName() { return name; }
	
	public ArrayList<IrcUser> getChannelMembers() { return channelMembers; }
	
	public void sendReplyToAll(IrcReply reply) {
		synchronized (channelMembers) {
			for (IrcUser user : channelMembers) {
				user.sendReply(reply);
			}
		}
	}
	
	public void sendReplyToAllExceptSender(IrcReply reply) {
		synchronized (channelMembers) {
			for (IrcUser user : channelMembers) {
				if (user != reply.getSender()) user.sendReply(reply);
			}
		}
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
			sendReplyToAll(joinReply);
			
			if (this.topic != null) {
				IrcReply topicReply = IrcReply.serverReply(IrcReplyCode.RPL_TOPIC, user.getNickname(), this.name, this.topic);
				IrcReply topicDateReply = IrcReply.serverReply("333", user.getNickname(), this.name, "admin!admin@IrcMud", "0", "");

				user.sendReply(topicReply);
				user.sendReply(topicDateReply);
			} else {
				IrcReply topicReply = IrcReply.serverReply(IrcReplyCode.RPL_NOTOPIC, user.getNickname(), this.name, "No topic is set");

				user.sendReply(topicReply);
			}

			String userlist = user.getNickname();
			for(IrcUser _user : channelMembers) userlist = userlist + " " +  _user.getNickname();

			IrcReply namesReply = IrcReply.serverReply(IrcReplyCode.RPL_NAMREPLY, user.getNickname(), "@", this.name, userlist.trim());
			IrcReply namesEndReply = IrcReply.serverReply("366", user.getNickname(), this.name , "End of /NAMES list.");

			user.sendReply(namesReply);
			user.sendReply(namesEndReply);
			
			channelMembers.add(user);
		}
	}

	
	public void memberLeave(IrcUser user, String msg) {
		synchronized (channelMembers) {
			channelMembers.remove(user);
		}
		sendReplyToAll(new IrcReply(user, "PART", this.getName(), msg));
	}
	
	public void memberQuit(IrcUser user, String msg) {
		synchronized (channelMembers) {
			channelMembers.remove(user);
		}
		sendReplyToAll(new IrcReply(user, "QUIT", msg));
	}
	
	
	public static boolean isValidPrefix(char prefix) {
		// TODO make to better
		switch(prefix) {
			case '#':
			case '&':
			case '!':
				return true;
			default:
				return false;
		}
	}
}
