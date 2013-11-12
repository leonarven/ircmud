package ircmud;

import java.util.ArrayList;

public class Channel {

    private ArrayList<Connection> channelMembers = new ArrayList<Connection>();
    private String topic;
    protected String name;
    
    public void sendNot(Connection not, String toSend) {
        for (Connection con : channelMembers) {
            if (con != not)
                con.send(toSend);
        }
    }
    
    public void sendAll(String toSend) {
        sendNot(null, toSend);
    }
    
    public void send(String toSend) {
        sendAll(toSend);
    }
    
    public void memberQuit(String nick)
    {
        
    }
	
}
