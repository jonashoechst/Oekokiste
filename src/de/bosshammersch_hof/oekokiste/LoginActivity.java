package de.bosshammersch_hof.oekokiste;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.EditText;

public class LoginActivity extends Activity {
	/** 
	 *   creats the login-view
	 *   @param Bundle saved Instance State
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		
		Constants.refreshableActivity = null;
	}
	
	/** 
	 *   set extra data to the intent to the mainActivity
	 *   @param View 
	 */
	public void loginButtonClicked(View view){
		EditText loginName = (EditText) findViewById(R.id.editText1);
		EditText password = (EditText) findViewById(R.id.editText2);
		
		Intent intent = new Intent(this, MainActivity.class);
		intent.putExtra(Constants.keyLoginName, loginName.getText().toString());
		intent.putExtra(Constants.keyLoginPassword, password.getText().toString());
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
	}

}
