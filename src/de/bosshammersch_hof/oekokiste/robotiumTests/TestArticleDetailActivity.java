package de.bosshammersch_hof.oekokiste.robotiumTests;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import de.bosshammersch_hof.oekokiste.ArticleDetailActivity;
import de.bosshammersch_hof.oekokiste.MainActivity;
import de.bosshammersch_hof.oekokiste.RecipeActivity;
import de.bosshammersch_hof.oekokiste.postgres.DatabaseConnection;
import android.test.ActivityInstrumentationTestCase2;
import com.jayway.android.robotium.solo.*;

public class TestArticleDetailActivity extends ActivityInstrumentationTestCase2<ArticleDetailActivity>{

	private Solo solo;
	
	public TestArticleDetailActivity(){
		super(ArticleDetailActivity.class);
	}
	
	public void setUp() throws Exception{
		solo = new Solo(getInstrumentation(), getActivity());
	}
	
	public void tearDown() throws Exception{
		solo.finishOpenedActivities();
	}
	
	/**
	 *  Test01: Click home-button to go to the MainActivity 
	 *          (ArticleDetailActivity -> MainActivity) 
	 */
	public void testGotoMainActivity(){
		solo.assertCurrentActivity("This is not the ArticleDetailActivity", ArticleDetailActivity.class);
		solo.clickOnActionBarHomeButton();
		solo.assertCurrentActivity("This is not the MainActivity", MainActivity.class);
	}
	
	/**
	 *  Test02: Click on 'Rezepte...-button' and find recipes for an article
	 */
	public void testRecipeButton(){
		solo.assertCurrentActivity("This is not the ArticleDetailActivity", ArticleDetailActivity.class);
		
		solo.scrollDown();
		solo.scrollUp();
		
		solo.clickOnButton("Rezepte...");
		solo.assertCurrentActivity("This is not the RecipeActivity", RecipeActivity.class);
	}
	
	/**
	 *  Test03: Click on 'Online Shop...-button' to visit the oekokiste-onlinestore
	 */
	public void testGo2OnlineStore(){
		solo.assertCurrentActivity("This is not the ArticleDetailActivity", ArticleDetailActivity.class);
		solo.clickOnButton("Online Shop...");
		// TODO CheckOut onlinestore URL u1 = new URL...
	}
	
	/**
	 *  Test04: Check the new price of the article with the database value
	 */
	@SuppressWarnings("null")
	public void testValidatePricesWithDatabase(){ //TODO
		solo.assertCurrentActivity("This is not the ArticleDetailActivity", ArticleDetailActivity.class);
		
		int crntPrice = 0; 
		ResultSet rs = null;
		DatabaseConnection con = new DatabaseConnection();
		Connection connection = con.getConnection();
		PreparedStatement pstmt = null;
		
		try {
			pstmt = connection.prepareStatement("SELECT price"+
												" FROM order_articles"+
												" WHERE price=?");
			
			pstmt.setInt(1, crntPrice);
			
			while(rs.next()){
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
