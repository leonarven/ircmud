package com.cb2.ircmud;

public class GameCommand {
	public static enum Action {
		UNDEFINED,
		WALK, // Kun pelaaja haluaa liikkua
		RUN,  // Kun pelaaja haluaa juosta (kuluttaa energiaa enemmän)
		HIDE; // Kun pelaaja haluaa piiloutua
	};

	public static enum Target {
		UNDEFINED,
		UNSPECIFIED,
		SELF,
		PLAYER,
		LOCATION,
		COMPASSPOINT;
		
		private String target;
		
		public void setTarget (String target) {
			this.target = target;
		}
		
		public String target() {
			return this.target;
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
}
