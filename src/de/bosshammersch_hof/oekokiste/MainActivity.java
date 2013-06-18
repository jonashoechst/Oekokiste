package de.bosshammersch_hof.oekokiste;

import java.sql.SQLException;

import de.bosshammersch_hof.oekokiste.model.*;
import de.bosshammersch_hof.oekokiste.ormlite.DatabaseManager;
import de.bosshammersch_hof.oekokiste.postgres.*;

import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	
	User user;
	
	UpdateDatabase updater;
	
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
		updater = new UpdateDatabase();
		
		
		Constants.refreshableActivity = null;

		
	}
	
	@SuppressLint("ShowToast")
	public void onResume(){
		super.onResume();

		// 1. Versuch: letzten Status �ffnen
		OpenState lastOpenState = null;
		
		try {
			lastOpenState = DatabaseManager.getHelper().getOpenStateDao().queryForId(1);
			
			if(lastOpenState != null) {
				user = lastOpenState.getUser();
				updater.execute(user);
				updateUiWithUser();
				return;
			}
		} catch (SQLException e) {
			Log.e("Ökokiste: Main Actitvity","SQL Exception finding the last OpenState");
			e.printStackTrace();
		}

		// 2. Versuch: Kommen wir von der LoginActivity?
		String loginName = getIntent().getStringExtra(Constants.keyLoginName);
		String password = getIntent().getStringExtra(Constants.keyLoginPassword);
		
		if(loginName != null || password != null){
			User loginUser = new User();
			loginUser.setLoginName(loginName);
			loginUser.setPassword(password);
			
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
		welcomeTextView.setText(welcomeTextView.getText()+" "+user.getFirstName()+" "+user.getLastName()+"!");
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
