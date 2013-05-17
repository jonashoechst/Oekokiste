package de.bosshammersch_hof.oekokiste;

import java.util.Date;
import java.util.LinkedList;

import de.bosshammersch_hof.oekokiste.model.Order;
import de.bosshammersch_hof.oekokiste.model.Recipe;
import de.bosshammersch_hof.oekokiste.model.User;

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
	
	LinkedList<Recipe> recipeList = new LinkedList<Recipe>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_recipe);
		
		recipeList.add(recipeSuggestionOne);
		recipeList.add(recipeSuggestionTwo);
		recipeList.add(recipeSuggestionThree);
		recipeList.add(recipeSuggestionFour);
		recipeList.add(recipeSuggestionFive);
		recipeList.add(recipeSuggestionSix);
		recipeList.add(recipeSuggestionSeven);
		recipeList.add(recipeSuggestionEight);
		recipeList.add(recipeSuggestionNine);
		
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
		        recipeNameTextView.setText(recipeList.get(position).name);
		        
		        TextView hitRateTextView = (TextView) row.findViewById(R.id.hitRate);
		        hitRateTextView.setText("" + recipeList.get(position).hitRate);
		       
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
	
	private LinkedList<Recipe> getDummyUser(){
			
	}
}
