package com.cb2.ircmud.domain.components;

import javax.persistence.ManyToOne;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;

import com.cb2.ircmud.domain.Item;
import com.cb2.ircmud.event.Event;

@RooJavaBean
@RooToString
@RooJpaActiveRecord(inheritanceType = "TABLE_PER_CLASS")
public class Component {

	@ManyToOne
    private Item item;
	

	public void handleEvent(Event event) {}
}
