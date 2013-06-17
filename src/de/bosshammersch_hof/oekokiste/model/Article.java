package de.bosshammersch_hof.oekokiste.model;

import java.sql.SQLException;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.misc.BaseDaoEnabled;
import com.j256.ormlite.table.DatabaseTable;

import de.bosshammersch_hof.oekokiste.ormlite.DatabaseManager;

@DatabaseTable
public class Article  extends BaseDaoEnabled<Article, Integer>{

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
	
	@DatabaseField(foreign = true,foreignAutoCreate = true,foreignAutoRefresh = true)
	private Category category;

	public Article(){
		this.setDao(DatabaseManager.getHelper().getArticleDao());
	}
	
	public void createOrUpdate() throws SQLException{
		this.getDao().createOrUpdate(this);
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
	
	public void setId(int id) {
		this.id = id;
	}
	
	@Override
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

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	
	
}
