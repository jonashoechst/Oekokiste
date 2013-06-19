package de.bosshammersch_hof.oekokiste;

import java.io.InputStream;
import java.net.URL;
import java.sql.SQLException;

import de.bosshammersch_hof.oekokiste.model.*;
import de.bosshammersch_hof.oekokiste.ormlite.*;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


public class ArticleDetailActivity extends Activity implements RefreshableActivity, ImageUpdatable{
	
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
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		Constants.refreshableActivity = this;
		refreshData();
	}


	@Override
	public void refreshData() {
		
		// 1. Möglichkeit: OrderedArticle wird übergeben
		
		try {
			int orderedArticleId = getIntent().getIntExtra(Constants.keyOrderedArticle, 0);
			orderedArticle = DatabaseManager.getHelper().getOrderedArticleDao().queryForId(orderedArticleId);
		} catch (SQLException e) {
			Log.e("Artikel Details", "kein bestellter Artikel gefunden...");
			e.printStackTrace();
		}

		if( orderedArticle != null ) {
			article = orderedArticle.getArticle();
			updateUi();
			return;
		}
		
		// 2. Möglichkeit: Aricle wird übergeben
		
		try {
			int articleId = getIntent().getIntExtra(Constants.keyArticleId, 0);
			article = DatabaseManager.getHelper().getArticleDao().queryForId(articleId);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		if(article != null) {
			updateUi();
			return;
		}
		Log.e("Artikel Details:", "Weder ein Bestellter, noch ein normaler Artikel konnten gefunden werden.");
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
		
		if(orderedArticle!=null){
			TextView oldPriceTextView = (TextView) findViewById(R.id.oldPriceTextView);
			oldPriceTextView.setText(orderedArticle.getPrice()+"€");
		}
		
		articleDescriptionView.setText(article.getDescription());
	}
	
	public void findRecipeButtonClicked(View view){
		Intent intent = new Intent(this, RecipeActivity.class);
		String[] articleGroupNames = {article.getArticleGroup().getName()};
		
		intent.putExtra(Constants.keyArticleGroupNameArray, articleGroupNames);
		startActivity(intent);
	}
	
	public void goToStoreButtonClicked(View view){
		Intent intent = new Intent(this, WebViewActivity.class);
		
		intent.putExtra(Constants.keyUrl, Constants.pathToArticleDescription+article.getId());
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

	@Override
	public void updateImage(Drawable d) {
		ImageView imageView = (ImageView) findViewById(R.id.recipeImageView);
		if( d != null) imageView.setImageDrawable(d);
	}
}
