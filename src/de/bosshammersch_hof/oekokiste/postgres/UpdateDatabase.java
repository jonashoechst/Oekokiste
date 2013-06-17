package de.bosshammersch_hof.oekokiste.postgres;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import java.util.Vector;

import com.j256.ormlite.dao.Dao;

import de.bosshammersch_hof.oekokiste.model.*;
import de.bosshammersch_hof.oekokiste.ormlite.DatabaseManager;
import android.os.AsyncTask;
import android.util.Log;

public class UpdateDatabase extends AsyncTask<Login, Integer, boolean[]> {
	
	private Connection connection = null;
	
	Dao<Article, Integer> articleDao = DatabaseManager.getHelper().getArticleDao();
	
	private boolean[] output;

	@Override
	public boolean[] doInBackground(Login... params) {
		
		DatabaseConnection con = new DatabaseConnection();
		con.connect();
		connection = con.getConnection();
		
		try {
			update();
			Log.i("UpdataDatabase", "General data was synced.");
		} catch (SQLException e) {
			Log.e("UpdateDatabase", "Could not sync general Data.");
			e.printStackTrace();
		}
		if(params[0].validateUser()){
			Log.i("FillDatabase", "User was validated!");
			try {
				updateUserForId(params[0].getUserId());
				Log.i("UpdataDatabase", "User data was synced.");
			} catch (SQLException e) {
				Log.e("UpdateDatabase", "Could not sync user data.");
				e.printStackTrace();
			}
			
		}
		
		con.disconnect();
		return output;
	}
	
	private void update() throws SQLException{
		updateArticles();
		updateCategories();
		updateArticleGroups();
		updateRecipes();
	}
	
	private void updateArticles() throws SQLException {
		PreparedStatement pst = connection.prepareStatement("select * from article");
		ResultSet rs = pst.executeQuery();
		while(rs.next()){
			Article a = null;
			try {
				a = DatabaseManager.getHelper().getArticleDao().queryForId(rs.getInt("article_id"));
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				if (a == null) a = new Article();
			}
			// TODO Get a description from anywhere
			a.setDescription("");
			a.setId(rs.getInt("article_id"));
			a.setName(rs.getString("article_name"));
			a.setOrigin(rs.getString("article_origin"));
			a.createOrUpdate();
		}
	}
	
	private void updateCategories() throws SQLException{
		
		PreparedStatement pst = connection.prepareStatement("select * from category");
		ResultSet rs = pst.executeQuery();
		while(rs.next()){
			Category c = null;
			try {
				c = DatabaseManager.getHelper().getCategoryDao().queryForId(rs.getString("category_name"));
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				if (c == null) c = new Category();
				else c.getArticles().clear();
			}
			c.setName(rs.getString("category_name"));
			
			PreparedStatement pst2 = connection.prepareStatement("select * from article where category_name = ?");
			pst2.setString(1, c.getName());
			ResultSet rs2 = pst2.executeQuery();
			while (rs2.next()){
				int article_id = rs2.getInt("article_id");
				Article a = DatabaseManager.getHelper().getArticleDao().queryForId(article_id);
				a.setCategory(c);
				a.createOrUpdate();
			}
			c.createOrUpdate();
		}
	}
	
	private void updateUserForId(int id) throws SQLException{
		
		User user = null;
		
		try {
			user = DatabaseManager.getHelper().getUserDao().queryForId(id);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (user == null) user = new User();
		}

		PreparedStatement ps = connection.prepareStatement("select * from users where user_id = ?");
		ps.setInt(1, id);
		ResultSet rs = ps.executeQuery();
		rs.next();
		
		user.setFirstName(rs.getString("firstname"));
		user.setLastName(rs.getString("lastname"));
		user.setLoginName(rs.getString("loginname"));
		user.setId(rs.getInt("user_id"));
		
		updateOrdersForUser(user);
		
		user.createOrUpdate();
		
	}
	
	private void updateOrdersForUser(User user) throws SQLException {
		
		user.getOrderCollection().clear();
		
		PreparedStatement pst = connection.prepareStatement("select * from orders where user_id = ?");
		pst.setInt(1, user.getId());
		ResultSet rs = pst.executeQuery();
		while(rs.next()){
			Order o = null;
			try {
				o = DatabaseManager.getHelper().getOrderDao().queryForId(rs.getInt("order_id"));
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				if (o == null) o = new Order();
			}
			o.setDate(rs.getDate("order_date"));
			o.setId(rs.getInt("order_id"));
			o.setName(rs.getString("order_name"));

			updateBarcodesForOrder(o);
			
			updateOrderedArticlesForOrder(o);
			
			o.setUser(user);
			o.createOrUpdate();
		}
	}
	
	private void updateOrderedArticlesForOrder(Order order) throws SQLException{

		order.getArticleCollection().clear();
		
		OrderedArticle exampleOrderedArticle = new OrderedArticle();
		exampleOrderedArticle.setOrder(order);
		
		for(OrderedArticle oa : DatabaseManager.getHelper().getOrderedArticleDao().queryForMatchingArgs(exampleOrderedArticle))
			oa.delete();
		
		PreparedStatement pst = connection.prepareStatement("select * from order_articles where order_id = ?");
		pst.setInt(1, order.getId());
		ResultSet rs = pst.executeQuery();
		while(rs.next()){
			
			OrderedArticle oa = new OrderedArticle();
			oa.setAmount(rs.getDouble("amount"));
			oa.setAmountType(rs.getString("amount_type"));
			
			Article article = DatabaseManager.getHelper().getArticleDao().queryForId(rs.getInt("article_id"));
			oa.setArticle(article);
			double price = rs.getDouble("price")*100;
			oa.setPrice((int) price);
			oa.setOrder(order);
			
			oa.createOrUpdate();
		}
		
	}

	private void updateBarcodesForOrder(Order order) throws SQLException {
		
		order.getBarcodeCollection().clear();
		
		PreparedStatement pst = connection.prepareStatement("select * from barcodes where order_id = ?");
		pst.setInt(1, order.getId());
		ResultSet rs = pst.executeQuery();
		while(rs.next()){
			Barcode b = null;
			try {
				b = DatabaseManager.getHelper().getBarcodeDao().queryForId(rs.getString("barcode_string"));
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				if (b == null) b = new Barcode();
			}

			b.setBarcodeString(rs.getString("barcode_string"));
			b.setOrder(order);

			b.createOrUpdate();
		}
	}
	
	private void updateRecipes() throws SQLException{
		
		PreparedStatement pst = connection.prepareStatement("select * from recipes");
		ResultSet rs = pst.executeQuery();
		while(rs.next()){
			Recipe r = null;
			try {
				r = DatabaseManager.getHelper().getRecipeDao().queryForId(rs.getInt("recipe_id"));
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				if (r == null ) r = new Recipe();
			}
			r.setName(rs.getString("recipe_name"));
			r.setInstructions(rs.getString("recipe_instructions"));
			r.setDescription(rs.getString("recipe_desc"));
			r.setDifficulty(rs.getInt("recipe_difficulty"));
			r.setId(rs.getInt("recipe_id"));
			r.setCookingTimeInMin(rs.getInt("recipe_timeinmin"));
			updateCookwareForRecipe(r);
			updateCookingArticleForRecipe(r);
			r.createOrUpdate();
		}
	}
	
	private void updateCookingArticleForRecipe(Recipe r) throws SQLException {
		
		r.getIngredients().clear();
		
		CookingArticle ex = new CookingArticle();
		ex.setRecipe(r);
		List<CookingArticle> caList = DatabaseManager.getHelper().getCookingArticleDao().queryForMatching(ex);
		
		for(CookingArticle ca : caList)
			ca.delete();
		
		PreparedStatement pst = connection.prepareStatement("select * from ingredients where recipe_id = ?");
		pst.setInt(1, r.getId());
		ResultSet rs = pst.executeQuery();
		while (rs.next()){
			CookingArticle ca = new CookingArticle();
			ca.setAmount(rs.getDouble("amount"));
			ca.setAmountType(rs.getString("amount_type"));
			ca.setPrimaryIngredient(rs.getBoolean("is_primary_ingredient"));
			ca.setStandartIngredient(rs.getBoolean("is_standart_ingredient"));
			ca.setArticleGroup(DatabaseManager.getHelper().getArticleGroupDao().queryForId(rs.getString("articlegroup_name")));
			ca.setRecipe(r);
			ca.createOrUpdate();
		}
		
	}
	
	private void updateArticleGroups() throws SQLException{
		
		PreparedStatement pst = connection.prepareStatement("select * from articlegroup");
		ResultSet rs = pst.executeQuery();
		while (rs.next()){
			ArticleGroup ag = null;
			try{
				ag = DatabaseManager.getHelper().getArticleGroupDao().queryForId(rs.getString("articlegroup_name"));
			} catch (SQLException e){
				e.printStackTrace();
			} finally{
				if (ag == null) ag = new ArticleGroup();
			}
			ag.setName(rs.getString("articlegroup_name"));
			
			updateArticlesForArticleGroup(ag);
			ag.createOrUpdate();
		}
	}

	private void updateArticlesForArticleGroup(ArticleGroup ag) throws SQLException {

		PreparedStatement pst = connection.prepareStatement("select * from article where articlegroup_name = ?");
		pst.setString(1, ag.getName());
		ResultSet rs = pst.executeQuery();
		
		while(rs.next()){
			Article article = DatabaseManager.getHelper().getArticleDao().queryForId(rs.getInt("article_id"));
			article.setArticleGroup(ag);
			article.createOrUpdate();
		}
		
	}

	private void updateCookwareForRecipe(Recipe r) throws SQLException{

		Cookware ex = new Cookware();
		ex.setRecipe(r);
		List<Cookware> cookware = DatabaseManager.getHelper().getCookwareDao().queryForMatching(ex);
		
		for(Cookware c : cookware)
			c.delete();
		
		PreparedStatement pst = connection.prepareStatement("select * from cookware where recipe_id = ?");
		pst.setInt(1, r.getId());
		ResultSet rs = pst.executeQuery();
		
		while(rs.next()){
			Cookware c = new Cookware();
			c.setName(rs.getString("cookware_name"));
			c.setRecipe(r);
			c.createOrUpdate();
		}
	}

}