 package de.bosshammersch_hof.oekokiste;

import java.sql.SQLException;
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
		if(recipe == null)
			recipe = new Recipe();
				
		updateUi();
		
	}
	
	private void fillIngeridents(){
		ListView ingredientListView = (ListView) findViewById(R.id.ingredientListView);
		
		final List<CookingArticle> cookingArticleList = recipe.getIngredientsList();
		
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
		        	//nameTextView.setText(cookingArticleList.get(position).getArticle().getName());
		        
		        	return row;
			}
		};
		
		ingredientListView.setAdapter(adapter);
	}

	public void updateUi() {
		// Fill the Recipe Activity
		setTitle(recipe.getName());
		
		TextView recipeNameTextView             = (TextView) findViewById(R.id.recipeNameTextView);
		recipeNameTextView.setText(recipe.getName());
		
		TextView recipeTimeTextView    			= (TextView) findViewById(R.id.recipeTimeTextView);
		recipeTimeTextView.setText((recipe.getCookingTimeInMin())+" Min.");
		
		TextView recipeNumberOfPersonTextView   = (TextView) findViewById(R.id.recipeNumberOfPersonTextView);
		recipeNumberOfPersonTextView.setText(recipe.getServings()+" Personen");
		
		// not yet used.
		// ImageView recipeImageView              	= (ImageView) findViewById(R.id.recipeImageView);
		
		TextView recipeLongDescriptionTextView     	= (TextView) findViewById(R.id.recipeLongDescriptionTextView);
		recipeLongDescriptionTextView.setText(recipe.getDescription());
				
		fillIngeridents();

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
		int recipeId = getIntent().getIntExtra(Constants.keyRecipe, 0);
		if(recipeId == 0) Log.e("RecipeDetail","Recipe was not found.");
		else Log.i(OrderActivity.class.getName(), "recipeId found: "+recipeId);
		try {
			recipe = DatabaseManager.getHelper().getRecipeDao().queryForId(recipeId);
			Log.i("RecipeDetail", "Printing Ingredients:");
			for(CookingArticle ca : recipe.getIngredientsList()){
				Log.i("RecipeDetail", "Ingredient: "+ca.getArticleGroup().getName());
			}
			
			
		} catch (SQLException e) {
			Log.e("RecipeDetail","Recipe was not found: ID not in ORMLite");
			e.printStackTrace();
		}
		//recipe = DatabaseManager.getRecipe(recipeId);
	}
}
