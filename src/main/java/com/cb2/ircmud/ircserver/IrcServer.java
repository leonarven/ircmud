package com.cb2.ircmud.ircserver;

import java.net.InetSocketAddress;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.nio.charset.Charset;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.io.IOException;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import com.cb2.ircmud.domain.Player;
import com.cb2.ircmud.ircserver.services.AuthService;
import com.cb2.ircmud.ircserver.services.ChannelService;
import com.github.rlespinasse.slf4j.spring.AutowiredLogger;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class IrcServer implements Runnable {

	private AsynchronousServerSocketChannel serverSocket;
	@Value("${config.server.name}")
	public String serverName;
	@Value("${config.server.port}")
	public int    serverPort;

	private ReadWriteLock connectionListLock = new ReentrantReadWriteLock(true);
	private List<Connection> connectionList = new LinkedList<Connection>();
	private Charset charset = Charset.forName("UTF-8");
	private volatile boolean running = false;
	
	@AutowiredLogger
	Logger logger;
	@Autowired
	AuthService authService;
	@Autowired
	ChannelService channels;
	@Autowired
	Environment env;
	
	@PostConstruct
	public void init() throws IOException {
		
		logger.info("Initializing IrcServer({}: {})",serverName, serverPort);

		logger.info("Initializing ServerSocket");
		serverSocket = AsynchronousServerSocketChannel.open().bind(new InetSocketAddress(serverPort));

		// Init channel Config.WorldChannel
		Channel worldChannel = new Channel( env.getProperty("config.server.WorldChannel"));
		channels.add(worldChannel);
	}
	
	@Override
	public void run() {
		//TODO: No hard-coded admin accounts :P
  		authService.addAccount("admin", "password", Player.ACCESS_ADMIN);

  		logger.info("Starting server loop");
  		
  		running = true;
  		Future<AsynchronousSocketChannel> newConFuture = null;
  		while (running) {
  			if (newConFuture == null) {
  				newConFuture = serverSocket.accept();
  			}
  			try {
				AsynchronousSocketChannel socketChannel = newConFuture.get(100, TimeUnit.MILLISECONDS);
				newConFuture = null;
				handleNewConnection(socketChannel);
			} catch (InterruptedException e) {
				e.printStackTrace();
				break;
			} catch (ExecutionException e) {
				e.printStackTrace();
			} catch (TimeoutException e) {
			}
  		}
  		logger.debug("Server is closing down");
  		try {
			serverSocket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void close() {
		running = false;
	}
	
	private void handleNewConnection(AsynchronousSocketChannel socket) {
		Connection con = new Connection(socket);
		connectionListLock.writeLock().lock();
		connectionList.add(con);
		connectionListLock.writeLock().unlock();
		
		con.initialize();
	}
	
	public void forEachConnection(ConnectionHandler conHandler) {
		connectionListLock.readLock().lock();
		for (Connection con : connectionList) {
			conHandler.handleConnection(con);
		}
		connectionListLock.readLock().unlock();
	}
	
	public void removeConnection(Connection con) {
		connectionListLock.writeLock().lock();
		connectionList.remove(con);
		connectionListLock.writeLock().unlock();
	}
	
	@PreDestroy
	protected void destroy(){
		close();
	}
	
	public Charset getCharset() {
		return charset;
	}
}