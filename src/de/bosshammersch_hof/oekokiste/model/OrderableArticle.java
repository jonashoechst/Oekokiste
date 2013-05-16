package de.bosshammersch_hof.oekokiste.model;

public class OrderableArticle extends Article {

	private float price;
	private int maxOrder;
	
	public OrderableArticle(int id, String name, String description, float price) {
		super(id, name, description);
		this.price = price;
		this.maxOrder = 0;
	}

	public float getPrice() {
		return price;
	}

	public void setPrice(float price) {
		this.price = price;
	}

	public int getMaxOrder() {
		return maxOrder;
	}

	public void setMaxOrder(int maxOrder) {
		this.maxOrder = maxOrder;
	}

}
