package de.bosshammersch_hof.oekokiste.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable
public class Article {

	@DatabaseField(id = true)
	private int id;
	
	@DatabaseField
	private String name;
	
	@DatabaseField
	private String description;
	
	@DatabaseField
	private String origin;
	
	@DatabaseField(foreign = true,foreignAutoCreate = true,foreignAutoRefresh = true)
	private ArticleGroup articleGroup;

	public Article(){
	}
	
	public Article(int id, String name, String description){
		this.id = id;
		this.name = name;
		this.description = description;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getId() {
		return id;
	}
	
	public String toString(){
		return "Article["+name+", "+id+"]";
	}

	public String getOrigin() {
		return origin;
	}

	public void setOrigin(String origin) {
		this.origin = origin;
	}
	
	public ArticleGroup getArticleGroup() {
		return articleGroup;
	}

	public void setArticleGroup(ArticleGroup articleGroup) {
		this.articleGroup = articleGroup;
	}

	
}
