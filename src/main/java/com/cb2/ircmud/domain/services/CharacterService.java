package com.cb2.ircmud.domain.services;

import java.util.HashSet;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.cb2.ircmud.domain.Item;
import com.cb2.ircmud.domain.World;
import com.cb2.ircmud.domain.components.*;

@Service
public class CharacterService {
	
	public Item createCharacter(World world) {
		Item character = new Item();
		Set<Component> components = createCharacterComponents(world);
		character.setComponents(components);
		
		return character;
	}
	
	private Set<Component> createCharacterComponents(World world) {
		Set<Component> components = new HashSet<Component>();
		PlayerComponent pcomp = new PlayerComponent();
		pcomp.setWorld(world);
		components.add(pcomp);
		components.add(new CharacterComponent());
		components.add(new Damageable());
		return components;
	}
	
	public void pickItem(CharacterComponent gchar, Item item) {
		
	}
}
