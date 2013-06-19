package de.bosshammersch_hof.oekokiste;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import de.bosshammersch_hof.oekokiste.model.*;
import de.bosshammersch_hof.oekokiste.ormlite.DatabaseManager;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
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
	
	Map<Recipe, Integer> recipeMap;
	
	/**
	 * Rezepte werden bei Bedarf mit Trefferquote erweitert.
	 *
	 */
	private class RecipeWithHits{
		public Recipe recipe;
		public int hits;
		public RecipeWithHits(Recipe r, int hits){
			this.recipe = r;
			this.hits = hits;
		}
	}
	
	/** 
	 *   creats the hole list of recipe
	 *   @param Bundle saved Instance State
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_recipe);
		getActionBar().setHomeButtonEnabled(true);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		Constants.refreshableActivity = this;
		refreshData();
	}
	
	/**
	 * Daten werden aktualisiert.
	 */
	@Override
	public void refreshData() {

		String[] articleGroupIdArray = this.getIntent().getStringArrayExtra(Constants.keyArticleGroupNameArray);
		
		List<ArticleGroup> articleGroups = new LinkedList<ArticleGroup>();
		for(String articleGroupName : articleGroupIdArray){
			try {
				articleGroups.add(DatabaseManager.getHelper().getArticleGroupDao().queryForId(articleGroupName));
			} catch (java.sql.SQLException e) {
				e.printStackTrace();
			}
		}
		
		recipeMap = new HashMap<Recipe, Integer>();
		
		for(ArticleGroup ag : articleGroups){
			// Create a Query Example
			CookingArticle example = new CookingArticle();
			example.setArticleGroup(ag);
			List<CookingArticle> caList;
			// excecute Query with example
			try {
				caList = DatabaseManager.getHelper().getCookingArticleDao().queryForMatching(example);
			} catch (java.sql.SQLException e) {
				caList = new LinkedList<CookingArticle>();
			}
			
			if(caList == null) continue;
			
			for(CookingArticle ca : caList){
				Recipe r = ca.getRecipe();
				if(recipeMap.containsKey(r))
					recipeMap.put(r, recipeMap.get(r)+1);
				else
					recipeMap.put(r, 1);
			}
		}
		
		updateUi();
		
	}
	
	/**
	 * Aktualisiert die UI.
	 */
	public void updateUi() {
		final ListView recipeListView = (ListView) findViewById(R.id.recipeListView);
		
		final List<RecipeWithHits> recipeList = new LinkedList<RecipeWithHits>();
		
		while(!recipeMap.isEmpty()){
			
			Recipe bestRecipe = null;
			
			for (Recipe key : recipeMap.keySet()) {
				if(bestRecipe == null) bestRecipe = key;
				if(recipeMap.get(bestRecipe) < recipeMap.get(key)) bestRecipe = key;
			}
			
			recipeList.add(new RecipeWithHits(bestRecipe, recipeMap.get(bestRecipe)));
			recipeMap.remove(bestRecipe);
		}
	
		ListAdapter adapter = new ArrayAdapter<RecipeWithHits>(this, R.layout.listview_item_recipe, recipeList){
			
			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				
		        	View row = convertView;
		        
		        	if(row == null){
		        		LayoutInflater inflater = ((Activity) this.getContext()).getLayoutInflater();
		            		row = inflater.inflate(R.layout.listview_item_recipe, parent, false);
		        	}
		        
		        	TextView recipeNameTextView = (TextView) row.findViewById(R.id.recipeName);
		        	recipeNameTextView.setText(recipeList.get(position).recipe.getName());
		        
		        	TextView recipeDifficultyTextView = (TextView) row.findViewById(R.id.recipeDifficulty);
		        	recipeDifficultyTextView.setText("" + recipeList.get(position).recipe.getDifficulty());
		        
		        	TextView hitRateTextView = (TextView) row.findViewById(R.id.hitRate);
		        	hitRateTextView.setText(recipeList.get(position).hits+"");
		       
		        	return row;
		    	}
		};
		
		recipeListView.setAdapter(adapter);
		
		recipeListView.setOnItemClickListener(new OnItemClickListener(){
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				Intent intent = new Intent(RecipeActivity.this, RecipeDetailActivity.class);
				intent.putExtra(Constants.keyRecipe, recipeList.get(arg2).recipe.getId());
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
