package de.bosshammersch_hof.oekokiste.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable
public class OrderedArticle {

	@DatabaseField(generatedId = true)
	private int id;

	@DatabaseField(foreign = true,foreignAutoCreate = true,foreignAutoRefresh = true)
	private Article article;
	
	@DatabaseField
	private double amount;
	
	@DatabaseField
	private String amountType;
	
	@DatabaseField
	private int price;

	private OrderedArticle(){	
	}
	
	public OrderedArticle(Article article, double amount, String amountType, int price) {
		this();
		this.article = article;
		this.amountType = amountType;
		this.amount = amount;
		this.price = price;
	}
	
	public Article getArticle() {
		return article;
	}

	public void setArticle(Article article) {
		this.article = article;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public String getAmountType() {
		return amountType;
	}

	public void setAmountType(String amountType) {
		this.amountType = amountType;
	}

	public int getPrice() {
		return price;
	}

	public double getTotalPrice() {
		return price*amount;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	
	
}
