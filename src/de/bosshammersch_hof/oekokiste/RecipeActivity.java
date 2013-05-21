package de.bosshammersch_hof.oekokiste;

import java.util.LinkedList;

import de.bosshammersch_hof.oekokiste.model.Recipe;

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

public class RecipeActivity extends Activity {
	
	
	TextView recipeNameTextView;
	TextView hitRateTextView;
	
	/** 
	 *   creats the hole list of recipe
	 *   @param Bundle saved Instance State
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_recipe);
		
		final LinkedList<Recipe> recipeList = getDummyRecipeList();
		
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
		        
		        	TextView hitRateTextView = (TextView) row.findViewById(R.id.hitRate);
		        	hitRateTextView.setText("" + recipeList.get(position).getHitPoints());
		       
		        	return row;
		    	}
		};
		
		recipeListView.setAdapter(adapter);
		
		recipeListView.setOnItemClickListener(new OnItemClickListener(){
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				Intent intent = new Intent(RecipeActivity.this, RecipeDetailActivity.class);
				startActivity(intent);
			}
		});
		
		getActionBar().setHomeButtonEnabled(true);
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
	
	/**
	 *   supplies dummy data for testing the app
	 *   @return  LinkedList<Recipe>
	 */
	private LinkedList<Recipe> getDummyRecipeList(){
		
		LinkedList<Recipe> recipeList = new LinkedList<Recipe>();

		recipeList.add(new Recipe("Chili sin carne", "", "", null, null, 3, 10, 30, 4, null, 16));
		recipeList.add(new Recipe("Käsebrot", "", "", null, null, 1, 2, 0, 1, null, 14));
		recipeList.add(new Recipe("Milchreis", "", "", null, null, 3, 0, 10, 2, null, 13));
		recipeList.add(new Recipe("Gemüse-Lasagne", "", "", null, null, 3, 20, 30, 6, null, 10));
		recipeList.add(new Recipe("Hacksteak ala Patrick", "", "", null, null, 5, 30, 50, 4, null, 6));
		recipeList.add(new Recipe("Death by cheese pizza", "", "", null, null, 3, 20, 50, 1, null, 6));
		recipeList.add(new Recipe("Mitternachtssuppe", "", "", null, null, 3, 30, 20, 10, null, 6));
		recipeList.add(new Recipe("Kandierte Orangenblüten", "", "", null, null, 4, 60, 0, 1, null, 4));
		recipeList.add(new Recipe("Pfannkuchen mit Blattspinatfüllung", "", "", null, null, 1, 20, 15, 4, null, 4));
		recipeList.add(new Recipe("Kartoffelnudeln mit Vanillesauce", "", "", null, null, 1, 45, 0, 4, null, 3));
		recipeList.add(new Recipe("Warmer Speckkrautsalat", "", "", null, null, 3, 30, 0, 10, null, 2));
		
		return recipeList;
		
	}
}
