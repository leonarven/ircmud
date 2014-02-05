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
import javax.persistence.Embedded;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;
import org.springframework.transaction.annotation.Transactional;

@RooJavaBean
@RooToString
@RooJpaActiveRecord
public class Item implements EventListener {
	@Embedded
    private Name name;

    private String description;

    @OneToMany(cascade = CascadeType.ALL, mappedBy="item")
    private Set<Component> components = new HashSet<Component>();

    @ManyToOne
    private Container location;

    private String hiddenName;

    public boolean isSessionOpen() {
    	return entityManager.contains(this);
    }
    
    @Transactional
    static Item refleshSession(Item item) {
    	if (item.isSessionOpen()) return item;
    	return Item.findItem(item.getId());
    }
    
	@Override
	@Transactional
	public void handleEvent(Event event) {
		for (Component c : components) {
			c.handleEvent(event);
		}
		
	}
	
	@Transactional(readOnly = true)
	public boolean itemHasComponentInstanceOf(Class<?> cl) {
		for (Component c : this.getComponents()) {
			if (cl.isInstance(c)) {
				return true;
			}
		}
		return false;
	}
	
	@Transactional(readOnly = true)
	public <T extends Component>T findFirstComponentInstanceOf(Class<T> cl) {
		for (Component c : this.getComponents()) {
			if (cl.isInstance(c)) {
				return cl.cast(c);
			}
		}
		return null;
	}
	
	@Transactional(readOnly = true)
	public <T extends Component>ArrayList<T> findComponentsInstanceOf(Class<T> cl) {
		ArrayList<T> result = new ArrayList<T>();
		for (Component c : this.getComponents()) {
			if (cl.isInstance(c)) {
				result.add(cl.cast(c));
			}
		}
		return result;
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
