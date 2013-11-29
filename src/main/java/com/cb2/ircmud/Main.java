package com.cb2.ircmud;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

public class Main {
	public static void main(String[] args) {
        System.out.println("DEBUG: Main::main()");

        ApplicationContext ctx = new FileSystemXmlApplicationContext(Config.applicationContextFile);
        
        //Do what @Autowired would do, because we are not in the context yet
        Ircmud ircmud = ctx.getBean(Ircmud.class);
        ircmud.main(args);
    }
}
