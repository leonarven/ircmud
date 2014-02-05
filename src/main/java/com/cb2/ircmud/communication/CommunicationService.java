package com.cb2.ircmud.communication;

import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cb2.ircmud.PlayerGameState;
import com.cb2.ircmud.PlayerState;
import com.cb2.ircmud.domain.Item;
import com.cb2.ircmud.domain.Player;
import com.cb2.ircmud.domain.World;
import com.cb2.ircmud.domain.components.PlayerComponent;
import com.cb2.ircmud.domain.containers.Container;
import com.cb2.ircmud.domain.containers.Room;
import com.cb2.ircmud.domain.services.GameService;
import com.cb2.ircmud.domain.services.RoomService;
import com.cb2.ircmud.domain.services.SoundService;
import com.cb2.ircmud.event.Event;
import com.cb2.ircmud.event.EventListener;
import com.cb2.ircmud.event.EventService;
import com.cb2.ircmud.event.SayEvent;
import com.cb2.ircmud.ircserver.Channel;
import com.cb2.ircmud.ircserver.IrcReply;
import com.cb2.ircmud.ircserver.IrcUser;
import com.github.rlespinasse.slf4j.spring.AutowiredLogger;

@Service
public class CommunicationService implements EventListener {
	@Autowired
	private SoundService soundService;
	@Autowired
	private EventService eventService;
	@Autowired
	private GameService gameService;
	@Autowired
	private RoomService roomService;
	@AutowiredLogger
	Logger logger;
	
	public void handleChannelMessage(IrcUser sender, String message) {
		Player player = Player.findPlayer(sender.getPlayerId());
		if (message.startsWith("!")) {
			handlePlayerCommand(message.substring(1), player);
		}
		else {
			handlePlayerMessage(message, player);
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
	
	public String generateItemList(List<Item> items) {
		String list = new String();
		Iterator<Item> i = items.iterator();
		if (i.hasNext()) {
			list += i.next().getName().getItemName(1, false);
			while (i.hasNext()) {
				String itemName = i.next().getName().getItemName(1, false);
				if (i.hasNext()) {
					list += ", " + itemName;
				}
				else {
					list += " and " + itemName;
 				}
			}
		}
		return list;
	}
	
	@Transactional
	public void handleEvent(SayEvent event) {
		Item sender = (Item)event.getSender();
		logger.debug("Say event! sender: " + sender.getName()  + "  message: " + event.getMessage());
		List<Item> items = soundService.listItemsWhichCanHear(sender, SoundService.SoundLevel.Normal);
		for (Item listener : items) {
			if (sender.getId() != listener.getId()) {
				if (!listener.isSessionOpen()) listener = Item.findItem(listener.getId());
				listener.handleEvent(event);
			}
		}
	}
	
	String getItemIrcName(Item character) {
		return character.getName().getItemName(1, true).replaceAll(" ", "_");
	}
	
	@Transactional
	public void sayToCharacter(Item speaker, Item character, String message) {
		PlayerComponent playerComponent = character.findFirstComponentInstanceOf(PlayerComponent.class);
		if (playerComponent == null) return;
		Player player = playerComponent.getPlayer();
		IrcUser ircUser = player.getIrcUser();
		IrcReply reply = new IrcReply(getItemIrcName(speaker), "PRIVMSG", gameService.getGameChannel().getName(), message);
		ircUser.sendReply(reply);
	}
	
	@Transactional
	public void sendStoryMessageToAllPlayersInRoom(Room room, String message) {
		List<Item> playerChars = roomService.findPlayerCharactersInRoom(room);
		for (Item c : playerChars) {
			sendStoryMessageToCharacter(c, message);
		}
	}
	
	@Transactional
	public void sendStoryMessageToAllPlayersInRoomExcept(Room room, String message, Item except) {
		List<Item> playerChars = roomService.findPlayerCharactersInRoom(room);
		for (Item c : playerChars) {
			if (c.getId() != except.getId()) {
				sendStoryMessageToCharacter(c, message);
			}
		}
	}
	
	@Transactional
	public void sendStoryMessageToCharacter(Item character, String message) {
		PlayerComponent playerComponent = character.findFirstComponentInstanceOf(PlayerComponent.class);
		Player player = playerComponent.getPlayer();
		IrcUser ircUser = player.getIrcUser();
		IrcReply reply = new IrcReply("Story", "PRIVMSG", gameService.getGameChannel().getName(), message);
		ircUser.sendReply(reply);
		
	}
	
	@Transactional
	public void sendErrorMessageToCharacter(Item character, String message) {
		PlayerComponent playerComponent = character.findFirstComponentInstanceOf(PlayerComponent.class);
		Player player = playerComponent.getPlayer();
		IrcUser ircUser = player.getIrcUser();
		IrcReply reply = new IrcReply("Error", "PRIVMSG", gameService.getGameChannel().getName(), message);
		ircUser.sendReply(reply);
	}
}
