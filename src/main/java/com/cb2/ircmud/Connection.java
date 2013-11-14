package com.cb2.ircmud;

import java.io.BufferedReader;
import java.io.OutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.HashMap;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;




import com.cb2.ircmud.IrcCommand;

public class Connection extends IrcUser implements Runnable {
	private InetSocketAddress address;
	private Socket socket = null;
	private Map<String, Channel> joinedChannels = new HashMap<String, Channel>();
	
	public Connection(Socket socket){
		this.socket = socket;
	}

	private LinkedBlockingQueue<String> outQueue = new LinkedBlockingQueue<String>(1000);
	
	private Thread outThread = new Thread() {
		public void run() {
			try {
				OutputStream out = socket.getOutputStream();
				while (true) {
					String s = outQueue.take();
					s = s.replace("\n", "").replace("\r", "");
					s = s + "\r\n";
					out.write(s.getBytes("UTF-8"));
					out.flush();
				}
			} catch (Exception e) {
				System.err.println("Connection:outThread: Outqueue died");
				outQueue.clear();
				outQueue = null;
				e.printStackTrace();
				try {
					closeConnection();
				} catch (Exception e2) {
					e2.printStackTrace();
				}
			}
		}
	};
	

	public void sendRawString(String s) {
		System.out.println("Sending line to " + nickname + ": " + s);
		outQueue.add(s);
	}
	
	public void sendPrivateMessage(String sender, String target, String msg) {
		sendRawString(":" + sender + " PRIVMSG " + target + " :" + msg);
	}

	public void sendServerCommand(String command, String string) {
		sendRawString(":" + IrcServer.globalServerName + " " + command + " " + nickname + " :" + string);
	}
	public void sendCommand(String command, String string) {
		sendRawString(":" + getRepresentation() + " " + command + " :" + string);
	}
	public void sendSelfNotice(String string) {
		sendServerCommand("NOTICE", string);
	}
	
	public void closeConnection() throws IOException {
		//TODO: Cleaner closing
		synchronized (socket) {
			if (socket.isConnected())
				socket.close();
		}
	}
	
	public void acceptConnection() {
		sendSelfNotice("Connection accepted, "+getRepresentation()+"("+realname+")");
		
		sendServerCommand("375", IrcServer.globalServerName+" - Message Of The Day:");
		sendServerCommand("372", "Tissit on kivoja.");
		sendServerCommand("372", "Niin on kuppikakutkin.");
		sendServerCommand("372", "");
		sendServerCommand("372", "On mahdotonta olla masentunut, jos sinulla on ilmapallo. -Nalle Puh");
		sendServerCommand("376", "End of /MOTD command.");

		this.mode = "+i";
		sendServerCommand("MODE", this.mode);

		this.joinChannel(IrcServer.findChannel("#world"));
	}
	
	private void processLine(String line) throws Exception {
		
		if (line == null) return;

		System.out.println("Processing line from " + nickname + ": " + line);
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
			IrcCommand.valueOf(command.toLowerCase());
		} catch (Exception e) {
		}
		if (commandObject == null) {
			commandObject = IrcCommand.valueOf(command.toUpperCase());
		}
		if (commandObject == null) {
			sendRawString(":" + IrcServer.globalServerName + " 421 " + nickname + " " + command + " :Unknown command ");
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
						sendRawString(":" + IrcServer.globalServerName + " 433 " + n + ":Nickname in use");
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
					
					//TODO: Fix me
					String n = command.arguments[0];
					if (IrcServer.trySetNickname(this, n)) {
						this.nickname = n;
						sendSelfNotice("Nick changed to "+this.nickname);
						if (this.username != null) {
							acceptConnection();
						}
					} else {
						sendRawString(":" + IrcServer.globalServerName + " 433 " + n + ":Nickname in use");
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
							IrcReply reply = new IrcReply(IrcServer.globalServerName, "368", command.arguments[0] + " :End of channel ban list");
							this.sendReply(reply);
							break;
						}
						sendSelfNotice("This server does not allow to change usermode");
					}
					else {
						if (Channel.isValidPrefix(command.arguments[0].charAt(0))) { //Channel
							IrcReply reply = new IrcReply(IrcServer.globalServerName, "324",  this.nickname + " " + command.arguments[0] + " +stn");
							this.sendReply(reply);
						}
						else if (command.arguments[0].equals(this.nickname)){
							IrcReply reply = new IrcReply(IrcServer.globalServerName, "221", this.nickname + " +i");
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
					if (joinedChannels.containsKey(command.arguments[0])) {
						joinedChannels.get(command.arguments[0]).sendReplyToAllExceptSender(new IrcReply(this, "PRIVMSG", command.arguments[0] + " :" + command.arguments[1]));
					} else {
						this.sendCommand("404", "No such channel");
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
				default:
					System.err.println("Unhandled IrcCommand");
			}
			
		}
	}
	
	@Override
	public void run() {
		sendRawString(":" + IrcServer.globalServerName + " 020 :Please wait while we process your connection");

		try {
			
			this.address = (InetSocketAddress) socket.getRemoteSocketAddress();
			this.hostname = address.getAddress().getHostAddress();
			System.out.println("Connection from host " + hostname);

			outThread.start();

			InputStream socketIn = socket.getInputStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader(socketIn, "UTF-8"));
			String line;
			
			while ((line = reader.readLine()) != null) {
				try {
					processLine(line);
				} catch (Exception e) {
					System.err.println("ERROR: Exception in Connection.run: " + e.getMessage() + "\n Stacktrace: ");
					e.printStackTrace();
				}
			}
		} catch (IOException e) {
			try {
				closeConnection();
			} catch (IOException e2) {
			}
			System.err.println("IOException in Connection::run : " + e.getMessage());
		} finally {
			
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
