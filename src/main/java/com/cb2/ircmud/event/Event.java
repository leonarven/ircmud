package com.cb2.ircmud.event;

public abstract class Event {
	public enum Type {
		Say,
		ServerNotification
	}
	
	protected final Type type;
	protected final EventListener target;
	protected final Object sender;
	
	public Event(Type type, Object sender, EventListener target) {
		this.type = type;
		this.sender = sender;
		this.target = target;
	}
	
	public Type getType() { return type; }
	public Object getSender() { return sender; }
	public EventListener getTarget() { return target; }
}
