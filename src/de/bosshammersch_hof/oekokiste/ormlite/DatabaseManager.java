package de.bosshammersch_hof.oekokiste.ormlite;

import java.sql.SQLException;
import java.util.List;

import de.bosshammersch_hof.oekokiste.Constants;
import de.bosshammersch_hof.oekokiste.model.*;
import android.content.Context;
import android.util.Log;

public class DatabaseManager {

    static private DatabaseManager instance;

    static public void init(Context ctx) {
        if (instance == null) {
            instance = new DatabaseManager(ctx);
        }
    }

    static public DatabaseManager getInstance() {
        return instance;
    }

    private static DatabaseHelper helper;
    
    private DatabaseManager(Context ctx) {
        helper = new DatabaseHelper(ctx);
    }

    public static DatabaseHelper getHelper() {
        return helper;
    }
    
    public static OpenState getLastOpenState() throws SQLException{
    	
		return getHelper().getOpenStateDao().queryForId(1);
	    	
    }
    
    public static User getUser(int id){
    	try {
			return helper.getUserDao().queryForId(id);
		} catch (SQLException e) {
			Log.e(Constants.errorName, "No User could be found.");
			return null;
		}
    }
    
    public static User getUserWithLoginName(String loginname){
    	try {
    		List<User> userList = helper.getUserDao().queryForAll();
    		for(User u : userList)
    			if(u.getLoginName().equals(loginname)) return u;
			return null;
		} catch (SQLException e) {
			Log.e(Constants.errorName, "No User could be found.");
			return null;
		}
    }
    
    public static Order getOrder(int id){
    	
    	try {
			return helper.getOrderDao().queryForId(id);
		} catch (SQLException e) {
			Log.e(Constants.errorName, "No Order could be found.");
			return null;
		}
    	
    }
    
    public static OrderedArticle getOrderedArticle(int id){
    	
    	try {
			return helper.getOrderedArticleDao().queryForId(id);
		} catch (SQLException e) {
			Log.e(Constants.errorName, "No OrderedArticle could be found.");
			return null;
		}
    	
    }

    public static Article getArticle(int id){
    	try {
			return helper.getArticleDao().queryForId(id);
		} catch (SQLException e) {
			Log.e(Constants.errorName, "No Article could be found.");
			return null;
		}
    }


    public static Recipe getRecipe(int id){
    	try {
			return helper.getRecipeDao().queryForId(id);
		} catch (SQLException e) {
			Log.e(Constants.errorName, "No Article could be found.");
			return null;
		}
    }
    
    public static void saveDataFromUser(User user){
    	try {
			helper.getUserDao().createIfNotExists(user);
		} catch (SQLException e) {
			e.printStackTrace();
		}
    	
    }

	public static void saveDataFromRecipe(Recipe recipe) {
		try {
			helper.getRecipeDao().createIfNotExists(recipe);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void saveOpenState(OpenState openState) {
		try {
			helper.getOpenStateDao().createIfNotExists(openState);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void clearUserData() throws SQLException{
		List<User> userList = helper.getUserDao().queryForAll();
		for(User user : userList){
			for(Order order : user.getOrderCollection()){
				for(OrderedArticle oa : order.getArticleCollection()){
					oa.delete();
				}
				for(Barcode b : order.getBarcodeCollection()){
					b.delete();
				}
				order.delete();
			}
			user.delete();
		}
		
	}

    /*public List<WishList> getAllWishLists() {
        List<WishList> wishLists = null;
        try {
            wishLists = getHelper().getWishListDao().queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return wishLists;
    }*/
}