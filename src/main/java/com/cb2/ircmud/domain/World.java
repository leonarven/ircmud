package com.cb2.ircmud.domain;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.OneToMany;

@RooJavaBean
@RooToString
@RooJpaActiveRecord
public class World {

    /**
     */
    @OneToMany(cascade = CascadeType.ALL)
    private List<UserCharacter> characters = new ArrayList<UserCharacter>();
}
