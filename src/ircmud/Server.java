package ircmud;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.io.IOException;

public class Server {
	
	private static ServerSocket serverSocket;
	public static String globalServerName;
	public static int globalServerPort;
	public static Map<String, Connection> connectionMap = new HashMap<String, Connection>();
	public static Map<String, Channel>       channelMap = new HashMap<String, Channel>();
	
	public static void init(String globalServerName, int globalServerPort) throws IOException {
		System.out.println("Initializing Server("+globalServerName+":"+globalServerPort+")");

		globalServerPort = globalServerPort;
		globalServerName = globalServerName;

		System.out.println("Initializing ServerSocket");
		serverSocket = new ServerSocket(globalServerPort);
		
		Channel worldChannel = new Channel(Config.WorldChannel);
		channelMap.put(worldChannel.name, worldChannel);
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

		}
		
		return false;
	}
	
	public static boolean close() throws IOException {

		serverSocket.close();
	
		return false;
	}
}
