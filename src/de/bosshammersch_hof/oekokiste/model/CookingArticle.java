package de.bosshammersch_hof.oekokiste.model;

import java.sql.SQLException;
import java.text.DecimalFormat;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.misc.BaseDaoEnabled;
import com.j256.ormlite.table.DatabaseTable;

import de.bosshammersch_hof.oekokiste.ormlite.DatabaseManager;

@DatabaseTable
public class CookingArticle extends BaseDaoEnabled<CookingArticle, Integer> implements CreateOrUpdateable{

	
	@DatabaseField(generatedId = true)
	private int generatedId;

	@DatabaseField(foreign = true,foreignAutoCreate = true,foreignAutoRefresh = true)
	private ArticleGroup articleGroup;
	
	@DatabaseField
	private double amount;
	
	@DatabaseField
	private String amountType;
	
	@DatabaseField
	private boolean standartIngredient;
	
	@DatabaseField
	private boolean primaryIngredient;
	
	@DatabaseField(foreign = true,foreignAutoCreate = true,foreignAutoRefresh = true)
	private Recipe recipe;

	public CookingArticle(){
		this.setDao(DatabaseManager.getHelper().getCookingArticleDao());
	}
	
	public void createOrUpdate() throws SQLException{
		DatabaseManager.getHelper().getCookingArticleDao().createOrUpdate(this);
	}

	public double getAmount() {
		return amount;
	}

	public String getAmountString() {
		DecimalFormat df = new DecimalFormat("#0.##");
		return df.format(getAmount());
	}

	public String getAmountStringMul(double mul) {
		DecimalFormat df = new DecimalFormat("#0.##");
		return df.format(getAmount()*mul);
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

	public boolean isStandartIngredient() {
		return standartIngredient;
	}

	public void setStandartIngredient(boolean in) {
		this.standartIngredient = in;
	}

	public ArticleGroup getArticleGroup() {
		return articleGroup;
	}

	public void setArticleGroup(ArticleGroup articleGroup) {
		this.articleGroup = articleGroup;
	}

	public Recipe getRecipe() {
		return recipe;
	}

	public void setRecipe(Recipe recipe) {
		this.recipe = recipe;
	}

	public boolean isPrimaryIngredient() {
		return primaryIngredient;
	}

	public void setPrimaryIngredient(boolean primaryIngredient) {
		this.primaryIngredient = primaryIngredient;
	}
	
	@Override
	public boolean equals(Object o){
		CookingArticle ca = (CookingArticle) o;
		return ca.getArticleGroup().getName() == this.getArticleGroup().getName() && ca.getRecipe().getId() == this.getRecipe().getId();
	}
	
}
