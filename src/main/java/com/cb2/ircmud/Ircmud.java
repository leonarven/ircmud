package com.cb2.ircmud;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.AbstractPlatformTransactionManager;

import com.cb2.ircmud.domain.Room;

@Component
public class Ircmud {
	
	public static String globalServerName = new String("IrcMud");
	public static int    globalServerPort = 6667;

	public void main(String[] args) {
		Room room = new Room();		//using plain roo generated entity
		room.setName("olohuone");
		room.persist();	
		room = new Room();
		room.setName("niitty");
		room.persist();	 						//save to database
		
		List<Room> rooms = Room.findAllRooms(); //fetch from database
		
		System.out.println(rooms);
		
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
