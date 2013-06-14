package de.bosshammersch_hof.oekokiste.ormlite;

import de.bosshammersch_hof.oekokiste.model.*;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

public class DatabaseHelper extends OrmLiteSqliteOpenHelper {
    // name of the database file for your application -- change to something appropriate for your app
    private static final String DATABASE_NAME = "Oekokiste21.sqlite";

    // any time you make changes to your database objects, you may have to increase the database version
    private static final int DATABASE_VERSION = 1;

    // the DAO object we use to access the SimpleData table
    private Dao<User, Integer> userDao = null;
    private Dao<Order, Integer> orderDao = null;
    private Dao<Barcode, String> barcodeDao = null;
    private Dao<OrderedArticle, Integer> orderedArticleDao = null;
    private Dao<Article, Integer> articleDao = null;
    private Dao<CookingArticle, Integer> cookingArticleDao = null;
    private Dao<ArticleGroup, String> articleGroupDao = null;
    private Dao<Category, String> categoryDao = null;
    private Dao<Recipe, Integer> recipeDao = null;
    private Dao<OpenState, Integer> openStateDao = null;
    private Dao<Cookware, Integer> cookwareDao = null;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database,ConnectionSource connectionSource) {
        try {
            TableUtils.createTable(connectionSource, Article.class);
            TableUtils.createTable(connectionSource, ArticleGroup.class);
            TableUtils.createTable(connectionSource, Barcode.class);
            TableUtils.createTable(connectionSource, Category.class);
            TableUtils.createTable(connectionSource, CookingArticle.class);
            TableUtils.createTable(connectionSource, OpenState.class);
            TableUtils.createTable(connectionSource, Order.class);
            TableUtils.createTable(connectionSource, OrderedArticle.class);
            TableUtils.createTable(connectionSource, Recipe.class);
            TableUtils.createTable(connectionSource, User.class);
            TableUtils.createTable(connectionSource, Cookware.class);
        } catch (SQLException e) {
            Log.e(DatabaseHelper.class.getName(), "Can't create database", e);
            throw new RuntimeException(e);
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }
        
    }

    @Override
    public void onUpgrade(SQLiteDatabase db,ConnectionSource connectionSource, int oldVersion, int newVersion) {
        try {
            List<String> allSql = new ArrayList<String>(); 
            switch(oldVersion) 
            {
              case 1: 
                  //allSql.add("alter table AdData add column `new_col` VARCHAR");
                  //allSql.add("alter table AdData add column `new_col2` VARCHAR");
            }
            for (String sql : allSql) {
                db.execSQL(sql);
            }
        } catch (SQLException e) {
            Log.e(DatabaseHelper.class.getName(), "exception during onUpgrade", e);
            throw new RuntimeException(e);
        }
        
    }

    public Dao<Article, Integer> getArticleDao() {
        if (null == articleDao) {
            try {
            	articleDao = getDao(Article.class);
            }catch (java.sql.SQLException e) {
                e.printStackTrace();
            }
        }
        return articleDao;
    }

    public Dao<CookingArticle, Integer> getCookingArticleDao() {
        if (null == cookingArticleDao) {
            try {
            	cookingArticleDao = getDao(CookingArticle.class);
            }catch (java.sql.SQLException e) {
                e.printStackTrace();
            }
        }
        return cookingArticleDao;
    }

	public Dao<User, Integer> getUserDao() {
		if (null == userDao) {
            try {
            	userDao = getDao(User.class);
            }catch (java.sql.SQLException e) {
                e.printStackTrace();
            }
        }
		return userDao;
	}

	public Dao<Order, Integer> getOrderDao() {
		if (null == orderDao) {
            try {
            	orderDao = getDao(Order.class);
            }catch (java.sql.SQLException e) {
                e.printStackTrace();
            }
        }
		return orderDao;
	}

	public Dao<Barcode, String> getBarcodeDao() {
		if (null == barcodeDao) {
            try {
            	barcodeDao = getDao(Barcode.class);
            }catch (java.sql.SQLException e) {
                e.printStackTrace();
            }
        }
		return barcodeDao;
	}

	public Dao<OrderedArticle, Integer> getOrderedArticleDao() {
		if (null == orderedArticleDao) {
            try {
            	orderedArticleDao = getDao(OrderedArticle.class);
            }catch (java.sql.SQLException e) {
                e.printStackTrace();
            }
        }
		return orderedArticleDao;
	}

	public Dao<ArticleGroup, String> getArticleGroupDao() {
		if (null == articleGroupDao) {
            try {
            	articleGroupDao = getDao(ArticleGroup.class);
            }catch (java.sql.SQLException e) {
                e.printStackTrace();
            }
        }
		return articleGroupDao;
	}

	public Dao<Category, String> getCategoryDao() {
		if (null == categoryDao) {
            try {
            	categoryDao = getDao(Category.class);
            }catch (java.sql.SQLException e) {
                e.printStackTrace();
            }
        }
		return categoryDao;
	}

	public Dao<Recipe, Integer> getRecipeDao() {
		if (null == recipeDao) {
            try {
            	recipeDao = getDao(Recipe.class);
            }catch (java.sql.SQLException e) {
                e.printStackTrace();
            }
        }
		return recipeDao;
	}

	public Dao<OpenState, Integer> getOpenStateDao() {
		if (null == openStateDao) {
            try {
            	openStateDao = getDao(OpenState.class);
            }catch (java.sql.SQLException e) {
                e.printStackTrace();
            }
        }
		return openStateDao;
	}

	public Dao<Cookware, Integer> getCookwareDao() {
		if (null == cookwareDao) {
            try {
            	cookwareDao = getDao(Cookware.class);
            }catch (java.sql.SQLException e) {
                e.printStackTrace();
            }
        }
		return cookwareDao;
	}

}