package de.bosshammersch_hof.oekokiste.model;

import java.sql.SQLException;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.misc.BaseDaoEnabled;
import com.j256.ormlite.table.DatabaseTable;

import de.bosshammersch_hof.oekokiste.ormlite.DatabaseManager;

@DatabaseTable
public class Order extends BaseDaoEnabled<Order, Integer>{
	
	@DatabaseField(id = true)
	private int id;
	
	@DatabaseField
	private Date date;
	
	@DatabaseField
	private String name;
	
	@DatabaseField(foreign = true,foreignAutoCreate = true,foreignAutoRefresh = true)
	private User user;
	
	@ForeignCollectionField(eager = false)
	private Collection<OrderedArticle> articleCollection;
	
	@ForeignCollectionField(eager = false)
	private Collection<Barcode> barcodeCollection;
	
	public Order(){
		this.setDao(DatabaseManager.getHelper().getOrderDao());
		articleCollection = new Vector<OrderedArticle>();
		barcodeCollection = new Vector<Barcode>();
	}
	
	public void createOrUpdate() throws SQLException{
		DatabaseManager.getHelper().getOrderDao().createOrUpdate(this);
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

	public Collection<OrderedArticle> getArticleCollection() {
		return articleCollection;
	}
	
	public List<OrderedArticle> getArticleList(){
		List<OrderedArticle> articleList = new LinkedList<OrderedArticle>();
		for(OrderedArticle oa : articleCollection)
			articleList.add(oa);
		return articleList;
	}
/*
	public void setArticleCollection(Collection<OrderedArticle> articleCollection) {
		this.articleCollection = articleCollection;
	}*/
	
	public Collection<Barcode> getBarcodeCollection() {
		return barcodeCollection;
	}
/*
	public void setBarcodeList(Collection<Barcode> barcodeList) {
		this.barcodeList = barcodeList;
	}*/

	public void addBarcode(Barcode barcode) {
		barcode.setOrder(this);
		this.barcodeCollection.add(barcode);
	}

	public void addOrderedArticle(OrderedArticle oa) {
		oa.setOrder(this);
		this.articleCollection.add(oa);
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
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
