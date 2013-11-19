package com.cb2.ircmud;

import java.util.Date;
import java.util.ArrayList;

public class Console {
	public static long _startTime = new Date().getTime(); 

	private static String[] ObjArrToStrArr(Object[] arr) {
		String[] output = new String[arr.length];
		for( int i = 0; i < arr.length; i++)  output[i] = arr[i].toString();
		return output;
	}	
	
	/*
	 */
	public static String getTimestamp() {
		String time = new String();
		time += (new Date().getTime()) - Console._startTime;
		return time;
	}

	/*
	 */
	public static void out(String... args) {
		String string = getTimestamp();
		for( String str : args ) string += ":\t"+str;
		System.out.println(string);
	}
	public static void out(Object... args) {
		out(ObjArrToStrArr(args));
	}

	/*
	 */
	public static void err(String... args) {
		String string = getTimestamp();
		for( String str : args ) string += ":\t"+str;
		System.err.println(string);
	}
	public static void err(Object... args) {
		err(ObjArrToStrArr(args));
	}

	/*
	 */
	public static void debug(String... args) {
		ArrayList<String> output = new ArrayList<String>();
		output.add("DEBUG");
		for( String str : args ) 
			output.add(str);
		out(output.toArray());
	}
	public static void debug(Object... args) {
		debug(ObjArrToStrArr(args));
	}
}
