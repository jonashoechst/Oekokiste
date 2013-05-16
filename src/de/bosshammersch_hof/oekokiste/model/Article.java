package de.bosshammersch_hof.oekokiste.model;

public abstract class Article {

	private final int id;
	private String name;
	private String description;
	
	public Article(int id, String name, String description){
		this.id = id;
		this.name = name;
		this.description = description;
		
	}
	
}
