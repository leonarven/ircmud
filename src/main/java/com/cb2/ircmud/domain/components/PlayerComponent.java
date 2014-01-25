package com.cb2.ircmud.domain.components;

import javax.persistence.ManyToOne;
import javax.persistence.Transient;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;

import com.cb2.ircmud.communication.CommunicationService;
import com.cb2.ircmud.domain.Player;
import com.cb2.ircmud.domain.World;
import com.cb2.ircmud.domain.services.PlayerService;
import com.cb2.ircmud.event.Event;
import com.cb2.ircmud.event.SayEvent;

@RooJavaBean
@RooToString
@RooJpaActiveRecord
public class PlayerComponent extends Component {
	
	@ManyToOne
	private Player player;
	
	@Transient
	@Autowired
	private CommunicationService communicationService;
	@Transient
	@Autowired
	private PlayerService playerService;
	
	@ManyToOne
	private World world;
	
	@Override
	public void handleEvent(Event event) {
		switch (event.getType()) {
		case Say:
			handleEvent((SayEvent)event); break;
			default:
				return;
		}
	}
	
	
	public void handleEvent(SayEvent event) {
		
	}


	@Override
	public Component cloneComponent() {
		PlayerComponent clone = new PlayerComponent();
		clone.player = this.player;
		clone.world = this.world;
		return clone;
	}
    
}
