package com.cb2.ircmud;

import com.cb2.ircmud.ChannelReplyProxy;
import com.cb2.ircmud.Channel;
import com.cb2.ircmud.IrcUser;

public class DefaultChannelReplyProxy implements ChannelReplyProxy {
	public IrcReply reply(IrcReply reply) {
		return reply;
	}
}
