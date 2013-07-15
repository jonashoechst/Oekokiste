package de.bosshammersch_hof.oekokiste.postgres;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import com.j256.ormlite.misc.BaseDaoEnabled;
import de.bosshammersch_hof.oekokiste.Constants;
import de.bosshammersch_hof.oekokiste.model.*;
import de.bosshammersch_hof.oekokiste.ormlite.DatabaseManager;
import android.os.AsyncTask;
import android.util.Log;

public class UpdateDatabaseGeneral extends AsyncTask<Void, Integer, Void> {
	
	private static Connection connection;

	/**
	 * Alle Daten werden synchronisiert.
	 */
	@Override
	public Void doInBackground(Void... params) {

		try {
			connection = DatabaseConnection.getAConnection();
			
			Log.i("Ökokiste: Update Database (General)", "General Data sync initiated");
			try {
				updateGeneralData();
				Log.i("Ökokiste: Update Database (General)", "General data was synced.");
			} catch (SQLException e) {
				Log.e("Ökokiste: Update Database (General)", "Could not sync general Data.");
				e.printStackTrace();
			}
			
			connection.close();
			
		} catch (SQLException e) {
			Log.e("Ökokiste: Update Database (General)", "Datenbank Verbindung konnte nicht augebaut werden.");
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			Log.e("Ökokiste: Update Database (General)", "Postgresql Treiber wurde nicht gefunden.");
			e.printStackTrace();
		}
		
		return null;
	}
	
	/**
	 * General Dataupdates.
	 * @throws SQLException
	 */
	private void updateGeneralData() throws SQLException{
		updateCategories();
		updateRecipes();
		updateCookware();
		updateArticleGroups();
		updateCookingArticles();
		updateArticles();
	}

	/**
	 * Kategorien werden aktualisiert.
	 * @throws SQLException
	 */
	private void updateCategories() throws SQLException{
		
		List<CreateOrUpdateable> toUpdate = new LinkedList<CreateOrUpdateable>();
		
		PreparedStatement pst = connection.prepareStatement("select * from category");
		ResultSet rs = pst.executeQuery();
		while(rs.next()){
			Category c = new Category();
			c.setName(rs.getString("category_name"));
			toUpdate.add(c);
		}
		
		for(BaseDaoEnabled<?,?> obj : DatabaseManager.getHelper().getCategoryDao().queryForAll())
			obj.delete();
		
		for(CreateOrUpdateable obj : toUpdate)
			obj.createOrUpdate();
		
		this.publishProgress(0);
	}
	
	/**
	 * Rezepte werden aktualisiert.
	 * @throws SQLException
	 */
	private void updateRecipes() throws SQLException{

		List<CreateOrUpdateable> toUpdate = new LinkedList<CreateOrUpdateable>();
		
		PreparedStatement pst = connection.prepareStatement("select * from recipes");
		ResultSet rs = pst.executeQuery();
		while(rs.next()){
			Recipe r = new Recipe();

			r.setId(rs.getInt("recipe_id"));
			r.setName(rs.getString("recipe_name"));
			r.setInstructions(rs.getString("recipe_instructions"));
			r.setDescription(rs.getString("recipe_desc"));
			r.setDifficulty(rs.getInt("recipe_difficulty"));
			r.setCookingTimeInMin(rs.getInt("recipe_timeinmin"));
			r.setServings(rs.getInt("recipe_servings"));
			r.setImagerUrl(rs.getString("recipe_image_url"));
			toUpdate.add(r);
		}
		
		for(BaseDaoEnabled<?,?> obj : DatabaseManager.getHelper().getRecipeDao().queryForAll())
			obj.delete();
		
		for(CreateOrUpdateable obj : toUpdate)
			obj.createOrUpdate();
		
		this.publishProgress(0);
	}
	
	/**
	 * Cookware wird aktualisiert.
	 * @throws SQLException
	 */
	private void updateCookware() throws SQLException{

		List<CreateOrUpdateable> toUpdate = new LinkedList<CreateOrUpdateable>();
		
		PreparedStatement pst = connection.prepareStatement("select * from cookware");
		ResultSet rs = pst.executeQuery();
		
		while(rs.next()){
			Cookware c = new Cookware();
			c.setName(rs.getString("cookware_name"));
			c.setRecipe(DatabaseManager.getHelper().getRecipeDao().queryForId(rs.getInt("recipe_id")));

			toUpdate.add(c);
		}
		
		for(BaseDaoEnabled<?,?> obj : DatabaseManager.getHelper().getCookwareDao().queryForAll())
			obj.delete();
		
		for(CreateOrUpdateable obj : toUpdate)
			obj.createOrUpdate();
		
		this.publishProgress(0);
	}
	
	/**
	 * ArticleGroups werden aktualisiert.
	 * @throws SQLException
	 */
	private void updateArticleGroups() throws SQLException{

		List<CreateOrUpdateable> toUpdate = new LinkedList<CreateOrUpdateable>();
		
		PreparedStatement pst = connection.prepareStatement("select * from articlegroup");
		ResultSet rs = pst.executeQuery();
		while (rs.next()){
			ArticleGroup ag = new ArticleGroup();
			ag.setName(rs.getString("articlegroup_name"));
			toUpdate.add(ag);
		}
		
		for(BaseDaoEnabled<?,?> obj : DatabaseManager.getHelper().getArticleGroupDao().queryForAll())
			obj.delete();
		
		for(CreateOrUpdateable obj : toUpdate)
			obj.createOrUpdate();
		this.publishProgress(0);
	}

	/**
	 * CookingArticles werden aktualisiert.
	 * @throws SQLException
	 */
	private void updateCookingArticles() throws SQLException {
		
		List<CreateOrUpdateable> toUpdate = new LinkedList<CreateOrUpdateable>();
		
		List<CookingArticle> caList = DatabaseManager.getHelper().getCookingArticleDao().queryForAll();
		
		for(CookingArticle ca : caList)
			ca.delete();
		
		PreparedStatement pst = connection.prepareStatement("select * from ingredients");
		ResultSet rs = pst.executeQuery();
		
		while (rs.next()){
			CookingArticle ca = new CookingArticle();
			ca.setAmount(rs.getDouble("amount"));
			ca.setAmountType(rs.getString("amount_type"));
			ca.setPrimaryIngredient(rs.getBoolean("is_primary_ingredient"));
			ca.setStandartIngredient(rs.getBoolean("is_standart_ingredient"));
			ca.setArticleGroup(DatabaseManager.getHelper().getArticleGroupDao().queryForId(rs.getString("articlegroup_name")));
			ca.setRecipe(DatabaseManager.getHelper().getRecipeDao().queryForId(rs.getInt("recipe_id")));
			toUpdate.add(ca);
		}
		
		for(BaseDaoEnabled<?,?> obj : DatabaseManager.getHelper().getCookingArticleDao().queryForAll())
			obj.delete();
		
		for(CreateOrUpdateable obj : toUpdate)
			obj.createOrUpdate();
		this.publishProgress(0);
	}
	
	/**
	 * Articles werden aktualisiert.
	 * @throws SQLException
	 */
	private void updateArticles() throws SQLException {

		List<CreateOrUpdateable> toUpdate = new LinkedList<CreateOrUpdateable>();
		
		PreparedStatement pst = connection.prepareStatement("select * from article");
		ResultSet rs = pst.executeQuery();
		while(rs.next()){
			Article a = new Article();
			a.setDescription(rs.getString("article_description"));
			a.setId(rs.getInt("article_id"));
			a.setName(rs.getString("article_name"));
			a.setOrigin(rs.getString("article_origin"));
			a.setArticleGroup(DatabaseManager.getHelper().getArticleGroupDao().queryForId(rs.getString("articlegroup_name")));
			a.setCategory(DatabaseManager.getHelper().getCategoryDao().queryForId(rs.getString("category_name")));
			toUpdate.add(a);
		}
		for(BaseDaoEnabled<?,?> obj : DatabaseManager.getHelper().getArticleDao().queryForAll())
			obj.delete();
		
		for(CreateOrUpdateable obj : toUpdate)
			obj.createOrUpdate();
		
		this.publishProgress(0);
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
}