package de.bosshammersch_hof.oekokiste.model;

import java.util.LinkedList;
import java.util.List;

import com.j256.ormlite.table.DatabaseTable;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;

@DatabaseTable
public class Category {

	@DatabaseField(id = true)
	private String name;
	
	@ForeignCollectionField(eager = false)
	private List<Article> articleGroups;
	
	private Category(){
		articleGroups = new LinkedList<Article>();
	}
	
	public Category(String name){
		this();
		this.name = name;
	}
	
}
