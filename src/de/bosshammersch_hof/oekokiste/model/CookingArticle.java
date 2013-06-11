package de.bosshammersch_hof.oekokiste.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable
public class CookingArticle {

	
	@DatabaseField(generatedId = true)
	private int generatedId;
	
	@DatabaseField(foreign = true,foreignAutoCreate = true,foreignAutoRefresh = true)
	private Article article;

	@DatabaseField(foreign = true,foreignAutoCreate = true,foreignAutoRefresh = true)
	private ArticleGroup articleGroup;
	
	@DatabaseField
	private double amount;
	
	@DatabaseField
	private String amountType;
	
	@DatabaseField
	private boolean isStandartIngredient;

	private CookingArticle(){
		
	}
	
	public CookingArticle(Article article, double amount, String amountType, boolean isStandartIngredient) {
		this();
		this.article = article;
		this.amountType = amountType;
		this.amount = amount;
		this.isStandartIngredient = isStandartIngredient;
	}
	
	public Article getArticle() {
		return article;
	}

	public void setArticle(Article article) {
		this.article = article;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public String getAmountType() {
		return amountType;
	}

	public void setAmountType(String amountType) {
		this.amountType = amountType;
	}

	public boolean getIsStandartIngredient() {
		return isStandartIngredient;
	}

	public void setIsStandartIngredient(boolean isStandartIngredient) {
		this.isStandartIngredient = isStandartIngredient;
	}

	public ArticleGroup getArticleGroup() {
		return articleGroup;
	}

	public void setArticleGroup(ArticleGroup articleGroup) {
		this.articleGroup = articleGroup;
	}
	
	
	
}
