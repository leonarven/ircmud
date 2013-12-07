package com.cb2.ircmud.event;

import java.util.PriorityQueue;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


public class TimedEventQueue implements Runnable {
	private PriorityQueue<TimedEvent> queue = new PriorityQueue<TimedEvent>();
	private Lock queueLock = new ReentrantLock();

	@Override
	public void run() {
		try {
			while(true) {
				queueLock.lock();
				if (!queue.isEmpty()) {
					long waitTime = queue.peek().getTriggerTime().getTime() - System.currentTimeMillis();
					if (waitTime <= 0) {
						TimedEvent event = queue.poll();
						queueLock.unlock();
						event.getTarget().handleEvent(event);
					} else {
						queueLock.unlock();
						this.wait(waitTime);
					}
				} else {
					queueLock.unlock();
					this.wait();
				}
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	void addEvent(TimedEvent event) {
		queueLock.lock();
		queue.add(event);
		queueLock.unlock();
		this.notify();
	}
}
