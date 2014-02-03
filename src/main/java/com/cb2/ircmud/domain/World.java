package com.cb2.ircmud.domain;
import org.hibernate.Hibernate;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;
import org.springframework.transaction.annotation.Transactional;

import com.cb2.ircmud.domain.containers.Container;
import com.cb2.ircmud.domain.containers.Room;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.EntityManager;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PersistenceContext;

@RooJavaBean
@RooToString
@RooJpaActiveRecord(finders = { "findWorldsByNameEquals", "findWorldsByNameLike" })
public class World {
	
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "world", targetEntity = Container.class)
    private Set<Room> rooms = new HashSet<Room>();
    
    @OneToMany
    private Map<String, Item> characters = new HashMap<String, Item>();

    private String name;

    @ManyToOne
    private Room defaultRoom;
    
    public void addRoom(Room room) {
    	rooms.add(room);
    }
}
