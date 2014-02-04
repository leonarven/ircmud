package com.cb2.ircmud.domain.services;

import java.util.List;
import java.util.Vector;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cb2.ircmud.domain.Item;
import com.cb2.ircmud.domain.World;
import com.cb2.ircmud.domain.components.CharacterComponent;
import com.cb2.ircmud.domain.components.PlayerComponent;
import com.cb2.ircmud.domain.containers.Room;

@Service
public class RoomService {
	@Autowired
	private ContainerService containerService;
	public void moveCharacterToRoom(Item character, Room room) {
		containerService.moveItemToContainer(character, room);
	}
	
	public List<Item> search(String name) {
		//TODO
		return null;
	}
	
	@Transactional
	public List<Item> findPlayerCharactersInRoom(Room room) {
		List<Item> r = new Vector<Item>();
		List<Item> items = room.getItems();
		for (Item i : items) {
			if (i.findFirstComponentInstanceOf(PlayerComponent.class) != null) {
				r.add(i);
			}
		}
		return r;
	}
	
	public Room createRoom(World world, String name) {
		Room room = new Room();
		room.setWorld(world);
		room.setName(name);
		world.addRoom(room);
		
		room.persist();
		return room;
	}
}
