package com.cb2.ircmud.domain;

import javax.persistence.Embeddable;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;

@Embeddable
public class LocationSpecifier {
	public enum Specifier {
		None,
		Over,
		Under,
		Next,
		Front,
		Inside,
		Through
	}
	
	public LocationSpecifier() {
		target = null;
		specifier = Specifier.None;
	}
	
	public LocationSpecifier(Item targetItem, Specifier s) {
		this.target = targetItem;
		this.specifier = s;
	}
	
	
	@Enumerated
	public Specifier specifier;
	@ManyToOne
	public Item target;
	
}
