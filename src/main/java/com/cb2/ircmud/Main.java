package com.cb2.ircmud;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.PropertySource;

public class Main {
	public static void main(String[] args) {
        System.out.println("DEBUG: Main::main()");

        ConfigurableApplicationContext ctx = new FileSystemXmlApplicationContext("src/main/resources/META-INF/spring/applicationContext.xml");
        
        //ctx.refresh();
        //Do what @Autowired would do, because we are not in the context yet
        Ircmud ircmud = ctx.getBean(Ircmud.class);
        ircmud.main(args);
    }
}
