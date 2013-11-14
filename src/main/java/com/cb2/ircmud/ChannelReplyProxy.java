package com.cb2.ircmud;

import com.cb2.ircmud.IrcUser;
import com.cb2.ircmud.IrcReply;

public interface ChannelReplyProxy {
	IrcReply reply(IrcReply reply);
}
