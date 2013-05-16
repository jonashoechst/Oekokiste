package de.bosshammersch_hof.oekokiste.model;

public class OrderedArticle extends Article {
	
	private int count;
	private float price;

	public OrderedArticle(int id, String name, String description, float price, int count) {
		super(id, name, description);
		
		this.price = price;
		this.count = count;
		
	}
	
	public float getPrice() {
		return price;
	}

	public void setPrice(float price) {
		this.price = price;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}
	
}
