package com.cb2.ircmud.ircserver.bots;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import com.cb2.ircmud.domain.Player;
import com.cb2.ircmud.domain.services.PlayerService;
import com.cb2.ircmud.ircserver.IrcUser;

@Component
public class AdminBot extends IrcBotUser {
	@Autowired
	PlayerService playerService;
	@Autowired
	Environment env;

	public AdminBot() {
		parsePrivateMessages = true;
	}
	
	protected void init(){
		nickname=env.getProperty("config.bots.admin.name");
		username=env.getProperty("config.bots.admin.username");
		realname=env.getProperty("config.bots.admin.realname");
		hostname=env.getProperty("config.bots.admin.hostname");
	}
	
	public enum Command {
		GIVE("GIVE",   "ADMIN/GAMEMASTER <nickname or username(email)>"),
		REMOVE("REMOVE", "ADMIN/GAMEMASTER <nickname or username(email)>"),
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
	
	
	@Override
	public void receivePrivateMessage(IrcUser user, String msg) {
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
		
		if (command == null) command = Command.UNKNOWN;
		switch(command) {
			case GIVE: {
				if (params.length != 2) {
					user.sendMessage(this, command.usage());
					break;
				}
				
				String paramsAccessStr = params[0];
				int access = 0;
				if (paramsAccessStr.equalsIgnoreCase("admin")) {
					access = Player.ACCESS_ADMIN;
				} else if (paramsAccessStr.equalsIgnoreCase("gamemaster")) {
					access = Player.ACCESS_GAMEMASTER;
				} else {
					user.sendMessage(this, command.usage());
					break;
				}
				String usernameOrNickname = params[1];
				Player player = playerService.findPlayerByNicknameOrUsername(usernameOrNickname);
				if (player == null) {
					user.sendMessage(this, "Can't find player \"" + usernameOrNickname + "\"");
					break;
				}
				
				player.giveAccess(access);
				
				user.sendMessage(this,  "Success");
				break;
			}
			case REMOVE: {
				if (params.length != 2) {
					user.sendMessage(this, command.usage());
					break;
				}
				
				String paramsAccessStr = params[0];
				int access = 0;
				if (paramsAccessStr.equalsIgnoreCase("admin")) {
					access = Player.ACCESS_ADMIN_ONLY;
				} else if (paramsAccessStr.equalsIgnoreCase("gamemaster")) {
					access = Player.ACCESS_GAMEMASTER;
				} else {
					user.sendMessage(this, command.usage());
					break;
				}
				String usernameOrNickname = params[1];
				Player player = playerService.findPlayerByNicknameOrUsername(usernameOrNickname);
				if (player == null) {
					user.sendMessage(this, "Can't find player \"" + usernameOrNickname + "\"");
					break;
				}
				
				player.removeAccess(access);
				
				user.sendMessage(this,  "Success");
			}
			case UNKNOWN:
				user.sendMessage(this, "Unknown command \"" + command_str + "\"");
		}
	}
}
