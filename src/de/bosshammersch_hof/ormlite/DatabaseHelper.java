package de.bosshammersch_hof.ormlite;

import java.sql.SQLException;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;
import de.bosshammersch_hof.oekokiste.Order;
import de.bosshammersch_hof.oekokiste.R;
import de.bosshammersch_hof.oekokiste.model.*;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

public class DatabaseHelper extends OrmLiteSqliteOpenHelper{

	private static final String DATABASE_NAME = "oekokiste.db";
	private static final int DATABASE_VERSION = 6;
	
	public DatabaseHelper(Context context){
		super(context, DATABASE_NAME, null, DATABASE_VERSION, R.raw.ormlite_config);
	}
	
	@Override
	public void onCreate(SQLiteDatabase sqliteDatabase, ConnectionSource connectionSource) {
		try{
			TableUtils.createTable(connectionSource, User.class);
			TableUtils.createTable(connectionSource, OrderedArticle.class);
			TableUtils.createTable(connectionSource, Order.class);
		} catch (SQLException e){
			Log.e(DatabaseHelper.class.getName(), "Unable to create Database.", e);
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase sqliteDatabase, ConnectionSource connectionSource, int oldVer, int newVer) {
		
	}

}
