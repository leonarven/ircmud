package com.cb2.ircmud.command;

public class ItemParameter extends CommandParameter {
	public enum QuantityType {
		Specific, //The
		Number,
		All       //All/Every
	}
	public enum Owner {
		Unspesified,
		Caller			// "my item"
	}
	
	
	private String itemName;
	private int quantity;
	private QuantityType quantityType;
	private Owner owner;
	
	public ItemParameter(int quantity, String itemName) {
		this.quantityType = QuantityType.Number;
		this.itemName = itemName;
		this.quantity = quantity;
		this.owner = Owner.Unspesified;
	}
	
	public ItemParameter(QuantityType type, String itemName) {
		this.quantityType = type;
		this.itemName = itemName;
		this.quantity = 1;
		this.owner = Owner.Unspesified;
	}
	
	@Override
	public Type type() {
		return Type.Item;
	}
	
	public String getItemNameWithArticle() {
		String prefix = null;
		 switch (quantityType) {
		 case All:
			 prefix = "all ";
			 break;
			 
		 case Number:
			 if (quantity == 1) {
				 prefix = "a ";
			 }
			 else {
				 prefix = String.valueOf(quantity) + " ";
			 }
			 break;
		 case Specific:
			 prefix = "the ";
			 break;
		 }
		 return prefix + itemName;
	}

	public String getItemName() { return itemName; }
	public QuantityType getQuantityType() { return quantityType; }
	public Owner getOwner() { return owner; }
	public void setOwner(Owner o) { owner = o; }
	public int getQuantity() { return quantity; }
	public void setQuantity(int q) { quantity = q; }
}
