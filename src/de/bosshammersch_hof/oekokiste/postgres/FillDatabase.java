package de.bosshammersch_hof.postgres;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;

import de.bosshammersch_hof.oekokiste.model.Order;
import de.bosshammersch_hof.oekokiste.model.User;
import android.os.AsyncTask;

public class FillDatabase extends AsyncTask<Integer, Integer, boolean[]> {

	public static final String POSTGRES = "org.postgresql.Driver";

	public static final String URL = "vcp-lumdatal.de:61089";

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
	
	public boolean connect() {
		if(connection == null ){
			try {
				connection = DriverManager.getConnection(URL, user, password);
				return true;
			} catch (SQLException e) {
				
			}
		}
		return false;
	}
	
	public void disconnect() {
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
	
	
	public boolean createUser(int userId){
		
		User user = null;
		//public User(int id, String lastName, String firstName, LinkedList<Order> orderList) 
		try{
			PreparedStatement stmt = null;
			stmt = connection.prepareStatement("SELECT *"+
											   " FROM users"+
											   " WHERE user_id = ?");
			stmt.setInt(1, userId); 
		
			ResultSet rs = stmt.executeQuery();
			
			user = new User(userId ,rs.getString("lastname") ,rs.getString("firstname") ,rs.getString("lastname") ,rs.getString("lastname") );
		}
		catch(SQLException e){
			e.printStackTrace();
		}
		return true;
	}
	
}