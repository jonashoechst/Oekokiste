package de.bosshammersch_hof.oekokiste.ormlite;

import android.content.Context;

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