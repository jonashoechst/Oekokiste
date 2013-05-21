package de.bosshammersch_hof.oekokiste.model;

public class CookingArticle extends Article {
	
	// ahlbeschreibt die anz
	private int count;

	public CookingArticle(int id, String name, String description, int count) {
		super(id, name, description);
		this.count = count;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}
}
