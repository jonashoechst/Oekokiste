package de.bosshammersch_hof.oekokiste.robotiumTests;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import junit.framework.Test;
import junit.framework.TestSuite;

import de.bosshammersch_hof.oekokiste.ArticleDetailActivity;
import de.bosshammersch_hof.oekokiste.FindRecipesByArticleActivity;
import de.bosshammersch_hof.oekokiste.LoginActivity;
import de.bosshammersch_hof.oekokiste.MainActivity;
import de.bosshammersch_hof.oekokiste.OrderActivity;
import de.bosshammersch_hof.oekokiste.OrderDetailActivity;
import de.bosshammersch_hof.oekokiste.R;
import de.bosshammersch_hof.oekokiste.RecipeActivity;
import de.bosshammersch_hof.oekokiste.model.ArticleGroup;
import de.bosshammersch_hof.oekokiste.model.Recipe;
import de.bosshammersch_hof.oekokiste.model.Recipe.RecipeWithHits;
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
	
	
	public static final Test suite()
	{ 
	                TestSuite testSuite = new TestSuite(); 
	                return testSuite; 
	} 
	
	
	
   /**
    *   TEST01_Click the 'Login'-Button and Log in
    */
	public void testLoginActivity(){
		//check that we are on the start-activity
		solo.assertCurrentActivity("Wrong activity!", MainActivity.class);

		solo.clickOnButton(3);
		solo.enterText(0, "a-dur1990");
		solo.enterText(1, "artur");
		solo.clickOnButton(0);
		
		solo.sleep(5000);
		
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
		

		ArrayList<ArticleGroup> articleGroupList = new ArrayList<ArticleGroup>();
		
		Log.w("Test", "Elements in List View: "+articleListView.getCount() );
		int artpos = 5;
		while(artpos < 10){
			solo.clickInList(artpos);
			
			TextView tv = solo.getText(artpos);
			String articleGroup = (String) tv.getText();
			
			try {
				articleGroupList.add(DatabaseManager.getHelper().getArticleGroupDao().queryForId(articleGroup));
			} catch (SQLException e) {
				e.printStackTrace();
			}
			artpos += 1;
		}
		
		solo.clickOnButton("Rezepte finden");
		solo.clickInList(0);
		
		solo.assertCurrentActivity("This is not the RecipeActivity", RecipeActivity.class);
		ListView recipeListView = (ListView) solo.getView(R.id.recipeListView);
		
		ArrayList<String> recipeStrings = new ArrayList<String>();
		View recipeListItem = null;
		
		for(int recipeIndex = 1; recipeIndex<=recipeListView.getChildCount(); recipeIndex++){
			recipeListItem = recipeListView.getChildAt(recipeIndex);
			TextView recipeTextListItem = (TextView)recipeListItem;
			String recipeNameString = (String) recipeTextListItem.getText();
			Log.i("TestMainActivity: ","TextView with pos "+recipeIndex+" was selected.");
            recipeStrings.add(recipeNameString);
		}
		
		//Second Step: Compare the UI-recipes with the database
		
		List <RecipeWithHits> dbRecipeList = new ArrayList<RecipeWithHits>();
	
		try {
			dbRecipeList = Recipe.findRecipesByArticleGroups(articleGroupList, true);
			for(RecipeWithHits rec : dbRecipeList){
				boolean contains = false;
				
				int recipeId = rec.getRecipeId();
				Recipe eRecipe = DatabaseManager.getHelper().getRecipeDao().queryForId(recipeId);
				
				String recipeName = eRecipe.getName();
				for(String recipeString : recipeStrings){
					if(recipeName.equals(recipeString)){
						contains=true;
						Log.i("TestMainActivity: ","Recipe "+recipeName+" found in database!");
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
		
		solo.clickOnButton(3);
		solo.enterText(0, "a-dur1990");
		solo.enterText(1, "artur");
		solo.clickOnButton(0);
		
		
		//click button 'orders'
		solo.clickOnButton(0);
		solo.sleep(50000);
		
		//click first order
		solo.clickInList(0);
		solo.assertCurrentActivity("Wrong activity! This is not the OrderActivity!", OrderActivity.class);
		
		//press findrecipes-button
		solo.clickOnButton(0);
		solo.assertCurrentActivity("Wrong activity! This is not the OrderDetailActivity!", OrderDetailActivity.class);
        solo.sleep(1000);
		
		//now we reached the recipedetailactivity
		solo.clickInList(0);
		solo.assertCurrentActivity("Wrong activity! This is not the ArticleDetailActivity!", ArticleDetailActivity.class);
	    solo.sleep(3000);
	}
}
