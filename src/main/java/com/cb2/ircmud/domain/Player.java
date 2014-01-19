package com.cb2.ircmud.domain;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import javax.persistence.OneToMany;
import javax.persistence.Transient;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;

import com.cb2.ircmud.PlayerGameState;
import com.cb2.ircmud.PlayerState;
import com.cb2.ircmud.domain.components.CharacterComponent;
import com.cb2.ircmud.ircserver.IrcUser;

@RooJavaBean
@RooToString
@RooJpaActiveRecord(finders = { "findPlayersByUsernameEquals" })
public class Player {

	public final static int ACCESS_NORMAL = 0;		//00
	public final static int ACCESS_GAMEMASTER = 1;	//01
	public final static int ACCESS_ADMIN_ONLY = 2;  //10
	public final static int ACCESS_ADMIN = 3;		//11
	
	
	
	private int access;
	
    /**
     */
    private String username;

    /**
     */
    private String passwordHash;
    
    
    @OneToMany
    private Set<CharacterComponent> characters = new HashSet<CharacterComponent>();

    @Transient
    private IrcUser ircUser;
    
    @Transient
    private List<PlayerState> state = new Vector<PlayerState>();
    
    public Item findCharacterByName(String name) {
    	for (CharacterComponent c : characters) {
			Item item = c.getItem();
    		if (item.getName().equalsIgnoreCase(name)) {
    			return item;
    		}
    	}
    	return null;
    }
    
    public void removeState(PlayerState state) {
    	this.state.remove(state);
    }
    
    public PlayerGameState getGameState() {
    	for (Iterator<PlayerState> i = state.iterator(); i.hasNext();) {
    		PlayerState s = i.next();
    		if (s instanceof PlayerGameState) {
    			return (PlayerGameState)s;
    		}
    	}
    	return null;
    }
    
    public boolean hasStateWithGroup(int group) {
    	return getStateByGroup(group) != null;
    }
    
    public PlayerState getStateByGroup(int group) {
    	for (Iterator<PlayerState> i = state.iterator(); i.hasNext();) {
    		PlayerState s = i.next();
    		if (s.getStateGroup() == group) {
    			return s;
    		}
    	}
    	return null;
    }
    
    public void addState(PlayerState state) {
    	this.state.add(state);
    }
    
    public boolean isPlaying() {
    	return getGameState() != null;
    }
    
    
    public boolean hasGamemasterAccess() {
    	return (access & ACCESS_GAMEMASTER) == ACCESS_GAMEMASTER;
    }
    
    public boolean hasAdminAccess() {
    	return (access & ACCESS_ADMIN) == ACCESS_ADMIN;
    }
    
    public void giveAccess(int access) {
    	this.access |= access;
    }
    
    public void removeAccess(int access) {
    	this.access &= ~access;
    }
}
