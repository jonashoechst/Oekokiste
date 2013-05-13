/* Author: 		Niklas Rappel
 * Created on: 	11.05.2013
 * Description:	This class describes a recipe-type for our RecipeDetailActivity and our RecipeActivity.
 */

package de.bosshammersch_hof.oekokiste;

public class Recipe {

	String recipeName;
	String recipePictureDescription;
	String recipeShortDescription;
	String recipeLongDescription;
	double recipeDifficulty;
	int recipePopularity;
	int recipeNumberOfPerson;
	int recipeTimeInMinutes;
	int hitRate;
	
	
	
	//CONSTRUCTORS
	public Recipe(){
		this.recipeName 				= "exemplaryRecipeName";
		this.recipePictureDescription	= "exemplaryRecipePictureDescription";
		this.recipeShortDescription 	= "exemplaryRecipeShortDescription";
		this.recipeLongDescription 		= "exemplaryRecipeLongDescription";
		this.recipeDifficulty			= 1.0;
		this.recipePopularity			= 3;
		this.recipeNumberOfPerson       = 2;
		this.recipeTimeInMinutes        = 100;
		this.hitRate = 0;
	}
	
	public Recipe(String name, int hitRate){
		Recipe recipe = new Recipe();
		recipe.setRecipeName(name);
		recipe.setHitRate(hitRate);
		
	}
	
	public Recipe(String recipeName, String recipePictureDescription,
				  String recipeShortDescription, String recipeLongDescription,
				  double recipeDifficulty, int recipePopularity, int recipeNumberOfPerson,
				  int recipeTimeInMinutes) {
		super();
		this.recipeName 				= recipeName;
		this.recipePictureDescription 	= recipePictureDescription;
		this.recipeShortDescription 	= recipeShortDescription;
		this.recipeLongDescription	    = recipeLongDescription;
		this.recipeDifficulty 			= recipeDifficulty;
		this.recipePopularity 			= recipePopularity;
		this.recipeNumberOfPerson		= recipeNumberOfPerson;
		this.recipeTimeInMinutes		= recipeTimeInMinutes;
	}


	
	


	//CREATE GETTER/SETTER
	public String getRecipeName(){
		return recipeName;
	}
	
	public String getRecipePictureDescription(){
		return recipePictureDescription;
	}
	
	public String getRecipeShortDescription(){
		return recipeShortDescription;
	}
	
	public String getRecipeLongDescription(){
		return recipeLongDescription;
	}
	
	public double getRecipeDifficulty(){
		return recipeDifficulty;
	}

	public int getRecipePopularity() {
		return recipePopularity;
	}
	
	public int getRecipeNumberOfPerson() {
		return recipeNumberOfPerson;
	}
	
	public int getRecipeTimeInMinutes() {
		return recipeTimeInMinutes;
	}

	public void setRecipePopularity(int recipePopularity) {
		this.recipePopularity = recipePopularity;
	}

	public void setRecipeName(String recipeName) {
		this.recipeName = recipeName;
	}

	public void setRecipePictureDescription(String recipePictureDescription) {
		this.recipePictureDescription = recipePictureDescription;
	}

	public void setRecipeShortDescription(String recipeShortDescription) {
		this.recipeShortDescription = recipeShortDescription;
	}

	public void setRecipeLongDescription(String recipeLongDescription) {
		this.recipeLongDescription = recipeLongDescription;
	}

	public void setRecipeDifficulty(double recipeDifficulty) {
		this.recipeDifficulty = recipeDifficulty;
	}
	
	public void setRecipeNumberOfPerson(int recipeNumberOfPerson) {
		this.recipeNumberOfPerson = recipeNumberOfPerson;
	}

	public void setRecipeTimeInMinutes(int recipeTimeInMinutes) {
		this.recipeTimeInMinutes = recipeTimeInMinutes;
	}
	
	public int getHitRate(){
		return this.hitRate;
	}
	
	public void setHitRate(int hitRate){
		this.hitRate = hitRate;
	}
}
