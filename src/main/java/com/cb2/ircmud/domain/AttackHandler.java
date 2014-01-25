package com.cb2.ircmud.domain;
import javax.persistence.Transient;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;

import com.cb2.ircmud.domain.services.CharacterAttributeService;
import com.cb2.ircmud.domain.services.CharacterService;

@RooJavaBean
@RooToString
@RooJpaActiveRecord(inheritanceType = "TABLE_PER_CLASS")
public class AttackHandler {
	@Transient
	@Autowired
	CharacterService service;
	@Transient
	@Autowired
	CharacterAttributeService characterAttributeService;
	
	class AttackResult {
		public boolean hit;
		public int damageInflictedOnAttacker;
		public int damageInflictedOnTarget;
		
	}
	
	AttackResult handleAttack(Item attacker, Item target, Item weapon) {
		AttackResult result = new AttackResult();
		return result;
	}
}
