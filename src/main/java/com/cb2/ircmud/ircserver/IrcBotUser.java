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
		if ((parsePrivateMessages || parseChannelMessages) && reply.command().equals("PRIVMSG")) {
			parsePrivateMessage(reply);
		}
		else if (parseJoinMessages && reply.command().equals("JOIN")) {
			
		}
	}
	
	public void parsePrivateMessage(IrcReply reply) {
		String sender = IrcUser.getNicknameFromRepresentation(reply.sender());
		if (Channel.isValidPrefix(reply.argument(0).charAt(0))) { //Channel msg
			if (parseChannelMessages) {
				receiveChannelMessage(sender, reply.argument(0), reply.postfix());
			}
		}
		else if (parsePrivateMessages) {
			receivePrivateMessage(sender, reply.postfix());
		}
	}
	
	public void parseJoinMessage(IrcReply join) {
		String joinedUser = IrcUser.getNicknameFromRepresentation(join.sender());
		String channel = join.argument(0);
		receiveJoinMessage(joinedUser, channel);
	}
	
	public void receivePrivateMessage(String sender, String msg) {}
	public void receiveChannelMessage(String sender, String chan, String msg) {}
	public void receiveJoinMessage(String joinedUser, String channel) {}

	@Override
	boolean isConnection() {
		return false;
	}

}
