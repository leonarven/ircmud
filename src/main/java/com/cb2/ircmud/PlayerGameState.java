package com.cb2.ircmud;

public class PlayerGameState extends PlayerState {
	private static final String stateName = "PlayerGameState";
	
	private 
	
	
	@Override
	public int getStateGroup() {
		return PlayerState.STATE_GROUP_WORLD;
	}

	@Override
	public String getStateName() {
		return stateName;
	}
}
