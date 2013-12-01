package com.cb2.ircmud.ircserver;

import java.util.HashMap;
import java.util.Map;
import java.util.Iterator;
import java.util.Date;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import com.cb2.ircmud.ircserver.services.UserService;
import com.github.rlespinasse.slf4j.spring.AutowiredLogger;

@Component
public class PingService implements Runnable {

	public final static String VERSION = "0.06";
	@Value("${config.server.pingTimeout}")
	private long pingTimeout  = 10000;
	private int pingCheckTime = 1000;
	@Value("${config.server.pingTimeout}")
	private long newPingTime  = 10000;
	private HashMap<Connection, Long> lastPongMap = new HashMap<Connection, Long>();
	private HashMap<Connection, Long> lastPingMap = new HashMap<Connection, Long>();
	
	@Autowired 
	UserService users;
	@AutowiredLogger
	Logger logger;
	@Autowired
	Environment env;
	

	public void addPartner(Connection connection) {
		logger.debug("addPartner({})",connection.getRepresentation());
		lastPongMap.put(connection, new Date().getTime());
		lastPingMap.put(connection, new Date().getTime());
	}

	public void dropPartner(Connection connection) {
		logger.debug("DEBUG: PingService:dropPartner({})",connection.getRepresentation());
		lastPongMap.remove(connection);
		lastPingMap.remove(connection);
	}
	
	public void pongFrom(Connection connection) {
		logger.debug("DEBUG: PingService:pongFrom({}) : {}",connection.getRepresentation(),lastPongMap.containsKey(connection)?(""+lastPongMap.get(connection)):"no");
		if (lastPongMap.containsKey(connection)) return;

		lastPongMap.put(connection, new Date().getTime());
	}
	
	private long pingTimeDiff(Connection connection, long time) {
		if (!lastPongMap.containsKey(connection)) return -1;
		
		return time - lastPongMap.get(connection).longValue();
	}
	
	private long pingTimeDiff(Connection connection) {
		return pingTimeDiff(connection, new Date().getTime());
	}
	
	@PostConstruct
	public void init() {
		logger.info("Initializing PingService");
		
		Thread pingService = new Thread(new PingService());
		
		pingService.start();
		
	}
	
	@Override
	public void run() {
		//TODO heaps would make this clearer and propably more robust
		
		long currentTime, diff;
		Connection user;
		while(true) {
	        currentTime = new Date().getTime();

	        Iterator<Map.Entry<Connection, Long>> pongit = lastPongMap.entrySet().iterator();
	        while (pongit.hasNext()) {
	            Map.Entry<Connection, Long> entry = (Map.Entry<Connection, Long>)pongit.next();
		        diff = currentTime - entry.getValue().longValue();
		        
		        user = entry.getKey();
		        if (diff > pingTimeout) {
	        		user.quit("Ping timeout ("+diff+"ms)");
	        		users.dropConnection(user);
		        }
		        pongit.remove();
	        }	        
	        
	        Iterator<Map.Entry<Connection, Long>> pingit = lastPingMap.entrySet().iterator();
	        while (pingit.hasNext()) {
	            Map.Entry<Connection, Long> entry = (Map.Entry<Connection, Long>)pingit.next();
		        
	        	user = entry.getKey();
		        diff = currentTime - entry.getValue().longValue();
	        	
	        	if (diff > newPingTime) {
	        		user.sendPing(env.getProperty("config.server.name"));
	        		lastPingMap.put(user, new Date().getTime());
	        	}
	            pingit.remove();
	        }
		    
		    try {
				Thread.sleep(pingCheckTime);
			} catch(Exception e) {
				
			}
		}
	}
	
}
