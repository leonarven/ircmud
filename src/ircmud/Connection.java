package ircmud;

import java.io.BufferedReader;
import java.io.OutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.concurrent.LinkedBlockingQueue;

public class Connection  implements Runnable {

    private Socket socket = null;
    private String hostname;
    private InetSocketAddress address;

	public Connection(Socket socket){
    	this.socket = socket;
    }

    private LinkedBlockingQueue<String> outQueue = new LinkedBlockingQueue<String>(1000);

	private Thread outThread = new Thread() {
		public void run() {
			try {
				OutputStream out = socket.getOutputStream();
				while (true) {
					String s = outQueue.take();
					s = s.replace("\n", "").replace("\r", "");
					s = s + "\r\n";
					out.write(s.getBytes());
					out.flush();
				}
			} catch (Exception e) {
				System.out.println("Outqueue died");
				outQueue.clear();
				outQueue = null;
				e.printStackTrace();
				try {
					socket.close();
				} catch (Exception e2) {
					e2.printStackTrace();
				}
			}
		}
	};
	
	
    @Override
    public void run() {

    	try {
			address = (InetSocketAddress) socket.getRemoteSocketAddress();
			hostname = address.getAddress().getHostAddress();
			System.out.println("Connection from host " + hostname);
			outThread.start();
			InputStream socketIn = socket.getInputStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader(socketIn));
			String line;
			while ((line = reader.readLine()) != null) {
				System.out.println("DEBUG: con: "+line);
			}    	
    	} catch(IOException e) {
    		
    	}
    	
	}

    	
}
