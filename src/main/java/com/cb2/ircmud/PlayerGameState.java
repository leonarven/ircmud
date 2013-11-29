package com.cb2.ircmud;

import com.cb2.ircmud.domain.Item;

public class PlayerGameState extends PlayerState {
	private static final String stateName = "PlayerGameState";
	
	Item character;
	
	@Override
	public int getStateGroup() {
		return PlayerState.STATE_GROUP_WORLD;
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
}
