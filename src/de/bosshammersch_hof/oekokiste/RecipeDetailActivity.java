package de.bosshammersch_hof.oekokiste;

import java.util.LinkedList;

import de.bosshammersch_hof.oekokiste.model.Article;
import de.bosshammersch_hof.oekokiste.model.CookingArticle;
import de.bosshammersch_hof.oekokiste.model.Recipe;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.MenuItem;
import android.widget.TextView;

public class RecipeDetailActivity extends Activity {
	
	/** 
	 *   creats the detail-view of recipe
	 *   @param Bundle saved Instance State
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_recipe_detail);
		
		getActionBar().setHomeButtonEnabled(true);
		
		Recipe recipe = getDummyRecipe();
		
		// Fill the Recipe Activity
		setTitle(recipe.getName());
		
		TextView recipeNameTextView             = (TextView) findViewById(R.id.recipeNameTextView);
		recipeNameTextView.setText(recipe.getName());
		
		TextView recipeTimeTextView    			= (TextView) findViewById(R.id.recipeTimeTextView);
		recipeTimeTextView.setText((recipe.getCookingTimeInMin()+recipe.getWorkingTimeInMin())+" Min.");
		
		TextView recipeNumberOfPersonTextView   = (TextView) findViewById(R.id.recipeNumberOfPersonTextView);
		recipeNumberOfPersonTextView.setText(recipe.getServings()+" Personen");
		
		// not yet used.
		// ImageView recipeImageView              	= (ImageView) findViewById(R.id.recipeImageView);
		
		TextView recipeLongDescriptionTextView     	= (TextView) findViewById(R.id.recipeLongDescriptionTextView);
		recipeLongDescriptionTextView.setText(recipe.getDescription());

		TextView recipeCookingUtensilsTextView 		= (TextView) findViewById(R.id.recipeCookingUtensilsTextView);
		String cookwareString = "";
		for(String item : recipe.getCookware()){
			cookwareString += item+"\n";
		}
		recipeCookingUtensilsTextView.setText(cookwareString);
		
		TextView recipeIngredientsTextView 		= (TextView) findViewById(R.id.recipeIngredientsTextView);
		String ingredientsString = "";
		for(CookingArticle item : recipe.getIngredients()){
			if(item.getAmount() != 1) ingredientsString += item.getAmount()+" ";
			ingredientsString += item.getArticle().getName()+"\n";
		}
		recipeIngredientsTextView.setText(ingredientsString);
		
		TextView recipeInstructionsTextView     	= (TextView) findViewById(R.id.recipeInstructionsTextView);
		recipeInstructionsTextView.setText(recipe.getInstructions());
		
	}
	
	/**
	 *   if the app icon in action bar is clicked => go home
	 *   else the super constructor of the function is called
	 *   @param MenuItem which was selected
	 *   @return boolean 
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	        case android.R.id.home:
	            Intent intent = new Intent(this, MainActivity.class);
	            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
	            startActivity(intent);
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);  
	    }
	}
	
	/** 
	 *   supplies dummy data for testing the app
	 *   @return Recipe with dummy data
	 */
	private Recipe getDummyRecipe(){
		
		String description = "Flammkuchen, Flammenkuchen oder Flammekueche (frz. Tarte flambée) ist ein herzhafter Kuchen aus dem Elsass (frz. Alsace) in Frankreich. Ursprünglich war Flammkuchen ein bäuerliches Essen, mit dem am Backtag getestet wurde, ob der dörfliche Backofen heiß genug war, bevor die Brote für die Woche gebacken wurden. Da der Backtag im dörflichen Arbeitsrhythmus ein Gemeinschaftsereignis war und der Backofen im Backhaus auch vor Feiertagen angeheizt wurde, entwickelte sich der Flammkuchen mit der Zeit zum Festtagsessen. Als Teig benutzte man Brotteig.";
		String instruction = "Den Brotteig in 45 Stücke zerteilen, mit einem Küchentuch abdecken und die Teigstücke 30 Minuten ruhen lassen. Den Quark (mit oder ohne Crème fraîche, siehe Varianten) mit Salz und Pfeffer verrühren. Zwiebel in dünne Ringe schneiden oder hobeln. Den Frühstücksspeck in Streifen schneiden. Den Teig auf einer bemehlten Arbeitsfläche so dünn wie möglich auswellen und mit dem Quark gleichmäßig dünn bestreichen. Mit Frühstücksspeck, Zwiebelringen (und Käse, siehe Varianten) belegen.";
		
		LinkedList<String> cookware = new LinkedList<String>();
		cookware.add("Nudelholz");
		cookware.add("Backblech");
		cookware.add("2 Bögen Backpapier");
		cookware.add("Teigschaber");
		cookware.add("Rührschüssel");
		cookware.add("Messer");
		
		LinkedList<CookingArticle> articleList = new LinkedList<CookingArticle>();

		Article article1 = new Article(1, "Mehl", "");
		articleList.add(new CookingArticle(article1, 200, "g", true));
		Article article2 = new Article(2, "Schmand", "");
		articleList.add(new CookingArticle(article2, 1, "Becher", false));
		Article article3 = new Article(3, "Speck", "");
		articleList.add(new CookingArticle(article3, 250, "g", true));
		Article article4 = new Article(4, "Gouda", "");
		articleList.add(new CookingArticle(article4, 1, "Block", true));
		Article article5 = new Article(5, "Zwiebeln", "");
		articleList.add(new CookingArticle(article5, 3, "Stück", true));
		
		Recipe recipeFlammkuchen = new Recipe("Flammkuchen", 
											  description, 
											  instruction,
											  cookware,//Cookware
											  null,//Image
											  3,//Difficulty
											  45,//WorkingTime
											  30,//CookingTime
											  5,//servings
											  articleList, //Ingediants
											  3);//hitrate)
		return recipeFlammkuchen;
		
		
	}
}
