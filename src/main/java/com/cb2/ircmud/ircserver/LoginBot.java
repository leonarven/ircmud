package com.cb2.ircmud.ircserver;

import java.util.regex.Pattern;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.cb2.ircmud.domain.Player;
import com.github.rlespinasse.slf4j.spring.AutowiredLogger;

@Component
public class LoginBot extends IrcBotUser {
	
	private Pattern usernamePattern;
	private Pattern passwordPattern;
	
	public enum Command {
		//NOTIFY("NOTIFY", "[<username>[, <username>[, ...]]]"),
		LOGIN("LOGIN",   "<username (your email address)> <password>"),
		LOGOUT("LOGOUT", ""),
		CREATE("CREATE", "<email> <password> <password>"),
		INFO(  "INFO",   ""),
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
	
	@Autowired
	AuthService authService;
	@AutowiredLogger
	Logger logger;
	
	@Value("${config.bots.login.name}")
	protected String nickname;
	@Value("${config.bots.login.realname}")
	protected String realname;
	
	public LoginBot() {
		super("", "");
		
		parsePrivateMessages = true;
		
		//Email pattern
		usernamePattern = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,4}$", Pattern.CASE_INSENSITIVE);
		
		//TODO: a good password pattern
		passwordPattern = Pattern.compile(".+", Pattern.CASE_INSENSITIVE);
	}
	
	@PostConstruct
	protected void init(){
		logger.info("Initializing LoginBot({}: {})",nickname, realname);
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
		
		if (command != null) {
			switch(command) {
				case LOGIN:
					if (params != null && params.length == 2) {
						String username = params[0];
						String password = params[1];
	
						if (user.getPlayer() != null) {
							user.sendMessage(this, "You have logged in already");
						} else {
							
							Player acc = authService.login(username, password, user);
		
							if (acc != null) {
								user.sendMessage(this, "You have successfully logged in");
							} else {
								user.sendMessage(this, "Invalid username or password");
							}
						}
	
					} else user.sendMessage(this, command.usage());
					break;
				case LOGOUT:
					if (user.getPlayer() == null) {
						user.sendMessage(this, "You haven't logged in");
					} else {
						authService.logout(user.getPlayer());
						user.sendMessage(this, "You have logged out");
					}
					break;
				case CREATE:
					if (params != null && params.length == 3) {
						if (user.getPlayer() != null) {
							user.sendMessage(this, "You have logged in");
							break;
						}
						String username = params[0];
						String password = params[1];
						String password2 = params[2];
						if (!password.equals(password2)) {
							user.sendMessage(this, "Passwords don't match");
							break;
						}
						//TODO: Enable username validation
						/*if (!usernamePattern.matcher(username).matches()) {
							user.sendMessage(this, "Invalid email address");
							break;
						}*/
						//TODO: Enable password validation
						/*if (!passwordPattern.matcher(password).matches()) {
							user.sendMessage(this, "Invalid password address");
							break;
						}*/
						
						if (!authService.addAccount(username, password)) {
							user.sendMessage(this, "Already created an account with the same email");
							break;
						}
						
						authService.login(username, password, user);
						user.sendMessage(this, "You have successfully created an account. You are now logged in.");
					} else user.sendMessage(this, command.usage());
					break;
				/*case NOTIFY:
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
					break;*/
				case INFO:
					if (!user.isLoggedIn()) {
						user.sendMessage(this, "You must be logged in to use this command");
						break;
					}
					
					user.sendMessage(this, "You are logged in as \"" + user.getPlayer().getUsername() + "\"");
					
					break;
				case UNKNOWN:
				default:
					user.sendMessage(this, "Unknown command \"" + command_str + "\"");
			}
		} else user.sendMessage(this, "Unknown command \"" + command_str + "\"");
	}
}
