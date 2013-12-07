package com.cb2.ircmud.event;

import java.util.Date;

import com.cb2.ircmud.ircserver.services.NotificationService;

public class TimedServerNotificationEvent extends TimedEvent {

	protected final String message;
	
	public TimedServerNotificationEvent(NotificationService notificationService, Date emitTime, String msg) {
		this(notificationService, emitTime, msg, notificationService);
	}
	
	public TimedServerNotificationEvent(Object sender, Date emitTime, String msg, NotificationService notificationService) {
		super(Event.Type.ServerNotification, sender, notificationService, emitTime);
		this.message = msg;
	}
	
	public String getMessage() { return message; }
}
