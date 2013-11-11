package ircmud;

import java.net.ServerSocket;
import java.net.Socket;

import ircmud.Connection;

public class Ircmud {
	
	public static String globalServerName = new String("IrcMud");
	public static int    globalServerPort = 6667;

	public static void main(String[] args) {

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
			ServerSocket serverSocket = new ServerSocket(globalServerPort);

			while (true) {
				Socket         socket = serverSocket.accept();
				Connection connection = new Connection(socket);
				Thread         thread = new Thread(connection);
				thread.start();
			}

		} catch(IOException e) {
			
		}
		
		
	}

}
