package com.cb2.ircmud.domain;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJpaActiveRecord
public class UserCharacter {

    /**
     */
    private String name;
    
    /**
     * Attributes
     */
    private int age;
    private int health;
    
}