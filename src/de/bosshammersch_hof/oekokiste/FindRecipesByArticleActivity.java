package de.bosshammersch_hof.oekokiste;

import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.ListAdapter;
import android.widget.ListView;
import de.bosshammersch_hof.oekokiste.model.ArticleGroup;
import de.bosshammersch_hof.oekokiste.ormlite.DatabaseManager;

public class FindRecipesByArticleActivity extends Activity {

	List<ArticleGroup> articleGroup;
	
	List<ArticleGroup> selectedGroup;
	
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
		
		selectedGroup = new LinkedList<ArticleGroup>();
		
		final ListView findArticleGroupListView = (ListView) findViewById(R.id.findArticleRecipeListView);
		
		final ListAdapter adapter = new ArrayAdapter<ArticleGroup>(this, R.layout.listview_item_find_recipe_article, articleGroup){
			
			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
		       	 	View row = convertView;
		        
		        	if(row == null){
		            	    LayoutInflater inflater = ((Activity) this.getContext()).getLayoutInflater();
		        	    row = inflater.inflate(R.layout.listview_item_find_recipe_article, parent, false);
		        	}
 
		        	CheckedTextView nameTextView = (CheckedTextView) row.findViewById(R.id.itemTextView2);
		        
		        	nameTextView.setText(articleGroup.get(position).getName());
		        
		        	return row;
			}
		};
		
		findArticleGroupListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
		
		findArticleGroupListView.setAdapter(adapter);
		
		findArticleGroupListView.setOnItemClickListener(new OnItemClickListener() {
			   public void onItemClick(AdapterView<?> parent, View view,
			     int position, long id) {
				   if(!selectedGroup.contains(articleGroup.get(position))){
					   selectedGroup.add(articleGroup.get(position));
				   } else {
					   selectedGroup.remove(articleGroup.get(position));
				   }
			   }
		});
		
		Button recipeFindButton = (Button) findViewById(R.id.findButton);
	    
    	recipeFindButton.setOnClickListener(new OnClickListener(){
    		@Override
    		public void onClick(View v){
    			
    			showDialog(0);
    			
    			/*Intent intent = new Intent(FindRecipesByArticleActivity.this, RecipeActivity.class);
    			
    			String[] articleGroupNameArray = new String[selectedGroup.size()];
    			for(int i = 0; i < selectedGroup.size(); i++){
    				articleGroupNameArray[i] = selectedGroup.get(i).getName();
    			}
    			
    			intent.putExtra(Constants.keyArticleGroupNameArray, articleGroupNameArray);
    			startActivity(intent);*/
    		}
    	});
		
		getActionBar().setHomeButtonEnabled(true);
	}
	
	protected Dialog onCreateDialog(int id){
		return new AlertDialog.Builder(FindRecipesByArticleActivity.this)
		.setTitle("Bitte wählen Sie, wonach Sie suchen möchten.")
		.setItems(R.array.select_dialog_items, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				Intent intent = new Intent(FindRecipesByArticleActivity.this, RecipeActivity.class);
	    			
				String[] articleGroupNameArray = new String[selectedGroup.size()];
				for(int i = 0; i < selectedGroup.size(); i++){
					articleGroupNameArray[i] = selectedGroup.get(i).getName();
				}
	    			
	    		intent.putExtra(Constants.keyArticleGroupNameArray, articleGroupNameArray);
	    		startActivity(intent);
			}
		})
		.create();
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
