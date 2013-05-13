package de.bosshammersch_hof.oekokiste;

//import java.util.LinkedList;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
//import android.widget.ImageView;
//import android.widget.TextView;

public class RecipeDetailActivity extends Activity {

	/*
	TextView recipeNameTextView;
	TextView recipeNumberOfPersonTextView;
	TextView recipeExampleNumberOfPerson;
	TextView recipeTimeInMinutesTextView;
	ImageView recipePicture;
	TextView recipeLongDescription;
	TextView recipeCookingUtensils;
	TextView recipeExampleCookingUtensils;
	TextView recipeDifficulty;
	TextView recipeExampleDifficulty;
	
	
	
	LinkedList<Recipe> recipeList = new LinkedList<Recipe>();
	Recipe spagetti = new Recipe("Spagetti", "Ein Spagettibild", "Einfaches Spagettigericht", "", 4.0, 4, 10, 110);
	*/
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_recipe_detail);
		
		String name = getIntent().getStringExtra(OrderDetailActivity.ARTICLE_NAME_KEY);
		
		setTitle(name);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.recipe_detail, menu);
		return true;
	}

}
