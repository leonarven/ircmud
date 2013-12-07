package com.cb2.ircmud.communication;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cb2.ircmud.domain.Item;
import com.cb2.ircmud.domain.Player;
import com.cb2.ircmud.domain.services.SoundService;
import com.cb2.ircmud.event.Event;
import com.cb2.ircmud.event.EventListener;
import com.cb2.ircmud.event.SayEvent;
import com.cb2.ircmud.ircserver.Channel;
import com.cb2.ircmud.ircserver.IrcReply;

@Service
public class CommunicationService implements EventListener {
	@Autowired
	private SoundService soundService;
	
	public void handle(IrcReply msg) {
		
		
	}
	
	public Player getPlayer(IrcReply privMsg) {
		
		return null;
	}
	
	public Channel getChannel(IrcReply privMsg) {
		
		return null;
	}
	
	@Override
	public void handleEvent(Event event) {
		switch (event.getType()) {
		case Say: {
			handleEvent((SayEvent)event);			
			break;
		}
		default:
			break;
		}
	}
	
	public void handleEvent(SayEvent event) {
		Item sender = (Item)event.getSender();
		List<Item> items = soundService.listItemsWhichCanHear(sender, SoundService.SoundLevel.Normal);
		for (Item listener : items) {
			listener.handleEvent(event);
		}
	}
}
