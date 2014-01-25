package com.cb2.ircmud.domain.components;

import javax.persistence.Embedded;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;

import com.cb2.ircmud.domain.Vec3;


@RooJavaBean
@RooToString
@RooJpaActiveRecord
public class SizeComponent extends Component {
	@Embedded
	private Vec3 size;
	private double weight;
	@Override
	public Component cloneComponent() {
		SizeComponent clone = new SizeComponent();
		clone.size = this.size;
		clone.weight = this.weight;
		return clone;
	}
}
