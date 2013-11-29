package com.cb2.ircmud;

import java.util.Date;
import java.util.ArrayList;

import org.springframework.stereotype.Component;

@Component
public class Console {
	public final long startTime = new Date().getTime(); 

	private String[] ObjArrToStrArr(Object[] arr) {
		String[] output = new String[arr.length];
		for( int i = 0; i < arr.length; i++)  output[i] = arr[i].toString();
		return output;
	}	
	
	/*
	 */
	public String getTimestamp() {
		String time = new String();
		time += (new Date().getTime()) - startTime;
		return time;
	}

	/*
	 */
	public void out(String... args) {
		String string = getTimestamp();
		for( String str : args ) string += ":\t"+str;
		System.out.println(string);
	}
	public void out(Object... args) {
		out(ObjArrToStrArr(args));
	}

	/*
	 */
	public void err(String... args) {
		String string = getTimestamp();
		for( String str : args ) string += ":\t"+str;
		System.err.println(string);
	}
	public void err(Object... args) {
		err(ObjArrToStrArr(args));
	}

	/*
	 */
	public void debug(String... args) {
		ArrayList<String> output = new ArrayList<String>();
		output.add("DEBUG");
		for( String str : args ) 
			output.add(str);
		out(output.toArray());
	}
	public void debug(Object... args) {
		debug(ObjArrToStrArr(args));
	}
}
