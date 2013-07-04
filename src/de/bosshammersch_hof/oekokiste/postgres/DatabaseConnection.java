package de.bosshammersch_hof.oekokiste.postgres;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import android.os.StrictMode;

/**
 * Standartklasse um zu einer Datenbank zu verbinden.
 *
 */
public class DatabaseConnection {

	private static final String POSTGRES = "org.postgresql.Driver";
	private static final String URL = "jdbc:postgresql://vcp-lumdatal.de:61089";
	private static final String user = "oekokiste"; 
	private static final String password = "testPassword123";
	
	private static boolean isInitialized = false;

	private static void init() throws ClassNotFoundException{
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);
		Class.forName(POSTGRES);
	}
	
	public static Connection getAConnection() throws SQLException, ClassNotFoundException{
		if(!isInitialized) init();
		Connection connection = DriverManager.getConnection(URL, user, password);
		return connection;
	}
	
}
