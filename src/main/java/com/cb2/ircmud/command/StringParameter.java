package com.cb2.ircmud.command;

public class StringParameter extends CommandParameter {
	private String value;
	
	public StringParameter(String val) {
		value = val;
	}
	
	@Override
	public Type type() {
		return Type.String;
	}
	
	public String getValue() { return value; }

}
