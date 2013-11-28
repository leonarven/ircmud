package com.cb2.ircmud.ircserver;

import java.io.BufferedReader;
import java.io.OutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;
import java.util.HashMap;
import java.util.Queue;
import java.util.Date;
import java.util.concurrent.LinkedBlockingQueue;








import com.cb2.ircmud.ircserver.IrcCommand;

public class Connection extends IrcUser implements Runnable {
	private InetSocketAddress address;
	private Socket socket = null;
	
	public Connection(Socket socket){
		this.socket = socket;
	}

	private LinkedBlockingQueue<String> outQueue = new LinkedBlockingQueue<String>(1000);
	
	private Thread outThread = new Thread() {
		public void run() {
			try {
				OutputStream out = socket.getOutputStream();
				while (keepRunning) {
					String s = outQueue.take();
					s = s.replace("\n", "").replace("\r", "");
					s = s + "\r\n";
					out.write(s.getBytes("UTF-8"));
					out.flush();
				}
			} catch (Exception e) {
				System.err.println("Connection: outThread: Outqueue died");
				outQueue.clear();
				outQueue = null;
			}
			try {
				closeConnection();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
	};

	public void sendRawString(String s) {
		System.out.println(nickname + " <<< " + s);
		outQueue.add(s);
	}
	
	public void sendPrivateMessage(String sender, String target, String msg) {
		sendRawString(":" + sender + " PRIVMSG " + target + " :" + msg);
	}

	public void sendServerCommand(Object command, String string) {
		sendRawString(":" + IrcServer.globalServerName + " " + command.toString() + " " + nickname + " :" + string);
	}

	public void sendServerReply(Object command, String string) {
		sendRawString(":" + IrcServer.globalServerName + " " + command.toString() + " " + nickname + " " + string);
	}

	public void sendCommand(Object command, String string) {
		sendRawString(":" + getRepresentation() + " " + command.toString() + " :" + string);
	}

	public void sendSelfNotice(String string) {
		sendServerCommand("NOTICE", string);
	}
	
	public void closeConnection() throws IOException {
		//TODO: Cleaner closing
		synchronized (socket) {
			if (socket.isConnected())
				socket.close();
			IrcServer.dropUser(this.nickname);
		}
	}
	
	public void sendPing(String ping) {
		sendRawString("PING :"+ping);
	}
	
	public void acceptConnection() {
		sendServerCommand(IrcReplyCode.RPL_WELCOME, "Welcome to "+IrcServer.globalServerName+", "+getRepresentation()+"("+realname+")");
		sendServerCommand(IrcReplyCode.RPL_YOURHOST, "Your host is "+IrcServer.globalServerName+", running version "+IrcServer.VERSION);

		sendServerReply(IrcReplyCode.RPL_BOUNCE, "RFC2812 PREFIX=(ov)@+ CHANTYPES=#&!+ MODES=3 CHANLIMIT=#&!+:21 :are supported by this server");
		sendServerReply(IrcReplyCode.RPL_BOUNCE, "NICKLEN=15 TOPICLEN=255 KICKLEN=255 CHANNELLEN=50 IDCHAN=!:5 :are supported by this server");
		sendServerReply(IrcReplyCode.RPL_BOUNCE, "PENALTY FNC EXCEPTS=e INVEX=I CASEMAPPING=ascii NETWORK=IrcMud :are supported by this server");

		sendServerCommand(IrcReplyCode.RPL_MOTDSTART, IrcServer.globalServerName+" - Message Of The Day:");
	    Iterator<String> itr = IrcServer.MOTD.iterator();
	    while (itr.hasNext())
			sendServerCommand(IrcReplyCode.RPL_MOTD, itr.next());
		sendServerCommand(IrcReplyCode.RPL_ENDOFMOTD, "End of /MOTD command.");

		this.mode = "+i";

		Channel worldChannel = IrcServer.findChannel("#world");
		if (worldChannel == null) IrcServer.addChannel(worldChannel);
		this.joinChannel(worldChannel);

		PingService.addPartner(this);
	}
	
	private void processLine(String line) throws Exception {
		
		if (line == null) return;

		System.out.println(nickname + " >>> " + line);
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
		} catch(IllegalArgumentException e) {}
		if (commandObject == null) {
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
	
	public void act(IrcCommand command) throws Exception {
		String mask;
		
		if (this.nickname == null || this.username == null) {
			switch(command) {
				case NICK:
					String n = command.arguments[0];
					if (IrcServer.trySetNickname(this, n)) {
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
						sendRawString(":" + IrcServer.globalServerName + " " + IrcReplyCode.ERR_NICKNAMEINUSE + " " + n + ":Nickname in use");
					}
					break;
				case USER:
					sendSelfNotice("You cannot change userinfo");
					break;
				case JOIN:
					String[] channels = command.arguments[0].split(",");
	                for (String channelName : channels) {
	                	if (joinedChannels.containsKey(channelName)) {
	                		//Already joined to channel
	                		break;
	                	}
	                	Channel chan = IrcServer.findChannel(channelName);
	                	if (chan == null) {
							chan = new Channel(channelName);
							IrcServer.addChannel(chan);
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
							Channel channel = IrcServer.findChannel(command.arguments[0]);
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
					socket.close();
					break;
				case PRIVMSG:
					String target  = command.arguments[0];
					if (Channel.isValidPrefix(target.charAt(0))) { //Channel
						if (IrcServer.findChannel(target) != null) {
							if (joinedChannels.containsKey(target)) {
								joinedChannels.get(command.arguments[0]).sendReplyToAllExceptSender(new IrcReply(this, "PRIVMSG", target, command.arguments[1]));
							} else {
								this.sendCommand(IrcReplyCode.ERR_CANNOTSENDTOCHAN, "Cannot send to channels you have not joined");
							}
						}
						else {
							this.sendCommand(IrcReplyCode.ERR_NOSUCHCHANNEL, "No such channel");
						}
					}
					else {
						IrcUser user = IrcServer.findUserByNickname(target);
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
					String op = "";
					if (command.arguments.length == 2)
						op = command.arguments[1];
					if (Channel.isValidPrefix(mask.charAt(0))) {
						if (joinedChannels.containsKey(mask)) {
							Channel channel = IrcServer.findChannel(mask);
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
					
					IrcUser con = IrcServer.findUserByNickname(mask);
					if (con != null) {
						
						this.sendWhoIsReply(con);
						
					} else {
						// TODO ERR_NOSUCHNICK = 401 
						sendSelfNotice("No such nick");
					}
					break;
				case PING:
					this.sendServerReply("PONG", IrcServer.globalServerName+" :"+IrcServer.globalServerName);
					break;
				case PONG:
					PingService.pongFrom(this);
					break;
				default:
					System.err.println("Unhandled IrcCommand");
			}
			
		}
	}
	
	@Override
	public void run() {
		sendReply(IrcReply.serverReply("020", "Please wait while we process your connection"));

		try {
			
			this.address = (InetSocketAddress) socket.getRemoteSocketAddress();
			this.hostname = address.getAddress().getHostAddress();
			System.out.println("Connection from host " + hostname);

			outThread.start();

			InputStream socketIn = socket.getInputStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader(socketIn, "UTF-8"));
			String line;
			
			while ((line = reader.readLine()) != null && this.keepRunning) {
				try {
					processLine(line);
				} catch (Exception e) {
					System.err.println("ERROR: Exception in Connection.run: " + e.getMessage() + "\n Stacktrace: ");
					e.printStackTrace();
				}
			}
		} catch (IOException e) {
			System.err.println("IOException in Connection::run : " + e.getMessage());
		} finally {
			try {
				closeConnection();
			} catch (IOException e2) {
			}
		}		
	}

	@Override
	void sendReply(IrcReply reply) {
		this.sendRawString(reply.toString());
	}

	@Override
	boolean isConnection() {
		return true;
	}
		
}
