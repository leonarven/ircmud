package com.cb2.ircmud.ircserver.services;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.cb2.ircmud.ircserver.Channel;
import com.cb2.ircmud.ircserver.Connection;
import com.cb2.ircmud.ircserver.IrcCommand;
import com.cb2.ircmud.ircserver.IrcReply;
import com.cb2.ircmud.ircserver.IrcReplyCode;
import com.cb2.ircmud.ircserver.IrcServer;
import com.cb2.ircmud.ircserver.IrcUser;
import com.cb2.ircmud.ircserver.PingService;
import com.github.rlespinasse.slf4j.spring.AutowiredLogger;

@Service
public class IrcCommandService implements Runnable {
	
	@Autowired 
	IrcServer server;
	@Autowired 
	UserService users;
	@Autowired 
	PingService pingService;
	@Autowired 
	ChannelService channels;
	@AutowiredLogger
	Logger logger;
	@Autowired
	MotdService motdService;
	@Autowired
	Environment env;
	
	private Thread thread;
	private BlockingQueue<IrcCommand> workQueue = new LinkedBlockingQueue<IrcCommand>();
	
	public void addMessage(Connection sender, String line) {
		IrcCommand cmd = parseCommand(sender, line);
		if (cmd != null) {
			workQueue.add(cmd);
		}
	}
	
	public IrcCommand parseCommand(Connection con, String line) {
		logger.debug("{} >>> {}", con.getNickname(),line);
		String prefix = "";
		if (line.startsWith(":")) {
			String[] tokens = line.split(" ", 2);
			prefix = tokens[0];
			line = (tokens.length > 1 ? tokens[1] : "");
		}
		String[] tokens1 = line.split(" ", 2);
		String command = tokens1[0];
		line = tokens1.length > 1 ? tokens1[1] : "";
		String[] tokens2 = line.split("(^| )\\:", 2);
		String trailing = null;
		line = tokens2[0];

		if (tokens2.length > 1) trailing = tokens2[1];
		ArrayList<String> argumentList = new ArrayList<String>();
		
		if (!line.equals("")) argumentList.addAll(Arrays.asList(line.split(" ")));
		if (trailing != null) argumentList.add(trailing);

		String[] arguments = argumentList.toArray(new String[0]);
		

		if (command.matches("[0-9][0-9][0-9]"))
			command = "n" + command;
		IrcCommand commandObject = null;
		try {
			commandObject = IrcCommand.valueOf(command.toUpperCase());
		} catch(IllegalArgumentException e) {
			con.sendServerReply(IrcReplyCode.ERR_UNKNOWNCOMMAND, command + " :Unknown command ");
			return null;
		}
		if (arguments.length < commandObject.getMin() || arguments.length > commandObject.getMax()) {
			//TODO: Use command
			con.sendSelfNotice("Invalid number of arguments for this" + " command, expected not more than " + commandObject.getMax() + " and not less than " + commandObject.getMin() + " but got " + arguments.length + " arguments");
			return null;
		}
		commandObject.init(con, prefix, arguments);
		return commandObject;
	}
	
	@PostConstruct
	void initialize() {
		thread = new Thread(this);
		thread.start();
	}
	
	@Override
	public void run() {
		try {
			while (true) {
				IrcCommand cmd = workQueue.take();
				actCommand(cmd);
			}
		}
		catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public void actCommand(IrcCommand command) {
		Connection sender = command.sender;
		
		if (!sender.isConnected()) {
			switch(command) {
				case NICK:
					handleNotConnectedNickCommand(command, sender);
					break;
				case USER:
					handleNotConnectedUserCommand(command, sender);
					break;
				default:
					sender.sendRawString("ERROR :Closing connection " + sender.getRepresentation() + " (Invalid command)");
			}
		}
		else { //Connection established
			switch(command) {
				case NICK:
					handleNickCommand(command, sender);
					break;
				case USER:
					handleUserCommand(sender);
					break;
				case JOIN:
					handleJoinCommand(command, sender);
					break;
				case MODE:
					handleModeCommand(command, sender);
					break;
				case PART:
					handlePartCommand(command, sender);
					break;
				case QUIT:
					handleQuitCommand(command, sender);
					break;
				case PRIVMSG:
					handlePrivMsgCommand(command, sender);
					break;
				case WHO:
					handleWhoCommand(command, sender);
					break;
				case WHOIS:
					handleWhoIsCommand(command, sender);
					break;
				case PING:
					handlePingCommand(sender);
					break;
				case PONG:
					handlePongCommand(sender);
					break;
				default:
					unhandledIrcCommandError();
			}
			
		}
	}

	private void unhandledIrcCommandError() {
		logger.error("Unhandled IrcCommand");
	}

	private void handlePongCommand(Connection sender) {
		pingService.pongFrom(sender);
	}

	private void handlePingCommand(Connection sender) {
		sender.sendServerReply("PONG", server.serverName+" :"+server.serverName);
	}

	private void handleWhoIsCommand(IrcCommand command, Connection sender) {
		String mask;
		mask = command.arguments[0];
		
		IrcUser con = users.findUserByNickname(mask);
		if (con != null) {
			
			sender.sendWhoIsReply(con);
			
		} else {
			// TODO ERR_NOSUCHNICK = 401 
			sender.sendSelfNotice("No such nick");
		}
	}

	private void handleWhoCommand(IrcCommand command, Connection sender) {
		String mask;
		mask = command.arguments[0];
		String op;
		if (command.arguments.length == 2)
			op = command.arguments[1];
		if (Channel.isValidPrefix(mask.charAt(0))) {
			if (sender.hasJoinedToChannel(mask)) {
				Channel channel = channels.find(mask);
				if (channel != null) {
					sender.sendWhoReply(channel);
				} else {
					sender.sendSelfNotice("Channel does not exits"); //ERR_NOSUCHCHANNEL = 403
				}
			} else {
				sender.sendSelfNotice("Server does not allow to asking about channels you are not in to");
			}
		} else {
			// TODO
			sender.sendSelfNotice("Server does not allow to asking about users");
		}
	}

	private void handlePrivMsgCommand(IrcCommand command, Connection sender) {
		String target  = command.arguments[0];
		if (Channel.isValidPrefix(target.charAt(0))) { //Channel
			if (channels.find(target) != null) {
				if (sender.hasJoinedToChannel(target)) {
					sender.findJoinedChannel(target).sendReplyToAllExceptSender(new IrcReply(sender, "PRIVMSG", target, command.arguments[1]));
				} else {
					sender.sendCommand(IrcReplyCode.ERR_CANNOTSENDTOCHAN, "Cannot send to channels you have not joined");
				}
			}
			else {
				sender.sendCommand(IrcReplyCode.ERR_NOSUCHCHANNEL, "No such channel");
			}
		}
		else {
			IrcUser user = users.findUserByNickname(target);
			if (user != null) {
				user.sendMessage(sender, command.arguments[1]);
			}
			else {
				sender.sendCommand(IrcReplyCode.ERR_NOSUCHNICK, "No such nick");
			}
		}
	}

	private void handleQuitCommand(IrcCommand command, Connection sender) {
		sender.quit(command.arguments[0]);
		sender.closeConnection();
	}

	private void handlePartCommand(IrcCommand command, Connection sender) {
		if (command.arguments.length == 2)
			sender.leaveChannel(command.arguments[0], command.arguments[1]);
		else
			sender.leaveChannel(command.arguments[0], "");
	}

	private void handleModeCommand(IrcCommand command, Connection sender) {
		//TODO: Implement modes
		if (command.arguments.length >= 2) {
			if (command.arguments[1].equals("b") && Channel.isValidPrefix(command.arguments[0].charAt(0))) { //Ban list
				IrcReply reply = IrcReply.serverReply(IrcReplyCode.RPL_ENDOFBANLIST, sender.getNickname(), command.arguments[0], "End of channel ban list");
				sender.sendReply(reply);
				
				return;
			}
			sender.sendSelfNotice("This server does not allow to change usermode");
		}
		else {
			if (Channel.isValidPrefix(command.arguments[0].charAt(0))) { //Channel
				Channel channel = channels.find(command.arguments[0]);
				if (channel != null) { 
					IrcReply reply = IrcReply.serverReply(IrcReplyCode.RPL_CHANNELMODEIS,  sender.getNickname(), command.arguments[0], channel.mode, "");
					sender.sendReply(reply);
				} else {
					sender.sendCommand(IrcReplyCode.ERR_CANNOTSENDTOCHAN, "No such channel");
				}
			}
			else if (command.arguments[0].equals(sender.getNickname())){
				IrcReply reply = IrcReply.serverReply(IrcReplyCode.RPL_UMODEIS, sender.getNickname(), sender.getMode(), "");
				sender.sendReply(reply);
			}
		}
	}

	private void handleJoinCommand(IrcCommand command, Connection sender) {
		String[] chans = command.arguments[0].split(",");
		for (String channelName : chans) {
			if (sender.hasJoinedToChannel(channelName)) {
				//Already joined to channel
				return;
			}
			Channel chan = channels.find(channelName);
			if (chan == null) {
				chan = new Channel(channelName);
				channels.add(chan);
			}
			sender.joinChannel(chan);
		}
	}

	private void handleUserCommand(Connection sender) {
		sender.sendSelfNotice("You cannot change userinfo");
	}

	private void handleNickCommand(IrcCommand command, Connection sender) {
		String n = command.arguments[0];
		if (!sender.tryChangeNickname(n)) {
			sender.sendRawString(":" + server.serverName + " " + IrcReplyCode.ERR_NICKNAMEINUSE + " " + n + ":Nickname in use");
		}
	}

	private void handleNotConnectedUserCommand(IrcCommand command,
			Connection sender) {
		if (sender.getUsername() != null) {
			handleUserCommand(sender);
			return;
		}
			
		sender.setUsername(command.arguments[0]);
		sender.setRealname(command.arguments[3]);

		if (sender.getNickname() != null) {
			acceptConnection(sender);
		}
	}

	private void handleNotConnectedNickCommand(IrcCommand command,
			Connection sender) {
		String n = command.arguments[0];
		if (users.trySetNickname(sender, n)) {
			sender.setNickname(n);
			if (sender.getUsername() != null) {
				acceptConnection(sender);
			}
		} else {
			sender.sendServerCommand(IrcReplyCode.ERR_NICKNAMEINUSE,  n + " :Nickname in use");
		}
	}
	
	public void acceptConnection(Connection con) {
		logger.debug("acceptConnection()");
		
		motdService.sendMotd(con);

		con.setMode("+i");

		Channel worldChannel = channels.find(env.getProperty("config.server.WorldChannel"));
		if (worldChannel == null) channels.add(worldChannel);
		con.joinChannel(worldChannel);

		pingService.addPartner(con);
	}
	
	

}
