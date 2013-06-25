package de.bosshammersch_hof.oekokiste.robotiumTests;

import de.bosshammersch_hof.oekokiste.LoginActivity;
import de.bosshammersch_hof.oekokiste.OrderActivity;
import android.test.ActivityInstrumentationTestCase2;
import com.jayway.android.robotium.solo.*;

public class TestLoginActivity extends ActivityInstrumentationTestCase2<LoginActivity> {

	/**
	 *   solo provides methods to call the Android user interface.
	 */
	private Solo solo;
	
	public TestLoginActivity(){
		super(LoginActivity.class);
	}
	
	public void setUp() throws Exception{
		solo = new Solo(getInstrumentation(), getActivity());
	}
	
	public void tearDown() throws Exception{
		solo.finishOpenedActivities();
	}
	
	

	/**
	 *  TEST01_Fill in user_name and user_passwort and login
	 */
		public void testLogin(){
			//check that we are on the start-activity
			solo.assertCurrentActivity("Wrong activity!", LoginActivity.class);
			
			solo.scrollDown();
			solo.scrollUp();
			
			solo.enterText(0, "testuser");
			solo.enterText(1, "testpassword");
			
			//did we loose information?
			solo.scrollDown();
			solo.scrollUp();
			
			solo.clickOnButton("Anmelden");
			solo.assertCurrentActivity("This is not the LoginActivity", LoginActivity.class);
		}
		
    /**
     *  TEST02_Click the 'Bestellungen'-Button and goto the 'Bestellungen'-Menu 
     * @throws InterruptedException 
     */
	public void testGo2OrderActivity() throws InterruptedException{
		
		solo.assertCurrentActivity("Wrong activity!", LoginActivity.class);
		
		solo.scrollDown();
		solo.scrollUp();
		
		solo.enterText(0, "testuser");
		solo.enterText(1, "testpassword");
		
		//did we loose information?
		solo.scrollDown();
		solo.scrollUp();
		
		solo.clickOnButton("Anmelden");
		solo.clickOnButton("Bestellungen");
		solo.assertCurrentActivity("This is not the OrderActivity", OrderActivity.class);	
	}
	
}
