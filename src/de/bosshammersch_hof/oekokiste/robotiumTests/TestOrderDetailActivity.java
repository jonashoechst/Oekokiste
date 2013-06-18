package de.bosshammersch_hof.oekokiste.robotiumTests;

import java.util.ArrayList;
import de.bosshammersch_hof.oekokiste.ArticleDetailActivity;
import de.bosshammersch_hof.oekokiste.MainActivity;
import de.bosshammersch_hof.oekokiste.OrderDetailActivity;
import de.bosshammersch_hof.oekokiste.RecipeActivity;import android.test.ActivityInstrumentationTestCase2;
import android.view.View;
import com.jayway.android.robotium.solo.*;

public class TestOrderDetailActivity extends ActivityInstrumentationTestCase2<OrderDetailActivity>{

	/**
	 *   solo provides methods to call the Android user interface.
	 */
	private Solo solo;
	
	public TestOrderDetailActivity(){
		super(OrderDetailActivity.class);
	}
	
	public void setUp() throws Exception{
		solo = new Solo(getInstrumentation(), getActivity());
	}
	
	public void tearDown() throws Exception{
		solo.finishOpenedActivities();
	}
	
	
	/**
	 *  Test01: Click on the button 'Rechnung'
	 *          (OrderDetailActivity -> WebPDFViewActivity) 
	 */
	public void testGotoWebPDFViewActivityAcitivty(){
		solo.assertCurrentActivity("This is not the OrderDetailAcitivty", OrderDetailActivity.class);
		solo.clickOnButton("Rechung");
		
		//TODO solo.assertCurrentActivity("This is not the WebPDFViewActivityAcitivty", WebPDFViewActivity.class);
	}
	
	/**
	 *  Test02: Click on the button 'Rezepte finden...'
	 *          (OrderDetailActivity -> WebPDFViewActivity) 
	 */
	public void testGotoRecipeActivity(){
		solo.assertCurrentActivity("This is not the OrderDetailActivity", OrderDetailActivity.class);
		solo.clickOnButton("Rezepte finden.");
		solo.assertCurrentActivity("This is not the RecipeActivity", RecipeActivity.class);
	}
	
	/**
	 *  Test03: Click home-button to go to the MainActivity 
	 *          (OrderDetailActivity -> MainActivity) 
	 */
	public void testGotoMainActivity(){
		solo.assertCurrentActivity("This is not the OrderDetailActivity", OrderDetailActivity.class);
		solo.clickOnActionBarHomeButton();
		solo.assertCurrentActivity("This is not the MainActivity", MainActivity.class);
	}
	
	/**
	 *  Test04: Click on all Orders in the ListView
	 *          (OrderDetailActivity -> ArticelDetailActivity)
	 */
	public void testAllOrders(){
		solo.assertCurrentActivity("This is not the OrderDetailActivity", OrderDetailActivity.class);
		ArrayList<View> list = solo.getCurrentViews();
		for(int i=0; i < list.size(); i++){
			solo.clickInList(i);
			solo.waitForActivity(ArticleDetailActivity.class);
			solo.assertCurrentActivity("This is not the ArticleActivity", ArticleDetailActivity.class);
			solo.goBack();
		}
	}
	
	
}
