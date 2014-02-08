package com.cb2.ircmud.domain.services;

import java.util.List;
import java.util.Vector;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cb2.ircmud.domain.Item;
import com.cb2.ircmud.domain.containers.Container;

@Service
public class ContainerService {
	
	public void removeItemFromContainer(Item item) {
		Container container = item.getLocation();
		if (container != null) {
			container.removeItem(item);
			item.setLocation(null);
		}
	}
	
	public void moveItemToContainer(Item item, Container container) {
		removeItemFromContainer(item);
		container.addItem(item);
		item.setLocation(container);
	}
	
	@Transactional
	public List<Item> findItemsByName(Container container, String name) {
		container = container.refleshSession();
		List<Item> ret = new Vector<Item>();
		for (Item i : container.getItems()) {
			if (i.getName().getName().equalsIgnoreCase(name) ||	 i.getName().getPluralForm().equalsIgnoreCase(name)) {
				ret.add(i);
			}
		}
		
		return ret;
	}
}
