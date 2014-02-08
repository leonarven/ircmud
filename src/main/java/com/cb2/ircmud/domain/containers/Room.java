package com.cb2.ircmud.domain.containers;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJpaActiveRecord
public class Room extends Container {
    private String name;

    @Override
	public boolean isSessionOpen() {
		return this.entityManager.contains(this);
	}

	@Override
	public Container refleshSession() {
		if (isSessionOpen()) return this;
		return Room.findRoom(this.getId());
	}
}
