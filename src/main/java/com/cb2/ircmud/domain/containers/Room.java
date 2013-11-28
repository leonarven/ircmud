package com.cb2.ircmud.domain.containers;


import com.cb2.ircmud.domain.World;
import com.cb2.ircmud.ircserver.Channel;

import javax.persistence.ManyToOne;
import javax.persistence.Transient;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJpaActiveRecord
public class Room extends Container {

    private String name;

    @ManyToOne
    private World world;
}
