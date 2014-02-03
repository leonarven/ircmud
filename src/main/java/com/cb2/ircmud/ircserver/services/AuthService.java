package com.cb2.ircmud.ircserver.services;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.springframework.stereotype.Component;

import com.cb2.ircmud.domain.Player;
import com.cb2.ircmud.ircserver.IrcUser;
import com.github.rlespinasse.slf4j.spring.AutowiredLogger;

@Component
public class AuthService {
	
	@AutowiredLogger
	Logger logger;
	
	public enum AuthError {
		ERR_OK,
		ERR_INVALIDLOGIN,
		ERR_ALREADYLOGGED,
	}
	
	private MessageDigest digestInstance = null;
	
	@PostConstruct
	public void init() {
		logger.info("Initializing AuthService");
		try {
			digestInstance = MessageDigest.getInstance("MD5");
		} catch(NoSuchAlgorithmException e) {
			logger.error("NoSuchAlgorithmException: {}", e.getMessage());
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
			logout(player.getIrcUser());
		}
		player.setIrcUser(user);
		user.setPlayer(player);
		return player;
	}
	
	public void logout(IrcUser ircUser) {
		Player.removePlayerTransientData(ircUser.getPlayerId());
		ircUser.setPlayerId(0);
	}
}
