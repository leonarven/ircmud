package com.cb2.ircmud.ircserver;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.io.IOException;

import com.cb2.ircmud.Config;
import com.cb2.ircmud.Console;
import com.cb2.ircmud.domain.Player;

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
	
	public static ArrayList<String> MOTD = new ArrayList<String>(); 
		
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
		
		// Try to init pingService
		Console.out("IrcServer", "Initializing AuthService");
        AuthService.init();
        
        AuthService.addAccount("test", "test");
        AuthService.addAccount("foo",  "test");
        AuthService.addAccount("bar",  "test");
		
		// Try to set Loginbot's nickname
		Console.out("IrcServer", "Initializing LoginBot("+loginBot.getUsername()+")");
		trySetNickname(loginBot, loginBot.getUsername());
		
		// Try to init pingService
		Console.out("IrcServer", "Initializing PingService");
		PingService.init(Config.connectionPingTime, Config.connectionPingTimeout);
		
		// Initializing IrcCommands
		Console.debug("Initializing IrcCommands");
		IrcCommand.load(Config.ircCommandsXmlFile);
		
		//TODO: No hard-coded admin accounts :P
		AuthService.addAccount("admin", "password", Player.ACCESS_ADMIN);
		
		
		MOTD = new ArrayList<String>(Arrays.asList(new String(
				  "Tissit on kivoja.\n"
				+ "Niin on kuppikakutkin.\n\n"
				+ "On mahdotonta olla masentunut, jos sinulla on ilmapallo. -Nalle Puh\n\n"
				+ "..................,+777777777777 +,,,,,,,,,,,,,,,,,,,,,,,:::\n"
				+ "................?77777777 7777777777?,,,,,,,,,,,,,,,,,,,::::\n"
				+ "..............,77777777 7 777777777777+,,,,,,,,,,,,,,,,:::::\n"
				+ "............~II7777777777777777777777777,,,,,,,,,,,,,,,,::::\n"
				+ "...........:IIIIII7777777777777777777777I=,:,:,,::::::::::::\n"
				+ "..........,+??IIIII777777777777777777777II::,:::::::::::::::\n"
				+ "..........:++??IIII77777777777777777777III+:::::::::::::::::\n"
				+ "..........:++???IIIII7777777777777777777I?+=::::::::::::::::\n"
				+ "..........=++??????IIII77777777777777777II?+::::::::::::::::\n"
				+ ".,........~++??III??III77777777777777777II?+~~::::::::::::::\n"
				+ "........,,=++???II??I7777777I7777777777IIII+~~~~~~::::::::::\n"
				+ ",,........~++????I???777III777777III777777I?~~~~~~~~~:~~::::\n"
				+ ",,,,.,.~77~,=????????II?++~::~+IIIII??=:,~,,.,:~~~~~~~~~~~~~\n"
				+ ",,,,,,.II=?7:,..=??I,,:=..,.,,:~,+=:,,..::~:::..~~~~~~~~~~~~\n"
				+ ",,,,,,,I?I??I:~......~==??++=++?+~,..,~++=~,..,,~~~~~~~~~~~~\n"
				+ ",,,,,,,+=I?,?++~~=.,,????I??III~, 7=????++=~:,:.=~~~~~~~~~~~\n"
				+ ",,,,,,,,+IIII++?=~=~=??????III:,?I77=++++==~:~.=~~~~~~~~~~~~\n"
				+ ",,,,,,,,:II:I++?I+~+~+++???III:=?I77,+++==~~,=.~=~~~~~~~~~~~\n"
				+ ",,,,,,,,,+=I+++?I??++::~?II?~,II?I77I,,~~~~:.~=~===~~~~~~~~~\n"
				+ ",,,,,,,,,~?I?+++??I=+++?I7II7+,.~?++,=???,.================~\n"
				+ ",,,,,,,,,,~II=++???III?III?7IIIII~~~I??I?=:===============~=\n"
				+ ",,,,,,,,,,,,:~+++???II??I?IIIIIII?II???+?=~==============~==\n"
				+ "::::::,:::..::===++????I??II?IIIIIII?++??=~=================\n"
				+ ":::::::::...~::=~=++????I++====~:=,,,,~I?===================\n"
				+ ":::::::,....7+:~==~+?????+?IIIIIII?I++?I+===================\n"
				+ "::::::......=I+~=+,~????????III??++++???=:~==++=============\n"
				+ ":::,.........I7I=+::=????IIIIIIIII???++~..,,,,,,:~==+=======\n"
				+ "~,............7I+=+::~==+?IIIIIIII?+?+:......,.,,.,,,,======\n"
				+ "...............77+?=:::~~=+III???++++~........,,.,.,,.,~====\n"
				+ "...............~ 7?+?=~~~~~~+===~~:,............,.......,+==\n"
				+ ",................ 7 7+++~~::,,,::,=......................,++\n"
				+ "..................7 7 ?+=~::,,,,.?=.......................,+\n"
				+ "..................,   77+==::,,.,??........................,\n"
				+ "....................I 7   I:.:.? 77.........................\n"
				+ ".....................      7~+=..7 .........................\n"
				+ ",.....................     7......77........................\n"
				+ ".,..,..................I   ~~=~~,.,7........................\n"
				+ "........................  I,::,:,. =........................\n"
				+ ".........................=.,~:=~+~ 7,.......................\n"
				+ "..........................?..::,..77........................\n"
				+ ",..........................7..:~:I= :.......................\n"
				+ ".,...........................,,:~+. 7.......................\n"
				+ ".....,.......................:,:+::?7:.......,..,..,........\n"
				+ "..,...........................,?:+:. ?.......,......,.,.....\n"
				+ "......,..,.....................~:~,.= ...........,..,..,.   \n"
				+ "..........,.....................=,,:.+........,..,.,,...I   \n"
				+ "...,...,...,,......................,..:.......,..,....,~77  \n"
				+ ".......,...,..,...................~........,..........~7    \n"
				+ "....,...,....,........................................+  77:\n"
				+ "....,...,...,,.,,......,...................,..........?  ...\n").split("\n")));
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
		if (nickName == null) return;
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
