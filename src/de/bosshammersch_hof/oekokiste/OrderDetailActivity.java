package de.bosshammersch_hof.oekokiste;

import java.io.File;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
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

public class OrderDetailActivity extends Activity implements RefreshableActivity{
	
	private View sumRow;
	private View recipeFindRow;
	
	private File billFile;
	
	private Order order;
	
	/** 
	 *   creates the detail-view of order
	 *   @param Bundle saved Instance State
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_order_detail);
		
	}
	
	/**
	 *  call the super constructor and call refreshData();
	 */
	@Override
	protected void onResume() {
		super.onResume();
		Constants.refreshableActivity = this;
		refreshData();
	}

	/**
	 * Daten werden aktualisiert.
	 */
	@Override
	public void refreshData() {
		int orderId = getIntent().getIntExtra(Constants.keyOrderId, 0);
		
		try {
			order = DatabaseManager.getHelper().getOrderDao().queryForId(orderId);
		} catch (SQLException e) {
			order = null;
		}
		
		if(order == null){
			// Print an Error message
			AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(this);
			dlgAlert.setMessage("Die Ansicht konnte nicht geladen werden.");
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
		
		File folder = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "oekokiste");
        folder.mkdir();
        
        billFile = new File(folder, "oekokiste-rechnung-"+order.getId()+".pdf");
		
		updateUi();
	}
	
	/**
	 *  update the Ui 
	 */
	public void updateUi() {
		ListView orderDetailArticleListView = (ListView) findViewById(R.id.orderDetailArticleListView);
		
		final List<OrderedArticle> orderedArticleList = order.getArticleList();
		
		ListAdapter adapter = new ArrayAdapter<OrderedArticle>(this, R.layout.listview_item_order_detail, orderedArticleList){
			
			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
		       	 	View row = convertView;
		        
		        	if(row == null){
		            	    LayoutInflater inflater = ((Activity) this.getContext()).getLayoutInflater();
		        	    row = inflater.inflate(R.layout.listview_item_order_detail, parent, false);
		        	}
 
		        	TextView nameTextView = (TextView) row.findViewById(R.id.nameTextView);
		        	TextView amountTextView = (TextView) row.findViewById(R.id.amountTextView);
		        	TextView priceTextView = (TextView) row.findViewById(R.id.priceTextView);
		        
		        	nameTextView.setText(orderedArticleList.get(position).getArticle().getName());
		    		DecimalFormat df = new DecimalFormat("##0.##");
		    		df.setDecimalSeparatorAlwaysShown(false);
		        	amountTextView.setText(df.format(orderedArticleList.get(position).getAmount())+" "+orderedArticleList.get(position).getAmountType());
		        	priceTextView.setText(orderedArticleList.get(position).getPriceString());
		        
		        	return row;
			}
		};
		
		// creating and filling the sum line
		LayoutInflater inflater = getLayoutInflater();
		if(sumRow == null){
			sumRow = inflater.inflate(R.layout.listview_item_order_detail_sum, null);
	    	orderDetailArticleListView.addFooterView(sumRow);
		}
        
    	TextView nameTextView = (TextView) sumRow.findViewById(R.id.nameTextView);
    	TextView finalPriceTextView = (TextView) sumRow.findViewById(R.id.finalPriceTextView);

    	nameTextView.setText("Summe: ");
    	finalPriceTextView.setText(order.getTotalOrderValueString());

    	// creating and filling the recipe finder line
    	if(recipeFindRow == null){
    		recipeFindRow = inflater.inflate(R.layout.listview_item_order_detail_recipe_button, null);
    		orderDetailArticleListView.addFooterView(recipeFindRow);
    	}
    	
    	Button recipeFindButton = (Button) recipeFindRow.findViewById(R.id.recipeFindButton);
    
    	recipeFindButton.setOnClickListener(new OnClickListener(){
    		@Override
    		public void onClick(View v){
    			Intent intent = new Intent(OrderDetailActivity.this, RecipeActivity.class);
    			
    			List<ArticleGroup> articleGroups = new LinkedList<ArticleGroup>();
    			for(OrderedArticle oa : order.getArticleCollection())
    				articleGroups.add(oa.getArticle().getArticleGroup());
    			
    			List<Recipe.RecipeWithHits> recipesWithHits;
				try {
					recipesWithHits = Recipe.findRecipesByArticleGroups(articleGroups, false);
				} catch (SQLException e) {
					recipesWithHits = null;
				}
				
				if(recipesWithHits == null)
					recipesWithHits = new LinkedList<Recipe.RecipeWithHits>();
				
    			intent.putExtra(Constants.keyRecipeIdArray, Recipe.RecipeWithHits.getRecipeIdArray(recipesWithHits));
    			intent.putExtra(Constants.keyRecipeHitsArray, Recipe.RecipeWithHits.getHitsArray(recipesWithHits));
    			
    			startActivity(intent);
    		}
    	});

		orderDetailArticleListView.setAdapter(adapter);
		
		orderDetailArticleListView.setOnItemClickListener(new OnItemClickListener(){
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				
				if (arg2 >= order.getArticleList().size()){
					return;
				}

				Intent intent = new Intent(OrderDetailActivity.this,ArticleDetailActivity.class);
				intent.putExtra(Constants.keyOrderId, order.getId());
				intent.putExtra(Constants.keyArticleId, orderedArticleList.get(arg2).getArticle().getId());
				startActivity(intent);
			}
		});
		
		TextView orderDateTextView = (TextView) findViewById(R.id.orderDateTextView);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy", Locale.GERMANY);
		orderDateTextView.setText(dateFormat.format(order.getDate()));
		
		Button billButton = (Button) findViewById(R.id.billButton);
		if(billFile.exists()) billButton.setText("Rechnung anzeigen");
		else billButton.setText("Rechnung speichern");
		
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
	
	/**
	 * Sends an intent to read .pdf-files. 
	 * @param	view	The clicked view.
	 */
	public void viewBillClicked(View view){
		
		String link = Constants.pathToBill+order.getId()+".pdf";
		
		Uri path = Uri.fromFile(billFile);
		
		DownloadManager.Request request = new DownloadManager.Request(Uri.parse(link));
		request.setDescription("Rechnung vom "+order.getDate().toString());
		request.setTitle("Ökokiste Rechnung ("+order.getId()+")");
		
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
		    request.allowScanningByMediaScanner();
		    request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
		}
		
		if(!billFile.exists()){	
			request.setDestinationUri(path);
			// get download service and enqueue file
			DownloadManager manager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
			manager.enqueue(request);
			Toast.makeText(OrderDetailActivity.this, 
	    			"Rechung wird gespeichert...", 
	    			Toast.LENGTH_LONG).show();
			Button billButton = (Button) findViewById(R.id.billButton);
			billButton.setText("Rechnung anzeigen");
		} else {
			try {
	    		Intent intent = new Intent(Intent.ACTION_VIEW);
	    		intent.setDataAndType(path, "application/pdf");
	    		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	    		startActivity(intent);
	    	} catch (ActivityNotFoundException e) {
	    		e.printStackTrace();
	    		Toast.makeText(OrderDetailActivity.this, 
	    			"Auf diesem Gerät ist keine App zum anzeigen von PDF Dokumenten installiert.", 
	    			Toast.LENGTH_SHORT).show();
	    	} 
		}
	}

}
