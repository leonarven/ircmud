package com.cb2.ircmud.command;

import java.util.regex.Pattern;
import com.cb2.ircmud.command.CommandParameter;
import java.util.Vector;

public class CommandDefinition {
	protected String						name;
	protected Pattern 						namePattern;
	
	protected Vector<CommandParameter.Type> parameterTypes = new Vector<CommandParameter.Type>();
	protected Vector<Pattern> 				preParameterPatterns = new Vector<Pattern>();
	protected Pattern						endPattern;	
	
	public CommandDefinition(String name, String namePattern) {
		this.name = name;
		this.namePattern = Pattern.compile(namePattern, Pattern.CASE_INSENSITIVE);
	}
	
	String getName() { return name; }
	Pattern getNamePattern() { return namePattern; }
	Vector<CommandParameter.Type> getParameterTypes() { return parameterTypes; }
	Vector<Pattern> getPreParameterPatterns() { return preParameterPatterns; }
	Pattern getEndPattern() { return endPattern; }
	
	void addParameter(Pattern preParameterPattern, CommandParameter.Type type) {
		parameterTypes.add(type);
		preParameterPatterns.add(preParameterPattern);
	}
	
}
