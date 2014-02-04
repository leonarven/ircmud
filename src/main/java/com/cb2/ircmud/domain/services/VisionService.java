package com.cb2.ircmud.domain.services;

import java.util.List;
import java.util.Vector;

import org.springframework.stereotype.Service;

import com.cb2.ircmud.domain.Item;
import com.cb2.ircmud.domain.components.CharacterComponent;
import com.cb2.ircmud.domain.components.PlayerComponent;
import com.cb2.ircmud.domain.containers.Container;
import com.cb2.ircmud.event.Event;
import com.cb2.ircmud.event.LookEvent;
import com.cb2.ircmud.event.VisionEvent;

@Service
public class VisionService {
	public List<Item> lookAround(Item eye) {
		Container container = eye.getLocation();
		List<Item> itemsInVision = itemsInVision(new LookEvent(eye, container));
		return itemsInVision;
	}

	private List<Item> itemsInVision(LookEvent e) {
		e.getTarget().handleEvent(e);
		return extractItemsLookEventVisionEvents(e);
	}
	
	private List<Item> extractItemsLookEventVisionEvents(LookEvent e) {
		List<Item> itemsInVision = new Vector<Item>();
		for (Event ce : e.getChildEvents()) {
			VisionEvent ve = (VisionEvent)ce;
			itemsInVision.add(ve.getItemInVision());
		}
		return itemsInVision;
	}
	
	public LookEvent lookAtItem(Item item, Item eye) {
		LookEvent e = new LookEvent(eye, item);
		item.handleEvent(e);
		return e;
	}
}
