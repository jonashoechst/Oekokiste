package de.bosshammersch_hof.oekokiste.model;

import java.sql.SQLException;
import java.util.Collection;
import java.util.Vector;

import com.j256.ormlite.misc.BaseDaoEnabled;
import com.j256.ormlite.table.DatabaseTable;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;

import de.bosshammersch_hof.oekokiste.ormlite.DatabaseManager;

@DatabaseTable
public class Category extends BaseDaoEnabled<Category, String>{

	@DatabaseField(id = true)
	private String name;
	
	@ForeignCollectionField(eager = false)
	private Collection<Article> articles;
	
	public Category(){
		this.setDao(DatabaseManager.getHelper().getCategoryDao());
		articles = new Vector<Article>();
	}
	
	public void createOrUpdate() throws SQLException{
		DatabaseManager.getHelper().getCategoryDao().createOrUpdate(this);
	}
	
	public void addArticle(Article a){
		a.setCategory(this);
		articles.add(a);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Collection<Article> getArticles() {
		return articles;
	}
	
}
