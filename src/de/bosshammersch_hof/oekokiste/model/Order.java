package de.bosshammersch_hof.oekokiste.model;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable
public class Order {
	
	@DatabaseField(id = true)
	private int id;
	
	@DatabaseField
	private Date date;
	
	@DatabaseField
	private String name;
	
	@ForeignCollectionField(eager = false)
	private List<OrderedArticle> articleList;
	
	@ForeignCollectionField(eager = false)
	private List<Barcode> barcodeList;
	
	public Order(){
		articleList = new LinkedList<OrderedArticle>();
		barcodeList = new LinkedList<Barcode>();
	}
	
	public Order(int id, Date date, String name) {
		this();
		this.id = id;
		this.date = date;
		this.name = name;
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
	
	
	
	public List<Barcode> getBarcodeList() {
		return barcodeList;
	}

	public void setBarcodeList(List<Barcode> barcodeList) {
		this.barcodeList = barcodeList;
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
