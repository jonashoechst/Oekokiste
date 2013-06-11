package de.bosshammersch_hof.oekokiste.model;

import java.util.LinkedList;
import java.util.List;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable
public class ArticleGroup {

	@DatabaseField(id = true)
	private String name;
	
	@DatabaseField(foreign = true,foreignAutoCreate = true,foreignAutoRefresh = true)
	private Category category;
	
	@ForeignCollectionField(eager = false)
	private List<Article> articles;

	public ArticleGroup(){
	}
	
	public ArticleGroup(String name){
		this.name = name;
		this.articles = new LinkedList<Article>();
	}

	public List<Article> getArticles() {
		return articles;
	}

	public void addArticle(Article article) {
		this.articles.add(article);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}
	
	
	
}
