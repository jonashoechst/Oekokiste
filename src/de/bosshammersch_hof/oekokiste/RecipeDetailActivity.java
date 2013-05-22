package de.bosshammersch_hof.oekokiste;

import de.bosshammersch_hof.oekokiste.model.Article;
import de.bosshammersch_hof.oekokiste.model.Recipe;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.MenuItem;
import android.widget.ImageView;
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
		TextView recipeTimeInMinutesTextView    = (TextView) findViewById(R.id.recipeTimeInMinutesTextView);
		TextView recipeNumberOfPersonTextView   = (TextView) findViewById(R.id.recipeNumberOfPersonTextView);
		
		ImageView articleImageView              = (ImageView) findViewById(R.id.articleImageView);
		
		TextView recipeLongDescriptionTextView     = (TextView) findViewById(R.id.recipeLongDescription);
		TextView recipeInstructionsTextView        = (TextView) findViewById(R.id.recipeInstructions);
		TextView recipeExampleInstructionsTextView = (TextView) findViewById(R.id.recipeExampleInstructions);
		TextView recipeCookingUtensils 			   = (TextView) findViewById(R.id.recipeCookingUtensils);
		TextView recipeExampleCookingUtensils      = (TextView) findViewById(R.id.recipeExampleCookingUtensils);
				
		recipeLongDescriptionTextView.setText(recipe.getDescription());
		recipeInstructionsTextView.setText(recipe.getInstructions());
		recipeExampleInstructionsTextView.setText(recipe.getInstructions());
		
		
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
		
		String description = "Flammkuchen, Flammenkuchen oder Flammekueche (frz. Tarte flamb�e) ist ein herzhafter Kuchen aus dem Elsass (frz. Alsace) in Frankreich. Urspr�nglich war Flammkuchen ein b�uerliches Essen, mit dem am Backtag getestet wurde, ob der d�rfliche Backofen hei� genug war, bevor die Brote f�r die Woche gebacken wurden. Da der Backtag im d�rflichen Arbeitsrhythmus ein Gemeinschaftsereignis war und der Backofen im Backhaus auch vor Feiertagen angeheizt wurde, entwickelte sich der Flammkuchen mit der Zeit zum Festtagsessen. Als Teig benutzte man Brotteig.";
		String instruction = "Den Brotteig in 4�5 St�cke zerteilen, mit einem K�chentuch abdecken und die Teigst�cke 30 Minuten ruhen lassen. Den Quark (mit oder ohne Cr�me fra�che, siehe Varianten) mit Salz und Pfeffer verr�hren. Zwiebel in d�nne Ringe schneiden oder hobeln. Den Fr�hst�cksspeck in Streifen schneiden. Den Teig auf einer bemehlten Arbeitsfl�che so d�nn wie m�glich auswellen und mit dem Quark gleichm��ig d�nn bestreichen. Mit Fr�hst�cksspeck, Zwiebelringen (und K�se, siehe Varianten) belegen.";
		
		Recipe recipeFlammkuchen = new Recipe("Flammkuchen", 
											  description, 
											  instruction,
											  null,//Cookware
											  null,//Image
											  3,//Difficulty
											  45,//WorkingTime
											  30,//CookingTime
											  0,//servings
											  null, //Incrediants
											  3);//hitrate)
		return recipeFlammkuchen;
		
		
	}
}
