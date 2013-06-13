package de.bosshammersch_hof.oekokiste;

import java.io.File;
import java.sql.SQLException;
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
import android.util.Log;
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

public class OrderDetailActivity extends Activity {
	
	private Order order;
	
	/** 
	 *   creates the detail-view of order
	 *   @param Bundle saved Instance State
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_order_detail);
		
		// setup order
		int orderId = getIntent().getIntExtra(Constants.keyOrder, 0);
		order = DatabaseManager.getOrder(orderId);
		
		updateUI();
	}

	private void updateUI() {
		// update UI
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
		        	amountTextView.setText(orderedArticleList.get(position).getAmount()+"");
		        	double price = orderedArticleList.get(position).getTotalPrice();
		        	priceTextView.setText((price/100)+","+(price%100)+"€");
		        
		        	return row;
			}
		};
		
		// creating and filling the sum line
		LayoutInflater inflater = getLayoutInflater();
      	  	View sumRow = inflater.inflate(R.layout.listview_item_order_detail_sum, null);
        
        	int finalPrice = 0;
        	for(OrderedArticle article : orderedArticleList){
        		finalPrice += article.getTotalPrice(); 
        	}
        
        	TextView nameTextView = (TextView) sumRow.findViewById(R.id.nameTextView);
        	TextView finalPriceTextView = (TextView) sumRow.findViewById(R.id.finalPriceTextView);

        	nameTextView.setText("Summe: ");
        	finalPriceTextView.setText((finalPrice/100)+","+(finalPrice%100)+"€");
        
        	orderDetailArticleListView.addFooterView(sumRow);

        	// creating and filling the recipe finder line
        	View recipeFindRow = inflater.inflate(R.layout.listview_item_order_detail_recipe_button, null);
        
        	Button recipeFindButton = (Button) recipeFindRow.findViewById(R.id.recipeFindButton);
        
        	// temporary disbale button
        	recipeFindButton.setEnabled(false);
        	
        	recipeFindButton.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v){
				Intent intent = new Intent(OrderDetailActivity.this, RecipeActivity.class);
				//intent.putExtra(Constants.keyOrderedArticle, orderedArticleList.get(location))
				startActivity(intent);
			}
		});
        
		orderDetailArticleListView.addFooterView(recipeFindRow);

		orderDetailArticleListView.setAdapter(adapter);
		
		orderDetailArticleListView.setOnItemClickListener(new OnItemClickListener(){
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				
				if (arg2 >= order.getArticleList().size()){
					return;
				}

				Intent intent = new Intent(OrderDetailActivity.this,ArticleDetailActivity.class);
				intent.putExtra(Constants.keyOrderedArticle, orderedArticleList.get(arg2).getId());
				startActivity(intent);
			}
		});
		
		TextView orderDateTextView = (TextView) findViewById(R.id.orderDateTextView);
        	SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy", Locale.GERMANY);
		orderDateTextView.setText(dateFormat.format(order.getDate()));
		
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
	 *
	 * @param	view	The clicked view.
	 */
	public void viewBillClicked(View view){

		//String link = "http://vcp-kurhessen.info/wordpress/wp-content/uploads/2011/08/2011-08-09_regionsordnung.pdf";
		
		File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/lieferscheine/1.pdf");
		Uri path = Uri.fromFile(file);
		
        	Intent intent = new Intent(Intent.ACTION_VIEW);
        	intent.setDataAndType(path, "application/pdf");
        	intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        	try {
            		startActivity(intent);
        	} catch (ActivityNotFoundException e) {
        		//Intent webIntent = new Intent(this, WebPDFViewActivity.class);
    			//webIntent.setDataAndType(path, "application/pdf");
    			//startActivity(webIntent);
        		Toast.makeText(OrderDetailActivity.this, 
        			"Auf diesem Gerät ist keine App zum anzeigen von PDF Dokumenten installiert.", 
        			Toast.LENGTH_SHORT).show();
        	}
	}
	/**
	 *   supplies dummy data for testing the app
	 *   @return Order returns a new Order
	 */
	private Order getDummyOrder(){
		
		LinkedList<OrderedArticle> articleList = new LinkedList<OrderedArticle>();
		
		
		Article article1 = new Article(1, "Gouda", "");
		articleList.add(new OrderedArticle(article1, 2.2, "100g", 245));
		Article article2 = new Article(2, "Blattsalat", "");
		articleList.add(new OrderedArticle(article2, 2, "Kopf", 120));
		
		@SuppressWarnings("deprecation")
		Order order = new Order(0, new Date(112, 3, 24), "Beispielkiste");
		
		//order.setArticleCollection(articleList);
		
		return order;
		
	}
}
