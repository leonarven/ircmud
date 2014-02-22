package com.cb2.ircmud.domain.components;

import com.cb2.ircmud.domain.components.Component;
import com.cb2.ircmud.event.Event;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJpaActiveRecord
public class Damageable extends Component {
	int health;
	@Override
	public void handleEvent(Event event) {
		//TODO: Handle DamagedEvent
	}

	@Override
	public Component cloneComponent() {
		Damageable clone = new Damageable();
		return clone;
	}
}
