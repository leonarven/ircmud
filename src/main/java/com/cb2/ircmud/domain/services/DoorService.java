package com.cb2.ircmud.domain.services;

import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cb2.ircmud.domain.Item;
import com.cb2.ircmud.domain.components.DoorComponent;
import com.cb2.ircmud.domain.containers.Room;

@Service
public class DoorService {
	@Autowired
	ItemService itemService;
	
	@Transactional
	public void passThrough(Item item,DoorComponent door){
		if(door.getIsOpen()){
			itemService.transfer(item, door.getTargetRoom());
		}
	}
	
	@Transactional
	public void addToExistingItem(Room to, Item item, boolean isOpen){
		//TODO
		DoorComponent door = new DoorComponent();
		door.setItem(item);
		door.setTargetRoom(to);
		door.setIsOpen(isOpen);
		door.persist();
	}
	
	
	
	public List<DoorComponent> findDoors(Room from){
		return DoorComponent.findDoorComponentsByTargetRoom(from).getResultList();
	}
	public List<DoorComponent> findbyName(Room from, String name){
		//TODO
		return DoorComponent.findDoorComponentsByTargetRoom(from).getResultList();
	}
}
