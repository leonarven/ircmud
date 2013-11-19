package com.cb2.ircmud.ircserver;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.io.IOException;

import com.cb2.ircmud.Config;
import com.cb2.ircmud.Console;

import org.aspectj.weaver.patterns.ThisOrTargetAnnotationPointcut;

public class IrcServer {
	
	private static ServerSocket serverSocket;
	public static String globalServerName;
	public static int    globalServerPort;
	public static String globalServerInfo = "";
	public static final String VERSION = "0.02";
	
	private static LoginBot loginBot = new LoginBot("LoginBot", "LoginBot");
	private static Map<String, IrcUser> userNicknameMap = new HashMap<String, IrcUser>();
	private static Map<String, Channel>       channelMap = new HashMap<String, Channel>();
		
	public static void init(String _globalServerName, int _globalServerPort) throws IOException {
		Console.out("IrcServer", "Initializing IrcServer("+_globalServerName+":"+_globalServerPort+")");

		globalServerPort = _globalServerPort;
		globalServerName = _globalServerName;

		Console.out("IrcServer", "Initializing ServerSocket");
		serverSocket = new ServerSocket(globalServerPort);

		// Init channel Config.WorldChannel
		Console.out("IrcServer", "Initializing "+Config.WorldChannel);
		Channel worldChannel = new Channel(Config.WorldChannel);
		channelMap.put(worldChannel.name, worldChannel);
		
		// Try to set Loginbot's nickname
		Console.out("IrcServer", "Initializing LoginBot("+loginBot.getUsername()+")");
		trySetNickname(loginBot, loginBot.getUsername());
		
		// Try to init pingService
		Console.out("IrcServer", "Initializing PingService");
		PingService.init(Config.connectionPingTime, Config.connectionPingTimeout);
		
		// Initializing IrcCommands
		Console.debug("Initializing IrcCommands");
		IrcCommand.load(Config.ircCommandsXmlFile);
	}
	
	public static boolean trySetNickname(IrcUser user, String nick) {
		nick = nick.toLowerCase();
		synchronized (userNicknameMap) {
			if (userNicknameMap.containsKey(nick)) return false;
			userNicknameMap.put(nick, user);
		}
		return true;
	}
	
	public static boolean trySetNickname(IrcUser user, String newNick, String oldNick) {
		newNick = newNick.toLowerCase();
		oldNick = oldNick.toLowerCase();
		synchronized (userNicknameMap) {
			if (userNicknameMap.containsKey(newNick)) return false;
			if (userNicknameMap.containsKey(oldNick)) {
				userNicknameMap.remove(oldNick);
			}
			userNicknameMap.put(newNick, user);
		}
		return true;
	}
	
	public static Channel findChannel(String channelName) {
		channelName = channelName.toLowerCase();
		synchronized (channelMap) {
			if (!channelMap.containsKey(channelName)) return null;
			return channelMap.get(channelName);
		}
	}
	public static IrcUser findUserByNickname(String nickName) {
		nickName = nickName.toLowerCase();
		synchronized (userNicknameMap) {
			if (!userNicknameMap.containsKey(nickName)) return null;
			return userNicknameMap.get(nickName);
		}
	}
	public static void dropUser(String nickName) {
		Console.debug("IrcServer::dropUser("+nickName+")");
		nickName = nickName.toLowerCase();
		synchronized (userNicknameMap) {
			if (!userNicknameMap.containsKey(nickName)) return;

			userNicknameMap.remove(nickName);
			PingService.dropPartner(nickName);
		}
	}
	public static void dropChannel(String channelName) {
		Console.debug("IrcServer::dropChannel("+channelName+")");
		channelName = channelName.toLowerCase();
		synchronized (channelMap) {
			if (!channelMap.containsKey(channelName)) return;

			channelMap.remove(channelName);
		}
	}
	
	public static void addChannel(Channel  chan) {
		Console.debug("IrcServer::addChannel("+chan.getName()+")");
		channelMap.put(chan.getName().toLowerCase(), chan);
	}
	
	public static void run() {
		Console.out("IrcServer", "Starting server loop");

		while (true) {
			try {
				Socket	   socket = serverSocket.accept();
				Connection connection = new Connection(socket);
				Thread	   thread = new Thread(connection);
				thread.start();
			} catch(IOException e) {
				System.err.println("IrcServer: IOException at Server.run: " + e.getMessage());
			}
		}
	}
	
	public static boolean close() throws IOException {

		serverSocket.close();
	
		return false;
	}
}
