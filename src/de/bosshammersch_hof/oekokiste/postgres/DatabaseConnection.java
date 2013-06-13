package de.bosshammersch_hof.oekokiste.postgres;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import android.os.StrictMode;

	public class DatabaseConnection {

	private static final String POSTGRES = "org.postgresql.Driver";

	private static final String URL = "jdbc:postgresql://vcp-lumdatal.de:61089";

	private Connection connection = null;

	private static final String user = "oekokiste"; 

	private static final String password = "testPassword123";
	
	public DatabaseConnection(){
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);
		try {
			Class.forName(POSTGRES);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			System.exit(-1);
		}
	}
	
	public boolean connect() {
		if(connection == null ){
			try {
				connection = DriverManager.getConnection(URL, user, password);
				return true;
			} catch (SQLException e) {
				e.printStackTrace();
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
	
	public Connection getConnection(){
		if(connection == null) this.connect();
		return connection;
	}
	
}
