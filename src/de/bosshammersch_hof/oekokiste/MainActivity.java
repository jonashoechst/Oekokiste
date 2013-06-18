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
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	
	User user;
	
	/**
	 *   calls the super Constructor
	 *   and set the Contentview 
	 *   and checks the login state so the user can work without login again
	 *   @param  Bundle saved Instance State
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		
		// Init the Databasemanager
		DatabaseManager.init(this);

		// 1. Versuch: letzten Status �ffnen
		OpenState lastOpenState = null;
		
		UpdateDatabase updater = new UpdateDatabase();
		
		try {
			lastOpenState = DatabaseManager.getHelper().getOpenStateDao().queryForId(1);
			
			if(lastOpenState != null) {
				Log.i("Main Activity", "last Open State found.");
				user = lastOpenState.getUser();
				updater.execute(user);
				updateUiWithUser();
				return;
			}
		} catch (SQLException e) {
			Log.i("Oekokiste: Main Actitvity","SQL Exception finding the last OpenState");
			e.printStackTrace();
		}

		// 2. Versuch: Kommen wir von der LoginActivity?
		String loginName = getIntent().getStringExtra(Constants.keyLoginName);
		String password = getIntent().getStringExtra(Constants.keyLoginPassword);
		
		if(loginName != null || password != null){
			User loginUser = new User();
			loginUser.setLoginName(loginName);
			loginUser.setPassword(password);
			//Login[] lArr = {login};
			
			try {
				loginUser = updater.validateUser(loginUser);
				
			} catch (SQLException e) {
				loginUser = null;
				e.printStackTrace();
			}
			
			if(loginUser == null){
				Toast.makeText(this, "Login fehlgeschlagen. Ist das eingegebene Passwort korrekt?", 20).show();
				updateUiNoUser();
			} else {
				updater.execute(loginUser);
				user = loginUser;
				updateUiWithUser();
				OpenState os = new OpenState();
				os.setUser(loginUser);
				try {
					os.create();
				} catch (SQLException e) {
					Log.e("MainActivity", "Open State could not be saved.");
					e.printStackTrace();
				}
			}
			return;
		} else {
			updateUiNoUser();
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
			user = null;
			DatabaseManager.clearUserData();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
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
	
	/**
	 *  update the Ui 
	 *  with an User 
	 */
	private void updateUiWithUser(){
		// Do stuff to update the UI
		Button orderButton = (Button) findViewById(R.id.orderButton);
		orderButton.setEnabled(true);
		
		Button logoutButton = (Button) findViewById(R.id.logoutButton);
		logoutButton.setText("Abmelden...");
		
		TextView welcomeTextView = (TextView) findViewById(R.id.welcomeTextView);
		welcomeTextView.setText(welcomeTextView.getText()+user.getFirstName()+" "+user.getLastName()+"!");
	}
	
	/**
	 *  update the Ui 
	 *  with no User 
	 */
	private void updateUiNoUser() {
		Button orderButton = (Button) findViewById(R.id.orderButton);
		orderButton.setEnabled(false);
		
		Button logoutButton = (Button) findViewById(R.id.logoutButton);
		logoutButton.setText("Anmelden...");
		
		TextView welcomeTextView = (TextView) findViewById(R.id.welcomeTextView);
		welcomeTextView.setText(welcomeTextView.getText()+" sie sind nicht eingeloggt.");
	}
	
}
