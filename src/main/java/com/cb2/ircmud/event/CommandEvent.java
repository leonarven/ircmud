package com.cb2.ircmud.event;

import com.cb2.ircmud.domain.Item;

public class CommandEvent extends Event {
	private final String commandString;
	public CommandEvent(Item sender, String commandString, EventListener target) {
		super(Event.Type.Command, sender, target);
		this.commandString = commandString;
	}
	
	public String getCommandString() { return this.commandString; }

}
