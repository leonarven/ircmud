package com.cb2.ircmud;

import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;
import java.io.File;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.*;

public enum IrcCommand {
	NICK(1, 1) {
		@Override
		public void init(Connection con, String prefix, String[] arguments) throws Exception {
			this.arguments = arguments;

			this.argumentMap.put("command",  "NICK");
			this.argumentMap.put("nick", arguments[0]);
			
			var_dump(con, prefix);
		}
	},
	USER(1, 4) {
		@Override
		public void init(Connection con, String prefix, String[] arguments) throws Exception {
			this.arguments  = arguments;

			this.argumentMap.put("command",  "USER");
			this.argumentMap.put("nick",     arguments[0]);
			this.argumentMap.put("mode",     arguments[1]);
			this.argumentMap.put("unused",   arguments[2]);
			this.argumentMap.put("realname", arguments[3]);

			var_dump(con, prefix);
		}
	},
	QUIT(1, 1) {
		@Override
		public void init(Connection con, String prefix, String[] arguments) throws Exception {
			this.arguments  = arguments;

			this.argumentMap.put("command",  "QUIT");
			this.argumentMap.put("quitMessage", arguments[0]);

			var_dump(con, prefix);
		}
	},
	MODE(0, 2) { // TODO: If user queries channel mode no 2. parameter. Now causes java.lang.ArrayIndexOutOfBoundsException
		@Override
		public void init(Connection con, String prefix, String[] arguments) throws Exception {
			this.arguments  = arguments;

			this.argumentMap.put("command",  "MODE");
			this.argumentMap.put("target", arguments[0]);
			if (arguments.length == 2)
				this.argumentMap.put("mode", arguments[1]);

			var_dump(con, prefix);
		}
	},
	JOIN(1, 2){
		@Override
		public void init(Connection con, String prefix, String[] arguments) throws Exception {
			this.arguments  = arguments;

			this.argumentMap.put("command",  "JOIN");
			this.argumentMap.put("channels", arguments[0]);
			if (arguments.length == 2)
				this.argumentMap.put("passwords", arguments[1]);

			var_dump(con, prefix);
		}
	},
	PART(1, 2){
		@Override
		public void init(Connection con, String prefix, String[] arguments) throws Exception {
			this.arguments  = arguments;

			this.argumentMap.put("command",  "PART");
			this.argumentMap.put("channels", arguments[0]);
			if (arguments.length == 2)
				this.argumentMap.put("leaveMessage", arguments[1]);

			var_dump(con, prefix);
		}
	},
	PRIVMSG(2, 2) {
		@Override
		public void init(Connection con, String prefix, String[] arguments) throws Exception {
			this.arguments = arguments;
			
			this.argumentMap.put("command", "PRIVMSG");
			this.argumentMap.put("channel", arguments[0]);
			this.argumentMap.put("msg", arguments[1]);
		}
	},
	WHO(1,2) {
		@Override
		public void init(Connection con, String prefix, String[] arguments) throws Exception {
			this.arguments = arguments;
			
			this.argumentMap.put("command", "WHO");
			this.argumentMap.put("mask", arguments[0]);
			if (arguments.length == 2)
				this.argumentMap.put("operator", arguments[1]);
		}
	},
	WHOIS(1,1) {
		@Override
		public void init(Connection con, String prefix, String[] arguments) throws Exception {
			this.arguments = arguments;
			
			this.argumentMap.put("command", "WHOIS");
			this.argumentMap.put("mask", arguments[0]);
		}
	},
	PING(1,1) {
		@Override
		public void init(Connection con, String prefix, String[] arguments) throws Exception {
			this.arguments = arguments;
			
			this.argumentMap.put("command", "PING");
			this.argumentMap.put("target", arguments[0]);
		}
	},
	PONG(1,1) {
		@Override
		public void init(Connection con, String prefix, String[] arguments) throws Exception {
			this.arguments = arguments;
			
			this.argumentMap.put("command", "PONG");
			this.argumentMap.put("target", arguments[0]);
		}
	};

	public void var_dump(Connection con, String prefix) {
		System.out.println("DEBUG: Command " + argument("command") + " from " +con.getRepresentation());
		System.out.print("DEBUG: prefix:" + prefix);
		
		Iterator it = this.argumentMap.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry pairs = (Map.Entry)it.next();

			if (pairs.getKey().equals("command")) continue;

			System.out.print( ", " + pairs.getKey() + " = " + pairs.getValue() );
			it.remove();
		}
		System.out.println("");
	}
	
	public String[] arguments;
	public HashMap<String, String> argumentMap = new HashMap<String, String>();
	
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
	
	public abstract void init(Connection con, String prefix, String[] arguments) throws Exception;
	
	public static void load(String file) {
		Console.out("IrcCommand", "Loading IrcCommand configurations");
		try {
			File fXmlFile = new File(file);
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(fXmlFile);
			
			doc.getDocumentElement().normalize();

			NodeList nList = doc.getElementsByTagName("command");

			for (int temp = 0; temp < nList.getLength(); temp++) {
				 
				Node nNode = nList.item(temp);
		 
				if (nNode.getNodeType() == Node.ELEMENT_NODE) {
		 
					Element eElement = (Element) nNode;
		 
				}
			}
		
		} catch(Exception e) {
			System.out.println("Error while initializing IrcCommands: "+e.getMessage());
		}
	}
}
