package com.cb2.ircmud.event;

import com.cb2.ircmud.MovementType;
import com.cb2.ircmud.domain.Item;
import com.cb2.ircmud.domain.LocationSpecifier;

public class MovementStartEvent extends Event {
	
	private LocationSpecifier targetLocation;
	private MovementType movementType;
	
	public MovementStartEvent(Item movingItem, LocationSpecifier targetLocation, MovementType movementType, EventListener target) {
		super(Event.Type.MovementStart, movingItem, target);
		this.targetLocation = targetLocation;
		this.movementType = movementType;
	}
	
	Item getMovingItem() { return (Item)getSender(); }
	LocationSpecifier getTargetLocation() { return targetLocation; }
	MovementType getMovementType() { return movementType; }

}
