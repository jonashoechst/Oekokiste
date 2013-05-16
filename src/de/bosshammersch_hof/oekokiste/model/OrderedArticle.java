package de.bosshammersch_hof.oekokiste.model;

public class OrderedArticle extends Article {
	
	private int count;
	
	private int price;

	public OrderedArticle(int id, String name, String description, int price, int count) {
		super(id, name, description);
		this.price = price;
		this.count = count;
	}
	
	public int getPrice() {
		return price;
	}
	
	public int getTotalPrice() {
		return count*price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}
}
