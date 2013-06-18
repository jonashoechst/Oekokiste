package de.bosshammersch_hof.oekokiste.model;

import java.sql.SQLException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.misc.BaseDaoEnabled;
import com.j256.ormlite.table.DatabaseTable;

import de.bosshammersch_hof.oekokiste.ormlite.DatabaseManager;

@DatabaseTable
public class ArticleGroup extends BaseDaoEnabled<ArticleGroup, String> implements CreateOrUpdateable{

	@DatabaseField(id = true)
	private String name;
	
	@ForeignCollectionField(eager = false)
	private Collection<Article> articles;

	public ArticleGroup(){
		this.setDao(DatabaseManager.getHelper().getArticleGroupDao());
		this.articles = new Vector<Article>();
	}
	
	public void createOrUpdate() throws SQLException{
		DatabaseManager.getHelper().getArticleGroupDao().createOrUpdate(this);
	}

	public Collection<Article> getArticles() {
		return articles;
	}
	
	public List<Article> getArticleList(){
		List<Article> article = new LinkedList<Article>();
		for(Article a : articles){
			article.add(a);
		}
		return article;
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
	
}
