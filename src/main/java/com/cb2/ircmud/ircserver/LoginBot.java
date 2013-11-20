package com.cb2.ircmud.ircserver;


public class LoginBot extends IrcBotUser {
	
	public enum Command {
		NOTIFY("NOTIFY", "[<username>[, <username>[, ...]]]"),
		AUTH(  "AUTH",   "<username> <password>"),
		INFO(  "INFO",   "<username>"),
		UNKNOWN(  "",   "");
		
		public String command, usage;
		
		private Command(String command, String usage) {
			this.command = command;
			this.usage = usage;
		}
		public String usage() {
			return "Usage: "+command+" "+usage;
		}
	}
	
	public LoginBot(String nick, String realname) {
		super(nick, realname);
		parsePrivateMessages = true;
	}
	
	@Override
	public void receivePrivateMessage(String sender, String msg) {
		IrcUser user = IrcServer.findUserByNickname(sender);
		if (user == null) return;
		
		String[] parts = msg.split(" ");
		String command = parts[0].toLowerCase();
		String[] params = null;
		if (parts.length > 1) {
			params = new String[parts.length - 1];
			System.arraycopy(parts, 1, params, 0, parts.length - 1);
		}
		
		handleCommand(user, command, params);
	}
	
	public void handleCommand(IrcUser user, String command_str, String[] params) {
		//TODO: EBIN Login System        ELS
		
		
		Command command = null;
		
		try {
			command = Command.valueOf(command_str.toUpperCase());
		} catch(IllegalArgumentException e) {}
		
		if (command != null) {
			switch(command) {
				case AUTH:
					if (params != null && params.length == 2) {
						String username = params[0];
						String password = params[1];
	
						boolean login = AuthService.testLogin(username);
						AuthService.Account acc = AuthService.login(username, password, user);
	
						if (acc != null) {
	
							if (!login) {
	
								user.sendMessage(this, "Login successfully");
	
							} else {
								user.sendMessage(this, "You have login already");
							}
	
						} else {
							user.sendMessage(this, "Invalid username or password");
						}
						
						acc.var_dump();
	
					} else user.sendMessage(this, command.usage());
					break;
				case NOTIFY:
					if (params != null && params.length >= 1) {
						String cantFindTheseUsers = "";
						for (String notifyUser : params) {
							IrcUser nuser = IrcServer.findUserByNickname(notifyUser);
							if (nuser != null) {
								nuser.sendReply(IrcReply.serverReply("NOTIFY", user.getNickname() + " wants to notify you"));
							}
							else {
								cantFindTheseUsers += " " + notifyUser;
							}
						}
						if (!cantFindTheseUsers.isEmpty()) {
							user.sendMessage(this, "Can't find nicks:" + cantFindTheseUsers);
						}
					} else user.sendMessage(this, command.usage());
					break;
				case INFO:
					if (params != null && params.length == 1) {
						// TODO
					} else user.sendMessage(this, command.usage());
					break;
				case UNKNOWN:
				default:
					user.sendMessage(this, "Unknown command \"" + command_str + "\"");
			}
		} else user.sendMessage(this, "Unknown command \"" + command_str + "\"");
	}
}
