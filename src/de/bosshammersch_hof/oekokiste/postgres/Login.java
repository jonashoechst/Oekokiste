package de.bosshammersch_hof.oekokiste.postgres;

import java.security.MessageDigest;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import android.util.Log;

public class Login {
	
	private int userId;
	private String password;
	private Connection connection = null;
	
	public Login(int id, String pss){
		this.userId = id;
		this.password = pss;
	}
	
	public int getUserId(){
		return userId;
	}
	
	public boolean validateUser(){
		try{

			DatabaseConnection con = new DatabaseConnection();
			
			connection = con.getConnection();
			
			PreparedStatement pst = connection.prepareStatement("select * from users where user_id = ?");
			
			pst.setInt(1, userId); 
			String passwordInSha = password;
			/*
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			md.update(password.getBytes("UTF-8"));
			byte[] digest = md.digest();
			//String passwordSha = new String(digest, "UTF-8");*/

			ResultSet rs = pst.executeQuery();
			rs.next();
			
			String passwordFromServer = rs.getString("password_sha256");
			
			rs.close();
			pst.close();
			connection.close();
			
			if(passwordInSha.equals(passwordFromServer)) return true;
			
		}catch(SQLException e){
			Log.e("Login.validateUser()", "Login could not be checked.");
			e.printStackTrace();
		}
		
		return false;
	}
	
}
