package com.cb2.ircmud.domain.components;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.OneToOne;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;

import com.cb2.ircmud.domain.AttackHandler;

@RooJavaBean
@RooToString
@RooJpaActiveRecord
public class WeaponComponent extends Component {	
	enum WeaponClass {
		Dagger,
		ShortSword,
		LongSword,
		TwoHandedSword,
		ShortBow,
		LongBow,
		Crossbow,
		Spear
	}
	
	@Enumerated(EnumType.ORDINAL)
	private WeaponClass weaponClass;
	
	@OneToOne
	private AttackHandler attackHandler;

	@Override
	public Component cloneComponent() {
		WeaponComponent clone = new WeaponComponent();
		clone.weaponClass = this.weaponClass;
		clone.attackHandler = attackHandler;
		return clone;
	}
}
