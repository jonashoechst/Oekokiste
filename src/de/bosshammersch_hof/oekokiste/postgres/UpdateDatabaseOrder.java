package de.bosshammersch_hof.oekokiste.postgres;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import com.j256.ormlite.dao.Dao;
import de.bosshammersch_hof.oekokiste.Constants;
import de.bosshammersch_hof.oekokiste.model.*;
import de.bosshammersch_hof.oekokiste.ormlite.DatabaseManager;
import android.os.AsyncTask;
import android.util.Log;

public class UpdateDatabaseOrder extends AsyncTask<String, Integer, boolean[]> {
	
	private Connection connection = new DatabaseConnection().getConnection();
	
	Dao<Article, Integer> articleDao = DatabaseManager.getHelper().getArticleDao();
	
	private boolean[] output;

	@Override
	public boolean[] doInBackground(String... params) {
		
		DatabaseConnection con = new DatabaseConnection();
		connection = con.getConnection();
		
		try {
			updateBarcodeForBarcodeStringGetOrderId(params[0]);
			Log.i("UpdataDatabaseOrder", "Order data was synced.");
		} catch (SQLException e) {
			Log.e("UpdataDatabaseOrder", "Could not sync order data.");
			e.printStackTrace();
		}

		con.disconnect();
		
		return output;
	}
	
	

	/**
	 * Barcodes werden aktualisiert.
	 * @throws SQLException
	 */
	public int updateBarcodeForBarcodeStringGetOrderId(String barcodeString) throws SQLException {

		Log.i("Order Update", "string"+barcodeString);
		
		PreparedStatement pst = connection.prepareStatement("select * from barcodes where barcode_string = ?");
		pst.setString(1, barcodeString);
		ResultSet rs = pst.executeQuery();
		rs.next();
		
		int orderId = rs.getInt("order_id");

		Log.i("Order Update", "Order id: "+orderId);
		Barcode b = new Barcode();
		b.setBarcodeString(rs.getString("barcode_string"));
		this.updateOrderForOrderId(orderId);
		b.setOrder(DatabaseManager.getHelper().getOrderDao().queryForId(orderId));
		
		b.createOrUpdate();
		
		rs.close();
		pst.close();

		this.publishProgress(0);
		
		return orderId;
	}
	
	/**
	 * Nutzerbestellungen werden aktualisiert.
	 * @throws SQLException
	 */
	private void updateOrderForOrderId(int orderId) throws SQLException {
		
		PreparedStatement pst = connection.prepareStatement("select * from orders where order_id = ?");
		pst.setInt(1, orderId);
		ResultSet rs = pst.executeQuery();
		rs.next();
		
		Order o = new Order();
		
		o.setDate(rs.getDate("order_date"));
		o.setId(rs.getInt("order_id"));
		o.setName(rs.getString("order_name"));
		o.createOrUpdate();
		
		rs.close();
		pst.close();
		
		updateOrderedArticlesForOrderId(orderId);
		
		this.publishProgress(0);
	}
	
	
	/**
	 * Vom Nutzer bestellte Artikel werden aktualisiert.
	 * @throws SQLException
	 */
	private void updateOrderedArticlesForOrderId(int orderId) throws SQLException {
		
		PreparedStatement pst = connection.prepareStatement("select * from order_articles where order_id = ?");
		pst.setInt(1, orderId);
		ResultSet rs = pst.executeQuery();
		while(rs.next()){
			OrderedArticle oa = new OrderedArticle();
			oa.setOrder(DatabaseManager.getHelper().getOrderDao().queryForId(orderId));
			oa.setArticle(DatabaseManager.getHelper().getArticleDao().queryForId(rs.getInt("article_id")));
			List<OrderedArticle> oas = DatabaseManager.getHelper().getOrderedArticleDao().queryForMatching(oa);
			if(oas.size() > 0) oa = oas.get(0);
			oa.setAmount(rs.getDouble("amount"));
			oa.setAmountType(rs.getString("amount_type"));
			double price = rs.getDouble("price");
			oa.setPrice((int) (price*100));
			oa.createOrUpdate();
			
		}
			
		rs.close();
		pst.close();
		
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