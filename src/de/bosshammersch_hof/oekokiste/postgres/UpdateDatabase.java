package de.bosshammersch_hof.oekokiste.postgres;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.misc.BaseDaoEnabled;
import de.bosshammersch_hof.oekokiste.Constants;
import de.bosshammersch_hof.oekokiste.model.*;
import de.bosshammersch_hof.oekokiste.ormlite.DatabaseManager;
import android.os.AsyncTask;
import android.util.Log;

public class UpdateDatabase extends AsyncTask<User, Integer, boolean[]> {
	
	private Connection connection = null;
	
	Dao<Article, Integer> articleDao = DatabaseManager.getHelper().getArticleDao();
	
	private boolean[] output;

	/**
	 * Alle Daten werden synchronisiert.
	 */
	@Override
	public boolean[] doInBackground(User... params) {
		
		DatabaseConnection con = new DatabaseConnection();
		connection = con.getConnection();
		Log.i("UpdataDatabase", "Sync initiated...");
		try {
			updateGeneralData();
			Log.i("UpdataDatabase", "General data was synced.");
		} catch (SQLException e) {
			Log.e("UpdateDatabase", "Could not sync general Data.");
			e.printStackTrace();
		}
		
		try {
			updateUserDataForId(params[0].getId());
			Log.i("UpdataDatabase", "User data was synced.");
		} catch (SQLException e) {
			Log.e("UpdateDatabase", "Could not sync user data.");
			e.printStackTrace();
		}

		Log.i("UpdateDatabase", "Sync finished!");
		con.disconnect();
		
		try {
			updateImagesForAllArticles();
			Log.i("UpdataDatabase", "Article Images updated.");
		} catch (SQLException e) {
			Log.i("UpdataDatabase", "Article Images could not be updated.");
			e.printStackTrace();
		}
		
		return output;
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
			// TODO Get a description from anywhere
			a.setDescription("");
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
	 * Nutzerdaten werden aktualisiert.
	 * @throws SQLException
	 */
	private void updateUserDataForId(int userId) throws SQLException{
		updateUserForId(userId);
		updateOrdersForUserId(userId);
		List<Order> orderList = DatabaseManager.getHelper().getOrderDao().queryForAll();
		List<Integer> orderIdList = new LinkedList<Integer>();
		
		for(Order o : orderList) orderIdList.add(o.getId());
		updateBarcodesForOrderIds(orderIdList);
		updateOrderedArticlesForOrderIds(orderIdList);
	}
	
	/**
	 *  Nutzer wird aktualisiert.
	 * @throws SQLException
	 */
	private User updateUserForId(int id) throws SQLException{
		
		User user = new User();

		PreparedStatement ps = connection.prepareStatement("select * from users where user_id = ?");
		ps.setInt(1, id);
		ResultSet rs = ps.executeQuery();
		
		rs.next();
		
		user.setFirstName(rs.getString("firstname"));
		user.setLastName(rs.getString("lastname"));
		user.setLoginName(rs.getString("loginname"));
		user.setId(rs.getInt("user_id"));
		
		user.createOrUpdate();

		this.publishProgress(0);
		
		return user;
	}
	
	/**
	 * Nutzerbestellungen werden aktualisiert.
	 * @throws SQLException
	 */
	private void updateOrdersForUserId(int userId) throws SQLException {
		
		List<CreateOrUpdateable> toUpdate = new LinkedList<CreateOrUpdateable>();
		
		PreparedStatement pst = connection.prepareStatement("select * from orders where user_id = ?");
		pst.setInt(1, userId);
		ResultSet rs = pst.executeQuery();
		while(rs.next()){
			Order o = new Order();
			
			o.setDate(rs.getDate("order_date"));
			o.setId(rs.getInt("order_id"));
			o.setName(rs.getString("order_name"));
			o.setUser(DatabaseManager.getHelper().getUserDao().queryForId(userId));
			toUpdate.add(o);
		}
		
		for(BaseDaoEnabled<?,?> obj : DatabaseManager.getHelper().getOrderDao().queryForAll())
			obj.delete();
		
		for(CreateOrUpdateable obj : toUpdate)
			obj.createOrUpdate();

		this.publishProgress(0);
	}
	
	/**
	 * Barcodes werden aktualisiert.
	 * @throws SQLException
	 */
	private void updateBarcodesForOrderIds(List<Integer> orderIds) throws SQLException {
		
		List<CreateOrUpdateable> toUpdate = new LinkedList<CreateOrUpdateable>();
		
		PreparedStatement pst = connection.prepareStatement("select * from barcodes where order_id = ?");
		for(int orderId : orderIds) {
			pst.setInt(1, orderId);
			ResultSet rs = pst.executeQuery();
			while(rs.next()){
				Barcode b = new Barcode();
				b.setBarcodeString(rs.getString("barcode_string"));
				b.setOrder(DatabaseManager.getHelper().getOrderDao().queryForId(orderId));
				toUpdate.add(b);
			}
		}
		
		for(BaseDaoEnabled<?,?> obj : DatabaseManager.getHelper().getBarcodeDao().queryForAll())
			obj.delete();
		
		for(CreateOrUpdateable obj : toUpdate)
			obj.createOrUpdate();

		this.publishProgress(0);
	}
	
	/**
	 * Vom Nutzer bestellte Artikel werden aktualisiert.
	 * @throws SQLException
	 */
	private void updateOrderedArticlesForOrderIds(List<Integer> orderIds) throws SQLException {
		
		// Get all old OrderedArticles
		List<OrderedArticle> toDelete = DatabaseManager.getHelper().getOrderedArticleDao().queryForAll();
		
		// Create a list for new OrderedArticles
		List<CreateOrUpdateable> toUpdate = new LinkedList<CreateOrUpdateable>();
		
		PreparedStatement pst = connection.prepareStatement("select * from order_articles where order_id = ?");
		for(int orderId : orderIds){
			pst.setInt(1, orderId);
			ResultSet rs = pst.executeQuery();
			while(rs.next()){//new OrderedArticle();
				// Create an  orderedArticle (also used as example)
				OrderedArticle oa = new OrderedArticle();
				oa.setOrder(DatabaseManager.getHelper().getOrderDao().queryForId(orderId));
				oa.setArticle(DatabaseManager.getHelper().getArticleDao().queryForId(rs.getInt("article_id")));

				// Do an example Query for the already created OrderedArticle
				List<OrderedArticle> oldOAList = DatabaseManager.getHelper().getOrderedArticleDao().queryForMatching(oa);
				if(oldOAList.size() > 0) {
					OrderedArticle oldOA = oldOAList.get(0);
					OrderedArticle deleteThis = null;
					for(OrderedArticle comp : toDelete){
						if(comp.equals(oldOA)) deleteThis = comp;
					}
					toDelete.remove(deleteThis);
					oa = oldOA;
				}
				oa.setAmount(rs.getDouble("amount"));
				oa.setAmountType(rs.getString("amount_type"));
				oa.setPrice((int) (rs.getDouble("price")*100));

				toUpdate.add(oa);
			}
		}

		for(OrderedArticle obj : toDelete){
			Log.i("UpdateDatabase", "OrderedArticle deleted: "+obj);
			obj.delete();
		}
			
		for(CreateOrUpdateable obj : toUpdate)
			obj.createOrUpdate();

		this.publishProgress(0);
	}

	/**
	 * Checkt, ob die vom Nutzer eingegebenen Daten korrrekt sind.
	 * 
	 * @param user
	 * @return user
	 * @throws SQLException
	 */
	public User validateUser(User user) throws SQLException{
		DatabaseConnection con = new DatabaseConnection();
		connection = con.getConnection();

		PreparedStatement pst;
		
		if(user.getLoginName() == null){
			pst = connection.prepareStatement("select * from users where user_id = ?");
			pst.setInt(1, user.getId()); 
		} else {
			pst = connection.prepareStatement("select * from users where loginname = ?");
			pst.setString(1, user.getLoginName()); 
		}
		
		ResultSet rs = pst.executeQuery();
		rs.next();
		
		String passwordFromServer = rs.getString("password_sha256");
		
		if(user.getPasswordSha().equals(passwordFromServer)) {
			user = updateUserForId(rs.getInt("user_id"));
		}
		rs.close();
		pst.close();
		
		return user;
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