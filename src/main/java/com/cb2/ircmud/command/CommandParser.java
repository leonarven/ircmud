package com.cb2.ircmud.command;

import com.cb2.ircmud.command.Command;
import com.cb2.ircmud.command.CommandDefinition;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommandParser {
	private Vector<CommandDefinition> commandDefinitions;
	private Pattern defaultPreParamPattern;
	
	public CommandParser() {
		defaultPreParamPattern = Pattern.compile("^\\s");
	}
	
	void addCommandDefinition(CommandDefinition def) {
		commandDefinitions.add(def);
	}
	
	CommandDefinition findCommandDefinitionByName(String name) {
		for (CommandDefinition def : commandDefinitions) {
			if (def.getNamePattern().matcher(name).matches()) {
				return def;
			}
		}
		return null;
	}
	
	
	public Command parse(String cmdLine) throws CommandParsingException {
		cmdLine = cmdLine.trim();
		
		int charIndex = cmdLine.indexOf(' ');
		if (charIndex == -1) { //command without parameters
			String cmdName = cmdLine;
			CommandDefinition cmdDef = findCommandDefinitionByName(cmdName);
			if (cmdDef == null) {
				throw new CommandParsingException(MessageFormat.format("Can't find command \"{0}\"", cmdName));
			}
			return new Command(cmdDef);
		}
		
		String cmdName = cmdLine.substring(0, charIndex);
		CommandDefinition cmdDef = findCommandDefinitionByName(cmdName);
		if (cmdDef == null) {
			throw new CommandParsingException(MessageFormat.format("Can't find command \"{0}\"", cmdName));
		}
		Command cmd = new Command(cmdDef);
		
		Iterator<CommandParameter.Type> paramTyIt = cmdDef.getParameterTypes().iterator();
		Iterator<Pattern> preParamPatternIt = cmdDef.getPreParameterPatterns().iterator();
		
		while(paramTyIt.hasNext()) {
			CommandParameter.Type paramTy = paramTyIt.next();
			Pattern preParamPattern = preParamPatternIt.next();
			if (preParamPattern == null) preParamPattern = defaultPreParamPattern;
			
			Matcher matcher = preParamPattern.matcher(cmdLine);
			if (matcher.lookingAt()) {
				cmdLine = cmdLine.substring(0, matcher.end()); //remove PreParamPattern
			} else {
				throw new CommandParsingException(MessageFormat.format("Pre param pattern \"{0}\" didn't match the start of \"{1}\"", preParamPattern.toString(), cmdLine));
			}
			
			cmdLine = parseParameter(cmdLine, paramTy, cmd);
		}
		
		
		return null;
	}
	
	public String parseParameter(String cmdLine, CommandParameter.Type type, Command cmd) throws CommandParsingException {
		switch (type) {
			case Integer:
				return parseInteger(cmdLine, cmd);
			case String:
				return parseString(cmdLine, cmd);
			case Location:
				return parseLocation(cmdLine, cmd);
			case Item:
				return parseItem(cmdLine, cmd);
		}
		
		
		return null;
		
	}
	
	public String parseInteger(String cmdLine, Command cmd)  throws CommandParsingException {
		
		
		return null;
	}
	public String parseString(String cmdLine, Command cmd)  throws CommandParsingException {
		
		
		return null;
	}
	
	public String parseLocation(String cmdLine, Command cmd)  throws CommandParsingException {
		
		
		return null;
	}
	public String parseItem(String cmdLine, Command cmd)  throws CommandParsingException {
		
		
		return null;
	}
	
}
