package com.cb2.ircmud;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;

import com.cb2.ircmud.communication.CommunicationService;
import com.cb2.ircmud.domain.Item;
import com.cb2.ircmud.domain.Player;
import com.cb2.ircmud.event.EventService;
import com.cb2.ircmud.event.SayEvent;

@Configurable
public class PlayerGameState extends PlayerState {
	@Autowired
	CommunicationService communicationService;
	@Autowired
	EventService eventService;
	
	Item character;
	
	private static final String stateName = "PlayerGameState";
	
	public PlayerGameState(Player player) {
		super(player);
	}
	
	@Override
	public int getStateGroup() {
		return PlayerState.STATE_GROUP_PLAY_AND_CHARACTER_EDIT;
	}

	@Override
	public String getStateName() {
		return stateName;
	}

	public Item getCharacter() {
		return character;
	}

	public void setCharacter(Item character) {
		this.character = character;
	}

	@Override
	public void handlePlayerCommand(String commandString) {
		//TODO: implement command handling (CommandService?)
	}

	@Override
	public void handlePlayerMessage(String message) {
		eventService.addEvent(new SayEvent(character, message, communicationService));
	}
}
