package de.bosshammersch_hof.oekokiste;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class ArticleDetailActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_article_detail);
		
		String name = getIntent().getStringExtra(OrderDetailActivity.ARTICLE_NAME_KEY);
		
		setTitle(name);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.article_detail, menu);
		return true;
	}

}
