package com.cb2.ircmud.event;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Service;

@Service
public class EventService {
	private EventQueue eventQueue = new EventQueue();
	private Thread eventQueueThread; 
	private TimedEventQueue timedEventQueue = new TimedEventQueue();
	private Thread timedEventQueueThread;
	
	@PostConstruct
	private void initialize() {
		eventQueueThread = new Thread(eventQueue);
		timedEventQueueThread = new Thread(timedEventQueue);
		eventQueueThread.start();
		timedEventQueueThread.start();
	}
	
	public void addTimedEvent(TimedEvent event) {
		timedEventQueue.addEvent(event);
	}
	public void addEvent(Event event) {
		eventQueue.addEvent(event);
	}
	
}
