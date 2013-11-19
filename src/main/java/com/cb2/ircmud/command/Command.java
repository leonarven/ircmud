package com.cb2.ircmud.command;


import com.cb2.ircmud.command.CommandDefinition;

public class Command {
	private CommandDefinition definition;
	
	Command(CommandDefinition def) {
		definition = def;
	}
	
	CommandDefinition getCommandDefinition() { return definition; }
	
}
