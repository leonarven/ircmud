package com.cb2.ircmud.domain.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.cb2.ircmud.domain.World;

@Service
public class WorldService {
	public World findWorldByName(String name) {
		List<World> result = World.findWorldsByNameLike(name).getResultList();
		if (result == null || result.size() == 0) return null;
		return result.get(0);
	}
}
