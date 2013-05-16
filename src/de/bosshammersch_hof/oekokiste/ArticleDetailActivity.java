package de.bosshammersch_hof.oekokiste;

import de.bosshammersch_hof.oekokiste.model.Article;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;

public class ArticleDetailActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_article_detail);
		
		Article article = getDummyArticle();
	
		setTitle(article.getName());
		
		getActionBar().setHomeButtonEnabled(true);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	        case android.R.id.home:
	            Intent intent = new Intent(this, MainActivity.class);
	            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
	            startActivity(intent);
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.article_detail, menu);
		return true;
	}
	
	private Article getDummyArticle(){
		
		String description = "Gouda stammt ursprünglich aus den Städten Stolwijk und Haastrecht, aus der Region Krimpenerwaard[2] südlich der gleichnamigen Stadt Gouda, im Westen der Niederlande. Seinen Namen verdankt er auch dieser, von deren Markt aus sich der Ruf dieses Käses in alle Welt verbreitet hat. Die erste urkundliche Erwähnung des Gouda-Käses findet sich bereits 1184.[3] Damit ist Gouda eine der ältesten schriftlich belegten Käsesorten, die bis in unsere Zeit hergestellt und gehandelt werden.";
		
		return new Article(0, "Gouda", description);
		
		
	}
	
}
