package com.cb2.ircmud.domain.components;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.ElementCollection;
import javax.persistence.MapKeyEnumerated;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import org.hibernate.type.IntegerType;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;

import com.cb2.ircmud.domain.containers.Inventory;
import com.cb2.ircmud.event.Event;
import com.cb2.ircmud.CharacterAttribute;

@RooJavaBean
@RooToString
@RooJpaActiveRecord
public class CharacterComponent extends Component {
    private int age;
    
    @MapKeyEnumerated
    @ElementCollection
    private Map<CharacterAttribute.Type, Integer> attributes = new HashMap<CharacterAttribute.Type, Integer>();
    
    @OneToOne
    private Inventory inventory;
    
    
	@Override
	public void handleEvent(Event event) {
		// TODO Auto-generated method stub
		
	}



	@Override
	public Component cloneComponent() {
		CharacterComponent clone = new CharacterComponent();
		clone.age = this.age;
		clone.attributes = new HashMap<CharacterAttribute.Type, Integer>(this.attributes);
		return clone;
	}
    
	
}
