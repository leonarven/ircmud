package com.cb2.ircmud;

import java.io.IOException;

import org.springframework.stereotype.Component;

import com.cb2.ircmud.ircserver.IrcServer;

@Component
public class Ircmud {
	
	public static String globalServerName = Config.ServerName;
	public static int    globalServerPort = Config.ServerPort;

	public void main(String[] args) {
        Console.debug("Ircmud::main()");

		
		switch(args.length) {
			case 0: break;
			case 1: 
				globalServerName = args[0];
				break;
			case 2:
				globalServerName = args[0];
				globalServerPort = Integer.parseInt(args[1]);
				break;
			default:
				System.out.println("Usage: Ircmud [servername [port]]");
		}

		try {
			Console.out("Creating Server");
			IrcServer.init(globalServerName, globalServerPort);

			IrcServer.run();

			IrcServer.close();
			
		} catch(IOException e) {
			
			System.err.println("ERROR: IOException at IrcMud.main:" + e.getMessage());
			e.printStackTrace();
			
		} finally {
			
		}
		
		
	}

}
