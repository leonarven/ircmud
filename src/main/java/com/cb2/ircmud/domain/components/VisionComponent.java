package com.cb2.ircmud.domain.components;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;

import com.cb2.ircmud.domain.Item;
import com.cb2.ircmud.event.Event;
import com.cb2.ircmud.event.VisionEvent;

@RooJavaBean
@RooToString
@RooJpaActiveRecord
public class VisionComponent extends Component {
	private int visibilityValue;
	private String description;
	private boolean visible;
	
	public VisionComponent() {
		visibilityValue = 100;
		description = "";
		visible = true;
	}
	
	@Override
	public void handleEvent(Event event) {
		if (event.getType() == Event.Type.Look) {
			//Can't see itself
			if (((Item)event.getSender()).getId() != this.getItem().getId() && visible) {
				
				//TODO: Visibility check?
				event.addChildEvent(new VisionEvent(this.getItem()));
			}
		}
	}
	
	@Override
	public Component cloneComponent() {
		VisionComponent clone = new VisionComponent();
		clone.visibilityValue = this.visibilityValue;
		clone.description = this.description;
		clone.visible = this.visible;
		return clone;
	}
}
