package com.cb2.ircmud;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.io.IOException;

public class IrcServer {
	
	private static ServerSocket serverSocket;
	public static String globalServerName;
	public static int globalServerPort;
	
	private static Map<String, Connection> connectionNicknameMap = new HashMap<String, Connection>();
	private static Map<String, Channel>       channelMap = new HashMap<String, Channel>();
	
	
	
	public static void init(String _globalServerName, int _globalServerPort) throws IOException {
		System.out.println("Initializing IrcServer("+_globalServerName+":"+_globalServerPort+")");

		globalServerPort = _globalServerPort;
		globalServerName = _globalServerName;

		System.out.println("IrcServer: Initializing ServerSocket");
		serverSocket = new ServerSocket(globalServerPort);

		System.out.println("IrcServer: Initializing "+Config.WorldChannel);

		Channel worldChannel = new Channel(Config.WorldChannel);
		channelMap.put(worldChannel.name, worldChannel);
	}
	
	public static boolean trySetNickname(Connection con, String nick) {
		synchronized (connectionNicknameMap) {
			if (connectionNicknameMap.containsKey(nick)) return false;
			connectionNicknameMap.put(nick, con);
		}
		return true;
	}
	
	public static Channel findChannel(String channelName) {
		synchronized (channelMap) {
			if (!channelMap.containsKey(channelName)) return null;
			return channelMap.get(channelName);
		}
	}
	
	public static void addChannel(Channel  chan) {
		channelMap.put(chan.name, chan);
	}
	
	public static boolean run() {
		System.out.println("Starting server loop");
		try {
			while (true) {
				Socket	   socket = serverSocket.accept();
				Connection connection = new Connection(socket);
				Thread	   thread = new Thread(connection);
				thread.start();
			}
		} catch(IOException e) {
			System.err.println("ERROR: IOException at Server.run: " + e.getMessage());
		}
		
		return false;
	}
	
	public static boolean close() throws IOException {

		serverSocket.close();
	
		return false;
	}
}
