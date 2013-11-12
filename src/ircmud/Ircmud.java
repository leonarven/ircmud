package ircmud;

import java.net.ServerSocket;
import java.net.Socket;
import java.io.IOException;

import ircmud.Connection;

public class Ircmud {
	
	public static String globalServerName = new String("IrcMud");
	public static int    globalServerPort = 6667;

	public static void main(String[] args) {

		System.out.println("Starting..");

		switch(args.length) {
			case 0: break;
			case 2: 
				globalServerPort = Integer.parseInt(args[1]);
			case 1: 
				globalServerName = args[0];
				break;
			default:
				System.out.println("Usage: Ircmud [servername [port]]");
		}

		Server server;

		try {

			server = new Server(globalServerName, globalServerPort);

			server.run();

			server.close();
			
		} catch(IOException e) {
			
			System.err.println("ERROR: IOException " + e.getMessage());
			e.printStackTrace();
			
		} finally {
			
		}
		
		
	}

}
