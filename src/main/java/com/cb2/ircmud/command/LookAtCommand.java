package com.cb2.ircmud.command;

import java.util.List;

import org.springframework.beans.factory.annotation.Configurable;

import com.cb2.ircmud.domain.Item;

@Configurable	
public class LookAtCommand extends CommandDefinition {

	public LookAtCommand() {
		super("lookAt", "looks?\\s+(at\\s+)?");
		setParameterType(CommandParameter.Type.Item);
	}

	@Override
	public void act(List<CommandParameter> parameterList, Item sender) {
		
		
	}
	

}
