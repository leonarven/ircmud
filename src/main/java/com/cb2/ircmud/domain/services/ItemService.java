package com.cb2.ircmud.domain.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cb2.ircmud.domain.Item;
import com.cb2.ircmud.domain.Vec3;
import com.cb2.ircmud.domain.components.Pickable;
import com.cb2.ircmud.domain.components.SizeComponent;
import com.cb2.ircmud.domain.containers.Container;

@Service
public class ItemService {
	@Transactional
	public void transfer(Item item, Container to) {
		item.setLocation(to);
	}
	
	public void clone(Item item, Container to) {
		//TODO
	}

	public Item createEmptyItem(String name, String description) {
		Item item = new Item();
		item.setName(name);
		item.setDescription(description);
		item.persist();
		return item;
	}
	
	public Item createPickableItemWithSize(String name, String description, Vec3 size, double weight) {
		Item item = new Item();
		item.setName(name);
		item.setDescription(description);
		
		SizeComponent sizeComp = new SizeComponent();
		sizeComp.setSize(size);
		sizeComp.setWeight(weight);
		item.addComponent(sizeComp);
		Pickable pickable = new Pickable();
		item.addComponent(pickable);
		
		item.persist();
		return item;
	}

}
