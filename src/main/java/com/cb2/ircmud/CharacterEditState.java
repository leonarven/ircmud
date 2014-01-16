package com.cb2.ircmud;

import com.cb2.ircmud.domain.Item;
import com.cb2.ircmud.domain.Player;


public class CharacterEditState extends PlayerState {
	private Item character;
	
	public CharacterEditState(Player player, Item character) {
		super(player);
		this.character = character;
	}

	private static final String stateName = "CharacterCreationState";
	
	
	@Override
	public int getStateGroup() { return PlayerState.STATE_GROUP_PLAY_AND_CHARACTER_EDIT; }
	public Item getCharacter() { return character; }
	

	@Override
	public String getStateName() {
		return stateName;
	}

	@Override
	public void handlePlayerCommand(String commandString) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void handlePlayerMessage(String message) {
		// TODO Auto-generated method stub
		
	}

}
