package de.bosshammersch_hof.oekokiste;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}
	
	/**
	 * Sends an intent if orderButton is clicked.
	 * Starts OrderActivity which shows the orderlist  
	 *
	 * @param  view	The clicked View.
	 */
	public void orderButtonClicked(View view){
		Intent intent = new Intent(this, OrderActivity.class);
		startActivity(intent);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
}
