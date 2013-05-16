package de.bosshammersch_hof.oekokiste.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable
public class Article {

	@DatabaseField(generatedId = true)
	private final int id;
	
	@DatabaseField
	private String name;
	
	@DatabaseField
	private String description;
	
	public Article(int id, String name, String description){
		this.id = id;
		this.name = name;
		this.description = description;
		
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getId() {
		return id;
	}
}
