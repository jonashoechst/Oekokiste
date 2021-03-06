package de.bosshammersch_hof.oekokiste;

import java.sql.SQLException;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
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

public class FindArticleActivity extends Activity implements RefreshableActivity{
	
	ArticleGroup articleGroup;
	
	/**  set the right view for the content.
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_find_article);
	}
	
	/**  if the app resume to this activity the methode refeshes the data for this activity.
	 *   calls the refreshData()
	 */
	@Override
	public void onResume(){
		super.onResume();
		Constants.refreshableActivity = this;
		refreshData();
	}

	/**  get extradata from the intent and query the database for the data the article.
	 * 	 if there is no article a message is shown
	 */
	@Override
	public void refreshData() {

		String articleGroupName = getIntent().getStringExtra(Constants.keyArticleGroupName);
		try {
			articleGroup = DatabaseManager.getHelper().getArticleGroupDao().queryForId(articleGroupName);
		} catch (SQLException e) {
			articleGroup = null;
		}
		
		if(articleGroup == null){
			AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(this);
			dlgAlert.setMessage("Die Ansicht konnte nicht geladen werden.");
			dlgAlert.setTitle("Ökokiste");
			dlgAlert.setPositiveButton("Zurück", 
				new DialogInterface.OnClickListener() {
		        	public void onClick(DialogInterface dialog, int which) {
		        		finish();
		        	}
				});
			dlgAlert.setCancelable(false);
			dlgAlert.create().show();
		}
		
		updateUi();
	}

	/**  refreshed the UI.
	 * 	 the methode shows a message if the article is not in the online-shop.
	 */
	public void updateUi() {
		ListView findArticleListView = (ListView) findViewById(R.id.findArticleListView);
		
		final List<Article> findArticleList = articleGroup.getArticleList();
		
		if(findArticleList.isEmpty()){
			AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(this);
			dlgAlert.setMessage("Keine Artikel gefunden.");
			dlgAlert.setTitle("Ökokiste");
			dlgAlert.setPositiveButton("Zurück", 
				new DialogInterface.OnClickListener() {
		        	public void onClick(DialogInterface dialog, int which) {
		        		finish();
		        	}
				}
			);
			dlgAlert.setCancelable(true);
			dlgAlert.create().show();
		}
		
		ListAdapter adapter = new ArrayAdapter<Article>(this, R.layout.listview_item_find_article, findArticleList){
			
			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
		       	 	View row = convertView;
		        
		        	if(row == null){
		        		LayoutInflater inflater = ((Activity) this.getContext()).getLayoutInflater();
		        	    row = inflater.inflate(R.layout.listview_item_find_article, parent, false);
		        	}
 
		        	TextView nameTextView = (TextView) row.findViewById(R.id.itemTextView);
		        
		        	nameTextView.setText(findArticleList.get(position).getName()+" ("+findArticleList.get(position).getOrigin()+")");
		        
		        	return row;
			}
		};
		
		findArticleListView.setAdapter(adapter);
		
		findArticleListView.setOnItemClickListener(new OnItemClickListener(){
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				Intent intent = new Intent(FindArticleActivity.this, ArticleDetailActivity.class);
				intent.putExtra(Constants.keyArticleId, findArticleList.get(arg2).getId());
				startActivity(intent);
			}
		});
		
		getActionBar().setHomeButtonEnabled(true);
	}
	
	/**  if the app icon in action bar is clicked => go home
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
