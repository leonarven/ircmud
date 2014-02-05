package com.cb2.ircmud.tests;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cb2.ircmud.domain.Item;
import com.cb2.ircmud.domain.Name;
import com.cb2.ircmud.domain.Player;
import com.cb2.ircmud.domain.Vec3;
import com.cb2.ircmud.domain.World;
import com.cb2.ircmud.domain.containers.Room;
import com.cb2.ircmud.domain.services.CharacterService;
import com.cb2.ircmud.domain.services.ItemService;
import com.cb2.ircmud.domain.services.PlayerService;
import com.cb2.ircmud.domain.services.RoomService;
import com.cb2.ircmud.domain.services.WorldService;
import com.cb2.ircmud.ircserver.services.AuthService;

@Service
public class TestDataGenerationService {
	@Autowired
	WorldService worldService;
	@Autowired
	RoomService roomService;
	@Autowired
	ItemService itemService;
	@Autowired
	PlayerService playerService;
	@Autowired
	CharacterService characterService;
	@Autowired
	AuthService authService;
	
	@Transactional
	public void generateTestWorld() {
		World world = worldService.createWorld("world");
		world.setDefaultRoom(generateLonelyBeach(world));
		
		Player testPlayer = generateTestPlayer();
		Item testCharacter = generateTestCharacter(testPlayer, world);
		
	}

	@Transactional
	Player generateTestPlayer() {
		Player player = authService.addAccount("test", "pass");
		return player;
	}
	
	@Transactional
	Item generateTestCharacter(Player player, World world) {
		Item character = characterService.createPlayerCharacter(player, "tester", world);
		return character;
	}
		
	@Transactional
	Room generateLonelyBeach(World world) {
		Room room = roomService.createRoom(world, "Lonely beach");
		room.setDescription("You stand on a small lonely beach. You can see a few shipwrecks in the east.");
		
		room.addItem(generatePlank());
		room.addItem(generateStick());
		return room;
	}
	
	@Transactional
	public Item generatePlank() {
		Item plank = itemService.createPickableItemWithSize(new Name("plank"), "A dry plank", new Vec3(1.2, 0.05, 0.3), 1.1);
		return plank;
	}
	
	@Transactional
	public Item generateStick() {
		Item plank = itemService.createPickableItemWithSize(new Name("stick"), "A stick", new Vec3(0.9, 0.02, 0.02), 1.1);
		return plank;
	}
}
