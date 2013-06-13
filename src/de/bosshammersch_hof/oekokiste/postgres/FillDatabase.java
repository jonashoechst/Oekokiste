package de.bosshammersch_hof.oekokiste.postgres;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import de.bosshammersch_hof.oekokiste.model.Article;
import de.bosshammersch_hof.oekokiste.model.ArticleGroup;
import de.bosshammersch_hof.oekokiste.model.Barcode;
import de.bosshammersch_hof.oekokiste.model.Category;
import de.bosshammersch_hof.oekokiste.model.Order;
import de.bosshammersch_hof.oekokiste.model.OrderedArticle;
import de.bosshammersch_hof.oekokiste.model.User;
import android.os.AsyncTask;
import android.util.Log;

public class FillDatabase extends AsyncTask<Login, Integer, boolean[]> {
	
	private Connection connection = null;
	
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
			
		return order;
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
		orderedArticle.setPrice((int) rs.getDouble("price"));
		orderedArticle.setArticle(getArticle(rs.getInt("article_id")));
		
		return orderedArticle;
	}
	
	private Article getArticle(int articleId) throws SQLException{
		
		Article article = new Article();
		PreparedStatement pst = connection.prepareStatement("select * from article where article_id = ?");
		pst.setInt(1, articleId);
		
		ResultSet rs = pst.executeQuery();
		rs.next();
		
		article.setId(articleId);
		article.setDescription("");
		article.setName(rs.getString("article_name"));
		article.setOrigin(rs.getString("article_origin"));
		
		rs.close();
		pst.close();
		
		return article;
	}
	
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
		
		for(ArticleGroup ag : getArticleGroups(category.getName()))
			category.addArticleGroup(ag);
		return category;
	}
	
	private List<ArticleGroup> getArticleGroups(String categoryName) throws SQLException{
		List<ArticleGroup> articleGroupList = new LinkedList<ArticleGroup>();
		
		PreparedStatement pst = connection.prepareStatement("select * from articlegroup where category_name = ?");
		pst.setString(1, categoryName);
		
		ResultSet rs = pst.executeQuery();
		while(rs.next())
			articleGroupList.add(getArticleGroupFromResultSet(rs));

		rs.close();
		pst.close();
		
		return articleGroupList;
	}

	private ArticleGroup getArticleGroupFromResultSet(ResultSet rs) throws SQLException {
		ArticleGroup ag = new ArticleGroup();
		
		ag.setName(rs.getString("articlegroup_name"));
		for(Article a : getArticlesForArticleGroupName(ag.getName()))
			ag.addArticle(a);
		
		return ag;
	}
	
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
	}
	
	private Article getArticleFromResultSet(ResultSet rs) throws SQLException{
		Article article = new Article();
		
		article.setId(rs.getInt("article_id"));
		article.setName(rs.getString("article_name"));
		article.setOrigin(rs.getString("article_origin"));
		
		return article;
	}
	
	
	/*
	private LinkedList<Order> createOrderList(int userId){
		LinkedList<Order> output = null;
		
		try{
			PreparedStatement ordersStatement = null;
			PreparedStatement barcodeStatement = null;
			PreparedStatement articelStatement = null;
			PreparedStatement articleGroupStatement = null;
			PreparedStatement categoryStatement = null;
			PreparedStatement orderedArticelStatement = null;
			
			ordersStatement = connection.prepareStatement("SELECT *"+
											   			 " FROM Orders"+
											   			 " WHERE user_id = ?");
			ordersStatement.setInt(1, userId); 
			
			barcodeStatement = connection.prepareStatement("SELECT *"+
											   			 " FROM Barcodes"+
											   			 " WHERE order_id = ?");
			
			articelStatement = connection.prepareStatement("SELECT *"+
											   			 " FROM Articel"+
											   			 " WHERE user_id = ?");
			
			articleGroupStatement = connection.prepareStatement("SELECT * " +
																"FROM Articlegroup " +
																"WHERE articlegroup_name = ?");
			
			categoryStatement = connection.prepareStatement("SELECT * " +
															"FROM Category " +
															"WHERE articlegroup_name = ?");
			
			orderedArticelStatement = connection.prepareStatement("SELECT * " +
																  "FROM Ordered_Articles " +
															      "WHERE order_id = ?");
			
			ResultSet ordersRS = ordersStatement.executeQuery();
			
			Order tmpOrder = null;
			Barcode tmpBarcode = null;
			Article tmpArticle = null;
			OrderedArticle tmpOrderedArticle = null;
			LinkedList<Barcode> tmpBarcodeList = null;
			LinkedList<OrderedArticle> tmpOrderedArticleList = null;
			
			
			while(ordersRS.next()){
				tmpOrder = new Order() ;
				//ordersRS.getInt("user_id") gibt es das nicht mehr???
				tmpOrder.setId(ordersRS.getInt("order_id"));
				tmpOrder.setName(ordersRS.getString("order_name"));
				tmpOrder.setDate(ordersRS.getDate("order_date"));
				
				barcodeStatement.setInt(1, ordersRS.getInt("order_id"));
				articelStatement.setInt(1, ordersRS.getInt("order_id"));
				orderedArticelStatement.setInt(1, ordersRS.getInt("order_id"));
				
				ResultSet barcodeRS = barcodeStatement.executeQuery();
				ResultSet articleRS = articelStatement.executeQuery();
				ResultSet orderedArticleRS = articelStatement.executeQuery();
				
				//build list of barcodes per order
				while(barcodeRS.next()){
					tmpBarcode = new Barcode(barcodeRS.getString("barcode_string"));
					tmpBarcodeList.add(tmpBarcode);
				}// done
				
				//build list of articels per order
				while(articleRS.next()){
					tmpOrderedArticle = new OrderedArticle();
					tmpOrderedArticle.setAmount(articleRS.getInt("amount"));
					tmpOrderedArticle.setAmountType(articleRS.getString("amount_type"));
					//tmpOrderedArticle.setId(articleRS.getInt("")); brauchen wir das wirklich nicht mehr ??
					tmpOrderedArticle.setPrice(orderedArticleRS.getInt("price"));
					
					tmpArticle = new Article();
					tmpArticle.setId(articleRS.getInt("article_id"));
					tmpArticle.setName(articleRS.getString("article_name"));
					tmpArticle.setDescription(articleRS.getString("article_description"));// hei§t das so?? ist im ER-Modell nicht enthalten
					tmpArticle.setOrigin(articleRS.getString("article_origin"));
					// Article bis auf ArticleGroup fertig zusammen gebaut...
					
					articleGroupStatement.setString(1, articleRS.getString("article_name")); 
					ResultSet articleGroupRS = articleGroupStatement.executeQuery();
					ArticleGroup tmpArticleGroup = new ArticleGroup();
					tmpArticleGroup.setName(articleGroupRS.getString("articlegroup_name"));
					//ArticleGroup bis auf Category zusammen gebaut...
					
					categoryStatement.setString(1, articleGroupRS.getString("articlegroup_name"));
					ResultSet categoryRS = categoryStatement.executeQuery();
					Category tmpCategory = new Category(categoryRS.getString("category_name"));
					//Category bis auf einer Liste von ArticleGroups zusammen gebaut...
					
					//Bis jetzt nicht in Category nicht implementiert
					//LinkedList<ArticleGroup> tmpArticleGroupList = buildArticleGroupListForArticelGroup(tmpArticleGroup.getName());
					
					//alles wieder zusammenbauen!!
					tmpArticleGroup.setCategory(tmpCategory);
					tmpArticle.setArticleGroup(tmpArticleGroup);
					tmpOrderedArticle.setArticle(tmpArticle);
					tmpOrderedArticleList.add(tmpOrderedArticle);
				}
			
			for(OrderedArticle articel : tmpOrderedArticleList)
				tmpOrder.addOrderedArticle(articel);
			for(Barcode barcode : tmpBarcodeList)
				tmpOrder.addBarcode(barcode);
			output.add(tmpOrder);
			}
			return output;
		}
		catch(SQLException e){
			e.printStackTrace();
		}
		return output;
		
	}*/
	
}