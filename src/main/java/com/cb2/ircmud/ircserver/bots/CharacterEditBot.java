package com.cb2.ircmud.ircserver.bots;

import java.util.List;
import java.util.Set;
import java.util.Vector;

import javax.persistence.Transient;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.cb2.ircmud.CharacterEditState;
import com.cb2.ircmud.PlayerGameState;
import com.cb2.ircmud.PlayerState;
import com.cb2.ircmud.domain.Item;
import com.cb2.ircmud.domain.Name;
import com.cb2.ircmud.domain.Player;
import com.cb2.ircmud.domain.World;
import com.cb2.ircmud.domain.components.CharacterComponent;
import com.cb2.ircmud.domain.services.CharacterService;
import com.cb2.ircmud.domain.services.GameService;
import com.cb2.ircmud.domain.services.PlayerService;
import com.cb2.ircmud.domain.services.WorldService;
import com.cb2.ircmud.ircserver.IrcUser;

@Component
public class CharacterEditBot extends IrcBotUser {
	@Autowired
	PlayerService playerService;
	@Autowired
	CharacterService characterService;
	@Autowired
	WorldService worldService;
	@Autowired
	GameService gameService;
	@Autowired
	Environment env;

	public CharacterEditBot() {
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
		CREATE("CREATE",   "<character name> <world name>  - Creates new character and enters to edit mode"),
		PLAY("PLAY", "<character name>   - Starts or continues playing as the character"),
		EDIT("EDIT", "<character name>      - Enters to the edit mode"),
		NAME("NAME", "[<character name>]      - Prints or sets the character name"),
		LIST("LIST", "    - Lists all created characters"),
		DESCRIPTION("DESCRIPTION", "[<description>]     - Prints or sets the character description"),
		SKILL("SKILL", "LIST|<skill name>"),
		EXIT("EXIT", "     - leaves the edit mode"),
		HELP(  "HELP",   "<command name>"),
		UNKNOWN(  "",   "");
		
		public String command, usage;
		
		private Command(String command, String usage) {
			this.command = command;
			this.usage = usage;
		}
		public String getUsage() {
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
	
	@Transactional
	public void handleCommand(IrcUser user, String command_str, Vector<String> params) {
		if (!user.isLoggedIn()) {
			user.sendMessage(this, "You have to be logged in to use this bot");
			return;
		}
		
		Player player = Player.findPlayer(user.getPlayerId());
		
		if (playerService.isPlayerInGame(player)) {
			user.sendMessage(this, "You can't edit your characters while playing. Please leave the game world and try again.");
			return;
		}
		
		PlayerState state = playerService.getPlayerStateByGroup(player, PlayerGameState.STATE_GROUP_PLAY_AND_CHARACTER_EDIT);
		CharacterEditState charCreationState = null;
		if (state instanceof CharacterEditState) charCreationState = (CharacterEditState)state;
		
		Command command = null;
		
		try {
			command = Command.valueOf(command_str.toUpperCase());
		} catch(IllegalArgumentException e) {}
		
		if (command == null) command = Command.UNKNOWN;
		switch(command) {
			case CREATE: {
				if (charCreationState != null) {
					user.sendMessage(this, "Exit the character editing before trying to create a new character.");
					break;
				}
				handleCreateCommand(player, command, params);
				break;
			}
			case PLAY: {
				if (charCreationState != null) {
					user.sendMessage(this, "Please exit the character editing before starting the game.");
					break;
				}
				handlePlayCommand(player, command, params);
				break;
			}
			case LIST: {
				String msg = " ";
				List<String> charNames = playerService.getPlayerCharacterNames(player);
				for (String cc : charNames) {
					msg = msg + "\"" + cc + "\"  ";
				}
				user.sendMessage(this, msg);
				break;
			}
			case EDIT: {
				if (charCreationState != null) {
					user.sendMessage(this, "You are already editing a character.");
					break;
				}
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
				break;
		}
	}
	
	public void handleCharacterEditModeCommand(Player player, CharacterEditState charEditState, Command cmd, Vector<String> params) {
		//TODO: implement this
		switch (cmd) {
			case EXIT:
				charEditState.saveChanges();
				player.removeState(charEditState);
				player.getIrcUser().sendMessage(this, "Changes to the character \"" + charEditState.getCharacter().getName() + "\" saved and you have exited the edit mode");
				return;
			case DESCRIPTION:
				break;
			case NAME:
				if (params.size() == 0) {
					player.getIrcUser().sendMessage(this,  charEditState.getCharacter().getName().getName());
				}
				else if (params.size() == 1) {
					
				}
				break;
			case SKILL:
				break;
			default:
				break;
		}
	}
	
	@Transactional
	public void handlePlayCommand(Player player, Command command, Vector<String> params) {
		if (params.size() != 1) {
			player.getIrcUser().sendMessage(this, command.getUsage());
			return;
		}
		
		Item character = player.findCharacterByName(params.get(0));
		if (character == null) {
			player.getIrcUser().sendMessage(this, "Can't find a character \"" + params.get(0) + "\"");
			return;
		}
		
		PlayerGameState state = new PlayerGameState(player, character);
		player.addState(state);
		gameService.joinPlayerToGame(character);
	}
	
	public void handleEditCommand(Player player, Command cmd, Vector<String> params) {
		if (params.size() != 1) {
			player.getIrcUser().sendMessage(this, cmd.getUsage());
			return;
		}
		Item character = player.findCharacterByName(params.firstElement().toLowerCase());
		if (character == null) {
			player.getIrcUser().sendMessage(this, "Can't find character with name");
			return;
		}
		
		startEditing(player, character);
	}
	
	@Transactional
	public void handleCreateCommand(Player player, Command cmd, Vector<String> params) {
		if (params.size() != 2) {
			player.getIrcUser().sendMessage(this, cmd.getUsage());
			return;
		}
		
		World world = worldService.findWorldByName(params.get(1));
		if (world == null) {
			player.getIrcUser().sendMessage(this, "Can't find world with name \"" + params.get(1) + "\"");
			return;
		}
		String characterName = params.get(0);
		Item character = worldService.findCharacterByName(world, characterName);
		if (character != null) {
			player.getIrcUser().sendMessage(this, "There is already character named \"" + characterName + "\"");
			return;
		}
		
		character = characterService.createPlayerCharacter(player, characterName, world);
		player.addCharacter(character.findFirstComponentInstanceOf(CharacterComponent.class));
		
		startEditing(player, character);
	}
	
	public void startEditing(Player player, Item character) {
		CharacterEditState state = new CharacterEditState(player, character);
		player.addState(state);
		player.getIrcUser().sendMessage(this, "Started editing character \"" + character.getName().getName() + "\"");
	}
}
