package com.cb2.ircmud;

public class PlayerCommandHandler {

	private GameCommand.Action actionToGameCommandAction(String action) {
		if (action.equals("run"))  return GameCommand.Action.RUN;
		if (action.equals("hide")) return GameCommand.Action.HIDE;
		if (action.equals("walk")) return GameCommand.Action.WALK;
		return GameCommand.Action.UNDEFINED;
	}

	private GameCommand.Target actionToGameCommandTarget(String target, String target_additional) {
		if (target.equals("north"))    return GameCommand.Target.COMPASSPOINT.setTarget("north");
		if (target.equals("east"))     return GameCommand.Target.COMPASSPOINT.setTarget("east");
		if (target.equals("south"))    return GameCommand.Target.COMPASSPOINT.setTarget("south");
		if (target.equals("west"))     return GameCommand.Target.COMPASSPOINT.setTarget("west");
		if (target.equals("player"))   return GameCommand.Target.PLAYER.setTarget(target_additional);
		if (target.equals("location")) return GameCommand.Target.LOCATION.setTarget(target_additional);
		if (target.equals(""))         return GameCommand.Target.SELF;
		return GameCommand.Target.UNDEFINED;
	}

	private GameCommand.Target actionToGameCommandTarget(String target) {
		return actionToGameCommandTarget(target, "");
	}

	public boolean isGameCommand(String command_str) {
		return command_str.startsWith(Config.gameCommandPrefix);
	}
	
	public GameCommand parse(String command_str) {
		
		if (!isGameCommand(command_str)) return null;
		command_str = command_str.substring(Config.gameCommandPrefix.length());
		
		GameCommand.Action action = GameCommand.Action.UNDEFINED;
		GameCommand.Target target = GameCommand.Target.UNDEFINED;
		
		// Ver 0.0.1. Syntaksi muotoa "!<action> <target>"
		String[] token = command_str.split(" ");
		String action_str, target_str, target_additional_str;
		switch(token.length) {
			case 1:
				action_str = token[0];
				target_str = "";
				target_additional_str = "";
				break;
			case 2:
				action_str = token[0];
				target_str = token[1];
				target_additional_str = "";
			case 3:
				action_str = token[0];
				target_str = token[1];
				target_additional_str = token[2];
				break;
			default:
			return null;
		}
		
		action = this.actionToGameCommandAction(action_str);		
		target = this.actionToGameCommandTarget(target_str, target_additional_str);
		
		GameCommand command = new GameCommand(action, target);
		
		return command;
	} 
	
}
