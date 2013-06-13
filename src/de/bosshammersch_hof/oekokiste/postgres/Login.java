package de.bosshammersch_hof.oekokiste.postgres;

import java.security.MessageDigest;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Login {
	
	private final int userId;
	private final String password;
	private Connection connection = null;
	
	public Login(int id, String pss){
		this.userId = id;
		this.password = pss;
	}
	
	public int getUserId(){
		return userId;
	}
	
	public boolean validateUser(){
		
		ConnectToDatabase con = new ConnectToDatabase();
		con.connect();
		connection = con.getConnection();
		int result = 0;

		try{
			PreparedStatement stmt = null;
			stmt = connection.prepareStatement("SELECT COUNT(*)"+
											   " FROM users"+
											   " WHERE user_id = ?" +
											   " AND password_sha256 = ?");
			stmt.setInt(1, userId); 
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			md.update(password.getBytes("UTF-8"));
			byte[] digest = md.digest();
			String passwordSha = new String(digest, "UTF-8");
			stmt.setString(2, passwordSha);
			ResultSet rs = stmt.executeQuery();
			rs.next();
			result = rs.getInt(1);
		}catch(Exception e){
			e.printStackTrace();
		}
		if(result != 1)
			return false;
		else
			return true;
	}
}
