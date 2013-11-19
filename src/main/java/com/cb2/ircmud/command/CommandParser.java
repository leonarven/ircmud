package com.cb2.ircmud.command;

import com.cb2.ircmud.Console;
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
	private Pattern integerPattern;
	private Pattern locationPattern;
	private Pattern itemPattern;
	private Pattern stringPattern;
	
	public CommandParser() {
		defaultPreParamPattern = Pattern.compile("^\\s+");
		integerPattern = Pattern.compile("^-?[1-9][0-9]*");
		stringPattern = Pattern.compile("^\"(.*)\"");
		locationPattern = Pattern.compile("^(the\\s+)?([a-zA-Z]+)");
		itemPattern = Pattern.compile("^(a|the|all|every|my|[0-9]+)?\\s+ ([a-z]+)", Pattern.CASE_INSENSITIVE);
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
				cmdLine = cmdLine.substring(matcher.end()); //remove PreParamPattern
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
		Matcher matcher = integerPattern.matcher(cmdLine);
		if (matcher.lookingAt()) {
			String intString = cmdLine.substring(0, matcher.end());
			cmdLine = cmdLine.substring(matcher.end()); //remove
			
			try {
				int val = Integer.parseInt(intString);
				cmd.addParameter(new IntegerParameter(val));
				return cmdLine;
			} catch (NumberFormatException e) {
				throw new CommandParsingException(MessageFormat.format("Integer parsing failed \"{0}\"", intString));
			}
			
			
		} else {
			throw new CommandParsingException(MessageFormat.format("Expecting integer... \"{0}\"", cmdLine));
		}
	}
	public String parseString(String cmdLine, Command cmd)  throws CommandParsingException {
		Matcher matcher = locationPattern.matcher(cmdLine);
		if (matcher.lookingAt()) {
			String stringVal = matcher.group(1);
			cmdLine = cmdLine.substring(matcher.end()); //remove
			cmd.addParameter(new StringParameter(stringVal));
			
		} else {
			throw new CommandParsingException(MessageFormat.format("Expecting location... \"{0}\"", cmdLine));
		}
		return null;
	}
	
	public String parseLocation(String cmdLine, Command cmd)  throws CommandParsingException {
		Matcher matcher = locationPattern.matcher(cmdLine);
		if (matcher.lookingAt()) {
			String locationString = matcher.group(2);
			cmdLine = cmdLine.substring(matcher.end()); //remove
			cmd.addParameter(new LocationParameter(locationString));
			
		} else {
			throw new CommandParsingException(MessageFormat.format("Expecting location... \"{0}\"", cmdLine));
		}
		return null;
	}
	public String parseItem(String cmdLine, Command cmd)  throws CommandParsingException {
		Matcher matcher = itemPattern.matcher(cmdLine);
		if (matcher.lookingAt()) {
			String preString = matcher.group(1).toLowerCase();
			String itemName = matcher.group(2);
			cmdLine = cmdLine.substring(matcher.end()); //remove
			
			ItemParameter param = null;
			if (preString.isEmpty()) {
				param = new ItemParameter(1, itemName);
			} else if (preString.equals("a")) {
				param = new ItemParameter(1, itemName);
			} else if (preString.equals("the")) {
				param = new ItemParameter(ItemParameter.QuantityType.Specific, itemName);
			} else if (preString.equals("my")) {
				param = new ItemParameter(ItemParameter.QuantityType.Specific, itemName);
				param.setOwner(ItemParameter.Owner.Caller);
			} else if (preString.equals("all") || preString.equals("every")) {
				param = new ItemParameter(ItemParameter.QuantityType.All, itemName);
			} else if (Character.isDigit(preString.charAt(0))) {
				try {
					int val = Integer.parseInt(preString);
					param = new ItemParameter(val, itemName);
				} catch (NumberFormatException e) {
					throw new CommandParsingException(MessageFormat.format("Integer parsing failed \"{0}\"", preString));
				}
			} else {
				throw new CommandParsingException("WTF error");
			}
			cmd.addParameter(param);
			return cmdLine;
			
		} else {
			throw new CommandParsingException(MessageFormat.format("Expecting location... \"{0}\"", cmdLine));
		}
	}
	/*
	public static void main(String[] args) {
		CommandParser parser = new CommandParser();
		CommandDefinition runDefinition = new CommandDefinition("run", "runs?");
		runDefinition.addParameter(Pattern.compile("^\\s+(to\\s+)?", Pattern.CASE_INSENSITIVE), CommandParameter.Type.Location);
		parser.addCommandDefinition(runDefinition);
		
		try {
			Command cmd = parser.parse("run to the west");
		} catch (CommandParsingException ex) {
			Console.out(ex);
		}
	}*/
	
}
