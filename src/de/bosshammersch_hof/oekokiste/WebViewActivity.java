package de.bosshammersch_hof.oekokiste;

import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.view.MenuItem;
import android.webkit.WebView;

public class WebViewActivity extends Activity{
	
	/** 
	 *   creats a webview to show a pdf in the app
	 *   @param Bundle saved Instance State
	 */
	@SuppressLint("SetJavaScriptEnabled")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		String url = getIntent().getStringExtra(Constants.keyUrl);
		
		WebView mWebView=new WebView(WebViewActivity.this);
	    mWebView.loadUrl(url);
	    //mWebView.getSettings().setJavaScriptEnabled(true);
	    setContentView(mWebView);
		
	    getActionBar().setHomeButtonEnabled(true);
	}
	
	/**
	 *   if the app icon in action bar is clicked => go home
	 *   else the super constructor of the function is called
	 *   @param MenuItem which was selected
	 *   @return boolean 
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	        case android.R.id.home:
	            // app icon in action bar clicked; go home
	            Intent intent = new Intent(this, MainActivity.class);
	            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
	            startActivity(intent);
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
}
