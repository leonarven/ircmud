package com.cb2.ircmud;

import com.cb2.ircmud.domain.Player;

public abstract class PlayerState {
	
	/** Player can only have one state of each group. */
	public static final int STATE_GROUP_PLAY_AND_CHARACTER_EDIT = 1;
	public static final int STATE_GROUP_WORLD_EDIT = 2;
	
	private final Player player;
	
	public PlayerState(Player player) {
		this.player = player;
	}
	
	public abstract int getStateGroup();
	public abstract String getStateName();
	Player getPlayer() { return player; }
	
	public abstract void handlePlayerCommand(String commandString);
	public abstract void handlePlayerMessage(String message);
}
