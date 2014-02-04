package com.cb2.ircmud.command;

import com.cb2.ircmud.command.Command;
import com.cb2.ircmud.command.CommandDefinition;
import com.github.rlespinasse.slf4j.spring.AutowiredLogger;

import java.io.Console;
import java.text.MessageFormat;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;

public class CommandParser {
	private Vector<CommandDefinition> commandDefinitions = new Vector<CommandDefinition>();
	private Pattern defaultEndPattern;
	private Pattern integerPattern;
	private Pattern locationPattern;
	private Pattern itemPattern;
	private Pattern stringPattern;
	private Pattern itemSeparatorPattern;
	
	@AutowiredLogger
	Logger logger;
	
	public CommandParser() {
		defaultEndPattern = Pattern.compile("^(\\s|\\.|!)*", Pattern.UNICODE_CHARACTER_CLASS);
		integerPattern = Pattern.compile("^-?[1-9][0-9]*", Pattern.UNICODE_CHARACTER_CLASS);
		stringPattern = Pattern.compile("^\"([^\"]*)\"", Pattern.UNICODE_CHARACTER_CLASS);
		locationPattern = Pattern.compile("^(the\\s+)?([a-zA-Z]+)");
		itemPattern = Pattern.compile("^((an?|the|all|every|my|[0-9]+)\\s+)?([a-z]+(\\s+[a-z]+)*)+", Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CHARACTER_CLASS);
		itemSeparatorPattern = Pattern.compile("((\\s+and)|(\\s*,))\\s+", Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CHARACTER_CLASS);
	}
	
	public void addCommandDefinition(CommandDefinition def) {
		commandDefinitions.add(def);
	}
	

	
	public Command parse(String cmdLine) throws CommandException {
		cmdLine = cmdLine.trim();
		
		CommandDefinition cmdDef = null;
		for (CommandDefinition def : commandDefinitions) {
			Matcher matcher = def.getNamePattern().matcher(cmdLine);
			if (matcher.lookingAt()) {
				cmdLine = cmdLine.substring(matcher.end());
				cmdDef = def;
			}
		}
		
		if (cmdDef == null) {
			throw new CommandException(MessageFormat.format("Can't find command \"{0}\"", cmdLine));
		}
		
		Command cmd = new Command(cmdDef);
		CommandParameter.Type paramTy = cmdDef.getParameterType();
		
		cmdLine = parseParameter(cmdLine, paramTy, cmd);
		
		if (cmdLine != null) {
			Pattern endPattern = cmdDef.getEndPattern();
			if (endPattern == null) endPattern = defaultEndPattern;
			if (!endPattern.matcher(cmdLine).matches()) {
				throw new CommandException(MessageFormat.format("The command end pattern \"{0}\" didn't match \"{1}\" ", endPattern.toString(), cmdLine));
			}
		}
		return cmd;
	}
	
	public String parseParameter(String cmdLine, CommandParameter.Type type, Command cmd) throws CommandException {
		switch (type) {
			case Integer:
				return parseInteger(cmdLine, cmd);
			case String:
				return parseString(cmdLine, cmd);
			case Location:
				return parseLocation(cmdLine, cmd);
			case Item:
				return parseItem(cmdLine, cmd);
			case None:
				return cmdLine;
		}
		
		
		return null;
		
	}
	
	public String parseInteger(String cmdLine, Command cmd)  throws CommandException {
		Matcher matcher = integerPattern.matcher(cmdLine);
		if (matcher.lookingAt()) {
			String intString = cmdLine.substring(0, matcher.end());
			cmdLine = cmdLine.substring(matcher.end()); //remove
			
			try {
				int val = Integer.parseInt(intString);
				cmd.addParameter(new IntegerParameter(val));
				return cmdLine;
			} catch (NumberFormatException e) {
				throw new CommandException(MessageFormat.format("Integer parsing failed \"{0}\"", intString));
			}
			
			
		} else {
			throw new CommandException(MessageFormat.format("Expecting integer... \"{0}\"", cmdLine));
		}
	}
	
	public String parseString(String cmdLine, Command cmd)  throws CommandException {
		Matcher matcher = stringPattern.matcher(cmdLine);
		if (matcher.lookingAt()) {
			String stringVal = matcher.group(1);
			cmdLine = cmdLine.substring(matcher.end()); //remove
			cmd.addParameter(new StringParameter(stringVal));
			
		} else {
			throw new CommandException(MessageFormat.format("Expecting string... \"{0}\"", cmdLine));
		}
		return null;
	}
	
	public String parseLocation(String cmdLine, Command cmd)  throws CommandException {
		Matcher matcher = locationPattern.matcher(cmdLine);
		if (matcher.lookingAt()) {
			String locationString = matcher.group(2);
			cmdLine = cmdLine.substring(matcher.end()); //remove
			cmd.addParameter(new LocationParameter(locationString));
			
		} else {
			throw new CommandException(MessageFormat.format("Expecting location... \"{0}\"", cmdLine));
		}
		return null;
	}
	public String parseItem(String cmdLine, Command cmd)  throws CommandException {
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
						throw new CommandException(MessageFormat.format("Integer parsing failed \"{0}\"", preString));
					}
				} else {
					throw new CommandException("WTF error");
				}
				cmd.addParameter(param);
				if (index == items.length) {
					return itemString;
				} else {
					if (itemString != null && !itemString.isEmpty()) {
						throw new CommandException(MessageFormat.format("Unexpected \"{0}\", while parsing the item name", itemString));
					}
				}
			} else {
				throw new CommandException(MessageFormat.format("Expecting item... \"{0}\"", itemString));
			}
			index++;
		}
		return null;
	}
	
	/*static public void main(String[] args) {
		CommandParser parser = new CommandParser();
		CommandDefinition runDefinition = new CommandDefinition("run", "runs?\\s+(to\\s+)");
		runDefinition.setParameterType(CommandParameter.Type.Location);
		parser.addCommandDefinition(runDefinition);
		
		CommandDefinition eatDefinition = new CommandDefinition("eat", "eats?\\s+");
		eatDefinition.setParameterType(CommandParameter.Type.Item);
		parser.addCommandDefinition(eatDefinition);
		
		try {
			Command cmd = parser.parse("eat 23 apples , a sword, the dead king and the banana");
			System.out.print(cmd.toString());
		} catch (CommandException ex) {
			System.err.print(ex);
		}
		
	}*/
	
}
