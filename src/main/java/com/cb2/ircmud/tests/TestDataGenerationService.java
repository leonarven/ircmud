package com.cb2.ircmud.tests;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cb2.ircmud.domain.Item;
import com.cb2.ircmud.domain.Name;
import com.cb2.ircmud.domain.Vec3;
import com.cb2.ircmud.domain.World;
import com.cb2.ircmud.domain.containers.Room;
import com.cb2.ircmud.domain.services.ItemService;
import com.cb2.ircmud.domain.services.RoomService;
import com.cb2.ircmud.domain.services.WorldService;

@Service
public class TestDataGenerationService {
	@Autowired
	WorldService worldService;
	@Autowired
	RoomService roomService;
	@Autowired
	ItemService itemService;
	
	@Transactional
	public void generateTestWorld() {
		World world = worldService.createWorld("world");
		Room startingRoom = roomService.createRoom(world, "Lonely beach");
		startingRoom.setDescription("You stand on a small lonely beach. You can see a few shipwrecks in the east.");
		
		startingRoom.addItem(generatePlank());
		world.setDefaultRoom(startingRoom);
	}
		
		
	
	
	@Transactional
	public Item generatePlank() {
		Item plank = itemService.createPickableItemWithSize(new Name("plank"), "A dry plank", new Vec3(1.2, 0.05, 0.3), 1.1);
		return plank;
	}
}
