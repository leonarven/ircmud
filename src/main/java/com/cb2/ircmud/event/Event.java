package com.cb2.ircmud.event;

import java.util.ArrayList;

public abstract class Event {
	public enum Type {
		Say,
		ServerNotification,
		Look,
		Vision
	}
	
	protected final Type type;
	protected final EventListener target;
	protected final Object sender;
	protected final ArrayList<Event> childEvents = new ArrayList<Event>();
	
	public Event(Type type, Object sender, EventListener target) {
		this.type = type;
		this.sender = sender;
		this.target = target;
	}
	
	public Type getType() { return type; }
	public Object getSender() { return sender; }
	public EventListener getTarget() { return target; }
	public ArrayList<Event> getChildEvents() { return childEvents; }
	public void addChildEvent(Event e) { childEvents.add(e); }
}
