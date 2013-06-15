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

public class RecipeActivity extends Activity implements UpdatableActivity{
	
	LinkedList<Recipe> recipeList;
	
	/** 
	 *   creats the hole list of recipe
	 *   @param Bundle saved Instance State
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_recipe);
		getActionBar().setHomeButtonEnabled(true);
		
		recipeList = getDummyRecipeList();
		
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
	
	/**
	 *   supplies dummy data for testing the app
	 *   @return  LinkedList<Recipe>
	 */
	private LinkedList<Recipe> getDummyRecipeList(){
		
		LinkedList<Recipe> recipeList = new LinkedList<Recipe>();

		recipeList.add(new Recipe(2, "Chili sin carne", "", "", null, null, 3, 10, 30, 4, null, 90));
		recipeList.add(new Recipe(1, "Flammkuchen", "", "", null, null, 1, 45, 30, 5, null, 80));
		recipeList.add(new Recipe(3, "Milchreis", "", "", null, null, 3, 0, 10, 2, null, 77));
		recipeList.add(new Recipe(4, "Gemüse-Lasagne", "", "", null, null, 3, 20, 30, 6, null, 50));
		recipeList.add(new Recipe(5, "Hacksteak ala Patrick", "", "", null, null, 5, 30, 50, 4, null, 45));
		recipeList.add(new Recipe(6, "Death by cheese pizza", "", "", null, null, 3, 20, 50, 1, null, 40));
		recipeList.add(new Recipe(7, "Mitternachtssuppe", "", "", null, null, 3, 30, 20, 10, null, 39));
		recipeList.add(new Recipe(8, "Kandierte Orangenblüten", "", "", null, null, 4, 60, 0, 1, null, 33));
		recipeList.add(new Recipe(9, "Pfannkuchen mit Blattspinatfüllung", "", "", null, null, 1, 20, 15, 4, null,30));
		recipeList.add(new Recipe(10, "Kartoffelnudeln mit Vanillesauce", "", "", null, null, 1, 45, 0, 4, null, 27));
		recipeList.add(new Recipe(11, "Warmer Speckkrautsalat", "", "", null, null, 3, 30, 0, 10, null, 26));
		recipeList.add(new Recipe(12, "Rinderfiletscheiben in Kürbiskernpanier", "", "", null, null, 3, 30, 0, 4, null, 25));
		recipeList.add(new Recipe(13, "Milder Gewürzessig", "", "", null, null, 1, 20, 0, 2, null, 24));
		recipeList.add(new Recipe(14, "Purple Rain", "", "", null, null, 1, 0, 5, 1, null, 20));
		recipeList.add(new Recipe(15, "Sauerkrautsauce mit Speck", "", "", null, null, 1, 20, 25, 4, null, 10));
		recipeList.add(new Recipe(16, "Honig-Senf-Schweinesteaks mit Bourbon Whiskey", "", "", null, null, 1, 8, 10, 4, null, 9));
		recipeList.add(new Recipe(17, "Lisbon", "", "", null, null, 3, 30, 0, 10, null, 5));
		recipeList.add(new Recipe(18, "Grünes Kohlcurry", "", "", null, null, 1, 10, 30, 4, null, 3));
		recipeList.add(new Recipe(19, "Anislikör", "", "", null, null, 1, 0, 10, 2, null, 2));
		recipeList.add(new Recipe(20, "Sangria-Eis-Charlotte", "", "", null, null, 1, 30, 0, 16, null, 1));
		
		return recipeList;
		
	}
}
