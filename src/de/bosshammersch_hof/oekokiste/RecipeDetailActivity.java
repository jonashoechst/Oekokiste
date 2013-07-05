 package de.bosshammersch_hof.oekokiste;

import java.sql.SQLException;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
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
	
	TableLayout ingredientTableLayout;
	
	int servings;
	
	ImageFromURL imageUpdater;
	Drawable image = null;
	
	/** 
	 *   Creates the detail-view of recipe
	 *   @param Bundle saved Instance State
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_recipe_detail);
		getActionBar().setHomeButtonEnabled(true);	

		imageUpdater = new ImageFromURL();
		imageUpdater.updateClass = this;
	}
	
	/**
	 *  call the super constructor and call refreshData();
	 */
	@Override
	protected void onResume() {
		super.onResume();
		Constants.refreshableActivity = this;
		setContentView(R.layout.activity_recipe_detail);
		refreshData();
		Log.i("RecipeDetail", "onResume()");
	}

	/**
	 * Daten werden aktualisiert.
	 */
	@Override
	public void refreshData() {
		int recipeId = getIntent().getIntExtra(Constants.keyRecipeId, 0);
		try {
			recipe = DatabaseManager.getHelper().getRecipeDao().queryForId(recipeId);
		} catch (SQLException e) {
			recipe = null;
		}
		
		if(recipe == null){
			// Print an Error message
			AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(this);
			dlgAlert.setMessage("Die Ansicht konnte nicht geladen werden.");
			dlgAlert.setTitle("Ökokiste");
			dlgAlert.setPositiveButton("Zurück", 
				new DialogInterface.OnClickListener() {
		        	public void onClick(DialogInterface dialog, int which) {
		        		finish();
		        	}
				}
			);
			dlgAlert.setCancelable(true);
			dlgAlert.create().show();
		}
		
		servings = recipe.getServings();
		updateUi();
		
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
		
		// not yet used.
		
		TextView recipeLongDescriptionTextView     	= (TextView) findViewById(R.id.recipeLongDescriptionTextView);
		recipeLongDescriptionTextView.setText(recipe.getDescription());

		TextView recipeCookingUtensilsTextView 		= (TextView) findViewById(R.id.recipeCookingUtensilsTextView);
		String cookwareString = "";
		for(Cookware item : recipe.getCookware()){
			cookwareString += item.getName()+"\n";
		}
		recipeCookingUtensilsTextView.setText(cookwareString);
		
		TextView recipeInstructionsTextView     	= (TextView) findViewById(R.id.recipeInstructionsTextView);
		recipeInstructionsTextView.setText(recipe.getInstructions());

		// Download Image if:
			// 1. there is no image yet
			// 2. there is no imageUpdater running.
			// 3. there is a valid link
		if(		image == null && 
				imageUpdater.getStatus() != AsyncTask.Status.RUNNING && 
				recipe.getImagerUrl() != null && 
				!recipe.getImagerUrl().equals("") ){
			imageUpdater.execute(recipe.getImagerUrl());
		} else if (image != null)
			updateImage(image);
		
		fillIngeridents(servings);
		setServingSpinner();
	}
	
	/**
	 * Die Zutaten werden in die UI geladen.
	 */
	private void fillIngeridents(int servings){
		if(!(ingredientTableLayout == null)){
			ingredientTableLayout.removeAllViews();
		}
		ingredientTableLayout = (TableLayout) findViewById(R.id.ingredientTableLayout);
		final List<CookingArticle> cookingArticleList = recipe.getIngredientsList();
		
		for(int i = 0; i < cookingArticleList.size(); i++){
			LayoutInflater inflater = ((Activity) this).getLayoutInflater();
    	    View row = inflater.inflate(R.layout.listview_item_recipe_ingrediends, ingredientTableLayout, false);
    	    
    	    TextView nameTextView = (TextView) row.findViewById(R.id.ingrediendName);
        	TextView amountTextView = (TextView) row.findViewById(R.id.ingredientAmount);
        	TextView unitTextView = (TextView) row.findViewById(R.id.ingredientUnit);
        
        	amountTextView.setText(cookingArticleList.get(i).getAmountStringMul(((double) servings)/((double) recipe.getServings())));
        	unitTextView.setText(cookingArticleList.get(i).getAmountType());
        	nameTextView.setText(cookingArticleList.get(i).getArticleGroup().getName());
        	
        	row.setClickable(true);
        	
        	final int position = i;
        	
        	row.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                	Intent intent = new Intent(RecipeDetailActivity.this, FindArticleActivity.class);
                	intent.putExtra(Constants.keyArticleGroupName, cookingArticleList.get(position).getArticleGroup().getName());
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
	 * Setzt den Spinner, in dem man die Personenanzahl auswählen kann.
	 */
	public void setServingSpinner(){
		Spinner spinner = (Spinner) findViewById(R.id.spinner);
		
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
		        R.array.servings, android.R.layout.simple_spinner_item);
		
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		
		spinner.setAdapter(adapter);
		spinner.setSelection(servings-1);
		spinner.setOnItemSelectedListener(new OnItemSelectedListener(){
			public void onItemSelected(AdapterView<?> parent, View view, 
		            int pos, long id) {
				servings = pos+1;
				fillIngeridents(servings);
		    }

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				return;
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
	
	/**
	 * Aktualisiert das Bild.
	 * 
	 * @params Das zu aktualisierende Bild.
	 */
	@Override
	public void updateImage(Drawable d) {
		ImageView imageView = (ImageView) findViewById(R.id.recipeImageView);
		if( d != null) {
			imageView.setImageDrawable(d);
			image = d;
		}
	}
	
	/**
	 *  destroys the imageUpdater if the app is closed
	 */
	@Override
	protected void onDestroy(){
		super.onDestroy();
		imageUpdater.cancel(true);
	}
}
