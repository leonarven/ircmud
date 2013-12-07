package com.cb2.ircmud.event;

import java.util.Date;

import com.cb2.ircmud.ircserver.services.NotificationService;

public class TimedServerNotificationEvent extends TimedEvent {

	protected final String message;
	protected final NotificationService notificationService;
	
	public TimedServerNotificationEvent(Object sender, Date emitTime, String msg, NotificationService notificationService) {
		super(Event.Type.ServerNotification, sender, emitTime);
		this.message = msg;
		this.notificationService = notificationService;
	}
	
	public String getMessage() { return message; }

	@Override
	public void trigger() {
		notificationService.sendNoticeToAll(message);
	}
}
