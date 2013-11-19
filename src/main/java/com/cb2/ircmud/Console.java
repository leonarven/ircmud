package com.cb2.ircmud;

import java.util.Date;
import java.util.ArrayList;

public class Console {
	public static long _startTime = new Date().getTime(); 
	public static void init() {
		
	}
	
	public static String getTimestamp() {
		String time = new String();
		time += (new Date().getTime()) - Console._startTime;
		return time;
	}
	
	public static void out(String... args) {
		String string = getTimestamp();
		for( String str : args ) string += ":\t"+str;
		System.out.println(string);
	}
	public static void out(Object... args) {
		String[] output = new String[args.length];
		for( int i = 0; i < args.length; i++)  output[i] = args[i].toString();
		out(output);
	}

	public static void debug(String... args) {
		ArrayList<String> output = new ArrayList<String>();
		output.add("DEBUG");
		for( String str : args ) 
			output.add(str);
		out(output.toArray());
	}
}
