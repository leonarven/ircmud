package com.cb2.ircmud.domain;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import javax.persistence.Embeddable;

@Embeddable
public final class Name implements Cloneable, Serializable {
	public static final int ITEM_QUANTITY_UNSPESIFIED = -1;
	
	public static final int USE_ARTICLE_AN = 1;
	public static final int PROPER_NOUN = 2;
	
	private static final long serialVersionUID = 1L;
	
	private String name;
	private String pluralForm;
	private int flags;
	
	public Name() {
		this.name = null;
		this.pluralForm = null;
		this.flags = 0;
	}
	
	public Name(String name) {
		name = name.toLowerCase();
		if ("aeiouåäö".indexOf(name.charAt(0)) >= 0) {
			this.flags = USE_ARTICLE_AN;
		}
		else {
			this.flags = 0;
		}
		this.name = name;
		this.pluralForm = null;
	}
	
	public Name(String name, String pluralForm) {
		this(name);
		this.pluralForm = pluralForm;
	}
	
	public Name(String name, String pluralForm, int flags) {
		this.name = name;
		this.pluralForm = pluralForm;
		this.flags = flags;
	}
	
	public String getName() { return name; }
	public String getPluralForm() {
		if (pluralForm == null) {
			return name + "s";
		}
		return pluralForm;
	}
	public String getPluralFormRaw() { return pluralForm; }
	
	public String undefiniteArticle() {
		if ((this.flags & USE_ARTICLE_AN) == USE_ARTICLE_AN) return "an";
		return "a";
	}
	
	public String definiteArticle() { return "the"; }
	
	public String getItemName(int itemQuantity, boolean definite) {
		if (itemQuantity == 1) {
			if ((this.flags & PROPER_NOUN) == PROPER_NOUN) {
				return getName();
			}
			if (definite) {
				return definiteArticle() + " " + getName();
			}
			return undefiniteArticle() + " " + getName();
		}
		if (itemQuantity == 0) {
			return "no " + getPluralForm();
		}
		if (itemQuantity < 0) { // ITEM_QUANTITY_UNSPESIFIED
			if (definite && (this.flags & PROPER_NOUN) != PROPER_NOUN) {
				return definiteArticle() + " " + getPluralForm();
			}
			return getPluralForm();
		}
		
		return String.valueOf(itemQuantity) + " " + getPluralForm();
	}
	
	
	
	void setName(String name) { this.name = name; }
	void setPluralForm(String pf) { this.pluralForm = pf; }
	
	
	@Override
	public Object clone() {
		Name c = new Name();
		c.name = this.name;
		c.pluralForm = this.pluralForm;
		c.flags = this.flags;
		return c;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if ( !(obj instanceof Name) ) return false;
		Name v = (Name)obj;
		return this.name.equals(v.name) && this.pluralForm.equals(v.pluralForm) && (this.flags == v.flags);
	}
	
	public static Name createCharacterName(String name) {
		return new Name(name, null, PROPER_NOUN);
	}
	
	private void readObject( ObjectInputStream aInputStream) throws ClassNotFoundException, IOException {
		 aInputStream.defaultReadObject();
	}

	private void writeObject( ObjectOutputStream aOutputStream) throws IOException {
		aOutputStream.defaultWriteObject();
	}
	
	@Override
	public String toString() {
		return getName();
	}
}
