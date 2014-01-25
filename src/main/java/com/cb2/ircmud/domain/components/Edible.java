package com.cb2.ircmud.domain.components;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;

import com.cb2.ircmud.event.Event;

@RooJavaBean
@RooToString
@RooJpaActiveRecord
public class Edible extends Component {

	@Override
	public void handleEvent(Event event) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Component cloneComponent() {
		Edible clone = new Edible();
		return clone;
	}
}
