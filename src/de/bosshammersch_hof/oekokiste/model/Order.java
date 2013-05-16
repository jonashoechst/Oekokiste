package de.bosshammersch_hof.oekokiste.model;

import java.util.Date;
import java.util.List;

public class Order {

	private int id;
	
	private Date date;
	
	private String name;
	
	private List<OrderedArticle> articleList;
	
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
}
