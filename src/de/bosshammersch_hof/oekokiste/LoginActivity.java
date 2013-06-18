package de.bosshammersch_hof.oekokiste;

import java.sql.SQLException;

import de.bosshammersch_hof.oekokiste.model.OpenState;
import de.bosshammersch_hof.oekokiste.ormlite.DatabaseManager;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;

public class LoginActivity extends Activity {
	/** 
	 *   creats the login-view
	 *   @param Bundle saved Instance State
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		
	}
	
	/** 
	 *   set extra data to the intent to the mainActivity
	 *   @param View 
	 */
	public void loginButtonClicked(View view){
		Intent intent = new Intent(this, MainActivity.class);
		intent.putExtra(Constants.keyLoginName, "a-dur1990");
		intent.putExtra(Constants.keyLoginPassword, "c98fa615f3eb3aa13aab4d607bb03deaedee9c254409ea6929661b1905dcb260");
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
	}

}
