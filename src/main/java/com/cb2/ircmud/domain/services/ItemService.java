package com.cb2.ircmud.domain.services;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cb2.ircmud.domain.Item;
import com.cb2.ircmud.domain.Name;
import com.cb2.ircmud.domain.Vec3;
import com.cb2.ircmud.domain.components.Component;
import com.cb2.ircmud.domain.components.Pickable;
import com.cb2.ircmud.domain.components.SizeComponent;
import com.cb2.ircmud.domain.containers.Container;

@Service
public class ItemService {
	@Transactional
	public void transfer(Item item, Container to) {
		Container oldLocation = item.getLocation();
		if (oldLocation != null) {
			oldLocation.removeItem(item);
		}
		
		item.setLocation(to);
		to.addItem(item);
	}
	
	public Item clone(Item item) {
		Set<Component> components = item.getComponents();
		
		Item clonedItem = new Item();
		clonedItem.setDescription(item.getDescription());
		clonedItem.setName(item.getName());
		clonedItem.setHiddenName(item.getHiddenName());
		for (Component c : components) {
			clonedItem.addComponent(c.cloneComponent());
		}	
		return clonedItem;
	}

	public Item createEmptyItem(Name name, String description) {
		Item item = new Item();
		item.setName(name);
		item.setDescription(description);
		item.persist();
		return item;
	}
	
	@Transactional(readOnly = true)
	public boolean itemHasComponentInstanceOf(Item item, Class<?> cl) {
		for (Component c : item.getComponents()) {
			if (cl.isInstance(c)) {
				return true;
			}
		}
		return false;
	}
	
	@Transactional(readOnly = true)
	public Component findFirstComponentInstanceOf(Item item, Class<?> cl) {
		for (Component c : item.getComponents()) {
			if (cl.isInstance(c)) {
				return c;
			}
		}
		return null;
	}
	
	@Transactional(readOnly = true)
	public ArrayList<Component> findComponentsInstanceOf(Item item, Class<?> cl) {
		ArrayList<Component> result = new ArrayList<Component>();
		for (Component c : item.getComponents()) {
			if (cl.isInstance(c)) {
				result.add(c);
			}
		}
		return result;
	}
	
	public Item createPickableItemWithSize(Name name, String description, Vec3 size, double weight) {
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
