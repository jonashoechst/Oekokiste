package de.bosshammersch_hof.oekokiste;

import java.sql.SQLException;
import java.util.List;

import jim.h.common.android.zxinglib.integrator.IntentIntegrator;
import jim.h.common.android.zxinglib.integrator.IntentResult;

import de.bosshammersch_hof.oekokiste.model.*;
import de.bosshammersch_hof.oekokiste.ormlite.DatabaseManager;
import de.bosshammersch_hof.oekokiste.postgres.*;

import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	
	private String txtScanResult;
	
	Order order;
	
	User user;
	
	UpdateDatabase updater;
	
	/**
	 *   calls the super Constructor
	 *   and set the Contentview 
	 *   and checks the login state so the user can work without login again
	 *   @param  Bundle saved Instance State
	 */
	@SuppressLint("ShowToast")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		// Init the Databasemanager
		DatabaseManager.init(this);
		
		Constants.refreshableActivity = null;

		updater = new UpdateDatabase();
		
		// 1. Versuch: letzten Status öffnen
		OpenState lastOpenState = null;
		
		try {
			lastOpenState = DatabaseManager.getHelper().getOpenStateDao().queryForId(1);
			
			if(lastOpenState != null) {
				user = lastOpenState.getUser();
				//updater.execute(user);
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
			// Creating a loginuser
			User loginUser = new User();
			loginUser.setLoginName(loginName);
			loginUser.setPassword(password);
			
			// validate the user
			try {
				loginUser = updater.validateUser(loginUser);
			} catch (SQLException e) {
				loginUser = null;
				Toast.makeText(this, "Login fehlgeschlagen. Die Datenbankverbindung konnte nicht aufgebaut werden.", 30).show();
				updateUiNoUser();
				return;
			}
			
			if(loginUser == null){
				// User could not be validated
				Toast.makeText(this, "Login fehlgeschlagen. Ist das eingegebene Passwort korrekt?", 30).show();
				updateUiNoUser();
				return;
			} else {
				// Login erfolgreich!
				user = loginUser;
				updater.execute(loginUser);
				updateUiWithUser();
				OpenState os = new OpenState();
				os.setUser(loginUser);
				try {
					os.create();
				} catch (SQLException e) {
					Log.e("MainActivity", "Open State could not be saved.");
					e.printStackTrace();
				}
				return;
			}
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
	
	public void findRecipesClicked(View view){
		Intent intent = new Intent(this, FindRecipesByArticleActivity.class);
		
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
		
		setScanButton();
		
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
		
		setScanButton();
		
		Button logoutButton = (Button) findViewById(R.id.logoutButton);
		logoutButton.setText("Anmelden...");
		
		TextView welcomeTextView = (TextView) findViewById(R.id.welcomeTextView);
		welcomeTextView.setText(welcomeTextView.getText()+" sie sind nicht eingeloggt.");
	}
	
	private void setScanButton(){
		View scanButton = findViewById(R.id.scan_button);
		
		scanButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // set the last parameter to true to open front light if available
                IntentIntegrator.initiateScan(MainActivity.this, R.layout.activity_scan_bar_code,
                        R.id.viewfinder_view, R.id.preview_view, true);
            }
        });
	}
	
	/**
	 * Parsed den gescannten Code und gibt die gefundene Order der OrderActivity zu anzeigen.
	 */
	@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == IntentIntegrator.REQUEST_CODE){
        	IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode,
                    resultCode, data);
            if (scanResult == null) {
                return;
            }
            final String result = scanResult.getContents();
            if (result != null) {
            	txtScanResult = buildCode(result);
            	Log.i("Barcode", txtScanResult);
            	try{
            		List<Barcode> bl = DatabaseManager.getHelper().getBarcodeDao().queryForAll();
            		
            		for(Barcode b : bl){
            			Log.i("Barcode", b.getBarcodeString());
            		}
            		
            		Barcode barcode = DatabaseManager.getHelper().getBarcodeDao().queryForId(txtScanResult);
            		Log.i("Barcode", barcode.getBarcodeString());
            		
                	order = DatabaseManager.getHelper().getOrderDao().queryForId(barcode.getOrder().getId());
            	} catch (SQLException e){
            		Log.e("OrderDetailActivity: ", "Kein Barcode gefunden.");
            	}
            	
                Intent intent = new Intent(this, OrderDetailActivity.class);
                intent.putExtra(Constants.keyOrder, order.getId());
        		startActivity(intent);
            }
        }
    }
	
	/**
	 * Generiert zu einem 13 Zeichen langen EAN-Code den Kistencode.
	 * @param str
	 * @return Kistencode
	 */
	private String buildCode(String str){
		String result;
	    char ascii = (char) Integer.parseInt(str.substring(7, 9));
	    result = ascii+"-"+str.substring(9, 12);
	    return result;
	}
	
}
