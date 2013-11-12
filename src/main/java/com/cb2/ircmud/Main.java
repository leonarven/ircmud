package com.cb2.ircmud;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

public class Main {

public static void main(String[] args) {
        ApplicationContext ctx = new FileSystemXmlApplicationContext("src/main/resources/META-INF/spring/applicationContext.xml");
 
        Ircmud ircmud = ctx.getBean(Ircmud.class);
        ircmud.main(args);
    }
}
