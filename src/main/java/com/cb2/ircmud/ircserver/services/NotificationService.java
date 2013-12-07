
package com.cb2.ircmud.ircserver.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cb2.ircmud.ircserver.IrcReply;
import com.cb2.ircmud.ircserver.IrcUser;

@Service
public class NotificationService {
	@Autowired
	UserService userService;
	
	public void sendNoticeToAll(String msg) {
		IrcReply notificationReply = IrcReply.serverReply("NOTICE", msg);
		for (IrcUser user : userService.getIrcUsers()) {
			user.sendReply(notificationReply);
		}
	}
}
