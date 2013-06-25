package de.bosshammersch_hof.oekokiste;

import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import de.bosshammersch_hof.oekokiste.model.Article;
import de.bosshammersch_hof.oekokiste.model.ArticleGroup;
import de.bosshammersch_hof.oekokiste.ormlite.DatabaseManager;

public class FindRecipesByArticleActivity extends Activity {

	List<ArticleGroup> articleGroup;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_find_recipe_article);
		
		try {
			articleGroup = DatabaseManager.getHelper().getArticleGroupDao().queryForAll();
		} catch (SQLException e) {
			articleGroup = null;
			e.printStackTrace();
		}
		
		updateUi();
	}

	/**
	 * Aktualisiert die UI.
	 */
	public void updateUi() {
		// update UI
		final ListView findArticleListView = (ListView) findViewById(R.id.findArticleRecipeListView);
		
		final List<Article> findArticleList = new LinkedList<Article>();
		
		for(int i = 0; i < articleGroup.size(); i++){
			for(int j = 0; j < articleGroup.get(i).getArticleList().size(); j++){
				findArticleList.add(articleGroup.get(i).getArticleList().get(j));
			}
		}
		
		final ListAdapter adapter = new ArrayAdapter<Article>(this, R.layout.listview_item_find_recipe_article, findArticleList){
			
			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
		       	 	View row = convertView;
		        
		        	if(row == null){
		            	    LayoutInflater inflater = ((Activity) this.getContext()).getLayoutInflater();
		        	    row = inflater.inflate(R.layout.listview_item_find_recipe_article, parent, false);
		        	}
 
		        	TextView nameTextView = (TextView) row.findViewById(R.id.itemTextView2);
		        
		        	nameTextView.setText(findArticleList.get(position).getName());
		        
		        	return row;
			}
		};
		
		findArticleListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
		
		findArticleListView.setAdapter(adapter);
		
		findArticleListView.setOnItemClickListener(new OnItemClickListener() {
			   public void onItemClick(AdapterView<?> parent, View view,
			     int position, long id) {
				   findArticleListView.setItemChecked(position, true);
			   }
		});
		
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
