package com.cb2.ircmud;

public class CharacterAttribute {
	public enum Type {
		Strength,
		Dexterity,
		Magic,
		Melee,
		SwordSkill,
		BowSkill,
		Dodging
		//TODO: add more skills
	}
	private Type skillType;
	private String name;
	private int maxLevel;
	
	public CharacterAttribute(Type type, String name, int maxLevel) {
		this.skillType = type;
		this.name = name;
		this.maxLevel = maxLevel;
	}
	
	public Type getType() { return skillType; }
	public String getName() { return name; }
	public int getMaxLevel() { return maxLevel; }
}
