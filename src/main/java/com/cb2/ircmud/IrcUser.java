package com.cb2.ircmud;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.HashSet;

import com.cb2.ircmud.Channel;
import com.cb2.ircmud.IrcReply;
import java.io.IOException;

public abstract class IrcUser {
	protected String nickname;
	protected String mode;
	protected String realname;
	protected String username;
	protected String hostname;
	protected Map<String, Channel> joinedChannels = new HashMap<String, Channel>();
	
	public static String getIrcUserRepresentation(String nick, String username, String hostname) {
		return nick + "!" + username + "@" + hostname;
	}
	
	public static String getNicknameFromRepresentation(String representation) {
		return representation.split("!")[0];
	}
	
	
	abstract void sendRawString(String s);
	abstract void sendReply(IrcReply reply);
	abstract boolean isConnection();
	abstract void closeConnection() throws IOException;
	
	public String getNickname() { return nickname; }
	public String getRealname() { return realname; }
	public String getHostname() { return hostname; }
	public String getUsername() { return username; }
	public String getMode() { return mode; }
	
	public boolean tryChangeNickname(String newNick) {
		//Update IrcServer.userNicknameMap
		if (!IrcServer.trySetNickname(this, newNick, nickname)) return false;
		
		//Send notification to all users in the same channels
		Set<IrcUser> userSet = new HashSet<IrcUser>();
		for (Map.Entry<String, Channel> entry : joinedChannels.entrySet()) {
			userSet.addAll(entry.getValue().getChannelMembers());
		}
		IrcReply nickReply = new IrcReply(getRepresentation(), "NICK", newNick);
		for (IrcUser user : userSet) {
			user.sendReply(nickReply);
		}
		
		nickname = newNick;
		return true;
	}
	
	public String getRepresentation() {
		return getIrcUserRepresentation(nickname, username, hostname);
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
		sendDebug("Leaving channel "+chan.getName());
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
		sendRawString("ERROR: Closing Link: "+this.getRepresentation()+"(\""+msg+"\")");
		return true;
	}
	
	public void sendMessage(IrcUser sender, String msg) {
		sendReply(new IrcReply(sender, "PRIVMSG", this.nickname, msg));
	}
	
	public void sendDebug(String msg) {
		sendReply(new IrcReply(this, "NOTICE", "DEBUG: "+msg));
	}
	
	public void sendWhoReply(Channel chan) {
		chan.sendWhoReply(this);
	}
	
	public void sendWhoIsReply(IrcUser who) {
		// RPL_WHOISUSER 311
		// RPL_WHOISSERVER 312
		// RPL_ENDOFWHOIS 318
		IrcReply whoIsUserReply   = IrcReply.serverReply(IrcReplyCode.RPL_WHOISUSER, this.nickname, who.nickname, who.username, who.hostname, "*", who.realname);
		IrcReply whoIsServerReply = IrcReply.serverReply(IrcReplyCode.RPL_WHOISSERVER, this.nickname, who.nickname, IrcServer.globalServerName, IrcServer.globalServerInfo);
		IrcReply whoIsEndReply    = IrcReply.serverReply(IrcReplyCode.RPL_ENDOFWHOIS, this.nickname, who.nickname, "End of /WHOIS list.");
		// TODO if is IRCoperator RPL_WHOISOPERATOR 313
		// TODO if is away RPL_WHOISIDLE 317

		this.sendReply(whoIsUserReply);
		this.sendReply(whoIsServerReply);
		this.sendReply(whoIsEndReply);
	}
	
}
