package com.cb2.ircmud;

import java.util.HashMap;
import java.util.Map;
import java.util.Iterator;
import java.util.Date;

public class PingService implements Runnable {

	private static long pingTimeout  = 10000;
	private static int pingCheckTime = 1000;
	private static long newPingTime  = 10000;
	private static HashMap<String, Long> lastPongMap = new HashMap<String, Long>();
	private static HashMap<String, Long> lastPingMap = new HashMap<String, Long>();

	public static void addPartner(String nickName) {
		System.out.println("DEBUG: PingService:addPartner("+nickName+")");
		lastPongMap.put(nickName, new Date().getTime());
		lastPingMap.put(nickName, new Date().getTime());
	}
	public static void addPartner(IrcUser user) {
		addPartner(user.getNickname().toLowerCase());
	}
	
	public static void pongFrom(String nickName) {
		System.out.println("DEBUG: PingService:pongFrom("+nickName+") :"+(lastPongMap.containsKey(nickName)?(""+lastPongMap.get(nickName)):"no"));
		if (lastPongMap.containsKey(nickName)) return;

		lastPongMap.put(nickName, new Date().getTime());
	}
	public static void pongFrom(IrcUser user) {
		pongFrom(user.getNickname().toLowerCase());
	}
	
	private static long pingTimeDiff(String nickName, long time) {
		if (!lastPongMap.containsKey(nickName)) return -1;
		
		return time - lastPongMap.get(nickName).longValue();
	}
	private static long pingTimeDiff(String nickName) {
		return pingTimeDiff(nickName, new Date().getTime());
	}
	
	public static void init(int pingCheckTime, long pingTimeout) {
		System.out.println("Initializing PingService");
		PingService.pingCheckTime = pingCheckTime;
		PingService.pingTimeout= pingTimeout;
		
		Thread pingService = new Thread(new PingService());
		
		pingService.start();
		
	}
	
	@Override
	public void run() {
		
		long currentTime, diff;
		IrcUser user;
		while(true) {
	        currentTime = new Date().getTime();

	        for (Map.Entry<String, Long> entry : PingService.lastPongMap.entrySet()) {
		        diff = currentTime - entry.getValue().longValue();
	        	user = IrcServer.findUserByNickname(entry.getKey());
		        
		        if (diff > PingService.pingTimeout) {
		        	if (user != null)
		        		user.quit("Ping timeout ("+diff+"ms)");
		        }
	        }		    
	        for (Map.Entry<String, Long> entry : PingService.lastPingMap.entrySet()) {
	        	user = IrcServer.findUserByNickname(entry.getKey());
		        diff = currentTime - entry.getValue().longValue();
	        	
	        	if (diff > PingService.newPingTime) {
	        		user.sendReply(new IrcReply("PING", IrcServer.globalServerName));
	        		lastPingMap.put(entry.getKey(), new Date().getTime());
	        	}
	        }
		    
		    try {
				Thread.sleep(PingService.pingCheckTime);
			} catch(Exception e) {
				
			}
		}
	}
	
}
