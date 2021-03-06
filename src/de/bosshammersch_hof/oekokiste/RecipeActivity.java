package de.bosshammersch_hof.oekokiste;

import java.sql.SQLException;
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

	List<RecipeWithHits> recipeWithHitsList;
	
	/**
	 * Rezepte werden bei Bedarf mit Trefferquote erweitert.
	 *
	 */
	public class RecipeWithHits{
		public Recipe recipe;
		public int hits;
		public RecipeWithHits(int recipeId, int hits) throws SQLException{
			this.recipe = DatabaseManager.getHelper().getRecipeDao().queryForId(recipeId);
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
	
	/**
	 *  call the super constructor and call refreshData();
	 */
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

		int[] recipeIdArray = this.getIntent().getIntArrayExtra(Constants.keyRecipeIdArray);
		int[] recipeHitsArray = this.getIntent().getIntArrayExtra(Constants.keyRecipeHitsArray);
		
		recipeWithHitsList = new LinkedList<RecipeWithHits>();
		for(int i = 0; i < recipeIdArray.length; i++) {
			try {
				recipeWithHitsList.add(new RecipeWithHits(recipeIdArray[i], recipeHitsArray[i]));
			} catch (SQLException e) {
				Log.e("Recipe Activity","Recipe could not be entered, id:"+recipeIdArray[i]);
				e.printStackTrace();
			}
		}
		
		updateUi();
		
	}
	
	/**
	 * Aktualisiert die UI.
	 */
	public void updateUi() {
		final ListView recipeListView = (ListView) findViewById(R.id.recipeListView);
	
		ListAdapter adapter = new ArrayAdapter<RecipeWithHits>(this, R.layout.listview_item_recipe, recipeWithHitsList){
			
			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				
		        	View row = convertView;
		        
		        	if(row == null){
		        		LayoutInflater inflater = ((Activity) this.getContext()).getLayoutInflater();
		            	row = inflater.inflate(R.layout.listview_item_recipe, parent, false);
		        	}
		        	
		        
		        	TextView recipeNameTextView = (TextView) row.findViewById(R.id.recipeName);
		        	recipeNameTextView.setText(recipeWithHitsList.get(position).recipe.getName());
		        
		        	TextView recipeDifficultyTextView = (TextView) row.findViewById(R.id.recipeDifficulty);
		        	recipeDifficultyTextView.setText(recipeWithHitsList.get(position).recipe.getDifficulty()+"");
		        
		        	TextView hitRateTextView = (TextView) row.findViewById(R.id.hitRate);
		        	if(recipeWithHitsList.get(position).hits != 0)
		        		hitRateTextView.setText(recipeWithHitsList.get(position).hits+"");
		       
		        	return row;
		    	}
		};
		
		recipeListView.setAdapter(adapter);
		
		recipeListView.setOnItemClickListener(new OnItemClickListener(){
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				Intent intent = new Intent(RecipeActivity.this, RecipeDetailActivity.class);
				intent.putExtra(Constants.keyRecipeId, recipeWithHitsList.get(arg2).recipe.getId());
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
