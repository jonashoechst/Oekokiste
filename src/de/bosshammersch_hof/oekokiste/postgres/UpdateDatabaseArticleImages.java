package de.bosshammersch_hof.oekokiste.postgres;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import de.bosshammersch_hof.oekokiste.Constants;
import de.bosshammersch_hof.oekokiste.model.*;
import de.bosshammersch_hof.oekokiste.ormlite.DatabaseManager;
import android.os.AsyncTask;
import android.util.Log;

public class UpdateDatabaseArticleImages extends AsyncTask<Void, Integer, Void> {
	
	/**
	 * Alle Daten werden synchronisiert.
	 */
	@Override
	public Void doInBackground(Void... params) {

		Log.i("Ökokiste: Update Database (Article Images)", "Article Images update initiated");
		try {
			updateImagesForAllArticles();
			Log.i("Ökokiste: Update Database (Article Images)", "Article Images updated.");
		} catch (SQLException e) {
			Log.i("Ökokiste: Update Database (Article Images)", "Article Images could not be updated.");
			e.printStackTrace();
		}
		
		return null;
	}
	
	/**
	 * Automatische aktualisierung.
	 */
	protected void onProgressUpdate(Integer... progress){
		if(Constants.refreshableActivity != null) {
			Log.i("Ökokiste: UpdateDatabase", "Updating View...");
			Constants.refreshableActivity.refreshData();
		}
	}
	
	private void updateImagesForAllArticles() throws SQLException{
		for(Article a : DatabaseManager.getHelper().getArticleDao().queryForAll())
			updateImageForArticle(a);
	}
	
	private void updateImageForArticle(Article article) throws SQLException{
		
		try {
			List<String> html = executeHttpGet(Constants.pathToArticleDescription + article.getId());
			LinkedList<String> possibleLinks = new LinkedList<String>();
			
			Pattern pattern = Pattern.compile("\"[^\"]*.jpg");
			for(String line : html){
				Matcher m = pattern.matcher(line);
				while(m.find()) {
					String possibleLink = m.group().substring(1, m.group().length());
					possibleLinks.add(possibleLink);
				}
			}
			
			if(possibleLinks.size() > 0) article.setImageUrl(possibleLinks.get(0));
			else article.setImageUrl("");
		} catch (Exception e) {
			Log.i("UpdataDatabase", "Couldn't get Sites html");
			e.printStackTrace();
			article.setImageUrl("");
		}
		article.createOrUpdate();
	}
	
	
	/**
	 * Helper Methods: gets the source of an html file
	 */
	public List<String> executeHttpGet(String urlString) {
		List<String> lines = new LinkedList<String>();
		
		try {
			URL url = new URL(urlString);
        	URLConnection connection = url.openConnection();
        	BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));

        	String inputLine;

        	while ((inputLine = in.readLine()) != null) 
            	lines.add(inputLine);

        	in.close();

		} catch(Exception e) {
			Log.e("httpGet", "Could not load Html.");
			e.printStackTrace();
		}
    	return lines;
    }
}