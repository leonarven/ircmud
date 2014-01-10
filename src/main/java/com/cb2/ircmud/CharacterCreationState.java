package com.cb2.ircmud;

import com.cb2.ircmud.domain.Player;


public class CharacterCreationState extends PlayerState {
	public CharacterCreationState(Player player) {
		super(player);
		// TODO Auto-generated constructor stub
	}

	private static final String stateName = "CharacterCreationState";
	
	
	@Override
	public int getStateGroup() { return PlayerState.STATE_GROUP_PLAY_AND_CHARACTER_EDIT; }

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
