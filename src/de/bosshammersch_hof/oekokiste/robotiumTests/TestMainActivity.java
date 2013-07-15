package de.bosshammersch_hof.oekokiste.robotiumTests;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import de.bosshammersch_hof.oekokiste.FindRecipesByArticleActivity;
import de.bosshammersch_hof.oekokiste.LoginActivity;
import de.bosshammersch_hof.oekokiste.MainActivity;
import de.bosshammersch_hof.oekokiste.R;
import de.bosshammersch_hof.oekokiste.RecipeActivity;
import de.bosshammersch_hof.oekokiste.model.Recipe;
import de.bosshammersch_hof.oekokiste.ormlite.DatabaseManager;
import android.content.Intent;
import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.jayway.android.robotium.solo.*;

public class TestMainActivity extends ActivityInstrumentationTestCase2<MainActivity> {

	/**
	 *   solo provides methods to call the Android user interface.
	 */
	private Solo solo;
	
	public TestMainActivity(){
		super(MainActivity.class);
	}
	
	public void setUp() throws Exception{
		solo = new Solo(getInstrumentation(), getActivity());
	}
	
	public void tearDown() throws Exception{
		solo.finishOpenedActivities();
	}
	
	
   /**
    *   TEST01_Click the 'Login'-Button and goto the 'Login'-Menu
    */
	public void testGo2LoginActivity(){
		//check that we are on the start-activity
		solo.assertCurrentActivity("Wrong activity!", MainActivity.class);
		
		solo.clickOnButton("Anmelden");
		solo.assertCurrentActivity("This is not the LoginActivity", LoginActivity.class);
	}
	
	
	
	/**
	 *  TEST02_Testing the article-result from 'FindRecipesByArticleActivity' 
	 * @throws InterruptedException 
	 */
	@SuppressWarnings("static-access")
	public void testFindRecipesByArticleActivity_WITHITEMS() throws InterruptedException{
		solo.assertCurrentActivity("Wrong activity!", MainActivity.class);
		
		//First Step: Get recipes for article - tested in UI
		solo.clickOnButton("Rezepte Finden");
		solo.assertCurrentActivity("This is not the FindRecipesByArticleActivity", FindRecipesByArticleActivity.class);
				
		ListView articleListView = (ListView) solo.getView(R.id.findArticleRecipeListView);
		
		Log.w("Test", "Elements in List View: "+articleListView.getCount() );
		int artpos = 1;
		while(artpos < articleListView.getCount()){
			solo.scrollListToLine(0, artpos-1);
			for(int i = 1; i <= 11; i++){
				if((artpos + i) >= articleListView.getCount()){
					break;
				}
				solo.clickInList(i);
			}
			artpos += 10;
		}
		
		solo.clickOnButton("Rezepte finden");
		solo.clickInList(0);
		
		solo.assertCurrentActivity("This is not the RecipeActivity", RecipeActivity.class);
		ListView recipeListView = (ListView) solo.getView(R.id.recipeListView);
		
		ArrayList<String> recipeStrings = new ArrayList<String>();
		View recipeListItem = null;
		
		for(int recipeIndex = 1; recipeIndex<=recipeListView.getChildCount(); recipeIndex++){
			recipeListItem = recipeListView.getChildAt(recipeIndex);
			TextView recipeName = (TextView) recipeListItem.findViewById(R.id.recipeName);
			String recipeNameString = (String) (recipeName).getText();
            recipeStrings.add(recipeNameString);
		}
		
		//Second Step: Compare the UI-recipes with the database
		
		List <Recipe> dbRecipeList = new ArrayList<Recipe>();
		try {
			dbRecipeList = DatabaseManager.getHelper().getRecipeDao().queryForAll();
			for(Recipe rec : dbRecipeList){
				boolean contains = false;
				String recipeName = rec.getName();
				for(String recipeString : recipeStrings){
					if(recipeName.equals(recipeString)){
						contains=true;
						break;
					}
				}
				if(!contains){
					this.fail("Recipe "+recipeName+" not found!");
				}
			}
		} catch (SQLException e) {
			this.fail("SQL-Error: Could not get recipes from internal database.");
			e.printStackTrace();
		}
		
	}
	
	
	/**
	 *  TEST03_Testing the article-result from 'FindRecipesByArticleActivity' WITHOUT ITEMS!!
	 * @throws InterruptedException 
	 */
	public void testFindRecipesByArticleActivity_NOITEMS() throws InterruptedException{
		solo.assertCurrentActivity("Wrong activity!", MainActivity.class);
		
		solo.clickOnButton("Rezepte Finden");
		
		solo.assertCurrentActivity("This is not the FindRecipesByArticleActivity", FindRecipesByArticleActivity.class);
		solo.clickOnButton("Rezepte finden");
		solo.clickInList(0);
		solo.assertCurrentActivity("This is not the RecipeActivity", RecipeActivity.class);
	}
	
	
	/**
	 *  TEST04_Check scale of amounts of article depending in RecipeDetailActivity
	 * @throws InterruptedException 
	 */
	public void testScaleRecipeDetailActivity() throws InterruptedException{
		solo.assertCurrentActivity("Wrong activity!", MainActivity.class);
		
		solo.clickOnButton("Anmelden");
		
		solo.enterText(0, "a-dur1990");
		solo.enterText(1, "artur");
		solo.clickOnButton("Anmelden");
		
		solo.wait(30000);
		
		//click on the first order
		solo.clickInList(0);
		
		//click on findrecipebutton
		//TODO solo.clickOnButton("Rezepte");
		
		//select each recipe
		ArrayList<View> recipeList = solo.getCurrentViews();
		
		if(recipeList != null){
			for(int i=0; i < recipeList.size(); i++){
				solo.clickInList(i);
				
				//reaching recipedetail
				//checking scale of different values (in this case 2,4)
				solo.pressSpinnerItem(0, 2);
				
				View view = solo.getView(R.id.ingredientAmount);
				//TODO get values of all amounts
			}
		}
	}
	
	
	
}
