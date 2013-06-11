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

    private static DatabaseHelper getHelper() {
        return helper;
    }
    
    public static OpenState getLastOpenState(){
    	
    	// get the last State of the app
    	
    	OpenState lastOpenState = null;
		try {
			List<OpenState> stateList = getHelper().getOpenStateDao().queryForAll();
	    	
	    	for(OpenState state : stateList){
	    		if(lastOpenState == null) lastOpenState = state;
	    		else if(lastOpenState.getId() < state.getId()) lastOpenState = state;
	    		
	    	}
		} catch (SQLException e) {
			Log.e(Constants.errorName, "No OpenState could be found.");
		}
    	
    	return lastOpenState;
    }
    
    public static User getUser(int id){
    	try {
			return helper.getUserDao().queryForId(id);
		} catch (SQLException e) {
			Log.e(Constants.errorName, "No User could be found.");
			return null;
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