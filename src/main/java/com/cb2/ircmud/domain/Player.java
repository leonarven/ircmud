package com.cb2.ircmud.domain;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import javax.persistence.CascadeType;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

import org.hibernate.Hibernate;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;
import org.springframework.transaction.annotation.Transactional;

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
	
	private class TransientData {
		List<PlayerState> state = new Vector<PlayerState>();;
		IrcUser ircUser = null;
	}
	
	@Transient
	private static Map<Long, TransientData> transientData = new HashMap<Long, TransientData>();
	
	private int access;
	
    /**
     */
    private String username;

    /**
     */
    private String passwordHash;
    
    
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Item> characters = new HashSet<Item>();
    
    public Item findCharacterByName(String name) {
    	for (Item i : characters) {
    		if (i.getName().getName().equalsIgnoreCase(name)) {
    			return i;
    		}
    	}
    	return null;
    }
    
    @Transient
    public void removeState(PlayerState state) {
		getTransientData().state.remove(state);
    }
    
    @Transient
    private TransientData getTransientData() {
    	Long id = Long.valueOf(getId());
    	TransientData data = transientData.get(id);
    	if (data != null) {
    		return data;
    	}
    	else {
    		data = new TransientData();
    		transientData.put(id, data);
    		return data;
    	}
    }
    
    @Transient
    public void addState(PlayerState state) {
    	getTransientData().state.add(state);
    }
    
    @Transient
    public List<PlayerState> getState() {
    	return getTransientData().state;
    }
    
    public void addCharacter(Item character) {
    	characters.add(character);
    }
    
    public void setIrcUser(IrcUser ircUser) {
    	getTransientData().ircUser = ircUser;
    }
    
    @Transient
    public IrcUser getIrcUser() {
    	return getTransientData().ircUser;
    }
    
    @Transient
    public static void removePlayerTransientData(long playerId) {
    	transientData.remove(playerId);
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
