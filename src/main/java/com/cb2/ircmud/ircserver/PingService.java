package com.cb2.ircmud.ircserver;

import java.util.HashMap;
import java.util.Map;
import java.util.Iterator;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;

public class PingService implements Runnable {

	public final static String VERSION = "0.06";
	private static long pingTimeout  = 10000;
	private static int pingCheckTime = 1000;
	private static long newPingTime  = 10000;
	private static HashMap<Connection, Long> lastPongMap = new HashMap<Connection, Long>();
	private static HashMap<Connection, Long> lastPingMap = new HashMap<Connection, Long>();
	
	
	@Autowired 
	IrcServer server;
	

	public static void addPartner(Connection connection) {
		System.out.println("DEBUG: PingService:addPartner("+connection.getRepresentation()+")");
		lastPongMap.put(connection, new Date().getTime());
		lastPingMap.put(connection, new Date().getTime());
	}

	public static void dropPartner(Connection connection) {
		System.out.println("DEBUG: PingService:dropPartner("+connection.getRepresentation()+")");
		lastPongMap.remove(connection);
		lastPingMap.remove(connection);
	}
	
	public static void pongFrom(Connection connection) {
		System.out.println("DEBUG: PingService:pongFrom("+connection.getRepresentation()+") :"+(lastPongMap.containsKey(connection)?(""+lastPongMap.get(connection)):"no"));
		if (lastPongMap.containsKey(connection)) return;

		lastPongMap.put(connection, new Date().getTime());
	}
	
	private static long pingTimeDiff(Connection connection, long time) {
		if (!lastPongMap.containsKey(connection)) return -1;
		
		return time - lastPongMap.get(connection).longValue();
	}
	
	private static long pingTimeDiff(Connection connection) {
		return pingTimeDiff(connection, new Date().getTime());
	}
	
	public static void init(int pingCheckTime, long pingTimeout) {
		PingService.pingCheckTime = pingCheckTime;
		PingService.pingTimeout= pingTimeout;
		
		Thread pingService = new Thread(new PingService());
		
		pingService.start();
		
	}
	
	@Override
	public void run() {
		
		long currentTime, diff;
		Connection user;
		while(true) {
	        currentTime = new Date().getTime();

	        Iterator<Map.Entry<Connection, Long>> pongit = PingService.lastPongMap.entrySet().iterator();
	        while (pongit.hasNext()) {
	            Map.Entry<Connection, Long> entry = (Map.Entry<Connection, Long>)pongit.next();
		        diff = currentTime - entry.getValue().longValue();
		        
		        user = entry.getKey();
		        if (diff > PingService.pingTimeout) {
	        		user.quit("Ping timeout ("+diff+"ms)");
	        		server.dropConnection(user);
		        }
		        pongit.remove();
	        }	        
	        
	        Iterator<Map.Entry<Connection, Long>> pingit = PingService.lastPingMap.entrySet().iterator();
	        while (pingit.hasNext()) {
	            Map.Entry<Connection, Long> entry = (Map.Entry<Connection, Long>)pingit.next();
		        
	        	user = entry.getKey();
		        diff = currentTime - entry.getValue().longValue();
	        	
	        	if (diff > PingService.newPingTime) {
	        		user.sendPing(server.globalServerName);
	        		lastPingMap.put(user, new Date().getTime());
	        	}
	            pingit.remove();
	        }
		    
		    try {
				Thread.sleep(PingService.pingCheckTime);
			} catch(Exception e) {
				
			}
		}
	}
	
}
