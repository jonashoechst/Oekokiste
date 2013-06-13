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
	private Collection<ArticleGroup> articleGroups;
	
	public Category(){
		this.setDao(DatabaseManager.getHelper().getCategoryDao());
		articleGroups = new Vector<ArticleGroup>();
	}
	
	public Category(String name){
		this();
		this.name = name;
	}
	
	@Override
	public int create() throws SQLException{
		for(ArticleGroup ag : articleGroups)
			ag.create();
		return super.create();
	}
	
	public void addArticleGroup(ArticleGroup ag){
		ag.setCategory(this);
		articleGroups.add(ag);
	}

	public String getName() {
		return name;
	}

	public Collection<ArticleGroup> getArticleGroups() {
		return articleGroups;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setArticleGroups(Collection<ArticleGroup> articleGroups) {
		this.articleGroups = articleGroups;
	}
	
	
	
}
