package com.cb2.ircmud.event;

import com.cb2.ircmud.domain.Item;
import com.cb2.ircmud.domain.LocationSpecifier;

public class MovementEndEvent extends Event {
MovementStartEvent movementStartEvent;
	
	public MovementEndEvent(Item sender, MovementStartEvent startEvent, EventListener target) {
		super(Event.Type.MovementEnd, sender, target);
		movementStartEvent = startEvent;
	}
	Item getMovingItem() { return (Item)getSender(); }
	MovementStartEvent getMovementStartEvent() { return movementStartEvent; }
	LocationSpecifier getTargetLocation() { return movementStartEvent.getTargetLocation(); }
}
