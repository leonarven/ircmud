package com.cb2.ircmud;

import com.cb2.ircmud.Connection;
import com.cb2.ircmud.Channel;

public interface IChannelMessageProxy {
	void privateMessageToChannel(Connection sender, Channel channel, String msg);
}
