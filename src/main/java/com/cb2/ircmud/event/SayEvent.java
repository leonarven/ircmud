package com.cb2.ircmud.event;

import com.cb2.ircmud.domain.Item;

public class SayEvent extends Event {
	protected final String message;
	
	
	public SayEvent(Item sender, String msg) {
		super(Event.Type.Say, sender);
		this.message = msg;
	}

	String getMessage() { return message; }

	@Override
	public void trigger() {
		// TODO Implement SayEvent.trigger
	}
	

}
