package com.cb2.ircmud.domain.components;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJpaActiveRecord
public class CharacterComponent extends Component {

    /**
     * Attributes
     */
    private int age;
    private int height;
    private int speed;
    private int awareness;
    
    /**
     * Skills
     */
    private int skillDodging;
    private int skillMeleeAttack;
    private int skillSwordAttack;
    private int skillBowAttack;
    private int skillThrowing;
    private int skillSneaking;
    private int skillHiding;
    private int skillRepairing;
    
}
