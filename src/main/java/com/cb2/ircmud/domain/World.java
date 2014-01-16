package com.cb2.ircmud.domain;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;

import com.cb2.ircmud.domain.containers.Container;
import com.cb2.ircmud.domain.containers.Room;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.OneToMany;

@RooJavaBean
@RooToString
@RooJpaActiveRecord(finders = { "findWorldsByNameEquals", "findWorldsByNameLike" })
public class World {

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "world", targetEntity = Container.class)
    private Set<Room> rooms = new HashSet<Room>();
    
    @OneToMany
    private Map<String, Item> characters = new HashMap<String, Item>();

    private String name;


    public Item findCharacterByName(String name) {
    	return characters.get(name.toLowerCase());
    }
}
