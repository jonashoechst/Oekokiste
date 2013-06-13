package de.bosshammersch_hof.oekokiste.postgres;

public class Login {
	
	private final int userId;
	private final String password;
	
	public Login(int id, String pss){
		this.userId = id;
		this.password = pss;
	}
	
	public int getUserId(){
		return userId;
	}
	
}
