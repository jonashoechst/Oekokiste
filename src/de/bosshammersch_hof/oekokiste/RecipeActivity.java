package de.bosshammersch_hof.oekokiste;

import java.util.LinkedList;

import android.os.Bundle;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class RecipeActivity extends Activity {

	Recipe recipeSuggestionOne = new Recipe("Nudeln mit Tomatenso§e",(int) Math.random() * 100);
	Recipe recipeSuggestionTwo = new Recipe("Spiegeleier", (int) Math.random() * 100);
	Recipe recipeSuggestionThree = new Recipe("KŠsebrot", (int) Math.random() * 100);
	Recipe recipeSuggestionFour = new Recipe("Schnitzel und Pommes", (int) Math.random() * 100);
	Recipe recipeSuggestionFive = new Recipe("Lasange", (int) Math.random() * 100);
	Recipe recipeSuggestionSix = new Recipe("Hummer", (int) Math.random() * 100);
	Recipe recipeSuggestionSeven = new Recipe("blah", (int) Math.random() * 100);
	Recipe recipeSuggestionEight = new Recipe("blih", (int) Math.random() * 100);
	Recipe recipeSuggestionNine = new Recipe("blubb", (int) Math.random() * 100);
	
	TextView recipeNameTextView;
	TextView recipePerPersonTextView;
	TextView recipeDifficultyTextView;
	TextView recipeTextTextView;
	
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
		
		final ListView recipeListView = (ListView) findViewById(R.id.orderListView);
	
		ListAdapter adapter = new ArrayAdapter<Recipe>(this, R.layout.order_item, recipeList){
			
			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				
		        View row = convertView;
		        
		        if(row == null){
		            LayoutInflater inflater = ((Activity) this.getContext()).getLayoutInflater();
		            
		            row = inflater.inflate(R.layout.order_item, parent, false);

		        }
		        
		        TextView orderDateTextView = (TextView) row.findViewById(R.id.orderDateTextView);
		        orderDateTextView.setText(recipeList.get(position).getName());
		        
		        TextView boxnameTextView = (TextView) row.findViewById(R.id.boxnameTextView);
		        boxnameTextView.setText(recipeList.get(position).getHitRate());
		       
		        return row;
		    }
		};
		
		recipeListView.setAdapter(adapter);
		
	}

}
