package de.bosshammersch_hof.oekokiste.model;

import java.sql.SQLException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.misc.BaseDaoEnabled;

import de.bosshammersch_hof.oekokiste.ormlite.DatabaseManager;

public class Recipe extends BaseDaoEnabled<Recipe, Integer> implements CreateOrUpdateable{

	@DatabaseField(id = true)
	private int id;
	
	@DatabaseField
	private String name;
	
	@DatabaseField
	private String description;
	
	@DatabaseField
	private String instructions;
	
	@ForeignCollectionField(eager = false)
	private Collection<Cookware> cookware;
	
	@DatabaseField
	private int difficulty;
	
	@DatabaseField
	private int cookingTimeInMin;
	
	@DatabaseField
	private int servings;
	
	@ForeignCollectionField(eager = false)
	private Collection<CookingArticle> ingredients;
	
	public Recipe(){
		this.setDao(DatabaseManager.getHelper().getRecipeDao());
		this.ingredients = new Vector<CookingArticle>();
		this.cookware = new Vector<Cookware>();
	}
	
	public void createOrUpdate() throws SQLException{
		DatabaseManager.getHelper().getRecipeDao().createOrUpdate(this);
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
	
	public String getInstructions() {
		return instructions;
	}
	
	public void setInstructions(String instructions) {
		this.instructions = instructions;
	}
	
	public Collection<Cookware> getCookware() {
		return cookware;
	}
	
	public void addCookware(Cookware inCookware) {
		inCookware.setRecipe(this);
		this.cookware.add(inCookware);
	}
	
	public int getDifficulty() {
		return difficulty;
	}
	
	public void setDifficulty(int difficulty) {
		this.difficulty = difficulty;
	}
	
	public int getCookingTimeInMin() {
		return cookingTimeInMin;
	}
	
	public void setCookingTimeInMin(int cookingTimeInMin) {
		this.cookingTimeInMin = cookingTimeInMin;
	}
	
	public int getServings() {
		return servings;
	}
	
	public void setServings(int servings) {
		this.servings = servings;
	}
	
	public Collection<CookingArticle> getIngredients() {
		return ingredients;
	}
	
	public List<CookingArticle> getIngredientsList(){
		List<CookingArticle> caList = new LinkedList<CookingArticle>();
		for(CookingArticle ca : ingredients){
			caList.add(ca);
		}
		return caList;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	@Override
	public boolean equals(Object o) {
		return this.getId() == ((Recipe) o).getId();
	}
	
	@Override
	public int hashCode(){
		return id;
	}
	
}