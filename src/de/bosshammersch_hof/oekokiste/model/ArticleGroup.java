package de.bosshammersch_hof.oekokiste.model;

import java.sql.SQLException;
import java.util.Collection;
import java.util.Vector;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.misc.BaseDaoEnabled;
import com.j256.ormlite.table.DatabaseTable;

import de.bosshammersch_hof.oekokiste.ormlite.DatabaseManager;

@DatabaseTable
public class ArticleGroup extends BaseDaoEnabled<ArticleGroup, String>{

	@DatabaseField(id = true)
	private String name;
	
	@DatabaseField(foreign = true,foreignAutoCreate = true,foreignAutoRefresh = true)
	private Category category;
	
	@ForeignCollectionField(eager = false)
	private Collection<Article> articles;

	public ArticleGroup(){
		this.setDao(DatabaseManager.getHelper().getArticleGroupDao());
		this.articles = new Vector<Article>();
	}
	
	public ArticleGroup(String name){
		this();
		this.name = name;
	}
	
	@Override
	public int create() throws SQLException{
		for(Article a : articles)
			a.create();
		return super.create();
	}

	public Collection<Article> getArticles() {
		return articles;
	}

	public void addArticle(Article article) {
		article.setArticleGroup(this);
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
