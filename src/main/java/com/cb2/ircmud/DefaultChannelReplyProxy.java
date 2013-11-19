package com.cb2.ircmud;

import com.cb2.ircmud.ChannelReplyProxy;
import com.cb2.ircmud.ircserver.Channel;
import com.cb2.ircmud.ircserver.IrcReply;
import com.cb2.ircmud.ircserver.IrcUser;

public class DefaultChannelReplyProxy implements ChannelReplyProxy {
	public IrcReply reply(IrcReply reply) {
		return reply;
	}
}
