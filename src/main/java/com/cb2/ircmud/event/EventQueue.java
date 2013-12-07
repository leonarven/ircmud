package com.cb2.ircmud.event;

import java.util.concurrent.BlockingQueue;

public class EventQueue implements Runnable {
	private BlockingQueue<Event> queue;

	@Override
	public void run() {
		try {
			while(true) {
				Event event = queue.take();
				event.trigger();
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	void addEvent(Event event) {
		queue.add(event);
		this.notify();
	}
}
