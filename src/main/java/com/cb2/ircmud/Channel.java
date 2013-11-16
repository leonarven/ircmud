package com.cb2.ircmud;

import java.util.ArrayList;

import com.cb2.ircmud.IrcUser;
import com.cb2.ircmud.DefaultChannelReplyProxy;

public class Channel {

	private ArrayList<IrcUser> channelMembers = new ArrayList<IrcUser>();
	private String topic;
	protected String name;
	public String mode;
	private ChannelReplyProxy messageProxy = new DefaultChannelReplyProxy();
	
	public Channel(String name) {
		this.name = name;
		this.mode = "+stn";
	}
	
	public String getName() { return name; }
	
	public ArrayList<IrcUser> getChannelMembers() { return channelMembers; }
	
	public void sendReplyToAll(IrcReply reply) {
		IrcReply proxyReply = messageProxy.reply(reply);
		synchronized (channelMembers) {
			for (IrcUser user : channelMembers) {
				user.sendReply(proxyReply);
			}
		}
	}
	
	public void sendReplyToAllExceptSender(IrcReply reply) {
		IrcReply proxyReply = messageProxy.reply(reply);
		synchronized (channelMembers) {
			for (IrcUser user : channelMembers) {
				if (!user.getRepresentation().equals(reply.sender())) user.sendReply(proxyReply);
			}
		}
	}
	
	public void sendWhoReply(IrcUser user) {
		// RPL_WHOREPLY 352
			for(IrcUser _user : channelMembers) {
				// TODO H/G => here/gone
				// TODO: H/G:n perään tulee henkilön statuksen indikoiva merkki (+/@)
				IrcReply whoReply = IrcReply.serverReply(Const.RPL_WHOREPLY, user.getNickname(), this.name, _user.getNickname(), _user.getHostname(), IrcServer.globalServerName, _user.getNickname(), "H", "0 0 " + _user.getRealname());
				user.sendReply(whoReply);
			}
			IrcReply whoEndReply = IrcReply.serverReply(Const.RPL_ENDOFWHO, user.getNickname(), this.name, "End of /WHO list.");
			user.sendReply(whoEndReply);
	}
	
	public void addMember(IrcUser user) {
		synchronized (channelMembers) {
			IrcReply joinReply = new IrcReply(user, "JOIN", this.name, "");
			user.sendReply(joinReply);
			sendReplyToAll(joinReply);
			
			if (this.topic != null) {
				IrcReply topicReply = IrcReply.serverReply(Const.RPL_TOPIC, user.nickname, this.name, this.topic);
				IrcReply topicDateReply = IrcReply.serverReply("333", user.nickname, this.name, "admin!admin@IrcMud", "0", "");

				user.sendReply(topicReply);
				user.sendReply(topicDateReply);
			}
			String userlist = user.nickname;
			for(IrcUser _user : channelMembers) userlist = userlist + " " +  _user.nickname;
			IrcReply namesReply = IrcReply.serverReply(Const.RPL_NAMREPLY, user.nickname, "@", this.name, userlist.trim());
			IrcReply namesEndReply = IrcReply.serverReply("366", user.nickname, this.name , "End of /NAMES list.");

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
