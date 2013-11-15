package com.cb2.ircmud;

import java.util.ArrayList;

public class IrcReply {

	private ArrayList<String> arguments = new ArrayList<String>();
	private String postfix = null;
	private String sender = null;
	private String command = null;

	public IrcReply(String sender, Object command, String... args) {
		this.sender = sender;
		this.command = command.toString();
		if (args.length > 0) {
			this.postfix = args[args.length-1];
			args[args.length-1] = "";
		}

		for( String argv: args ) if (!argv.isEmpty()) this.arguments.add(argv);

		for(String str : this.arguments) System.out.println("args[]:"+str);
	}
	public IrcReply(IrcUser sender, Object command, String... args) {
		this(sender.getRepresentation(), command, args);
	}

	public static IrcReply serverReply(Object command, String... args) {
		return new IrcReply(IrcServer.globalServerName, command, args);
	}
	
	public String argument( int n ) {
		if ( n < 0 ) return null;

		if ( n > arguments.size() ) return null;

		return this.arguments.get(n);
	}

	public String postfix() {
		return this.postfix;
	}
	
	public String sender() {
		return this.sender;
	}

	public String command() {
		return this.command;
	}
	
	public String toString() {
		String string = ":" + this.sender + " "  + this.command + " ";
		for( String str : this.arguments )
			string = string + str + " ";
		if (this.postfix != null)
			string += ":" + this.postfix;
		return string;
	}
	public void var_dump() {
		System.out.println("DEBUG: IrcReply::var_dump()");
		System.out.println("Sender = "+sender);
		System.out.println("Command = "+this.command);
		for( String str : this.arguments )
			System.out.println("Argument[] = "+str);
		System.out.println("Postfix = "+this.postfix);
	}
}
