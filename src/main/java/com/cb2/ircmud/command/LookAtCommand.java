package com.cb2.ircmud.command;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;

import com.cb2.ircmud.communication.CommunicationService;
import com.cb2.ircmud.domain.Item;
import com.cb2.ircmud.domain.containers.Container;
import com.cb2.ircmud.domain.services.ContainerService;
import com.cb2.ircmud.domain.services.ItemService;
import com.cb2.ircmud.domain.services.VisionService;

@Configurable	
public class LookAtCommand extends CommandDefinition {
	@Autowired
	ItemService itemService;
	@Autowired
	VisionService visionService;
	@Autowired
	CommunicationService communicationService;
	
	public LookAtCommand() {
		super("lookAt", "looks?\\s+(at\\s+)?");
		setParameterType(CommandParameter.Type.Item);
	}

	@Override
	public void act(List<CommandParameter> parameterList, Item sender) {
		String ret = "";
		for (CommandParameter p : parameterList) {
			ItemParameter ip = (ItemParameter)p;
			String itemName = ip.getItemName();
			List<Item> items = visionService.findItemsInVisionByName(sender, itemName);
			if (items.size() == 0) {
				communicationService.sendErrorMessageToCharacter(sender, "Can't find " + ip.getItemNameWithArticle());
				return;
			}
			else {
				for (Item i : items) {
					ret += i.getDescription() + " ";
				}
			}
		}
		
		communicationService.sendStoryMessageToCharacter(sender, ret);
		
	}
	

}
