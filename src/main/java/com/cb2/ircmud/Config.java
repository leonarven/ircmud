package com.cb2.ircmud;

import java.util.Properties;
import java.io.FileInputStream;
import java.io.IOException;

public class Config {
	/* Kanaviin liittyvä rajoite
	 * Maailman tiloihin viittaavien kanavien etuliite
	 */
	public static String WorldChannelPrefix = "#";

	/* 
	 * Maailman oletustila/-kanava, johon pelaajat liitetään. Manjäristykset ja meteoripommitukset
	 */
	public static String WorldChannel = WorldChannelPrefix + "world";
	
	/* 
	 * IRC-Serverin määrite: portti, jota kuunnellaan
	 */
	public static int ServerPort = 6667;
	
	/* 
	 * IRC-Serverin määrite: nimi, jolla serveriä ylläpidetään
	 */
	public static String ServerName = "IrcMud";

	/* 
	 * 
	 */
	public static String applicationContextFile = "src/main/resources/META-INF/spring/applicationContext.xml";

	public static String gameCommandPrefix = "!";
	
	public static int connectionPingTime    = 60000;
	public static int connectionPingTimeout = 100000;
	
	public static String ircCommandsXmlFile = "ircCommands.xml";
	
	/*
	 * Config.load tulee lataamaan tiedostosta asetukset
	 */
	public static boolean load( String file ) throws IOException {
		
		//TODO: Make me
		
		Console.out("Config", "Loading configurations from file " +file);

		Properties config = new Properties();
		
		config.load(new FileInputStream( file ));

		return false;
	}	
}
