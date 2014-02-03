package com.cb2.ircmud.event;

import com.cb2.ircmud.domain.Item;

public class SayEvent extends Event {
	protected final String message;
	
	
	public SayEvent(Item sender, String msg, EventListener target) {
		super(Event.Type.Say, sender, target);
		this.message = msg;
	}

	public String getMessage() { return message; }
}
