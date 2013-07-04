package de.bosshammersch_hof.oekokiste.robotiumTests;

import java.util.ArrayList;

import de.bosshammersch_hof.oekokiste.FindRecipesByArticleActivity;
import de.bosshammersch_hof.oekokiste.LoginActivity;
import de.bosshammersch_hof.oekokiste.MainActivity;
import de.bosshammersch_hof.oekokiste.R;
import de.bosshammersch_hof.oekokiste.RecipeActivity;
import android.app.Instrumentation;
import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;

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
	 * Gives you the view from an list view index
	 * @param listElement
	 * @param indexInList
	 * @param instrumentation
	 * @return
	 */
	public View getViewAtIndex(final ListView listElement, final int indexInList, Instrumentation instrumentation) {
	    ListView parent = listElement;
	    if (parent != null) {
	        if (indexInList <= parent.getAdapter().getCount()) {
	            scrollListTo(parent, indexInList, instrumentation);
	            int indexToUse = indexInList - parent.getFirstVisiblePosition();
	            return parent.getChildAt(indexToUse);
	        }
	    }
	    return null;
	}

	/**
	 * Allows you to scroll in a list view
	 * @param listView
	 * @param index
	 * @param instrumentation
	 */
	public <T extends AbsListView> void scrollListTo(final T listView,
	        final int index, Instrumentation instrumentation) {
	    instrumentation.runOnMainSync(new Runnable() {
	        @Override
	        public void run() {
	            listView.setSelection(index);
	        }
	    });
	    instrumentation.waitForIdleSync();
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
	public void testFindRecipesByArticleActivity_WITHITEMS() throws InterruptedException{
		solo.assertCurrentActivity("Wrong activity!", MainActivity.class);
		
		//First Step: Get recipes for article-examples
		solo.clickOnButton("Rezepte Finden");
		solo.assertCurrentActivity("This is not the FindRecipesByArticleActivity", FindRecipesByArticleActivity.class);
				
		for(int artpos=1; artpos<=4; artpos++){
			solo.clickInList(artpos);
			Log.i("testFindRecipesByArticleActivity_WITHITEMS","ArticleItem Nr. "+artpos+" was clicked!");
		}
		
		solo.clickOnButton("Rezepte finden");
		solo.clickInList(0);
		solo.assertCurrentActivity("This is not the RecipeActivity", RecipeActivity.class);
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
