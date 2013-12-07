package com.cb2.ircmud.domain.components;

import com.cb2.ircmud.domain.containers.Room;
import com.cb2.ircmud.event.Event;

import javax.persistence.ManyToOne;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJpaActiveRecord(finders = { "findDoorsByTargetRoom" })
public class Door extends Component {

    @ManyToOne
    private Room targetRoom;

    private Boolean isOpen;

	@Override
	public void handleEvent(Event event) {
		// TODO Auto-generated method stub
		
	}
}
