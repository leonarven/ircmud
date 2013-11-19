package com.cb2.ircmud.command;



public abstract class CommandParameter {
	public enum Type {
		Integer,
		String,
		Location,
		Item
	}
	
	public abstract Type type();
}
