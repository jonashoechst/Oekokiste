package de.bosshammersch_hof.oekokiste;

public class Order {
	
	String date;
	String name;
	float price;
	
	public Order(){
		this.date = "";
		this.name = "";
		this.price = 0;
	}
	
	public Order (String date, String name, float price){
		this.date = date;
		this.name = name;
		this.price = price;
	}
	
	public String getDate(){
		return this.date;
	}
	
	public String getName(){
		return this.name;
	}
	
	public float getPrice(){
		return this.price;
	}

}
