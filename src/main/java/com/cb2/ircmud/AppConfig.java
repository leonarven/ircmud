package com.cb2.ircmud;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("file:src/main/resources/META-INF/spring/config.properties")
public class AppConfig {
    @Autowired
    ApplicationContext ctx;

    @Bean
    public Ircmud ircmud() {
        return ctx.getBean(Ircmud.class);
    }
}
