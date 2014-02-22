package com.cb2.ircmud.event;

import java.util.Comparator;
import java.util.Date;

public class TimedEvent implements Comparator<TimedEvent>, Comparable<TimedEvent> {

	private final Date emitTime;
	private final Event containedEvent;
	
	public TimedEvent(Date emitTime, Event containedEvent) {
		this.emitTime = emitTime;
		this.containedEvent = containedEvent;
	}

	public Date getEmitTime() { return emitTime; }
	public Event getContainedEvent() { return containedEvent; } 
	
	@Override
	public int compareTo(TimedEvent o) {
		return this.emitTime.compareTo(o.emitTime);
	}

	@Override
	public int compare(TimedEvent o1, TimedEvent o2) {
		return o1.emitTime.compareTo(o2.emitTime);
	}
}
