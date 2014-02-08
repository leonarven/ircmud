package com.cb2.ircmud.domain.containers;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;
import org.springframework.transaction.annotation.Transactional;

import com.cb2.ircmud.domain.Item;
import com.cb2.ircmud.domain.World;
import com.cb2.ircmud.event.Event;
import com.cb2.ircmud.event.EventListener;
import com.cb2.ircmud.event.VisionEvent;

@RooJavaBean
@RooToString
@RooJpaActiveRecord(inheritanceType = "SINGLE_TABLE")
public abstract class Container implements EventListener {

	@ManyToOne
	World world;
	
    @OneToMany(cascade = CascadeType.ALL, mappedBy="location")
    private List<Item> items = new ArrayList<Item>();
    
    private String description;
    
    public abstract boolean isSessionOpen();
    
    
    public abstract Container refleshSession();
    
    @Transactional
    public void handleEvent(Event event) {
    	switch (event.getType()) {
		case Look:
			for (Item i : items) {
				//Can't see itself
				if (i.getId() != ((Item)event.getSender()).getId()) {
					event.addChildEvent(new VisionEvent(i));
				}
			}
			
			break;
		default:
    	}
    	
    	for (Item i : items) {
			if (!i.isSessionOpen()) {
				i = Item.findItem(i.getId());
			}
    		i.handleEvent(event);
    	}
    	
    }
    
    public void addItem(Item item) {
    	items.add(item);
    	item.setLocation(this);
    }
    public boolean removeItem(Item item) {
    	return items.remove(item);
    }
    
}
