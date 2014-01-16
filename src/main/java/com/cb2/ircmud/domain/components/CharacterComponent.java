package com.cb2.ircmud.domain.components;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.ElementCollection;
import javax.persistence.MapKeyEnumerated;
import javax.persistence.OneToMany;

import org.hibernate.type.IntegerType;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;

import com.cb2.ircmud.event.Event;
import com.cb2.ircmud.CharacterAttribute;

@RooJavaBean
@RooToString
@RooJpaActiveRecord
public class CharacterComponent extends Component {
    private int age;
    private int height;
    
    @MapKeyEnumerated
    @ElementCollection
    private Map<CharacterAttribute.Type, Integer> attributes = new HashMap<CharacterAttribute.Type, Integer>();
    
    
    
	@Override
	public void handleEvent(Event event) {
		// TODO Auto-generated method stub
		
	}
    
}
