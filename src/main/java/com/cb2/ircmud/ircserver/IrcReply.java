package com.cb2.ircmud.ircserver;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;

@Configurable
public class IrcReply {

	private ArrayList<String> arguments = new ArrayList<String>();
	private String postfix = null;
	private IrcUser sender = null;
	private String command = null;
	
	@Autowired 
	IrcServer server;
	
	
	public IrcReply(IrcUser sender, Object command, String... args) {
		this.sender = sender;
		this.command = command.toString();
		if (args.length > 0) {
			this.postfix = args[args.length-1];
			args[args.length-1] = "";
		}

		for( String argv: args ) if (!argv.isEmpty()) this.arguments.add(argv);
	}

	public static IrcReply serverReply(Object command, String... args) {
		return new IrcReply(null, command, args);
	}
	
	public String argument( int n ) {
		if ( n < 0 ) return null;

		if ( n > arguments.size() ) return null;

		return this.arguments.get(n);
	}

	public String getPostfix() {
		return this.postfix;
	}
	
	public String senderRepresentation() {
		if (this.sender == null)
			return server.serverName;
		return this.sender.getRepresentation();
	}
	
	public IrcUser getSender() {
		return this.sender;
	}
	

	public String getCommandName() {
		return this.command;
	}
	
	public String toString() {
		String string = ":" + senderRepresentation() + " "  + this.command + " ";
		for( String str : this.arguments )
			string = string + str + " ";
		if (this.postfix != null)
			string += ":" + this.postfix;
		return string;
	}
	public void var_dump() {
		System.out.println("DEBUG: IrcReply::var_dump()");
		System.out.println("Sender = "+senderRepresentation());
		System.out.println("Command = "+this.command);
		for( String str : this.arguments )
			System.out.println("Argument[] = "+str);
		System.out.println("Postfix = "+this.postfix);
	}
}
