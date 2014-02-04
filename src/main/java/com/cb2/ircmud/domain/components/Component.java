package com.cb2.ircmud.domain.components;

import javax.persistence.ManyToOne;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;

import com.cb2.ircmud.domain.Item;
import com.cb2.ircmud.event.Event;
import com.cb2.ircmud.event.EventListener;

@RooJavaBean
@RooToString
@RooJpaActiveRecord(inheritanceType = "TABLE_PER_CLASS")
public abstract class Component implements EventListener {

	@ManyToOne
    protected Item item;

	@Override
	public void handleEvent(Event event) {
	}
	
	public abstract Component cloneComponent();
	
}
