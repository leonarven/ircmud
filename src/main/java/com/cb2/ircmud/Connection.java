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

public class Connection  implements Runnable {

	public String nick;
	public String mode;
	public String realname;
	public String username;
	public String hostname;

	private InetSocketAddress address;
	private Socket socket = null;
	private Map<String, Channel> joinedChannels = new HashMap<String, Channel>();
	
	public Connection(Socket socket){
		this.socket = socket;
	}

	private LinkedBlockingQueue<String> outQueue = new LinkedBlockingQueue<String>(1000);
	public ArrayList<IrcCommand> commandQuery = new ArrayList<IrcCommand>();
	
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
				System.err.println("Outqueue died");
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

	public String getRepresentation() {
		return this.nick + "!" + this.username + "@" + this.hostname;
	}
	

	public void sendRawString(String s) {
		System.out.println("Sending line to " + nick + ": " + s);
		outQueue.add(s);
	}
	
	public void sendPrivateMessage(String sender, String target, String msg) {
		sendRawString(":" + sender + " PRIVMSG " + target + " :" + msg);
	}

	public void sendServerCommand(String command, String string) {
		sendRawString(":" + Server.globalServerName + " " + command + " " + nick + " :" + string);
	}
	public void sendCommand(String command, String string) {
		sendRawString(":" + getRepresentation() + " " + command + " :" + string);
	}
	public void sendSelfNotice(String string) {
		sendServerCommand("NOTICE", string);
	}

	public boolean joinChannel(String channelName) {
		Channel chan = Server.findChannel(channelName);
		if (chan == null) return false;
		chan.addConnection(this);
		joinedChannels.put(channelName, chan);
		
		return true;
	}
	public boolean leaveChannel(String channelName, String msg) {
		
		if (!joinedChannels.containsKey(channelName)) return false;
		joinedChannels.remove(channelName);
		Channel chan = Server.findChannel(channelName);
		if (chan == null) return false;
		chan.sendRawStringAll(":" + this.getRepresentation() + " PART " + channelName + " :" + msg);
		chan.removeConnection(this);
		return true;
	}
	
	public boolean quit(String msg) {
		for (Map.Entry<String, Channel> entry : this.joinedChannels.entrySet()) {
			entry.getValue().removeConnection(this);
			entry.getValue().sendRawStringAll(":" + this.getRepresentation() + " QUIT :" + msg);
		}
		return true;
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
		
		sendServerCommand("375", Server.globalServerName+" - Message Of The Day:");
		sendServerCommand("372", "Tissit on kivoja.");
		sendServerCommand("372", "Niin on kuppikakutkin.");
		sendServerCommand("372", "");
		sendServerCommand("372", "On mahdotonta olla masentunut, jos sinulla on ilmapallo. -Nalle Puh");
		sendServerCommand("376", "End of /MOTD command.");

		this.mode = "+i";
		sendServerCommand("MODE", this.mode);

		Server.findChannel("#world").addConnection(this);
		this.joinedChannels.put("#world", Server.findChannel("#world"));
	}
	
	private void processLine(String line) throws Exception {
		
		if (line == null) return;

		System.out.println("Processing line from " + nick + ": " + line);
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
			try {
				commandObject = IrcCommand.valueOf(command.toUpperCase());
			} catch (Exception e) {
			}
		}
		if (commandObject == null) {
			sendRawString(":" + Server.globalServerName + " 421 " + nick + " " + command + " :Unknown command ");
			return;
		}
		if (arguments.length < commandObject.getMin() || arguments.length > commandObject.getMax()) {
			
			//TODO: Use command
			sendSelfNotice("Invalid number of arguments for this" + " command, expected not more than " + commandObject.getMax() + " and not less than " + commandObject.getMin() + " but got " + arguments.length + " arguments");
			return;
		}
		try {
			commandObject.init(this, prefix, arguments);
		} catch(Exception e) {
			System.err.println("ERROR at processLine: "+e.getMessage());
			e.printStackTrace();
		}

		act(commandObject);
	}
	
	public void act(IrcCommand command) throws Exception {
		if (this.nick == null || this.username == null) {
			switch(command) {
				case NICK:
					String n = command.arguments[0];
					if (Server.trySetNickname(this, n)) {
						this.nick = n;
						sendSelfNotice("Nick changed to "+this.nick);
						if (this.username != null) {
							acceptConnection();
						}
					} else {
						sendRawString(":" + Server.globalServerName + " 433 " + n + ":Nickname in use");
					}
					break;
				case USER:
					if (this.username != null) {
						sendSelfNotice("You cannot change userinfo");
						break;
					}
						
					this.username = command.arguments[0];
					this.realname = command.arguments[3];
	
					if (this.nick != null) {
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
					if (Server.trySetNickname(this, n)) {
						this.nick = n;
						sendSelfNotice("Nick changed to "+this.nick);
						if (this.username != null) {
							acceptConnection();
						}
					} else {
						sendRawString(":" + Server.globalServerName + " 433 " + n + ":Nickname in use");
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
	                	Channel chan = Server.findChannel(channelName);
	                	if (chan == null) {
							chan = new Channel(channelName);
							Server.addChannel(chan);
	                	}
						chan.addConnection(this);
						synchronized (joinedChannels) {
							joinedChannels.put(channelName, chan);
						}
	                }
					break;
				case MODE:
					//TODO: Implement modes
					if (command.arguments.length >= 2)
						sendSelfNotice("This server does not allow to change usermode");
					else {
						if (command.arguments[0].startsWith("#")) { //Channel
							sendRawString(":" + Server.globalServerName + " 324 " + this.nick + " " + command.arguments[0] + " +stn");
						}
						else if (command.arguments[0].equals(this.nick)){
							sendRawString(":" + Server.globalServerName + " 221 " + this.nick + " +i");
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
						joinedChannels.get(command.arguments[0]).sendPrivateMessage(this, command.arguments[1]);
					} else {
						this.sendCommand("404", "No such channel");
					}
					break;
				default:
					System.err.println("Unhandled IrcCommand");
			}
			
		}
	}
	
	@Override
	public void run() {
		sendRawString(":" + Server.globalServerName + " 020 :Please wait while we process your connection");

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
					System.err.println("ERROR: Exception in Connection.run: " + e.getMessage() + "\n Stacktrace: "); e.printStackTrace();
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
		
}
