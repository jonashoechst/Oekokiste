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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class RecipeActivity extends Activity implements RefreshableActivity{
	
	LinkedList<Recipe> recipeList;
	List<ArticleGroup> articleGroups;
	
	/** 
	 *   creats the hole list of recipe
	 *   @param Bundle saved Instance State
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_recipe);
		getActionBar().setHomeButtonEnabled(true);
		
		refreshData();
	}
	
	@Override
	public void refreshData() {

		String[] articleGroupIdArray = this.getIntent().getStringArrayExtra(Constants.keyArticleGroupNameArray);
		
		articleGroups = new LinkedList<ArticleGroup>();
		for(String articleGroupName : articleGroupIdArray){
			try {
				articleGroups.add(DatabaseManager.getHelper().getArticleGroupDao().queryForId(articleGroupName));
			} catch (java.sql.SQLException e) {
				e.printStackTrace();
			}
		}
		
		// Find Recipies only for one Articlegroup
		
		ArticleGroup ag = articleGroups.get(0);
		
		// Create a Query Example
		CookingArticle ca = new CookingArticle();
		ca.setArticleGroup(ag);
		
		List<CookingArticle> caList;
		
		// Try to look for Cooking Articles Matching the Example
		try {
			caList = DatabaseManager.getHelper().getCookingArticleDao().queryForMatching(ca);
		} catch (java.sql.SQLException e) {
			caList = new LinkedList<CookingArticle>();
			e.printStackTrace();
		}
		
		// Get Recipes from the Cooking Articles.
		recipeList = new LinkedList<Recipe>();
		for(CookingArticle cal : caList) {
			Log.i("Recipe Activity", "Recipe added: "+cal.getRecipe().getName());
			recipeList.add(cal.getRecipe());
		}
		
		updateUi();
		
	}
	
	
	public void updateUi() {
		final ListView recipeListView = (ListView) findViewById(R.id.recipeListView);
	
		ListAdapter adapter = new ArrayAdapter<Recipe>(this, R.layout.listview_item_recipe, recipeList){
			
			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				
		        	View row = convertView;
		        
		        	if(row == null){
		        		LayoutInflater inflater = ((Activity) this.getContext()).getLayoutInflater();
		            		row = inflater.inflate(R.layout.listview_item_recipe, parent, false);
		        	}
		        
		        	TextView recipeNameTextView = (TextView) row.findViewById(R.id.recipeName);
		        	recipeNameTextView.setText(recipeList.get(position).getName());
		        
		        	TextView recipeDifficultyTextView = (TextView) row.findViewById(R.id.recipeDifficulty);
		        	recipeDifficultyTextView.setText("" + recipeList.get(position).getDifficulty());
		        
		        	TextView hitRateTextView = (TextView) row.findViewById(R.id.hitRate);
		        	hitRateTextView.setText("");
		       
		        	return row;
		    	}
		};
		
		recipeListView.setAdapter(adapter);
		
		recipeListView.setOnItemClickListener(new OnItemClickListener(){
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				Intent intent = new Intent(RecipeActivity.this, RecipeDetailActivity.class);
				intent.putExtra(Constants.keyRecipe, recipeList.get(arg2).getId());
				startActivity(intent);
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
	            // app icon in action bar clicked; go home
	            Intent intent = new Intent(this, MainActivity.class);
	            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
	            startActivity(intent);
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
}
