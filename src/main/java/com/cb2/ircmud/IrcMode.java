package com.cb2.ircmud;

public class IrcMode {
	
	public enum Channel {
		o('o'),
		p('p'),
		s('s'),
		i('i'),
		t('t'),
		n('n'),
		m('m'),
		l('l'),
		b('b'),
		v('v'),
		k('k');
		
		char ch;
		String context = new String();
		private Channel(char ch) {
			this.ch = ch;
		}
		public void context(String string) {
			this.context = string;
		}
	}
	public enum User {
		i('o'),
		s('p'),
		w('s'),
		o('i'),
		O('t');
		
		char ch;
		String context = new String();
		private User(char ch) {
			this.ch = ch;
		}
		public void context(String string) {
			this.context = string;
		}
	}

}
