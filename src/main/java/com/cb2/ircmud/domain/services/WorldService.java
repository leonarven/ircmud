package com.cb2.ircmud.domain.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cb2.ircmud.domain.Item;
import com.cb2.ircmud.domain.World;
import com.cb2.ircmud.domain.components.PlayerComponent;
import com.cb2.ircmud.domain.containers.Room;

@Service
public class WorldService {
	
	@Autowired
	private RoomService roomService;
	@Autowired
	private ItemService itemService;
	
	@Transactional
	public World findWorldByName(String name) {
		List<World> result = World.findWorldsByNameLike(name).getResultList();
		if (result == null || result.size() == 0) return null;
		return result.get(0);
	}
	
	@Transactional
	public void addCharacterToGame(Item character) {
		PlayerComponent playerComponent = (PlayerComponent)itemService.findFirstComponentInstanceOf(character, PlayerComponent.class);
		World world = playerComponent.getWorld();
		
		if (character.getLocation() == null) {
			Room defaultRoom = world.getDefaultRoom();
			roomService.moveCharacterToRoom(character, defaultRoom);
		}
		roomService.moveCharacterToRoom(character, (Room)character.getLocation());
	}
	
	@Transactional
	public World createWorld(String name) {
		if (findWorldByName(name) != null) return null;
		
		World world = new World();
		world.setName(name);
		world.persist();
		return world;
	}
	
	@Transactional
	public Item findCharacterByName(World world, String name) {
		//World world = World.findWorld(worldp.getId());
		return world.getCharacters().get(name.toLowerCase());
	}
	
}
