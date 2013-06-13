package de.bosshammersch_hof.oekokiste.postgres;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;

import de.bosshammersch_hof.oekokiste.model.Article;
import de.bosshammersch_hof.oekokiste.model.ArticleGroup;
import de.bosshammersch_hof.oekokiste.model.Barcode;
import de.bosshammersch_hof.oekokiste.model.Category;
import de.bosshammersch_hof.oekokiste.model.Order;
import de.bosshammersch_hof.oekokiste.model.OrderedArticle;
import de.bosshammersch_hof.oekokiste.model.User;
import android.os.AsyncTask;

public class FillDatabase extends AsyncTask<Integer, Integer, boolean[]> {

	private static final String POSTGRES = "org.postgresql.Driver";

	private static final String URL = "vcp-lumdatal.de:61089";

	private Connection connection = null;

	private static final String user = "oekokiste"; 

	private static final String password = "testPassword123";
	
	private boolean[] output;

	@Override
	protected boolean[] doInBackground(Integer... params) {
		try {
			Class.forName(POSTGRES);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			System.exit(-1);
		}
		connect();
		
		output[0] = createUser(params[0]);

		return output;
	}
	
	private boolean connect() {
		if(connection == null ){
			try {
				connection = DriverManager.getConnection(URL, user, password);
				return true;
			} catch (SQLException e) {
				
			}
		}
		return false;
	}
	
	private void disconnect() {
		if (connection != null){
			try {
				if (!connection.isClosed()) {
					connection.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		connection = null;
	}
	
	
	private boolean createUser(int userId){
		
		User user = null;
		
		try{
			PreparedStatement stmt = null;
			stmt = connection.prepareStatement("SELECT *"+
											   " FROM users"+
											   " WHERE user_id = ?");
			stmt.setInt(1, userId); 
		
			ResultSet rs = stmt.executeQuery();
			
			LinkedList<Order> orderList = createOrderList(userId);
			
			user = new User();
			user.setId(userId);
			user.setLastName(rs.getString("lastname"));
			user.setFirstName(rs.getString("firstname"));
			user.setLoginName(rs.getString("loginname"));
			user.setOrderList(orderList);
		}
		catch(SQLException e){
			e.printStackTrace();
		}
		return true;
	}
	
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
											   			 " FROM Barcode"+
											   			 " WHERE user_id = ?");
			
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
			tmpOrder.setArticleList(tmpOrderedArticleList);
			tmpOrder.setBarcodeList(tmpBarcodeList);
			output.add(tmpOrder);
			}
			return output;
		}
		catch(SQLException e){
			e.printStackTrace();
		}
		return output;
		
	}
	
}