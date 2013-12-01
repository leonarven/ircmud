package com.cb2.ircmud.domain.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cb2.ircmud.domain.Item;
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

	public Item create(String name, String description, Container location) {
		// TODO Auto-generated method stub
		Item item = new Item();
		item.setLocation(location);
		item.setName(name);
		item.setDescription(description);
		item.persist();
		return item;
	}

}
