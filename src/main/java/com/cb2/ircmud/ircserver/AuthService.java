package com.cb2.ircmud.ircserver;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashMap;

import com.cb2.ircmud.Console;

public class AuthService {
	
	public static class Account {
		public String  username;
		public byte[]  passdigest;
		public boolean login;
		
		public Account(String username, byte[] passdigest) {
			this.username   = username;
			this.passdigest = passdigest;
			this.login      = false;
		}
		public void var_dump() {
			Console.debug("AuthService.Account", "User  " + this.username);
			Console.debug("AuthService.Account", "Hash  " + new String(this.passdigest));
			Console.debug("AuthService.Account", "Login " + (this.login?"true":"false"));
		}
	}
	
	private static MessageDigest digestInstance = null;
	
	private static HashMap<String, Account> accountMap = new HashMap<String, Account>();
	
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
	public static Account login(String username, byte[] passdigest) {
		if (!accountMap.containsKey(username))  return null;
		if (!compareBytes(accountMap.get(username).passdigest, passdigest)) return null;

		accountMap.get(username).login = true;
		return accountMap.get(username);
	}
	public static boolean testLogin(String username) {
		if (!accountMap.containsKey(username)) return false;

		return accountMap.get(username).login;
	}
	public static Account login(String username, String password) {
		return login(username, AuthService.digestInstance.digest(password.getBytes()));
	}
}
