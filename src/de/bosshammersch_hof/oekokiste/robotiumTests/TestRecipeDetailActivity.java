package de.bosshammersch_hof.oekokiste.robotiumTests;

import de.bosshammersch_hof.oekokiste.ArticleDetailActivity;
import de.bosshammersch_hof.oekokiste.MainActivity;
import de.bosshammersch_hof.oekokiste.RecipeDetailActivity;
import android.test.ActivityInstrumentationTestCase2;
import com.jayway.android.robotium.solo.*;

public class TestRecipeDetailActivity extends ActivityInstrumentationTestCase2<RecipeDetailActivity>{

	/**
	 *   solo provides methods to call the Android user interface.
	 */
	private Solo solo;
	
	public TestRecipeDetailActivity(){
		super(RecipeDetailActivity.class);
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
		solo.assertCurrentActivity("This is not the RecipeDetailActivity", RecipeDetailActivity.class);
		solo.clickOnActionBarHomeButton();
		solo.assertCurrentActivity("This is not the MainActivity", MainActivity.class);
	}	
	
	
	/**
	 *  TEST02_ArticleDetail-Test
	 */
	public void testGo2ArticleDetailActivity(){
		solo.assertCurrentActivity("This is not the RecipeDetailActivity", RecipeDetailActivity.class);
		
		solo.scrollDown();
		solo.scrollUp();
		
		solo.clickInList(0);
		solo.assertCurrentActivity("This is not the ArticleDetailActivity", ArticleDetailActivity.class);
	}
}
