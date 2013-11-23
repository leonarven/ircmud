package com.cb2.ircmud.command;

public class IntegerParameter extends CommandParameter {
	private int value;
	
	public IntegerParameter(int val) {
		value = val;
	}
	
	@Override
	public Type type() {
		return Type.Integer;
	}
	
	public int getValue() { return value; }
	

}
