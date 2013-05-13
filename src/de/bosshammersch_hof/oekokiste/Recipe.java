package de.bosshammersch_hof.oekokiste;

public class Recipe {
	
	String name;
	int hitRate;
	
	public Recipe (String inName, int inHitRate){
		this.name = inName;
		this.hitRate = inHitRate;
	}
	
	public String getName(){
		return this.name;
	}
	
	public int getHitRate(){
		return this.hitRate;
	}
}
