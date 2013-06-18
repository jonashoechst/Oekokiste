package de.bosshammersch_hof.oekokiste.model;

import java.sql.SQLException;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.misc.BaseDaoEnabled;
import com.j256.ormlite.table.DatabaseTable;

import de.bosshammersch_hof.oekokiste.ormlite.DatabaseManager;

@DatabaseTable
public class Cookware extends BaseDaoEnabled<Cookware, Integer> implements CreateOrUpdateable{

	@DatabaseField(generatedId = true)
	private int id;
	
	@DatabaseField
	private String name;
	
	@DatabaseField(foreign = true, foreignAutoCreate = true, foreignAutoRefresh = true)
	private Recipe recipe;
	
	public Cookware(){
		this.setDao(DatabaseManager.getHelper().getCookwareDao());
	}
	
	public void createOrUpdate() throws SQLException{
		DatabaseManager.getHelper().getCookwareDao().createOrUpdate(this);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public Recipe getRecipe() {
		return recipe;
	}

	public void setRecipe(Recipe recipe) {
		this.recipe = recipe;
	}
	
	@Override
	public boolean equals(Object o){
		Cookware c = (Cookware) o;
		return this.getRecipe().getId() == c.getRecipe().getId() && c.getName() == this.getName();
	}
	
}
