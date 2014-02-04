package com.cb2.ircmud.event;

import com.cb2.ircmud.domain.Item;

public class VisionEvent extends Event {
	
	public VisionEvent(Item itemInVision) {
		super(Event.Type.Vision, itemInVision, null);
	}
	
	public Item getItemInVision() { return (Item)getSender(); }
}
