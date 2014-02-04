package com.cb2.ircmud.command;

import java.util.List;
import java.util.regex.Pattern;

import com.cb2.ircmud.command.CommandParameter;
import com.cb2.ircmud.domain.Item;

public abstract class CommandDefinition {
	protected String						name;
	protected Pattern 						namePattern;
	protected CommandParameter.Type 		parameterType;
	protected Pattern						endPattern;	
	
	public CommandDefinition(String name, String namePattern) {
		this.name = name;
		this.namePattern = Pattern.compile(namePattern, Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CHARACTER_CLASS);
	}
	
	public String getName() { return name; }
	public Pattern getNamePattern() { return namePattern; }
	public CommandParameter.Type getParameterType() { return parameterType; }
	public Pattern getEndPattern() { return endPattern; }
	
	public void setParameterType(CommandParameter.Type type) {
		parameterType = type;
	}
	
	public abstract void act(List<CommandParameter> parameterList, Item sender);
	
	
}
