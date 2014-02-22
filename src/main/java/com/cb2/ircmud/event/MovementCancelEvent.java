package com.cb2.ircmud.event;

import com.cb2.ircmud.domain.Item;
import com.cb2.ircmud.domain.LocationSpecifier;

public class MovementCancelEvent extends Event {
	MovementStartEvent movementStartEvent;
	
	public MovementCancelEvent(Item sender, MovementStartEvent startEvent, EventListener target) {
		super(Event.Type.MovementCancel, sender, target);
		movementStartEvent = startEvent;
	}
	Item getMovingItem() { return (Item)getSender(); }
	MovementStartEvent getMovementStartEvent() { return movementStartEvent; }
	LocationSpecifier getTargetLocation() { return movementStartEvent.getTargetLocation(); }
}
