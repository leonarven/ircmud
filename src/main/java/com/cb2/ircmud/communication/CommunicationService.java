package com.cb2.ircmud.communication;

import java.util.List;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cb2.ircmud.PlayerGameState;
import com.cb2.ircmud.PlayerState;
import com.cb2.ircmud.domain.Item;
import com.cb2.ircmud.domain.Player;
import com.cb2.ircmud.domain.World;
import com.cb2.ircmud.domain.services.SoundService;
import com.cb2.ircmud.event.Event;
import com.cb2.ircmud.event.EventListener;
import com.cb2.ircmud.event.EventService;
import com.cb2.ircmud.event.SayEvent;
import com.cb2.ircmud.ircserver.Channel;
import com.cb2.ircmud.ircserver.IrcReply;
import com.github.rlespinasse.slf4j.spring.AutowiredLogger;

@Service
public class CommunicationService implements EventListener {
	@Autowired
	private SoundService soundService;
	@Autowired
	private EventService eventService;
	@AutowiredLogger
	Logger logger;
	
	public void handleChannelMessage(IrcReply reply) {
		String msg = reply.getPostfix();
		Player player = Player.findPlayer(reply.getSender().getPlayerId());
		if (msg.startsWith("!")) {
			handlePlayerCommand(msg.substring(1), player);
		}
		else {
			handlePlayerMessage(msg, player);
		}
	}
	
	public void handlePlayerCommand(String commandString, Player sender) {
		for (PlayerState s : sender.getState()) {
			s.handlePlayerCommand(commandString);
		}
	}
	
	public void handlePlayerMessage(String message, Player sender) {
		for (PlayerState s : sender.getState()) {
			s.handlePlayerMessage(message);
		}
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
		logger.debug("Say event! sender: " + sender.getName()  + "  message: " + event.getMessage());
		List<Item> items = soundService.listItemsWhichCanHear(sender, SoundService.SoundLevel.Normal);
		for (Item listener : items) {
			listener.handleEvent(event);
		}
	}
}
