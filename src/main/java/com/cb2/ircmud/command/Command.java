package com.cb2.ircmud.command;


import java.util.Vector;

import com.cb2.ircmud.command.CommandDefinition;

public class Command {
	private CommandDefinition definition;
	private Vector<CommandParameter> parameters = new Vector<CommandParameter>();
	Command(CommandDefinition def) {
		definition = def;
	}
	
	CommandDefinition getCommandDefinition() { return definition; }
	
	void addParameter(CommandParameter param) { parameters.add(param); }
	Vector<CommandParameter> getParameters() { return parameters; }
	
}
