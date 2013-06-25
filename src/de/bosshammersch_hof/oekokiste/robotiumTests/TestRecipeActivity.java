package de.bosshammersch_hof.oekokiste.robotiumTests;

import de.bosshammersch_hof.oekokiste.ArticleDetailActivity;
import de.bosshammersch_hof.oekokiste.MainActivity;
import de.bosshammersch_hof.oekokiste.OrderActivity;
import de.bosshammersch_hof.oekokiste.RecipeActivity;
import android.test.ActivityInstrumentationTestCase2;
import com.jayway.android.robotium.solo.*;

public class TestRecipeActivity extends ActivityInstrumentationTestCase2<RecipeActivity>{

	/**
	 *   solo provides methods to call the Android user interface.
	 */
	private Solo solo;
	
	public TestRecipeActivity(){
		super(RecipeActivity.class);
	}
	
	public void setUp() throws Exception{
		solo = new Solo(getInstrumentation(), getActivity());
	}
	
	public void tearDown() throws Exception{
		solo.finishOpenedActivities();
	}
	
	
	
	/**
	 *  Test01: Click home-button to go to the MainActivity 
	 *          (RecipeDetailActivity -> MainActivity) 
	 */
	public void testGotoMainActivity(){
		solo.assertCurrentActivity("This is not the RecipeActivity", RecipeActivity.class);
		solo.clickOnActionBarHomeButton();
		solo.assertCurrentActivity("This is not the MainActivity", MainActivity.class);
	}	
	
	
	/**
	 *  Test02: Press 'Back-Androidbutton' to reach the OrderActivity
	 */
	public void testGoBackToOrderActivity(){
		solo.assertCurrentActivity("This is not the RecipeActivity", RecipeActivity.class);
		solo.goBackToActivity(OrderActivity.class.getName());
		
		solo.assertCurrentActivity("This is not the OrderActivity", OrderActivity.class);
	}
	
	
	/**
	 *  Test03: Press an 'ArticleListElement' to go to the ArticleDetailActivity
	 */
	public void testClickOnListArticle(){
		solo.assertCurrentActivity("This is not the RecipeActivity", RecipeActivity.class);
		
		solo.scrollDown();
		solo.scrollUp();
		
		solo.clickInList(0);
		solo.assertCurrentActivity("This is not the ArticleDetailActivity", ArticleDetailActivity.class);
	}
}
