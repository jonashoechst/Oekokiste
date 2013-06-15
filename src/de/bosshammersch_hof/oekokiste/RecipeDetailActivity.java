package de.bosshammersch_hof.oekokiste;

import java.util.LinkedList;
import java.util.List;

import de.bosshammersch_hof.oekokiste.model.*;
import de.bosshammersch_hof.oekokiste.ormlite.DatabaseManager;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class RecipeDetailActivity extends Activity implements UpdatableActivity{
	
	Recipe recipe;
	
	/** 
	 *   creats the detail-view of recipe
	 *   @param Bundle saved Instance State
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_recipe_detail);
		getActionBar().setHomeButtonEnabled(true);
		
		setupRecipe();
		
		updateUi();
		
	}
	
	private void fillIngeridends(){
		ListView cookingArticleListView = (ListView) findViewById(R.id.articleListView);
		
		final List<CookingArticle> cookingArticleList = recipe.collectionToList(recipe.getIngredients());
		
		ListAdapter adapter = new ArrayAdapter<CookingArticle>(this, R.layout.listview_item_recipe_ingrediends, cookingArticleList){
			
			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
		       	 	View row = convertView;
		        
		        	if(row == null){
		            	    LayoutInflater inflater = ((Activity) this.getContext()).getLayoutInflater();
		        	    row = inflater.inflate(R.layout.listview_item_recipe_ingrediends, parent, false);
		        	}
 
		        	TextView nameTextView = (TextView) row.findViewById(R.id.ingrediendName);
		        	TextView amountTextView = (TextView) row.findViewById(R.id.ingredientAmount);
		        	TextView unitTextView = (TextView) row.findViewById(R.id.ingredientUnit);
		        
		        	amountTextView.setText(cookingArticleList.get(position).getAmount()+"");
		        	unitTextView.setText(cookingArticleList.get(position).getAmountType());
		        	nameTextView.setText(cookingArticleList.get(position).getArticle().getName());
		        
		        	return row;
			}
		};
		
		cookingArticleListView.setAdapter(adapter);
	}

	public void updateUi() {
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
				
		fillIngeridends();

		TextView recipeCookingUtensilsTextView 		= (TextView) findViewById(R.id.recipeCookingUtensilsTextView);
		String cookwareString = "";
		for(Cookware item : recipe.getCookware()){
			cookwareString += item.getName()+"\n";
		}
		recipeCookingUtensilsTextView.setText(cookwareString);
		
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
	
	private void setupRecipe() {
		// get User Id and matching User 
		int recipeId = getIntent().getIntExtra(Constants.keyRecipe, 1);
		Log.i(OrderActivity.class.getName(), "userId found: "+recipeId);
		recipe = DatabaseManager.getRecipe(recipeId);
	}
	
	/** 
	 *   supplies dummy data for testing the app
	 *   @return Recipe with dummy data
	 */
	/*private Recipe getDummyRecipe(){
		
		String description = "Flammkuchen, Flammenkuchen oder Flammekueche (frz. Tarte flambée) ist ein herzhafter Kuchen aus dem Elsass (frz. Alsace) in Frankreich. Ursprünglich war Flammkuchen ein bäuerliches Essen, mit dem am Backtag getestet wurde, ob der dörfliche Backofen heiß genug war, bevor die Brote für die Woche gebacken wurden. Da der Backtag im dörflichen Arbeitsrhythmus ein Gemeinschaftsereignis war und der Backofen im Backhaus auch vor Feiertagen angeheizt wurde, entwickelte sich der Flammkuchen mit der Zeit zum Festtagsessen. Als Teig benutzte man Brotteig.";
		String instruction = "Den Brotteig in 45 Stücke zerteilen, mit einem Küchentuch abdecken und die Teigstücke 30 Minuten ruhen lassen. Den Quark (mit oder ohne Crème fraîche, siehe Varianten) mit Salz und Pfeffer verrühren. Zwiebel in dünne Ringe schneiden oder hobeln. Den Frühstücksspeck in Streifen schneiden. Den Teig auf einer bemehlten Arbeitsfläche so dünn wie möglich auswellen und mit dem Quark gleichmäßig dünn bestreichen. Mit Frühstücksspeck, Zwiebelringen (und Käse, siehe Varianten) belegen.";
		
		LinkedList<Cookware> cookware = new LinkedList<Cookware>();
		cookware.add(new Cookware("Nudelholz"));
		cookware.add(new Cookware("Backblech"));
		cookware.add(new Cookware("2 Bögen Backpapier"));
		cookware.add(new Cookware("Teigschaber"));
		cookware.add(new Cookware("Rührschüssel"));
		cookware.add(new Cookware("Messer"));
		
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
		
		
	}*/
}
