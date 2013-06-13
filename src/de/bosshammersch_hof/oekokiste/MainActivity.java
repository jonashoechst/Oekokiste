package de.bosshammersch_hof.oekokiste;

import de.bosshammersch_hof.oekokiste.model.*;
import de.bosshammersch_hof.oekokiste.ormlite.DatabaseFillMock;
import de.bosshammersch_hof.oekokiste.ormlite.DatabaseManager;
import de.bosshammersch_hof.oekokiste.postgres.*;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends Activity {
	
	User user;
	
	/**
	 *   calls the super Constructor
	 *   and set the Contentview 
	 *   @param  Bundle saved Instance State
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		
		// Init the Databasemanager
		DatabaseManager.init(this);
		
		// Try to fill the DB
		DatabaseFillMock.main(new String[0]);
		
		// Login login = new Login(123, "b");
		
		//new FillDatabase().execute(login);
		
		OpenState lastOpenState = DatabaseManager.getLastOpenState();
		
		if(lastOpenState != null) {
			Log.i("Main Activity", "last Open State found.");
			setupUser(lastOpenState.getId());
			
			updateUi();
		}
		else {
			// Print a warning / Maybe show Login-Screen?
			Toast warning = Toast.makeText(this, R.string.mainActivity_userCouldNotBeLoaded, 50);
			warning.show();
		}
		
	}
	
	/**
	 * Sends an intent if orderButton is clicked.
	 * Starts OrderActivity which shows the orderlist  
	 * @param  view The clicked View.
	 */
	public void orderButtonClicked(View view){
		Intent intent = new Intent(this, OrderActivity.class);
		intent.putExtra(Constants.keyUser, user.getId());
		Log.i("MainActivity", "Order Activity Intent created, UserId: "+intent.getIntExtra(Constants.keyUser, 0));
		startActivity(intent);
	}
	
	/**
	 * Sends an intent if loginButton is clicked.
	 * Starts LoginActivity lets the User login to the DB  
	 * @param  view The clicked View.
	 */
	public void loginButtonClicked(View view){
		Intent intent = new Intent(this, LoginActivity.class);
		startActivity(intent);
	}
	
	
	/**
	 *   Inflate the menu; 
	 *   this adds items to the action bar if it is present.
	 *   @param  Menu 
	 *   @return returns true
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	private void setupUser(int id){	
		user = DatabaseManager.getUser(id);	
	}
	
	private void updateUi(){
		// Do stuff to update the UI
		
	}
	
}
