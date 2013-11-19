package com.cb2.ircmud;

import com.cb2.ircmud.ircserver.IrcReply;
import com.cb2.ircmud.ircserver.IrcUser;

public interface ChannelReplyProxy {
	IrcReply reply(IrcReply reply);
}
