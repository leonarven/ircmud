package com.cb2.ircmud;

import com.cb2.ircmud.IChannelMessageProxy;
import com.cb2.ircmud.Channel;
import com.cb2.ircmud.Connection;

public class DefaultChannelMessageProxy implements IChannelMessageProxy {
	public void privateMessageToChannel(Connection sender, Channel channel, String msg) {
		channel.sendRawStringAllExcept(sender, ":" + sender.getRepresentation() + " PRIVMSG " + channel.getName() + " :" + msg );
	}
}
