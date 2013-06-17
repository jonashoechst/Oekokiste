package de.bosshammersch_hof.oekokiste;

import java.sql.SQLException;

import de.bosshammersch_hof.oekokiste.model.*;
import de.bosshammersch_hof.oekokiste.ormlite.DatabaseManager;
import de.bosshammersch_hof.oekokiste.postgres.*;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;
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

		// 1. Versuch: letzten Status šffnen
		OpenState lastOpenState = null;
		
		try {
			lastOpenState = DatabaseManager.getHelper().getOpenStateDao().queryForId(1);
		} catch (SQLException e) {
			Log.i("Oekokiste: Main Actitvity","SQL Exception finding the last OpenState");
			e.printStackTrace();
		}
		
		if(lastOpenState != null) {
			Log.i("Main Activity", "last Open State found.");
			user = lastOpenState.getUser();
			updateUiWithUser();
			return;
		}

		// 2. Versuch: Kommen wir von der LoginActivity?
		String loginName = getIntent().getStringExtra(Constants.keyLoginName);
		String password = getIntent().getStringExtra(Constants.keyLoginPassword);
		
		if(loginName != null || password != null){
			Login login = new Login(loginName, password);
			//Login[] lArr = {login};
			
			if(!login.validateUser()){
				Toast.makeText(this, "Login fehlgeschlagen. Ist das eingegebene Passwort korrekt?", 20).show();
				updateUiNoUser();
				Log.i("Main", "login ("+login.getLoginname()+", "+login.getPassword()+") could not be validated!");
			} else {
				new UpdateDatabase().execute(login);
				user = login.getUser();
				updateUiWithUser();
			}
			return;
		}
		
	}

	/**
	 * Sends an intent if orderButton is clicked.
	 * Starts OrderActivity which shows the orderlist  
	 * @param  view The clicked View.
	 */
	public void orderButtonClicked(View view){
		Intent intent = null;
		if(user != null){
			intent = new Intent(this, OrderActivity.class);
			intent.putExtra(Constants.keyUser, user.getId());
			Log.i("MainActivity", "Order Activity Intent created, UserId: "+intent.getIntExtra(Constants.keyUser, 0));
		}
		else{
			intent = new Intent(this, LoginActivity.class);
		}
		startActivity(intent);
	}
	
	/**
	 * Sends an intent if loginButton is clicked.
	 * Starts LoginActivity lets the User login to the DB  
	 * @param  view The clicked View.
	 */
	public void logoutButtonClicked(View view){
		Intent intent = new Intent(this, LoginActivity.class);
		try {
			DatabaseManager.getHelper().getOpenStateDao().deleteById(1);
		} catch (SQLException e) {
			e.printStackTrace();
		}
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
	
	private void updateUiWithUser(){
		// Do stuff to update the UI
		TextView welcomeTextView = (TextView) findViewById(R.id.welcomeTextView);
		welcomeTextView.setText(welcomeTextView.getText()+user.getFirstName()+" "+user.getLastName()+"!");
	}
	
	private void updateUiNoUser() {

		TextView welcomeTextView = (TextView) findViewById(R.id.welcomeTextView);
		welcomeTextView.setText(welcomeTextView.getText()+" sie sind nicht eingeloggt.");
	}
	
}
