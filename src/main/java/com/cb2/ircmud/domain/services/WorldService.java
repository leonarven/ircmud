package com.cb2.ircmud.domain.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cb2.ircmud.domain.Item;
import com.cb2.ircmud.domain.World;
import com.cb2.ircmud.domain.components.CharacterComponent;
import com.cb2.ircmud.domain.components.PlayerComponent;
import com.cb2.ircmud.domain.containers.Room;

@Service
public class WorldService {
	@Autowired
	RoomService roomService;
	
	public World findWorldByName(String name) {
		List<World> result = World.findWorldsByNameLike(name).getResultList();
		if (result == null || result.size() == 0) return null;
		return result.get(0);
	}
	
	public void addCharacterToGame(Item character) {
		PlayerComponent playerComponent = (PlayerComponent)character.findFirstComponentInstanceOf(PlayerComponent.class);
		World world = playerComponent.getWorld();
		if (character.getLocation() == null) {
			Room defaultRoom = world.getDefaultRoom();
			roomService.moveCharacterToRoom(character, defaultRoom);
		}
		roomService.moveCharacterToRoom(character, (Room)character.getLocation());
	}
	
	public World createWorld(String name) {
		if (findWorldByName(name) != null) return null;
		
		World world = new World();
		world.setName(name);
		world.persist();
		return world;
	}
}
