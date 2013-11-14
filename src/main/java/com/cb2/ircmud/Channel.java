package com.cb2.ircmud;

import java.util.ArrayList;

import com.cb2.ircmud.IrcUser;
import com.cb2.ircmud.DefaultChannelReplyProxy;

public class Channel {

	private ArrayList<IrcUser> channelMembers = new ArrayList<IrcUser>();
	private String topic;
	private ChannelReplyProxy messageProxy = new DefaultChannelReplyProxy();
	protected String name;
	
	public Channel(String name) {
		this.name = name;
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
				if (user.getRepresentation() != reply.sender()) user.sendReply(proxyReply);
			}
		}
	}
	
	public void sendWhoReply(IrcUser user) {
		// RPL_WHOREPLY 352
			for(IrcUser _user : channelMembers) {
				// TODO H/G => here/gone
				// TODO: H/G:n perään tulee henkilön statuksen indikoiva merkki (+/@)
				IrcReply whoReply = new IrcReply(IrcServer.globalServerName, "352", user.getNickname()+" "+this.name+" "+_user.getNickname()+" "+_user.getHostname()+" "+IrcServer.globalServerName+" "+_user.getNickname()+" H :0 " + _user.getRealname());
				user.sendReply(whoReply);
			}
			IrcReply whoEndReply = new IrcReply(IrcServer.globalServerName, "315", user.getNickname()+" " + this.name + " :End of /WHO list.");
			user.sendReply(whoEndReply);
	}
	
	public void addMember(IrcUser user) {
		synchronized (channelMembers) {
			IrcReply joinReply = new IrcReply(user, "JOIN", this.name);
			user.sendReply(joinReply);
			sendReplyToAll(joinReply);
			
			String topicT;
			if (this.topic != null)
				topicT = this.topic;
			else 
				topicT = "";
			IrcReply topicReply = new IrcReply(IrcServer.globalServerName, "332", user.nickname+" "+this.name+" :"+topicT);
			user.sendReply(topicReply);
			
			IrcReply topicDateReply = new IrcReply(IrcServer.globalServerName, "333", user.nickname+" "+this.name+" admin!admin@IrcMud 0");
			user.sendReply(topicDateReply);
			
			String userlist = user.nickname;
			for(IrcUser _user : channelMembers) userlist = userlist + " " +  _user.nickname;
			IrcReply namesReply = new IrcReply(IrcServer.globalServerName, "353", user.nickname+" @ "+ this.name + " :" + userlist.trim());
			user.sendReply(namesReply);
			
			IrcReply namesEndReply = new IrcReply(IrcServer.globalServerName, "366", user.nickname+" " + this.name + " :End of /NAMES list.");
			user.sendReply(namesEndReply);
			
			channelMembers.add(user);
		}
	}
	
	public void memberLeave(IrcUser user, String msg) {
		synchronized (channelMembers) {
			channelMembers.remove(user);
		}
		sendReplyToAll(new IrcReply(user, "PART", ":" + msg));
	}
	
	public void memberQuit(IrcUser user, String msg) {
		synchronized (channelMembers) {
			channelMembers.remove(user);
		}
		sendReplyToAll(new IrcReply(user, "QUIT", ":" + msg));
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
