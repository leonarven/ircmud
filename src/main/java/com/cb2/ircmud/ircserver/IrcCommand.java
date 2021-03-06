package com.cb2.ircmud.ircserver;


import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import org.springframework.stereotype.Component;

@Component
public enum IrcCommand {

	NICK(1, 1) {
		@Override
		public void init(Connection con, String prefix, String[] arguments) {
			this.sender = con;
			this.arguments = arguments;

			this.argumentMap.put("command",  "NICK");
			this.argumentMap.put("nick", arguments[0]);
		}
	},
	USER(1, 4) {
		@Override
		public void init(Connection con, String prefix, String[] arguments) {
			this.sender = con;
			this.arguments  = arguments;

			this.argumentMap.put("command",  "USER");
			this.argumentMap.put("nick",     arguments[0]);
			this.argumentMap.put("mode",     arguments[1]);
			this.argumentMap.put("unused",   arguments[2]);
			this.argumentMap.put("realname", arguments[3]);
		}
	},
	QUIT(1, 1) {
		@Override
		public void init(Connection con, String prefix, String[] arguments) {
			this.sender = con;
			this.arguments  = arguments;

			this.argumentMap.put("command",  "QUIT");
			this.argumentMap.put("quitMessage", arguments[0]);
		}
	},
	MODE(0, 2) { // TODO: If user queries channel mode no 2. parameter. Now causes java.lang.ArrayIndexOutOfBoundsException
		@Override
		public void init(Connection con, String prefix, String[] arguments) {
			this.sender = con;
			this.arguments  = arguments;

			this.argumentMap.put("command",  "MODE");
			this.argumentMap.put("target", arguments[0]);
			if (arguments.length == 2)
				this.argumentMap.put("mode", arguments[1]);
		}
	},
	JOIN(1, 2){
		@Override
		public void init(Connection con, String prefix, String[] arguments) {
			this.sender = con;
			this.arguments  = arguments;

			this.argumentMap.put("command",  "JOIN");
			this.argumentMap.put("channels", arguments[0]);
			if (arguments.length == 2)
				this.argumentMap.put("passwords", arguments[1]);
		}
	},
	PART(1, 2){
		@Override
		public void init(Connection con, String prefix, String[] arguments) {
			this.sender = con;
			this.arguments  = arguments;

			this.argumentMap.put("command",  "PART");
			this.argumentMap.put("channels", arguments[0]);
			if (arguments.length == 2)
				this.argumentMap.put("leaveMessage", arguments[1]);
		}
	},
	PRIVMSG(2, 2) {
		@Override
		public void init(Connection con, String prefix, String[] arguments) {
			this.sender = con;
			this.arguments = arguments;
			
			this.argumentMap.put("command", "PRIVMSG");
			this.argumentMap.put("channel", arguments[0]);
			this.argumentMap.put("msg", arguments[1]);
		}
	},
	WHO(1,2) {
		@Override
		public void init(Connection con, String prefix, String[] arguments) {
			this.sender = con;
			this.arguments = arguments;
			
			this.argumentMap.put("command", "WHO");
			this.argumentMap.put("mask", arguments[0]);
			if (arguments.length == 2)
				this.argumentMap.put("operator", arguments[1]);
		}
	},
	WHOIS(1,1) {
		@Override
		public void init(Connection con, String prefix, String[] arguments) {
			this.sender = con;
			this.arguments = arguments;
			
			this.argumentMap.put("command", "WHOIS");
			this.argumentMap.put("mask", arguments[0]);
		}
	},
	PING(1,1) {
		@Override
		public void init(Connection con, String prefix, String[] arguments) {
			this.sender = con;
			this.arguments = arguments;
			
			this.argumentMap.put("command", "PING");
			this.argumentMap.put("target", arguments[0]);
		}
	},
	PONG(1,1) {
		@Override
		public void init(Connection con, String prefix, String[] arguments) {
			this.sender = con;
			this.arguments = arguments;
			
			this.argumentMap.put("command", "PONG");
			this.argumentMap.put("target", arguments[0]);
		}
	};

	public void var_dump(Connection con, String prefix) {
		System.out.println("DEBUG: Command " + argument("command") + " from " +con.getRepresentation());
		System.out.print("DEBUG: prefix:" + prefix);
		
		Iterator<Entry<String, String>> it = this.argumentMap.entrySet().iterator();
		while (it.hasNext()) {
			Entry<String, String> pairs = it.next();

			if (pairs.getKey().equals("command")) continue;

			System.out.print( ", " + pairs.getKey() + " = " + pairs.getValue() );
			it.remove();
		}
		System.out.println("");
	}
	
	public String[] arguments;
	public HashMap<String, String> argumentMap = new HashMap<String, String>();
	public Connection sender;
	
	private int minCmds;
	private int maxCmds;
	
	public int getMin() { return minCmds; }
	public int getMax() { return maxCmds; }
	
	private IrcCommand(int min, int max) {
		minCmds = min;
		maxCmds = max;
	}
	
	public String argument(String key) {
		if (key != null && this.argumentMap.containsKey(key))
			return this.argumentMap.get(key);
		else
			return null;
	}
	public String argument(int i) {
		if (0 < i && i < this.arguments.length)
			return this.arguments[i];
		else
			return null;
	}
	
	
	public abstract void init(Connection con, String prefix, String[] arguments);
}
