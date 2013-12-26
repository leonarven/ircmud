package com.cb2.ircmud.ircserver;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.LinkedBlockingQueue;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.core.env.Environment;

import com.cb2.ircmud.ircserver.IrcCommand;
import com.cb2.ircmud.ircserver.services.ChannelService;
import com.cb2.ircmud.ircserver.services.MotdService;
import com.cb2.ircmud.ircserver.services.UserService;
import com.github.rlespinasse.slf4j.spring.AutowiredLogger;

@Configurable
public class Connection extends IrcUser {
	private InetSocketAddress address;
	private AsynchronousSocketChannel socketChannel = null;
	private ByteBuffer readBuffer = ByteBuffer.allocate(512);
	private volatile boolean writing = false;

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
	
	
	public Connection(AsynchronousSocketChannel socketChannel){
		this.socketChannel = socketChannel;
	}

	private LinkedBlockingQueue<String> outQueue = new LinkedBlockingQueue<String>(100);

	public void sendRawString(String s) {
		logger.debug("{} <<< {}", nickname, s);
		outQueue.add(s);
	}
	
	public void sendPrivateMessage(String sender, String target, String msg) {
		sendRawString(":" + sender + " PRIVMSG " + target + " :" + msg);
	}

	public void sendServerCommand(Object command, String string) {
		sendRawString(":" + server.serverName + " " + command.toString() + " " + nickname + " :" + string);
	}

	public void sendServerReply(Object command, String string) {
		sendRawString(":" +server.serverName + " " + command.toString() + " " + nickname + " " + string);
	}

	public void sendCommand(Object command, String string) {
		sendRawString(":" + getRepresentation() + " " + command.toString() + " :" + string);
	}

	public void sendSelfNotice(String string) {
		sendServerCommand("NOTICE", string);
	}
	
	public void closeConnection() {
		//TODO: Cleaner closing
		synchronized (socketChannel) {
			if (socketChannel.isOpen()) {
				try {
					socketChannel.close();
				}
				catch (IOException e) {
					logger.error("{}: IOException while closing socket channel {}", getRepresentation(), e.getMessage());
				}
			}
			users.dropUser(this.nickname);
		}
	}
	
	public void sendPing(String ping) {
		sendRawString("PING :"+ping);
	}
	
	public void acceptConnection() {
		logger.debug("acceptConnection()");
		
		motdService.sendMotd(this);

		this.mode = "+i";

		Channel worldChannel = channels.find(env.getProperty("config.server.WorldChannel"));
		if (worldChannel == null) channels.add(worldChannel);
		this.joinChannel(worldChannel);

		pingService.addPartner(this);
	}
	
	private void processLine(String line) {
		
		if (line == null) return;

		logger.debug("{} >>> {}",nickname ,line);
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
			sendServerReply(IrcReplyCode.ERR_UNKNOWNCOMMAND, command + " :Unknown command ");
			return;
		}
		if (arguments.length < commandObject.getMin() || arguments.length > commandObject.getMax()) {
			//TODO: Use command
			sendSelfNotice("Invalid number of arguments for this" + " command, expected not more than " + commandObject.getMax() + " and not less than " + commandObject.getMin() + " but got " + arguments.length + " arguments");
			return;
		}
		commandObject.init(this, prefix, arguments);

		act(commandObject);
	}
	
	public void act(IrcCommand command) {
		String mask;
		
		if (this.nickname == null || this.username == null) {
			switch(command) {
				case NICK:
					String n = command.arguments[0];
					if (users.trySetNickname(this, n)) {
						this.nickname = n;
						sendSelfNotice("Nick changed to "+this.nickname);
						if (this.username != null) {
							acceptConnection();
						}
					} else {
						sendServerCommand(IrcReplyCode.ERR_NICKNAMEINUSE,  n + " :Nickname in use");
					}
					break;
				case USER:
					if (this.username != null) {
						sendSelfNotice("You cannot change userinfo");
						break;
					}
						
					this.username = command.arguments[0];
					this.realname = command.arguments[3];
	
					if (this.nickname != null) {
						acceptConnection();
					}
	
					break;
				default:
					sendRawString("ERROR :Closing connection " + getRepresentation() + " (Invalid command)");
			}
		}
		else { //Connection established
			switch(command) {
				case NICK:
					String n = command.arguments[0];
					if (!this.tryChangeNickname(n)) {
						sendRawString(":" + server.serverName + " " + IrcReplyCode.ERR_NICKNAMEINUSE + " " + n + ":Nickname in use");
					}
					break;
				case USER:
					sendSelfNotice("You cannot change userinfo");
					break;
				case JOIN:
					String[] chans = command.arguments[0].split(",");
	                for (String channelName : chans) {
	                	if (joinedChannels.containsKey(channelName)) {
	                		//Already joined to channel
	                		break;
	                	}
	                	Channel chan = channels.find(channelName);
	                	if (chan == null) {
							chan = new Channel(channelName);
							channels.add(chan);
	                	}
	                	this.joinChannel(chan);
	                }
					break;
				case MODE:
					//TODO: Implement modes
					if (command.arguments.length >= 2) {
						if (command.arguments[1].equals("b") && Channel.isValidPrefix(command.arguments[0].charAt(0))) { //Ban list
							IrcReply reply = IrcReply.serverReply(IrcReplyCode.RPL_ENDOFBANLIST, this.getNickname(), command.arguments[0], "End of channel ban list");
							this.sendReply(reply);
							break;
						}
						sendSelfNotice("This server does not allow to change usermode");
					}
					else {
						if (Channel.isValidPrefix(command.arguments[0].charAt(0))) { //Channel
							Channel channel = channels.find(command.arguments[0]);
							if (channel != null) { 
								IrcReply reply = IrcReply.serverReply(IrcReplyCode.RPL_CHANNELMODEIS,  this.nickname, command.arguments[0], channel.mode, "");
								this.sendReply(reply);
							} else {
								this.sendCommand(IrcReplyCode.ERR_CANNOTSENDTOCHAN, "No such channel");
							}
						}
						else if (command.arguments[0].equals(this.nickname)){
							IrcReply reply = IrcReply.serverReply(IrcReplyCode.RPL_UMODEIS, this.nickname, this.mode, "");
							this.sendReply(reply);
						}
					}
					break;
				case PART:
					if (command.arguments.length == 2)
						leaveChannel(command.arguments[0], command.arguments[1]);
					else
						leaveChannel(command.arguments[0], "");
					break;
				case QUIT:
					quit(command.arguments[0]);
					closeConnection();
					break;
				case PRIVMSG:
					String target  = command.arguments[0];
					if (Channel.isValidPrefix(target.charAt(0))) { //Channel
						if (channels.find(target) != null) {
							if (joinedChannels.containsKey(target)) {
								joinedChannels.get(target).sendReplyToAllExceptSender(new IrcReply(this, "PRIVMSG", target, command.arguments[1]));
							} else {
								this.sendCommand(IrcReplyCode.ERR_CANNOTSENDTOCHAN, "Cannot send to channels you have not joined");
							}
						}
						else {
							this.sendCommand(IrcReplyCode.ERR_NOSUCHCHANNEL, "No such channel");
						}
					}
					else {
						IrcUser user = users.findUserByNickname(target);
						if (user != null) {
							user.sendMessage(this, command.arguments[1]);
						}
						else {
							this.sendCommand(IrcReplyCode.ERR_NOSUCHNICK, "No such nick");
						}
					}
					break;
				case WHO:
					mask = command.arguments[0];
					String op;
					if (command.arguments.length == 2)
						op = command.arguments[1];
					if (Channel.isValidPrefix(mask.charAt(0))) {
						if (joinedChannels.containsKey(mask)) {
							Channel channel = channels.find(mask);
							if (channel != null) {
								this.sendWhoReply(channel);
							} else {
								sendSelfNotice("Channel does not exits"); //ERR_NOSUCHCHANNEL = 403
							}
						} else {
							sendSelfNotice("Server does not allow to asking about channels you are not in to");
						}
					} else {
						// TODO
						sendSelfNotice("Server does not allow to asking about users");
					}
					break;
				case WHOIS:
					mask = command.arguments[0];
					
					IrcUser con = users.findUserByNickname(mask);
					if (con != null) {
						
						this.sendWhoIsReply(con);
						
					} else {
						// TODO ERR_NOSUCHNICK = 401 
						sendSelfNotice("No such nick");
					}
					break;
				case PING:
					this.sendServerReply("PONG", server.serverName+" :"+server.serverName);
					break;
				case PONG:
					pingService.pongFrom(this);
					break;
				default:
					logger.error("Unhandled IrcCommand");
			}
			
		}
	}
	
	public void initialize() {
		try {
			this.address = (InetSocketAddress) socketChannel.getRemoteAddress();
			this.hostname = address.getAddress().getHostAddress();
			logger.info("Connection from host {}", hostname);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		send020Reply();
	}
	
	public void send020Reply() {
		sendReply(IrcReply.serverReply("020", "Please wait while we process your connection"));	
	}
	
	public void initAsynchronousRead() {
		socketChannel.read(readBuffer, null, new CompletionHandler<Integer, Object>() {

			@Override
			public void completed(Integer result, Object attachment) {
				int readBytes = result.intValue();
				readBuffer.flip();
				String data = new String(readBuffer.array(), 0, readBytes, server.getCharset());
				String[] lines = data.split("\r\n");
				for (String line : lines) {
					if (!line.isEmpty())
						processLine(line);
				}
				readBuffer.clear();
				
				socketChannel.read(readBuffer, null, this);
			}

			@Override
			public void failed(Throwable exc, Object attachment) {
				logger.error("{}: Socket channel read error {}", getNickname(), exc.getMessage());
			}
		});
	}
	
	public void asynchronousHandleOutQueue() {
		String data;
		if (!writing && (data = outQueue.poll()) != null) {
			writing = true;
			socketChannel.write(ByteBuffer.wrap((data + "\r\n").getBytes(server.getCharset())), null, new CompletionHandler<Integer, Object>() {

				@Override
				public void completed(Integer result, Object attachment) {
					String data;
					if ((data = outQueue.poll()) != null) {
						socketChannel.write(ByteBuffer.wrap((data + "\r\n").getBytes(server.getCharset())), null, this);
					} else {
						writing = false;
					}
				}

				@Override
				public void failed(Throwable exc, Object attachment) {
					logger.error("{}: Socket channel write error {}", getNickname(), exc.getMessage());
				}
			});
		}
	}

	@Override
	public void sendReply(IrcReply reply) {
		this.sendRawString(reply.toString());
	}

	@Override
	public boolean isConnection() {
		return true;
	}
		
}
