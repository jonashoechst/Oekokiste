package de.bosshammersch_hof.oekokiste.ormlite;

import java.sql.SQLException;
import java.util.List;

import de.bosshammersch_hof.oekokiste.model.*;
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

    public static DatabaseHelper getHelper() {
        return helper;
    }
    
    /**
     * Wenn sich der User abmeldet, werden alle Daten des Users gelöscht (damit künftige User keine Informationen des alten Users sehen.
     * 
     * @throws SQLException
     */
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
}