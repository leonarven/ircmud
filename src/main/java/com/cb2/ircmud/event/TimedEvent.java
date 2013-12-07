package com.cb2.ircmud.event;

import java.util.Comparator;
import java.util.Date;

public abstract class TimedEvent extends Event implements Comparator<TimedEvent>, Comparable<TimedEvent> {

	protected final Date triggerTime;
	
	public TimedEvent(Type type, Object sender, EventListener target, Date emitTime) {
		super(type, sender, target);
		this.triggerTime = emitTime;
	}

	Date getTriggerTime() { return triggerTime; }
	
	@Override
	public int compareTo(TimedEvent o) {
		return this.triggerTime.compareTo(o.triggerTime);
	}

	@Override
	public int compare(TimedEvent o1, TimedEvent o2) {
		return o1.triggerTime.compareTo(o2.triggerTime);
	}
}
