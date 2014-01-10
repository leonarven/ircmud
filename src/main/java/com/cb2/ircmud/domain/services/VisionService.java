package com.cb2.ircmud.domain.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.cb2.ircmud.domain.Item;
import com.cb2.ircmud.domain.components.CharacterComponent;
import com.cb2.ircmud.domain.components.PlayerComponent;
import com.cb2.ircmud.event.Event;
import com.cb2.ircmud.event.LookEvent;
import com.cb2.ircmud.event.VisionEvent;

@Service
public class VisionService {
	public List<Item> itemsInVision(Item eye) {
		//TODO: implement proper vision algorithm
		
		return null;
	}
	
	public LookEvent lookAtItem(Item item, Item eye) {
		LookEvent e = new LookEvent(eye, item);
		eye.handleEvent(e);
		return e;
	}
	
	public String characterLookEventResultMessage(Item character, LookEvent e) {
		//CharacterComponent characterComponent = (CharacterComponent)character.findFirstComponentInstanceOf(CharacterComponent.class);
		//PlayerComponent playerComponent = (PlayerComponent)character.findFirstComponentInstanceOf(PlayerComponent.class);
		String result = "You see";
		int itemCount = 0;
		for (Event ce : e.getChildEvents()) {
			VisionEvent ve = (VisionEvent)ce;
			result +=  " " + ve.getItemInVision().getName();
			itemCount++;
		}
		if (itemCount == 0) {
			result = "You see nothing interesting";
		}
		
		return result;
	}
}
