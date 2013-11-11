package ircmud;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.io.IOException;

public class Server {
	
	private ServerSocket serverSocket;
    public static Map<String, Connection> connectionMap = new HashMap<String, Connection>();
	
	public Server(ServerSocket serverSocket) {
		System.out.println("Initializing server");
		this.serverSocket = serverSocket;		
	}
	
	public boolean run() {
		System.out.println("Starting server loop");

		try {
			while (true) {
				Socket         socket = serverSocket.accept();
				Connection connection = new Connection(socket);
				Thread         thread = new Thread(connection);
				thread.start();
			}
		} catch(IOException e) {

		}
		
		return false;
	}

}
