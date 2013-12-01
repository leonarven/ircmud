package com.cb2.ircmud.ircserver;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.io.IOException;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import com.cb2.ircmud.domain.Player;
import com.github.rlespinasse.slf4j.spring.AutowiredLogger;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class IrcServer {

	private ServerSocket serverSocket;
	@Value("${config.server.name}")
	public String serverName;
	@Value("${config.server.port}")
	public int    serverPort;
	public String globalServerInfo = "";
	public final String VERSION = "0.02";
	
	@Autowired
	private LoginBot loginBot;
	private Map<String, IrcUser> userNicknameMap = new HashMap<String, IrcUser>();
	private Map<String, Channel>       channelMap = new HashMap<String, Channel>();
	
	public ArrayList<String> MOTD = new ArrayList<String>(); 
		
	@AutowiredLogger
	Logger logger;
	@Autowired
	AuthService authService;
	@Autowired
	PingService pingService;
	@Autowired
	Environment env;

	@PostConstruct
	public void init() throws IOException {
		
		logger.info("Initializing IrcServer({}: {})",serverName, serverPort);

		logger.info("Initializing ServerSocket");
		serverSocket = new ServerSocket(serverPort);

		// Init channel Config.WorldChannel
		logger.info("Initializing {}",env.getProperty("config.server.WorldChannel"));
		Channel worldChannel = new Channel( env.getProperty("config.server.WorldChannel"));
		channelMap.put(worldChannel.name, worldChannel);
		
        
       //TODO: No hard-coded admin accounts :P
  		authService.addAccount("admin", "password", Player.ACCESS_ADMIN);
        
		// Initializing IrcCommands
		logger.debug("Initializing IrcCommands");
		//IrcCommand.load(config.ircCommandsXmlFile);
		
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
	
	public boolean trySetNickname(IrcUser user, String nick) {
		nick = nick.toLowerCase();
		synchronized (userNicknameMap) {
			if (userNicknameMap.containsKey(nick)) return false;
			userNicknameMap.put(nick, user);
		}
		return true;
	}
	
	public boolean trySetNickname(IrcUser user, String newNick, String oldNick) {
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
	
	public Channel findChannel(String channelName) {
		channelName = channelName.toLowerCase();
		synchronized (channelMap) {
			if (!channelMap.containsKey(channelName)) return null;
			return channelMap.get(channelName);
		}
	}
	public IrcUser findUserByNickname(String nickName) {
		nickName = nickName.toLowerCase();
		synchronized (userNicknameMap) {
			if (!userNicknameMap.containsKey(nickName)) return null;
			return userNicknameMap.get(nickName);
		}
	}
	public void dropUser(String nickName) {
		if (nickName == null) return;
		logger.info("dropUser({})",nickName);
		nickName = nickName.toLowerCase();
		synchronized (userNicknameMap) {
			if (!userNicknameMap.containsKey(nickName)) return;
			IrcUser u = userNicknameMap.get(nickName);
			userNicknameMap.remove(nickName);
			
			if (u instanceof Connection)
				pingService.dropPartner((Connection)u);
		}
	}
	public void dropConnection(Connection con) {
		logger.info("dropConnection({})",con.getRepresentation());
		String nickName = con.getNickname().toLowerCase();
		synchronized (userNicknameMap) {
			if (!userNicknameMap.containsKey(nickName)) return;

			userNicknameMap.remove(nickName);
			pingService.dropPartner(con);
		}
	}
	
	public void dropChannel(String channelName) {
		logger.debug("dropChannel({})",channelName);
		channelName = channelName.toLowerCase();
		synchronized (channelMap) {
			if (!channelMap.containsKey(channelName)) return;

			channelMap.remove(channelName);
		}
	}
	
	public void addChannel(Channel  chan) {
		logger.debug("addChannel({})",chan.getName());
		channelMap.put(chan.getName().toLowerCase(), chan);
	}
	
	public void run() {
		logger.info("Starting server loop");

		while (true) {
			try {
				Socket	   socket = serverSocket.accept();
				Connection connection = new Connection(socket);
				Thread	   thread = new Thread(connection);
				thread.start();
			} catch(IOException e) {
				logger.error("IOException at Server.run: {}", e.getMessage());
			}
		}
	}
	
	public boolean close() throws IOException {

		serverSocket.close();
	
		return false;
	}
	
	@PreDestroy
	protected void destroy(){
		try {
			close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
