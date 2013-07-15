package de.bosshammersch_hof.oekokiste.model;

import java.sql.SQLException;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.misc.BaseDaoEnabled;

import de.bosshammersch_hof.oekokiste.ormlite.DatabaseManager;

public class Recipe extends BaseDaoEnabled<Recipe, Integer> implements CreateOrUpdateable{

	public static class RecipeWithHits implements Comparable<RecipeWithHits>{
		public int hits;
		public int recipeId;
		public RecipeWithHits(int recipeId){
			this.recipeId = recipeId;
		}
		@Override
		public int compareTo(RecipeWithHits r2) {
			return r2.hits-this.hits;
		}
		
		public static int[] getRecipeIdArray(List<RecipeWithHits> list){
			int[] recipeIds = new int[list.size()];
			for(int i = 0; i < list.size(); i++){
				recipeIds[i] = list.get(i).recipeId;
			}
			return recipeIds;
		}
		
		public static int[] getHitsArray(List<RecipeWithHits> list){
			int[] hits = new int[list.size()];
			for(int i = 0; i < list.size(); i++){
				hits[i] = list.get(i).hits;
			}
			return hits;
		}
		
	}
	
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
	
	@DatabaseField
	private String imagerUrl;
	
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
	
	public void setCookware(Collection<Cookware> ca){
		this.cookware = ca;
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
	
	public void setIngredients(Collection<CookingArticle> ingrColl){
		this.ingredients = ingrColl;
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

	public String getImagerUrl() {
		return imagerUrl;
	}

	public void setImagerUrl(String imagerUrl) {
		this.imagerUrl = imagerUrl;
	}
	
	public static List<RecipeWithHits> findRecipesByArticleGroups(List<ArticleGroup> articleGroups, boolean onlyMainIngrediants) throws SQLException{
		
		List<RecipeWithHits> recipesWithHits = new LinkedList<RecipeWithHits>();
		
		List<Recipe> recipeList = DatabaseManager.getHelper().getRecipeDao().queryForAll();
		for(Recipe r : recipeList){
			RecipeWithHits rwh = new RecipeWithHits(r.getId());
			recipesWithHits.add(rwh);
		}
		
		for(ArticleGroup a : articleGroups){
			if(a == null) continue;
			for(RecipeWithHits r : recipesWithHits){
				if (r == null) continue;
				for(CookingArticle ca : DatabaseManager.getHelper().getRecipeDao().queryForId(r.recipeId).getIngredients()){
					if (ca == null) continue;
					try {
						if(a.getName().equals(ca.getArticleGroup().getName())){
							if(!onlyMainIngrediants || ca.isPrimaryIngredient()){
								r.hits++;
							}
						}
					} catch (NullPointerException e){
						continue;
					}
				}
			}
		}
		
		Collections.sort(recipesWithHits);
		
		int zeroIndex;
		for(zeroIndex = 0; recipesWithHits.get(zeroIndex).hits > 0; zeroIndex++);
		
		return recipesWithHits.subList(0, zeroIndex);
	}
	
}