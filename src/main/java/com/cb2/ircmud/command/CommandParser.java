package com.cb2.ircmud.command;

import com.cb2.ircmud.command.Command;
import com.cb2.ircmud.command.CommandDefinition;
import com.github.rlespinasse.slf4j.spring.AutowiredLogger;

import java.text.MessageFormat;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;

public class CommandParser {
	private Vector<CommandDefinition> commandDefinitions = new Vector<CommandDefinition>();
	private Pattern defaultPreParamPattern;
	private Pattern defaultEndPattern;
	private Pattern integerPattern;
	private Pattern locationPattern;
	private Pattern itemPattern;
	private Pattern stringPattern;
	private Pattern itemSeparatorPattern;
	
	@AutowiredLogger
	Logger logger;
	
	public CommandParser() {
		defaultPreParamPattern = Pattern.compile("^\\s+");
		defaultEndPattern = Pattern.compile("^(\\s|\\.|!)*");
		integerPattern = Pattern.compile("^-?[1-9][0-9]*");
		stringPattern = Pattern.compile("^\"(.*)\"");
		locationPattern = Pattern.compile("^(the\\s+)?([a-zA-Z]+)");
		itemPattern = Pattern.compile("^((an?|the|all|every|my|[0-9]+)\\s+)?([a-z]+(\\s+[a-z]+)*)+", Pattern.CASE_INSENSITIVE);
		itemSeparatorPattern = Pattern.compile("((\\s+and)|(\\s*,))\\s+", Pattern.CASE_INSENSITIVE);
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
		cmdLine = cmdLine.substring(charIndex);
		
		
		CommandParameter.Type paramTy = cmdDef.getParameterType();
		Pattern preParamPattern = cmdDef.getPreParameterPattern();
		if (preParamPattern == null) preParamPattern = defaultPreParamPattern;
		
		Matcher matcher = preParamPattern.matcher(cmdLine);
		if (matcher.lookingAt()) {
			cmdLine = cmdLine.substring(matcher.end()); //remove PreParamPattern
		} else {
			throw new CommandParsingException(MessageFormat.format("Pre param pattern \"{0}\" didn't match the start of \"{1}\"", preParamPattern.toString(), cmdLine));
		}
		
		cmdLine = parseParameter(cmdLine, paramTy, cmd);
		
		if (cmdLine != null) {
			Pattern endPattern = cmdDef.getEndPattern();
			if (endPattern == null) endPattern = defaultEndPattern;
			if (!endPattern.matcher(cmdLine).matches()) {
				throw new CommandParsingException(MessageFormat.format("The command end pattern \"{0}\" didn't match \"{1}\" ", endPattern.toString(), cmdLine));
			}
		}
		return cmd;
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
		Matcher matcher = stringPattern.matcher(cmdLine);
		if (matcher.lookingAt()) {
			String stringVal = matcher.group(1);
			cmdLine = cmdLine.substring(matcher.end()); //remove
			cmd.addParameter(new StringParameter(stringVal));
			
		} else {
			throw new CommandParsingException(MessageFormat.format("Expecting string... \"{0}\"", cmdLine));
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
		String[] items = itemSeparatorPattern.split(cmdLine);
		int index = 1;
		for (String itemString : items) {
			itemString = itemString.trim();
			Matcher matcher = itemPattern.matcher(itemString);
			if (matcher.lookingAt()) {
				String preString = matcher.group(2).toLowerCase();
				String itemName = matcher.group(3).trim().replaceAll("\\s+", " ");
				itemString = itemString.substring(matcher.end()); //remove
				
				ItemParameter param = null;
				if (preString.isEmpty()) {
					param = new ItemParameter(1, itemName);
				} else if (preString.equals("a") || preString.equals("an")) {
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
				if (index == items.length) {
					return itemString;
				} else {
					if (itemString != null && !itemString.isEmpty()) {
						throw new CommandParsingException(MessageFormat.format("Unexpected \"{0}\", while parsing the item name", itemString));
					}
				}
			} else {
				throw new CommandParsingException(MessageFormat.format("Expecting item... \"{0}\"", itemString));
			}
			index++;
		}
		return null;
	}
	
	public void main(String[] args) {
		CommandParser parser = new CommandParser();
		CommandDefinition runDefinition = new CommandDefinition("run", "runs?");
		runDefinition.setParameterType(CommandParameter.Type.Location);
		runDefinition.setPreparameterPattern(Pattern.compile("^\\s+(to\\s+)?", Pattern.CASE_INSENSITIVE));
		parser.addCommandDefinition(runDefinition);
		
		CommandDefinition eatDefinition = new CommandDefinition("eat", "eats?");
		eatDefinition.setParameterType(CommandParameter.Type.Item);
		eatDefinition.setPreparameterPattern(Pattern.compile("^\\s+", Pattern.CASE_INSENSITIVE));
		parser.addCommandDefinition(eatDefinition);
		
		try {
			Command cmd = parser.parse("eat 23 apples , a sword, the dead king and the banana");
			logger.info("{}",cmd);
		} catch (CommandParsingException ex) {
			logger.info("{}",ex);
		}
		
	}
	
}
