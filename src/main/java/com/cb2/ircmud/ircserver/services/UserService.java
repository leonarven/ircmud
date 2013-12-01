package com.cb2.ircmud.ircserver.services;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cb2.ircmud.ircserver.Connection;
import com.cb2.ircmud.ircserver.IrcUser;
import com.cb2.ircmud.ircserver.PingService;
import com.github.rlespinasse.slf4j.spring.AutowiredLogger;

@Service
public class UserService {
	@Autowired
	PingService pingService;
	@AutowiredLogger
	Logger logger;
	
	private Map<String, IrcUser> userNicknameMap = new HashMap<String, IrcUser>();
	
	public boolean trySetNickname(IrcUser user, String nick) {
		nick = nick.toLowerCase();
		synchronized (userNicknameMap) {
			if (userNicknameMap.containsKey(nick)) return false;
			userNicknameMap.put(nick, user);
		}
		return true;
	}
	
	public boolean trySetNickname(IrcUser user, String newNick, String oldNick) {
		newNick = newNick.toLowerCase();
		oldNick = oldNick.toLowerCase();
		synchronized (userNicknameMap) {
			if (userNicknameMap.containsKey(newNick)) return false;
			if (userNicknameMap.containsKey(oldNick)) {
				userNicknameMap.remove(oldNick);
			}
			userNicknameMap.put(newNick, user);
		}
		return true;
	}
	
	
	public IrcUser findUserByNickname(String nickName) {
		nickName = nickName.toLowerCase();
		synchronized (userNicknameMap) {
			if (!userNicknameMap.containsKey(nickName)) return null;
			return userNicknameMap.get(nickName);
		}
	}
	
	public void dropUser(String nickName) {
		if (nickName == null) return;
		logger.info("dropUser({})",nickName);
		nickName = nickName.toLowerCase();
		synchronized (userNicknameMap) {
			if (!userNicknameMap.containsKey(nickName)) return;
			IrcUser u = userNicknameMap.get(nickName);
			userNicknameMap.remove(nickName);
			
			if (u instanceof Connection)
				pingService.dropPartner((Connection)u);
		}
	}
	public void dropConnection(Connection con) {
		logger.info("dropConnection({})",con.getRepresentation());
		String nickName = con.getNickname().toLowerCase();
		synchronized (userNicknameMap) {
			if (!userNicknameMap.containsKey(nickName)) return;

			userNicknameMap.remove(nickName);
			pingService.dropPartner(con);
		}
	}

}
