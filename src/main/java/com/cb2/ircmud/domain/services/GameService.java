package com.cb2.ircmud.domain.services;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cb2.ircmud.GameChannel;
import com.cb2.ircmud.domain.Item;
import com.cb2.ircmud.domain.Player;
import com.cb2.ircmud.domain.World;
import com.cb2.ircmud.domain.components.PlayerComponent;
import com.cb2.ircmud.domain.containers.Room;

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
	
	
	public void joinPlayerToGame(Item character) {
		PlayerComponent playerComponent = (PlayerComponent)itemService.findFirstComponentInstanceOf(character, PlayerComponent.class);
		Player player = playerComponent.getPlayer();
		player.getIrcUser().joinChannel(gameChannel);
		worldService.addCharacterToGame(character);
		
	}
}
