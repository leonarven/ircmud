package com.cb2.ircmud.command;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.cb2.ircmud.communication.CommunicationService;
import com.cb2.ircmud.domain.Item;
import com.cb2.ircmud.domain.services.ItemService;
import com.cb2.ircmud.domain.services.VisionService;

public class WalkToCommand extends CommandDefinition {
	@Autowired
	ItemService itemService;
	@Autowired
	VisionService visionService;
	@Autowired
	CommunicationService communicationService;
	
	public WalkToCommand() {
		super("walkTo", "walks?\\s+((to)|(towards)\\s+)?");
		setParameterType(CommandParameter.Type.Location);
	}

	@Override
	public void act(List<CommandParameter> parameterList, Item sender) {
		String ret = "";
		LocationParameter param = (LocationParameter)parameterList.get(0);
		String location = param.getLocation();
		
		communicationService.sendStoryMessageToCharacter(sender, ret);
		
	}
	
}
