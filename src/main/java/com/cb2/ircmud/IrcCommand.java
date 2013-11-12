package ircmud;

import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;

public enum IrcCommand {
	NICK(1, 1) {
		@Override
		public void init(Connection con, String prefix, String[] arguments) throws Exception {
			this.arguments = arguments;
			HashMap<String, String> map = new HashMap<String, String>();

			map.put("command",  "NICK");
			map.put("nick", arguments[0]);
			
			var_dump(con, prefix, map);
			this.argumentMap = map;
		}
	},
	USER(1, 4) {
		@Override
		public void init(Connection con, String prefix, String[] arguments) throws Exception {
			this.arguments  = arguments;

			HashMap<String, String> map = new HashMap<String, String>();
			map.put("command",  "USER");
			map.put("nick",     arguments[0]);
			map.put("mode",     arguments[1]);
			map.put("unused",   arguments[2]);
			map.put("realname", arguments[3]);
			var_dump(con, prefix, map);

			this.argumentMap = map;
		}
	},
	QUIT(1, 1) {
		@Override
		public void init(Connection con, String prefix, String[] arguments) throws Exception {
			this.arguments  = arguments;

			HashMap<String, String> map = new HashMap<String, String>();
			map.put("command",  "QUIT");
			map.put("quitMessage", arguments[0]);
			var_dump(con, prefix, map);

			this.argumentMap = map;
		}
	},
    MODE(0, 2) {
		@Override
		public void init(Connection con, String prefix, String[] arguments) throws Exception {
			this.arguments  = arguments;

			HashMap<String, String> map = new HashMap<String, String>();
			map.put("command",  "QUIT");
			map.put("nick", arguments[0]);
			map.put("mode", arguments[1]);
			var_dump(con, prefix, map);

			this.argumentMap = map;
		}
    };

	

    public void var_dump(Connection con, String prefix, HashMap<String, String> argumentMap) {
    	System.out.println("DEBUG: Command " + argument("command") + " from  " +con.hostname);
    	System.out.println("DEBUG: prefix:" + prefix);

    	
		Iterator it = argumentMap.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry pairs = (Map.Entry)it.next();
			System.out.println("DEBUG: "+pairs.getKey() + " = " + pairs.getValue());
			it.remove();
		}
    }
	
	public String[] arguments;
	public HashMap<String, String> argumentMap;
	
	private int minCmds;
	private int maxCmds;
	
	
	public int getMin() { return minCmds; }
	public int getMax() { return maxCmds; }
	
	private IrcCommand(int min, int max) {
		minCmds = min;
		maxCmds = max;
	}
	
	public String argument(String key) {
		if (this.argumentMap.containsKey(key))
			return this.argumentMap.get(key);
		else
			return null;
	}
	
    public abstract void init(Connection con, String prefix, String[] arguments) throws Exception;
}
