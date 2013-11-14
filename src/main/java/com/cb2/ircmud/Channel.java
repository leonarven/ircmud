package com.cb2.ircmud;

import java.util.ArrayList;
import com.cb2.ircmud.Connection;
import com.cb2.ircmud.DefaultChannelMessageProxy;

public class Channel {

	private ArrayList<Connection> channelMembers = new ArrayList<Connection>();
	private String topic;
	private IChannelMessageProxy messageProxy = new DefaultChannelMessageProxy();
	protected String name;
	
	public Channel(String name) {
		this.name = name;
	}
	
	public String getName() { return name; }
	
	public ArrayList<Connection> getChannelMembers() { return channelMembers; }
	
	public void sendMessage(Connection sender, String msg) {
		messageProxy.privateMessageToChannel(sender, this, msg);
	}
	
	public void sendRawStringAllExcept(Connection except, String msg) {
		synchronized (channelMembers) {
			for (Connection con : channelMembers) {
				if (con != except) con.sendRawString(msg);
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
				con.sendRawString(":"+IrcServer.globalServerName+" 332 "+con.nick+" "+this.name+" :"+this.topic);
			else con.sendRawString(":"+IrcServer.globalServerName+" 332 "+con.nick+" "+this.name+" :");
			
			con.sendRawString(":"+IrcServer.globalServerName+" 333 "+con.nick+" "+this.name+" admin!admin@IrcMud 0");
	
			String userlist = "Foobarasd " + con.nick;
			for(Connection _con : channelMembers) userlist = userlist + " " +  _con.nick;
			con.sendRawString(":"+IrcServer.globalServerName+" 353 "+con.nick+" @ "+ this.name + " :" + userlist.trim());
			
			con.sendRawString(":"+IrcServer.globalServerName+" 366 "+con.nick+" " + this.name + " :End of /NAMES list.");
			
			channelMembers.add(con);
		}
	}
	
	public void sendWhoToCon(Connection con) {
		// RPL_WHOREPLY 352
		
		for(Connection _con : channelMembers)
			// TODO H/G => here/gone
			// TODO: H/G:n perään tulee henkilön statuksen indikoiva merkki (+/@)
			con.sendRawString(":"+IrcServer.globalServerName+" 352 "+con.nick+" "+this.name+" "+_con.username+" "+_con.hostname+" "+IrcServer.globalServerName+" "+_con.nick+" H :0 " + _con.realname);

		con.sendRawString(":"+IrcServer.globalServerName+" 315 "+con.nick+" " + this.name + " :End of /WHO list.");
	}
	
	public void removeConnection(Connection con) {
		synchronized (channelMembers) {
			channelMembers.remove(con);
		}
	}
	
	
	public static boolean isValidPrefix(char prefix) {
		// TODO make to better
		switch(prefix) {
			case '#':
			case '&':
			case '!':
				return true;
			default:
				return false;
		}
	}
}
