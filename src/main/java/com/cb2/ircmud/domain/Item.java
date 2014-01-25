package com.cb2.ircmud.domain;

import com.cb2.ircmud.domain.components.Component;
import com.cb2.ircmud.domain.containers.Container;
import com.cb2.ircmud.event.Event;
import com.cb2.ircmud.event.EventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.ElementCollection;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJpaActiveRecord
public class Item implements EventListener {

    private String name;

    private String description;


    @OneToMany(cascade = CascadeType.ALL, mappedBy="item")
    private Set<Component> components = new HashSet<Component>();

    @ManyToOne
    private Container location;

    private String hiddenName;

	@Override
	public void handleEvent(Event event) {
		for (Component c : components) {
			c.handleEvent(event);
		}
	}
	
	
	
	public void setComponents(Set<Component> components) {
		for (Component c : components) {
			c.setItem(this);
		}
		this.components = components;
	}
	
	public void addComponent(Component component) {
		component.setItem(this);
		this.components.add(component);
	}
}
