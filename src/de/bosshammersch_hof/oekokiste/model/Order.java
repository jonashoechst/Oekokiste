package de.bosshammersch_hof.oekokiste.model;

import java.util.Date;
import java.util.List;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable
public class Order {
	
	@DatabaseField(generatedId = true)
	private int id;
	
	@DatabaseField
	private Date date;
	
	@DatabaseField
	private String name;
	
	@DatabaseField
	private List<OrderedArticle> articleList;
	
	@DatabaseField
	private String barcodeString;
	
	public Order(int id, Date date, String name,
			List<OrderedArticle> articleList) {
		super();
		this.id = id;
		this.date = date;
		this.name = name;
		this.articleList = articleList;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<OrderedArticle> getArticleList() {
		return articleList;
	}

	public void setArticleList(List<OrderedArticle> articleList) {
		this.articleList = articleList;
	}
	
	public String getBarcodeString() {
		return barcodeString;
	}

	public void setBarcodeString(String barcodeString) {
		this.barcodeString = barcodeString;
	}

	public String getTotalOrderValue(){
		int totalValue = 0;
		totalValue = (int) (Math.random()*10000);
		/* For later use.
		for(OrderedArticle article : articleList){
			totalValue += article.getTotalPrice();
		}*/
		return String.format("%d,%2d€", (totalValue/100), (totalValue%100));
	}
}
