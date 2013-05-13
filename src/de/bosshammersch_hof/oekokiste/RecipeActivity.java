package de.bosshammersch_hof.oekokiste;

import java.util.LinkedList;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class RecipeActivity extends Activity {

	Recipe recipeSuggestionOne = new Recipe("Nudeln mit Tomatensoße",(int) (Math.random() * 100));
	Recipe recipeSuggestionTwo = new Recipe("Spiegeleier", (int) (Math.random() * 100));
	Recipe recipeSuggestionThree = new Recipe("Käsebrot", (int) (Math.random() * 100));
	Recipe recipeSuggestionFour = new Recipe("Schnitzel und Pommes", (int) (Math.random() * 100));
	Recipe recipeSuggestionFive = new Recipe("Lasange", (int) (Math.random() * 100));
	Recipe recipeSuggestionSix = new Recipe("Hummer", (int) (Math.random() * 100));
	Recipe recipeSuggestionSeven = new Recipe("blah", (int) (Math.random() * 100));
	Recipe recipeSuggestionEight = new Recipe("blih", (int) (Math.random() * 100));
	Recipe recipeSuggestionNine = new Recipe("blubb", (int) (Math.random() * 100));
	
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
		
	}

}
