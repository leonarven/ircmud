package com.cb2.ircmud.command;

public class LocationParameter extends CommandParameter {
	String location;
	public LocationParameter(String locationName) {
		location = locationName;
	}
	
	@Override
	public Type type() {
		return Type.Location;
	}

	String getLocation() { return location; }
	
}
