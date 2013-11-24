package com.cb2.ircmud.services;

import java.util.List;

import javax.persistence.TypedQuery;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cb2.ircmud.domain.Item;
import com.cb2.ircmud.domain.components.Door;
import com.cb2.ircmud.domain.containers.Room;

@Service
public class DoorService {
	@Autowired
	ItemService itemService;
	
	@Transactional
	public void passThrough(Item item,Door door){
		if(door.getIsOpen()){
			itemService.transfer(item, door.getTargetRoom());
		}
	}
	
	@Transactional
	public void addToExistingItem(Room to, Item item, boolean isOpen){
		//TODO
		Door door = new Door();
		door.setItem(item);
		door.setTargetRoom(to);
		door.setIsOpen(isOpen);
		door.persist();
	}
	
	@Transactional
	public void create(Room from, Room to, String name, String description, boolean isOpen){
		//TODO
		addToExistingItem(to,itemService.create(name,description,from),isOpen);
	}
	
	
	public List<Door> find(Room from){
		return Door.findDoorsByTargetRoom(from).getResultList();
	}
	public List<Door> findbyName(Room from, String name){
		//TODO
		return Door.findDoorsByTargetRoom(from).getResultList();
	}
}
