package com.cb2.ircmud;

public class Player {
	
	public String username;
	public String nick;
	public String hostname;
	
	public Player(String nick, String username) {
		this.nick     = nick;
		this.username = username;
	}
}
