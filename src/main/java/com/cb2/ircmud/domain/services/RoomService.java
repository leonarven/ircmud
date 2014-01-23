package com.cb2.ircmud.domain.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cb2.ircmud.domain.Item;
import com.cb2.ircmud.domain.World;
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
	
	public Room createRoom(World world, String name) {
		Room room = new Room();
		room.setWorld(world);
		room.setName(name);
		world.addRoom(room);
		
		room.persist();
		return room;
	}
}
