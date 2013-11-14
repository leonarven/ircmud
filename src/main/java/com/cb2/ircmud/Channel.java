package com.cb2.ircmud;

import java.util.ArrayList;
import com.cb2.ircmud.Connection;

public class Channel {

	private ArrayList<Connection> channelMembers = new ArrayList<Connection>();
	private String topic;
	protected String name;
	
	public Channel(String name) {
		this.name = name;
	}
	
	public void sendRawStringAllExcept(Connection except, String msg) {
		synchronized (channelMembers) {
			for (Connection con : channelMembers) {
				if (con != except) con.sendRawString(msg);
			}
		}
	}
	
	public void sendPrivateMessage(Connection sender, String msg) {
		synchronized (channelMembers) {
			for (Connection con : channelMembers) {
				if (con != sender) con.sendPrivateMessage(sender.getRepresentation(), this.name, msg);
			}
		}
	}
	
	public void sendRawStringAll(String toSend) {
		sendRawStringAllExcept(null, toSend);
	}
	
	public void addConnection(Connection con) {
		synchronized (channelMembers) {
			con.sendRawString(":" + con.getRepresentation() + " JOIN "+ this.name);
			sendRawStringAll(":" + con.getRepresentation() + " JOIN "+ this.name);
			
			if (this.topic != null)
				con.sendRawString(":"+Server.globalServerName+" 332 "+con.nick+" "+this.name+" :"+this.topic);
	
			String userlist = con.nick;
			for(Connection _con : channelMembers) userlist = userlist + " " +  _con.nick;
			con.sendRawString(":"+Server.globalServerName+" 353 "+con.nick+" @ "+ this.name + " :" + userlist.trim());
			
			con.sendRawString(":"+Server.globalServerName+" 366 "+con.nick+" " + this.name + " :End of /NAMES list.");
			
			channelMembers.add(con);
		}
	}
	
	public void removeConnection(Connection con) {
		synchronized (channelMembers) {
			channelMembers.remove(con);
		}
	}
	
}
