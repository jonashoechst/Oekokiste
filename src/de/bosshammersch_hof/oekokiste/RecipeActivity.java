package de.bosshammersch_hof.oekokiste;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.LinkedList;

import de.bosshammersch_hof.oekokiste.model.Article;
import de.bosshammersch_hof.oekokiste.model.ArticleGroup;
import de.bosshammersch_hof.oekokiste.model.CookingArticle;
import de.bosshammersch_hof.oekokiste.model.Cookware;
import de.bosshammersch_hof.oekokiste.model.Order;
import de.bosshammersch_hof.oekokiste.model.Recipe;
import de.bosshammersch_hof.oekokiste.model.User;
import de.bosshammersch_hof.oekokiste.ormlite.DatabaseManager;
import de.bosshammersch_hof.oekokiste.postgres.DatabaseConnection;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.database.SQLException;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class RecipeActivity extends Activity implements UpdatableActivity{
	
	LinkedList<Recipe> recipeList;
	Article tmpArticle = null;
	
	/** 
	 *   creats the hole list of recipe
	 *   @param Bundle saved Instance State
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_recipe);
		getActionBar().setHomeButtonEnabled(true);
		setupArticle();
		if(tmpArticle == null)
			recipeList = getRecipeListAll();
		else
			recipeList = getRecipeList(tmpArticle.getId());
		
		updateUi();
		
	}
	
	private void setupArticle() {
		// get User Id and matching User 
		int articleId = getIntent().getIntExtra(Constants.keyOrderedArticle, 1);
		Log.i(OrderActivity.class.getName(), "Article found: "+articleId);
		tmpArticle = DatabaseManager.getArticle(articleId);
	}
	
	public void updateUi() {
		final ListView recipeListView = (ListView) findViewById(R.id.recipeListView);
	
		ListAdapter adapter = new ArrayAdapter<Recipe>(this, R.layout.listview_item_recipe, recipeList){
			
			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				
		        	View row = convertView;
		        
		        	if(row == null){
		        		LayoutInflater inflater = ((Activity) this.getContext()).getLayoutInflater();
		            		row = inflater.inflate(R.layout.listview_item_recipe, parent, false);
		        	}
		        
		        	TextView recipeNameTextView = (TextView) row.findViewById(R.id.recipeName);
		        	recipeNameTextView.setText(recipeList.get(position).getName());
		        
		        	TextView recipeDifficultyTextView = (TextView) row.findViewById(R.id.recipeDifficulty);
		        	recipeDifficultyTextView.setText("" + recipeList.get(position).getDifficulty());
		        
		        	TextView hitRateTextView = (TextView) row.findViewById(R.id.hitRate);
		        	hitRateTextView.setText("");
		       
		        	return row;
		    	}
		};
		
		recipeListView.setAdapter(adapter);
		
		recipeListView.setOnItemClickListener(new OnItemClickListener(){
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				Intent intent = new Intent(RecipeActivity.this, RecipeDetailActivity.class);
				intent.putExtra(Constants.keyRecipe, recipeList.get(arg2).getId());
				startActivity(intent);
			}
		});
	}
	
	/**
	 *   if the app icon in action bar is clicked => go home
	 *   else the super constructor of the function is called
	 *   @param MenuItem which was selected
	 *   @return boolean 
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	        case android.R.id.home:
	            // app icon in action bar clicked; go home
	            Intent intent = new Intent(this, MainActivity.class);
	            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
	            startActivity(intent);
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
	
	/**
	 *   supplies dummy data for testing the app
	 *   @return  LinkedList<Recipe>
	 */
	private LinkedList<Recipe> getDummyRecipeList(){
		
		LinkedList<Recipe> recipeList = new LinkedList<Recipe>();

		recipeList.add(new Recipe(2, "Chili sin carne", "", "", null, null, 3, 10, 30, 4, null, 90));
		recipeList.add(new Recipe(1, "Flammkuchen", "", "", null, null, 1, 45, 30, 5, null, 80));
		recipeList.add(new Recipe(3, "Milchreis", "", "", null, null, 3, 0, 10, 2, null, 77));
		recipeList.add(new Recipe(4, "Gemüse-Lasagne", "", "", null, null, 3, 20, 30, 6, null, 50));
		recipeList.add(new Recipe(5, "Hacksteak ala Patrick", "", "", null, null, 5, 30, 50, 4, null, 45));
		recipeList.add(new Recipe(6, "Death by cheese pizza", "", "", null, null, 3, 20, 50, 1, null, 40));
		recipeList.add(new Recipe(7, "Mitternachtssuppe", "", "", null, null, 3, 30, 20, 10, null, 39));
		recipeList.add(new Recipe(8, "Kandierte Orangenblüten", "", "", null, null, 4, 60, 0, 1, null, 33));
		recipeList.add(new Recipe(9, "Pfannkuchen mit Blattspinatfüllung", "", "", null, null, 1, 20, 15, 4, null,30));
		recipeList.add(new Recipe(10, "Kartoffelnudeln mit Vanillesauce", "", "", null, null, 1, 45, 0, 4, null, 27));
		recipeList.add(new Recipe(11, "Warmer Speckkrautsalat", "", "", null, null, 3, 30, 0, 10, null, 26));
		recipeList.add(new Recipe(12, "Rinderfiletscheiben in Kürbiskernpanier", "", "", null, null, 3, 30, 0, 4, null, 25));
		recipeList.add(new Recipe(13, "Milder Gewürzessig", "", "", null, null, 1, 20, 0, 2, null, 24));
		recipeList.add(new Recipe(14, "Purple Rain", "", "", null, null, 1, 0, 5, 1, null, 20));
		recipeList.add(new Recipe(15, "Sauerkrautsauce mit Speck", "", "", null, null, 1, 20, 25, 4, null, 10));
		recipeList.add(new Recipe(16, "Honig-Senf-Schweinesteaks mit Bourbon Whiskey", "", "", null, null, 1, 8, 10, 4, null, 9));
		recipeList.add(new Recipe(17, "Lisbon", "", "", null, null, 3, 30, 0, 10, null, 5));
		recipeList.add(new Recipe(18, "Grünes Kohlcurry", "", "", null, null, 1, 10, 30, 4, null, 3));
		recipeList.add(new Recipe(19, "Anislikör", "", "", null, null, 1, 0, 10, 2, null, 2));
		recipeList.add(new Recipe(20, "Sangria-Eis-Charlotte", "", "", null, null, 1, 30, 0, 16, null, 1));
		
		return recipeList;
	}
	
	/**
	 *   creats a list of cookingArticle from the extern database 
	 *   @return  LinkedList<CookingArticle>
	 */
	private LinkedList<CookingArticle> getIngredientForRecipe(int id, Connection con){
		LinkedList<CookingArticle> tmpList = new LinkedList<CookingArticle>();
		CookingArticle tmpCA = null;
		try{
			PreparedStatement pst = con.prepareStatement("SELECT * FROM Ingredients " +
				"WHERE  recipe_id = ?");
		pst.setInt(1, id); 

		ResultSet rs = pst.executeQuery();
		while(rs.next()){
			tmpCA = new CookingArticle();
			tmpCA.setAmount(rs.getInt("amount"));
			tmpCA.setAmountType(rs.getString("amount_type"));
			//tmpCA.setIsStandartIngredient(rs.getBoolean("is_standart_ingredient"));
			tmpCA.setArticleGroup(new ArticleGroup(rs.getString("articlegroup_name")));
			tmpCA.create();
		}
			
		
		}catch(Exception e){
			Log.i("RecipeActivity", "SqlQuery for CookingArticle failt");
			e.printStackTrace();
		}
		return tmpList;
	}
	
	/**
	 *   creats a list of Recipes from the extern database 
	 *   @return  LinkedList<CookingArticle>
	 */
	private LinkedList<Recipe> getRecipeList(int articleId){
		Recipe tmpRecipe = null;
		LinkedList<Recipe> tmpList = new LinkedList();
		DatabaseConnection con = new DatabaseConnection();
		
		try{
			Connection connection = null;
			con.connect();
			connection = con.getConnection();
		
		
			PreparedStatement pst = connection.prepareStatement("SELECT * FROM Recipes JOIN Cookware using(recipe_id) JOIN  Ingredients using(recipe_id) "+ 
																"WHERE ArticleGroup_name IN "+
																"(SELECT ArticleGroup_name FROM Article WHERE Article_id = ?)");
			pst.setInt(1, articleId); 
			
			ResultSet rs = pst.executeQuery();
			
			while(rs.next()){
				tmpRecipe = new Recipe();
				tmpRecipe.setName(rs.getString("recipe_name"));
				tmpRecipe.setDifficulty(rs.getInt("recipe_difficulty"));
				tmpRecipe.setId(rs.getInt("recipe_id"));
				tmpRecipe.setDescription(rs.getString("recipe_description"));
				tmpRecipe.setInstructions(rs.getString("recipe_instructions"));
				tmpRecipe.addCookware(new Cookware(rs.getString("cookware_name")));
				tmpRecipe.setServings(rs.getInt("recipe_servings"));
				tmpRecipe.setCookingTimeInMin(rs.getInt("recipe_timeinmin"));
				
				for(CookingArticle tmp : getIngredientForRecipe(rs.getInt("recipe_id"), connection))
					tmpRecipe.addIngredient(tmp);
				
				//tut nicht warum auch immer ?!
				//tmpRecipe.create();
				tmpList.add(tmpRecipe);
				Log.i("RecipeActivity", "SqlQuery done");
			}
			rs.close();
			pst.close();
			
		}catch(Exception e){
			Log.i("RecipeActivity", "SqlQuery failt");
			e.printStackTrace();
		}finally{
			con.disconnect();
		}
		
		
		return tmpList;
	}
	
	
	
	private LinkedList<Recipe> getRecipeListAll(){
		Recipe tmpRecipe = null;
		LinkedList<Recipe> tmpList = new LinkedList();
		DatabaseConnection con = new DatabaseConnection();
		
		try{
			Connection connection = null;
			con.connect();
			connection = con.getConnection();
		
		
			Statement st = connection.createStatement();
			
			ResultSet rs = st.executeQuery("SELECT DISTINCT * FROM Recipes JOIN Cookware using(recipe_id)");
			
			while(rs.next()){
				tmpRecipe = new Recipe();
				tmpRecipe.setName(rs.getString("recipe_name"));
				tmpRecipe.setDifficulty(rs.getInt("recipe_difficulty"));
				tmpRecipe.setId(rs.getInt("recipe_id"));
				tmpRecipe.setDescription(rs.getString("recipe_description"));
				tmpRecipe.setInstructions(rs.getString("recipe_instructions"));
				tmpRecipe.addCookware(new Cookware(rs.getString("cookware_name")));
				tmpRecipe.setServings(rs.getInt("recipe_servings"));
				tmpRecipe.setCookingTimeInMin(rs.getInt("recipe_timeinmin"));
				
				for(CookingArticle tmp : getIngredientForRecipe(rs.getInt("recipe_id"), connection))
					tmpRecipe.addIngredient(tmp);
				
				tmpRecipe.create();
				tmpList.add(tmpRecipe);
				Log.i("RecipeActivity", "SqlQuery RecipeAll done");
			}
			rs.close();
			st.close();
			
		}catch(Exception e){
			Log.i("RecipeActivity", "SqlQuery RecipeAll failt");
			e.printStackTrace();
		}finally{
			con.disconnect();
		}
		
		
		return tmpList;
	}
	
}
