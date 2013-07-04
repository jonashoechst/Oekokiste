package de.bosshammersch_hof.oekokiste;

import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import de.bosshammersch_hof.oekokiste.model.*;
import de.bosshammersch_hof.oekokiste.ormlite.*;
import de.bosshammersch_hof.oekokiste.webiste.ImageFromURL;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


public class ArticleDetailActivity extends Activity implements RefreshableActivity, ImageUpdatable{
	
	private OrderedArticle orderedArticle;
	private Article article;
	private ImageFromURL imageUpdater;
	private Drawable image = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_article_detail);
		getActionBar().setHomeButtonEnabled(true);

		imageUpdater = new ImageFromURL();
		imageUpdater.updateClass = this;
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		Constants.refreshableActivity = this;
		refreshData();
	}

	/**
	 * Daten werden akutalisiert.
	 */
	@Override
	public void refreshData() {
		
		article = null;
		orderedArticle = null;
		
		int articleId = getIntent().getIntExtra(Constants.keyArticleId, 0);
		int orderId = getIntent().getIntExtra(Constants.keyOrderId, 0);
		
		try {
			article = DatabaseManager.getHelper().getArticleDao().queryForId(articleId);
		} catch (SQLException e) {
			article = null;
		}
		
		if(article == null){
			// Print an Error message
						AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(this);
						dlgAlert.setMessage("Die Ansicht konnte nicht geladen werden.");
						dlgAlert.setTitle("Ökokiste");
						dlgAlert.setPositiveButton("Zurück", 
							new DialogInterface.OnClickListener() {
					        	public void onClick(DialogInterface dialog, int which) {
					        		finish();
					        	}
							});
						dlgAlert.setCancelable(false);
						dlgAlert.create().show();
		}
		
		
		Order order;
		try {
			order = DatabaseManager.getHelper().getOrderDao().queryForId(orderId);
		} catch (SQLException e1) {
			order = null;
		}
		if(order != null){
			OrderedArticle orderedArticle = new OrderedArticle();
			orderedArticle.setOrder(order);
			orderedArticle.setArticle(article);
		
			List<OrderedArticle> matchingOrderedArticles;
		
			try {
				matchingOrderedArticles = DatabaseManager.getHelper().getOrderedArticleDao().queryForMatching(orderedArticle);
			} catch (SQLException e) {
				matchingOrderedArticles = null;
			}
		
			if(matchingOrderedArticles != null && matchingOrderedArticles.size() > 0)
				orderedArticle = matchingOrderedArticles.get(0);
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
		
		TextView textViewOrigin = (TextView) findViewById(R.id.textViewOrigin);
		textViewOrigin.setText(article.getOrigin());
		
		TextView oldPriceTextView = (TextView) findViewById(R.id.oldPriceTextView);
		if(orderedArticle!=null){
			oldPriceTextView.setText(orderedArticle.getPrice()+"€");
		} else {
			oldPriceTextView.setText("");
		}

		// Download Image if:
					// 1. there is no image yet
					// 2. there is no imageUpdater running.
					// 3. there is a valid link
		if(	image == null && 
			imageUpdater.getStatus() != AsyncTask.Status.RUNNING && 
			article.getImageUrl() != null && 
			!article.getImageUrl().equals("")){
			
			imageUpdater.execute(article.getImageUrl());
			imageUpdater.updateClass = this;
		} else if (image != null)
			updateImage(image);
		
	}
	
	public void findRecipeButtonClicked(View view){
		// Dialog: Haupt oder Nebenzutat
		
		AlertDialog.Builder b = new AlertDialog.Builder(ArticleDetailActivity.this);
		
		b.setTitle("Soll der Artikel als Hauptzutat oder auch als Nebenzutat auftauchen?");
		b.setItems(R.array.select_dialog_items, new DialogInterface.OnClickListener(){
			public void onClick(DialogInterface dialog, int i) {
				
				boolean onlyMainIngrediants = false;
				if(i == 1) onlyMainIngrediants = true;
				
				List<ArticleGroup> articleGroups = new LinkedList<ArticleGroup>();
				articleGroups.add(article.getArticleGroup());
				
				List<Recipe.RecipeWithHits> recipesWithHits;
				try {
					recipesWithHits = Recipe.findRecipesByArticleGroups(articleGroups, onlyMainIngrediants);
				} catch (SQLException e) {
					recipesWithHits = new LinkedList<Recipe.RecipeWithHits>();
				}
    			
				Intent intent = new Intent(ArticleDetailActivity.this, RecipeActivity.class);
    			intent.putExtra(Constants.keyRecipeIdArray, Recipe.RecipeWithHits.getRecipeIdArray(recipesWithHits));
    			intent.putExtra(Constants.keyRecipeHitsArray, Recipe.RecipeWithHits.getHitsArray(recipesWithHits));
				startActivity(intent);
			}
			
		});
		b.create().show();
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
	
	/**
	 * Bild wird nachgeladen.
	 * 
	 * @params Drawable d Das zu ladene Bild.
	 */
	@Override
	public void updateImage(Drawable d) {
		ImageView imageView = (ImageView) findViewById(R.id.articleImageView);
		if( d != null) {
			imageView.setImageDrawable(d);
			image = d;
		}
	}

	@Override
	protected void onDestroy(){
		super.onDestroy();
		imageUpdater.cancel(true);
	}
}
