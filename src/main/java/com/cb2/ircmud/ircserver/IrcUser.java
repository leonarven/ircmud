package com.cb2.ircmud.ircserver;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.HashSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;

import com.cb2.ircmud.domain.Player;
import com.cb2.ircmud.ircserver.services.ChannelService;
import com.cb2.ircmud.ircserver.services.UserService;

public abstract class IrcUser {

	protected String nickname;
	protected String mode;
	protected String realname;
	protected String username;
	protected String hostname;
	protected boolean keepRunning = true;
	protected Map<String, Channel> joinedChannels = new HashMap<String, Channel>();
	protected Player player = null;
	
	@Autowired 
	Environment env;
	@Autowired 
	UserService users;
	@Autowired 
	ChannelService channels;
	
	
	public static String getIrcUserRepresentation(String nick, String username, String hostname) {
		return nick + "!" + username + "@" + hostname;
	}
	
	public static String getNicknameFromRepresentation(String representation) {
		return representation.split("!")[0];
	}
	
	
	public abstract void sendReply(IrcReply reply);
	protected abstract boolean isConnection();
	
	public String getNickname() { return nickname; }
	public String getRealname() { return realname; }
	public String getHostname() { return hostname; }
	public String getUsername() { return username; }
	public String getMode() { return mode; }
	public Player getPlayer() { return player; }
	public boolean isLoggedIn() { return player != null; }
	
	/**
	 * Only for AuthService
	 * @param player
	 */
	public void setPlayer(Player player) {
		this.player = player;
		//TODO: Kick IrcUser (player == null) if doesn't have access to the channel anymore
	}
	
	public boolean tryChangeNickname(String newNick) {
		//Update IrcServer.userNicknameMap
		if (!users.trySetNickname(this, newNick, nickname)) return false;
		
		//Send notification to all users in the same channels
		Set<IrcUser> userSet = new HashSet<IrcUser>();
		for (Map.Entry<String, Channel> entry : joinedChannels.entrySet()) {
			userSet.addAll(entry.getValue().getChannelMembers());
		}
		IrcReply nickReply = new IrcReply(this, "NICK", newNick);
		for (IrcUser user : userSet) {
			user.sendReply(nickReply);
		}
		
		nickname = newNick;
		return true;
	}
	
	public boolean hasJoinedToChannel(String channelName) {
		return joinedChannels.containsKey(channelName);
	}
	
	public Channel findJoinedChannel(String channelName) {
		return joinedChannels.get(channelName);
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
		Channel chan = channels.find(channelName);
		if (chan == null) return false;
		sendDebug("Leaving channel "+chan.getName());
		chan.memberLeave(this, msg);
		if (chan.getChannelMembers().size() == 0) channels.dropChannel(channelName);
		return true;
	}
	
	public boolean quit(String msg) {
		synchronized (joinedChannels) {
			for (Map.Entry<String, Channel> entry : this.joinedChannels.entrySet()) {
				entry.getValue().memberQuit(this, msg);
			}
		}
		if (this instanceof Connection) {
			((Connection)this).sendRawString("ERROR :Closing Link: " + this.getRepresentation() + " (\"" + msg + "\")");
		}
		keepRunning = false;
		return true;
	}
	
	public void sendMultiLineMessage(IrcUser sender, String msg) {
		String[] parts = msg.split("\n");
		for (String p : parts) {
			sendReply(new IrcReply(sender, "PRIVMSG", this.nickname, p));
		}
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
		IrcReply whoIsServerReply = 
				IrcReply.serverReply(IrcReplyCode.RPL_WHOISSERVER, this.nickname, who.nickname, 
				env.getProperty("config.server.name"), 
				env.getProperty("config.server.info"));
		IrcReply whoIsEndReply    = IrcReply.serverReply(IrcReplyCode.RPL_ENDOFWHOIS, this.nickname, who.nickname, "End of /WHOIS list.");
		// TODO if is IRCoperator RPL_WHOISOPERATOR 313
		// TODO if is away RPL_WHOISIDLE 317

		this.sendReply(whoIsUserReply);
		this.sendReply(whoIsServerReply);
		this.sendReply(whoIsEndReply);
	}
	
}
