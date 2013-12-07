package com.cb2.ircmud.event;

import org.springframework.stereotype.Service;

@Service
public class EventService {
	private EventQueue eventQueue;
	private TimedEventQueue timedEventQueue;
	
	public void addTimedEvent(TimedEvent event) {
		timedEventQueue.addEvent(event);
	}
	public void addEvent(Event event) {
		eventQueue.addEvent(event);
	}
	
}
