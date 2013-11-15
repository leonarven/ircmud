package com.cb2.ircmud;


public class LoginBot extends IrcBotUser {
	
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
	
	public void handleCommand(IrcUser user, String command, String[] params) {
		//TODO: EBIN Login System        ELS
		
		
		//Test command
		String cantFindTheseUsers = "";
		if (command.equals("notify")) {
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
		}
		else {
			user.sendMessage(this, "Unknown command \"" + command + "\"");
		}
	}
	
}
