package com.cb2.ircmud.domain.services;

import org.springframework.stereotype.Service;

import com.cb2.ircmud.domain.Item;
import com.cb2.ircmud.domain.containers.Container;

@Service
public class ContainerService {
	
	void removeItemFromContainer(Item item) {
		Container container = item.getLocation();
		if (container != null) {
			container.removeItem(item);
			item.setLocation(null);
		}
	}
	
	void moveItemToContainer(Item item, Container container) {
		removeItemFromContainer(item);
		container.addItem(item);
		item.setLocation(container);
	}
}
