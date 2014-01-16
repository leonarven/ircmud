package com.cb2.ircmud.domain.services;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.PostConstruct;
import org.springframework.stereotype.Service;
import com.cb2.ircmud.CharacterAttribute;

@Service
public class CharacterAttributeService {
	private Map<CharacterAttribute.Type, CharacterAttribute> characterAttributes = new HashMap<CharacterAttribute.Type, CharacterAttribute>();
	
	@PostConstruct
	private void initialize() {
		createCharacterAttributes();
	}
	
	private void createCharacterAttributes() {
		addCharacterAttribute(new CharacterAttribute(CharacterAttribute.Type.Melee, "melee", 100));
		addCharacterAttribute(new CharacterAttribute(CharacterAttribute.Type.Strength, "strength", 100));
		addCharacterAttribute(new CharacterAttribute(CharacterAttribute.Type.BowSkill, "bow", 100));
		addCharacterAttribute(new CharacterAttribute(CharacterAttribute.Type.SwordSkill, "sword", 100));
		addCharacterAttribute(new CharacterAttribute(CharacterAttribute.Type.Magic, "magic", 100));
		addCharacterAttribute(new CharacterAttribute(CharacterAttribute.Type.Dexterity, "dexterity", 100));
		addCharacterAttribute(new CharacterAttribute(CharacterAttribute.Type.Dodging, "dodging", 100));
	}
	public void addCharacterAttribute(CharacterAttribute att) {
		characterAttributes.put(att.getType(), att);
	}
	
	public CharacterAttribute getCharacterAttribute(CharacterAttribute.Type type) {
		return characterAttributes.get(type);
	}
	
	public Collection<CharacterAttribute> getCharacterAttributes() {
		return characterAttributes.values();
	}
	
}
