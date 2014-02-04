package com.cb2.ircmud.command;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.cb2.ircmud.communication.CommandService;
import com.cb2.ircmud.communication.CommunicationService;
import com.cb2.ircmud.domain.Item;
import com.cb2.ircmud.domain.containers.Container;
import com.cb2.ircmud.domain.services.ItemService;
import com.cb2.ircmud.domain.services.VisionService;

@Configurable
public class LookAroundCommand extends CommandDefinition {
	@Autowired
	ItemService itemService;
	@Autowired
	VisionService visionService;
	@Autowired
	CommunicationService communicationService;
	
	public LookAroundCommand() {
		super("lookAround", "looks?\\s+around");
		setParameterType(CommandParameter.Type.None);
	}
	
	@Override
	@Transactional
	public void act(List<CommandParameter> parameterList, Item sender) {
		List<Item> itemsInVision = visionService.lookAround(sender);
		Container location = sender.getLocation();
		String message = location.getDescription();
		if (itemsInVision.isEmpty()) {
			message += " You see nothing else.";
		}
		else {
			message += " You see ";
			if (itemsInVision.size() == 1) {
				message += communicationService.generateItemList(itemsInVision) + ".";
			}
			else {
				message += "a few items: " + communicationService.generateItemList(itemsInVision) + ".";
			}
		}
		
		communicationService.sendStoryMessageToCharacter(sender, message);
	}
}
