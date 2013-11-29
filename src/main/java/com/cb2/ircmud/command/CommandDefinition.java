package com.cb2.ircmud.command;

import java.util.regex.Pattern;
import com.cb2.ircmud.command.CommandParameter;

public class CommandDefinition {
	protected String						name;
	protected Pattern 						namePattern;
	protected CommandParameter.Type 		parameterType;
	protected Pattern						preParameterPattern;
	protected Pattern						endPattern;	
	
	public CommandDefinition(String name, String namePattern) {
		this.name = name;
		this.namePattern = Pattern.compile(namePattern, Pattern.CASE_INSENSITIVE);
	}
	
	String getName() { return name; }
	Pattern getNamePattern() { return namePattern; }
	CommandParameter.Type getParameterType() { return parameterType; }
	Pattern getPreParameterPattern() { return preParameterPattern; }
	Pattern getEndPattern() { return endPattern; }
	
	void setParameterType(CommandParameter.Type type) {
		parameterType = type;
	}
	
	void setPreparameterPattern(Pattern pattern) {
		preParameterPattern = pattern;
	}
	
}
