package com.cb2.ircmud.event;

public interface EventFilter {
	public boolean accept(Event event);
}
