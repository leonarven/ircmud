package com.cb2.ircmud.event;

import java.util.Iterator;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class EventQueue implements Runnable {
	private BlockingQueue<Event> queue = new LinkedBlockingQueue<Event>();
	@Override
	public void run() {
			while(true) {
				try {
					Event event;
					synchronized (queue) {
						event = queue.take();
					}
					event.getTarget().handleEvent(event);
				} catch (InterruptedException e) {
					e.printStackTrace();
					break;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
	}
	
	public void addEvent(Event event) {
		synchronized (queue) {
			queue.add(event);
		}
	}
	
	public void removeEventsByFilter(EventFilter filter) {
		synchronized (queue) {
			Iterator<Event> i = queue.iterator();
			while (i.hasNext()) {
				Event e = i.next();
				if (filter.accept(e)) {
					i.remove();
				}
			}
		}
	}
}
