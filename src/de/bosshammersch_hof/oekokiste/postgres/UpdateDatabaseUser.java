package de.bosshammersch_hof.oekokiste.postgres;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.misc.BaseDaoEnabled;
import de.bosshammersch_hof.oekokiste.Constants;
import de.bosshammersch_hof.oekokiste.model.*;
import de.bosshammersch_hof.oekokiste.ormlite.DatabaseManager;
import android.os.AsyncTask;
import android.util.Log;

public class UpdateDatabaseUser extends AsyncTask<User, Integer, boolean[]> {
	
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
		
		try {
			updateUserDataForId(params[0].getId());
			Log.i("UpdataDatabase", "User data was synced.");
		} catch (SQLException e) {
			Log.e("UpdateDatabase", "Could not sync user data.");
			e.printStackTrace();
		}

		con.disconnect();
		
		return output;
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
}