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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		
	}
	
	public void loginButtonClicked(View view){
		Intent intent = new Intent(this, MainActivity.class);
		intent.putExtra(Constants.keyLoginName, "a-dur1990");
		intent.putExtra(Constants.keyLoginPassword, "c98fa615f3eb3aa13aab4d607bb03deaedee9c254409ea6929661b1905dcb260");
		// scheint nicht so zu funktionieren wie gedacht
		//OpenState tmpOpenState = new OpenState();
		//tmpOpenState.setLastUserId(8893);
		//DatabaseManager.saveOpenState(tmpOpenState);
		startActivity(intent);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.login, menu);
		return true;
	}

}
