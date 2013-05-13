package de.bosshammersch_hof.oekokiste;

import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.webkit.WebView;

public class WebPDFViewActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//setContentView(R.layout.activity_web_pdfview);
		
		Uri uri = getIntent().getData();
		
		
		WebView mWebView=new WebView(WebPDFViewActivity.this);
	    mWebView.getSettings().setJavaScriptEnabled(true);
	    mWebView.getSettings().setPluginsEnabled(true);
	    mWebView.loadUrl("https://docs.google.com/gview?embedded=true&url="+uri);
	    setContentView(mWebView);
		
		
	}

}
