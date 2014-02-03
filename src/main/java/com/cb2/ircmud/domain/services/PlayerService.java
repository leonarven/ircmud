package com.cb2.ircmud.domain.services;

import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cb2.ircmud.PlayerGameState;
import com.cb2.ircmud.PlayerState;
import com.cb2.ircmud.domain.Player;
import com.cb2.ircmud.domain.components.CharacterComponent;
import com.cb2.ircmud.ircserver.IrcServer;
import com.cb2.ircmud.ircserver.IrcUser;
import com.cb2.ircmud.ircserver.services.UserService;

@Service
public class PlayerService {
	
	@Autowired 
	IrcServer server;
	@Autowired 
	UserService users;
	
	@Transactional
	public Player findPlayerByUsername(String username) {
		List<Player> playerList = Player.findPlayersByUsernameEquals(username.toLowerCase()).getResultList();
		if (playerList.size() == 0) return null;
		assert(playerList.size() == 1);
		return playerList.get(0);
	}
	
	@Transactional
	public Player findPlayerByNickname(String nick) {
		IrcUser ircUser = users.findUserByNickname(nick);
		if (ircUser != null) {
			return Player.findPlayer(ircUser.getPlayerId());
		}
		return null;
		
	}
	@Transactional
	public Player findPlayerByNicknameOrUsername(String usernameOrNickname) {
		Player player = findPlayerByNickname(usernameOrNickname);
		if (player != null) return player;
		
		return findPlayerByUsername(usernameOrNickname);
	}
	
	@Transactional
	public PlayerGameState getPlayerGameState(Player player) {
    	for (Iterator<PlayerState> i = player.getState().iterator(); i.hasNext();) {
    		PlayerState s = i.next();
    		if (s instanceof PlayerGameState) {
    			return (PlayerGameState)s;
    		}
    	}
    	return null;
    }
    
    public boolean hasPlayerStateWithGroup(Player player, int group) {
    	return getPlayerStateByGroup(player, group) != null;
    }
    
	@Transactional
    public PlayerState getPlayerStateByGroup(Player player, int group) {
    	for (Iterator<PlayerState> i = player.getState().iterator(); i.hasNext();) {
    		PlayerState s = i.next();
    		if (s.getStateGroup() == group) {
    			return s;
    		}
    	}
    	return null;
    }
    
    @Transactional
    public List<String> getPlayerCharacterNames(Player player) {
    	Vector<String> names = new Vector<String>();
    	for (CharacterComponent c : player.getCharacters()) {
    		names.add(c.getItem().getName());
    	}
    	return names;
    }
    
    public boolean isPlayerInGame(Player player) {
    	return getPlayerGameState(player) != null;
    }
}
