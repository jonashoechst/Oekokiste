package de.bosshammersch_hof.oekokiste;

import de.bosshammersch_hof.oekokiste.model.*;
import de.bosshammersch_hof.oekokiste.ormlite.*;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;


public class ArticleDetailActivity extends Activity implements RefreshableActivity{
	
	private OrderedArticle orderedArticle;
	
	private Article article;
	
	/** 
	 *   creats the detail-view for articles
	 *   @param Bundle saved Instance State
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_article_detail);
		getActionBar().setHomeButtonEnabled(true);
		
		refreshData();
		
	}


	@Override
	public void refreshData() {
		
		int orderedArticleId = getIntent().getIntExtra(Constants.keyOrderedArticle, 0);
		orderedArticle = DatabaseManager.getOrderedArticle(orderedArticleId);
		
		if(orderedArticle != null) article = orderedArticle.getArticle();
		else {
			int articleId = getIntent().getIntExtra(Constants.keyArticleId, 0);
			article = DatabaseManager.getArticle(articleId);
		}
		
		updateUi();
	}

	/**
	 *  update the Ui 
	 *  an set the title to the name of the articlename
	 */
	private void updateUi() {
		// Fill the Article Activity
		setTitle(article.getName());
		
		//ImageView articleImageView = (ImageView) findViewById(R.id.articleImageView);
		TextView articleDescriptionView = (TextView) findViewById(R.id.articleDescriptionView);
		
		articleDescriptionView.setText(article.getDescription());
	}
	
	public void findRecipeButtonClicked(View view){
		Intent intent = new Intent(this, RecipeActivity.class);
		String[] articleGroupNames = {article.getArticleGroup().getName()};
		
		intent.putExtra(Constants.keyArticleGroupNameArray, articleGroupNames);
		startActivity(intent);
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
	            Intent intent = new Intent(this, MainActivity.class);
	            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
	            startActivity(intent);
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
}
