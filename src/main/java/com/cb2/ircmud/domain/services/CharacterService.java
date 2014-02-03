package com.cb2.ircmud.domain.services;

import java.util.HashSet;
import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cb2.ircmud.domain.Item;
import com.cb2.ircmud.domain.Player;
import com.cb2.ircmud.domain.Vec3;
import com.cb2.ircmud.domain.World;
import com.cb2.ircmud.domain.components.*;

@Service
public class CharacterService {
	
	@Transactional
	public Item createPlayerCharacter(Player player, World world) {
		Item character = createCharacter();
		PlayerComponent playerComponent = new PlayerComponent();
		playerComponent.setWorld(world);
		playerComponent.setPlayer(player);
		playerComponent.persist();
		character.addComponent(playerComponent);
		return character;
	}
	
	@Transactional
	public Item createCharacter() {
		Item character = new Item();
		Set<Component> components = createCharacterComponents();
		for (Component c : components) c.persist();
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
		components.add(sizeComp);
		return components;
	}
	
	public void pickItem(CharacterComponent gchar, Item item) {
		
	}
}
