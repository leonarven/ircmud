package com.cb2.ircmud.event;

import java.util.PriorityQueue;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;


@Configurable
public class TimedEventQueue implements Runnable {
	private PriorityQueue<TimedEvent> queue = new PriorityQueue<TimedEvent>();
	private Lock queueLock = new ReentrantLock();

	
	@Autowired
	private EventService eventService;
	
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
						eventService.addEvent(event);
					} else {
						queueLock.unlock();
						this.wait(waitTime);
					}
				} else {
					queueLock.unlock();
					synchronized(this) {
						this.wait();
					}
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
		synchronized(this) {
			this.notify();
		}
	}
}
