package com.cb2.ircmud.ircserver.bots;

import java.util.Vector;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import com.cb2.ircmud.CharacterEditState;
import com.cb2.ircmud.PlayerGameState;
import com.cb2.ircmud.PlayerState;
import com.cb2.ircmud.domain.Item;
import com.cb2.ircmud.domain.Player;
import com.cb2.ircmud.domain.World;
import com.cb2.ircmud.domain.services.CharacterService;
import com.cb2.ircmud.domain.services.PlayerService;
import com.cb2.ircmud.domain.services.WorldService;
import com.cb2.ircmud.ircserver.IrcUser;

@Component
public class CharacterCreationBot extends IrcBotUser {
	@Autowired
	PlayerService playerService;
	@Autowired
	CharacterService characterService;
	@Autowired
	WorldService worldService;
	@Autowired
	Environment env;

	public CharacterCreationBot() {
		parsePrivateMessages = true;
	}
	
	@Override
	protected void init(){
		nickname=env.getProperty("config.bots.character.name");
		username=env.getProperty("config.bots.character.username");
		realname=env.getProperty("config.bots.character.realname");
		hostname=env.getProperty("config.bots.character.hostname");
	}
	
	public enum Command {
		CREATE("CREATE",   "CREATE <character name> <world name>  - Creates new character and enters to edit mode"),
		EDIT("EDIT", "EDIT <character name>      - Enters to the edit mode"),
		NAME("NAME", "NAME [<character name>]      - Prints or sets the character name"),
		DESCRIPTION("DESCRIPTION", "DESCRIPTION [<description>]     - Prints or sets the character description"),
		SKILL("SKILL", "SKILL LIST|<skill name>"),
		EXIT("EXIT", "EXIT  - leaves the edit mode"),
		HELP(  "HELP",   "<command name>"),
		UNKNOWN(  "",   "");
		
		public String command, usage;
		
		private Command(String command, String usage) {
			this.command = command;
			this.usage = usage;
		}
		public String usage() {
			return "Usage: "+command+" "+usage;
		}
	}
	
	
	@Override
	public void receivePrivateMessage(IrcUser user, String msg) {
		if (user == null) return;
		msg = msg.trim();
		if (msg.isEmpty()) return;
		
		String commandName;
		Vector<String> command = parseCommand(msg);
		commandName = command.firstElement();
		command.remove(0);
		
		handleCommand(user, commandName, command);
	}
	
	public void handleCommand(IrcUser user, String command_str, Vector<String> params) {
		if (!user.isLoggedIn()) {
			user.sendMessage(this, "You have to be logged in to use this bot");
			return;
		}
		
		Player player = user.getPlayer();
		
		if (player.isPlaying()) {
			user.sendMessage(this, "You can't edit your characters while playing. Please leave the game world and try again.");
			return;
		}
		
		PlayerState state = player.getStateByGroup(PlayerGameState.STATE_GROUP_PLAY_AND_CHARACTER_EDIT);
		CharacterEditState charCreationState = null;
		if (state instanceof CharacterEditState) charCreationState = (CharacterEditState)state;
		
		Command command = null;
		
		try {
			command = Command.valueOf(command_str.toUpperCase());
		} catch(IllegalArgumentException e) {}
		
		if (command == null) command = Command.UNKNOWN;
		switch(command) {
			case CREATE: {
				handleCreateCommand(player, command, params);
				break;
			}
			case EDIT: {
				handleEditCommand(player, command, params);
				break;
			}
			default: {
				if (charCreationState == null) {
					user.sendMessage(this, "This command is only available when editing a character");
				}
				else {
					handleCharacterEditModeCommand(player, charCreationState, command, params);
				}
				break;
			}
				
			case UNKNOWN:
				user.sendMessage(this, "Unknown command \"" + command_str + "\"");
		}
	}
	
	public void handleCharacterEditModeCommand(Player player, CharacterEditState charCreationState, Command cmd, Vector<String> params) {
		//TODO: implement this
	}
	
	public void handleEditCommand(Player player, Command cmd, Vector<String> params) {
		if (params.size() != 1) {
			player.getIrcUser().sendMessage(this, cmd.usage());
			return;
		}
		Item character = player.findCharacterByName(params.firstElement().toLowerCase());
		if (character == null) {
			player.getIrcUser().sendMessage(this, "Can't find character with name");
			return;
		}
		
		startEditing(player, character);
	}
	
	public void handleCreateCommand(Player player, Command cmd, Vector<String> params) {
		if (params.size() != 2) {
			player.getIrcUser().sendMessage(this, cmd.usage());
			return;
		}
		
		World world = worldService.findWorldByName(params.get(1));
		if (world == null) {
			player.getIrcUser().sendMessage(this, "Can't find world with name \"" + params.get(1) + "\"");
			return;
		}
		
		Item character = world.findCharacterByName(params.get(0));
		if (character != null) {
			player.getIrcUser().sendMessage(this, "Can't find character with name \"" + params.get(0) + "\"");
			return;
		}
		
		
		
		startEditing(player, character);
	}
	
	public void startEditing(Player player, Item character) {
		CharacterEditState state = new CharacterEditState(player, character);
		player.addState(state);
		player.getIrcUser().sendMessage(this, "Started editing character \"" + character.getName() + "\"");
	}
}
