package ircmud;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.io.IOException;

public class Server {
	
	private ServerSocket serverSocket;
	private String globalServerName;
	private int globalServerPort;
    public static Map<String, Connection> connectionMap = new HashMap<String, Connection>();
    public static Map<String, Channel>       channelMap = new HashMap<String, Channel>();
	
	public Server(String globalServerName, int globalServerPort) throws IOException {
		System.out.println("Initializing Server("+globalServerName+":"+globalServerPort+")");

		this.globalServerPort = globalServerPort;
		this.globalServerName = globalServerName;

		System.out.println("Initializing ServerSocket");
		this.serverSocket = new ServerSocket(this.globalServerPort);		
	}
	
	public boolean run() {
		System.out.println("Starting server loop");

		try {
			while (true) {
				Socket         socket = serverSocket.accept();
				Connection connection = new Connection(socket, this.globalServerName);
				Thread         thread = new Thread(connection);
				thread.start();
			}
		} catch(IOException e) {

		}
		
		return false;
	}
	
	public boolean close() throws IOException {

		this.serverSocket.close();
	
		return false;
	}
	

}
