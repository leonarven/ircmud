package ircmud;

import java.io.IOException;

public class Ircmud {
	
	public static String globalServerName = Config.ServerName;
	public static int    globalServerPort = Config.ServerPort;

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

		try {

			Server.init(globalServerName, globalServerPort);

			Server.run();

			Server.close();
			
		} catch(IOException e) {
			
			System.err.println("ERROR: IOException " + e.getMessage());
			e.printStackTrace();
			
		} finally {
			
		}
		
		
	}

}
