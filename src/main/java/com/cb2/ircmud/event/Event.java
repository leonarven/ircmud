package com.cb2.ircmud.event;

public abstract class Event {
	public enum Type {
		Say,
		ServerNotification
	}
	
	protected final Type type;
	protected final Object sender;
	
	public Event(Type type, Object sender) {
		this.type = type;
		this.sender = sender;
	}
	
	public Type getType() { return type; }
	public Object getSender() { return sender; }
	
	abstract public void trigger();
}
