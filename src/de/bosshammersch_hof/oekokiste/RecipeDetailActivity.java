 package de.bosshammersch_hof.oekokiste;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import de.bosshammersch_hof.oekokiste.model.*;
import de.bosshammersch_hof.oekokiste.ormlite.DatabaseManager;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.MotionEvent;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.PopupWindow;
import android.widget.AdapterView.OnItemClickListener;

public class RecipeDetailActivity extends Activity implements UpdatableActivity{
	
	Recipe recipe;
	
	private PopupWindow popUp;
	
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
		
		TableLayout ingredientTableLayout = (TableLayout) findViewById(R.id.ingredientTableLayout);
		
		final List<CookingArticle> cookingArticleList = recipe.getIngredientsList();
		
		for(int i = 0; i < cookingArticleList.size(); i++){
			CookingArticle ca = cookingArticleList.get(i);
			Log.i("CookingArticle", "Art.: "+ca.getArticleGroup().getName());
			LayoutInflater inflater = ((Activity) this).getLayoutInflater();
    	    View row = inflater.inflate(R.layout.listview_item_recipe_ingrediends, ingredientTableLayout, false);
    	    
    	    TextView nameTextView = (TextView) row.findViewById(R.id.ingrediendName);
        	TextView amountTextView = (TextView) row.findViewById(R.id.ingredientAmount);
        	TextView unitTextView = (TextView) row.findViewById(R.id.ingredientUnit);
        
        	amountTextView.setText(cookingArticleList.get(i).getAmount()+"");
        	unitTextView.setText(cookingArticleList.get(i).getAmountType());
        	nameTextView.setText(cookingArticleList.get(i).getArticleGroup().getName());
        	
        	row.setClickable(true);
        	
        	final int position = i;
        	
        	row.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                	Intent intent = new Intent(RecipeDetailActivity.this, FindArticleActivity.class);
                	intent.putExtra(Constants.keyArticleGroupId, cookingArticleList.get(position).getArticleGroup().getName());
    				startActivity(intent);
                }
            });
        	
        	row.setOnTouchListener(new OnTouchListener() {
        		public boolean onTouch(View arg0, MotionEvent arg1) {
        		    arg0.requestFocus();
        		    arg0.setBackgroundColor(Color.parseColor("#6FEEFC"));
        		    return false;
        		}});
        	
        	ingredientTableLayout.addView(row);
		}
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
			
		} catch (SQLException e) {
			Log.e("RecipeDetail","Recipe was not found: ID not in ORMLite");
			e.printStackTrace();
		}
	}
}
