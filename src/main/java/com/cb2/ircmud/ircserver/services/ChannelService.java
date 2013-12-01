package com.cb2.ircmud.ircserver.services;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.springframework.stereotype.Service;

import com.cb2.ircmud.ircserver.Channel;
import com.github.rlespinasse.slf4j.spring.AutowiredLogger;

@Service
public class ChannelService {
	@AutowiredLogger
	Logger logger;
	
	private Map<String, Channel> channelMap = new HashMap<String, Channel>();
	
	public void add(Channel channel){
		logger.debug("addChannel({})",channel.getName());
		channelMap.put(channel.getName().toLowerCase(), channel);
	}
	
	public Channel getChannel(String name){
		return channelMap.get(name);
	}
	
	public Channel find(String channelName) {
		channelName = channelName.toLowerCase();
		synchronized (channelMap) {
			if (!channelMap.containsKey(channelName)) return null;
			return channelMap.get(channelName);
		}
	}
	
	public void dropChannel(String channelName) {
		logger.debug("dropChannel({})",channelName);
		channelName = channelName.toLowerCase();
		synchronized (channelMap) {
			if (!channelMap.containsKey(channelName)) return;

			channelMap.remove(channelName);
		}
	}

}
