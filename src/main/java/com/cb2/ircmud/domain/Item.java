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
	
	public boolean hasComponentInstanceOf(Class<?> cl) {
		for (Component c : components) {
			if (cl.isInstance(c)) {
				return true;
			}
		}
		return false;
	}
	
	public Component findFirstComponentInstanceOf(Class<?> cl) {
		for (Component c : components) {
			if (cl.isInstance(c)) {
				return c;
			}
		}
		return null;
	}
	
	public ArrayList<Component> findComponentsInstanceOf(Class<?> cl) {
		ArrayList<Component> result = new ArrayList<Component>();
		for (Component c : components) {
			if (cl.isInstance(c)) {
				result.add(c);
			}
		}
		return result;
	}
}
