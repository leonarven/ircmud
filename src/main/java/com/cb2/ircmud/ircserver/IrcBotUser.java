package com.cb2.ircmud.ircserver;

public abstract class IrcBotUser extends IrcUser {
	protected boolean parsePrivateMessages = false;
	protected boolean parseChannelMessages = false;
	protected boolean parseJoinMessages = false;
	
	public IrcBotUser(String nick, String realname) {
		this.nickname = nick;
		this.hostname = "IrcMud.service.bot";
		this.username = nick;
		this.realname = realname;
	}
	
	@Override
	public void sendReply(IrcReply reply) {
		if ((parsePrivateMessages || parseChannelMessages) && reply.getCommandName().equals("PRIVMSG")) {
			parsePrivateMessage(reply);
		}
		else if (parseJoinMessages && reply.getCommandName().equals("JOIN")) {
			
		}
	}
	
	public void parsePrivateMessage(IrcReply reply) {
		IrcUser sender = reply.getSender();
		if (Channel.isValidPrefix(reply.argument(0).charAt(0))) { //Channel msg
			if (parseChannelMessages) {
				receiveChannelMessage(sender, reply.argument(0), reply.getPostfix());
			}
		}
		else if (parsePrivateMessages) {
			receivePrivateMessage(sender, reply.getPostfix());
		}
	}
	
	public void parseJoinMessage(IrcReply join) {
		IrcUser joinedUser = join.getSender();
		String channel = join.argument(0);
		receiveJoinMessage(joinedUser, channel);
	}
	
	public void receivePrivateMessage(IrcUser sender, String msg) {}
	public void receiveChannelMessage(IrcUser sender, String chan, String msg) {}
	public void receiveJoinMessage(IrcUser joinedUser, String channel) {}

	@Override
	boolean isConnection() {
		return false;
	}
}
