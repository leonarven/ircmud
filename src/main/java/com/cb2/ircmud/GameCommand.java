package com.cb2.ircmud;

public class GameCommand {
	public static enum Action {
		WALK, // Kun pelaaja haluaa liikkua
		RUN,  // Kun pelaaja haluaa juosta (kuluttaa energiaa enemmän)
		HIDE; // Kun pelaaja haluaa piiloutua
	};

	public static enum Target {
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
	
	Target target;
	Action action;
	
	public GameCommand(Target target, Action action) {
		this.target = target;
		this.action = action;
	}
}
