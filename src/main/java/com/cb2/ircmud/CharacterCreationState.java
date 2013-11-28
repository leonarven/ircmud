package com.cb2.ircmud;

public class CharacterCreationState extends PlayerState {
	private static final String stateName = "CharacterCreationState";
	
	
	@Override
	public int getStateGroup() { return PlayerState.STATE_GROUP_CHARACTER_CREATION; }

	@Override
	public String getStateName() {
		return stateName;
	}

}
