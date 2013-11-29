package com.cb2.ircmud.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.cb2.ircmud.domain.Player;
import com.cb2.ircmud.ircserver.IrcServer;
import com.cb2.ircmud.ircserver.IrcUser;

@Service
public class PlayerService {
	public Player findPlayerByUsername(String username) {
		List<Player> playerList = Player.findPlayersByUsernameEquals(username.toLowerCase()).getResultList();
		if (playerList.size() == 0) return null;
		assert(playerList.size() == 1);
		return playerList.get(0);
	}
	
	public Player findPlayerByNickname(String nick) {
		IrcUser ircUser = IrcServer.findUserByNickname(nick);
		if (ircUser != null) {
			return ircUser.getPlayer();
		}
		return null;
		
	}
	
	public Player findPlayerByNicknameOrUsername(String usernameOrNickname) {
		Player player = findPlayerByNickname(usernameOrNickname);
		if (player != null) return player;
		
		return findPlayerByUsername(usernameOrNickname);
	}
}
