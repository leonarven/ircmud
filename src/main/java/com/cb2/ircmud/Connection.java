package com.cb2.ircmud;

import java.net.Socket;

public class Connection  implements Runnable {

    private Socket socket = null;

	public Connection(Socket socket){
    	this.socket = socket;
    }
	
    @Override
    public void run() {
    	
	}

    	
}
