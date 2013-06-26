 package de.bosshammersch_hof.oekokiste;

import java.sql.SQLException;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TextView;
import de.bosshammersch_hof.oekokiste.model.CookingArticle;
import de.bosshammersch_hof.oekokiste.model.Cookware;
import de.bosshammersch_hof.oekokiste.model.Recipe;
import de.bosshammersch_hof.oekokiste.ormlite.DatabaseManager;
import de.bosshammersch_hof.oekokiste.webiste.ImageFromURL;

public class RecipeDetailActivity extends Activity implements RefreshableActivity, ImageUpdatable{
	
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
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		Constants.refreshableActivity = this;
		setContentView(R.layout.activity_recipe_detail);
		refreshData();
	}

	/**
	 * Daten werden aktualisiert.
	 */
	@Override
	public void refreshData() {
		// TODO Auto-generated method stub
		int recipeId = getIntent().getIntExtra(Constants.keyRecipe, 0);
		try {
			recipe = DatabaseManager.getHelper().getRecipeDao().queryForId(recipeId);
			ImageFromURL imageUpdater = new ImageFromURL();
			imageUpdater.execute(recipe.getName());
			imageUpdater.updateClass = this;
			updateUi();
		} catch (SQLException e) {
			Log.e("RecipeDetail","Recipe was not found: ID not in ORMLite");
			e.printStackTrace();
		}

		
	}
	
	/**
	 * Die Zutaten werden in die UI geladen.
	 */
	private void fillIngeridentsDefault(){
		
		TableLayout ingredientTableLayout = (TableLayout) findViewById(R.id.ingredientTableLayout);
		
		final List<CookingArticle> cookingArticleList = recipe.getIngredientsList();
		
		for(int i = 0; i < cookingArticleList.size(); i++){
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
        			if(arg1.getAction() == MotionEvent.ACTION_UP){
        				arg0.requestFocus();
        				arg0.setBackgroundColor(Color.parseColor("#6FD4FC"));
        		    }
        		    return false;
        		}});
        	
        	ingredientTableLayout.addView(row);
		}
	}

	/**
	 * Aktualisiert die UI.
	 */
	public void updateUi() {
		// Fill the Recipe Activity
		setTitle(recipe.getName());
		
		TextView recipeNameTextView             = (TextView) findViewById(R.id.recipeNameTextView);
		recipeNameTextView.setText(recipe.getName());
		
		TextView recipeTimeTextView    			= (TextView) findViewById(R.id.recipeTimeTextView);
		recipeTimeTextView.setText((recipe.getCookingTimeInMin())+" Min.");
		
		setServingSpinner();
		
		// not yet used.
		
		TextView recipeLongDescriptionTextView     	= (TextView) findViewById(R.id.recipeLongDescriptionTextView);
		recipeLongDescriptionTextView.setText(recipe.getDescription());
		
		fillIngeridentsDefault();

		TextView recipeCookingUtensilsTextView 		= (TextView) findViewById(R.id.recipeCookingUtensilsTextView);
		String cookwareString = "";
		for(Cookware item : recipe.getCookware()){
			cookwareString += item.getName()+"\n";
		}
		recipeCookingUtensilsTextView.setText(cookwareString);
		
		TextView recipeInstructionsTextView     	= (TextView) findViewById(R.id.recipeInstructionsTextView);
		recipeInstructionsTextView.setText(recipe.getInstructions());

		ImageFromURL imageUpdater = new ImageFromURL();
		imageUpdater.execute(recipe.getImagerUrl());
		imageUpdater.updateClass = this;
	}
	
	/**
	 * Setzt den Spinner, in dem man die Personenanzahl auswählen kann.
	 */
	public void setServingSpinner(){
		Spinner spinner = (Spinner) findViewById(R.id.spinner);
		
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
		        R.array.servings, android.R.layout.simple_spinner_item);
		
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		
		spinner.setAdapter(adapter);
		spinner.setSelection(recipe.getServings()-1);
		spinner.setOnItemSelectedListener(new OnItemSelectedListener(){
			public void onItemSelected(AdapterView<?> parent, View view, 
		            int pos, long id) {
		    }

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
			}
		});
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
	
	public double[] calculateNewAmount(int newServings){
		double[] oldAmount = new double[recipe.getIngredientsList().size()];
		double[] newAmount = new double[oldAmount.length];
		for(int i = 0; i < oldAmount.length; i++){
			oldAmount[i] = recipe.getIngredientsList().get(i).getAmount();
		}
		
		for(int i = 0; i < oldAmount.length; i++){
			double temp = oldAmount[i];
			temp = (temp/recipe.getServings())*newServings;
			newAmount[i] = temp;
		}
		return newAmount;
	}

	/**
	 * Aktualisiert das Bild.
	 * 
	 * @params Das zu aktualisierende Bild.
	 */
	@Override
	public void updateImage(Drawable d) {
		ImageView imageView = (ImageView) findViewById(R.id.recipeImageView);
		if( d != null) imageView.setImageDrawable(d);
	}
}
