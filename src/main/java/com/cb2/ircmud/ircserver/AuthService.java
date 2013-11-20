package com.cb2.ircmud.ircserver;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashMap;

import com.cb2.ircmud.Console;
import com.cb2.ircmud.Player;


public class AuthService {
	
	public static enum AuthError {
		ERR_OK,
		ERR_INVALIDLOGIN,
		ERR_ALREADYLOGGED,
	}
	
	public static class Account {
		public String  username;
		public byte[]  passdigest;
		public IrcUser user;
		
		public Account(String username, byte[] passdigest) {
			this.username   = username;
			this.passdigest = passdigest;
			this.user       = null;
		}
		public void var_dump() {
			Console.debug("AuthService.Account", "User    " + this.username);
			Console.debug("AuthService.Account", "Hash    " + new String(this.passdigest));
			Console.debug("AuthService.Account", "IrcUser " + (this.user==null?"null":this.user.getRepresentation()));
		}
	}
	
	private static MessageDigest digestInstance = null;
	
	private static HashMap<String, Account> accountMap = new HashMap<String, Account>();
	private static HashMap<String, String> loginMap = new HashMap<String, String>();
	
	public static void init() {
		try {
			digestInstance = MessageDigest.getInstance("MD5");
		} catch(NoSuchAlgorithmException e) {
			Console.err("AuthService", "NoSuchAlgorithmException: "+e.getMessage());
		}
	}
	
	public static boolean addAccount(String username, String password) {
		if (accountMap.containsKey(username)) return true;

		Account acc = new Account(username, AuthService.digestInstance.digest(password.getBytes()));
		accountMap.put(username, acc);
		
		return true;
	}
	
	private static boolean compareBytes(byte[] bytes1, byte[] bytes2) {
		return Arrays.equals(bytes1, bytes2);
	}
	
	private static Player getPlayer(Account account) {
		// TODO: 
		return new Player(account.user.getNickname(), account.username);
	}

	public static Account login(String username, byte[] passdigest, IrcUser user) {
		if (!accountMap.containsKey(username))  return null;
		if (!compareBytes(accountMap.get(username).passdigest, passdigest)) return null;

		accountMap.get(username).user = user;
		return accountMap.get(username);
	}

	public static Account login(String username, String password, IrcUser user) {
		return login(username, AuthService.digestInstance.digest(password.getBytes()), user);
	}

	public static boolean testLogin(String username) {
		if (!accountMap.containsKey(username)) return false;

		return accountMap.get(username).user != null;
	}
}
