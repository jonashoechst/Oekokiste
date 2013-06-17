package de.bosshammersch_hof.oekokiste.postgres;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import com.j256.ormlite.dao.Dao;

import de.bosshammersch_hof.oekokiste.model.*;
import de.bosshammersch_hof.oekokiste.ormlite.DatabaseManager;
import android.os.AsyncTask;
import android.util.Log;

public class FillDatabase extends AsyncTask<Login, Integer, boolean[]> {
	
	private Connection connection = null;
	
	Dao<Article, Integer> articleDao = DatabaseManager.getHelper().getArticleDao();
	
	private boolean[] output;

	@Override
	public boolean[] doInBackground(Login... params) {
		
		DatabaseConnection con = new DatabaseConnection();
		con.connect();
		connection = con.getConnection();
		if(params[0].validateUser()){
			Log.i("FillDatabase", "User was validated!");
			
			try {
				User user = getUserForUserId(params[0].getUserId());
				user.create();
			} catch (SQLException e) {
				Log.e("FillDatabase","Error: User could not be created!");
				e.printStackTrace();
			}
			
			try {
				List<Category> categories = getCategories();
				for(Category c : categories)
					c.create();
			} catch (SQLException e) {
				Log.e("FillDatabase","Error: Categories could not be created!");
				e.printStackTrace();
			}
			
		}
		
		con.disconnect();
		return output;
	}

	
	private User getUserForUserId(int userId) throws SQLException{
		PreparedStatement pst = connection.prepareStatement("select * from users where user_id = ?");
		pst.setInt(1, userId); 
		
		ResultSet rs = pst.executeQuery();
		rs.next();
		
		User user = new User();
		user.setId(userId);
		user.setLastName(rs.getString("lastname"));
		user.setFirstName(rs.getString("firstname"));
		user.setLoginName(rs.getString("loginname"));
		for(Order order : getOrdersForUserId(userId))
			user.addOrder(order);

		rs.close();
		pst.close();
		
		return user;
	}
	
	private List<Order> getOrdersForUserId(int userId) throws SQLException{

		List<Order> orderList = new LinkedList<Order>();
		
		PreparedStatement pst = connection.prepareStatement("select * from orders where user_id = ?");
		pst.setInt(1, userId);
		ResultSet rs = pst.executeQuery();
	
		while(rs.next())
			orderList.add(getOrderFromResultSet(rs));
		
		rs.close();
		pst.close();
			
		return orderList;
	}
	
	private Order getOrderFromResultSet(ResultSet rs) throws SQLException{
		
		Order order = new Order();
		order.setId( rs.getInt("order_id"));
		order.setName(rs.getString("order_name"));
		order.setDate(rs.getDate("order_date"));
		for(OrderedArticle oa : getOrderedArticlesForOrderId(order.getId()))
			order.addOrderedArticle(oa);
		for(Barcode b : getBarcodesForOrderId(order.getId()))
			order.addBarcode(b);
			
		return order;
	}
	
	private List<Barcode> getBarcodesForOrderId(int id) throws SQLException {
		
		LinkedList<Barcode> barcodeList = new LinkedList<Barcode>(); 
		
		PreparedStatement pst = connection.prepareStatement("select * from barcodes where order_id = ?");
		pst.setInt(1, id);
		
		ResultSet rs = pst.executeQuery();
		
		while(rs.next())
			barcodeList.add(getBarcodeFromResultSet(rs));
		
		return barcodeList;
	}


	private Barcode getBarcodeFromResultSet(ResultSet rs) throws SQLException {
		
		Barcode barcode = new Barcode();
		
		barcode.setBarcodeString(rs.getString("barcode_string"));
		
		return barcode;
	}


	private List<OrderedArticle> getOrderedArticlesForOrderId(int orderId) throws SQLException{
		List<OrderedArticle> orderedArticleList = new LinkedList<OrderedArticle>();
		
		PreparedStatement pst = connection.prepareStatement("select * from order_articles where order_id = ?");
		pst.setInt(1, orderId);
		ResultSet rs = pst.executeQuery();
		while(rs.next())
			orderedArticleList.add(getOrderedArticleFromResultSet(rs));

		rs.close();
		pst.close();
		
		return orderedArticleList;
	}
	
	private OrderedArticle getOrderedArticleFromResultSet(ResultSet rs) throws SQLException{
		OrderedArticle orderedArticle = new OrderedArticle();

		orderedArticle.setAmount(rs.getDouble("amount"));
		orderedArticle.setAmountType(rs.getString("amount_type"));
		double price = rs.getDouble("price")*100;
		orderedArticle.setPrice((int) price);
		orderedArticle.setArticle(getArticleWithId(rs.getInt("article_id")));
		
		
		
		return orderedArticle;
	}
	/*
	private Article getArticle(int articleId) throws SQLException{
		
		Article article = new Article();
		PreparedStatement pst = connection.prepareStatement("select * from article where article_id = ?");
		pst.setInt(1, articleId);
		
		ResultSet rs = pst.executeQuery();
		rs.next();
		
		article.setId(articleId);
		// TODO get a description of the article
		article.setDescription("");
		article.setName(rs.getString("article_name"));
		article.setOrigin(rs.getString("article_origin"));
		
		rs.close();
		pst.close();
		
		return article;
	}*/
	
	private List<Category> getCategories() throws SQLException{
		
		List<Category> categoryList = new LinkedList<Category>();
 		
		PreparedStatement pst = connection.prepareStatement("select * from category");
		ResultSet rs = pst.executeQuery();
		
		while(rs.next())
			categoryList.add(getCategoryFromResultSet(rs));
		
		rs.close();
		pst.close();
		return categoryList;
	}
	
	private Category getCategoryFromResultSet(ResultSet rs) throws SQLException{
		Category category = new Category();
		category.setName(rs.getString("category_name"));
		
		for(Article a : getArticlesForCategoryName(category.getName()))
			category.addArticle(a);
		return category;
	}
	
	private List<Article> getArticlesForCategoryName(String categoryName) throws SQLException{
		List<Article> articleList = new LinkedList<Article>();
		
		PreparedStatement pst = connection.prepareStatement("select article_id from article where category_name = ?");
		pst.setString(1, categoryName);
		
		ResultSet rs = pst.executeQuery();
		while(rs.next())
			articleList.add(getArticleWithId(rs.getInt("article_id")));

		rs.close();
		pst.close();
		
		return articleList;
	}
/*
	private ArticleGroup getArticleFromResultSet(ResultSet rs) throws SQLException {
		ArticleGroup ag = new ArticleGroup();
		
		ag.setName(rs.getString("articlegroup_name"));
		for(Article a : getArticlesForArticleGroupName(ag.getName()))
			ag.addArticle(a);
		
		return ag;
	}*/
	/*
	private List<Article> getArticlesForArticleGroupName(String articleGroupName) throws SQLException{
		
		List<Article> articleList = new LinkedList<Article>();
		
		PreparedStatement pst = connection.prepareStatement("select * from article where articlegroup_name = ?");
		pst.setString(1, articleGroupName);
		
		ResultSet rs = pst.executeQuery();
		while(rs.next())
			articleList.add(getArticleFromResultSet(rs));
		
		rs.close();
		pst.close();
		
		return articleList;		
	}*/
	
	private Article getArticleWithId(int id){
		Article article = null;
		try {
			article = articleDao.queryForId(id);
		} catch (SQLException e) {
			// Article not created yet.
			PreparedStatement pst;
			try {
				pst = connection.prepareStatement("select * from article where article_id = ?");
				pst.setInt(1, id);
				ResultSet rs = pst.executeQuery();
				rs.next();
				article = createArticleFromResultSet(rs);
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		}
		
		return article;
	}

	private Article createArticleFromResultSet(ResultSet rs) throws SQLException{
		Article article = new Article();
		
		article.setId(rs.getInt("article_id"));
		article.setName(rs.getString("article_name"));
		article.setOrigin(rs.getString("article_origin"));
		
		return article;
	}
}