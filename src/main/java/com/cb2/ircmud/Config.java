package com.cb2.ircmud;

import java.util.Properties;
import java.io.FileInputStream;
import java.io.IOException;

import org.slf4j.Logger;
import org.springframework.stereotype.Component;

import com.github.rlespinasse.slf4j.spring.AutowiredLogger;

@Component
public class Config {
	
	@AutowiredLogger
	Logger logger;
	
	/* Kanaviin liittyvä rajoite
	 * Maailman tiloihin viittaavien kanavien etuliite
	 */
	public String WorldChannelPrefix = "#";

	/* 
	 * Maailman oletustila/-kanava, johon pelaajat liitetään. Manjäristykset ja meteoripommitukset
	 */
	public String WorldChannel = WorldChannelPrefix + "world";
	
	/* 
	 * IRC-Serverin määrite: portti, jota kuunnellaan
	 */
	public int ServerPort = 6667;
	
	/* 
	 * IRC-Serverin määrite: nimi, jolla serveriä ylläpidetään
	 */
	public String ServerName = "IrcMud";

	public String gameCommandPrefix = "!";
	
	public int connectionPingTime    = 60000;
	public int connectionPingTimeout = 100000;
	
	public String ircCommandsXmlFile = "ircCommands.xml";
	
	/*
	 * Config.load tulee lataamaan tiedostosta asetukset
	 */
	public boolean load( String file ) throws IOException {
		
		//TODO: Make me
		
		logger.info("Config", "Loading configurations from file " +file);

		Properties config = new Properties();
		
		config.load(new FileInputStream( file ));

		return false;
	}	
}
