package com.cb2.ircmud.domain.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.cb2.ircmud.domain.Item;
import com.cb2.ircmud.domain.containers.Container;

@Service
public class SoundService {
	public enum SoundLevel {
		Whisper,
		Normal,
		Shout
	}
	public List<Item> listItemsWhichCanHear(Item source, SoundLevel level) {
		//TODO: Implement
		Container container = source.getLocation();
		return container.getItems();
	}
}
