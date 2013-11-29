package com.cb2.ircmud.ircserver;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cb2.ircmud.Console;
import com.cb2.ircmud.domain.Player;

@Component
public class AuthService {
	
	@Autowired
	Console logger;
	
	public enum AuthError {
		ERR_OK,
		ERR_INVALIDLOGIN,
		ERR_ALREADYLOGGED,
	}
	
	private MessageDigest digestInstance = null;
	
	public void init() {
		try {
			digestInstance = MessageDigest.getInstance("MD5");
		} catch(NoSuchAlgorithmException e) {
			logger.err("AuthService", "NoSuchAlgorithmException: "+e.getMessage());
		}
	}
	
	public boolean addAccount(String username, String password) {
		return addAccount(username, password, Player.ACCESS_NORMAL);
	}
	
	public boolean addAccount(String username, String password, int access) {
		username = username.toLowerCase();
		List<Player> playerList = Player.findPlayersByUsernameEquals(username).getResultList();
		if (!playerList.isEmpty()) { //Already created an account with the same user name
			return false;
		}
		String passwordHash = new String(digestInstance.digest(password.getBytes()));
		Player player = new Player();
		player.setUsername(username);
		player.setPasswordHash(passwordHash);
		player.setAccess(access);
		player.persist();
		return true;
	}

	public Player login(String username, String password, IrcUser user) {
		username = username.toLowerCase();
		List<Player> playerList = Player.findPlayersByUsernameEquals(username).getResultList();
		if (playerList.isEmpty()) { //Already created an account with the same user name
			return null;
		}
		String passwordHash = new String(digestInstance.digest(password.getBytes()));
		
		Player player = playerList.get(0);
		if (!player.getPasswordHash().equals(passwordHash)) {
			return null;
		}
		if (player.getIrcUser() != null) { //Someone has already logged in with the same account
			logout(player);
		}
		player.setIrcUser(user);
		user.setPlayer(player);
		return player;
	}
	
	public void logout(Player player) {
		player.getIrcUser().setPlayer(null);
		player.setIrcUser(null);
	}
}
