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
		OpenState os = new OpenState();
		os.setLastUserId(8893);
		os.setId(1);
		try {
			DatabaseManager.getHelper().getOpenStateDao().createOrUpdate(os);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		startActivity(intent);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.login, menu);
		return true;
	}

}
