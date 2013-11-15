package com.cb2.ircmud;

import java.util.HashMap;
import java.util.Map;

import com.cb2.ircmud.Channel;
import com.cb2.ircmud.IrcReply;

public abstract class IrcUser {
	protected String nickname;
	protected String mode;
	protected String realname;
	protected String username;
	protected String hostname;
	protected Map<String, Channel> joinedChannels = new HashMap<String, Channel>();
	
	abstract void sendReply(IrcReply reply);
	abstract boolean isConnection();
	
	public String getNickname() { return nickname; }
	public String getRealname() { return realname; }
	public String getHostname() { return hostname; }
	public String getUsername() { return username; }
	public String getMode() { return mode; }
	
	public String getRepresentation() {
		return this.nickname + "!" + this.username + "@" + this.hostname;
	}
	
	public void joinChannel(Channel chan) {
		chan.addMember(this);
		
		synchronized (joinedChannels) {
			joinedChannels.put(chan.getName(), chan);
		}
	}
	
	public boolean leaveChannel(String channelName, String msg) {
		if (!joinedChannels.containsKey(channelName)) return false;
		joinedChannels.remove(channelName);
		Channel chan = IrcServer.findChannel(channelName);
		if (chan == null) return false;
		chan.memberLeave(this, msg);
		if (chan.getChannelMembers().size() == 0) IrcServer.dropChannel(channelName);
		return true;
	}
	
	public boolean quit(String msg) {
		synchronized (joinedChannels) {
			for (Map.Entry<String, Channel> entry : this.joinedChannels.entrySet()) {
				entry.getValue().memberQuit(this, msg);
			}
		}
		return true;
	}
	
	public void sendWhoReply(Channel chan) {
		chan.sendWhoReply(this);
	}
	
	public void sendWhoIsReply(IrcUser who) {
		// RPL_WHOISUSER 311
		// RPL_WHOISSERVER 312
		// RPL_ENDOFWHOIS 318
		IrcReply whoIsUserReply = new IrcReply(IrcServer.globalServerName, "331", this.nickname+" "+who.nickname+" "+who.username+" "+who.hostname+" * :"+who.realname);
		this.sendReply(whoIsUserReply);
		
		IrcReply whoIsServerReply = new IrcReply(IrcServer.globalServerName, "312", this.nickname+" "+who.nickname+" "+IrcServer.globalServerName+" :"+IrcServer.globalServerInfo);
		this.sendReply(whoIsServerReply);
		// TODO if is IRCoperator RPL_WHOISOPERATOR 313
		// TODO if is away RPL_WHOISIDLE 317
		
		IrcReply whoIsEndReply = new IrcReply(IrcServer.globalServerName, "318", this.nickname+" "+who.nickname+" :End of /WHOIS list.");
		this.sendReply(whoIsEndReply);
	}
	
}
