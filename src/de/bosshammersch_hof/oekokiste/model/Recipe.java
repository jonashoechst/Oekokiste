package de.bosshammersch_hof.oekokiste.model;

import java.util.Collection;
import java.util.Vector;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.misc.BaseDaoEnabled;

import de.bosshammersch_hof.oekokiste.ormlite.DatabaseManager;

import android.graphics.Bitmap;

public class Recipe extends BaseDaoEnabled<Recipe, Integer>{

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
	private int workingTimeInMin;
	@DatabaseField
	private int cookingTimeInMin;
	@DatabaseField
	private int servings;
	
	@ForeignCollectionField(eager = false)
	private Collection<CookingArticle> ingredients;
	
	public Recipe(){
		this.setDao(DatabaseManager.getHelper().getRecipeDao());
		this.ingredients = (Collection<CookingArticle>) new Vector<CookingArticle>();
		this.cookware = (Collection<Cookware>) new Vector<Cookware>();
	}
	
	public Recipe(String name, String description, String instructions,
			Collection<Cookware> cookware, Bitmap image, int difficulty,
			int workingTimeInMin, int cookingTimeInMin, int servings,
			Collection<CookingArticle> ingredients, int hitPoints) {
		this();
		this.name = name;
		this.description = description;
		this.instructions = instructions;
		this.cookware = cookware;
		//this.image = image;
		this.difficulty = difficulty;
		this.workingTimeInMin = workingTimeInMin;
		this.cookingTimeInMin = cookingTimeInMin;
		this.servings = servings;
		this.ingredients = ingredients;
		//this.hitPoints = hitPoints;
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
	
	public int getWorkingTimeInMin() {
		return workingTimeInMin;
	}
	
	public void setWorkingTimeInMin(int workingTimeInMin) {
		this.workingTimeInMin = workingTimeInMin;
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
	/*
	public void setIngredients(Collection<CookingArticle> ingredients){
		this.ingredients = ingredients;
	}*/
	
	public void addIngredient(CookingArticle article){
		article.setRecipe(this);
		ingredients.add(article);
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
}