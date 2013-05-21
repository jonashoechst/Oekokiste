package de.bosshammersch_hof.oekokiste.model;

import java.util.List;

import android.graphics.Bitmap;

public class Recipe {

	private String name;
	private String description;
	private String instructions;
	
	private List<String> cookware;
	
	private Bitmap image;
	
	private int difficulty;
	private int workingTimeInMin;
	private int cookingTimeInMin;
	private int servings;
	
	private List<CookingArticle> ingredients;
	
	private int hitPoints;
	
	public Recipe(String name, String description, String instructions,
			List<String> cookware, Bitmap image, int difficulty,
			int workingTimeInMin, int cookingTimeInMin, int servings,
			List<CookingArticle> ingredients, int hitPoints) {
		super();
		this.name = name;
		this.description = description;
		this.instructions = instructions;
		this.cookware = cookware;
		this.image = image;
		this.difficulty = difficulty;
		this.workingTimeInMin = workingTimeInMin;
		this.cookingTimeInMin = cookingTimeInMin;
		this.servings = servings;
		this.ingredients = ingredients;
		this.hitPoints = hitPoints;
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
	
	public List<String> getCookware() {
		return cookware;
	}
	
	public void setCookware(List<String> cookware) {
		this.cookware = cookware;
	}
	
	public Bitmap getImage() {
		return image;
	}
	
	public void setImage(Bitmap image) {
		this.image = image;
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
	
	public List<CookingArticle> getIngredients() {
		return ingredients;
	}
	
	public void setIngredients(List<CookingArticle> ingredients) {
		this.ingredients = ingredients;
	}
	
	public int getHitPoints() {
		return hitPoints;
	}
	
	public void setHitPoints(int hitPoints) {
		this.hitPoints = hitPoints;
	}	
}