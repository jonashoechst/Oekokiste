package de.bosshammersch_hof.oekokiste.robotiumTests;

import android.test.ActivityInstrumentationTestCase2;

import com.jayway.android.robotium.solo.Solo;

import de.bosshammersch_hof.oekokiste.MainActivity;
import de.bosshammersch_hof.oekokiste.OrderActivity;
import de.bosshammersch_hof.oekokiste.OrderDetailActivity;


public class TestOrderActivity extends ActivityInstrumentationTestCase2<OrderActivity> {
	
	/**
	 *   solo provides methods to call the Android user interface.
	 */
	private Solo solo;
	
	public TestOrderActivity(){
		super(OrderActivity.class);
	}
	
	public void setUp() throws Exception{
		solo = new Solo(getInstrumentation(), getActivity());
	}
	
	public void tearDown() throws Exception{
		solo.finishOpenedActivities();
	}
	
	/**
	 *  Test01: Click home-button to go to the MainActivity 
	 *          (OrderActivity -> LoginActivity) 
	 */
	public void testGotoMainActivity(){
		solo.assertCurrentActivity("This is not the OrderActivity", OrderActivity.class);
		solo.clickOnActionBarHomeButton();
		solo.assertCurrentActivity("This is not the MainActivity", MainActivity.class);
	}
	
	/**
	 *  Test02: Click on an Order to go to the OrderDetailActivity
	 *          (OrderActivity -> OrderDetailActivity) 
	 */
	public void testGotoOrderDetailActivity(){
		solo.assertCurrentActivity("This is not the OrderActivity", OrderActivity.class);
		solo.clickInList(0);
		solo.assertCurrentActivity("This is not the OrderDetailActivity", OrderDetailActivity.class);
	}
	
}
