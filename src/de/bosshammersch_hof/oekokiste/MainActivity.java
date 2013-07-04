package de.bosshammersch_hof.oekokiste;

import java.sql.SQLException;

import jim.h.common.android.zxinglib.integrator.IntentIntegrator;
import jim.h.common.android.zxinglib.integrator.IntentResult;

import de.bosshammersch_hof.oekokiste.model.*;
import de.bosshammersch_hof.oekokiste.ormlite.DatabaseManager;
import de.bosshammersch_hof.oekokiste.postgres.*;

import android.os.AsyncTask;
import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	
	private String txtScanResult;
	
	User user;
	
	UpdateDatabaseGeneral updaterGeneral;
	UpdateDatabaseUser updaterUser;
	UpdateDatabaseArticleImages updaterImages;
	
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

		updaterGeneral = new UpdateDatabaseGeneral();
		updaterUser = new UpdateDatabaseUser();
		updaterImages = new UpdateDatabaseArticleImages();
		
		if (!updaterGeneral.getStatus().equals(AsyncTask.Status.RUNNING))
			updaterGeneral.execute();
		
		// 1. Versuch: letzten Status öffnen
		OpenState lastOpenState = null;
		
		try {
			lastOpenState = DatabaseManager.getHelper().getOpenStateDao().queryForId(1);
			
			if (lastOpenState != null) {
				user = lastOpenState.getUser();
				if (!updaterUser.getStatus().equals(AsyncTask.Status.RUNNING))
					updaterUser.execute(user);
				updateUiWithUser();
				if (!updaterImages.getStatus().equals(AsyncTask.Status.RUNNING))
					updaterImages.execute();
				return;
			}
		} catch (SQLException e) {
			Log.e("Ökokiste: Main Actitvity","SQL Exception finding the last OpenState");
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
				loginUser = UpdateDatabaseUser.validateUser(loginUser);
			} catch (SQLException e) {
				loginUser = null;
			}
			
			if(loginUser == null){
				// User could not be validated
				Toast.makeText(this, "Login fehlgeschlagen. Ist das eingegebene Passwort korrekt?", 30).show();
				updateUiNoUser();
			} else {
				// Login success!
				user = loginUser;
				updaterUser.execute(loginUser);
				updateUiWithUser();
				OpenState os = new OpenState();
				os.setUser(loginUser);
				try {
					os.create();
				} catch (SQLException e) {
					Log.e("MainActivity", "Open State could not be saved.");
				}
			}
		} else {
			updateUiNoUser();
		}
		if (!updaterImages.getStatus().equals(AsyncTask.Status.RUNNING))
			updaterImages.execute();
		
	}

	/**
	 * Sends an intent if orderButton is clicked.
	 * Starts OrderActivity which shows the orderlist  
	 * @param  view The clicked View.
	 */
	public void orderButtonClicked(View view){
		Intent intent = new Intent(this, OrderActivity.class);
		intent.putExtra(Constants.keyUserId, user.getId());
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
			updaterUser.cancel(true);
			user = null;
			DatabaseManager.clearUserData();
		} catch (SQLException e) {
			Log.e("Ökokiste: MainActivity", "User data could not be deleted.");
			e.printStackTrace();
		}
		updateUiNoUser();
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
		welcomeTextView.setText(getResources().getString(R.string.mainActivity_welcome)+" "+user.getFirstName()+" "+user.getLastName()+"!");
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
		welcomeTextView.setText(getResources().getString(R.string.mainActivity_welcome)+" sie sind nicht eingeloggt.");
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) throws NullPointerException {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == IntentIntegrator.REQUEST_CODE){
        	IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode,
                    resultCode, data);
            if (scanResult == null || scanResult.getContents().length() != 13) {
            	Log.e("Main Activity", "Could not find barcode or barcode has invalid barcode length!");
            	
            	// Print an Error message
    			AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(this);
    			dlgAlert.setMessage("Barcode ist nicht von der Oekokiste!");
    			dlgAlert.setTitle("Ökokiste");
    			dlgAlert.setPositiveButton("Zurück", 
    				new DialogInterface.OnClickListener() {
    		        	public void onClick(DialogInterface dialog, int which) {
    		        		return;
    		        	}
    				}
    			);
    			dlgAlert.setCancelable(true);
    			dlgAlert.create().show();
            	return;
            	
            }
            final String result = scanResult.getContents();
            if (result != null) {
            	txtScanResult = buildCode(result);
            	Log.i("Barcode", txtScanResult);
            	
            	try {
					int orderId = new UpdateDatabaseOrder().updateBarcodeForBarcodeStringGetOrderId(txtScanResult);
					Intent intent = new Intent(this, OrderDetailActivity.class);
	                intent.putExtra(Constants.keyOrderId, orderId);
	        		startActivity(intent);
				} catch (SQLException e) {
					
					AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(this);
	    			dlgAlert.setMessage("Es liegen uns keine Bestellungen zu diesem Barcode vor!");
	    			dlgAlert.setTitle("Ökokiste");
	    			dlgAlert.setPositiveButton("Zurück", 
	    				new DialogInterface.OnClickListener() {
	    		        	public void onClick(DialogInterface dialog, int which) {
	    		        		return;
	    		        	}
	    				}
	    			);
	    			dlgAlert.setCancelable(true);
	    			dlgAlert.create().show();
	            	
					Log.e("Main Activity", "Could not find Order for Barcode");
					e.printStackTrace();
				}
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
	
	@Override
	protected void onDestroy(){
		super.onDestroy();
		updaterGeneral.cancel(true);
		updaterUser.cancel(true);
	}
	
}
