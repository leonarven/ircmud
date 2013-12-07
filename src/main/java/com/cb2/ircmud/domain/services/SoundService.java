package com.cb2.ircmud.domain.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.cb2.ircmud.domain.Item;

@Service
public class SoundService {
	public enum SoundLevel {
		Whisper,
		Normal,
		Shout
	}
	public List<Item> listItemsWhichCanHear(Item source, SoundLevel level) {
		
		return null;
	}
}
