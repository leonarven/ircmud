package com.cb2.ircmud.event;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class EventQueue implements Runnable {
	private BlockingQueue<Event> queue = new LinkedBlockingQueue<Event>();

	@Override
	public void run() {
			while(true) {
				try {
					Event event = queue.take();
					event.getTarget().handleEvent(event);
				} catch (InterruptedException e) {
					e.printStackTrace();
					break;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
	}
	
	void addEvent(Event event) {
		queue.add(event); 
	}
}
