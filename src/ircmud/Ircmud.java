package ircmud;

import java.net.ServerSocket;
import java.net.Socket;

import ircmud.Connection;

public class Ircmud {
	
	public static String globalServerName = new String("IrcMud");
	public static int    globalServerPort = 6668;

	public static void main(String[] args) {

		System.out.println("Starting..");

		switch(args.length) {
			case 0: break;
			case 1: 
				globalServerName = args[0];
				break;
			case 2: 
				globalServerPort = Integer.parseInt(args[1]);
				break;
			default:
				System.out.println("Usage: Ircmud [servername [port]]");
		}

		try {

			System.out.println("Initializing ServerSocket");
			ServerSocket serverSocket = new ServerSocket(globalServerPort);

			Server server = new Server(serverSocket);

			server.serverLoop();

		} catch(Exception e) {
			
			System.err.println("ERROR: Exception " + e.getMessage());
			e.printStackTrace();
			
		}
		
		
	}

}
