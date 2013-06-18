package de.bosshammersch_hof.oekokiste;

import java.io.File;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;


import de.bosshammersch_hof.oekokiste.model.*;
import de.bosshammersch_hof.oekokiste.ormlite.*;

public class FindArticleActivity extends Activity{
	
	ArticleGroup articleGroup;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_find_article);
		
		String articleGroupId = getIntent().getStringExtra(Constants.keyArticleGroupId);
		try {
			articleGroup = DatabaseManager.getHelper().getArticleGroupDao().queryForId(articleGroupId);
		} catch (SQLException e) {
			articleGroup = null;
			e.printStackTrace();
		}
		
		updateUi();
	}

	public void updateUi() {
		// update UI
		ListView findArticleListView = (ListView) findViewById(R.id.findArticleListView);
		
		final List<Article> findArticleList = articleGroup.getArticleList();
		
		ListAdapter adapter = new ArrayAdapter<Article>(this, R.layout.listview_item_find_article, findArticleList){
			
			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
		       	 	View row = convertView;
		        
		        	if(row == null){
		            	    LayoutInflater inflater = ((Activity) this.getContext()).getLayoutInflater();
		        	    row = inflater.inflate(R.layout.listview_item_find_article, parent, false);
		        	}
 
		        	TextView nameTextView = (TextView) row.findViewById(R.id.itemTextView);
		        
		        	nameTextView.setText(findArticleList.get(position).getName());
		        
		        	return row;
			}
		};
		
		findArticleListView.setAdapter(adapter);
		
		/*findArticleListView.setOnItemClickListener(new OnItemClickListener(){
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				
				if (arg2 >= order.getArticleList().size()){
					return;
				}

				Intent intent = new Intent(OrderDetailActivity.this,ArticleDetailActivity.class);
				intent.putExtra(Constants.keyOrderedArticle, orderedArticleList.get(arg2).getId());
				startActivity(intent);
			}
		});*/
		
		getActionBar().setHomeButtonEnabled(true);
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
}
