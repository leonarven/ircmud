package com.cb2.ircmud.event;

import java.util.concurrent.PriorityBlockingQueue;


public class TimedEventQueue implements Runnable {
	private PriorityBlockingQueue<TimedEvent> queue;

	@Override
	public void run() {
		try {
			while(true) {
				if (!queue.isEmpty()) {
					long waitTime = queue.peek().getTriggerTime().getTime() - System.currentTimeMillis();
					if (waitTime <= 0) {
						TimedEvent event = queue.take();
						event.trigger();
					} else {
						this.wait(waitTime);
					}
				} else {
					this.wait();
				}
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	void addEvent(TimedEvent event) {
		queue.add(event);
		this.notify();
	}
}
