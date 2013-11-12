package com.cb2.ircmud;

import java.util.ArrayList;

public class Channel {

	private ArrayList<Connection> channelMembers = new ArrayList<Connection>();
	private String topic;
	protected String name;
	
	public Channel(String name) {
		this.name = name;
	}
	
	public void sendAllExcept(Connection except, String msg) {
		for (Connection con : channelMembers) {
			if (con != except) con.send(msg);
		}
	}
	
	public void sendAll(String toSend) {
		sendAllExcept(null, toSend);
	}
	
	public void send(String toSend) {
		sendAll(toSend);
	}
	
	public void addConnection(Connection con) {
		channelMembers.add(con);
		send(":" + con.getRepresentation() + " JOIN "+ this.name);
		con.send(":"+Server.globalServerName+" 332 "+con.nick+" "+this.name+" :"+this.topic);

		String userlist = "";
		for(Connection _con : channelMembers) userlist = userlist + " " + _con.nick; 
		con.send(":"+Server.globalServerName+" 353 "+con.nick+" @ "+this.name+" :"+userlist);

		con.send(":"+Server.globalServerName+" 366 "+con.nick+" "+this.name+" :End of /NAMES list.");
	}
	
	public void memberQuit(String nick) {
		
	}
	
}
