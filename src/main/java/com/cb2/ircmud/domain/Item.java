package com.cb2.ircmud.domain;

import com.cb2.ircmud.domain.components.Component;
import com.cb2.ircmud.domain.containers.Container;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJpaActiveRecord
public class Item {

    private String name;

    private String description;


    @OneToMany(cascade = CascadeType.ALL, mappedBy="item")
    private Set<Component> components = new HashSet<Component>();
    
    private Map<String, Component> componentsById = new HashMap<String, Component>();

    @ManyToOne
    private Container location;

    private String hiddenName;
}
