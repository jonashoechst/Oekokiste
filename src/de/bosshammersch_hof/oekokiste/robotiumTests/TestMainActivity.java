package de.bosshammersch_hof.oekokiste.robotiumTests;

import de.bosshammersch_hof.oekokiste.LoginActivity;
import de.bosshammersch_hof.oekokiste.MainActivity;
import android.test.ActivityInstrumentationTestCase2;
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
	
	

	//Following Tests are for users who are still logged in
   /**
    *   TEST01_Click the 'Login'-Button and goto the 'Login'-Menu
    */
	public void testGo2LoginActivity(){
		//check that we are on the start-activity
		solo.assertCurrentActivity("Wrong activity!", MainActivity.class);
		
		solo.clickOnButton("Anmelden...");
		solo.assertCurrentActivity("This is not the LoginActivity", LoginActivity.class);
	}
}
