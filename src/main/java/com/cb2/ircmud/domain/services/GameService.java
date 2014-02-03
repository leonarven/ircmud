package com.cb2.ircmud.domain.services;

import javax.annotation.PostConstruct;
import javax.persistence.Transient;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cb2.ircmud.GameChannel;
import com.cb2.ircmud.domain.Item;
import com.cb2.ircmud.domain.Player;
import com.cb2.ircmud.domain.World;
import com.cb2.ircmud.domain.components.PlayerComponent;
import com.cb2.ircmud.domain.containers.Room;
import com.cb2.ircmud.ircserver.IrcUser;

@Service
public class GameService {
	private GameChannel gameChannel;
	@Autowired
	private WorldService worldService;
	@Autowired
	private ItemService itemService;
	
	@PostConstruct
	private void initialize() {
		gameChannel = new GameChannel("!game");
	}
	
	public GameChannel getGameChannel() { return gameChannel; }
	
	
	@Transactional
	public void joinPlayerToGame(Item character) {
		PlayerComponent playerComponent = character.findFirstComponentInstanceOf(PlayerComponent.class);
		Player player = playerComponent.getPlayer();
		IrcUser ircUser = player.getIrcUser();
		ircUser.joinChannel(gameChannel);
		worldService.addCharacterToGame(character);
		
	}
}
