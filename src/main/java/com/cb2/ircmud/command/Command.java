package com.cb2.ircmud.command;


import java.util.Vector;

import com.cb2.ircmud.command.CommandDefinition;
import com.cb2.ircmud.domain.Item;

public class Command {
	private CommandDefinition definition;
	private Vector<CommandParameter> parameters = new Vector<CommandParameter>();
	public Command(CommandDefinition def) {
		definition = def;
	}
	
	public CommandDefinition getCommandDefinition() { return definition; }
	public void act(Item sender) {
		definition.act(parameters, sender);
	}
	
	public void addParameter(CommandParameter param) { parameters.add(param); }
	public Vector<CommandParameter> getParameters() { return parameters; }
	
}
