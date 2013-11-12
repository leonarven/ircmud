package com.cb2.ircmud;

public class Config {
	/* Kanaviin liittyvä rajoite
	 * Maailman tiloihin viittaavien kanavien etuliite
	 */
	protected static String WorldChannelPrefix = "#";

	/* 
	 * Maailman oletustila/-kanava, johon pelaajat liitetään. Manjäristykset ja meteoripommitukset
	 */
	protected static String WorldChannel = WorldChannelPrefix + "World";
	
	/* 
	 * IRC-Serverin määrite: portti, jota kuunnellaan
	 */
	protected static int    ServerPort = 6667;
	
	/* 
	 * IRC-Serverin määrite: nimi, jolla serveriä ylläpidetään
	 */
	protected static String ServerName = "IrcMud";
}
