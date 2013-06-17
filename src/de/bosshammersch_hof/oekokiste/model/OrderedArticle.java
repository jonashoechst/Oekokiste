package de.bosshammersch_hof.oekokiste.model;

import java.sql.SQLException;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.misc.BaseDaoEnabled;
import com.j256.ormlite.table.DatabaseTable;

import de.bosshammersch_hof.oekokiste.ormlite.DatabaseManager;

@DatabaseTable
public class OrderedArticle extends BaseDaoEnabled<OrderedArticle, Integer>{

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
	
	@DatabaseField(foreign = true,foreignAutoCreate = true,foreignAutoRefresh = true)
	private Order order;

	public OrderedArticle(){	
		this.setDao(DatabaseManager.getHelper().getOrderedArticleDao());
	}
	
	public void createOrUpdate() throws SQLException{
		DatabaseManager.getHelper().getOrderedArticleDao().createOrUpdate(this);
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

	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}
	
	
	
}
