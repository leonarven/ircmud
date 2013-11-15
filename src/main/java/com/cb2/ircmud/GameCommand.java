package com.cb2.ircmud;

public class GameCommand {
	public static enum Action {
		UNDEFINED,
		WALK, // Kun pelaaja haluaa liikkua
		RUN,  // Kun pelaaja haluaa juosta (kuluttaa energiaa enemmän)
		HIDE; // Kun pelaaja haluaa piiloutua
		
		@Override
		public String toString() {
			switch(this) {
				case WALK: return "Walk";
				case RUN: return "Run";
				case HIDE: return "Hide";
				case UNDEFINED: return "Undefined";
			}
			return "Undefined";
		}
	};

	public static enum Target {
		UNDEFINED,
		UNSPECIFIED,
		SELF,
		PLAYER,
		LOCATION,
		COMPASSPOINT;
		
		private String target;
		
		public Target setTarget (String target) {
			this.target = target;
			return this;
		}
		
		public String target() {
			return this.target;
		}
		
		@Override
		public String toString() {
			String str = "Undefined";
			switch(this) {
				case SELF: str = "Self"; break;
				case PLAYER: str = "Player"; break;
				case LOCATION: str = "Location"; break;
				case COMPASSPOINT: str = "Compasspoint"; break;
				case UNDEFINED: str = "Undefined"; break;
				case UNSPECIFIED: str = "Unspecified"; break;
			}
			if (this.target != null) str = str + "("+this.target+")";
			return str;
		}
	}
	
	Action action;
	Target target;
	
	public GameCommand(Action action, Target target) {
		this.action = action;
		this.target = target;
	}
	public GameCommand(Action action) {
		this(action, Target.UNSPECIFIED);
	}
	
	public Action action() {
		return this.action;
	}
	public Target target() {
		return this.target;
	}
	@Override
	public String toString() {
		return "COMMAND{action:("+this.action.toString()+"),target:("+this.target.toString()+")}";
	}
}
