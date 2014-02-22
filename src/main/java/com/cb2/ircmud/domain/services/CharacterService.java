package com.cb2.ircmud.domain.services;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cb2.ircmud.MovementType;
import com.cb2.ircmud.domain.Item;
import com.cb2.ircmud.domain.LocationSpecifier;
import com.cb2.ircmud.domain.Name;
import com.cb2.ircmud.domain.Player;
import com.cb2.ircmud.domain.Vec3;
import com.cb2.ircmud.domain.World;
import com.cb2.ircmud.domain.components.*;
import com.cb2.ircmud.domain.containers.Container;
import com.cb2.ircmud.event.EventService;
import com.cb2.ircmud.event.MovementEndEvent;
import com.cb2.ircmud.event.MovementStartEvent;

@Service
public class CharacterService {
	@Autowired
	EventService eventService;
	
	@Transactional
	public Item createPlayerCharacter(Player player, String charName, World world) {
		Item character = createCharacter(charName);
		PlayerComponent playerComponent = new PlayerComponent();
		playerComponent.setWorld(world);
		playerComponent.setPlayer(player);
		playerComponent.persist();
		player.addCharacter(character);
		character.addComponent(playerComponent);
		return character;
	}
	
	@Transactional
	public Item createCharacter(String name) {
		Item character = new Item();
		Set<Component> components = createCharacterComponents();
		for (Component c : components) c.persist();
		character.setName(Name.createCharacterName(name));
		character.setComponents(components);
		character.persist();
		return character;
	}
	
	@Transactional
	private Set<Component> createCharacterComponents() {
		Set<Component> components = new HashSet<Component>();
		SizeComponent sizeComp = new SizeComponent();
		sizeComp.setSize(new Vec3(0.5, 1.79, 0.2));
		sizeComp.setWeight(75);
		components.add(new CharacterComponent());
		components.add(new Damageable());
		components.add(new VisionComponent());
		components.add(sizeComp);
		return components;
	}
	
	public void startMovingTo(Item character, LocationSpecifier target, MovementType movementType, Date arrivalTime) {
		Container c = character.getLocation();
		MovementStartEvent start = new MovementStartEvent(character, target, movementType, c);
		MovementEndEvent end = new MovementEndEvent(character, start, c);
		eventService.addEvent(start);
		eventService.addTimedEvent(end, arrivalTime);
	}
	
	public void pickItem(Item character, Item item) {
		
	}
}
