package com.cb2.ircmud.domain;
import java.util.List;

import javax.persistence.Transient;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;

import com.cb2.ircmud.PlayerState;
import com.cb2.ircmud.ircserver.IrcUser;

@RooJavaBean
@RooToString
@RooJpaActiveRecord(finders = { "findPlayersByUsernameEquals" })
public class Player {

	public final static int ACCESS_NORMAL = 0;
	public final static int ACCESS_GAMEMASTER = 10;
	public final static int ACCESS_ADMIN = 100;
	
	
	
	private int access;
	
    /**
     */
    private String username;

    /**
     */
    private String passwordHash;

    @Transient
    private IrcUser ircUser;
    
    @Transient
    private List<PlayerState> state;
    
    @Transient
    boolean hasGamemasterAccess() {
    	return access >= ACCESS_GAMEMASTER;
    }
    
    @Transient
    boolean hasAdminAccess() {
    	return access >= ACCESS_ADMIN;
    }
}
