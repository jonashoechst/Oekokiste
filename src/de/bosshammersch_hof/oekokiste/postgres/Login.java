package de.bosshammersch_hof.oekokiste.postgres;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import de.bosshammersch_hof.oekokiste.model.User;

import android.util.Log;

public class Login {
	
	private int userId;
	private String loginname;
	private String password;
	private Connection connection = null;
	private User user = null;

	public Login(int id, String pss){
		this.userId = id;
		this.password = pss;
		this.loginname = null;
	}
	
	public Login(String loginname, String pss){
		this.userId = 0;
		this.loginname = loginname;
		this.password = pss;
	}
	
	public int getUserId(){
		return userId;
	}
	
	public boolean validateUser(){

		PreparedStatement pst;
		ResultSet rs;
		
		try{

			DatabaseConnection con = new DatabaseConnection();
			connection = con.getConnection();
			String passwordInSha;
			
			if(loginname == null){
				pst = connection.prepareStatement("select * from users where user_id = ?");
			
				pst.setInt(1, userId); 
				passwordInSha = password;//getShaPassword(password);

				rs = pst.executeQuery();
				rs.next();
			} else {
				pst = connection.prepareStatement("select * from users where loginname = ?");
				
				pst.setString(1, loginname); 
				passwordInSha = password;//getShaPassword(password);

				rs = pst.executeQuery();
				rs.next();
			}
			
			String passwordFromServer = rs.getString("password_sha256");
			userId = rs.getInt("user_id");
			loginname = rs.getString("loginname");
			
			
			boolean result = false;
			
			if(passwordInSha.equals(passwordFromServer)) {
				user = new User();
				user.setFirstName(rs.getString("firstname"));
				user.setLastName(rs.getString("lastname"));
				user.setLoginName(rs.getString("loginname"));
				user.setId(rs.getInt("user_id"));
				
				result = true;
			}
			
			rs.close();
			pst.close();
			connection.close();
			return result;
			
		}catch(SQLException e){
			Log.e("Login.validateUser()", "Login could not be checked.");
			e.printStackTrace();
		} 
		
		return false;
	}

	private String getShaPassword() throws NoSuchAlgorithmException, UnsupportedEncodingException {
		MessageDigest md = MessageDigest.getInstance("SHA-256");
		md.update(password.getBytes("UTF-8"));
		byte[] digest = md.digest();
		return new String(digest, "UTF-8");
	}

	public String getLoginname() {
		return loginname;
	}

	public String getPassword() {
		return password;
	}

	public User getUser() {
		return user;
	}
	
	 
	
	
}
