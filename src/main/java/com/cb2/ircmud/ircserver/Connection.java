package com.cb2.ircmud.ircserver;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.LinkedBlockingQueue;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.core.env.Environment;

import com.cb2.ircmud.ircserver.services.IrcCommandService;
import com.github.rlespinasse.slf4j.spring.AutowiredLogger;


@Configurable
public class Connection extends IrcUser {
	private InetSocketAddress address;
	private AsynchronousSocketChannel socketChannel = null;
	private ByteBuffer readBuffer = ByteBuffer.allocate(512);
	private volatile boolean writing = false;

	@Autowired 
	IrcServer server;
	@AutowiredLogger
	Logger logger;
	@Autowired 
	IrcCommandService ircCommandService;
	@Autowired
	Environment env;
	
	
	public Connection(AsynchronousSocketChannel socketChannel){
		this.socketChannel = socketChannel;
	}

	private LinkedBlockingQueue<String> outQueue = new LinkedBlockingQueue<String>(100);

	public void sendRawString(String s) {
		logger.debug("{} <<< {}", nickname, s);
		outQueue.add(s);
		asynchronousHandleOutQueue();
	}
	
	public void sendPrivateMessage(String sender, String target, String msg) {
		sendRawString(":" + sender + " PRIVMSG " + target + " :" + msg);
	}

	public void sendServerCommand(Object command, String string) {
		sendRawString(":" + server.serverName + " " + command.toString() + " " + nickname + " :" + string);
	}

	public void sendServerReply(Object command, String string) {
		sendRawString(":" +server.serverName + " " + command.toString() + " " + nickname + " " + string);
	}

	public void sendCommand(Object command, String string) {
		sendRawString(":" + getRepresentation() + " " + command.toString() + " :" + string);
	}

	public void sendSelfNotice(String string) {
		sendServerCommand("NOTICE", string);
	}
	
	public boolean isConnected() {
		return this.username != null && this.nickname != null;
	}
	
	public void closeConnection() {
		//TODO: Cleaner closing
		synchronized (socketChannel) {
			if (socketChannel.isOpen()) {
				try {
					socketChannel.close();
				}
				catch (IOException e) {
					logger.error("{}: IOException while closing socket channel {}", getRepresentation(), e.getMessage());
				}
			}
			users.dropUser(this.nickname);
		}
	}
	
	public void sendPing(String ping) {
		sendRawString("PING :"+ping);
	}
	
	public void initialize() {
		try {
			initAsynchronousRead();
			this.address = (InetSocketAddress) socketChannel.getRemoteAddress();
			this.hostname = address.getAddress().getHostAddress();
			logger.info("Connection from host {}", hostname);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		send020Reply();
	}
	
	public void send020Reply() {
		sendReply(IrcReply.serverReply("020", "Please wait while we process your connection"));	
	}
	
	public void initAsynchronousRead() {
		socketChannel.read(readBuffer, null, new CompletionHandler<Integer, Object>() {

			@Override
			public void completed(Integer result, Object attachment) {
				int readBytes = result.intValue();
				readBuffer.flip();
				String data = new String(readBuffer.array(), 0, readBytes, server.getCharset());
				String[] lines = data.split("\r\n");
				for (String line : lines) {
					if (!line.isEmpty())
						handleLine(line);
				}
				readBuffer.clear();
				
				socketChannel.read(readBuffer, null, this);
			}

			@Override
			public void failed(Throwable exc, Object attachment) {
				logger.error("{}: Socket channel read error {}", getNickname(), exc.getMessage());
			}
		});
	}
	
	private void handleLine(String line) {
		ircCommandService.addMessage(this, line);
	}
	
	public void asynchronousHandleOutQueue() {
		String data;
		if (!writing && (data = outQueue.poll()) != null) {
			writing = true;
			socketChannel.write(ByteBuffer.wrap((data + "\r\n").getBytes(server.getCharset())), null, new CompletionHandler<Integer, Object>() {

				@Override
				public void completed(Integer result, Object attachment) {
					String data;
					if ((data = outQueue.poll()) != null) {
						socketChannel.write(ByteBuffer.wrap((data + "\r\n").getBytes(server.getCharset())), null, this);
					} else {
						writing = false;
					}
				}

				@Override
				public void failed(Throwable exc, Object attachment) {
					logger.error("{}: Socket channel write error {}", getNickname(), exc.getMessage());
				}
			});
		}
	}
	
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	public void setRealname(String realName) {
		this.realname = realName;
	}
	
	public void setMode(String mode) {
		this.mode = mode;
	}

	@Override
	public void sendReply(IrcReply reply) {
		this.sendRawString(reply.toString());
	}

	@Override
	public boolean isConnection() {
		return true;
	}
		
}
