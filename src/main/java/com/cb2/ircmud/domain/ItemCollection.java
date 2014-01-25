package com.cb2.ircmud.domain;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.OneToMany;
import javax.persistence.Transient;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;

import com.cb2.ircmud.domain.services.ItemService;

@RooJavaBean
@RooToString
@RooJpaActiveRecord
public class ItemCollection {
	@Transient
	@Autowired
	private ItemService itemService;
	
	@OneToMany
	private Map<String, Item> itemMap = new HashMap<String, Item>();
	
	public void storeItem(Item item) {
		itemMap.put(item.getHiddenName(), item);
	}
	
	public Item findItem(String hiddenName) {
		return itemMap.get(hiddenName);
	}
	
	public Item cloneItem(String hiddenName) {
		Item i = itemMap.get(hiddenName);
		if (i == null) return null;
		return itemService.clone(i);
	}
	
}
