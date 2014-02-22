package com.cb2.ircmud.event;

import java.util.Date;

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
	
	public void addTimedEvent(Event event, Date emitTime) {
		timedEventQueue.addEvent(new TimedEvent(emitTime, event));
	}
	
	public void addEvent(Event event) {
		eventQueue.addEvent(event);
	}
	
	public void removeAllEventsByFilter(EventFilter eventFilter) {
		//TODO: Will this break if addEvent/addTimedEvent is called at the same time? (It shouldn't be possible?)
		timedEventQueue.removeEventsByFilter(eventFilter);
		eventQueue.removeEventsByFilter(eventFilter);
	}
	
}
