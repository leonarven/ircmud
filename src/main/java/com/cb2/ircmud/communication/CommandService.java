package com.cb2.ircmud.communication;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cb2.ircmud.command.Command;
import com.cb2.ircmud.command.CommandParameter;
import com.cb2.ircmud.command.CommandParser;
import com.cb2.ircmud.command.CommandException;
import com.cb2.ircmud.command.LookAroundCommand;
import com.cb2.ircmud.command.LookAtCommand;
import com.cb2.ircmud.domain.Item;
import com.cb2.ircmud.domain.services.SoundService;
import com.cb2.ircmud.event.CommandEvent;
import com.cb2.ircmud.event.Event;
import com.cb2.ircmud.event.EventListener;
import com.cb2.ircmud.event.EventService;
import com.github.rlespinasse.slf4j.spring.AutowiredLogger;

@Service
public class CommandService implements EventListener {
	@Autowired
	private SoundService soundService;
	@Autowired
	private EventService eventService;
	@Autowired
	private CommunicationService communicationService;
	@AutowiredLogger
	Logger logger;
	
	private CommandParser commandParser = new CommandParser();
	
	@PostConstruct
	void initialize() {
		commandParser.addCommandDefinition(new LookAroundCommand());
		commandParser.addCommandDefinition(new LookAtCommand());
	}
	
	@Override
	public void handleEvent(Event event) {
		switch (event.getType()) {
		case Command:
			handleCommandEvent((CommandEvent)event); break;
		default:
		}
		
	}
	
	public void handleCommandEvent(CommandEvent event) {
		Item sender = (Item)event.getSender();
		logger.debug("Command event! sender: " + sender.getName()  + "  cmd: " + event.getCommandString());
		
		try {
			Command cmd = commandParser.parse(event.getCommandString());
			cmd.act(sender);
		} catch (CommandException e) {
			communicationService.sendErrorMessageToCharacter(sender, e.getMessage());
		}
	}

}
